package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {
	
	Account getAccount(int userId);

	int getUserIdFromAccountId(int accountId);
	
	
}
