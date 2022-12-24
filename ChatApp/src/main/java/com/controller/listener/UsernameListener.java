package main.java.com.controller.listener;

import main.java.com.model.User;

/**
 * UsernameListener is the listener associated to username modification events.
 * @author Sandro
 * @author sarah
 *
 */
public interface UsernameListener {
	
	// TODO
	// What happens when an online user modifies their username?
	public void onUsernameModification(User user, String newUsername);
	// What happens when the current user modifies their username?
	public void onSelfUsernameModification(String newUsername);

}
