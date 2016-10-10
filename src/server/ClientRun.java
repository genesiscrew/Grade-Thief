package server;

import java.io.IOException;

public class ClientRun {
	public static void main(String[] args) throws IOException {
		Client mos = new Client("127.0.0.1");
		mos.startRunning();
	}
}
