package main.java.com.controller.listener;

import java.time.LocalDateTime;

import main.java.com.model.Message;
import main.java.com.model.User;

/**
 * DBListener is the listener associated to the local user's database-related events.
 * @author Sandro
 * @author sarah
 *
 */
public interface DBListener {
	
	public void onSelfLoginDB(String username, String password);
	public void onMessageSuccessfullySent(User localUser, User remoteUser, String messageContent, LocalDateTime date);
	public void onMessageToReceiveDB(Message message);
	public void onMessageToDeleteDB(Message message);
}
