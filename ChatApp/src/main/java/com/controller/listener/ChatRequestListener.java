package main.java.com.controller.listener;

import main.java.com.model.User;

public interface ChatRequestListener {

	// What happens when you accept the chat request?
	public void onChatAcceptRequest(User remoteUser);
	// What happens when you cancel your chat request?
	public void onChatCancelRequest(User remoteUser);
	// What happens when your chat request is accepted?
	public void onChatAcceptedRequest(User remoteUser);
	// What happens when the chat request has been cancelled?
	public void onChatCancelledRequest(User remoteUser);
}
