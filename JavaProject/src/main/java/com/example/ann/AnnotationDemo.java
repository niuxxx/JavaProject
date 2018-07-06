package com.example.ann;

import com.example.annotation.UserServiceImpl;

public class AnnotationDemo {
	
	public static void main(String[] args) {
		
		ApplicationContext ac = new ApplicationContext("application.xml");
		UserServiceImpl bean = ac.getBean(UserServiceImpl.class);
		System.out.println(bean);
		bean.login();
	}
}
