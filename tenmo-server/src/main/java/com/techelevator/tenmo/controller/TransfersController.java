package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Transfer;

@RestController
public class TransfersController {
	private TransferDAO transferDAO;
	
	public TransfersController(TransferDAO transferDAO) {
		this.transferDAO = transferDAO;
	}

	
	@RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
	public boolean sendTransfer(@RequestBody Transfer transfer) {
		try {
			return transferDAO.sendTransfer(transfer);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
	public Transfer transferRequest(@RequestBody Transfer transfer) {
		
		return transferDAO.transferRequest(transfer);
	}
	
	@RequestMapping(path = "/transfer/update", method = RequestMethod.PUT)
	public boolean updateTransferStatus(@RequestBody Transfer transfer) {
		
		return transferDAO.updateTransferStatus(transfer);
	}
	
	@RequestMapping(path = "/transfer/update/balance", method = RequestMethod.PUT)
	public boolean updateBalanceFromTransfer(@RequestBody Transfer transfer) {
		
		return transferDAO.updateBalanceFromTransfer(transfer);
	}
	
	@RequestMapping(path = "/transfer/{userId}/history", method = RequestMethod.GET)
	public List<Transfer> history(@PathVariable int userId) {
		return transferDAO.history(userId);
	}
	
	@RequestMapping(path = "/transfer/{transferId}/details", method = RequestMethod.GET)
	public Transfer transferDetails(@PathVariable int transferId) {
		return transferDAO.transferDetails(transferId);
	}

	@RequestMapping(path = "/transfer/{accountId}/status", method = RequestMethod.GET)
	public List<Transfer> pending(@PathVariable int accountId) {
		return transferDAO.pending(accountId);
	}
	
	@RequestMapping(path = "transfer/{transferStatusId}/statusname", method = RequestMethod.GET)
	public String getTransferStatusName(@PathVariable int transferStatusId) {
		return transferDAO.getTransferStatusName(transferStatusId);
	}
	
	@RequestMapping(path = "transfer/{transferTypeId}/type", method = RequestMethod.GET)
	public String getTransferTypeName(@PathVariable int transferTypeId) {
		return transferDAO.getTransferTypeName(transferTypeId);
	}
}
