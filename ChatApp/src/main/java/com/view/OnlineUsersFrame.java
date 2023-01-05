package main.java.com.view;

import javax.swing.*;

import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.User;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class OnlineUsersFrame extends JPanel implements LoginListener, UsernameListener {

	private static final long serialVersionUID = 1496366638423922933L;
	private JList<User> users;
	// TODO make private
    public DefaultListModel<User> userListVector;
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
                    ListenerManager.getInstance().addChatListener(chatFrame);
                    // TODO Think more about this: the request will be sent from both PCs since both will double-click.
                    // TODO solution: auto-pop the frame for the receiving user? 
                    ListenerManager.getInstance().fireOnChatRequest(remoteUser);

                    JFrame f = new JFrame("Message: " + remoteUser);
                    
                    // Chat frame behavior when the X button is clicked
                    f.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                        	ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), remoteUser, "", LocalDateTime.now());
                        	f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        }
                    });
                    f.setSize(500, 500);
                    f.getContentPane().add(chatFrame, BorderLayout.CENTER);
                    f.setVisible(true);
                }
            }
        });
    }

    /**
     * 
     * @return the online users frame instance.
     */
    public static OnlineUsersFrame getInstance() {
    	if (onlineUsersFrame == null) onlineUsersFrame = new OnlineUsersFrame();
		return onlineUsersFrame;
	}


    /**
     * 
     * @return the DefaultListModel.
     */
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

	@Override
	public void onUsernameModification(User user, String newUsername) {
		userListVector.removeElement(user);
		userListVector.addElement(user);
	}

	@Override
	public void onSelfUsernameModification(String newUsername) {
		// Nothing to do
	}
	
	
}


