package com.revature.bank.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

import com.revature.bank.exceptions.AccountNotOwnedException;
import com.revature.bank.exceptions.InsufficientBalanceException;
import com.revature.bank.exceptions.InvalidUsernameException;
import com.revature.bank.exceptions.PasswordMismatchException;
import com.revature.bank.exceptions.UserNotFoundException;
import com.revature.bank.model.Account;
import com.revature.bank.model.User;

public class ValidationService {

	private static ValidationService validationService;
	
	private ValidationService() {
		
	}
	
	public static ValidationService getValidationService() {
		if (validationService == null) {
			validationService = new ValidationService();
		}
		return validationService;
	}
	
	public boolean userIsUnique(String name) throws InvalidUsernameException {
		UserService userServ = UserService.getUserService();
		Optional<User> user = userServ.getUserByName(name);
		try {
			user.get();
		}
		catch (NoSuchElementException e) {
			return true;
		}
		throw new InvalidUsernameException();
	}
	
	public boolean userExists(String name) throws UserNotFoundException {
		UserService userServ = UserService.getUserService();
		Optional<User> user = userServ.getUserByName(name);
		try {
			user.get();
		}
		catch (NoSuchElementException e) {
			throw new UserNotFoundException();
		}
		return true;
	}
	
	public boolean userExists(Integer id) throws UserNotFoundException {
		UserService userServ = UserService.getUserService();
		Optional<User> user = userServ.getUserByID(id);
		try {
			user.get();
		}
		catch (NoSuchElementException e) {
			throw new UserNotFoundException();
		}
		return true;
	}
	
	public boolean passwordIsCorrect(String name, String pass) throws PasswordMismatchException {
		UserService userServ = UserService.getUserService();
		User user = null;
		Optional<User> lookup = userServ.getUserByName(name);
		try {
			user = lookup.get();
		}
		catch (NoSuchElementException e) {
		}
		if (user.getPassword().equals(pass)) {
			return true;
		}
		throw new PasswordMismatchException();
	}
	
	public boolean balanceIsSufficient(Integer id, Double amount) throws InsufficientBalanceException {
		AccountService accountServ = AccountService.getAccountService();
		Account account = null;
		Optional<Account> lookup = accountServ.getAccount(id);
		try {
			account = lookup.get();
		}
		catch (NoSuchElementException e) {
		}
		if (account.getBalance() >= amount) {
			return true;
		}
		throw new InsufficientBalanceException();
	}
	
	public boolean isSuperUser(String name, String pass) {
		Properties props = new Properties();
		try {
			InputStream in = new FileInputStream("src\\main\\resources\\connection.properties");
			props.load(in);
		}
		catch (IOException e) {
			return false;
		}
		
		String superName = props.getProperty("username");
		String superPass = props.getProperty("password");
		
		return superName.equals(name) && superPass.equals(pass);
	}
	
	public boolean ownsAccount(Integer id, Integer user) throws AccountNotOwnedException {
		AccountService accountServ = AccountService.getAccountService();
		Account account = null;
		Optional<Account> lookup = accountServ.getAccount(id);
		try {
			account = lookup.get();
		}
		catch (NoSuchElementException e) {
		}
		if (account.getUserID() == user) {
			return true;
		}
		throw new InsufficientBalanceException();
	}
	
	public boolean accountIsEmpty(Integer id) {
		AccountService accountServ = AccountService.getAccountService();
		Account account = null;
		Optional<Account> lookup = accountServ.getAccount(id);
		try {
			account = lookup.get();
		}
		catch (NoSuchElementException e) {
		}
		if (account.getBalance() > 0) {
			return false;
		}
		throw new InsufficientBalanceException();
	}
}
