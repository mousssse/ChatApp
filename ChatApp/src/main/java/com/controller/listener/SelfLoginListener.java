package main.java.com.controller.listener;

/**
 * SelfLoginListener is the listener associated to the local user's log-in and
 * log-off events.
 * 
 * @author Sandro
 * @author sarah
 *
 */
public interface SelfLoginListener {

	// What happens locally when the local user logs in?
	public void onSelfLoginOnlineUsers(String username);

	public void onSelfLoginNetwork();

	// What happens locally when the local user logs out?
	public void onSelfLogout();
}
