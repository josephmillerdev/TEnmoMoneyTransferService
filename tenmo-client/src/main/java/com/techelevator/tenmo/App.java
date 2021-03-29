package com.techelevator.tenmo;

import java.util.Scanner;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.tenmo.services.UserServiceException;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private UserService userService;
    private Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) throws UserServiceException {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new UserService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, UserService userService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.userService = userService;
	}

	public void run() throws UserServiceException {
		System.out.println("*********************");
		System.out.println("* Welcome to Tenmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() throws UserServiceException {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() throws UserServiceException {
		
		Integer userId = currentUser.getUser().getId();
		Account account = userService.getAccount(userId);
		Double balance = account.getBalance();
		
		System.out.println("Your current account balance is: $" + balance);
		
		
		
	}

	private void viewTransferHistory() throws UserServiceException {
		Transfer[] transfers = userService.history(currentUser.getUser().getId());
		int accountId = userService.getAccount(currentUser.getUser().getId()).getUserId();
			
		System.out.println("\n------------------------------" +
				"\nTransfers\n" +
				"\nID\tFrom/To\t\tAmount" +
				"\n------------------------------");
			
			for (int i=0; i<transfers.length; i++) {

				if(accountId == transfers[i].getFromAccountId()) {
					int accountToUserId = userService.getUserIdFromAcountId(transfers[i].getToAccountId());
					String toAccountUsername = userService.findUserNameByUserId(accountToUserId);

					System.out.println(transfers[i].getTransferTypeId() + "\tTo: " + toAccountUsername.toUpperCase() +
									"\t$ " + transfers[i].getAmount());
				} else {
					int accountFromUserId = userService.getUserIdFromAcountId(transfers[i].getFromAccountId());
					String fromAccountUsername = userService.findUserNameByUserId(accountFromUserId);
					System.out.println(transfers[i].getTransferTypeId() + "\tFrom: " + fromAccountUsername.toUpperCase() +
							"\t$ " + transfers[i].getAmount());
				}
			}
			System.out.println("\nPlease enter Transfer ID to view details (0 to cancel): ");
			int transferId = Integer.parseInt(userInput.nextLine());
			
			if (transferId == 0) {
				System.out.println();
			} else {
				Transfer transfer = userService.transferDetails(transferId);
				int accountToUserId = userService.getUserIdFromAcountId(transfer.getToAccountId());
				String toAccountUsername = userService.findUserNameByUserId(accountToUserId);
				int accountFromUserId = userService.getUserIdFromAcountId(transfer.getFromAccountId());
				String fromAccountUsername = userService.findUserNameByUserId(accountFromUserId);
				String status = userService.getTransferStatusName(transfer.getTransferStatusId());
				String transferType = userService.getTransferTypeName(transfer.getTransferTypeId());
			
				System.out.println("\n------------------------------" +
								"\nTransfer Details" +
								"\n------------------------------" +
								"\nId: " + transfer.getTransferTypeId() +
								"\nFrom: " + fromAccountUsername.toUpperCase() +
								"\nTo: " + toAccountUsername.toUpperCase() +
								"\nType: " + transferType +
								"\nStatus: " + status +
								"\nAmount: $" + transfer.getAmount());
			}		
		
	}

	private void viewPendingRequests() throws UserServiceException{
		int accountId = userService.getAccount(currentUser.getUser().getId()).getAccountId();
		Transfer[] transfers = userService.pending(accountId);
		
		boolean pendingRequest = false;
		
		for (int i=0; i<transfers.length; i++) {
			if(accountId == transfers[i].getFromAccountId()) {		
				pendingRequest = true;
			}
		}
		
		if (pendingRequest == true) {
			System.out.println("\n------------------------------" +
					"\nPending Transfers\n" +
					"\nID\tTo\t\tAmount" +
					"\n------------------------------");
		}
		
		for (int i=0; i<transfers.length; i++) {
			if(accountId == transfers[i].getFromAccountId()) {
				int accountToUserId = userService.getUserIdFromAcountId(transfers[i].getToAccountId());
				String toAccountUsername = userService.findUserNameByUserId(accountToUserId);
				System.out.println(transfers[i].getTransferStatusId() + "\tTo: " + toAccountUsername.toUpperCase() +
								"\t\t$ " + transfers[i].getAmount());
			}
		}
				
		if (pendingRequest == true) {
			System.out.print("\nnter Transfer ID to approve or reject request (0 to cancel): ");
			int transferId = Integer.parseInt(userInput.nextLine());
			System.out.print("\n1: Approve\n2: Reject\n0: Don't approve or reject\n----------\nPlease choose an option:");
			int transferStatusId = Integer.parseInt(userInput.nextLine());
			
			if (transferStatusId == 1) {
				transferStatusId = 2;				
			} else if (transferStatusId ==2) {
				transferStatusId = 3;
			} else System.out.println("Request status will remain \"Pending\"");
			
			if (transferStatusId == 3) {
				for (int i=0; i<transfers.length; i++) {
					if (transferId == transfers[i].getTransferStatusId()) {
						transfers[i].setTransferStatusId(transferStatusId);
						userService.updateTransferStatus(transfers[i]);
						System.out.println("\nReject successful");
					}
				}
			} else if (transferStatusId == 2) {
				for (int i=0; i<transfers.length; i++) {
					if (transferId == transfers[i].getTransferStatusId()) {
						transfers[i].setTransferStatusId(transferStatusId);
						userService.updateTransferStatus(transfers[i]);
						userService.updateBalanceFromTransfer(transfers[i]);
						System.out.println("\nApproval successful");
					}
				}
			}
			
		} else System.out.println("You have no pending requests.");
		
	}

	private void sendBucks() throws UserServiceException {
		
		User[] allUsers = userService.getUsers();
		System.out.println("\n------------------------------" +
				"\nUsers\n" +
				"\nID\t\tName" +
				"\n------------------------------");
		for (int i = 0; i < allUsers.length; i++) {
			System.out.println(allUsers[i].getId() + "\t\t" + allUsers[i].getUsername().toUpperCase());
		}
		System.out.println("------------------------------");
		System.out.println("\nEnter ID of user you are sending to (0 to cancel): ");
		int userId = Integer.parseInt(userInput.nextLine());
		System.out.println("\nEnter amount: ");
		Double amountToTransfer = Double.parseDouble(userInput.nextLine());
		
		Transfer transfer = new Transfer();
		int accountFromId = userService.getAccount(currentUser.getUser().getId()).getAccountId();
		transfer.setFromAccountId(accountFromId);
		int accountToId = userService.getAccount(userId).getAccountId();
		transfer.setToAccountId(accountToId);
		transfer.setAmount(amountToTransfer);
		
		userService.sendTransfer(transfer);
		System.out.println("\nMoney sent successfully");
		
	}

	private void requestBucks() throws UserServiceException {
		User[] allUsers = userService.getUsers();
		System.out.println("\n------------------------------" +
				"\nUsers\n" +
				"\nID\t\tName" +
				"\n------------------------------");
		for (int i = 0; i < allUsers.length; i++) {
			System.out.println(allUsers[i].getId() + "\t\t" + allUsers[i].getUsername().toUpperCase());
		}
		System.out.println("------------------------------");
		System.out.println("\nEnter ID of user you are requesting from (0 to cancel): ");
		int userId = Integer.parseInt(userInput.nextLine());
		System.out.println("\nEnter amount: ");
		Double amountTransfer = (Double.parseDouble(userInput.nextLine()));
		
		Transfer transfer = new Transfer();
		int accountId = userService.getAccount(currentUser.getUser().getId()).getAccountId();
		transfer.setFromAccountId(accountId);
		int accountToId = userService.getAccount(userId).getAccountId();
		transfer.setToAccountId(accountToId);
		transfer.setAmount(amountTransfer);
		
		userService.requestTransfer(transfer);
		System.out.println("\nRequest sent");
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+ e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}

