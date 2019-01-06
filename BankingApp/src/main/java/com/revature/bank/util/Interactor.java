package com.revature.bank.util;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

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
import com.revature.bank.services.AccountService;
import com.revature.bank.services.TransactionService;
import com.revature.bank.services.UserService;
import com.revature.bank.services.ValidationService;

public class Interactor {

	private static Interactor interactor;
	private ValidationService validator = ValidationService.getValidationService();
	private UserService userService = UserService.getUserService();
	private AccountService accountService = AccountService.getAccountService();
	private TransactionService transactionService = TransactionService.getTransactionService();
	private static final Logger log = LogManager.getLogger(Interactor.class);
	
	private Interactor() {
	}
	
	public static Interactor getInteractor() {
		if (interactor == null) {
			interactor = new Interactor();
		}
		return interactor;
	}
	
	public Boolean greet(Scanner s) {
		log.traceEntry("Begin greeting");
		System.out.println("Welcome to JDBCBank!\n");
		boolean selected = false;
			while (!selected) {
				System.out.println("Please select one of the following:");
				System.out.println("1. Log in as a returning user");
				System.out.println("2. Register a new user");
				System.out.print("\nSelection: ");
				String choice = s.nextLine();
				System.out.println("\nRedirecting...\n");
				choice = choice.toLowerCase().trim();
				switch (choice) {
				case "1":
				case "1.":
				case "login":
				case "log in":
					selected = true;
					return log.traceExit(false);
				case "2":
				case "2.":
				case "register":
					selected = true;
					return log.traceExit(true);
				default:
					System.out.println("Your choice could not be recognized.");
					System.out.println("Please enter a number for your selection.\n");
			}
		}
		return log.traceExit(false);
	}
	
