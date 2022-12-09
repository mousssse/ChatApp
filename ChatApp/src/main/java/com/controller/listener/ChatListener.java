package main.java.com.controller.listener;

import main.java.com.model.Message;
import main.java.com.model.User;

public interface ChatListener {
	
	// Parameters to be determined tomorrow, I'm just brainstorming

	// What happens if a user requests to chat with another online user?
	//public void onChatRequestReceived(User user, int port);
	
	// What happens when you want to start a conversation
	public void onChatRequest(User user);
	// What happens when one of both parties decides to end the chat?
	public void onChatClosure(User user1, User user2);
	// What happens when a user wants to send a message?
	public void onMessageToSend(User user, Message message);
	// What happens when a user has to receive a message?
	public void onMessageToReceive(User user, Message message);

}
