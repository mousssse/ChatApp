package main.java.com.controller.listener;

/**
 * DBListener is the listener associated to the local user's database-related events.
 * @author Sandro
 * @author sarah
 *
 */
public interface DBListener {
	
	public void onSelfLogin(String username, String password);
}
