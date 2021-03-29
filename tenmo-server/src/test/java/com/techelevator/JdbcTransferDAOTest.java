package com.techelevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;

public class JdbcTransferDAOTest {
	
	private static SingleConnectionDataSource dataSource;
	private static TransferDAO dao;
	
	private static final Transfer transfer = new Transfer(777, 2, 2, 999, 888, 777.0);

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
		String sqlInsertTransfer = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
									"VALUES (777, 2, 2, 999, 888, 777)";
		
		String sqlInsertUserTo = "INSERT INTO users(user_id, username, password_hash) VALUES (888, 555, 555)";
		String sqlInsertAccountTo = "INSERT INTO accounts (account_id, user_id, balance) VALUES (888, 888, 888.00)";
		String sqlInsertUserFrom = "INSERT INTO users(user_id, username, password_hash) VALUES (999, 666, 666)";
		String sqlInsertAccountFrom = "INSERT INTO accounts (account_id, user_id, balance) VALUES (999, 999, 999.00)";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertUserTo);
		jdbcTemplate.update(sqlInsertAccountTo);
		jdbcTemplate.update(sqlInsertUserFrom);
		jdbcTemplate.update(sqlInsertAccountFrom);
		jdbcTemplate.update(sqlInsertTransfer);
		
		dao = new JdbcTransferDAO(jdbcTemplate);
	}

	@AfterEach
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	
	@Test
	public void transferRequest() {
		Transfer results = new Transfer();
		
		results = dao.transferRequest(transfer);
		
		assertNotNull(results);
		assertEquals(transfer.getTransferId(), results.getTransferId());
		assertEquals(transfer.getTransferStatusId(), results.getTransferStatusId());
		assertEquals(transfer.getTransferType(), results.getTransferType());
		assertEquals(transfer.getFromAccountId(), results.getFromAccountId());
		assertEquals(transfer.getToAccountId(), results.getToAccountId());
		assertEquals(transfer.getAmount(), results.getAmount());
		
	}
	


}