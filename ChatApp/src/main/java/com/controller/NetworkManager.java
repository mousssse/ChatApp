package main.java.com.controller;

import java.net.DatagramSocket;
import java.net.SocketException;

import main.java.com.model.TCPServer;
import main.java.com.model.UDPServer;

public class NetworkManager {
	
	private UDPServer UDPserver;
	private TCPServer TCPserver;
	
	private static final NetworkManager manager = new NetworkManager();
	
	private NetworkManager() {
		//this.UDPserver = new UDPServer();
		//this.TCPserver = new TCPServer();
		//(new Thread(this.UDPserver)).start();
		//(new Thread(this.TCPserver)).start();
	}
	
	public NetworkManager getInstance() {
		return manager;
	}
	
	public static void main(String[] args) throws SocketException {
		UDPServer UDPServer = new UDPServer();
		(new Thread(UDPServer)).start();
	}
	
}
