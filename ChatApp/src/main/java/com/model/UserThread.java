package main.java.com.model;

public abstract class UserThread extends Thread {

	public final static int ACCEPT_PORT = 9000;
	private String receiverID;
	
	public UserThread(String threadID, String receiverID) {
		super(threadID);
		this.receiverID = receiverID;
	}
	
	public String getReceiverID() {
		return this.receiverID;
	}
}
