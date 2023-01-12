package main.java.com.view.element;

import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import main.java.com.model.User;

public class ButtonCell extends ListCell<User> {
    private HBox hbox = new HBox();
    private TextField text = new TextField("(empty)");
    private Pane pane = new Pane();
    private ChatRequestButton button;
    private User remoteUser;

    public ButtonCell() {
        super();
        this.text.setEditable(false);
        this.text.setStyle("-fx-text-box-border: transparent; -fx-background-color: transparent;");
        this.text.setPrefWidth(this.text.getText().length() * 9);
        this.button = new ChatRequestButton();
        this.hbox.getChildren().addAll(this.text, this.pane, this.button);
        HBox.setHgrow(this.pane, Priority.ALWAYS);
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
        	this.text.setText(user!=null ? user.getUsername() : "<null>");
            setGraphic(this.hbox);
        }
    }
}