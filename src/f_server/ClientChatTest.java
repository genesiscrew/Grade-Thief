package f_server;

import javax.swing.JFrame;



public class ClientChatTest {


	public static void main(String[] args) {
		ChatClient mos = new ChatClient("127.0.0.1");
		mos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mos.startRunning();
	}

}


