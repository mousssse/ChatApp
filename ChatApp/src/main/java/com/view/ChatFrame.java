package main.java.com.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.listener.ChatListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.Message;
import main.java.com.model.MessageType;
import main.java.com.model.User;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class ChatFrame extends JPanel implements ChatListener, UsernameListener {

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
		try {
			vector.addAll(DBManager.getInstance().getConversationHistory(remoteUser.getId()));
		} catch (SQLException e1) {
			// Couldnt retrieve history -> TODO: show error message?
			e1.printStackTrace();
		}
		
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String messageContent = inputField.getText();
                // The empty string is not allowed.
                if (!messageContent.equals("")) {
                    Message message = new Message(OnlineUsersManager.getInstance().getLocalUser(), remoteUser, messageContent, LocalDateTime.now(), MessageType.MESSAGE);
    				ListenerManager.getInstance().fireOnMessageToSend(message.getFromUser(), message.getToUser(), message.getContent(), message.getDate(), message.getType());
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

	@Override
	public void onChatClosure(User remoteUser) {
		this.setVisible(false);
		this.dispose();
	}

	@Override
	public void onMessageToSend(User localUser, User remoteUser, String messageContent, LocalDateTime date, MessageType type) {
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

	@Override
	public void onUsernameModification(User user, String newUsername) {
		this.remoteUser = user;
	}

	@Override
	public void onSelfUsernameModification(String newUsername) {
		// Nothing to do
	}

}
