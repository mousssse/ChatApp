package main.java.com.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
import main.java.com.controller.OnlineUsersManager;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class UsernameModificationWindow extends JFrame {

	private static final long serialVersionUID = 4437822057846139416L;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JButton usernameButton;
	
	public UsernameModificationWindow() {
		this.usernameLabel = new JLabel("New username: ");
		this.usernameField = new JTextField();
		this.usernameButton = new JButton("Change username");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(usernameLabel);
		panel.add(usernameField);
		panel.add(usernameButton);
		
		this.usernameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newUsername = usernameField.getText();
				if (DBManager.getInstance().updateUsername(OnlineUsersManager.getInstance().getLocalUser().getId(), newUsername)) {
					ListenerManager.getInstance().fireOnSelfUsernameModification(newUsername);
					panel.setVisible(false);
					JFrame parent = (JFrame) panel.getTopLevelAncestor();
					parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
				}
				else {
					//Display an error message if the username is already taken
					 JOptionPane.showMessageDialog(panel, "This username is already taken");
					 usernameField.setText(null);
				}
			}
		});
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		
	}
	
    public static void main(String[] args) throws UnknownHostException {
        UsernameModificationWindow window = new UsernameModificationWindow();
        window.setVisible(true);
    }
}
