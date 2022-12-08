package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import main.java.com.model.TCPServer;

public class Test {

	
	public static void main(String[] args) {
		TCPServer TCPserver = new TCPServer();
		(new Thread(TCPserver)).start();
		try {
			//envoi demande connexion
			DatagramSocket UDPserver = new DatagramSocket();
			DatagramPacket packetInit = new DatagramPacket("1026".getBytes(), 4, InetAddress.getByName("localhost"), 1025);
			UDPserver.send(packetInit);
			UDPserver.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
