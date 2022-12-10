package main.java.com.controller;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import main.java.com.controller.listener.LoginListener;
import main.java.com.model.User;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class OnlineUsersManager implements LoginListener {
	
	// The AccountManager is a singleton
	private static OnlineUsersManager onlineUsersManager = null;
	private User localUser;
	// Active users mapping
	private Map<InetAddress, User> accountsMap;
	
	private OnlineUsersManager() {
		accountsMap = new HashMap<InetAddress, User>();
	}
	
	public void setLocalUser(User localUser) {
		this.localUser = localUser;
	}
	
	public User getLocalUser() {
		return this.localUser;
	}
	
	/**
	 * 
	 * @return The AccountManager singleton
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
	public void onLogin(User remoteUser) {
		accountsMap.put(remoteUser.getIP(), remoteUser);
	}

	/**
	 * Removes user from the active users mapping
	 */
	@Override
	public void onLogout(InetAddress inetAddress) {
		this.accountsMap.remove(inetAddress);
	}
	
	
}
