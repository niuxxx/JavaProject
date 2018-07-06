package com.example.proxy.statics;

import com.example.proxy.Subject;
import com.example.proxy.SubjectImpl;

/**
 * 静态代理工程
 * @author Admin
 *
 */
public class SubjectStaticFactory {
	
	public static Subject getInstance(){
		
		return new SubjectStaticProxy(new SubjectImpl());
	}
}
