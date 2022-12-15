package main.java.com.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import main.java.com.controller.listener.ChatListener;
import main.java.com.model.Message;
import main.java.com.model.User;

public class ChatFrame extends JPanel implements ChatListener {
	//TODO for now only works for textual messages
	private static final long serialVersionUID = -4715687549491428225L;
	private User remoteUser;

	private DefaultListModel<Message> vector = new DefaultListModel<>();
	private JList<Message> messageList = new JList<>(vector);
	private JTextField inputField = new JTextField();

	public ChatFrame(User remoteUser) {
		this.remoteUser = remoteUser;
		setLayout(new BorderLayout());
		add(new JScrollPane(messageList), BorderLayout.CENTER);
		add(inputField, BorderLayout.SOUTH);
	}
	
	@Override
	public void onChatRequest(User remoteUser) {
		
	}

	/**
	 * TODO JPanel isn't closable, think more about the handshake.
	 */
	@Override
	public void onChatClosure(User remoteUser) {
		this.setVisible(false);
	}

	@Override
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date) {
		// TODO Auto-generated method stub
	}

	/**
	 * Adds the message to the chat frame
	 */
	@Override
	public void onMessageToReceive(Message message) {
		vector.addElement(message);
	}

}
