package com.revature.bank.util;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.model.User;
import com.revature.bank.services.ValidationService;

public class JDBCBank {
	
	private static Interactor interactor;
	private static ValidationService validator;
	private static User user;
	private static final Logger log = LogManager.getLogger(JDBCBank.class);
	private static SuperInteractor superInteractor;
	
	public static void main(String[] args) {
		log.info("Session started");
		Scanner s = new Scanner(System.in);
		interactor = Interactor.getInteractor();
		superInteractor = SuperInteractor.getSuperInteractor();
		validator = ValidationService.getValidationService();
		Boolean registering = interactor.greet(s);
		while (registering) {
			registering = interactor.register(s);
		}
		boolean loggedIn = false;
		boolean loggedInSuper = false;
		while ((!loggedIn || user == null) && !loggedInSuper) {
			String username = interactor.getUsername(s);
			String password = interactor.getPassword(s);
			loggedInSuper = validator.isSuperUser(username, password);
			if (!loggedInSuper) {
				loggedIn = validator.isValidUser(username, password);
			}
			if (loggedIn) {
				user = interactor.retrieveUserInfo(username);
				System.out.println("\nWelcome to JDBCBank, " + username + "!\n");
			}
		}
		while (loggedInSuper) {
			Integer choice = superInteractor.selectMenuOption(s);
			switch (choice) {
			case 1:
				superInteractor.viewUser(s);
				break;
			case 2:
				superInteractor.viewAccount(s);
				break;
			case 3:
				superInteractor.updateInfo(s);
				break;
			case 4:
				superInteractor.createNewUser(s);
				break;
			case 5:
				superInteractor.createNewAccount(s);
				break;
			case 6:
				superInteractor.deleteUser(s);
				break;
			case 7:
				superInteractor.deleteAllUsers(s);
				break;
			case 8:
				loggedInSuper = false;
				break;
			default:
				break;
			}
		}
		while (loggedIn) {
			Integer choice = interactor.selectMenuOption(s);
			switch (choice) {
			case 1:
				interactor.viewAccount(s, user);
				break;
			case 2:
				interactor.createAccount(s, user);
				break;
			case 3:
				interactor.deleteAccount(s, user);
				break;
			case 4:
				interactor.makeTransaction(s, user);
				break;
			case 5:
				interactor.viewTransaction(s, user);
				break;
			case 6:
				loggedIn = false;
				break;
			default:
				break;
			}
		}
		System.out.println("\nThank you for using JDBCBank today!\n");
		s.close();
		log.info("Session terminated\n\n");
		
	}
}
