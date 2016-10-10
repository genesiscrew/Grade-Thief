package model.server;

import java.io.IOException;

public class ServerRun {
	public static void main(String[] args) throws IOException {
		Server mos = new Server();
		mos.startRunning();
	}
}
