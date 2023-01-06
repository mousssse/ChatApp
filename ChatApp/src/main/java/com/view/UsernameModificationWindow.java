package main.java.com.view;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
		
		usernameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					verifyUsernameUnicity();
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		
	}
	
	private void verifyUsernameUnicity() {
		//TODO verify username unicity
		System.out.println("I'm bored, code me");
	}
	
    public static void main(String[] args) throws UnknownHostException {
        UsernameModificationWindow window = new UsernameModificationWindow();
        window.setVisible(true);
    }
}
