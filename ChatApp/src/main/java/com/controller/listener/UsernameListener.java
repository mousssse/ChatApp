package main.java.com.controller.listener;

import main.java.com.model.User;

/**
 * UsernameListener is the listener associated to username modification events.
 * 
 * @author Sandro
 * @author sarah
 *
 */
public interface UsernameListener {

	// What happens when a remote user modifies their username?
	public void onUsernameModification(User user, String newUsername);

	// What happens when the local user modifies their username?
	public void onSelfUsernameModification(String newUsername);

}
