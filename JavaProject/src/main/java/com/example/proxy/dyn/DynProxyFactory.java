package com.example.proxy.dyn;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import com.example.proxy.Subject;
import com.example.proxy.SubjectImpl;

/**
 * 动态代理工厂
 * @author Admin
 *
 */
public class DynProxyFactory {
	
	public static Subject getInstance() {
		
		Subject subject = new SubjectImpl();
		
		SubjectInvocationHandler handler = new SubjectInvocationHandler(subject);
		
		return (Subject) Proxy.newProxyInstance(subject.getClass().getClassLoader(), subject.getClass().getInterfaces(), handler);
		
	}
	/**
	 * 使用反射，访问对象
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static Subject getSubject() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		//获取动态代理类
		Class<?> proxyClass = Proxy.getProxyClass(Subject.class.getClassLoader(), Subject.class.getInterfaces());
		
		//获取代理类构造函数
		Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
		//通过构造方法创建代理对象
		Subject subject = (Subject) constructor.newInstance(new SubjectInvocationHandler(new SubjectImpl()));
		
		return subject;
	}
}
