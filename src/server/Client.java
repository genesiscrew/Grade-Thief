package server;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.tools.DocumentationTool.Location;

import controller.GameController;



public class Client {

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String serverIP;
	private Socket connection;
	private GameController player = new GameController(false);

	/**
	 *
	 * @param host
	 *
	 */
	public Client(String host) {
		serverIP = host;
	}

	/**
	 * Game starts from here
	 * First connecting to server then setting up stream and then update client every single time
	 */
	public void startRunning() {
		try {
			connectionToServer();
			setupStreams();
			update();

		} catch (EOFException e) {
			System.out.println("Client terminated connection");
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			closeCrap();
		}
	}

	/**
	 *
	 * @throws IOException
	 * This method is responsible for connecting client to server
	 */
	private void connectionToServer() throws IOException {
		System.out.println("Attempting connection...");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		System.out.println("Connected to: " + connection.getInetAddress().getHostName());
	}

	/**
	 *
	 * @throws IOException
	 *	This method is responsible for setting up stream between client and server.
	 */
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		System.out.println("You are now connected");
	}


	/**
	 * This method is responsible for sending data and receive the data from Server.
	 * @throws IOException
	 */
	private void update() throws IOException {
		while (true) {
			sendData();
			recieveData();
		}
	}

	/**
	 * This method is responsible for sending player's position to server.
	 * @throws IOException
	 */

	private void sendData() throws IOException {
		double[] playerPos = player.getPlayerPosition();
		output.writeDouble(playerPos[0]);
		output.flush();
		output.writeDouble(playerPos[1]);
		output.flush();
		output.writeDouble(playerPos[2]);
		output.flush();
		int timer = player.getPlayer().timer;
		output.writeInt(timer);
		output.flush();
		String room = player.getPlayer().getCurrentPlayer().getLevelName();
		output.writeObject(room);
		output.flush();

		model.floor.Location loc = player.getPlayer().getCurrentPlayer().getLocation();
		int x = loc.locX(); int y = loc.locY();
		output.writeInt(x);
		output.flush();
		output.writeInt(y);
		output.flush();
	}
	/**
	 * This method is responsible for receiving data from server and update the guard position
	 * @throws IOException
	 */
	private void recieveData() throws IOException {

		try {
			double guardPosX = (double) input.readDouble();
			double guardPosY = (double) input.readDouble();
			double guardPosZ = (double) input.readDouble();
			double[] newPos = new double[] { guardPosX, guardPosY, 0 };
			player.setGuardPosition(newPos);
			String room = (String) input.readObject();
			player.getPlayer().getOtherPlayer().setRoom(room);
			int x = (int) input.readInt();
			int y = (int) input.readInt();
			player.getPlayer().getOtherPlayer().setLocation(new model.floor.Location(x,y));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * This method is responsible for closing the connection after game is finished.
	 */
	private void closeCrap() {
		System.out.println("Closing connectin...");
		try {

			output.close();
			input.close();
			connection.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}

	public ObjectInputStream getInput() {
		return input;
	}

	public void setInput(ObjectInputStream input) {
		this.input = input;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public Socket getConnection() {
		return connection;
	}

	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public GameController getPlayer() {
		return player;
	}

	public void setPlayer(GameController player) {
		this.player = player;
	}






}
