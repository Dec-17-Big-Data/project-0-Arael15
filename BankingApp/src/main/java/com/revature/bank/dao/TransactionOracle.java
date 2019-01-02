package com.revature.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.bank.model.Transaction;
import com.revature.bank.util.ConnectionUtil;

public class TransactionOracle implements TransactionDao {
	
	private static TransactionOracle transactionOracle;
	private static final Logger log = LogManager.getLogger(TransactionOracle.class);
	
	private TransactionOracle() {
		
	}
	
	public static TransactionOracle getTransactionOracle() {
		if (transactionOracle == null) {
			transactionOracle = new TransactionOracle();
		}
		return transactionOracle;
	}

	public Optional<Transaction> getTransaction(Integer id) {
		log.traceEntry("id = {}", id);
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}
		
		try {
			String sql = "select * from transaction where transaction_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			Transaction transaction = null;

			while(rs.next()) {
				transaction = new Transaction(rs.getInt("transaction_id"), rs.getInt("account1"),
						rs.getInt("account2"), rs.getString("transaction_type"), rs.getDouble("amount"), 
						rs.getTimestamp("transaction_time"));
			}
			
			if (transaction == null) {
				log.traceExit(Optional.empty());
				return Optional.empty();
			}
			
			return log.traceExit(Optional.of(transaction));
			
		} catch (SQLException e) {
			log.catching(e);
			log.error("SQLException occurred", e);
		}
		
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

	public Optional<List<Transaction>> getAllTransactions() {
		log.traceEntry();
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}

		try {
			String sql = "select * from transaction";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			List<Transaction> transactions = new ArrayList<Transaction>();
			while (rs.next()) {
				transactions.add(new Transaction(rs.getInt("transaction_id"), rs.getInt("account1"),
						rs.getInt("account2"), rs.getString("transaction_type"), rs.getDouble("amount"), 
						rs.getTimestamp("transaction_time")));
			}
			return log.traceExit(Optional.of(transactions));
		} catch (SQLException e) {
			log.catching(e);
			log.error("SQLException occurred", e);
		}
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

	public Optional<List<Transaction>> getAllTransactionsForAccount(Integer account) {
		log.traceEntry("id = {}", account);
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}

		try {
			String sql = "select * from transaction where account1 = ? or account2 = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, account);
			ps.setInt(2, account);
			
			ResultSet rs = ps.executeQuery();

			List<Transaction> transactions = new ArrayList<Transaction>();
			
			while (rs.next()) {
				transactions.add(new Transaction(rs.getInt("transaction_id"), rs.getInt("account1"),
						rs.getInt("account2"), rs.getString("transaction_type"), rs.getDouble("amount"), 
						rs.getTimestamp("transaction_time")));
			}
			return log.traceExit(Optional.of(transactions));
		} catch (SQLException e) {
			log.catching(e);
			log.error("SQLException occurred", e);
		}
		
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

}
