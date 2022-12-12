package main.java.com.controller.listener;

/**
 * SelfLoginListener is the listener associated to the local user's log-in and log-off events.
 * @author Sandro
 * @author sarah
 *
 */
public interface DBListener {
	
	public void onSelfLogin(String username, String password);
}
