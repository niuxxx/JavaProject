package com.example.proxy.dyn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 调用处理类
 * @author Admin
 *
 */
public class SubjectInvocationHandler implements InvocationHandler{

	private Object obj;
	
	public SubjectInvocationHandler(Object obj) {
		
		this.obj = obj;
	}
	
	//处理在动态代理类对象上方法调用，通常在该方法中实现对委托类的代理访问，
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		
		//前置通知
		System.out.println("前置通知");
		//反射机制代理对象，调用方法
		method.invoke(obj, args);
		//后置通知
		System.out.println("后置通知");
		return null;
	}

		
}
