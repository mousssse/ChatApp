package main.java.com.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.SelfLoginListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.User;

/**
 * The OnlineUsersManager locally manages the active users as well as the local user.
 * @author Sandro
 * @author sarah
 *
 */
public class OnlineUsersManager implements LoginListener, SelfLoginListener, UsernameListener {
	
	private static OnlineUsersManager onlineUsersManager = null;
	private User localUser;
	private Map<InetAddress, User> accountsMap;
	
	private OnlineUsersManager() {
		this.accountsMap = new HashMap<InetAddress, User>();
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
		return this.accountsMap.get(IP);	
	}
	
	/**
	 * @param userId is the ID associated to the user in question
	 * @return the user connected to the ChatApp from IP, or null if not found
	 */
	public User getUserFromID(String userId) {
		for (User user : this.accountsMap.values()) {
			if (user.getId().equals(userId)) {
				return user;
			}
		}
		return null;
	}
	
	public User getLocalUser() {
		return this.localUser;
	}

	/**
	 * Adds user to the active users mapping
	 */
	@Override
	public void onLogin(User remoteUser) {
		this.accountsMap.put(remoteUser.getIP(), remoteUser);
	}

	/**
	 * Removes user from the active users mapping
	 */
	@Override
	public void onLogout(User remoteUser) {
		this.accountsMap.remove(remoteUser.getIP());
	}

	/**
	 * Creates the local user
	 */
	@Override
	public void onSelfLoginOnlineUsers(String username) {
		try {
			this.localUser = new User(DBManager.getInstance().getIdFromUsername(username), username, InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onSelfLoginNetwork() {
		// Nothing to do
	}

	@Override
	public void onSelfLogout() {
		this.accountsMap.clear();
	}

	@Override
	public void onUsernameModification(User user, String newUsername) {
		user.setUsername(newUsername);
		this.accountsMap.remove(user.getIP());
		this.accountsMap.put(user.getIP(), user);
	}

	@Override
	public void onSelfUsernameModification(String newUsername) {
		this.localUser.setUsername(newUsername);
	}
	
}
