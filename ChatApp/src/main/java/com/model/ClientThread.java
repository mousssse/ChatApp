/**
 * The ClientThread class is an active conversation between two online users.
 */

package main.java.com.model;

import java.io.IOException;
import java.net.Socket;
/**
 * 
 * @author Sandro
 * @author sarah
 *
 */
public class ClientThread extends UserThread {

	public ClientThread(Socket socket) throws IOException {
		super(socket);
	}

}
