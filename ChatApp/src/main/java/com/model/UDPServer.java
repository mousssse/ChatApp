package main.java.com.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;

// TODO: change how this works: will only be used for login/logout, need to store ppl's TCP server port
public class UDPServer implements Runnable {
	private static final int UDPserverPort = 1025;
	private DatagramSocket serverDatagram;
	
	public static int getUDPserverPort() {
		return UDPserverPort;
	}
	
	@Override
	public void run() {
		try {
			this.serverDatagram = new DatagramSocket(UDPserverPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (true) {			
			try {
				// waiting for new message
				System.out.println("UDP: Waiting for someone to say something");
				byte[] content = new byte[100];
				DatagramPacket received = new DatagramPacket(content, content.length);
				this.serverDatagram.receive(received);
				
				// The string received will have the format: 
				// "username: xxxxxxxx; port: xxxx. UUID: xxxxx!" for a login
				// or "logout" for a logout
				String contentReceived = new String(content);
				if (received.getAddress() == OnlineUsersManager.getInstance().getLocalUser().getIP()) {
					// This packet comes from us, we shouldn't process it
					System.out.println("Packet from us, ignored.");
					continue;
				}
				if (contentReceived.contains("logout")) {
					// TODO This packet is a broadcast to notify a logout
					ListenerManager.getInstance().fireOnLogout(received.getAddress());
				}
				else {
					// This packet is a broadcast to notify a login
					String remoteId = contentReceived.substring(contentReceived.indexOf("UUID:") + 6, contentReceived.indexOf("!"));
					String username = contentReceived.substring(contentReceived.indexOf("username:") + 10, contentReceived.indexOf(";"));
					int remoteTCPServerPort = Integer.parseInt(contentReceived.substring(contentReceived.indexOf("port:") + 6, contentReceived.indexOf(".")));
					User remoteUser = new User(remoteId, username, received.getAddress(), remoteTCPServerPort);
					ListenerManager.getInstance().fireOnLogin(remoteUser);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
