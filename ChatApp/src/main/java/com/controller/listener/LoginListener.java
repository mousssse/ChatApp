package main.java.com.controller.listener;

import main.java.com.model.User;

/**
 * LoginListener is the listener associated to remote users' log-in and log-off events.
 * @author Sandro
 * @author sarah
 *
 */
public interface LoginListener {
	
	public void onLogin(User remoteUser);
	public void onLogout(User remoteUser);

}
