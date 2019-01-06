package com.revature.bank.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.dao.AccountDao;
import com.revature.bank.dao.AccountOracle;
import com.revature.bank.exceptions.AccountNotEmptyException;
import com.revature.bank.model.Account;

public class AccountService {

	private static AccountService accountService;
	final static AccountDao accountDao = AccountOracle.getAccountOracle();
	final static ValidationService validationService = ValidationService.getValidationService();
	private static final Logger log = LogManager.getLogger(AccountService.class);
	
	private AccountService() {
		
	}
	
	public static AccountService getAccountService() {
		if (accountService == null) {
			accountService = new AccountService();
		}
		return accountService;
	}
	
	public Optional<Account> getAccount(Integer id) {
		log.traceEntry("id = {}", id);
		return log.traceExit(accountDao.getAccount(id));
	}
	
	public Optional<List<Account>> getUserAccounts(Integer user) {
		log.traceEntry("user = {}", user);
		return log.traceExit(accountDao.getUserAccounts(user));
	}
	
	public Optional<List<Account>> getAllAccounts() {
		log.traceEntry();
		return log.traceExit(accountDao.getAllAccounts());
	}
	
	public Optional<Boolean> removeAccount(Integer id) {
		log.traceEntry("account id = {}", id);
		return log.traceExit(accountDao.removeAccount(id));
	}
	
	public Optional<Integer> addAccount(Integer id) {
		log.traceEntry("user id = {}", id);
		return log.traceExit(accountDao.addAccount(id));
	}
	
	
	
}
