package main.java.com.controller.listener;

import main.java.com.model.User;

/**
 * LoginListener is the listener associated to remote users' log-in and log-off
 * events.
 * 
 * @author Sandro
 * @author sarah
 *
 */
public interface LoginListener {

	// What happens when an inactive remote user becomes active?
	public void onLogin(User remoteUser);

	// What happens when an active remote user becomes inactive?
	public void onLogout(User remoteUser);

}
