package main.java.com.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.sql.Time;

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