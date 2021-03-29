package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;

public class UserService {
	
	private String BASE_URL;
	private RestTemplate restTemplate;
	private String AUTH_TOKEN = "";
	
	public UserService(String URL) {
		this.BASE_URL = URL;
		restTemplate = new RestTemplate();
	}

	public Account getAccount (int userId) throws UserServiceException {
		Account account = null;
		try {
			account = restTemplate.exchange(BASE_URL + "account/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		} return account;
	}
	
	public int getUserIdFromAcountId(int accountId) throws UserServiceException {
		int userId = 0;
		try {
			userId = restTemplate.exchange(BASE_URL + "account/accountId/" + accountId, HttpMethod.GET, makeAuthEntity(), Integer.class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		} return userId;
	}

	public User[] getUsers() throws UserServiceException {
		User[] users = null;
		try {
			users = restTemplate.exchange(BASE_URL + "user", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		} return users;
	}
	
	public String findUserNameByUserId(int userId) throws UserServiceException {
		String user = "";
		try {
			user = restTemplate.exchange(BASE_URL + "user/username/" + userId, HttpMethod.GET, makeAuthEntity(), String.class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		} return user;
	}
	
	public User getUser(String username) throws UserServiceException {
		User user = null;
		try {
			user = restTemplate.exchange(BASE_URL + "user/" + username, HttpMethod.GET, makeAuthEntity(), User.class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		} return user;
	}
	
	public boolean create(String username, String password) throws UserServiceException {
		try {
			restTemplate.exchange(BASE_URL + "user", HttpMethod.POST, makeAuthEntity(), Boolean.class);
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		} return true;
	}
	
	public boolean sendTransfer(Transfer transfer) throws UserServiceException {
		
		try {
			restTemplate.exchange(BASE_URL + "transfer/send", HttpMethod.POST, makeTransferEntity(transfer), Boolean.class);
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return true;
	}
	
	public Transfer requestTransfer(Transfer transfer) throws UserServiceException {
		Transfer thisTransfer = null;
		try {
			thisTransfer = restTemplate.exchange(BASE_URL + "transfer/request", HttpMethod.POST, makeTransferEntity(transfer), Transfer.class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return thisTransfer;
	}
	
	public boolean updateTransferStatus(Transfer transfer) throws UserServiceException {
		
		try {
			restTemplate.exchange(BASE_URL + "transfer/update", HttpMethod.PUT, makeTransferEntity(transfer), Boolean.class);
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return true;
	}
	
	public boolean updateBalanceFromTransfer(Transfer transfer) throws UserServiceException {
		try {
			restTemplate.exchange(BASE_URL + "transfer/update/balance", HttpMethod.PUT, makeTransferEntity(transfer), Boolean.class);
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
		}
		return true;
	}
	
	public Transfer[] history (int userId) throws UserServiceException {
		Transfer[] transferHistory = null;
		try { 
			transferHistory = restTemplate.exchange(BASE_URL + "transfer/" + userId + "/history" , HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
			
		} return transferHistory;
	}
	
	public Transfer transferDetails(int transferId) throws UserServiceException {
		Transfer transfer = null;
		try {
			transfer = restTemplate.exchange(BASE_URL + "transfer/" + transferId + "/details", HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
			
		} return transfer;
	}
	
	public Transfer[] pending (int accountId) throws UserServiceException {
		Transfer[] transfersPending = null;
		try { 
			transfersPending = restTemplate.exchange(BASE_URL + "transfer/" + accountId + "/status" , HttpMethod.GET, makeAuthEntity(), Transfer[].class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
			
		} return transfersPending;
	}
	
	public String getTransferStatusName(int transferStatusId) throws UserServiceException {
		String status = "";
		try {
			status = restTemplate.exchange(BASE_URL + "transfer/" + transferStatusId + "/statusname", HttpMethod.GET, makeAuthEntity(), String.class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
			
		} return status;
	}
	
	public String getTransferTypeName(int transferTypeId) throws UserServiceException {
		String transferType = "";
		try {
			transferType = restTemplate.exchange(BASE_URL + "transfer/" + transferTypeId + "/type" , HttpMethod.GET, makeAuthEntity(), String.class).getBody();
		} catch (RestClientResponseException ex) {
			throw new UserServiceException(ex.getRawStatusCode() + " : " + ex.getResponseBodyAsString());
			
		} return transferType;
	}


	private HttpEntity makeAuthEntity() {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setBearerAuth(AUTH_TOKEN);
	    HttpEntity entity = new HttpEntity<>(headers);
	    return entity;
	  }
	
	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(AUTH_TOKEN);
	    HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
	    return entity;
	  }
}
