package com.revature.bank.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.exceptions.InvalidUsernameException;
import com.revature.bank.exceptions.UserNotFoundException;
import com.revature.bank.model.Account;
import com.revature.bank.model.User;
import com.revature.bank.services.AccountService;
import com.revature.bank.services.UserService;
import com.revature.bank.services.ValidationService;

public class SuperInteractor {

	private static SuperInteractor superInteractor;
	private static final Logger log = LogManager.getLogger(SuperInteractor.class);
	private UserService userService = UserService.getUserService();
	private AccountService accountService = AccountService.getAccountService();
	private ValidationService validator = ValidationService.getValidationService();
	private String superPassword;
	
	private SuperInteractor() {
		Properties props = new Properties();
		try {
			InputStream in = new FileInputStream("src\\main\\resources\\admin.properties");
			props.load(in);
			superPassword = props.getProperty("password");
		}
		catch (IOException e) {log.traceExit(false);
		}
		
		
	}
	
	public static SuperInteractor getSuperInteractor() {
		if (superInteractor == null) {
			superInteractor = new SuperInteractor();
		}
		return superInteractor;
	}

	public Integer selectMenuOption(Scanner s) {
		log.traceEntry("Entry selection menu");
		System.out.println("\nWhich of the following can we help you with today?");
		while (true) {
			System.out.println("1. View all users");
			System.out.println("2. View accounts belonging to a particular user");
			System.out.println("3. Update a user's information");
			System.out.println("4. Create a new user");
			System.out.println("5. Create a new account for a user");
			System.out.println("6. Delete a user");
			System.out.println("7. Delete all users");
			System.out.println("8. Logout");
			System.out.print("\nSelection: ");
			String choice = s.nextLine().toLowerCase().trim();
			log.trace("Menu selection = {}", choice);
			switch(choice) {
			case "1":
			case "1.":
				return log.traceExit(1);
			case "2":
			case "2.":
				return log.traceExit(2);
			case "3":
			case "3.":
			case "update":
				return log.traceExit(3);
			case "4":
			case "4.":
				return log.traceExit(4);
			case "5":
			case "5.":
				return log.traceExit(5);
			case "6":
			case "6.":
				return log.traceExit(6);
			case "7":
			case "7.":
				return log.traceExit(7);
			case "8":
			case "8.":
				return log.traceExit(8);
			default:
				System.out.println("\nYour selection could not be processed.");
				System.out.println("\nPlease select the number of the option you desire.\n");
				break;
			}	
		}
	}

	public void viewUser(Scanner s) {
		log.traceEntry();
		Optional<List<User>> allUsers = userService.getAllUsers();
		List<User> allUsersList = null;
		try {
			allUsersList = allUsers.get();
		}
		catch (NoSuchElementException e) {
			log.error("Encountered error while retrieving users from database");
			System.out.println("\nCould not retrieve user data from database.");
		}
		if (allUsersList != null) {
			System.out.println("\nThe following users are on record:");
			System.out.println(String.format("%-10s %-20s %-20s %-20s %-20s",
					"User ID", "Username", "Password", "First Name", "Last Name"));
			for (User u : allUsersList) {
				System.out.println(String.format("%-10s %-20s %-20s %-20s %-20s",
						u.getUserID(), u.getUsername(), u.getPassword(), u.getFirstName(), u.getLastName()));
			}
		}
		System.out.println("\nPlease hit ENTER to be taken back to the main menu.");
		s.nextLine();
		log.traceExit();
	}

	public void viewAccount(Scanner s) {
		log.traceEntry();
		System.out.println("\nPlease enter the ID of the user whose accounts you would like to view.");
		System.out.print("User ID: ");
		String inputID = s.nextLine();
		Integer numId = 0;
		try {
			numId = Integer.valueOf(inputID);
		}
		catch (NumberFormatException e) {
			log.warn("Invalid user ID entered");
			System.out.println("\nThat is not a valid User ID.");
		}
		if (numId != 0) {
			Optional<List<Account>> userAccounts = accountService.getUserAccounts(numId);
			List<Account> userAccountsList = null;
			try {
				userAccountsList = userAccounts.get();
			}
			catch (NoSuchElementException e) {
				log.error("Error occurred while trying to retrieve account info from database");
				System.out.println("\nCould not retrieve account info from database");
			}
			if (userAccountsList != null) {
				System.out.println("\nThe following accounts were found belonging to the specified user:");
				System.out.println(String.format("\n%-15s %-15s", "Account ID", "Balance"));
				for (Account a : userAccountsList) {
					System.out.println(String.format("%-15s %-15s", a.getAccountID(), a.getBalance()));
				}
			}
		}
		System.out.println("\nPlease hit ENTER to be taken back to the main menu.");
		s.nextLine();
		log.traceExit();
	}

