package com.revature.bank.dao;

import java.util.List;
import java.util.Optional;
import com.revature.bank.model.Account;

public interface AccountDao {
	Optional<Account> getAccount(Integer id);
	Optional<List<Account>> getUserAccounts(Integer user);
	Optional<List<Account>> getAllAccounts();
}
