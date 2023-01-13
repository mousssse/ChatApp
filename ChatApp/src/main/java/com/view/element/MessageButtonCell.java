package main.java.com.view.element;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import main.java.com.controller.DBManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.Message;
import main.java.com.model.User;

public class MessageButtonCell extends ListCell<Message> implements UsernameListener {
    
	private HBox hbox = new HBox();
    private Label label = new Label("(empty)");
    private DeleteMessageButton button;
    private Message message;

    public MessageButtonCell() {
        super();
        
        this.label.setStyle("-fx-text-box-border: transparent; -fx-background-color: transparent;");
        this.label.setPrefWidth(this.getPrefWidth());
        this.label.setWrapText(true);
        
        this.button = new DeleteMessageButton();
        button.setVisible(false);
        
        Pane pane = new Pane();
        
        this.hbox.getChildren().addAll(this.label, pane, this.button);
        HBox.setHgrow(this.label, Priority.ALWAYS);
        HBox.setHgrow(pane, Priority.SOMETIMES);
        
        this.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered && !this.isEmpty() && !this.message.getContent().equals(DBManager.deletedMessage)) {
                button.setVisible(true);
            } else {
                button.setVisible(false);
            }
        });
    }
    
    private void updateUsername(User user, String newUsername) {
    	if (this.message.getFromUser().getId().equals(user.getId())) {
    		this.message.getFromUser().setUsername(newUsername);
    	}
    	else if (this.message.getToUser().getId().equals(user.getId())) {
    		this.message.getToUser().setUsername(newUsername);
    	}
    }

	@Override
	public void onUsernameModification(User user, String newUsername) {
		this.updateUsername(user, newUsername);
	}

	@Override
	public void onSelfUsernameModification(String newUsername) {
		this.updateUsername(OnlineUsersManager.getInstance().getLocalUser(), newUsername);
	}

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);
        setText(null);
        if (empty) {
        	this.message = null;
            setGraphic(null);
        } else {
        	this.message = message;
        	this.button.setMessage(this.message);
        	this.label.setText(message != null ? message.toString() : "<null>");
            setGraphic(this.hbox);
        }
    }
}