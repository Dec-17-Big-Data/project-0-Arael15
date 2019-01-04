package com.revature.bank.services;

import java.util.List;
import java.util.Optional;

import com.revature.bank.dao.UserDao;
import com.revature.bank.dao.UserOracle;
import com.revature.bank.model.User;

public class UserService {

	
	private static UserService userService;
	final static UserDao userDao = UserOracle.getUserOracle();
	
	private UserService() {
		
	}
	
	public static UserService getUserService() {
		if (userService == null) {
			userService = new UserService();
		}
		return userService;
	}
	
	public Optional<User> getUserByID(Integer id) {
		return null;
		
	}
	
	public Optional<User> getUserByName(String name) {
		return null;
		
	}
	
	public Optional<List<User>> getAllUsers() {
		return null;
		
	}
	
	public Optional<User> createUser(String firstName, String lastName, String userName, String password) {
		return null;
		
	}
	
	public Optional<Boolean> deleteUser(Integer id) {
		return null;
		
	}
}
