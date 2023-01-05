package main.java.com.controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

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
	private List<InetAddress> distantIPs;
	private UDPServer UDPserver;
	private TCPServer TCPserver;
	
	
	private static NetworkManager networkManager = null;
	
	private NetworkManager() {
		this.distantSockets = new ArrayList<>();
		try {
			this.distantIPs = this.listAllBroadcastAddresses();
		} catch (SocketException e) {
			e.printStackTrace();
		}
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
	 * Method used to obtain all IPs on the local network prior to broadcasting
	 * @return the distant IPs
	 * @throws SocketException - SocketException
	 */
	private List<InetAddress> listAllBroadcastAddresses() throws SocketException {
	    List<InetAddress> broadcastList = new ArrayList<>();
	    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	    while (interfaces.hasMoreElements()) {
	        NetworkInterface networkInterface = interfaces.nextElement();

	        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
	            continue;
	        }

	        networkInterface.getInterfaceAddresses().stream() 
	          .map(a -> a.getBroadcast())
	          .filter(Objects::nonNull)
	          .forEach(broadcastList::add);
	    }
	    return broadcastList;
	}
	
	/**
	 * Method used to broadcast
	 * @param broadcastMessage is the message to broadcast
	 * @param address is the IP address that will receive the broadcast
	 * @throws IOException - IOException
	 */
    public void broadcast(String broadcastMessage, InetAddress address) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        byte[] buffer = broadcastMessage.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, UDPServer.UDPserverPort);
        socket.send(packet);
        socket.close();
    }
	
	/**
	 * 
	 * @param socket is the socket to be added
	 */
	public void addDistantSocket(Socket socket) {
		this.distantSockets.add(socket);
	}
	
	/**
	 * Broadcasting online status
	 */
	@Override
	public void onSelfLoginNetwork() {
		this.UDPserver = new UDPServer();
		this.TCPserver = new TCPServer();
		(new Thread(this.UDPserver, "UDP Server")).start();
		(new Thread(this.TCPserver, "TCP Server")).start();
		// Login message format: "login username port UUID"
		User localUser = OnlineUsersManager.getInstance().getLocalUser();
		String loginMessage = "login " + localUser.getUsername() + " " + localUser.getTCPserverPort() + " " + localUser.getId();
		for (InetAddress address : this.distantIPs) {
			try {
				this.broadcast(loginMessage, address);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Broadcasting offline status
	 */
	@Override
	public void onSelfLogout() {
		// Logout message format: "logout"
		String logoutMessage = "logout";
		for (InetAddress address : this.distantIPs) {
			try {
				this.broadcast(logoutMessage, address);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onSelfLoginOnlineUsers(String username) {
		// Nothing to do
	}
	
}
