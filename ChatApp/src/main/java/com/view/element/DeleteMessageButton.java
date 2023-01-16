package main.java.com.view.element;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;

/**
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class DeleteMessageButton extends Button {

	private Message message;

	public DeleteMessageButton() {
		this.setStyle("-fx-faint-focus-color: -fx-control-inner-background;");
		this.setText("X");

		this.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				User remoteUser;
				User localUser = OnlineUsersManager.getInstance().getLocalUser();
				if (message.getToUser().getId().equals(localUser.getId())) {
					remoteUser = message.getFromUser();
				} else {
					remoteUser = message.getToUser();
				}
				ListenerManager.getInstance().fireOnMessageToDelete(message);
				ListenerManager.getInstance().fireOnMessageToSend(localUser, remoteUser, null, message.getDate(),
						MessageType.DELETE_MESSAGE);
			}
		});
	}

	/**
	 * Sets the associated message to the button
	 * 
	 * @param message the message associated to the button
	 */
	public void setMessage(Message message) {
		this.message = message;
	}
}
