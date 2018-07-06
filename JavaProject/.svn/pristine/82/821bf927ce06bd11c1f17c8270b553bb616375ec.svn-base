package com.example.reflex;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;

import org.apache.log4j.Logger;

import com.example.ann.ApplicationContext;

public class Emp {
	
	private int age;
	
	private String name;
	
	private String password;
	
	public Emp() {
		
	}
	
	private static final Logger log = Logger.getLogger(Emp.class);
	
	public Emp(int age, String name, String password) {
		this.age = age;
		this.name = name;
		this.password = password;
	}
	
	private void getName1() {
		System.out.println("A");
	}
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Emp [age=" + age + ", name=" + name + ", password=" + password + "]";
	}
	
	public static void main(String[] args) throws IOException {
		
		/*File file = new File("E:/maven仓库/workspace/JavaProject/target/classes/com/example");
		File[] listFiles = file.listFiles();
		for (File f : listFiles) {
			System.out.println(f.getName());
		}*/
		//判断是否有扫描注解
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		//根据包名是查找类名]
		String str = "com/example/ann";
		Enumeration<URL> url = loader.getResources(str);
		while(url.hasMoreElements()){
			URL next = url.nextElement();
			//System.out.println(next.getPath());
			if(next == null) {
				continue;
			}
			String protocol = next.getProtocol();
			if(protocol.equals("file")) {
				String path = next.getPath();
				path = URLDecoder.decode(path, "utf-8").substring(1);
				System.out.println(path);
				File file = new File(path);
				boolean exists = file.exists();
				if(!exists){
					return;
				}
				File[] listFiles = file.listFiles();
				if(listFiles == null) {
					System.out.println("null");
				}
				for (File f : listFiles) {
					if(f.isDirectory()) { //是否目录
						
					}
					int indexOf = f.getPath().replaceAll("\\\\", "/").indexOf(str);
					System.out.println(indexOf);
					String substring = f.getPath().substring(indexOf);
					
					substring = substring.replaceAll("\\\\", ".").substring(0, substring.indexOf(".class"));
					System.out.println(substring);
				}
				
			}
		}
		
	}
}
