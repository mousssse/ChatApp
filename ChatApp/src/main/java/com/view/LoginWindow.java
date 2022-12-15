/**
 * The LoginWindow class is the very first frame displayed to a user who launches the ChatApp.
 * The User is asked to enter their ID and password, or otherwise register. 
 */

package main.java.com.view;

import javax.swing.*;

import main.java.com.model.User;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class LoginWindow extends JFrame {
	/**
	 * TODO Remember to ask yourself: the hell is serialization ?
	 */
	private static final long serialVersionUID = 6781528607158272898L;
	JLabel loginLabel = new JLabel("ID: ");
	JTextField loginField = new JTextField();
	JLabel passwordLabel = new JLabel("Password: ");
	JPasswordField passwordField = new JPasswordField();
	JButton loginButton = new JButton("Login");

	public LoginWindow() throws UnknownHostException {
		super("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(loginLabel);
		panel.add(loginField);
		panel.add(passwordLabel);
		panel.add(passwordField);
		panel.add(loginButton);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//onSuccessfulLogin();
			}
		});

		getContentPane().add(panel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

//	private void onSuccessfulLogin() {
//		String login = loginField.getText();
//		String password = passwordField.getText();
//
//		if (user.connect(login, password)) {
//			// As soon as the user is logged in, the online users frame is created, and the login frame disappears.
//			setVisible(false);
//			OnlineUsersFrame onlineUsersFrame = new OnlineUsersFrame();
//			JFrame frame = new JFrame("Online users");
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.setSize(300, 500);
//			frame.getContentPane().add(onlineUsersFrame, BorderLayout.CENTER);
//			frame.setVisible(true);
//		} else {
//			// Display an error message if the user enters an invalid ID or password.
//			JOptionPane.showMessageDialog(this, "Invalid ID/password.");
//		}
//	}
	
    public static void main(String[] args) throws UnknownHostException {
        LoginWindow loginWin = new LoginWindow();
        loginWin.setVisible(true);
    }
}
