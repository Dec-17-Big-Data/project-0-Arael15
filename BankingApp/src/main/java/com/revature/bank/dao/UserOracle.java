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

import com.revature.bank.model.User;
import com.revature.bank.util.ConnectionUtil;

public class UserOracle implements UserDao {
	private static UserOracle userOracle;
	private static final Logger log = LogManager.getLogger(UserOracle.class);

	private UserOracle() {
		
	}
	
	public static UserOracle getUserOracle() {
		if (userOracle == null) {
			userOracle = new UserOracle();
		}
		return userOracle;
	}
	
	public Optional<User> getUserByID(Integer id) {
		log.traceEntry("id = {}", id);
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}
		
		try {
			String sql = "select * from users where user_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			User user = null;

			while(rs.next()) {
			user = new User(rs.getString("username"), rs.getString("user_password"), rs.getString("first_name"),
					rs.getString("last_name"), rs.getInt("user_id"));
			}
			
			if (user == null) {
				log.traceExit(Optional.empty());
				return Optional.empty();
			}
			
			return log.traceExit(Optional.of(user));
			
		} catch (SQLException e) {
			log.catching(e);
			log.error("SQLExcpetion occurred", e);
		}
		
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

	public Optional<User> getUserByName(String name) {
		log.traceEntry("name = {}", name);
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}
		
		try {
			String sql = "select * from users where username = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			
			User user = null;

			while(rs.next()) {
				user = new User(rs.getString("username"), rs.getString("user_password"), rs.getString("first_name"),
						rs.getString("last_name"), rs.getInt("user_id"));
			}
			
			if (user == null) {
				log.traceExit(Optional.empty());
				return Optional.empty();
			}
			
			return log.traceExit(Optional.of(user));
			
		} catch (SQLException e) {
			log.catching(e);
			log.error("SQLExcpetion occurred", e);
		}
		
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

	public Optional<List<User>> getAllUsers() {
		log.traceEntry();
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}

		try {
			String sql = "select * from users";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			List<User> users = new ArrayList<User>();
			while (rs.next()) {
				users.add(new User(rs.getString("username"), rs.getString("user_password"), rs.getString("first_name"),
						rs.getString("last_name"), rs.getInt("user_id")));
			}
			return log.traceExit(Optional.of(users));
		} catch (SQLException e) {
			log.catching(e);
			log.error("SQLException occurred", e);
		}
		
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

	public Optional<User> createUser(String firstName, String lastName, String userName, String password) {
		log.traceEntry();
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}
		
		try {
			String sql = "call add_user(?, ?, ?, ?, ?)";
			CallableStatement cs = con.prepareCall(sql);
			cs.setString(1, firstName);
			cs.setString(2, lastName);
			cs.setString(3, userName);
			cs.setString(4, password);
			cs.registerOutParameter(5, java.sql.Types.INTEGER);
			
			cs.executeUpdate();
			
			User user = new User(userName, password, firstName, lastName, cs.getInt(5));
			
			return log.traceExit(Optional.of(user));
		}
		catch (SQLException e) {
			log.catching(e);
			log.error("SQLException occurred", e);
		}
		
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

	public Optional<Boolean> deleteUser(Integer id) {
		log.traceEntry("id = {}", id);
		Connection con = ConnectionUtil.getConnection();

		if (con == null) {
			log.traceExit(Optional.empty());
			return Optional.empty();
		}
		
		try {
			String sql = "call remove_user(?)";
			CallableStatement cs = con.prepareCall(sql);
			cs.setInt(1, id);
			
			boolean result = cs.execute();
			
			return log.traceExit(Optional.of(result));
		}
		catch (SQLException e) {
			log.catching(e);
			log.error("SQLException occurred", e);
		}
		
		log.traceExit(Optional.empty());
		return Optional.empty();
	}

}
