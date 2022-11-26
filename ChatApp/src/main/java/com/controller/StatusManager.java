/**
 * The StatusManager class manages the list of active users, and consists of a single self-constructed instance.
 */

package main.java.com.controller;

import java.util.List;

/**
 * 
 * @author Sandro
 * @author Sarah
 *
 */
public class StatusManager {
	
	private static final StatusManager statusManager = new StatusManager();
	private List<String> activeUsers;
	
	/**
	 * 
	 * @param username is the username of the user who logged in
	 */
	public void onLogin(String username) {
		if(!activeUsers.contains(username))
			activeUsers.add(username);
	}
	
	/**
	 * 
	 * @param  username is the username of the user who logged out
	 */
	public void onLogout(String username) {
		activeUsers.remove(username);
	}
	
	/**
	 * 
	 * @param curUsername is the current username of the user wishing to change their username
	 * @param newUsername is the potential new username of the same user
	 */
	public void onUsernameModification(String curUsername, String newUsername) {
		if (!activeUsers.contains(newUsername))
			activeUsers.set(activeUsers.indexOf(curUsername), newUsername);
	}
	

}
