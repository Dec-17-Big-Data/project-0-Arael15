package com.revature.bank.dao;

import java.util.List;
import java.util.Optional;

import com.revature.bank.model.User;

public interface UserDao {
	Optional<User> getUserByID(Integer id);
	Optional<User> getUserByName(String name);
	Optional<List<User>> getAllUsers();
	Optional<User> createUser(String firstName, String lastName, String userName, String password);
	Optional<Boolean> deleteUser(Integer id);
	Optional<Boolean> updateUser(Integer id, String update, String type);
}
