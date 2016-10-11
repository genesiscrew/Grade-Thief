package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import controller.GameController;
import controller.MakeSound;
import model.floor.Location;


/**
 * This class is resposible to make a ServerSocket
 * @author shenavmost
 *
 */
public class Server{

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private GameController guard = new GameController(true);


	/**
	 * Game starts from here and its responsible for wait for players to connect and setup stream and update the game
	 */
	public void startRunning() {
		try {
			Thread playMusic = new Thread(){
				@Override
				public void run(){
					MakeSound ms = new MakeSound();
					ms.playSound("/am/courtenay/home1/abubakhami/workspace/grade-thief/src/bg-music.wav");
				}
			};

			playMusic.start();

			server = new ServerSocket(6789, 100);
			while (true) {
				try {
					waitForConnection();
					setupStream();
				    update();
				} catch (EOFException e) {
					System.out.println("You got disconnected");
				} finally {
					closeCrap();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is responsible for waiting for other players to connect
	 * @throws IOException
	 */
	private void waitForConnection() throws IOException {
		System.out.println("Waiting for someone to connect...");
		connection = server.accept();
		System.out.println("Now connected to" + connection.getInetAddress().getHostName());
	}

	/**
	 * This method is responsible for setup stream between server and clinet
	 * @throws IOException
	 */
	private void setupStream() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		System.out.println("Stream are now setup! ");
	}


	/**
	 * This method is responsible for updating the game world
	 * @throws IOException
	 */
	private void update() throws IOException {


		while (true) {
			sendData();
			recieveData();
		}
	}

	/**
	 * This method is responsible for sending guard position to client
	 * @throws IOException
	 */
	private void sendData() throws IOException {

		//Get player's position
		double []guardPos = guard.getGuardPosition();
		output.writeDouble(guardPos[0]);
		output.flush();
		output.writeDouble(guardPos[1]);
		output.flush();
		output.writeDouble(guardPos[2]);
		output.flush();

		//Get player's room
		String room = guard.getPlayer().getCurrentPlayer().getLevelName();
		output.writeObject(room);
		output.flush();

		//Get the other player's position
		Location loc = guard.getPlayer().getCurrentPlayer().getLocation();
		int x = loc.locX(); int y = loc.locY();
		output.writeInt(x);
		output.flush();
		output.writeInt(y);
		output.flush();

	}

	/**
	 * This method is responsible for receinving data from client and update the server
	 * @throws IOException
	 */
	private void recieveData() throws IOException {
		try {


			//Receive other player's Position
			double playerPosX = (double) input.readDouble();
			double playerPosY = (double) input.readDouble();
			double playerPosZ = (double) input.readDouble();
			double[] newPos = new double[]{playerPosX,playerPosY,0};
			int timer = (int) input.readInt();
			String room = (String) input.readObject();
			guard.getPlayer().getOtherPlayer().setRoom(room);
			guard.getPlayer().timer = timer;
			guard.setPlayerPosition(newPos);
			int x = (int) input.readInt();
			int y = (int) input.readInt();

			guard.getPlayer().getOtherPlayer().setLocation(new Location(x,y));



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

	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}

	public Socket getConnection() {
		return connection;
	}

	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public GameController getGuard() {
		return guard;
	}

	public void setGuard(GameController guard) {
		this.guard = guard;
	}



}
