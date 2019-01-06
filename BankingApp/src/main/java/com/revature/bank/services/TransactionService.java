package com.revature.bank.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.dao.TransactionDao;
import com.revature.bank.dao.TransactionOracle;
import com.revature.bank.model.Transaction;

public class TransactionService {
	
	private static TransactionService transactionService;
	final static TransactionDao transactionDao = TransactionOracle.getTransactionOracle();
	private static final Logger log = LogManager.getLogger(TransactionService.class);
	
	private TransactionService() {
		
	}
	
	public static TransactionService getTransactionService() {
		if (transactionService == null) {
			transactionService = new TransactionService();
		}
		return transactionService;
	}
	
	public Optional<Transaction> getTransaction(Integer id) {
		log.traceEntry("id = {}", id);
		return log.traceExit(transactionDao.getTransaction(id));
	}
	
	public Optional<List<Transaction>> getAllTransactions() {
		log.traceEntry();
		return log.traceExit(transactionDao.getAllTransactions());
	}
	
	public Optional<List<Transaction>> getAllTransactionsForAccount(Integer account) {
		log.traceEntry("account = {}", account);
		return log.traceExit(transactionDao.getAllTransactionsForAccount(account));
	}
	
	public Optional<Integer> makeWithdrawal(Integer account, Double amount) {
		log.traceEntry("account = {}, amount = {}", account, amount);
		return log.traceExit(transactionDao.makeWithdrawal(account, amount));
	}
	
	public Optional<Integer> makeDeposit(Integer account, Double amount) {
		log.traceEntry("account = {}, amount = {}", account, amount);
		return log.traceExit(transactionDao.makeDeposit(account, amount));
	}
	
	public Optional<Integer> issueTransfer(Integer account1, Integer account2, Double amount) {
		log.traceEntry("account1 = {}, account2 = {}, amount = {}", account1, account2, amount);
		return log.traceExit(transactionDao.issueTransfer(account1, account2, amount));
	}
	

}
