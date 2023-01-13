package main.java.com.view.element;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import main.java.com.controller.ListenerManager;
import main.java.com.model.Message;

public class DeleteMessageButton extends Button {
	
    private Message message;
    
	public DeleteMessageButton() {
		this.setStyle("-fx-faint-focus-color: -fx-control-inner-background;");
		this.setText("X");
		
		this.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ListenerManager.getInstance().fireOnMessageToDelete(message);
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