	public void updateInfo(Scanner s) {
		log.traceEntry();
		System.out.println("\nPlease enter the ID of the user whose information you would like to update.");
		System.out.print("User ID: ");
		String inputID = s.nextLine();
		Integer numId = 0;
		try {
			numId = Integer.valueOf(inputID);
		}
		catch (NumberFormatException e) {
			log.warn("Invalid user ID entered");
			System.out.println("\nThat is not a valid User ID.");
		}
		try {
			boolean exists = validator.userExists(numId);
		}
		catch (UserNotFoundException e) {
			log.warn("User ID does not match value in table");
			System.out.println("\nThere is no user belonging to that ID.");
			numId = 0;
		}
		if (numId != 0) {
			boolean chosen = false;
			while (!chosen) {
				System.out.println("\nPlease select the field you would like to modify:");
				System.out.println("1. First name");
				System.out.println("2. Last name");
				System.out.println("3. Username");
				System.out.println("4. Password");
				System.out.println("5. Cancel");
				System.out.print("Selection: ");
				String choice = s.nextLine().toLowerCase().trim();
				switch (choice) {
				case "1":
				case "1.":
				case "first":
				case "first name":
				case "1. first name":
					chosen = true;
					System.out.println("\nPlease enter the new value for this field:");
					System.out.print("First name: ");
					String input1 = s.nextLine();
					try {
						Optional<Boolean> done = userService.updateUser(numId, input1, "firstName");
						done.get();
						System.out.println("\nUser field successfully updated.");
					}
					catch (NoSuchElementException e) {
						System.out.println("\nAn error occurred while attempting to update user info");
						System.out.println("\nNo changes will be made to the user at this time.");
						log.error("Error while updating user in database");
					}
					break;
				case "2":
				case "2.":
				case "last":
				case "last name":
				case "2. last name":
					chosen = true;
					System.out.println("\nPlease enter the new value for this field:");
					System.out.print("Last name: ");
					String input2 = s.nextLine();
					try {
						Optional<Boolean> done = userService.updateUser(numId, input2, "lastName");
						done.get();
						System.out.println("\nUser field successfully updated.");
					}
					catch (NoSuchElementException e) {
						System.out.println("\nAn error occurred while attempting to update user info");
						System.out.println("\nNo changes will be made to the user at this time.");
						log.error("Error while updating user in database");
					}
					break;
				case "3":
				case "3.":
				case "username":
				case "3. username":
					chosen = true;
					System.out.println("\nPlease enter the new value for this field:");
					System.out.print("Username: ");
					String input3 = s.nextLine();
					try {
						Optional<Boolean> done = userService.updateUser(numId, input3, "userame");
						done.get();
						System.out.println("\nUser field successfully updated.");
					}
					catch (NoSuchElementException e) {
						System.out.println("\nAn error occurred while attempting to update user info");
						System.out.println("\nNo changes will be made to the user at this time.");
						log.error("Error while updating user in database");
					}
					break;
				case "4":
				case "4.":
				case "password":
				case "4. password":
					chosen = true;
					System.out.println("\nPlease enter the new value for this field:");
					System.out.print("Password: ");
					String input4 = s.nextLine();
					try {
						Optional<Boolean> done = userService.updateUser(numId, input4, "password");
						done.get();
						System.out.println("\nUser field successfully updated.");
					}
					catch (NoSuchElementException e) {
						System.out.println("\nAn error occurred while attempting to update user info");
						System.out.println("\nNo changes will be made to the user at this time.");
						log.error("Error while updating user in database");
					}
					break;
				case "5":
				case "5.":
				case "cancel":
				case "5. first name":
					chosen = true;
					break;
				default:
					System.out.println("\nYour selection could not be processed.");
					System.out.println("\nPlease select the number of the option you desire.\n");
					break;
				}
			}
		}
		System.out.println("\nPlease hit ENTER to be taken back to the main menu.");
		s.nextLine();
		log.traceExit();
	}

	public void createNewAccount(Scanner s) {
		log.traceEntry();
		System.out.println("\nPlease enter the ID of the user who you want to create a new account for.");
		System.out.print("User ID: ");
		String inputID = s.nextLine();
		Integer numId = 0;
		try {
			numId = Integer.valueOf(inputID);
		}
		catch (NumberFormatException e) {
			log.warn("Invalid user ID entered");
			System.out.println("\nThat is not a valid User ID.");
		}
		if (numId != 0) {
			Optional<Integer> newAccount = accountService.addAccount(numId);
			Integer accountID = 0;
			try {
				accountID = newAccount.get();
			}
			catch (NoSuchElementException e) {
				log.error("Error during retrieval of new account");
				System.out.println("\nAn error occurred while creating a new account");
			}
			if (accountID > 0) {
				System.out.println("\nA new account with ID " + accountID + " has been created.");
			}
		}
		System.out.println("\nPlease hit ENTER to be taken back to the main menu.");
		s.nextLine();
		log.traceExit();
	}

