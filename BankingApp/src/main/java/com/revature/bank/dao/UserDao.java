package com.revature.bank.dao;

import java.util.List;
import java.util.Optional;

import com.revature.bank.model.User;

public interface UserDao {
	Optional<User> getUserByID(Integer id);
	Optional<User> getUserByName(String name);
	Optional<List<User>> getAllUsers();
}
