package com.revature.bank.dao;

import java.util.List;
import java.util.Optional;

import com.revature.bank.model.Transaction;

public interface TransactionDao {
	Optional<Transaction> getTransaction(Integer id);
	Optional<List<Transaction>> getAllTransaction();
	Optional<List<Transaction>> getAllTransactionsForAccount(Integer account);
}
