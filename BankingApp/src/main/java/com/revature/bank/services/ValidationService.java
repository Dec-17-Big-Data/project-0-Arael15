package com.revature.bank.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.exceptions.AccountNotEmptyException;
import com.revature.bank.exceptions.AccountNotOwnedException;
import com.revature.bank.exceptions.InsufficientBalanceException;
import com.revature.bank.exceptions.InvalidUsernameException;
import com.revature.bank.exceptions.PasswordMismatchException;
import com.revature.bank.exceptions.UserNotFoundException;
import com.revature.bank.model.Account;
import com.revature.bank.model.User;

public class ValidationService {

	private static ValidationService validationService;
	private static final Logger log = LogManager.getLogger(ValidationService.class);
	
	private ValidationService() {
		
	}
	
	public static ValidationService getValidationService() {
		if (validationService == null) {
			validationService = new ValidationService();
		}
		return validationService;
	}
	
	public boolean userIsUnique(String name) throws InvalidUsernameException {
		log.traceEntry("name = {}", name);
		UserService userServ = UserService.getUserService();
		Optional<User> user = userServ.getUserByName(name);
		try {
			user.get();
		}
		catch (NoSuchElementException e) {
			return log.traceExit(true);
		}
		log.info("Duplicate username");
		throw new InvalidUsernameException();
	}
	
	public boolean userExists(String name) throws UserNotFoundException {
		log.traceEntry("name = {}", name);
		UserService userServ = UserService.getUserService();
		Optional<User> user = userServ.getUserByName(name);
		try {
			user.get();
		}
		catch (NoSuchElementException e) {
			log.info("user does not exist");
			throw new UserNotFoundException();
		}
		return log.traceExit(true);
	}
	
	public boolean userExists(Integer id) throws UserNotFoundException {
		log.traceEntry("id = {}", id);
		UserService userServ = UserService.getUserService();
		Optional<User> user = userServ.getUserByID(id);
		try {
			user.get();
		}
		catch (NoSuchElementException e) {
			log.info("user does not exist");
			throw new UserNotFoundException();
		}
		return log.traceExit(true);
	}
	
	public boolean passwordIsCorrect(String name, String pass) throws PasswordMismatchException {
		log.traceEntry("username = {}, password = {}", name, pass);
		UserService userServ = UserService.getUserService();
		User user = null;
		Optional<User> lookup = userServ.getUserByName(name);
		try {
			user = lookup.get();	
		}
		catch (NoSuchElementException e) {
			log.warn("user not found");
			throw new PasswordMismatchException();
		}
		if (user.getPassword().equals(pass)) {
			return log.traceExit(true);
		}
		log.info("password does not match");
		throw new PasswordMismatchException();
	}
	
	public boolean balanceIsSufficient(Integer id, Double amount) throws InsufficientBalanceException {
		log.traceEntry("id = {}, amount = {}", id, amount);
		AccountService accountServ = AccountService.getAccountService();
		Account account = null;
		Optional<Account> lookup = accountServ.getAccount(id);
		try {
			account = lookup.get();
			
		}
		catch (NoSuchElementException e) {
			log.warn("account not found");
			throw new InsufficientBalanceException();
		}
		if (account.getBalance() >= amount) {
			return log.traceExit(true);
		}
		log.info("balance insufficient");
		throw new InsufficientBalanceException();
	}
	
	public boolean isSuperUser(String name, String pass) {
		log.traceEntry();
		Properties props = new Properties();
		try {
			InputStream in = new FileInputStream("src\\main\\resources\\admin.properties");
			props.load(in);
		}
		catch (IOException e) {
			return log.traceExit(false);
		}
		
		String superName = props.getProperty("username");
		String superPass = props.getProperty("password");
		
		return log.traceExit(superName.equals(name) && superPass.equals(pass));
	}
	
	public boolean ownsAccount(Integer id, Integer user) throws AccountNotOwnedException {
		log.traceEntry("id = {}, user = {}", id, user);
		AccountService accountServ = AccountService.getAccountService();
		Account account = null;
		Optional<Account> lookup = accountServ.getAccount(id);
		try {
			account = lookup.get();
		}
		catch (NoSuchElementException e) {
			log.warn("account does not exist");
			throw new AccountNotOwnedException();
		}
		if (account.getUserID() == user) {
			return log.traceExit(true);
		}
		log.info("account not owned by user");
		throw new AccountNotOwnedException();
	}
	
	public boolean accountIsEmpty(Integer id) {
		log.traceEntry("id = {}", id);
		AccountService accountServ = AccountService.getAccountService();
		Account account = null;
		Optional<Account> lookup = accountServ.getAccount(id);
		try {
			account = lookup.get();
			if (account.getBalance() == 0) {
				return log.traceExit(true);
			}
		}
		catch (NoSuchElementException e) {
			log.warn("account does not exist");
			throw new AccountNotEmptyException();
		}
		log.info("account balance not 0");
		throw new AccountNotEmptyException();
	}
	
	public boolean isValidUser(String username, String password) {
		log.traceEntry();
		try {
			userExists(username);
			return log.traceExit(passwordIsCorrect(username, password));
		}
		catch (UserNotFoundException e) {
			System.out.println("No account can be found with that username.");
		}
		catch (PasswordMismatchException e) {
			System.out.println("That password does not match that user account.");
		}
		return log.traceExit(false);
	}
}
