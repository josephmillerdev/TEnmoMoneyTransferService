package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;

@Component
public class JdbcAccountDAO implements AccountDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	@Override
	public Account getAccount(int userId) {
		Account account = new Account();
		String sql = "SELECT * FROM accounts WHERE user_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		
		if (results.next()) {
			account = mapRowToAccounts(results);
		}
		return account;
	}
	
	@Override 
	public int getUserIdFromAccountId(int accountId) {
		int userId = 0;
		String sql = "SELECT * FROM accounts WHERE account_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
		
		if (results.next()) {
			userId = results.getInt("user_id");	
		}
		return userId;
	}
	
	private Account mapRowToAccounts(SqlRowSet rs) {
		Account account = new Account(); 
		
		account.setAccountId(rs.getInt("account_id"));
		account.setUserId(rs.getInt("user_id"));
		account.setBalance(rs.getDouble("balance"));
		return account;
	}
}

	

