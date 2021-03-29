package com.techelevator;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;
import java.text.DecimalFormat;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JdbcAccountDAO;
import com.techelevator.tenmo.model.Account;

public class JdbcAccountDAOTest {

	private static SingleConnectionDataSource dataSource;
	private static AccountDAO dao;

	private static final Account testAccount = new Account(888, 888, 888);

	

	@BeforeAll
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterAll
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@BeforeEach
	public void setup() {
		String sqlInsertUser = "INSERT INTO users(user_id, username, password_hash) VALUES (888, 555, 555)";
		String sqlInsertAccount = "INSERT INTO accounts (account_id, user_id, balance) VALUES (888, 888, 888.00)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertUser);
		jdbcTemplate.update(sqlInsertAccount);
		
		dao = new JdbcAccountDAO(jdbcTemplate);
		
	}

	@AfterEach
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	
	@Test
	public void getAccount() {
		Account results = new Account();
		
		results = dao.getAccount(testAccount.getUserId());
		
		assertNotNull(results);
		assertEquals(testAccount.getBalance(), (results.getBalance()));
		assertEquals(testAccount.getAccountId(), results.getAccountId());
		assertEquals(testAccount.getUserId(), results.getUserId());
	}
	
	@Test
	public void getUserIdFromAccountId() {
		int userId = 0;
		
		userId = dao.getUserIdFromAccountId(testAccount.getAccountId());
		
		assertNotNull(userId);
		assertEquals(testAccount.getUserId(), userId);
	}
}
