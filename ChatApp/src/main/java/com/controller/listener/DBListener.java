package main.java.com.controller.listener;

import java.time.LocalDateTime;

import main.java.com.model.Message;
import main.java.com.model.User;

/**
 * DBListener is the listener associated to the local user's database-related
 * events.
 * 
 * @author Sandro
 * @author sarah
 *
 */
public interface DBListener {

	// What happens in the database when the local user tries to log in?
	public void onSelfLoginDB(String username, String password);

	// What happens in the database when a message has been successfully sent to a
	// remote user?
	public void onMessageSuccessfullySent(User localUser, User remoteUser, String messageContent, LocalDateTime date);

	// What happens in the database when the local user has a message to receive?
	public void onMessageToReceiveDB(Message message);

	// What happens in the database when the local user deletes one of their
	// messages?
	public void onMessageToDeleteDB(Message message);
}
