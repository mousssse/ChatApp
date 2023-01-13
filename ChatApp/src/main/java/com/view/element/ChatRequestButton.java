package main.java.com.view.element;

import java.time.LocalDateTime;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.model.MessageType;
import main.java.com.model.User;
import main.java.com.view.ChatAppStage;

public class ChatRequestButton extends Button {
	
    private User remoteUser;
    public final static String requestChat = "Invite to chat";
    public final static String cancelRequest = "Cancel";
    public final static String acceptRequest = "Accept";
    public final static String endChat = "End chat";
    
	public ChatRequestButton() {
		this.setStyle("-fx-faint-focus-color: -fx-control-inner-background;");
		
		this.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				switch (getText()) {
					case acceptRequest:
						ListenerManager.getInstance().fireOnChatAcceptRequest(remoteUser);
						ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), remoteUser, null, LocalDateTime.now(), MessageType.ACCEPT_REQUEST);
						break;
					case cancelRequest:
						ListenerManager.getInstance().fireOnChatCancelRequest(remoteUser);
						ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), remoteUser, null, LocalDateTime.now(), MessageType.CANCEL_REQUEST);
						break;
					case requestChat:
						ListenerManager.getInstance().fireOnChatRequest(remoteUser);
						ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), remoteUser, null, LocalDateTime.now(), MessageType.REQUEST_CONVERSATION);
						break;
					case endChat:
						ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), remoteUser, null, LocalDateTime.now(), MessageType.END_CONVERSATION);
						ListenerManager.getInstance().fireOnChatClosure(remoteUser);
						break;
					default:
						break;
				}
			}
		});
	}
	
	/**
	 * Sets the associated remote user to the button and adds the button
	 * in the ChatAppStage map of all buttons
	 * 
	 * @param remoteUser the remote user associated to the button
	 */
	public void setRemoteUser(User remoteUser) {
		this.remoteUser = remoteUser;
		ChatAppStage.getInstance().addButton(remoteUser.getId(), this);
	}
}
