package main.java.com.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.SelfLoginListener;
import main.java.com.model.User;

/**
 * The OnlineUsersManager locally manages the active users as well as the local user.
 * @author Sandro
 * @author sarah
 *
 */
public class OnlineUsersManager implements LoginListener, SelfLoginListener {
	
	// The OnlineUsersManager is a singleton
	private static OnlineUsersManager onlineUsersManager = null;
	private User localUser;
	// Active users mapping
	private Map<InetAddress, User> accountsMap;
	
	private OnlineUsersManager() {
		accountsMap = new HashMap<InetAddress, User>();
	}
	
	/**
	 * 
	 * @return the OnlineUsersManager singleton
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
	
	public User getLocalUser() {
		return this.localUser;
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
	public void onLogout(User remoteUser) {
		accountsMap.remove(remoteUser.getIP());
	}

	/**
	 * Creates the local user
	 */
	@Override
	public void onSelfLogin(String id, String username) {
		try {
			this.localUser = new User(id, username, InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSelfLogout() {
		// TODO Auto-generated method stub
		
	}
	
}
