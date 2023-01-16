package main.java.com.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;

/**
 * UsernameModificationStage is the window that lets a user modify their
 * username.
 * 
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

	/**
	 * Checks if the chosen username is valid
	 * @param newUsername is the new username
	 * @return
	 */
	private boolean validUsernameFormat(String newUsername) {
		if (newUsername.isEmpty())
			return false;
		if (newUsername.length() > 20)
			return false;
		for (int i = 0; i < newUsername.length(); i++) {
			if ((Character.isLetterOrDigit(newUsername.charAt(i)) == false))
				return false;
		}
		return true;
	}

	private void init() {
		this.rootPane.add(this.usernameLabel, 0, 0);
		this.rootPane.add(this.usernameField, 1, 0);
		this.rootPane.add(this.usernameButton, 1, 1, 2, 1);
		this.usernameButton.setPrefWidth(80);

		this.usernameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String newUsername = usernameField.getText().trim();
				if (newUsername == null)
					return;
				if (!validUsernameFormat(newUsername)) {
					// Display an error message if the username is not of valid format
					Alert invalidUsername = new Alert(AlertType.NONE);
					invalidUsername.getDialogPane().getButtonTypes().add(ButtonType.OK);
					invalidUsername.setTitle("Username error");
					invalidUsername.setContentText(
							"A username can only contain letters and numbers, can't be longer than 20 characters, and shouldn't be empty.");
					invalidUsername.showAndWait();
				} else if (DBManager.getInstance()
						.updateUsername(OnlineUsersManager.getInstance().getLocalUser().getId(), newUsername)) {
					ListenerManager.getInstance().fireOnSelfUsernameModification(newUsername);
					usernameModifStage.hide();
					usernameField.setText(null);
				} else {
					// Display an error message if the username is already taken
					Alert invalidUsername = new Alert(AlertType.NONE);
					invalidUsername.getDialogPane().getButtonTypes().add(ButtonType.OK);
					invalidUsername.setTitle("Username error");
					invalidUsername.setContentText("Sorry, this username is already taken.");
					invalidUsername.showAndWait();
				}
			}
		});

		this.rootPane.setVgap(10);
		this.rootPane.setAlignment(Pos.CENTER);
		this.rootPane.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				this.usernameButton.fire();
			}
		});

		Scene scene = new Scene(this.rootPane, 300, 100);
		this.setScene(scene);
		this.setTitle("Username modification");
		this.setScene(scene);
		this.setResizable(false);
	}

	/**
	 * 
	 * @return the UsernameModificationStage instance.
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