	public void createNewUser(Scanner s) {
		log.traceEntry();
		boolean goodName = false;
		String choice = "", pass = "", first = "", last = "";
		
		// Take input for username
		while (!goodName ) {
			System.out.println("\nUsername must be at least 8 characters.");
			System.out.print("\nPlease enter desired username: ");
			choice = s.nextLine();
			try {
				goodName = validator.userIsUnique(choice);
			}
			catch (InvalidUsernameException e) {
				log.info("Duplicate username {}", choice);
				System.out.println("\nThat username is not available.");
				System.out.println("\nPlease try again.");
			}
			if (goodName) {
				if (choice.length() < 8) {
					System.out.println("\nUsername is of insufficient length");
					goodName = false;
				}
			}
		}
		System.out.println("\nThat username is available.");
		log.trace("Username accepted");
		
		// Take input for password
		boolean goodPass = false;
		while (!goodPass) {
			System.out.print("\nPlease enter desired password: ");
			pass = s.nextLine();
			if (pass.length() > 0) {
				goodPass = true;
				System.out.println("\nThat password is acceptable.");
			}
			else {
				System.out.println("\nPassword cannot be empty");
			}
		}
		log.trace("Password accepted");
		
		// Take input for first name
		boolean goodFirst = false;
		while (!goodFirst) {
			System.out.print("\nPlease enter first name: ");
			first = s.nextLine();
			if (first.length() > 0) {
				goodFirst = true;
			}
			else {
				System.out.println("\nFirst name cannot be empty");
			}
		}
		log.trace("First name accepted");
		
		//	Take input for last name
		boolean goodLast = false;
		while (!goodLast) {
			System.out.print("Please enter last name: ");
			last = s.nextLine();
			if (last.length() > 0) {
				goodLast = true;
			}
			else {
				System.out.println("\nLast name cannot be empty");
			}
		}
		log.trace("Last name accepted");
		
		Optional<User> newUser = userService.createUser(first, last, choice, pass);
		User unpacked = null;
		try {
			unpacked = newUser.get();
			System.out.println("\nA new user has been created with the following information: ");
			System.out.println("First name: " + unpacked.getFirstName());
			System.out.println("Last name: " + unpacked.getLastName());
			System.out.println("Username: " + unpacked.getUsername());
			System.out.println("\nThe user ID is " + unpacked.getUserID());
			log.info("New user {} created", unpacked.getUserID());
		}
		catch (NoSuchElementException e) {
			log.error("New user could not be created");
			System.out.println("\nThere was an error creating a new user.");
		}
		
	}

	public void deleteUser(Scanner s) {
		log.traceEntry();
		System.out.println("\nPlease enter the ID of the user you would like to delete.");
		System.out.print("User ID: ");
		String inputID = s.nextLine();
		Integer numId = 0;
		try {
			numId = Integer.valueOf(inputID);
		}
		catch (NumberFormatException e) {
			log.warn("Invalid user ID entered");
			System.out.println("\nThat is not a valid User ID.");
		}
		if (numId != 0) {
			System.out.println("\nPlease re-enter password to confirm deletion of user.");
			System.out.print("Password: ");
			String entry = s.nextLine();
			if (entry.equals(superPassword)) {
				System.out.println("\nThe user " + numId + " will be deleted.");
				userService.deleteUser(numId);
				log.info("User {} has been deleted", numId);
			}
			else {
				System.out.println("\nThe user will not be deleted at this time");
			}
		}
		System.out.println("\nPlease hit ENTER to be taken back to the main menu.");
		s.nextLine();
		log.traceExit();
	}

	public void deleteAllUsers(Scanner s) {
		System.out.println("\nPlease re-enter password to confirm deletion of all users.");
		System.out.print("Password: ");
		String entry = s.nextLine();
		if (entry.equals(superPassword)) {
			Optional<List<User>> allUsers = userService.getAllUsers();
			List<User> allUsersList = null;
			try {
				allUsersList = allUsers.get();
			}
			catch (NoSuchElementException e) {
				log.error("Error retrieving users from the database");
				System.out.println("\nCould not remove users from the database");
			}
			if (allUsersList != null) {
				for (User u : allUsersList) {
					userService.deleteUser(u.getUserID());
				}
				System.out.println("\nAll users have been removed from records.");
				log.info("All users deleted");
			}
		}
		System.out.println("\nPlease hit ENTER to be taken back to the main menu.");
		s.nextLine();
		log.traceExit();
	}

}
