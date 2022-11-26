package main.java.com.model;


public class User {
    private String username;

	public User() {
		super();
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
