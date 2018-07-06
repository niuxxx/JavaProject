package com.example.proxy;

import com.example.proxy.dyn.DynProxyFactory;

public class ProxyDemo {
	
	public static void main(String[] args) {
		
//		Subject subject = SubjectStaticFactory.getInstance();
//		subject.add("niu");
		
		
//		Subject s = new SubjectImpl();
//		s.add(name);
		
		
		Subject suject = DynProxyFactory.getInstance();
		suject.add("niu");
		
	}
}
