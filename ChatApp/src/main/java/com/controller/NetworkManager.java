package main.java.com.controller;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import main.java.com.controller.listener.LoginListener;
import main.java.com.model.TCPServer;
import main.java.com.model.UDPServer;

public class NetworkManager implements LoginListener {
	
	private UDPServer UDPserver;
	private TCPServer TCPserver;
	
	private List<Socket> distantSockets;
	
	private static final NetworkManager networkManager = new NetworkManager();
	
	private NetworkManager() {
		this.distantSockets = new ArrayList<>();
		this.UDPserver = new UDPServer();
		this.TCPserver = new TCPServer();
		(new Thread(this.UDPserver)).start();
		(new Thread(this.TCPserver)).start();
	}
	
	public static NetworkManager getInstance() {
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
	public void onLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogout() {
		// TODO Auto-generated method stub
	}
	
}
