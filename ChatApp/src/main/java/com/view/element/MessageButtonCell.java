package main.java.com.view.element;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.java.com.controller.DBManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.Message;
import main.java.com.model.User;
import main.java.com.view.ChatAppStage;

public class MessageButtonCell extends ListCell<Message> implements UsernameListener {
    
	private HBox hbox = new HBox();
    private Label label;
	private Text time = new Text();
	private Text from = new Text();
	private Text content = new Text();
    private DeleteMessageButton button = new DeleteMessageButton();
    
    private Message message;
    private User remoteUser;
    private boolean messageIsFromLocalUser;

    public MessageButtonCell() {
        super();
        
        this.label = new Label(null, new TextFlow(this.time, this.from, this.content));
        this.label.setStyle("-fx-text-box-border: transparent; -fx-background-color: transparent;");
        this.label.setPrefWidth(this.getPrefWidth());
        this.label.setWrapText(true);
        
        Pane pane = new Pane();
        button.setVisible(false);
        
        this.hbox.getChildren().addAll(this.label, pane, this.button);
        HBox.setHgrow(this.label, Priority.ALWAYS);
        HBox.setHgrow(pane, Priority.SOMETIMES);
        
        this.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered && !this.isEmpty() && ChatAppStage.getInstance().conversationLaunchedWith(this.remoteUser.getId()) && this.messageIsFromLocalUser && !this.message.getContent().equals(DBManager.deletedMessage)) {
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
        	this.content.setText(message.getContent());
        	if (this.message.getContent().equals(DBManager.deletedMessage)) {
        		// TODO some messages are italic when they shouldn't
        		this.content.setStyle("-fx-font-style: italic;");
        	}
        	
        	this.time.setText("[" + message.getDate().format(Message.formatter) + "] ");
        	this.from.setText(message.getFromUser().getUsername() + ": ");

            this.messageIsFromLocalUser = this.message.getFromUser().getId().equals(OnlineUsersManager.getInstance().getLocalUser().getId());
            if (this.messageIsFromLocalUser) {
            	this.remoteUser = message.getToUser();
            }
            else {
            	this.remoteUser = message.getFromUser();
            }
            setGraphic(this.hbox);
        }
    }
}