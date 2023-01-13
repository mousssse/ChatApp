package main.java.com.view.element;

import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import main.java.com.model.User;

public class OnlineUsersButtonCell extends ListCell<User> {
    private HBox hbox = new HBox();
    private TextField text = new TextField("(empty)");
    private Pane pane = new Pane();
    private ChatRequestButton button;
    private User remoteUser;

    public OnlineUsersButtonCell() {
        super();
        this.text.setEditable(false);
        this.text.setStyle("-fx-text-box-border: transparent; -fx-background-color: transparent;");
        this.text.setPrefWidth(this.getWidth());
        this.button = new ChatRequestButton();
        this.button.setText(ChatRequestButton.requestChat);
        this.hbox.getChildren().addAll(this.text, this.pane, this.button);
        HBox.setHgrow(this.text, Priority.ALWAYS);
        HBox.setHgrow(this.pane, Priority.SOMETIMES);
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        setText(null);
        if (empty) {
        	this.remoteUser = null;
            setGraphic(null);
        } else {
        	this.remoteUser = user;
        	this.button.setRemoteUser(this.remoteUser);
        	this.text.setText(user!=null ? user.toString() : "<null>");
            setGraphic(this.hbox);
        }
    }
}