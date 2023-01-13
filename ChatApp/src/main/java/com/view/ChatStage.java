package main.java.com.view;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;
import main.java.com.view.element.ChatRequestButton;
import main.java.com.view.element.MessageButtonCell;

/**
 * @author sarah
 * @author Sandro
 *
 */
public class ChatStage extends Stage implements ChatListener, UsernameListener, LoginListener {
	
	private User remoteUser;
	private boolean isOnline;
	private boolean conversationLaunched;
	
	private ObservableList<Message> vector = FXCollections.observableArrayList();
	private ListView<Message> messageList = new ListView<>();
	
	private BorderPane rootPane = new BorderPane();
	private VBox inputBox = new VBox();
	private TextField inputField = new TextField();
	private ChatRequestButton requestButton = new ChatRequestButton();
	private Label onlineLabel = new Label();
	
	private Double minWidth = (double) 400;
	private Double minHeight = (double) 300;
	
	public ChatStage(User remoteUser, String initButtonText, boolean isOnline, boolean conversationLaunched) {
		ListenerManager.getInstance().addChatListener(this);
		ListenerManager.getInstance().addUsernameListener(this);
		ListenerManager.getInstance().addLoginListener(this);
		
		this.remoteUser = remoteUser;
		this.requestButton.setRemoteUser(this.remoteUser);
		this.requestButton.setText(initButtonText);
		this.isOnline = isOnline;
		this.conversationLaunched = conversationLaunched;

		this.updateMessageVector();
        this.messageList.setItems(this.vector);
        this.messageList.setStyle("-fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");
		this.messageList.scrollTo(this.vector.size());
        
    	this.updateInputBox();
		this.inputBox.setPadding(new Insets(8));
        this.rootPane.setCenter(this.messageList);
        this.rootPane.setBottom(this.inputBox);
        Scene scene = new Scene(this.rootPane, 500, 500);
        //scene.getStylesheets().add("path to CSS file");
        this.setMinWidth(this.minWidth);
        this.setMinHeight(this.minHeight);
        this.setScene(scene);
        this.setTitle("Conversation with " + this.remoteUser.getUsername());
        
        this.messageList.setMinWidth(this.getWidth());
        
        this.messageList.setFocusTraversable(false);
		this.messageList.setStyle("-fx-selection-bar-non-focused: -fx-control-inner-background;");
		this.messageList.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
			@Override
            public ListCell<Message> call(ListView<Message> param) {
                MessageButtonCell cell = new MessageButtonCell();
                cell.setPrefWidth(minWidth);
                return cell;
            }
        });
		
        this.inputField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String messageContent = inputField.getText().trim();
                // The empty string is not allowed.
                if (!messageContent.isEmpty()) {
    				ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), remoteUser, messageContent, LocalDateTime.now(), MessageType.MESSAGE);
    				// Resetting the text field.
    				inputField.setText(null);
                }
            }
        });
        
        this.show();
	}
	
	public void setConversationLaunched(boolean conversationLaunched) {
		this.conversationLaunched = conversationLaunched;
		Platform.runLater(() -> this.updateInputBox());
	}
	
	private void updateInputBox() {
		this.inputBox.getChildren().clear();
        if (this.isOnline && this.conversationLaunched) {
        	this.inputField.setPromptText("Write a message...");
    		HBox hbox = new HBox(this.inputField, this.requestButton);
    		HBox.setHgrow(this.inputField, Priority.ALWAYS);
    		hbox.setAlignment(Pos.CENTER);
    		hbox.setSpacing(5);
    		this.inputBox.getChildren().add(hbox);
        }
        else if (this.isOnline) {
        	this.onlineLabel.setText("Conversation with " + this.remoteUser.getUsername() + " hasn't been requested/accepted.");
        	Pane pane = new Pane();
        	HBox hbox = new HBox(onlineLabel, pane, this.requestButton);
        	HBox.setHgrow(pane, Priority.ALWAYS);
        	hbox.setAlignment(Pos.CENTER_LEFT);
        	this.inputBox.getChildren().add(hbox);
        }
        else {
        	this.inputBox.getChildren().add(new Label(this.remoteUser.getUsername() + " is offline. You can't send them messages."));
        }
	}
    
    public boolean remoteUserIsOnline() {
    	return this.isOnline;
    }
    
    private void updateMessageVector() {
    	this.vector.clear();
    	try {
    		vector.addAll(DBManager.getInstance().getConversationHistory(this.remoteUser.getId()));
		} catch (SQLException e) {
			//Display an error message if the database could not retrieve history
			Alert historyNotLoaded = new Alert(AlertType.NONE);
			historyNotLoaded.getDialogPane().getButtonTypes().add(ButtonType.OK);
			historyNotLoaded.setTitle("History error");
			historyNotLoaded.setContentText("Could not load conversation history with " + this.remoteUser.getUsername());
			historyNotLoaded.showAndWait();
		}
    }

	@Override
	public void onUsernameModification(User user, String newUsername) {
		if (user.getId().equals(this.remoteUser.getId())) {
			user.setUsername(newUsername);
			this.remoteUser = user;
			Platform.runLater(() -> {
				this.setTitle("Conversation with " + newUsername);
				this.updateMessageVector();
				this.onlineLabel.setText("Conversation with " + this.remoteUser.getUsername() + " hasn't been requested/accepted.");
			});
		}
	}

	@Override
	public void onSelfUsernameModification(String newUsername) {
		this.updateMessageVector();
	}
	
	@Override
	public void onChatRequestReceived(User remoteUser) {
		// Nothing to do
	}

	@Override
	public void onChatRequest(User remoteUser) {
		// Nothing to do
	}

	@Override
	public void onChatClosureReceived(User remoteUser) {
		if (remoteUser.getId().equals(this.remoteUser.getId())) {
			this.conversationLaunched = false;
			Platform.runLater(() -> this.updateInputBox());
		}
	}
	
	@Override
	public void onChatClosure(User remoteUser) {
		if (remoteUser.getId().equals(this.remoteUser.getId())) {
			this.conversationLaunched = false;
			Platform.runLater(() -> this.updateInputBox());
		}
	}

	@Override
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date,	MessageType type) {
		Platform.runLater(() -> {
			if (type == MessageType.MESSAGE) {
				vector.add(new Message(localUser, remoteUser, messageContent, date, type));
				this.messageList.scrollTo(this.vector.size());
			}
		});
	}

	@Override
	public void onMessageToReceive(Message message) {
		Platform.runLater(() -> {
			vector.add(message);
			this.messageList.scrollTo(this.vector.size());
		});
	}

	@Override
	public void onLogin(User remoteUser) {
		if (remoteUser.getId().equals(this.remoteUser.getId())) {
			this.isOnline = true;
			Platform.runLater(() -> {
				this.requestButton.setText(ChatRequestButton.requestChat);
				this.updateInputBox();
			});
		}
	}

	@Override
	public void onLogout(User remoteUser) {
		if (remoteUser.getId().equals(this.remoteUser.getId())) {
			this.isOnline = false;
			Platform.runLater(() -> this.updateInputBox());
		}
	}

	@Override
	public void onMessageToDelete(Message message) {
		if (message.getToUser().getId().equals(this.remoteUser.getId()) || message.getFromUser().getId().equals(this.remoteUser.getId())) {
			Platform.runLater(() -> this.updateMessageVector());
		}
	}
}
