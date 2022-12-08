package main.java.com.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class NetworkManager {
	// The NetworkManager is a singleton
	private static final NetworkManager networkManager = new NetworkManager();
	// Original TCP acceptance port
	public final static int TCP_ACCEPT_PORT = 1026;
	// Original TCP acceptance port
	public final static int UDP_ACCEPT_PORT = 1025;
	// Next available port of the server socket (works by increments of 1)
	private int nextAvailablePort = 1027;
	
	public static NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	// TODO Working on this
	public void UDPInitialization() {
		try {
			DatagramSocket UDPServer = new DatagramSocket(UDP_ACCEPT_PORT);
			
			new Thread(new Runnable() {
				byte[] tcpPort = new byte[5];
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(true) {
						DatagramPacket received = new DatagramPacket(tcpPort, 5);
						try {
							UDPServer.receive(received);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}).start();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
