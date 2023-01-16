package main.java.com.controller.listener;

import java.time.LocalDateTime;

import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;

/**
 * ChatListener is the listener associated to conversation events.
 * 
 * @author Sandro
 * @author sarah
 *
 */
public interface ChatListener {

	// What happens when a user receives a chat request?
	public void onChatRequestReceived(User remoteUser);

	// What happens when a user requests to start a conversation with a remote user?
	public void onChatRequest(User remoteUser);

	// What happens when a user unilaterally decides to end the chat with
	// remoteUser?
	public void onChatClosure(User remoteUser);

	// What happens when a user wants to send a message to a remote user?
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date,
			MessageType type);

	// What happens when a user has a message to receive?
	public void onMessageToReceive(Message message);

	// What happens when a user deletes a message?
	public void onMessageToDelete(Message message);

}
