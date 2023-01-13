package main.java.com.controller.listener;

import java.time.LocalDateTime;

import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;

/**
 * ChatListener is the listener associated to conversation events.
 * @author Sandro
 * @author sarah
 *
 */
public interface ChatListener {

	// What happens when you receive a chat request?
	public void onChatRequestReceived(User remoteUser);
	// What happens when you want to start a conversation with remoteUser
	public void onChatRequest(User remoteUser);
	// What happens when the remoteUser ends the chat?
	// TODO the following is useless, we always do the same as chat closure
	public void onChatClosureReceived(User remoteUser);
	// What happens when you decide to end the chat with remoteUser?
	public void onChatClosure(User remoteUser);
	// What happens when a user wants to send a message?
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date, MessageType type);
	// What happens when a user has to receive a message?
	public void onMessageToReceive(Message message);
	// What happens when a user deletes a message?
	public void onMessageToDelete(Message message);

}
