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

import com.revature.bank.model.Account;
import com.revature.bank.model.User;
import com.revature.bank.util.ConnectionUtil;

public class AccountOracle implements AccountDao {

	private static AccountOracle accountOracle;
	private static final Logger log = LogManager.getLogger(AccountOracle.class);

	private AccountOracle() {
		
	}
	
	public static AccountOracle getAccountOracle() {
		if (accountOracle == null) {
			accountOracle = new AccountOracle();
		}
		return accountOracle;
	}
	
	public Optional<Account> getAccount(Integer id) {
		log.traceEntry("id = {}", id);
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}
		
		try {
			String sql = "select * from account where account_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			Account account = null;

			while(rs.next()) {
				account = new Account(rs.getInt("account_id"), rs.getDouble("balance"), rs.getInt("user_id"));
			}
			
			if (account == null) {
				log.traceExit(Optional.empty());
				return Optional.empty();
			}
			
			return log.traceExit(Optional.of(account));
			
		} catch (SQLException e) {
			log.catching(e);
			log.error("SQLExcpetion occurred", e);
		}
		
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

	public Optional<List<Account>> getUserAccounts(Integer user) {
		log.traceEntry();
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}

		try {
			String sql = "select * from account where user_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user);
			ResultSet rs = ps.executeQuery();

			List<Account> accounts = new ArrayList<Account>();
			while (rs.next()) {
				accounts.add(new Account(rs.getInt("account_id"), rs.getDouble("balance"), rs.getInt("user_id")));
			}
			return log.traceExit(Optional.of(accounts));
		} catch (SQLException e) {
			log.catching(e);
			log.error("SQLExcpetion occurred", e);
		}
		
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

	public Optional<List<Account>> getAllAccounts() {
		log.traceEntry();
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}

		try {
			String sql = "select * from account";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			List<Account> accounts = new ArrayList<Account>();
			while (rs.next()) {
				accounts.add(new Account(rs.getInt("account_id"), rs.getDouble("balance"), rs.getInt("user_id")));
			}
			return log.traceExit(Optional.of(accounts));
		} catch (SQLException e) {
			log.catching(e);
			log.error("SQLExcpetion occurred", e);
		}
		
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

}
