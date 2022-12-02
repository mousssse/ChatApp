package main.java.com.model;

public abstract class UserThread extends Thread {

	public final static int ACCEPT_PORT = 9000;
	private String receiverID;
	
	/**
	 * 
	 * @param threadName is the threadName attribute of the Thread class
	 * @param receiverID is the ID of the other user concerned by the current thread
	 */
	public UserThread(String receiverID) {
		super();
		this.receiverID = receiverID;
	}
	
	public String getReceiverID() {
		return this.receiverID;
	}
}
