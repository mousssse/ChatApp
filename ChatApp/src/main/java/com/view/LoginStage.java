package main.java.com.view;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;

/**
 * LoginStage is the sign-up / login window.
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class LoginStage extends Stage {
	
	private static LoginStage loginStage = null;
	
	private GridPane rootPane = new GridPane();
	private Label usernameLabel, passwordLabel;
	private TextField usernameField;
	private PasswordField passwordField;
	private Button loginButton;
	
	private int errorCount = 0;

	private void init() {
		String title;
		if (DBManager.getInstance().localUserIsInDB()) {
			title = "Login";
		} else {
			title = "Sign up";
		}

		this.rootPane.setVgap(10);
		this.rootPane.setHgap(5);
		this.usernameLabel = new Label("Username: ");
		this.rootPane.add(this.usernameLabel, 0, 0);
		this.usernameField = new TextField();
		this.rootPane.add(this.usernameField, 1, 0, 2, 1);
		this.passwordLabel = new Label("Password: ");
		this.rootPane.add(this.passwordLabel, 0, 1);
		this.passwordField = new PasswordField();
		this.rootPane.add(this.passwordField, 1, 1, 2, 1);

		this.loginButton = new Button(title);
		this.loginButton.setPrefWidth(80);
		this.loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					checkLogin();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		this.rootPane.add(this.loginButton, 1, 2, 1, 1);

		this.rootPane.setAlignment(Pos.CENTER);
		this.rootPane.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				this.loginButton.fire();
			}
		});
		Scene scene = new Scene(this.rootPane, 300, 150);
		scene.getStylesheets().add("css/style.css");
		this.setScene(scene);
		this.setTitle("ChatApp - " + title);
		this.setResizable(false);
		this.show();
	}

	/**
	 * 
	 * @throws SQLException - SQLException
	 */
	private void checkLogin() throws SQLException {
		String username = this.usernameField.getText();
		String password = this.passwordField.getText();

		boolean connected = false;
		String localUsername = DBManager.getInstance().getLocalUsername();
		if (localUsername == null) {
			ListenerManager.getInstance().fireOnSelfLoginDB(username, password);
			connected = true;
		} else {
			// TODO password should be hashed when checking w db
			connected = username.equals(localUsername) && password.equals(DBManager.getInstance().getHashedPassword());
		}

		if (connected) {
			// This will create the local user in OnlineUsersManager
			ListenerManager.getInstance().fireOnSelfLoginOnline(username);

			// As soon as the user is logged in, the ChatAppStage is created
			ChatAppStage.getInstance();

			if (OnlineUsersManager.getInstance().getLocalUser().getIP() == null) {
				// Display an error message if the user isn't connected to the Internet
				Alert offline = new Alert(AlertType.NONE);
				offline.getDialogPane().getButtonTypes().add(ButtonType.OK);
				offline.setTitle("Network warning");
				offline.setContentText(
						"You are currently offline. You can still check past conversations. \nTo connect to the app, close the app, connect to the Internet and re-launch the app.");
				offline.setHeight(200);
				offline.showAndWait();
			} else {
				// This will send a broadcast to say we exist
				ListenerManager.getInstance().fireOnSelfLoginNetwork();
			}

			// Closing the login window
			this.close();
		} else {
			// Display an error message if the user enters an invalid ID or password.
			Alert invalidCredentials = new Alert(AlertType.NONE);
			invalidCredentials.getDialogPane().getButtonTypes().add(ButtonType.OK);
			invalidCredentials.setTitle("Login error");
			String content = "Invalid username / password.";
			if (++this.errorCount > 2) {
				content += "\nTo reset the app, delete the chatApp.db file. You will lose all\npast conversations but will be able to create a new account.";
			}
			invalidCredentials.setContentText(content);;
			invalidCredentials.showAndWait();
		}
	}

	/**
	 * 
	 * @return the LoginStage instance.
	 */
	public static LoginStage getInstance() {
		if (loginStage == null) {
			loginStage = new LoginStage();
			loginStage.init();
		}
		return loginStage;
	}
}
