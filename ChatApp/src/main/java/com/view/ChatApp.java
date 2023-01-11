package main.java.com.view;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.NetworkManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.ThreadManager;

/**
 * @author sarah
 * @author Sandro
 *
 */
public class ChatApp extends Application {
	
    @Override
    public void start(Stage primaryStage) {
    	ListenerManager.getInstance();
    	DBManager.getInstance();
    	NetworkManager.getInstance();
    	OnlineUsersManager.getInstance();
    	ThreadManager.getInstance();
    	LoginStage.getInstance();
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
