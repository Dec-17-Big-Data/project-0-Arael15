package com.revature.bank.services;

import java.util.List;
import java.util.Optional;

import com.revature.bank.dao.TransactionDao;
import com.revature.bank.dao.TransactionOracle;
import com.revature.bank.model.Transaction;

public class TransactionService {
	
	private static TransactionService transactionService;
	final static TransactionDao transactionDao = TransactionOracle.getTransactionOracle();
	
	private TransactionService() {
		
	}
	
	public static TransactionService getTransactionService() {
		if (transactionService == null) {
			transactionService = new TransactionService();
		}
		return transactionService;
	}
	
	public Optional<Transaction> getTransaction(Integer id) {
		return transactionDao.getTransaction(id);
	}
	
	public Optional<List<Transaction>> getAllTransactions() {
		return transactionDao.getAllTransactions();
	}
	
	public Optional<List<Transaction>> getAllTransactionsForAccount(Integer account) {
		return transactionDao.getAllTransactionsForAccount(account);
	}
	
	public Optional<Integer> makeWithdrawal(Integer account, Double amount) {
		return transactionDao.makeWithdrawal(account, amount);
	}
	
	public Optional<Integer> makeDeposit(Integer account, Double amount) {
		return transactionDao.makeDeposit(account, amount);
	}
	
	public Optional<Integer> issueTransfer(Integer account1, Integer account2, Double amount) {
		return transactionDao.issueTransfer(account1, account2, amount);
	}
	

}
