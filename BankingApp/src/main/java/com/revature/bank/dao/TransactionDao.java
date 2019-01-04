package com.revature.bank.dao;

import java.util.List;
import java.util.Optional;

import com.revature.bank.model.Transaction;

public interface TransactionDao {
	Optional<Transaction> getTransaction(Integer id);
	Optional<List<Transaction>> getAllTransactions();
	Optional<List<Transaction>> getAllTransactionsForAccount(Integer account);
	Optional<Integer> makeWithdrawal(Integer account, Double amount);
	Optional<Integer> makeDeposit(Integer account, Double amount);
	Optional<Integer> issueTransfer(Integer account1, Integer account2, Double amount);
}
