package main.java.com.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.listener.ChatListener;
import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;

public class ChatFrame extends JPanel implements ChatListener {
	// TODO for now, our program only works for textual messages
	private static final long serialVersionUID = -4715687549491428225L;
	private User remoteUser;

	private DefaultListModel<Message> vector = new DefaultListModel<>();
	private JList<Message> messageList = new JList<>(vector);
	private JTextField inputField = new JTextField();

	/**
	 * 
	 * @param remoteUser is the remote user with which the chat is taking place.
	 */
	public ChatFrame(User remoteUser) {
		this.remoteUser = remoteUser;
		setLayout(new BorderLayout());
		add(new JScrollPane(messageList), BorderLayout.CENTER);
		add(inputField, BorderLayout.SOUTH);
		
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageContent = inputField.getText();
                // The empty string is not allowed.
                if (!messageContent.equals("")) {
                    Message message = new Message(OnlineUsersManager.getInstance().getLocalUser(), remoteUser, messageContent, LocalDateTime.now(), MessageType.MESSAGE);
    				ListenerManager.getInstance().fireOnMessageToSend(message.getFromUser(), message.getToUser(), message.getContent(), message.getDate());
    				// Adding the message to the view.
    				vector.addElement(message);
    				// Resetting the text field.
    				inputField.setText("");
                }
            }
        });
	}
	
	@Override
	public void onChatRequest(User remoteUser) {
		// TODO automatically open the chat frame? create an accept / deny mechanism?
		//ChatFrame newFrame = new ChatFrame(remoteUser);
	}

	/**
	 * TODO JPanel isn't closable, think more about the handshake.
	 */
	@Override
	public void onChatClosure(User remoteUser) {
		this.setVisible(false);
		this.dispose();
	}

	@Override
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date) {
		// Nothing to do
	}

	/**
	 * Adds the message to the chat frame
	 */
	@Override
	public void onMessageToReceive(Message message) {
		vector.addElement(message);
	}
	
	/**
	 * Disposing the ChatFrame
	 */
	public void dispose() {
	    JFrame parent = (JFrame) this.getTopLevelAncestor();
	    parent.dispose();
	}

}
