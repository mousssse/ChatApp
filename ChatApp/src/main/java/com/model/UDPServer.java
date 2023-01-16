package main.java.com.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;

/**
 * 
 * @author sarah
 * @author Sandro
 *
 */
public class UDPServer implements Runnable {
	public static final int UDPserverPort = 8025;
	private DatagramSocket serverDatagram;

	@Override
	public void run() {
		try {
			this.serverDatagram = new DatagramSocket(UDPserverPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				// Waiting for a message
				System.out.println("UDP: Waiting for someone to say something");
				byte[] content = new byte[100];
				DatagramPacket received = new DatagramPacket(content, content.length);
				this.serverDatagram.receive(received);

				// The received message is treated depending on its format
				String contentReceived = new String(content).trim();
				System.out.println(contentReceived);
				if (received.getAddress().getHostAddress()
						.equals(OnlineUsersManager.getInstance().getLocalUser().getIP().getHostAddress())) {
					// This packet comes from the local user, it should be ignored.
					System.out.println("Packet from self, ignored.");
					continue;
				}
				if (contentReceived.contains("logout")) {
					// This packet is a broadcast to notify a logout
					ListenerManager.getInstance()
							.fireOnLogout(OnlineUsersManager.getInstance().getUserFromIP(received.getAddress()));
				} else if (contentReceived.contains("username")) {
					// This packet is a broadcast to notify a username update
					User remoteUser = OnlineUsersManager.getInstance().getUserFromIP(received.getAddress());
					String[] parts = contentReceived.split(" ");
					String remoteUsername = parts[1];
					System.out.println(
							"firing username change from " + remoteUser.getUsername() + " to " + remoteUsername);
					DBManager.getInstance().updateUsername(remoteUser.getId(), remoteUsername);
					ListenerManager.getInstance().fireOnUsernameModification(remoteUser, remoteUsername);
				} else {
					// This packet is a broadcast to notify a login or the answer to a login
					String[] parts = contentReceived.split(" ");
					String flag = parts[0];
					String remoteUsername = parts[1];
					int remoteTCPServerPort = Integer.parseInt(parts[2]);
					String remoteId = parts[3];
					User remoteUser = new User(remoteId, remoteUsername, received.getAddress(), remoteTCPServerPort);
					ListenerManager.getInstance().fireOnLogin(remoteUser);

					if (flag.equals("login")) {
						// We should send an ack back to say who we are
						User localUser = OnlineUsersManager.getInstance().getLocalUser();
						String ackMessage = "ack " + localUser.getUsername() + " " + localUser.getTCPserverPort() + " "
								+ localUser.getId();
						this.serverDatagram.send(new DatagramPacket(ackMessage.getBytes(), ackMessage.length(),
								received.getAddress(), UDPserverPort));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
