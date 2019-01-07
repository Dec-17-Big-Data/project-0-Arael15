package com.revature.bank.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.dao.UserDao;
import com.revature.bank.dao.UserOracle;
import com.revature.bank.exceptions.InvalidUsernameException;
import com.revature.bank.exceptions.UserNotFoundException;
import com.revature.bank.model.User;

public class UserService {

	
	private static UserService userService;
	final static UserDao userDao = UserOracle.getUserOracle();
	final static ValidationService validationService = ValidationService.getValidationService();
	private static final Logger log = LogManager.getLogger(UserService.class);
	
	private UserService() {
		
	}
	
	public static UserService getUserService() {
		if (userService == null) {
			userService = new UserService();
		}
		return userService;
	}
	
	public Optional<User> getUserByID(Integer id) {
		log.traceEntry("id = {}", id);
		return userDao.getUserByID(id);
	}
	
	public Optional<User> getUserByName(String name) {
		log.traceEntry("name = {}", name);
		return userDao.getUserByName(name);
	}
	
	public Optional<List<User>> getAllUsers() {
		log.traceEntry();
		return userDao.getAllUsers();
	}
	
	public Optional<User> createUser(String firstName, String lastName, String userName, String password) {
		log.traceEntry();
		return log.traceExit(userDao.createUser(firstName, lastName, userName, password));
	}
	
	public Optional<Boolean> deleteUser(Integer id) {
		log.traceEntry("id = {}", id);
		return log.traceExit(userDao.deleteUser(id));
	}
	
	public Optional<Boolean> updateUser(Integer id, String update, String type){
		log.traceEntry();
		return log.traceExit(userDao.updateUser(id, update, type));
	}
}
