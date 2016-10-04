package f_server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatClient extends JFrame{
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;


	public ChatClient(String host){
		super("Client mofo");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						sendMessage(e.getActionCommand());
						userText.setText("");
					}
				}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}

	//Connect to server
		public void startRunning(){
			try{

				connectionToServer();
				setupStreams();
				whileChatting();

			}catch(EOFException e){
				showMessage("\n Client terminated connection");
			}catch(IOException e1){
				e1.printStackTrace();
			}finally {
				closeCrap();
			}
		}

		//Connecting to Server
		private void connectionToServer() throws IOException{
			showMessage("Attempting connection...\n");
			connection = new Socket(InetAddress.getByName(serverIP), 1234);
			showMessage("Connected to: " + connection.getInetAddress().getHostName());
		}

		//set up the stream to send and recieve message!
		private void setupStreams() throws IOException{
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection.getInputStream());
			showMessage("\n You are now connected and you can send message! \n");
		}

		//While chatting
		private void whileChatting() throws IOException{
			ableToType(true);
			do{
				try {
					message = (String) input.readObject();
					showMessage("\n" + message);
				} catch (ClassNotFoundException e) {
					showMessage("\n I don't know the object type");
				}
			}while(!message.equals("SERVER - END"));
		}

		//close after its done
		private void closeCrap(){
			showMessage("\n Closing the chat");
			ableToType(false);
			try {

				output.close();
				input.close();
				connection.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//send message
		private void sendMessage(String message){
			try {
				output.writeObject("CLIENT - " + message);
				output.flush();
				showMessage("\nCLIENT - "+ message);
			} catch (IOException e) {
				chatWindow.append("\n Something messsed up sending message");
			}

		}

		//change or update chatwindow
		private void showMessage(final String message){
			SwingUtilities.invokeLater(
					new Runnable() {

						@Override
						public void run() {
							chatWindow.append(message);
						}
					}
			);

		}

		//User able to type!
		private void ableToType(final boolean tof){
			SwingUtilities.invokeLater(
					new Runnable() {

						@Override
						public void run() {
							userText.setEditable(tof);

						}
					}
			);
		}
}
