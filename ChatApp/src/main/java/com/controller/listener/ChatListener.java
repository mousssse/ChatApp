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
	// What happens when you want to start a conversation
	public void onChatRequest(User remoteUser);
	// What happens when one of both parties decides to end the chat?
	public void onChatClosure(User remoteUser);
	// What happens when a user wants to send a message?
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date, MessageType type);
	// What happens when a user has to receive a message?
	public void onMessageToReceive(Message message);

}
