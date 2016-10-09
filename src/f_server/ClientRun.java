package f_server;

import java.io.IOException;

import characters.Player;
import f_server.Client;
import model.Game;

public class ClientRun {
	public static void main(String[] args) throws IOException {
		Client mos = new Client("127.0.0.1");
		mos.startRunning();
	}
}
