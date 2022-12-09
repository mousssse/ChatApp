package main.java.com.controller;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import main.java.com.controller.listener.LoginListener;
import main.java.com.model.TCPServer;
import main.java.com.model.UDPServer;
import main.java.com.model.User;

/**
 * TODO For now this class is absolutely useless but I can't find the courage to delete it quite yet.
 * @author Sandro
 * @author sarah
 *
 */
public class NetworkManager implements LoginListener {
	
	private UDPServer UDPserver;
	private TCPServer TCPserver;
	
	private List<Socket> distantSockets;
	
	private static NetworkManager networkManager;
	
	private NetworkManager() {
		this.distantSockets = new ArrayList<>();
		this.UDPserver = new UDPServer();
		this.TCPserver = new TCPServer();
		(new Thread(this.UDPserver)).start();
		(new Thread(this.TCPserver)).start();
	}
	
	public static NetworkManager getInstance() {
		if (networkManager == null) networkManager = new NetworkManager();
		return networkManager;
	}
	
	public void addDistantSocket(Socket socket) {
		this.distantSockets.add(socket);
	}
	
	public static void main(String[] args) {
		//UDPServer UDPServer = new UDPServer();
		//(new Thread(UDPServer)).start();
		NetworkManager.getInstance();
	}

	@Override
	public void onLogin(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogout(User user) {
		// TODO Auto-generated method stub
	}
	
}
