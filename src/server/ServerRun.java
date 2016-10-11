package server;

import java.io.IOException;



/**
 *
 * @author shenavmost
 *	This class runs the server
 */
public class ServerRun {
	public static void main(String[] args) throws IOException {
		Server mos = new Server();
		mos.startRunning();
	}
}
