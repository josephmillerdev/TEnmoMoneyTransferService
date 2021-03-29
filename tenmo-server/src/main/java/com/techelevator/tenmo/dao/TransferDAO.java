package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

public interface TransferDAO {

	Transfer transferRequest(Transfer transfer);
	
	boolean updateTransferStatus(Transfer transfer);
	
	List<Transfer> history(int account_id);
	
	List<Transfer> pending(int account_id);
	
	boolean sendTransfer(Transfer transfer);
	
	boolean updateBalanceFromTransfer(Transfer transfer);
	
	Transfer transferDetails(int transferId);
	
	String getTransferStatusName(int transferStatusId);
	
	String getTransferTypeName(int transferTypeId);
	
	double getBalance(Long id);

}