package com.example.annotation;

import com.example.ann.Autowired;
import com.example.ann.Service;

@Service
public class UserServiceImpl implements UserService{
	
	
	
	private UserDao dao;
	
	
	public UserDao getDao() {
		return dao;
	}

	@Autowired
	public void setDao(UserDao dao) {
		this.dao = dao;
	}

	public void login() {
		
		String userName = dao.getUserName();
		System.out.println("µÇÂ¼³É¹¦");
	}
}