	public Boolean register(Scanner s) {
		log.traceEntry();
		boolean goodName = false;
		String choice = "", pass = "", first = "", last = "";
		
		// Take input for username
		while (!goodName ) {
			System.out.println("Username must be at least 8 characters.");
			System.out.print("Please enter desired username: ");
			choice = s.nextLine();
			try {
				goodName = validator.userIsUnique(choice);
			}
			catch (InvalidUsernameException e) {
				log.info("Duplicate username {}", choice);
				System.out.println("That username is not available.");
				System.out.println("Please try again.");
			}
			if (goodName) {
				if (choice.length() < 8) {
					System.out.println("Username is of insufficient length");
					goodName = false;
				}
			}
		}
		System.out.println("\nThat username is available.\n");
		log.trace("Username accepted");
		
		// Take input for password
		boolean goodPass = false;
		while (!goodPass) {
			System.out.print("Please enter desired password: ");
			pass = s.nextLine();
			if (pass.length() > 0) {
				goodPass = true;
				System.out.println("\nThat password is acceptable\n");
			}
			else {
				System.out.println("Password cannot be empty");
			}
		}
		log.trace("Password accepted");
		
		// Take input for 
		boolean goodFirst = false;
		while (!goodFirst) {
			System.out.print("Please enter your first name: ");
			first = s.nextLine();
			if (first.length() > 0) {
				goodFirst = true;
			}
			else {
				System.out.println("First name cannot be empty");
			}
		}
		log.trace("First name accepted");
		
		boolean goodLast = false;
		while (!goodLast) {
			System.out.print("Please enter your last name: ");
			last = s.nextLine();
			if (last.length() > 0) {
				goodLast = true;
			}
			else {
				System.out.println("Last name cannot be empty");
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
			System.out.println("\nYour user ID is " + unpacked.getUserID());
			System.out.println("Please keep this number for your records.");
			System.out.println("\nYou will now be redirected to the login screen.\n");
			log.info("New user {} created", unpacked.getUserID());
		}
		catch (NoSuchElementException e) {
			log.error("New user could not be created");
			System.out.println("There was an error processing your request.");
			System.out.println("If you would like to try registering again, please type 'TRY AGAIN'");
			System.out.println("Otherwise, you will be taken to the login screen.");
			String response = s.nextLine();
			if (response.toLowerCase().equals("try again")) {
				return log.traceExit(true);
			}
		}
		return log.traceExit(false);
		
	}

	public User retrieveUserInfo(String username) {
		log.traceEntry("Username = {}", username);
		Optional<User> u = userService.getUserByName(username);
		try {
			return log.traceExit(u.get());
		}
		catch (NoSuchElementException e) {
			log.error("Could not retrieve user info from the database");
			System.out.println("An error occured while trying to retrieve user information.");
			return null;
		}
	}

	public Integer selectMenuOption(Scanner s) {
		log.traceEntry("Entry selection menu");
		System.out.println("Which of the following can we help you with today?");
		while (true) {
			System.out.println("1. View your accounts");
			System.out.println("2. Create a new account");
			System.out.println("3. Delete an existing account");
			System.out.println("4. Make a transaction");
			System.out.println("5. Logout");
			System.out.print("\nSelection: ");
			String choice = s.nextLine().toLowerCase().trim();
			log.trace("Menu selection = {}", choice);
			switch(choice) {
			case "1":
			case "1.":
			case "view":
				return log.traceExit(1);
			case "2":
			case "2.":
			case "create":
				return log.traceExit(2);
			case "3":
			case "3.":
			case "delete":
				return log.traceExit(3);
			case "4":
			case "4.":
			case "make":
				return log.traceExit(4);
			case "5":
			case "5.":
			case "logout":
			case "log out":
				return log.traceExit(5);
			default:
				System.out.println("\nYour selection could not be processed.");
				System.out.println("\nPlease select the number of the option you desire.\n");
				break;
			}	
		}
	}

	public void viewAccount(Scanner s, User user) {
		log.traceEntry("User = {}", user.getUserID());
		Optional<List<Account>> accounts = accountService.getUserAccounts(user.getUserID());
		List<Account> unpacked = null;
		try {
			unpacked = accounts.get();
		}
		catch (NoSuchElementException e) {
			log.error("Error occured while trying to retrieve user info from database");
			System.out.println("There was an error in retrieving account information");
		}
		if (unpacked != null) {
			System.out.println("\nThe following account belonging to you were found:\n");
			System.out.println("Account ID\tBalance");
			for (Account a : unpacked) {
				System.out.println(a.getAccountID() + "\t\t" + a.getBalance());
			}
		}
		System.out.println("\nPlease hit ENTER to be taken back to the main menu.");
		s.nextLine();
		log.traceExit();
	}

	public void createAccount(Scanner s, User user) {
		log.traceEntry("User = {}", user.getUserID());
		System.out.println("\nPlease enter your password to confirm that you would like to create a new account.");
		System.out.print("\nPassword: ");
		String pass = s.nextLine();
		if (pass.equals(user.getPassword())) {
			System.out.println("\nA new account will be created for you.");
			Optional<Integer> newAcc = accountService.addAccount(user.getUserID());
			try {
				Integer newID = newAcc.get();
				System.out.println("\nThe ID for your new account is " + newID + ".");
				System.out.println("\nPlease keep this number for your records.");
				log.info("New account made with ID {}", newID);
			}
			catch (NoSuchElementException e) {
				log.error("Could not insert new account into database");
				System.out.println("\nThere was an error in processing your request.");
			}
		}
		else {
			System.out.println("\nA new account will not be created at this time.");
		}
		System.out.println("\nPlease hit ENTER to be taken back to the main menu.");
		s.nextLine();
		log.traceExit();
	}

	public void deleteAccount(Scanner s, User user) {
		log.traceEntry("User = {}", user.getUserID());
		System.out.println("\nPlease enter the ID for the account that you would like deleted.");
		System.out.print("Account ID: ");
		String id = s.nextLine();
		Integer numId = 0;
		try {
			numId = Integer.valueOf(id);
		}
		catch (NumberFormatException e) {
			log.warn("Non-integer account ID input");
			System.out.println("\nThat is not a valid Account ID");
		}
		if (numId != 0) {
			boolean owned = false;
			try {
				owned = validator.ownsAccount(numId, user.getUserID());
			}
			catch (AccountNotOwnedException e) {
				log.warn("Account {} does not belong to User {}", numId, user.getUserID());
				System.out.println("\nNo account owned by you could be found with that ID");
			}
			if (owned) {
				boolean empty = false;
				try {
					empty = validator.accountIsEmpty(numId);
				}
				catch (AccountNotEmptyException e) {
					log.warn("Account {} is not empty", numId);
					System.out.println("\nThe specified account is not empty and cannot be deleted.");
				}
				if (empty) {
					System.out.println("\nThe specified account has been located successfully.");
					System.out.println("\nPlease re-enter your password to verify account deletion.");
					System.out.print("Password: ");
					String pass = s.nextLine();
					if (pass.equals(user.getPassword())) {
						System.out.println("\nThe account " + id + " will be deleted.");
						accountService.removeAccount(numId);
						log.info("Account {} has been deleted", numId);
					}
					else {
						System.out.println("The account will not be deleted at this time.");
					}
				}
			}
		}
		System.out.println("\nPlease hit ENTER to be taken back to the main menu.");
		s.nextLine();	
		log.traceExit();
	}

	public void makeTransaction(Scanner s, User user) {
		log.traceEntry("User = {}", user.getUserID());
		boolean selected = false;
		while (!selected) {
			System.out.println("\nPlease select one of the following transaction types:");
			System.out.println("1. Deposit");
			System.out.println("2. Withdrawal");
			System.out.println("3. Transfer");
			System.out.print("Selection: ");
			String choice = s.nextLine().toLowerCase().trim();
			switch (choice) {
			case "1":
			case "1.":
			case "1. deposit":
			case "deposit":
				selected = true;
				makeDeposit(s, user);
				break;
			case "2":
			case "2.":
			case "2. withdrawal":
			case "withdrawal":
				selected = true;
				makeWithdrawal(s, user);
				break;
			case "3":
			case "3.":
			case "3. transfer":
			case "transfer":
				selected = true;
				makeTransfer(s, user);
				break;
			default:
				System.out.println("\nYour selection could not be resolved.");
				System.out.println("\nPlease use a number to indicate your choice of transaction.\n");
				break;
			}
		}
		System.out.println("\nPlease hit ENTER to be taken back to the main menu.");
		s.nextLine();
		log.traceExit();
	}

	private void makeTransfer(Scanner s, User user) {
		log.traceEntry("User = {}", user.getUserID());
		System.out.println("\nPlease input the account IDs of the accounts you would like to make a transfer between.");
		System.out.print("Account ID 1 (Source): ");
		String id1 = s.nextLine();
		System.out.print("Account ID 2 (Destination): ");
		String id2 = s.nextLine();
		Integer numId1 = 0;
		Integer numId2 = 0;
		Integer transId = 0;
		try {
			numId1 = Integer.valueOf(id1);
			numId2 = Integer.valueOf(id2);
		}
		catch (NumberFormatException e) {
			log.warn("Some account ID not valid");
			System.out.println("\nOne of the IDs specified is not a valid ID.");
		}
		if (numId1 != 0 && numId2 != 0) {
			boolean owned1 = false;
			boolean owned2 = false;
			try {
				owned1 = validator.ownsAccount(numId1, user.getUserID());
				owned2 = validator.ownsAccount(numId1, user.getUserID());
			}
			catch (AccountNotOwnedException e) {
				log.warn("Some account ID not owned by user {}", user.getUserID());
				System.out.println("\nNo account owned by you could be found for one of the IDs entered.");
			}
			if (owned1 & owned2) {
				System.out.println("\nPlease enter the amount you would like to transfer.");
				System.out.print("Amount: ");
				String amount = s.nextLine();
				Double numAmount = 0.0;
				try {
					numAmount = Double.valueOf(amount);
				}
				catch (NumberFormatException e) {
					log.warn("Invalid transaction amount");
					System.out.println("\nThat is not a valid amount.");
				}
				boolean sufficient = false;
				try {
					sufficient = validator.balanceIsSufficient(numId1, numAmount);
				}
				catch (InsufficientBalanceException e) {
					log.warn("Insufficient balance for transaction");
					System.out.println("\nThe source account does not contain sufficient funds for this withdrawal.");
				}
				if (numAmount > 0 && sufficient) {
					Optional<Integer> returnId = transactionService.issueTransfer(numId1, numId2, numAmount);
					try {
						transId = returnId.get();
					}
					catch (NoSuchElementException e) {
						log.error("Error occurred while trying to extract transaction info from database");
						System.out.println("\nThere was an error in processing this transaction.");
					}
				}
			}
		}
		if (transId == 0) {
			System.out.println("\nThe transaction will be canceled.");
			log.traceExit("Transaction canceled");
		}
		else {
			System.out.println("\nTransaction successful.");
			System.out.println("\nThe ID for this transaction is " + transId + ".");
			System.out.println("\nPlease keep this number for your records");
			log.traceExit("Successful transaction {}", transId);
		}
		
	}

	private void makeWithdrawal(Scanner s, User user) {
		log.traceEntry("User = {}", user.getUserID());
		System.out.println("\nPlease input the account ID of the account you would like to make a withdrawal from.");
		System.out.print("Account ID: ");
		String id = s.nextLine();
		Integer numId = 0;
		Integer transId = 0;
		try {
			numId = Integer.valueOf(id);
		}
		catch (NumberFormatException e) {
			log.warn("Invalid account ID entered");
			System.out.println("\nThat is not a valid Account ID.");
		}
		if (numId != 0) {
			boolean owned = false;
			try {
				owned = validator.ownsAccount(numId, user.getUserID());
			}
			catch (AccountNotOwnedException e) {
				log.warn("Account {} not owned by user {}", numId, user.getUserID());
				System.out.println("\nNo account owned by you could be found with that ID.");
			}
			if (owned) {
				System.out.println("\nPlease enter the amount you would like to withdraw.");
				System.out.print("Amount: ");
				String amount = s.nextLine();
				Double numAmount = 0.0;
				try {
					numAmount = Double.valueOf(amount);
				}
				catch (NumberFormatException e) {
					log.warn("Invalid amount entered");
					System.out.println("\nThat is not a valid amount.");
				}
				boolean sufficient = false;
				try {
					sufficient = validator.balanceIsSufficient(numId, numAmount);
				}
				catch (InsufficientBalanceException e) {
					log.warn("Account reports insufficient balance");
					System.out.println("\nThat account does not contain sufficient funds for this withdrawal.");
				}
				if (numAmount > 0 && sufficient) {
					Optional<Integer> returnId = transactionService.makeWithdrawal(numId, numAmount);
					try {
						transId = returnId.get();
					}
					catch (NoSuchElementException e) {
						log.error("Error occurred while trying to retrieve transaction info from database");
						System.out.println("\nThere was an error in processing this transaction.");
					}
				}
			}
		}
		if (transId == 0) {
			System.out.println("\nThe transaction will be canceled.");
			log.traceExit("Transaction canceled");
		}
		else {
			System.out.println("\nTransaction successful.");
			System.out.println("\nThe ID for this transaction is " + transId + ".");
			System.out.println("\nPlease keep this number for your records");
			log.traceExit("Successful transaction {}", transId);
		}
	}

	private void makeDeposit(Scanner s, User user) {
		log.traceEntry("User = {}", user.getUserID());
		System.out.println("\nPlease input the account ID of the account you would like to make a deposit for.");
		System.out.print("Account ID: ");
		String id = s.nextLine();
		Integer numId = 0;
		Integer transId = 0;
		try {
			numId = Integer.valueOf(id);
		}
		catch (NumberFormatException e) {
			log.warn("Invalid account ID entered");
			System.out.println("\nThat is not a valid Account ID.");
		}
		if (numId != 0) {
			boolean owned = false;
			try {
				owned = validator.ownsAccount(numId, user.getUserID());
			}
			catch (AccountNotOwnedException e) {
				log.warn("Account {} not owned by user {}", numId, user.getUserID());
				System.out.println("\nNo account owned by you could be found with that ID.");
			}
			if (owned) {
				System.out.println("\nPlease enter the amount you would like to deposit.");
				System.out.print("Amount: ");
				String amount = s.nextLine();
				Double numAmount = 0.0;
				try {
					numAmount = Double.valueOf(amount);
				}
				catch (NumberFormatException e) {
					log.warn("Invalid amount for transaction entered");
					System.out.println("\nThat is not a valid amount.");
				}
				if (numAmount > 0) {
					Optional<Integer> returnId = transactionService.makeDeposit(numId, numAmount);
					try {
						transId = returnId.get();
					}
					catch (NoSuchElementException e) {
						log.error("Error in trying to retrieve transaction info from database");
						System.out.println("\nThere was an error in processing this transaction.");
					}
				}
			}
		}
		if (transId == 0) {
			System.out.println("\nThe transaction will be canceled.");
			log.traceExit("Transaction canceled");
		}
		else {
			System.out.println("\nTransaction successful.");
			System.out.println("\nThe ID for this transaction is " + transId + ".");
			System.out.println("\nPlease keep this number for your records");
			log.traceExit("Successful transaction {}", transId);
		}
	}

	public String getUsername(Scanner s) {
		log.traceEntry();
		System.out.print("\nPlease enter username: ");
		return log.traceExit(s.nextLine());
	}

	public String getPassword(Scanner s) {
		System.out.print("Please enter password: ");
		return log.traceExit(s.nextLine());
	}
}
