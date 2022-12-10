package main.java.com.controller;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import main.java.com.controller.listener.SelfLoginListener;
import main.java.com.model.TCPServer;
import main.java.com.model.UDPServer;
import main.java.com.model.User;

/**
 * The NetworkManager manages a user's UDP and TCP servers.
 * @author Sandro
 * @author sarah
 *
 */
public class NetworkManager implements SelfLoginListener {
	
	private List<Socket> distantSockets;
	private UDPServer UDPserver;
	private TCPServer TCPserver;
	
	private static NetworkManager networkManager = null;
	
	private NetworkManager() {
		this.distantSockets = new ArrayList<>();
		this.UDPserver = new UDPServer();
		this.TCPserver = new TCPServer();
		(new Thread(this.UDPserver, "UDP Server")).start();
		(new Thread(this.TCPserver, "TCP Server")).start();
	}
	
	/**
	 * 
	 * @return the NetworkManager singleton
	 */
	public static NetworkManager getInstance() {
		if (networkManager == null) networkManager = new NetworkManager();
		return networkManager;
	}
	
	/**
	 * 
	 * @param socket is the socket to be added
	 */
	public void addDistantSocket(Socket socket) {
		this.distantSockets.add(socket);
	}
	
	@Override
	public void onSelfLogin(String id, String username) {
		// TODO pay attention, id was added to selfLogin. Might be useless here but just notice that you can't just remove it.
		// TODO broadcast UDP with following message
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
	}
	
}
