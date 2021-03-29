package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

@RestController
public class UserController {
	private UserDAO userDAO;
	private AccountDAO accountDAO;
	
	public UserController(UserDAO userDAO, AccountDAO accountDAO) {
		this.userDAO = userDAO;
		this.accountDAO = accountDAO;
		
	}
	
	@RequestMapping(path = "/account/{userId}", method = RequestMethod.GET)
	public Account getAccount(@PathVariable int userId) {
		return accountDAO.getAccount(userId);
	}
	
	@RequestMapping(path = "/account/accountId/{accountId}", method = RequestMethod.GET)
	public int getUserIdFromAcountId(@PathVariable int accountId) {
		return accountDAO.getUserIdFromAccountId(accountId);
	}
	
	@RequestMapping(path = "/user", method = RequestMethod.GET)
	public List<User> getUsers() {
		return userDAO.findAll();
	}
	
	@RequestMapping(path ="/user/username/{userId}", method = RequestMethod.GET)
	public String findUserNameByUserId(@PathVariable int userId) {
		return userDAO.findUserNameByUserId(userId);
	}
	
	@RequestMapping(path = "/user/{username}", method = RequestMethod.GET)
	public User getUser(@PathVariable String username) {
		return userDAO.findByUsername(username);
	}
	
	@RequestMapping(path = "/user", method = RequestMethod.POST)
	public boolean create(@RequestBody String username, String password) {
		return userDAO.create(username, password);
	}
	
}
