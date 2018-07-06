package com.example.reflex;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

public class GetClassTest {
	
	public static void main(String[] args) throws Exception {
		
		/*//第一种
		Emp emp = new Emp();
		Class<? extends Emp> class1 = emp.getClass();
		*/
		//第二种
		Class class2 = Emp.class;
		System.out.println(class2.getName());
		
		//第三种
		Class<?> clazz = Class.forName("com.example.reflex.Emp");
		
		/*System.out.println(class1);
		System.out.println(clazz);*/
		System.out.println(clazz);
		System.out.println(clazz.getName());
		
		//创建类对象
		//实际上调用的是类的无参构造，一般的一个类若声明了带参数的构造器，也要声明一个无参构造
		Object newInstance = clazz.newInstance();
		
		System.out.println(newInstance);
		
		//类加载器
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		System.out.println(classLoader);
		System.out.println(classLoader.getParent());
		
		System.out.println(clazz.getClassLoader());
		
		InputStream is = GetClassTest.class.getClassLoader().getResourceAsStream("url.properties");
	
		System.out.println(is);
		
		Properties p = new Properties();
		p.load(is);
		String url = p.getProperty("jdbc.url");
		System.out.println(url);
		
		//获取方法，不能获取private方法
		Method[] methods = clazz.getMethods();
		System.out.println("--------不能获取private方法");
		for (Method method : methods) {
			System.out.println(method);
		}
		
		//获取方法，只获取类中声明的方法，包括private方法
		Method[] declaredMethods = clazz.getDeclaredMethods();
		
		System.out.println("--------获取所有方法");
		for (Method method : declaredMethods) {
			System.out.println(method.getName());
		}
		
		//获取指定方法，第一个参数是方法名，第二个参数是方法的参数
		Method declaredMethod = clazz.getDeclaredMethod("setName", String.class);
		System.out.println("---------获取指定方法");
		System.out.println(declaredMethod);
		
		//方法执行
		declaredMethod.invoke(newInstance, "niuyan");
		System.out.println(newInstance);
		
		//获取字段，private字段获取不到
		Field[] fields = clazz.getFields();
		System.out.println("----------获取字段");
		for (Field field : fields) {
			System.out.println(field.getName());
		}
		
		Field[] declaredFields = clazz.getDeclaredFields();
		System.out.println("-----------获取所有字段");
		for (Field field : declaredFields) {
			System.out.println(field.getName() + "," + field.getType());
		}
		//获取指定字段
		Field declaredField = clazz.getDeclaredField("name");
		System.out.println("-----------获取指定字段");
		System.out.println(declaredField.getName());
		
		//获取指定值
		System.out.println("-----------获取指定字段的值");
		//如果是私有的强制访问
		declaredField.setAccessible(true);
		System.out.println(declaredField.get(newInstance));
		
		//获取构造器
		Constructor<?>[] constructors = clazz.getConstructors();
		System.out.println("-----------获取构造器");
		for (Constructor<?> constructor : constructors) {
			System.out.println(constructor);
		}
		
		//获取指定构造器
		System.out.println("-----------获取指定构造器");
		Constructor<?> constructor = clazz.getConstructor(int.class, String.class, String.class);
		System.out.println(constructor);
		//调用构造器创建对象
		System.out.println("-----------调用构造器创建对象");
		Object obj = constructor.newInstance(25, "niuyan", "passwrod");
		System.out.println(obj);
		
	}
	
}
