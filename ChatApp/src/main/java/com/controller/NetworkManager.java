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
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.TCPServer;
import main.java.com.model.UDPServer;
import main.java.com.model.User;

/**
 * The NetworkManager manages a user's UDP and TCP servers.
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class NetworkManager implements SelfLoginListener, UsernameListener {

	private List<Socket> distantSockets;
	private List<InetAddress> broadcastAddresses;
	private UDPServer UDPserver;
	private TCPServer TCPserver;

	private static NetworkManager networkManager = null;

	private NetworkManager() {
		ListenerManager.getInstance().addSelfLoginListener(this);
		ListenerManager.getInstance().addUsernameListener(this);
		this.distantSockets = new ArrayList<>();
		try {
			this.broadcastAddresses = this.findBroadcastAddresses();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return the NetworkManager singleton
	 */
	public static NetworkManager getInstance() {
		if (networkManager == null)
			networkManager = new NetworkManager();
		return networkManager;
	}

	/**
	 * Method used to obtain all broadcast addresses of the local networks to which
	 * the machine belongs
	 * 
	 * @return the list of broadcast addresses
	 * @throws SocketException - SocketException
	 */
	private List<InetAddress> findBroadcastAddresses() throws SocketException {
		List<InetAddress> broadcastList = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			if (networkInterface.isLoopback() || !networkInterface.isUp()) {
				continue;
			}

			// Performs getBroadcast() on each element of the stream and adds non-null ones
			// to broadcastList
			networkInterface.getInterfaceAddresses().stream().map(a -> a.getBroadcast()).filter(Objects::nonNull)
					.forEach(broadcastList::add);
		}
		return broadcastList;
	}

	/**
	 * Method used to broadcast
	 * 
	 * @param message is the message to broadcast
	 * @param address is the broadcast address
	 * @throws IOException - IOException
	 */
	public void broadcast(String message, InetAddress address) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		socket.setBroadcast(true);
		byte[] buffer = message.getBytes();
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
		String loginMessage = "login " + localUser.getUsername() + " " + localUser.getTCPserverPort() + " "
				+ localUser.getId();
		for (InetAddress address : this.broadcastAddresses) {
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
		for (InetAddress address : this.broadcastAddresses) {
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

	@Override
	public void onSelfUsernameModification(String newUsername) {
		// Username modification message format: "username newUsername"
		String usernameUpdateMessage = "username " + newUsername;
		for (InetAddress address : this.broadcastAddresses) {
			try {
				this.broadcast(usernameUpdateMessage, address);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUsernameModification(User user, String newUsername) {
		// Nothing to do
	}

}
