package test;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import javax.swing.JFrame;

import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.NetworkManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.ThreadManager;
import main.java.com.model.User;
import main.java.com.view.OnlineUsersFrame;

public class MachineA {
	
	public static void main(String[] args) {
		DBManager dbManager = DBManager.getInstance();
		NetworkManager networkManager = NetworkManager.getInstance();
		OnlineUsersManager onlineUsersManager = OnlineUsersManager.getInstance();
		ThreadManager threadManager = ThreadManager.getInstance();
		OnlineUsersFrame onlineUsersFrame = new OnlineUsersFrame();
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
		
		listenerManager.addLoginListener(onlineUsersFrame);
		
        JFrame frame = new JFrame("Online users");
        // TODO When the user closes the OnlineUsersFrame, the user is choosing to log out. To be managed in LoginWindow!
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.getContentPane().add(onlineUsersFrame, BorderLayout.CENTER);
        frame.setVisible(true);
		
		listenerManager.fireOnSelfLogin("mousse", "pwd");
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
