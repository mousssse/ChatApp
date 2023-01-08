package main.java.com.view;

import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.ThreadManager;
import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.User;

/**
 * @author sarah
 * @author Sandro
 *
 */
public class ChatAppStage extends Stage implements LoginListener, UsernameListener {
    private static ChatAppStage chatAppStage = null;
	private BorderPane rootPane = new BorderPane();
	private ListView<User> users;
	private ObservableList<User> userListVector;
	private ListView<String> offlineUsers;
	private ObservableList<String> offlineUserListVector;
	private Label usernameLabel;
	private Button usernameButton;
	
	
	private boolean idIsOnline(final String id){
	    return this.userListVector.stream().filter(user -> user.getId().equals(id)).findFirst().isPresent();
	}
	
	private void initUserListVectors() {
		// Online users
		this.userListVector = FXCollections.observableArrayList();
        this.users = new ListView<User>();
        this.users.setItems(this.userListVector);
        
        // Offline users
        this.offlineUserListVector = FXCollections.observableArrayList();
        for (Entry<String, String> usernameEntry : DBManager.getInstance().getAllUsernames().entrySet()) {
        	if (!this.idIsOnline(usernameEntry.getKey())) {
        		this.offlineUserListVector.add(usernameEntry.getValue());
        		System.out.println(usernameEntry.getValue() + " is offline.");
        	}
        }
        this.offlineUsers = new ListView<String>();
		this.offlineUsers.setItems(this.offlineUserListVector);
	}
	
	
    private void init() {
    	this.initUserListVectors();
    	
    	ScrollPane onlineScrollPane = new ScrollPane();
    	onlineScrollPane.setFitToWidth(true);
    	onlineScrollPane.setStyle("-fx-focus-color: transparent;");
        onlineScrollPane.setContent(this.users);
        VBox onlineBox = new VBox();
        onlineBox.getChildren().add(new Label("Online users"));
        onlineBox.getChildren().add(onlineScrollPane);

        ScrollPane offlineScrollPane = new ScrollPane();
    	offlineScrollPane.setFitToWidth(true);
    	offlineScrollPane.setStyle("-fx-focus-color: transparent;");
    	offlineScrollPane.setContent(this.offlineUsers);
        VBox offlineBox = new VBox();
        offlineBox.getChildren().add(new Label("Offline users"));
        offlineBox.getChildren().add(offlineScrollPane);
        
        GridPane usernamePane = new GridPane();
        this.usernameLabel = new Label("My username: " + OnlineUsersManager.getInstance().getLocalUser().getUsername());
        this.usernameButton = new Button("Change");
        this.usernameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				UsernameModificationStage.getInstance().show();
			}
		});
        usernamePane.add(usernameLabel, 0, 0);
        usernamePane.add(this.usernameButton, 1, 0);
        usernamePane.setHgap(5);
        usernamePane.setAlignment(Pos.BOTTOM_RIGHT);
        usernamePane.setStyle("-fx-focus-color: transparent;");
        
        this.users.addEventHandler(MouseEvent.MOUSE_CLICKED, count -> {
        	// On double-click on an online user's username, open chat frame with that user
        	if (count.getClickCount() > 1) {
        		 User remoteUser = users.getSelectionModel().getSelectedItem();
        		 ChatStage chatStage = new ChatStage(remoteUser, true);
                 ListenerManager.getInstance().addChatListener(chatStage);
                 ListenerManager.getInstance().addUsernameListener(chatStage);
                 // The chat request will only be sent from one person
                 if (!ThreadManager.getInstance().conversationExists(remoteUser.getId())) ListenerManager.getInstance().fireOnChatRequest(remoteUser);
                 // TODO accept chat request window
        	}
        });
        
        this.offlineUsers.addEventHandler(MouseEvent.MOUSE_CLICKED, count -> {
        	// On double-click on an online user's username, open chat frame with that user
        	if (count.getClickCount() > 1) {
        		String username = offlineUsers.getSelectionModel().getSelectedItem();
        		String id = DBManager.getInstance().getIdFromUsername(username);
        		new ChatStage(new User(id, username, null, 0), false);
        	}
        });
        
        this.rootPane.setTop(onlineBox);
        this.rootPane.setCenter(offlineBox);
        this.rootPane.setBottom(usernamePane);
        Scene scene = new Scene(this.rootPane, 400, 600);
        this.setScene(scene);
    	this.setTitle("ChatApp");
        
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	@Override
        	public void handle(WindowEvent e) {
        		ListenerManager.getInstance().fireOnSelfLogout();
        		Platform.exit();
        		System.exit(0);
        	}
        });

        this.show();
    }
    
    
    /**
     * 
     * @return the ChatApp stage instance.
     */
    public static ChatAppStage getInstance() {
    	if (chatAppStage == null) {
    		chatAppStage = new ChatAppStage();
    		chatAppStage.init();
    	}
		return chatAppStage;
	}


	@Override
	public void onLogin(User remoteUser) {
		Platform.runLater(() -> {
			this.userListVector.add(remoteUser);
			this.offlineUserListVector.remove(remoteUser.getUsername());
		});
	}


	@Override
	public void onLogout(User remoteUser) {
		Platform.runLater(() -> {
			this.userListVector.remove(remoteUser);
			this.offlineUserListVector.add(remoteUser.getUsername());
		});
	}


	@Override
	public void onUsernameModification(User user, String newUsername) {
		user.setUsername(newUsername);
		Platform.runLater(() -> {
			this.userListVector.remove(user);
			this.userListVector.add(user);
		});
	}


	@Override
	public void onSelfUsernameModification(String newUsername) {
		Platform.runLater(() -> this.usernameLabel.textProperty().bind(new SimpleStringProperty("My username: " + newUsername)));
	}
}
