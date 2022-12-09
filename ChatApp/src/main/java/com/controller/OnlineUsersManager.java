package main.java.com.controller;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import main.java.com.controller.listener.LoginListener;
import main.java.com.model.User;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class OnlineUsersManager implements LoginListener{
	
	// The AccountManager is a singleton
	private static OnlineUsersManager onlineUsersManager = null;
	// Active users mapping
	private Map<InetAddress, User> accountsMap;
	
	private OnlineUsersManager() {
		accountsMap = new HashMap<InetAddress, User>();
	}
	
	/**
	 * 
	 * @return the AccountManager singleton
	 */
	public static OnlineUsersManager getInstance() {
		if (onlineUsersManager == null) onlineUsersManager = new OnlineUsersManager();
		return onlineUsersManager;
	}
	
	/**
	 * 
	 * @param IP is the IP address associated to the user in question.
	 * @return the user connected to the ChatApp from IP.
	 */
	public User getUserFromIP(InetAddress IP) {
		return accountsMap.get(IP);	
	}

	/**
	 * Adds user to the active users mapping
	 */
	@Override
	public void onLogin(User user) {
		accountsMap.put(user.getUserIP(), user);
		
	}

	/**
	 * Removes user from the active users mapping
	 */
	@Override
	public void onLogout(User user) {
		accountsMap.remove(user.getUserIP());
		
	}
	
	
}
