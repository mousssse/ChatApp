package main.java.com.view;

import javax.swing.*;

import main.java.com.controller.listener.LoginListener;
import main.java.com.model.User;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 
 * @author Sandro
 *
 */
public class OnlineUsersFrame extends JPanel implements LoginListener {

	private static final long serialVersionUID = 1496366638423922933L;
	private JList<User> users;
    private DefaultListModel<User> userListVector;
    private static OnlineUsersFrame onlineUsersFrame = null;

    public OnlineUsersFrame() {
    	userListVector = new DefaultListModel<>();
        users = new JList<>(userListVector);
        setLayout(new BorderLayout());
        add(new JScrollPane(users), BorderLayout.CENTER);

        users.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent count) {
            	// On double-click on an online user's username, open chat frame with that user
                if (count.getClickCount() > 1) {
                    User remoteUser = users.getSelectedValue();
                    ChatFrame chatFrame = new ChatFrame(remoteUser);

                    JFrame f = new JFrame("Message: " + remoteUser);
                    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    f.setSize(500, 500);
                    f.getContentPane().add(chatFrame, BorderLayout.CENTER);
                    f.setVisible(true);
                }
            }
        });
    }

    public static OnlineUsersFrame getInstance() {
    	if (onlineUsersFrame == null) onlineUsersFrame = new OnlineUsersFrame();
		return onlineUsersFrame;
	}


	public DefaultListModel<User> getUserListVector() {
		return userListVector;
	}

	public static void main(String[] args) {
        OnlineUsersFrame onlineUsersFrame = new OnlineUsersFrame();
        JFrame frame = new JFrame("Online users");
        // TODO When the user closes the OnlineUsersFrame, the user is choosing to log out.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.getContentPane().add(onlineUsersFrame, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * When a user logs in, they're added to the frame.
     */
	@Override
	public void onLogin(User remoteUser) {
		userListVector.addElement(remoteUser);
	}

    /**
     * When a user logs in, they're removed from the frame.
     */
	@Override
	public void onLogout(User remoteUser) {
		userListVector.removeElement(remoteUser);	
	}
}


