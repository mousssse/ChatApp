package main.java.com.view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.ChatRequestListener;
import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;
import main.java.com.view.element.OnlineUsersButtonCell;
import main.java.com.view.element.ChatRequestButton;

/**
 * ChatAppStage is the window where all ChatApp users are displayed, separated
 * into online and offline categories.
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class ChatAppStage extends Stage implements LoginListener, UsernameListener, ChatListener, ChatRequestListener {

	private static ChatAppStage chatAppStage = null;
	private VBox rootBox = new VBox();

	private Label usernameLabel;
	private Button usernameButton;

	private ListView<User> users;
	private ObservableList<User> userListVector;
	private ListView<User> offlineUsers;
	private ObservableList<User> offlineUserListVector;

	private Map<String, ChatStage> chatStageMap = new HashMap<String, ChatStage>();

	private Map<String, List<ChatRequestButton>> chatRequestButtons = new HashMap<String, List<ChatRequestButton>>();

	public ChatAppStage() {
		ListenerManager.getInstance().addLoginListener(this);
		ListenerManager.getInstance().addUsernameListener(this);
		ListenerManager.getInstance().addChatListener(this);
		ListenerManager.getInstance().addChatRequestListener(this);
	}

	/**
	 * 
	 * @param id is the user ID
	 * @return true if the user associated to id is online; false otherwise.
	 */
	private boolean idIsOnline(String id) {
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
				this.offlineUserListVector.add(new User(usernameEntry.getKey(), usernameEntry.getValue(), null, 0));
			}
		}
		this.offlineUsers = new ListView<User>();
		this.offlineUsers.setItems(this.offlineUserListVector);
	}

	private void initLocalUserOnline() {
		this.initUserListVectors();

		Label onlineLabel = new Label("Online users");
		onlineLabel.setPadding(new Insets(2));
		Label offlineLabel = new Label("Offline users");
		offlineLabel.setPadding(new Insets(2));

		GridPane usernamePane = new GridPane();
		this.usernameLabel = new Label("My username: " + OnlineUsersManager.getInstance().getLocalUser().getUsername());
		this.usernameButton = new Button("Change");
		this.usernameButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				UsernameModificationStage usernameStage = UsernameModificationStage.getInstance();
				usernameStage.show();
				usernameStage.setIconified(false);
				usernameStage.toFront();
			}
		});
		usernamePane.add(usernameLabel, 0, 0);
		if (OnlineUsersManager.getInstance().getLocalUser().getIP() != null) {
			usernamePane.add(this.usernameButton, 1, 0);
		}
		usernamePane.setHgap(5);
		usernamePane.setPadding(new Insets(5));
		usernamePane.setAlignment(Pos.BOTTOM_RIGHT);
		usernamePane.setStyle("-fx-focus-color: transparent;");

		this.users.addEventHandler(MouseEvent.MOUSE_CLICKED, count -> {
			// On double-click on an online user's username, open chat window with that user
			if (count.getClickCount() > 1) {
				User remoteUser = users.getSelectionModel().getSelectedItem();
				if (remoteUser == null)
					return;
				ChatStage chatStage = this.chatStageMap.get(remoteUser.getId());
				if (chatStage == null) {
					chatStage = new ChatStage(remoteUser, getButtonCurrentString(remoteUser.getId()), true,
							conversationLaunchedWith(remoteUser.getId()));
					this.chatStageMap.put(remoteUser.getId(), chatStage);
				} else {
					chatStage.setIconified(false);
					chatStage.toFront();
				}
				chatStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent e) {
						chatStageMap.remove(remoteUser.getId());
					}
				});
			}
		});

		this.offlineUsers.addEventHandler(MouseEvent.MOUSE_CLICKED, count -> {
			// On double-click on an online user's username, open chat window with that user
			if (count.getClickCount() > 1) {
				User remoteUser = offlineUsers.getSelectionModel().getSelectedItem();
				if (remoteUser == null)
					return;
				ChatStage chatStage = this.chatStageMap.get(remoteUser.getId());
				if (chatStage == null) {
					chatStage = new ChatStage(remoteUser, getButtonCurrentString(remoteUser.getId()), false, false);
					chatStage.setConversationLaunched(false);
					this.chatStageMap.put(remoteUser.getId(), chatStage);
				} else {
					chatStage.setIconified(false);
					chatStage.toFront();
				}
				chatStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent e) {
						chatStageMap.remove(remoteUser.getId());
					}
				});
			}
		});

		VBox.setVgrow(this.users, Priority.ALWAYS);
		VBox.setVgrow(this.offlineUsers, Priority.ALWAYS);

		this.rootBox.getChildren().addAll(onlineLabel, this.users, offlineLabel, this.offlineUsers, usernamePane);
		Scene scene = new Scene(this.rootBox, 400, 600);
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

		this.users.setFocusTraversable(false);
		this.users.setStyle("-fx-selection-bar-non-focused: -fx-control-inner-background;");
		this.users.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
			@Override
			public ListCell<User> call(ListView<User> param) {
				return new OnlineUsersButtonCell();
			}
		});

		this.setMinHeight(400);
		this.setMinWidth(250);
		this.show();
	}

	private void initLocalUserOffline() {
		this.offlineUserListVector = FXCollections.observableArrayList();
		for (Entry<String, String> usernameEntry : DBManager.getInstance().getAllUsernames().entrySet()) {
			this.offlineUserListVector.add(new User(usernameEntry.getKey(), usernameEntry.getValue(), null, 0));
		}
		this.offlineUsers = new ListView<User>();
		this.offlineUsers.setItems(this.offlineUserListVector);

		Label offlineLabel = new Label("Offline users");
		offlineLabel.setPadding(new Insets(2));

		HBox usernameBox = new HBox();
		Label offlineWarning = new Label("You are currently offline.");
		Pane pane = new Pane();
		this.usernameLabel = new Label("My username: " + OnlineUsersManager.getInstance().getLocalUser().getUsername());
		usernameBox.getChildren().addAll(offlineWarning, pane, this.usernameLabel);

		HBox.setHgrow(pane, Priority.ALWAYS);
		usernameBox.setPadding(new Insets(5));
		usernameBox.setStyle("-fx-focus-color: transparent;");

		this.offlineUsers.addEventHandler(MouseEvent.MOUSE_CLICKED, count -> {
			// On double-click on an online user's username, open chat window with that user
			if (count.getClickCount() > 1) {
				User remoteUser = offlineUsers.getSelectionModel().getSelectedItem();
				if (remoteUser == null)
					return;
				ChatStage chatStage = this.chatStageMap.get(remoteUser.getId());
				if (chatStage == null) {
					chatStage = new ChatStage(remoteUser, getButtonCurrentString(remoteUser.getId()), false, false);
					chatStage.setConversationLaunched(false);
					this.chatStageMap.put(remoteUser.getId(), chatStage);
				} else {
					chatStage.setIconified(false);
					chatStage.toFront();
				}
				chatStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent e) {
						chatStageMap.remove(remoteUser.getId());
					}
				});
			}
		});

		VBox.setVgrow(this.offlineUsers, Priority.ALWAYS);

		this.rootBox.getChildren().addAll(offlineLabel, this.offlineUsers, usernameBox);
		Scene scene = new Scene(this.rootBox, 400, 600);
		scene.getStylesheets().add("css/style.css");
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

		this.setMinHeight(400);
		this.setMinWidth(300);
		this.show();
	}

	/**
	 * 
	 * @return the ChatAppStage instance.
	 */
	public static ChatAppStage getInstance() {
		if (chatAppStage == null) {
			chatAppStage = new ChatAppStage();
			if (OnlineUsersManager.getInstance().getLocalUser().getIP() == null) {
				chatAppStage.initLocalUserOffline();
			} else {
				chatAppStage.initLocalUserOnline();
			}
		}
		return chatAppStage;
	}

	/**
	 * adds a button to the map of all request buttons
	 * 
	 * @param id     the user's id to which the button is associated to
	 * @param button the button to add
	 */
	public void addButton(String id, ChatRequestButton button) {
		List<ChatRequestButton> buttons = this.chatRequestButtons.get(id);
		if (buttons == null) {
			this.chatRequestButtons.put(id, new ArrayList<ChatRequestButton>(Arrays.asList(button)));
		} else {
			buttons.add(button);
		}
	}

	/**
	 * Updates the text shown on all the buttons associated with the same user
	 * 
	 * @param id          the id of the user associated to the buttons we want to
	 *                    update
	 * @param updatedText the text to be shown on the buttons
	 */
	public void updateButtons(String id, String updatedText) {
		List<ChatRequestButton> buttons = this.chatRequestButtons.get(id);
		if (buttons != null) {
			for (ChatRequestButton button : buttons) {
				Platform.runLater(() -> button.setText(updatedText));
			}
		}
	}

	/**
	 * @param id the id of the user associated with the button we want to get the
	 *           current text from
	 * @return the string that's currently set on the first button in the map
	 */
	private String getButtonCurrentString(String id) {
		List<ChatRequestButton> buttons = this.chatRequestButtons.get(id);
		if (buttons != null) {
			return buttons.get(0).getText();
		}
		return null;
	}

	/**
	 * @param id the id of the user associated with the conversation
	 * @return true if the conversation has been accepted already; false otherwise
	 */
	public boolean conversationLaunchedWith(String id) {
		if (this.chatRequestButtons.get(id).get(0).getText() == null)
			return false;
		return this.chatRequestButtons.get(id).get(0).getText().equals(ChatRequestButton.endChat);
	}

	@Override
	public void onLogin(User remoteUser) {
		Platform.runLater(() -> {
			this.userListVector.add(remoteUser);

			if (!this.offlineUserListVector.remove(remoteUser)) {
				// The offline user wasn't found.
				// It can either be a user we have never met,
				// or a user that has changed username
				User toRemove = null;
				for (User offlineUser : this.offlineUserListVector) {
					if (offlineUser.getId().equals(remoteUser.getId())) {
						toRemove = offlineUser;
						break;
					}
				}
				this.offlineUserListVector.remove(toRemove);
			}
		});
	}

	@Override
	public void onLogout(User remoteUser) {
		Platform.runLater(() -> {
			this.userListVector.remove(remoteUser);
			this.offlineUserListVector.add(remoteUser);
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
		Platform.runLater(
				() -> this.usernameLabel.textProperty().bind(new SimpleStringProperty("My username: " + newUsername)));
	}

	@Override
	public void onChatRequestReceived(User remoteUser) {
		this.updateButtons(remoteUser.getId(), ChatRequestButton.acceptRequest);
	}

	@Override
	public void onChatRequest(User remoteUser) {
		this.updateButtons(remoteUser.getId(), ChatRequestButton.cancelRequest);
	}

	@Override
	public void onChatClosure(User remoteUser) {
		this.updateButtons(remoteUser.getId(), ChatRequestButton.requestChat);
		ChatStage chatStage = this.chatStageMap.get(remoteUser.getId());
		if (chatStage != null) {
			chatStage.setConversationLaunched(false);
		}
	}

	@Override
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date,
			MessageType type) {
		// Nothing to do
	}

	@Override
	public void onMessageToReceive(Message message) {
		// Nothing to do
	}

	@Override
	public void onMessageToDelete(Message message) {
		// Nothing to do
	}

	@Override
	public void onChatAcceptRequest(User remoteUser) {
		this.updateButtons(remoteUser.getId(), ChatRequestButton.endChat);
		ChatStage chatStage = this.chatStageMap.get(remoteUser.getId());
		if (chatStage != null) {
			chatStage.setConversationLaunched(true);
		}
	}

	@Override
	public void onChatCancelRequest(User remoteUser) {
		this.updateButtons(remoteUser.getId(), ChatRequestButton.requestChat);
	}

	@Override
	public void onChatAcceptedRequest(User remoteUser) {
		this.updateButtons(remoteUser.getId(), ChatRequestButton.endChat);
		ChatStage chatStage = this.chatStageMap.get(remoteUser.getId());
		if (chatStage != null) {
			chatStage.setConversationLaunched(true);
		}
	}

	@Override
	public void onChatCancelledRequest(User remoteUser) {
		this.updateButtons(remoteUser.getId(), ChatRequestButton.requestChat);
	}
}
