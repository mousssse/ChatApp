package main.java.com.view.element;

import java.time.LocalDateTime;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.ChatRequestListener;
import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;

public class ChatRequestButton extends Button implements ChatListener, ChatRequestListener {

    private User remoteUser;
    private final String requestChat = "Invite to chat";
    private final String cancelRequest = "Cancel";
    private final String acceptRequest = "Accept";
    private final String endChat = "End chat";
    
	public ChatRequestButton() {
		ListenerManager.getInstance().addChatRequestListener(this);
		ListenerManager.getInstance().addChatListener(this);
		this.setStyle("-fx-faint-focus-color: -fx-control-inner-background;");
		this.setText(this.requestChat);
		
		this.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				switch (getText()) {
					case acceptRequest:
						ListenerManager.getInstance().fireOnChatAcceptRequest(remoteUser);
						break;
					case cancelRequest:
						ListenerManager.getInstance().fireOnChatCancelRequest(remoteUser);
						break;
					case requestChat:
						ListenerManager.getInstance().fireOnChatRequest(remoteUser);
						break;
					case endChat:
						ListenerManager.getInstance().fireOnChatClosure(remoteUser);
						break;
					default:
						break;
				}
			}
		});
	}
	
	public void setRemoteUser(User remoteUser) {
		this.remoteUser = remoteUser;
	}

	@Override
	public void onChatRequestReceived(User remoteUser) {
		if (this.remoteUser != null && remoteUser.getId().equals(this.remoteUser.getId())) {
			Platform.runLater(() -> this.setText(this.acceptRequest));
		}
	}

	@Override
	public void onChatRequest(User remoteUser) {
		if (this.remoteUser != null && remoteUser.getId().equals(this.remoteUser.getId())) {
			Platform.runLater(() -> this.setText(this.cancelRequest));
			ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), this.remoteUser, null, LocalDateTime.now(), MessageType.REQUEST_CONVERSATION);
		}
	}

	@Override
	public void onChatClosureReceived(User remoteUser) {
		if (this.remoteUser != null && remoteUser.getId().equals(this.remoteUser.getId())) {
			Platform.runLater(() -> this.setText(this.requestChat));
		}
	}

	@Override
	public void onChatClosure(User remoteUser) {
		if (this.remoteUser != null && remoteUser.getId().equals(this.remoteUser.getId())) {
			Platform.runLater(() -> this.setText(this.requestChat));
			ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), this.remoteUser, null, LocalDateTime.now(), MessageType.END_CONVERSATION);
		}
	}

	@Override
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date, MessageType type) {
		// Nothing to do
	}

	@Override
	public void onMessageToReceive(Message message) {
		// Nothing to do
	}

	@Override
	public void onChatAcceptRequest(User remoteUser) {
		if (this.remoteUser != null && remoteUser.getId().equals(this.remoteUser.getId())) {
			Platform.runLater(() -> this.setText(this.endChat));
			ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), this.remoteUser, null, LocalDateTime.now(), MessageType.ACCEPT_REQUEST);
		}
	}

	@Override
	public void onChatCancelRequest(User remoteUser) {
		if (this.remoteUser != null && remoteUser.getId().equals(this.remoteUser.getId())) {
			Platform.runLater(() -> this.setText(this.requestChat));
			ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), this.remoteUser, null, LocalDateTime.now(), MessageType.CANCEL_REQUEST);
		}
	}

	@Override
	public void onChatAcceptedRequest(User remoteUser) {
		if (this.remoteUser != null && remoteUser.getId().equals(this.remoteUser.getId())) {
			Platform.runLater(() -> this.setText(this.endChat));
		}
	}

	@Override
	public void onChatCancelledRequest(User remoteUser) {
		if (this.remoteUser != null && remoteUser.getId().equals(this.remoteUser.getId())) {
			Platform.runLater(() -> this.setText(this.requestChat));
		}
	}
}
