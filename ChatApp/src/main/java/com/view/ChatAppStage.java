package main.java.com.view;

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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
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
	private Label usernameLabel;
	private Button usernameButton;
	
	
    private void init() {
    	ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-focus-color: transparent;");
    	this.userListVector = FXCollections.observableArrayList();
        this.users = new ListView<User>(this.userListVector);
        scrollPane.setContent(this.users);
        
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
        
        users.addEventHandler(MouseEvent.MOUSE_CLICKED, count -> {
        	// On double-click on an online user's username, open chat frame with that user
        	if (count.getClickCount() > 1) {
        		 User remoteUser = users.getSelectionModel().getSelectedItem();
                 ListenerManager.getInstance().addChatListener(new ChatStage(remoteUser));
                 // TODO Think more about this: the request will be sent from both PCs since both will double-click.
                 // solution: auto-pop the frame for the receiving user? 
                 ListenerManager.getInstance().fireOnChatRequest(remoteUser);
        	}
        });
        
        this.rootPane.setCenter(scrollPane);
        this.rootPane.setBottom(usernamePane);
        Scene scene = new Scene(this.rootPane, 400, 600);
        this.setScene(scene);
    	this.setTitle("ChatApp");
        
        this.setOnCloseRequest(new EventHandler<WindowEvent>() {
        	@Override
        	public void handle(WindowEvent e) {
        		ListenerManager.getInstance().fireOnSelfLogout();
        		chatAppStage.close();
        		UsernameModificationStage.getInstance().close();
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
		Platform.runLater(() -> this.userListVector.add(remoteUser));
	}


	@Override
	public void onLogout(User remoteUser) {
		Platform.runLater(() -> this.userListVector.remove(remoteUser));
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
