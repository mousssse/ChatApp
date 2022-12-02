package main.java.com.model;

import main.java.com.controller.ThreadManager;


public class User {
    private String username;
    private String id;

	public User() {
		super();
		ThreadManager.threadManager.addNewUser(this.id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	private void setUsername(String username) {
		this.username = username;
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
