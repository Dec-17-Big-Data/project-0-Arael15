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
	
	public static void main(String[] args) {
		log.traceEntry("Session started");
		Scanner s = new Scanner(System.in);
		interactor = Interactor.getInteractor();
		validator = ValidationService.getValidationService();
		Boolean registering = interactor.greet(s);
		while (registering) {
			registering = interactor.register(s);
		}
		boolean loggedIn = false;
		boolean loggedInSuper = false;
		while (!loggedIn || user == null) {
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
			break;
		}
		while (loggedInSuper) {
			System.out.println("Success!");
			loggedInSuper = false;
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
				loggedIn = false;
				break;
			default:
				break;
			}
		}
		System.out.println("\nThank you for using JDBCBank today!\n");
		s.close();
		log.traceExit("Session terminated\n\n");
		
	}
}
