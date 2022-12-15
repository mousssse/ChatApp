package test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.NetworkManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.ThreadManager;
import main.java.com.model.User;

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
		
		listenerManager.fireOnSelfLogin("mousse", "pwd");
		try {
			Thread.sleep(1000);
			User sandro = onlineUsersManager.getUserFromIP(InetAddress.getByName("10.32.46.16"));
			listenerManager.fireOnChatRequest(sandro);
			listenerManager.fireOnMessageToSend(onlineUsersManager.getLocalUser(), sandro, "heyo", LocalDateTime.now());
		} catch (UnknownHostException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
