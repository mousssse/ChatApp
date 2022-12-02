package main.java.com.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import main.java.com.controller.ThreadManager;


public class User {
    private String username;
    private String id;
    private String userIP;

	public User() throws UnknownHostException {
		super();
		this.userIP = InetAddress.getLocalHost().toString();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserIP() {
		return this.userIP;
	}

	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		
	}

	/**
	 * Log-in method
	 * @param id is the ID entered by the user in the ID text field of the login frame
	 * @param password is the password entered by the user in the password field of the login frame
	 * @return
	 */
	public boolean connect(String id, String password) {
		// If connection successful, add the user to the ThreadManager's map.
		ThreadManager.getInstance().addUser(id);
		return false;
	}
	
	/**
	 * Log-off method
	 */
	private void disconnect() {
		ThreadManager.getInstance().removeUser(this.id);
	}
	
	/** TODO how to start a conversation with someone whose ID we don't know? Obviously, same for sendMessage, endConvo etc.
	 * Do we need a mapping of usernames to IDs? If so, which manager should deal with that?
	 * More realistically, I think we don't even care about these methods, I think the frames will be directly linked to the
	 * specific threads somehow.
	 * @param username
	 */
	private void startConversation(String username) {
		
	}
	
	private void sendMessage(String username, String content) {
		Message message = new Message(content);
	}
	
	private void endConversation(String username) {
		
	}
    
}
