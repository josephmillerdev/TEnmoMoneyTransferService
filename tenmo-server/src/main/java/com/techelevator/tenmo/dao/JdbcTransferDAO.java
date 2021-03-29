package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

@Component
public class JdbcTransferDAO implements TransferDAO {
	
	private JdbcTemplate jdbcTemplate;

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

	@Override
	public Transfer transferRequest(Transfer transfer) {
		
		String sql = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, "
				+ "account_to, amount) VALUES(?, ?, ?, ?, ?, ?)";
		transfer.setTransferId(getNextId());
		
		jdbcTemplate.update(sql, transfer.getTransferId(), 1, 1, transfer.getFromAccountId(), 
				transfer.getToAccountId(), transfer.getAmount());
		
		return transfer;
	}
	
	@Override
	public boolean sendTransfer(Transfer transfer) {
		String sql = "INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, " + 
				"account_to, amount) VALUES(?, ?, ?, ?, ?, ?)";
		transfer.setTransferId(getNextId());
		jdbcTemplate.update(sql, transfer.getTransferId(), 2, 2, transfer.getFromAccountId(),
				transfer.getToAccountId(), transfer.getAmount());
		
		String sqlUpdateFromAccount = "UPDATE accounts SET balance = (accounts.balance - ?) FROM transfers WHERE accounts.account_id = transfers.account_from AND transfers.account_from = ?";
		jdbcTemplate.update(sqlUpdateFromAccount, transfer.getAmount(), transfer.getFromAccountId());
		
		String sqlUpdateToAccount = "UPDATE accounts SET balance = (accounts.balance + ?) FROM transfers WHERE accounts.account_id = transfers.account_to AND transfers.account_to = ?";
		jdbcTemplate.update(sqlUpdateToAccount, transfer.getAmount(), transfer.getToAccountId());
		return true;
	}
	
	public boolean updateTransferStatus(Transfer transfer) {
		String sql = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?";
		jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
		
		return true;
	}
	
	public boolean updateBalanceFromTransfer(Transfer transfer) {
		String sqlUpdateFromAccount = "UPDATE accounts SET balance = (accounts.balance - ?) FROM transfers WHERE accounts.account_id = transfers.account_from AND transfers.account_from = ?";
		jdbcTemplate.update(sqlUpdateFromAccount, transfer.getAmount(), transfer.getFromAccountId());
		
		String sqlUpdateToAccount = "UPDATE accounts SET balance = (accounts.balance + ?) FROM transfers WHERE accounts.account_id = transfers.account_to AND transfers.account_to = ?";
		jdbcTemplate.update(sqlUpdateToAccount, transfer.getAmount(), transfer.getToAccountId());
		
		return true;
	}
		
	public List<Transfer> history(int userId) {
		List<Transfer> transHist = new ArrayList<>();
		String sql = "SELECT * FROM transfers JOIN accounts ON transfers.account_from = accounts.account_id "
				+ "OR transfers.account_to = accounts.account_id WHERE accounts.user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		
		while (results.next()) {
			Transfer temp = mapRowToTransfers(results);
			transHist.add(temp);
			}
			return transHist;
	}
	
	@Override
	public Transfer transferDetails(int transferId) {
		Transfer transfer = null;
		String sql = "SELECT * FROM transfers WHERE transfer_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
		
		if(results.next()) {
			transfer = mapRowToTransfers(results);
		}
		return transfer;
	}

	@Override
	public List<Transfer> pending(int account_id) {
		List<Transfer> pending = new ArrayList<>();
		String sql = "SELECT * FROM transfers WHERE (account_from = ? OR account_to = ?) AND transfer_status_id = 1";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, account_id, account_id);
		
		while (results.next()) {
		Transfer temp = mapRowToTransfers(results);
		pending.add(temp);
		}
		return pending;
	}
	
	@Override
	public String getTransferStatusName(int transferId) {
		String status = "";
		String sql = "SELECT * FROM transfer_statuses WHERE transfer_status_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
		
		if (results.next()) {
			status = results.getString("transfer_status_desc"); 
		}
		return status;
	}
	
	@Override
	public String getTransferTypeName(int transferTypeId) {
		String transferType = "";
		String sql = "SELECT * FROM transfer_types WHERE transfer_type_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferTypeId);
		
		if (results.next()) {
			transferType = results.getString("transfer_type_desc"); 
		}
		return transferType;
	}
	
	private Transfer mapRowToTransfers(SqlRowSet rs) {
		Transfer transfer = new Transfer();
		
		transfer.setTransferId(rs.getInt("transfer_id"));
		transfer.setTransferType(rs.getInt("transfer_type_id"));
		transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
		transfer.setFromAccountId(rs.getInt("account_from"));
		transfer.setToAccountId(rs.getInt("account_to"));
		transfer.setAmount(rs.getDouble("amount"));
		
		return transfer;		
	}
	
	
	private int getNextId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval ('seq_transfer_id')");
		
		if (nextIdResult.next()) {
			return nextIdResult.getInt(1);
		}
		throw new RuntimeException("Error in getNextTransferId");
	}

	@Override
	public double getBalance(Long id) {
		String sql = "SELECT balance FROM accounts WHERE user_id = ?";
		double balance = jdbcTemplate.queryForObject(sql, double.class, id);
		return balance;
	}
	


}