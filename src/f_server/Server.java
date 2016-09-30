package f_server;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;

import gui.GameController;

public class Server extends JFrame{

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private PrintStream p;
	private Scanner sc;
	private GameController guard = new GameController(false);
	private GameController player;
	private Scanner getInput = new Scanner(System.in);


	//set up and run the server
	public void startRunning(){
		try {
			server = new ServerSocket(6789, 100);
			while(true){
				try {
					waitForConnection();
					setupStream();
					//whileChatting();
					update();
				} catch (EOFException e) {
					System.out.println("You got disconnected");
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
		System.out.println("Waiting for someone to connect...");
		connection = server.accept();
		System.out.println("Now connected to" + connection.getInetAddress().getHostName());
	}


	private void setupStream() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		System.out.println("Stream are now setup! ");
	}


	//During the chat conversation
	private void whileChatting() throws IOException{
		/*boolean continueType = true;
		do {
			while (continueType) {
				System.out.print("Write anything man: ");
				String message = getInput.nextLine();
				sendMessage(message);
				if (message.equalsIgnoreCase("END")) {
					continueType = false;
				}
			}

			appearMessage();
		} while (true);*/
	}

	private void update() {
		while (true) {
			try {
				sendData();
				receiveData();
			} catch (IOException e) {

			}

		}
	}

	//close streams and sockets after you are done chatting!!
	private void closeCrap(){
		System.out.println("Closing connectin...");
		try {

			output.close();
			input.close();
			connection.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*private void sendMessage(String message) throws IOException{
		p = new PrintStream(connection.getOutputStream());
		p.println(message);
	}

	private void appearMessage() throws IOException{
		sc = new Scanner(connection.getInputStream());
		System.out.println(sc.nextLine());
	}*/


	//Send data to clients
	private void sendData() throws IOException{
		//Send updated board to the Clients
		//The loop is true for now
		double []guard = this.guard.getOtherPlayersPosition(true);
		output.writeObject(guard);
		output.flush();

		/*double []player = this.player.getOtherPlayersPosition(false);
		output.writeObject(player);*/
		output.flush();

	}
	//Recieve data from Clients
	private void receiveData() throws IOException{
		//Recieve the coordinates of the client and update the board.
		try {
			double[] updatedGuard = (double []) input.readObject();
			this.guard.updatePosition(true, updatedGuard);
			double[] updatedPlayer = (double []) input.readObject();
			this.player.updatePosition(false, updatedPlayer);


		}catch (ClassNotFoundException e){
			e.printStackTrace();
		}

	}

}
