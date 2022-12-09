package main.java.com.controller;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import main.java.com.model.User;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class AccountManager {
	
	// The AccountManager is a singleton
	private static final AccountManager accountManager = new AccountManager();
	// Mapping of IPs and their associated users
	private static Map<InetAddress, User> accountsMap;
	
	public AccountManager() {
		accountsMap = new HashMap<InetAddress, User>();
	}
	
	/**
	 * 
	 * @return the AccountManager singleton
	 */
	public static AccountManager getInstance() {
		return accountManager;
	}
	
	/**
	 * 
	 * @param user is the User object to add to the accounts map.
	 */
	public static void addUser(User user) {
		accountsMap.put(user.getUserIP(), user);
	}
	
	/**
	 * 
	 * @param IP is the IP address associated to the user in question.
	 * @return the user connected to the ChatApp from IP.
	 */
	public static User getUserFromIP(InetAddress IP) {
		return accountsMap.get(IP);	
	}
}
