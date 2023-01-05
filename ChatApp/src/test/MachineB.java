package test;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.NetworkManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.ThreadManager;
import main.java.com.view.OnlineUsersFrame;

public class MachineB {

	public static void main(String[] args) {
		DBManager dbManager = DBManager.getInstance();
		NetworkManager networkManager = NetworkManager.getInstance();
		OnlineUsersManager onlineUsersManager = OnlineUsersManager.getInstance();
		ThreadManager threadManager = ThreadManager.getInstance();
		OnlineUsersFrame onlineUsersFrame = new OnlineUsersFrame();
		ListenerManager listenerManager = ListenerManager.getInstance();
		
		listenerManager.addDBListener(dbManager);
		listenerManager.addLoginListener(dbManager);
		
		listenerManager.addSelfLoginListener(networkManager);
		
		listenerManager.addLoginListener(onlineUsersManager);
		listenerManager.addSelfLoginListener(onlineUsersManager);
		
		listenerManager.addSelfLoginListener(threadManager);
		listenerManager.addLoginListener(threadManager);
		listenerManager.addChatListener(threadManager);
		
		listenerManager.addLoginListener(onlineUsersFrame);
		
        JFrame frame = new JFrame("Online users");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	ListenerManager.getInstance().fireOnSelfLogout();
            	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        // TODO When the user closes the OnlineUsersFrame, the user is choosing to log out. To be managed in LoginWindow!
        frame.setSize(400, 600);
        frame.getContentPane().add(onlineUsersFrame, BorderLayout.CENTER);
        frame.setVisible(true);
		
		ListenerManager.getInstance().fireOnSelfLogin("sandro", "betterPassevorde");
	}
}
