package com.example.ann;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ApplicationContext {
	
	private Logger log = Logger.getLogger(ApplicationContext.class);
	
	private List<BeanPojo> list = new ArrayList<BeanPojo>();
	
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public ApplicationContext(String name) {
		
		log.info("开始初始化上下文对象.......");
		//读取配置文件
		readXml(name);
		//实例化bean
		instanceBean();
		//注解扫描
		scanBean();
		log.info("初始化上下文对象完毕.......");
	}
	
	private void scanBean() {
		log.info("注解扫描");
		for(String key : map.keySet()) {
			Object obj = map.get(key);
			if(obj != null) {
				filedAnnotation(obj);
				methodAnnotation(obj);
			}
		}
	}
	
	
	/**
	 * 读取配置文件
	 * @param filename 文件名
	 */
	@SuppressWarnings({ "unchecked" })
	private void readXml(String filename) {
		
		Document document = null;
		SAXReader reader = new SAXReader();
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream in = classLoader.getResourceAsStream(filename);
		try {
			log.info("读取配置文件：" + filename);
			document = reader.read(in);
			//获取根目录
			Element rootElement = document.getRootElement();
			log.info(rootElement.getName());
			
			if(!"beans".equals(rootElement.getName())) {
				throw new XmlElementDefineException("顶级元素错误:" + rootElement.getName() + ",顶级元素必须为beans");
			}
			
			List<Element> elements = rootElement.elements();
			for (Element element : elements) {
				
				//log.info(element.getText());
				if(element.getName().equals("bean")){
					log.info("字标签元素：" + element.getName());
					//获取标签属性
					BeanPojo pojo = new BeanPojo();
					String id = element.attributeValue("id");
					String classname = element.attributeValue("class");
					log.info("解析标签class属性：" + classname);
					if("".equals(id)){
						pojo.setId(classname.substring(classname.lastIndexOf(".") + 1).toLowerCase());
					}else{
						pojo.setId(id);
						
					}
					pojo.setClassName(classname);
					//添加到list中
					list.add(pojo);
				} else if(element.getName().equals("service")){
					//<service name="pack">
					log.info("标签元素：" + element.getName());
					log.info("开启@Service注解扫描");
					//获取属性
					String packageName = element.attributeValue("name").replace(".", "/");
					log.info("扫描包下所有类：" + packageName);
					try {
						Enumeration<URL> resources = classLoader.getResources(packageName);
						while(resources.hasMoreElements()) {
							URL next = resources.nextElement();
							if(next.getProtocol().equals("file")) {
								String path = next.getPath().substring(1);
								//调用方法
								Set<String> service = service(path,packageName);
								for (String string : service) {
									Class<?> forName = Class.forName(string);
									if(forName.isAnnotationPresent(Service.class)) {
										Service annotation = forName.getAnnotation(Service.class);
										
										BeanPojo pojo = new BeanPojo();
										if(!"".equals(annotation.name())) { //属性名不为空
											log.info("声明类的Id：" + annotation.name());
											pojo.setId(annotation.name());
										} else {
											//默认给一个类名小写的id
											log.info("声明类的Id：" + string.substring(string.lastIndexOf(".") + 1).toLowerCase());
											pojo.setId(string.substring(string.lastIndexOf(".") + 1).toLowerCase());
										}
										pojo.setClassName(string);
										log.info("加载类：" + string);
										list.add(pojo);
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					
				}
			}
			
			/*Iterator<Element> elementIterator = rootElement.elementIterator();
			while(elementIterator.hasNext()){
				Element element = elementIterator.next();
				element.attributeValue("id");
			}*/
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 扫描注解
	 * @throws UnsupportedEncodingException 
	 */
	private Set<String> service(String path, String pathName) throws UnsupportedEncodingException {
		
		Set<String> set = new HashSet<String>();
		path = URLDecoder.decode(path, "utf-8");
		
		File file = new File(path);
		File[] listFiles = file.listFiles();
		if(listFiles == null) {
			return set;
		}
		for (File f : listFiles) {
			if(f.isDirectory()) {
				Set<String> service = service(f.getPath(),pathName);
				set.addAll(service);
			} else {
				log.info(f.getPath());
				//log.info(f.getPath().replaceAll("\\\\", "/"));
				int indexOf = f.getPath().replaceAll("\\\\", "/").indexOf(pathName);
				//log.info(indexOf);
				String substring = f.getPath().substring(indexOf);
				//substring  = substring.replaceAll("\\\\", ".");
				substring = substring.replaceAll("\\\\", ".").substring(0, substring.indexOf(".class"));
				set.add(substring);
			}
		}
	
		//将类的信息添加list中
		
		return set;
	}
	
	/**
	 * 实例化bean
	 */
	private void instanceBean() {
		for (BeanPojo beanPojo : list) {
			
			try {
				Class<?> clazz = Class.forName(beanPojo.getClassName());
				//实例化bean//调用无参构造
				Object obj = clazz.newInstance();
				//将bean信息添加到map
				//将创建好的对象放到map
				log.info("实例化bean对象:" + beanPojo.getId() + " {" + obj  + "}");
				map.put(beanPojo.getId(), obj);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		log.info("实例化bean对象完成:" + map);
		
	}
	/**
	 * 注解处理器
	 * 字段上
	 */
	private void filedAnnotation(Object bean) {
		
		//获取对象属性
		Field[] fields = bean.getClass().getDeclaredFields();
		//遍历
		for (Field field : fields) {
			//如果字段有注解声明
			if(field.isAnnotationPresent(Autowired.class)){
				log.info("所在类中字段依赖注入:" + bean);
				log.info("注入字段属性：" + field.getName());
				Object value = null;
				Autowired annotation = field.getAnnotation(Autowired.class);
				//获取注解的属性
				String name = annotation.name();
				if(name != null && !"".equals(name)) {
					//从map中取对象
					value = map.get(name);
				}else {
					//判断类型是否匹配
					Set<String> keySet = map.keySet();
					for (String key : keySet) {
						if(field.getType().isAssignableFrom(map.get(key).getClass())){
							value = map.get(key);
							break;
						}
					}
				}
				//暴力访问，private
				field.setAccessible(true);
				try {
					field.set(bean, value);//给bean添加属性
					log.info("注入成功：" + value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	private void methodAnnotation(Object bean) {
		
		Method[] methods = bean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			//判断是不是set方法
			int index = method.getName().indexOf("set");
			if(index > -1) {//是set方法
				
				Object value = null;
				if(method.isAnnotationPresent(Autowired.class)){
					log.info("所在类中set方法依赖注入:" + bean);
					log.info("set方法注入：" + method.getName());
					Autowired annotation = method.getAnnotation(Autowired.class);
					String name = annotation.name();
					if(name != null && !"".equals(name)) {
						
						value = map.get(name);
					} else {
						//判断类型
						for(String key : map.keySet()) {
							if(method.getParameterTypes()[0].isAssignableFrom(map.get(key).getClass())) {
								
								value = map.get(key);
								break;
							}
						}
					}
					method.setAccessible(true);
					try {
						method.invoke(bean, value);
						log.info("注入成功:" + value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/*public <T> T getBean(Class<T> clazz){
		
		//通过反射生成对象
		try {
			//判断这个类中是有注解Service声明
			if(clazz.isAnnotationPresent(Service.class)) {
				
				Service annotation = clazz.getAnnotation(Service.class);
				String name = annotation.name();//获取注解属性
				System.out.println(name);
				return clazz.newInstance();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(String str, Class<T> clazz) {
		
		T t = (T) map.get(str);
		return t;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> clazz) {
		
		String name = clazz.getName();
		name = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
		
		T t = (T) map.get(name);
		return t;
	}
}



