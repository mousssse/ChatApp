package main.java.com.view;

import javax.swing.*;

import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;
import main.java.com.controller.listener.LoginListener;
import main.java.com.controller.listener.UsernameListener;
import main.java.com.model.MessageType;
import main.java.com.model.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private DefaultListModel<User> userListVector;
    private static OnlineUsersFrame onlineUsersFrame = null;
    private JButton usernameButton;

    public OnlineUsersFrame() {
    	this.userListVector = new DefaultListModel<>();
        this.users = new JList<>(this.userListVector);
        this.usernameButton = new JButton("Change username");
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(users), BorderLayout.CENTER);
        this.add(new JScrollPane(usernameButton), BorderLayout.SOUTH);
        
		usernameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					UsernameModificationWindow usernameModificationWindow = new UsernameModificationWindow();
					JFrame frame = new JFrame("Modify your username");
		            frame.addWindowListener(new WindowAdapter() {
		                public void windowClosing(WindowEvent e) {
		                	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		                }
		            });
		            // TODO this is throwing an exception
					frame.getContentPane().add(usernameModificationWindow, BorderLayout.CENTER);
					frame.setVisible(true);
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				}
			}
		});

        users.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent count) {
            	// On double-click on an online user's username, open chat frame with that user
                if (count.getClickCount() > 1) {
                    User remoteUser = users.getSelectedValue();
                    ChatFrame chatFrame = new ChatFrame(remoteUser);
                    ListenerManager.getInstance().addChatListener(chatFrame);
                    // TODO Think more about this: the request will be sent from both PCs since both will double-click.
                    // solution: auto-pop the frame for the receiving user? 
                    ListenerManager.getInstance().fireOnChatRequest(remoteUser);

                    JFrame f = new JFrame("Message: " + remoteUser);
                    
                    // Chat frame behavior when the X button is clicked
                    f.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                        	ListenerManager.getInstance().fireOnMessageToSend(OnlineUsersManager.getInstance().getLocalUser(), remoteUser, null, LocalDateTime.now(), MessageType.CLOSING_CONVERSATION);
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
		return this.userListVector;
	}

	public static void main(String[] args) {
        OnlineUsersFrame onlineUsersFrame = new OnlineUsersFrame();
        JFrame frame = new JFrame("Online users");
        // When the user closes the OnlineUsersFrame, the user is choosing to log out.
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
		this.userListVector.addElement(remoteUser);
	}

    /**
     * When a user logs in, they're removed from the frame.
     */
	@Override
	public void onLogout(User remoteUser) {
		this.userListVector.removeElement(remoteUser);	
	}

	@Override
	public void onUsernameModification(User user, String newUsername) {
		user.setUsername(newUsername);
		this.userListVector.removeElement(user);
		this.userListVector.addElement(user);
	}

	@Override
	public void onSelfUsernameModification(String newUsername) {
		// Nothing to do
	}
	
	
}


