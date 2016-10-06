package f_server;

import java.io.IOException;

import characters.Player;
import f_server.Client;
import model.Game;

public class ClientRun {
	public static void main(String[] args) throws IOException {
		Client mos = new Client("130.195.6.162");

		mos.startRunning();
	}
}
