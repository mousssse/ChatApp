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
		ThreadManager.getInstance().addNewUser(this.id);
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

	public boolean connect(String id, String password) {
		return false;
	}
	
	private void disconnect() {
		
	}
	
	private void startConversation(String username) {
		
	}
	
	private void sendMessage(String username, String content) {
		Message message = new Message(content);
	}
	
	private void endConversation(String username) {
		
	}
    
}
