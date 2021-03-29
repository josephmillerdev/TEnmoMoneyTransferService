package com.techelevator.tenmo.model;

public class Transfer {
	private int transferId;
	private int transferStatusId;
	private int fromAccountId;
	private int toAccountId;
	private int fromUserId;
	private int toUserId;
	private String fromUsername;
	private String toUsername;
	private Double amount;
	private int transferType;
	
	public Transfer() {
		
	}
	
	public Transfer(int transferId, int transferType, int transferStatusId, int fromAccountId, 
			 int toAccountId, Double amount) {
		
		this.transferId = transferId;
		this.transferType = transferType;
		this.transferStatusId = transferStatusId;
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
		
	}
	public int getTransferId() {
		return transferId;
	}
	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}
	public int getFromAccountId() {
		return fromAccountId;
	}
	public void setFromAccountId(int fromAccountId) {
		this.fromAccountId = fromAccountId;
	}
	public int getToAccountId() {
		return toAccountId;
	}
	public void setToAccountId(int toAccountId) {
		this.toAccountId = toAccountId;
	}
	public int getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}
	public int getToUserId() {
		return toUserId;
	}
	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}
	public String getFromUsername() {
		return fromUsername;
	}
	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}
	public String getToUsername() {
		return toUsername;
	}
	public void setToUsername(String toUsername) {
		this.toUsername = toUsername;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public int getTransferType() {
		return transferType;
	}
	public void setTransferType(int transferType) {
		this.transferType = transferType;
	}

	public int getTransferStatusId() {
		return transferStatusId;
	}

	public void setTransferStatusId(int transferStatusId) {
		this.transferStatusId = transferStatusId;
	}
}
