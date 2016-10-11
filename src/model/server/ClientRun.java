package model.server;

import java.io.IOException;

public class ClientRun {
	public static void main(String[] args) throws IOException {
		Client mos = new Client("130.195.4.165");
		mos.startRunning();
	}
	
}
