package main.java.com.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.SelfLoginListener;
import main.java.com.model.User;
import main.java.com.view.OnlineUsersFrame;

/**
 * The OnlineUsersManager locally manages the active users as well as the local user.
 * @author Sandro
 * @author sarah
 *
 */
public class OnlineUsersManager implements LoginListener, SelfLoginListener {
	
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
		System.out.println("adding : " + remoteUser.getUsername());
		this.accountsMap.put(remoteUser.getIP(), remoteUser);
		// Update the view
		OnlineUsersFrame.getInstance().getUserListVector().addElement(remoteUser);
	}

	/**
	 * Removes user from the active users mapping
	 */
	@Override
	public void onLogout(User remoteUser) {
		this.accountsMap.remove(remoteUser.getIP());
		OnlineUsersFrame.getInstance().getUserListVector().removeElement(remoteUser);
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
	}

	@Override
	public void onSelfLogout() {
		this.accountsMap.clear();
	}
	
}
