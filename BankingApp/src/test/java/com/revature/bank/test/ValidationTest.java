package com.revature.bank.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.bank.exceptions.AccountNotEmptyException;
import com.revature.bank.exceptions.AccountNotOwnedException;
import com.revature.bank.exceptions.InsufficientBalanceException;
import com.revature.bank.exceptions.InvalidUsernameException;
import com.revature.bank.exceptions.PasswordMismatchException;
import com.revature.bank.exceptions.UserNotFoundException;
import com.revature.bank.services.ValidationService;

public class ValidationTest {
	
	private static ValidationService validator;
	
	@BeforeClass
	public static void setup() {
		validator = ValidationService.getValidationService();
	}
	

	@Test
	public void superUserTest() {
		assertTrue(validator.isSuperUser("jdbcbank", "IAmSuperUser"));
	}
	
	@Test
	public void notSuperUserTest() {
		assertFalse(validator.isSuperUser("NotASuperUser", "Whatever"));
	}
	
	@Test
	public void uniqueUserTest() {
		assertTrue(validator.userIsUnique("I_AM_UNIQUE"));
	}
	
	@Test (expected = InvalidUsernameException.class)
	public void notUniqueUserTest() {
		validator.userIsUnique("StarPlatinum");
	}
	
	@Test
	public void userExistsName() {
		assertTrue(validator.userExists("StarPlatinum"));
	}
	
	@Test
	public void userExistsID() {
		assertTrue(validator.userExists(1));
	}
	
	@Test (expected = UserNotFoundException.class)
	public void userNotExistsName() {
		validator.userExists("I_DO_NOT_EXIST");
	}
	
	@Test (expected = UserNotFoundException.class)
	public void userNotExistsID() {
		validator.userExists(0);
	}
	
	@Test
	public void goodPasswordTest() {
		assertTrue(validator.passwordIsCorrect("HermitPurple", "OhMyGod"));
	}
	
	@Test (expected = PasswordMismatchException.class)
	public void badPasswordTest() {
		validator.passwordIsCorrect("StarPlatinum", "WrongPassword");
	}
	
	@Test (expected = PasswordMismatchException.class)
	public void passwordNoUserTest() {
		validator.passwordIsCorrect("I_DO_NOT_EXIST", "Something");
	}
	
	@Test
	public void sufficientBalanceTest() {
		assertTrue(validator.balanceIsSufficient(1, 1.0));
	}
	
	@Test (expected = InsufficientBalanceException.class)
	public void notSufficientBalanceTest() {
		validator.balanceIsSufficient(1, Double.MAX_VALUE);
	}
	
	@Test (expected = InsufficientBalanceException.class)
	public void noAccountBalanceTest() {
		validator.balanceIsSufficient(0, 1.0);
	}
	
	@Test
	public void accountOwnedTest() {
		assertTrue(validator.ownsAccount(1, 1));
	}
	
	@Test (expected = AccountNotOwnedException.class)
	public void accountNotOwnedTest() {
		validator.ownsAccount(1, 0);
	}
	
	@Test (expected = AccountNotOwnedException.class)
	public void accountNoUserOwnedTest() {
		validator.ownsAccount(0, 1);
	}
	
	@Test
	public void accountEmptyTest() {
		assertTrue(validator.accountIsEmpty(2));
	}
	
	@Test (expected = AccountNotEmptyException.class)
	public void noAccountEmptyTest() {
		validator.accountIsEmpty(0);
	}
	
	@Test (expected = AccountNotEmptyException.class)
	public void accountNotEmptyTest() {
		validator.accountIsEmpty(1);
	}
	
	@Test
	public void validUserTest() {
		assertTrue(validator.isValidUser("ZaWarudo", "RoadRollerDa"));
	}
	
	@Test
	public void notValidUserTest() {
		assertFalse(validator.isValidUser("NOT_VALID", "OH_REALLLY"));
	}
}
