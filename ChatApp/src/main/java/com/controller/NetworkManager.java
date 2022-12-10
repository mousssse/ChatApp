package main.java.com.controller;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import main.java.com.controller.listener.SelfLoginListener;
import main.java.com.model.TCPServer;
import main.java.com.model.UDPServer;
import main.java.com.model.User;

/**
 * TODO For now this class is absolutely useless but I can't find the courage to delete it quite yet.
 * @author Sandro
 * @author sarah
 *
 */
public class NetworkManager implements SelfLoginListener {
	
	private UDPServer UDPserver;
	private TCPServer TCPserver;
	
	private List<Socket> distantSockets;
	
	private static NetworkManager networkManager;
	
	private NetworkManager() {
		this.distantSockets = new ArrayList<>();
		this.UDPserver = new UDPServer();
		this.TCPserver = new TCPServer();
		(new Thread(this.UDPserver, "UDP Server")).start();
		(new Thread(this.TCPserver, "TCP Server")).start();
	}
	
	public static NetworkManager getInstance() {
		if (networkManager == null) networkManager = new NetworkManager();
		return networkManager;
	}
	
	public void addDistantSocket(Socket socket) {
		this.distantSockets.add(socket);
	}
	
	@Override
	public void onSelfLogin(String username) {
		// TODO broadcast UDP with following message
		// TODO problem: wow getting the local user that hasnt been created yet
		// Login message format: "username: xxxxxxxx; port: xxxx. UUID: xxxxx!"
		User localUser = OnlineUsersManager.getInstance().getLocalUser();
		String loginMessage = "username: " + localUser.getUsername() + "; ";
		loginMessage += "port: " + localUser.getTCPserverPort() + ". ";
		loginMessage += "UUID: " + localUser.getId() + "!";
	}
	
	@Override
	public void onSelfLogout() {
		// TODO broadcast udp to tell ppl we've logged out
		// Logout message format: "UUID: xxxxxxx! logout"
		User localUser = OnlineUsersManager.getInstance().getLocalUser();
		String logoutMessage = "UUID: " + localUser.getId() + "! logout";
		// TODO: should this be done directly in onSelfLogout in the ThreadManager?
		ThreadManager.getInstance().clearConversationsMap();
	}
	
}
