package main.java.com.model;

import java.net.InetAddress;


/**
 * TODO remove all methods, everything will be done by listeners
 * @author Sandro
 * @author sarah
 *
 */
public class User {
    private String id;
    private String username;
    private InetAddress userIP;
    private int TCPserverPort;

	public User(String id, String username, InetAddress userIP, int TCPServerPort) {
		super();
		this.id = id;
		this.username = username;
		this.userIP = userIP;
		this.TCPserverPort = TCPServerPort;
	}

	public String getId() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public InetAddress getIP() {
		return this.userIP;
	}
	
	public int getTCPserverPort() {
		return this.TCPserverPort;
	}

	/** TODO The following will be removed but keep it for now.
	 * Log-in method
	 * @param username is the username entered by the user in the ID text field of the login frame
	 * @param password is the password entered by the user in the password field of the login frame
	 * @return
	 */
	public boolean connect(String username, String password) {
		// If connection successful, add the user to the ThreadManager's map.
		return false;
	}
    
}
