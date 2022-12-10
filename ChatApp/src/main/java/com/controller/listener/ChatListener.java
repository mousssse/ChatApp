package main.java.com.controller.listener;

import main.java.com.model.Message;
import main.java.com.model.User;

public interface ChatListener {

	// What happens if a user requests to chat with another online user?
	//public void onChatRequestReceived(User user, int port);
	
	// What happens when you want to start a conversation
	public void onChatRequest(User remoteUser);
	// What happens when one of both parties decides to end the chat?
	public void onChatClosure(User remoteUser);
	// What happens when a user wants to send a message?
	public void onMessageToSend(User localUser, User remoteUser, String messageContent);
	// What happens when a user has to receive a message?
	public void onMessageToReceive(Message message);

}
