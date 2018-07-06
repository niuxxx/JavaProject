package com.example.proxy.statics;

import com.example.proxy.Subject;

/**
 * 静态代理类
 * @author Admin
 *
 */
public class SubjectStaticProxy implements Subject{

	private Subject subject;
	
	public SubjectStaticProxy(Subject subject) {
		
		this.subject = subject;
	}

	public void add(String name) {
		
		try {
			System.out.println("开始添加。。。");
			subject.add(name);
			System.out.println("添加成功。。。");
		} catch (Exception e) {
			System.out.println("error");
		}
	}

}
