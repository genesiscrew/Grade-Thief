package f_server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ServerChat extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;


	public ServerChat(){

		super("Mostafa instant Messanger");
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener() {


					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						sendMessage(e.getActionCommand());
						userText.setText("");
					}
				}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(300,150);
		setVisible(true);

	}

	//set up and run the server
	public void startRunning(){
		try {
			server = new ServerSocket(1234, 100);
			while(true){
				try {
					waitForConnection();
					setupStream();
					whileChatting();
				} catch (EOFException e) {
					showMessage("\n Server ended the connection! ");
				}finally {
					closeCrap();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Wait for connection, then display connection information
	private void waitForConnection() throws IOException{
		showMessage(" Waiting for someone to connect...\n");
		connection = server.accept();
		showMessage(" Now connected to" + connection.getInetAddress().getHostName());

	}


	private void setupStream() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Stream are now setup! \n");
	}


	//During the chat conversation
	private void whileChatting() throws IOException{
		String message = " You are now connected! ";
		sendMessage(message);
		ableToType(true);
		do{
			//Have a conversation!
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			} catch (ClassNotFoundException e) {
				showMessage("\n I don't know what user send");
			}
		}while(!message.equals("CLIENT - END"));
	}

	//close streams and sockets after you are done chatting!!
	private void closeCrap(){
		showMessage("\n Closing connectin...\n");
		ableToType(false);
		try {

			output.close();
			input.close();
			connection.close();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	//send a message to a client
	private void sendMessage(String message){
		try {

			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER -" + message);

		} catch (IOException e) {
			chatWindow.append("\n ERROR: I can't send that message");
		}
	}

	//update chatWindow
	private void showMessage(final String text){
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						chatWindow.append(text);
					}
				}
		);
	}

	//let user type texts into box
	private void ableToType(final boolean tof){
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						userText.setEditable(tof);
					}
				}
		);
	}

}
