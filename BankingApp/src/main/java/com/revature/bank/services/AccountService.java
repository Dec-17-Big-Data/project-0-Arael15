package com.revature.bank.services;

import java.util.List;
import java.util.Optional;

import com.revature.bank.dao.AccountDao;
import com.revature.bank.dao.AccountOracle;
import com.revature.bank.model.Account;

public class AccountService {

	private static AccountService accountService;
	final static AccountDao accountDao = AccountOracle.getAccountOracle();
	
	private AccountService() {
		
	}
	
	public static AccountService getAccountService() {
		if (accountService == null) {
			accountService = new AccountService();
		}
		return accountService;
	}
	
	public Optional<Account> getAccount(Integer id) {
		return null;
	}
	
	public Optional<List<Account>> getUserAccounts(Integer user) {
		return null;
	}
	
	public Optional<List<Account>> getAllAccounts() {
		return null;
	}
	
	public Optional<Boolean> removeAccount(Integer id) {
		return null;
	}
	
	public Optional<Integer> addAccount(Integer id) {
		return null;
	}
	
	
	
}
