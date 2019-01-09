package com.revature.bank.dao;

import java.sql.CallableStatement;
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

	public Optional<Integer> makeWithdrawal(Integer account, Double amount) {
		log.traceEntry();
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}
		
		try {
			String sql = "call make_withdrawal(?, ?, ?)";
			CallableStatement cs = con.prepareCall(sql);
			cs.setInt(1, account);
			cs.setDouble(2,  amount);
			cs.registerOutParameter(3, java.sql.Types.INTEGER);
			
			cs.executeUpdate();
			
			Integer i = cs.getInt(3);
			return log.traceExit(Optional.of(i));
		}
		catch (SQLException e) {
			log.catching(e);
			log.error("SQLException occurred", e);
		}

		log.traceExit(Optional.empty());
		return Optional.empty();
	}

	public Optional<Integer> makeDeposit(Integer account, Double amount) {
		log.traceEntry();
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}
		
		try {
			String sql = "call make_deposit(?, ?, ?)";
			CallableStatement cs = con.prepareCall(sql);
			cs.setInt(1, account);
			cs.setDouble(2,  amount);
			cs.registerOutParameter(3, java.sql.Types.INTEGER);
			
			cs.executeUpdate();
			
			Integer i = cs.getInt(3);
			return log.traceExit(Optional.of(i));
		}
		catch (SQLException e) {
			log.catching(e);
			log.error("SQLException occurred", e);
		}

		log.traceExit(Optional.empty());
		return Optional.empty();
	}

	public Optional<Integer> issueTransfer(Integer account1, Integer account2, Double amount) {
		log.traceEntry();
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}
		
		try {
			String sql = "call issue_transfer(?, ?, ?, ?)";
			CallableStatement cs = con.prepareCall(sql);
			cs.setInt(1, account1);
			cs.setInt(2, account2);
			cs.setDouble(3,  amount);
			cs.registerOutParameter(4, java.sql.Types.INTEGER);
			
			cs.executeUpdate();
			
			Integer i = cs.getInt(4);
			return log.traceExit(Optional.of(i));
		}
		catch (SQLException e) {
			log.catching(e);
			log.error("SQLException occurred", e);
		}

		log.traceExit(Optional.empty());
		return Optional.empty();
	}

}
