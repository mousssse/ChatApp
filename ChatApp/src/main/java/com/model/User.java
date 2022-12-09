package main.java.com.model;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * TODO remove all methods, everything will be done by listeners
 * @author Sandro
 * @author sarah
 *
 */
public class User {
    private String id;
    private InetAddress userIP;

	public User() throws UnknownHostException {
		super();
		this.userIP = InetAddress.getLocalHost();
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public InetAddress getUserIP() {
		return this.userIP;
	}

	/**
	 * Log-in method
	 * @param username is the username entered by the user in the ID text field of the login frame
	 * @param password is the password entered by the user in the password field of the login frame
	 * @return
	 */
	public boolean connect(String username, String password) {
		// If connection successful, add the user to the ThreadManager's map.
		return false;
	}
	
	/**
	 * Log-off method
	 */
	private void disconnect() {
	}
	
	/** TODO how to start a conversation with someone whose ID we don't know? Obviously, same for sendMessage, endConvo etc.
	 * Do we need a mapping of usernames to IDs? If so, which manager should deal with that?
	 * More realistically, I think we don't even care about these methods, I think the frames will be directly linked to the
	 * specific threads somehow.
	 * @param username
	 */
	private void startConversation(String username) {
		
	}
	
	private void sendMessage(String toId, String content) {
		//TODO: why not give id as parameter all the time instead of username?
		Message message = new Message(this.id, toId, content);
	}
	
	private void endConversation(String username) {
		
	}
    
}
