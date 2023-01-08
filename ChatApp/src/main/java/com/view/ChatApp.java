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
	
	private static ChatApp chatApp = null;
	
	/**
     * @return the ChatApp instance.
     */
    public static ChatApp getInstance() {
    	if (chatApp == null) chatApp = new ChatApp();
		return chatApp;
	}
	
    @Override
    public void start(Stage primaryStage) {
    	LoginStage.getInstance();
    }
    

    public static void main(String[] args) {
    	DBManager dbManager = DBManager.getInstance();
		NetworkManager networkManager = NetworkManager.getInstance();
		OnlineUsersManager onlineUsersManager = OnlineUsersManager.getInstance();
		ThreadManager threadManager = ThreadManager.getInstance();

		ListenerManager listenerManager = ListenerManager.getInstance();
		
		listenerManager.addDBListener(dbManager);
		listenerManager.addLoginListener(dbManager);
		
		listenerManager.addSelfLoginListener(networkManager);
		listenerManager.addUsernameListener(networkManager);
		
		listenerManager.addLoginListener(onlineUsersManager);
		listenerManager.addSelfLoginListener(onlineUsersManager);
		listenerManager.addUsernameListener(onlineUsersManager);
		
		listenerManager.addSelfLoginListener(threadManager);
		listenerManager.addLoginListener(threadManager);
		listenerManager.addChatListener(threadManager);
		
        launch(args);
    }
}
