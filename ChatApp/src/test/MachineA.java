package test;

import java.net.UnknownHostException;

import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.NetworkManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.ThreadManager;
import main.java.com.view.LoginWindow;

public class MachineA {
	
	public static void main(String[] args) {
		DBManager dbManager = DBManager.getInstance();
		NetworkManager networkManager = NetworkManager.getInstance();
		OnlineUsersManager onlineUsersManager = OnlineUsersManager.getInstance();
		ThreadManager threadManager = ThreadManager.getInstance();

		ListenerManager listenerManager = ListenerManager.getInstance();
		
		listenerManager.addDBListener(dbManager);
		listenerManager.addLoginListener(dbManager);
		listenerManager.addChatListener(dbManager);
		
		listenerManager.addSelfLoginListener(networkManager);
		
		listenerManager.addLoginListener(onlineUsersManager);
		listenerManager.addSelfLoginListener(onlineUsersManager);
		
		listenerManager.addSelfLoginListener(threadManager);
		listenerManager.addLoginListener(threadManager);
		listenerManager.addChatListener(threadManager);
		
		LoginWindow loginWindow;
		try {
			loginWindow = new LoginWindow();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

//		try {
//			Thread.sleep(1000);
//			User sandro = onlineUsersManager.getUserFromIP(InetAddress.getByName("192.168.1.106"));
//			//listenerManager.fireOnChatRequest(sandro);
//			//listenerManager.fireOnMessageToSend(onlineUsersManager.getLocalUser(), sandro, "heyo", LocalDateTime.now());
//			//listenerManager.fireOnChatClosure(sandro);
//		} catch (UnknownHostException | InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
