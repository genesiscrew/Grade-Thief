package f_server;

import javax.swing.JFrame;

public class ServerChatTest {
	public static void main(String[] args) {
		ServerChat mos = new ServerChat();
		mos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mos.startRunning();
	}
}
