package main.java.com.controller.listener;

import main.java.com.model.User;

public interface ChatRequestListener {

	// What happens when a user accepts a chat request from a remote user?
	public void onChatAcceptRequest(User remoteUser);

	// What happens when a user cancels a chat request they had sent?
	public void onChatCancelRequest(User remoteUser);

	// What happens when a chat request is accepted?
	public void onChatAcceptedRequest(User remoteUser);

	// What happens, for the request-receiving user, when a chat request has been
	// cancelled by the requesting user?
	public void onChatCancelledRequest(User remoteUser);
}
