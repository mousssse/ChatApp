/**
 * The LoginWindow class is the very first frame displayed to a user who launches the ChatApp.
 * The User is asked to enter their ID and password, or otherwise register. 
 */

package main.java.com.view;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.java.com.controller.DBManager;
import main.java.com.controller.ListenerManager;
/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class LoginWindow extends JFrame {
	
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
				try {
					onSuccessfulLogin();
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
	}

	// TODO
	private void onSuccessfulLogin() throws HeadlessException, SQLException {
		String login = loginField.getText();
		String password = new String(passwordField.getPassword());
		
		boolean connected = false;
		String localUsername = DBManager.getInstance().getLocalUsername();
		if (localUsername == null) {
			ListenerManager.getInstance().fireOnSelfLogin(login, password);
			connected = true;
		}
		else {
			// TODO password should be hashed when checking w db
			connected = login.equals(localUsername) && password.equals(DBManager.getInstance().getHashedPassword());
		}
		
		if (connected) {
			ListenerManager.getInstance().fireOnSelfLoginNext(login);
			// As soon as the user is logged in, the online users frame is created, and the login frame disappears.
			this.setVisible(false);
			OnlineUsersFrame onlineUsersFrame = new OnlineUsersFrame();
			ListenerManager.getInstance().addLoginListener(onlineUsersFrame);
			JFrame frame = new JFrame("Online users");
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                	ListenerManager.getInstance().fireOnSelfLogout();
                	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
            });
			frame.setSize(300, 500);
			frame.getContentPane().add(onlineUsersFrame, BorderLayout.CENTER);
			frame.setVisible(true);
		} else {
			 //Display an error message if the user enters an invalid ID or password.
			 JOptionPane.showMessageDialog(this, "Invalid ID/password.");
		}
	}
	
    public static void main(String[] args) throws UnknownHostException {
        LoginWindow loginWin = new LoginWindow();
        loginWin.setVisible(true);
    }
}
