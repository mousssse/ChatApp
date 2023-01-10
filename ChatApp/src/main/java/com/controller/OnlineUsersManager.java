package main.java.com.controller;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
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
			InetAddress localIP = null;
			// Not using InetAddress.getLocalHost() because it can return 
			// 127.0.0.1 instead of an IP address associated with a network
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp()) continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet6Address) continue;

                    String ip = addr.getHostAddress();
                    localIP = InetAddress.getByName(ip);
                    // We keep the first one
                    break;
                }
            }
			this.localUser = new User(DBManager.getInstance().getIdFromUsername(username), username, localIP);
		} catch (UnknownHostException | SocketException e) {
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
