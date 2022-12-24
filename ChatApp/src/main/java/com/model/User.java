package main.java.com.model;

import java.io.Serializable;
import java.net.InetAddress;


/**
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class User implements Serializable {

	private static final long serialVersionUID = 3935125359654183855L;
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
	
	/**
	 * User as displayed in the chat frame
	 */
	@Override
	public String toString() {
		return this.username;
	}

	/**
	 * 
	 * @return the user's ID
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * 
	 * @return the user's username
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * 
	 * @param username is the chosen username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * 
	 * @return is the user's IP address
	 */
	public InetAddress getIP() {
		return this.userIP;
	}
	
	/**
	 * 
	 * @return is the user's TCP server port.
	 */
	public int getTCPserverPort() {
		return this.TCPserverPort;
	}
    
}
