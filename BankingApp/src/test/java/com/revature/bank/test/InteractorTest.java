package com.revature.bank.test;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.Scanner;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.bank.model.User;
import com.revature.bank.util.Interactor;

public class InteractorTest {

	private static Interactor interactor;
	
	@BeforeClass
	public static void setup() {
		interactor = Interactor.getInteractor();
	}
	
	@Test
	public void getUsernameTest() {
		Scanner s = new Scanner(new StringReader("MyUsername"));
		assertEquals(interactor.getUsername(s), "MyUsername");
		s.close();
	}
	
	@Test
	public void getPasswordTest() {
		Scanner s = new Scanner(new StringReader("MyPassword"));
		assertEquals(interactor.getPassword(s), "MyPassword");
		s.close();
	}
	
	@Test
	public void greetLoginTest() {
		Scanner s = new Scanner(new StringReader("3\n1"));
		assertFalse(interactor.greet(s));
		s.close();
	}
	
	@Test
	public void greetRegisterTest() {
		Scanner s = new Scanner(new StringReader("3\n2"));
		assertTrue(interactor.greet(s));
		s.close();
	}
	
	@Test
	public void registerNewUserTest() {
		Scanner s = new Scanner(new StringReader("StarPlatinum\n\nIAmNewUser\n\nasdf\n\nNew\n\nUser\n"));
		assertFalse(interactor.register(s));
		s.close();
	}
	
	@Test
	public void retrieveUsertest() {
		User expected = new User("StarPlatinum", "HesOnly17", "Jotaro", "Kujo", 1);
		assertEquals(expected, interactor.retrieveUserInfo("StarPlatinum"));
	}
	
	@Test
	public void retrieveNoUserTest() {
		assertEquals(null, interactor.retrieveUserInfo("badUser"));
	}
	
	@Test
	public void selecteMenuOptionTest() {
		Scanner s = new Scanner(new StringReader("7\n1  "));
		assertTrue(interactor.selectMenuOption(s) == 1);
		s.close();
	}	
	
}
