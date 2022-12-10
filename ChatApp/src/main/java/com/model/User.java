package main.java.com.model;

import java.net.InetAddress;


/**
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class User {
    private String id;
    private String username;
    private InetAddress userIP;
    private int TCPserverPort;

    /**
     * This constructor is used for the local user.
     * @param id is the local user's id.
     * @param username is the local user's username.
     * @param userIP is the local user's IP address
     */
	public User(String id, String username, InetAddress userIP) {
		super();
		this.id = id;
		this.username = username;
		this.userIP = userIP;
		this.TCPserverPort = TCPServer.getTCPserverPort();
	}
	
	/**
	 * This constructor is used for remote users.
	 * @param id is the remote user's id.
	 * @param username is the remote user's username.
	 * @param userIP is the remote user's IP address.
	 * @param port is the remote user's TCP server port.
	 */
	public User(String id, String username, InetAddress userIP, int port) {
		super();
		this.id = id;
		this.username = username;
		this.userIP = userIP;
		this.TCPserverPort = port;
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
    
}
