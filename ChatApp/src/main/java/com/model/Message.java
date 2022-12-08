package main.java.com.model;
import java.sql.Date;

public class Message {
    private String content;
    private Date date;
    
	public Message(String content) {
		super();
		long millis = System.currentTimeMillis();
		this.content = content;
		this.date = new java.sql.Date(millis);
	}
	
}