package server;

import java.io.IOException;

/**
 *
 * @author shenavmost This class runs the client.
 */
public class ClientRun {
	public static void main(String[] args) throws IOException {
		// Client gets Server ip address as a String.
		Client mos = new Client("130.195.4.152");
		mos.startRunning();
	}
}
