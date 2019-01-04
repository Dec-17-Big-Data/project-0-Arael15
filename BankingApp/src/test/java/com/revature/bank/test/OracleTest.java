package com.revature.bank.test;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.bank.dao.AccountDao;
import com.revature.bank.dao.AccountOracle;
import com.revature.bank.dao.TransactionDao;
import com.revature.bank.dao.TransactionOracle;
import com.revature.bank.dao.UserDao;
import com.revature.bank.dao.UserOracle;
import com.revature.bank.model.Account;
import com.revature.bank.model.Transaction;
import com.revature.bank.model.User;

//	This file was made to verify that the Oracle implementations correctly executed
//	the PL/SQL commands for the database. They should not generally be run except against
//	known data in the database, and the inputs for functions ought to be adjusted accordingly.
//	Additionally, since some of the tests require inserting/deleting rows into tables
//	and others check against the size of tables, running these tests simultaneously is a
//  known source of failing tests.

public class OracleTest {
	
	private static UserDao userOracle;
	private static AccountDao accountOracle;
	private static TransactionDao transactionOracle;
	private static Logger log = LogManager.getLogger(OracleTest.class);

	//	Instantiate the oracles once
	@BeforeClass public static void initialize() {
		log.info("NEW PROGRAM EXECUTION");
		log.info("Beginning Oracle Tests");
		userOracle = UserOracle.getUserOracle();
		accountOracle = AccountOracle.getAccountOracle();
		transactionOracle = TransactionOracle.getTransactionOracle();
	}
	
	@AfterClass public static void finish() {
		log.info("Oracle Tests Concluded" + System.lineSeparator());
	}
	
	//	Tests for UserOracle
	
	@Test
	public void userIDLookupTest() {
		User u = new User("HermitPurple", "OhMyGod", "Joseph", "Joestar", 2);
		Optional<User> returned = userOracle.getUserByID(2);
		assertEquals(u, returned.get());
	}
	
	@Test
	public void userNameLookupTest() {
		User u = new User("HierophantGreen", "EmeraldSplash", "Noriaki", "Kakyoin", 3);
		Optional<User> returned = userOracle.getUserByName("HierophantGreen");
		assertEquals(u, returned.get());
	}
	
	@Test (expected = NoSuchElementException.class)
	public void userIDLookupNoSuchIDTest() {
		userOracle.getUserByID(-1).get();
	}
	
	@Test (expected = NoSuchElementException.class)
	public void userNameLookupNoSuchNameTest() {
		userOracle.getUserByName("RobSchneider").get();
	}
	
	@Test
	public void retrieveAllUsersTest() {
		User u1 = new User("HermitPurple", "OhMyGod", "Joseph", "Joestar", 2);
		User u2 = new User("HierophantGreen", "EmeraldSplash", "Noriaki", "Kakyoin", 3);
		User u3 = new User("MagiciansRed", "YesIAm", "Mohammed", "Avdol", 4);
		User u4 = new User("SilverChariot", "NowJustATurtle", "Jean-Pierre", "Polnareff", 5);
		User u5 = new User("ZaWarudo", "RoadRollerDa", "Dio", "Brando", 6);
		Optional<List<User>> returned = userOracle.getAllUsers();
		List<User> users = returned.get();
		assertEquals(u1, users.get(0));
		assertEquals(u2, users.get(1));
		assertEquals(u3, users.get(2));
		assertEquals(u4, users.get(3));
		assertEquals(u5, users.get(4));
	}
	
	@Test
	public void addUserTest() {
		Optional<User> user = userOracle.createUser("Johnny", "Joestar", "CantWalk", "HamonForever");
		assertTrue(user.get().getUserID() > 6);
	}
	
	@Test
	public void removeUserTest() {
		Optional<Boolean> result = userOracle.deleteUser(7);
		assertFalse(result.get());
	}
	
	//	Tests for AccountOracle
	
	@Test
	public void accountLookupTest() {
		Account expected = new Account(9, 0, 5);
		Optional<Account> actual = accountOracle.getAccount(9);
		assertEquals(expected, actual.get());
	}
	
	@Test (expected = NoSuchElementException.class)
	public void noSuchAccountLookupTest() {
		accountOracle.getAccount(0).get();
	}
	
	@Test
	public void accountLookupForUserTest() {
		Account expected1 = new Account(5, 400, 3);
		Account expected2 = new Account(6, 400, 3);
		Optional<List<Account>> returned = accountOracle.getUserAccounts(3);
		List<Account> actual = returned.get();
		assertEquals(expected1, actual.get(0));
		assertEquals(expected2, actual.get(1));
		assertTrue(actual.size() == 2);
	}
	
	@Test
	public void accountLookupForUserWithNoAccountsTest() {
		Optional<List<Account>> returned = accountOracle.getUserAccounts(2);
		List<Account> actual = returned.get();
		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void accountLookupForNonexistentUserTest() {
		Optional<List<Account>> returned = accountOracle.getUserAccounts(1);
		List<Account> actual = returned.get();
		assertTrue(actual.isEmpty());
	}
	
	@Test
	public void getAllAccountsTest() {
		Optional<List<Account>> returned = accountOracle.getAllAccounts();
		List<Account> actual = returned.get();
		assertEquals(actual.size(), 8);
	}
	
	@Test
	public void removeAccountTest() {
		Optional<Boolean> result = accountOracle.removeAccount(7);
		assertFalse(result.get());
	}
	
	@Test
	public void addAccountTest() {
		Optional<Integer> account = accountOracle.addAccount(4);
		assertTrue(account.get() > 10);
	}
	
	//Tests for TransactionOracle
	
	@Test
	public void transactionLookupTest() {
		Transaction expected = new Transaction(23, 7, 8, "transfer", 111.11, new Date());
		Optional<Transaction> actual = transactionOracle.getTransaction(23);
		expected.setTransTime(actual.get().getTransTime());
		assertEquals(expected, actual.get());
	}
	
	@Test (expected = NoSuchElementException.class)
	public void transactionLookupDoesNotExistTest() {
		transactionOracle.getTransaction(0).get();
	}
	
	@Test
	public void accountTransactionsLookupTest() {
		Optional<List<Transaction>> trans = transactionOracle.getAllTransactionsForAccount(8);
		assertEquals(trans.get().size(), 2);
	}
	
	@Test
	public void accountTransactionsLookupNoTransactionsTest() {
		Optional<List<Transaction>> trans = transactionOracle.getAllTransactionsForAccount(9);
		assertTrue(trans.get().isEmpty());
	}
	
	@Test
	public void accountTransactionsLookupNoSuchAccountTest() {
		Optional<List<Transaction>> trans = transactionOracle.getAllTransactionsForAccount(3);
		assertTrue(trans.get().isEmpty());
	}
	
	@Test
	public void getAllTransactionsTest() {
		Optional<List<Transaction>> trans = transactionOracle.getAllTransactions();
		assertEquals(trans.get().size(), 16);
	}
	
	@Test
	public void makeWithdrawalTest() {
		Optional<Integer> trans = transactionOracle.makeWithdrawal(5, 5.00);
		assertTrue(trans.get() > 10);
	}
	
	@Test
	public void makeDepositTest() {
		Optional<Integer> trans = transactionOracle.makeDeposit(5, 5.00);
		assertTrue(trans.get() > 10);
	}
	
	@Test
	public void issueTransferTest() {
		Optional<Integer> trans = transactionOracle.issueTransfer(5, 6, 5.00);
		assertTrue(trans.get() > 10);
	}

}
