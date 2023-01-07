package main.java.com.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;

/**
 * @author sarah
 * @author Sandro
 *
 */
public class UsernameModificationStage extends Stage {
    private static UsernameModificationStage usernameModifStage = null;
	private GridPane rootPane = new GridPane();
	private Label usernameLabel = new Label("New username: ");
	private TextField usernameField = new TextField();
	private Button usernameButton = new Button("OK");
	
	
    private void init() {
    	this.rootPane.add(this.usernameLabel, 0, 0);
    	this.rootPane.add(this.usernameField, 1, 0);
    	this.rootPane.add(this.usernameButton, 1, 1, 2, 1);
    	this.usernameButton.setPrefWidth(80);
    	
    	this.usernameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String newUsername = usernameField.getText();
				if (DBManager.getInstance().updateUsername(OnlineUsersManager.getInstance().getLocalUser().getId(), newUsername)) {
					ListenerManager.getInstance().fireOnSelfUsernameModification(newUsername);
					usernameModifStage.hide();
					usernameField.setText(null);
				}
				else {
					//Display an error message if the username is already taken
					Alert invalidCredentials = new Alert(AlertType.NONE);
					invalidCredentials.getDialogPane().getButtonTypes().add(ButtonType.OK);
					invalidCredentials.setTitle("Username error");
					invalidCredentials.setContentText("Sorry, this username is already taken");
					invalidCredentials.showAndWait();
				}
			}
    	});

    	this.rootPane.setVgap(10);
    	this.rootPane.setAlignment(Pos.CENTER);
    	
        Scene scene = new Scene(this.rootPane, 300, 100);
        this.setScene(scene);
    	this.setTitle("Username modification");
    	this.setScene(scene);
        this.setResizable(false);
    }
    
    
    /**
     * 
     * @return the username modification stage instance.
     */
    public static UsernameModificationStage getInstance() {
    	if (usernameModifStage == null) {
    		usernameModifStage = new UsernameModificationStage();
    		usernameModifStage.init();
    	}
		return usernameModifStage;
	}
    
    
    public Stage getStage() {
		return this;
	}
}
