package f_server;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import gui.GameController;
import model.Game;

public class Server{

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	private GameController guard = new GameController(true);

	public void startRunning() {
		try {

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

	private void waitForConnection() throws IOException {
		System.out.println("Waiting for someone to connect...");
		connection = server.accept();
		System.out.println("Now connected to" + connection.getInetAddress().getHostName());
	}

	private void setupStream() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		System.out.println("Stream are now setup! ");
	}


	private void update() throws IOException {
		while (true) {
			sendData();
			recieveData();
		}
	}

	private void sendData() throws IOException {
		double []guardPos = guard.getGuardPosition();
		output.writeDouble(guardPos[0]);
		output.flush();
		output.writeDouble(guardPos[1]);
		output.flush();
		output.writeDouble(guardPos[2]);
		output.flush();
	}

	private void recieveData() throws IOException {
		try {
			double playerPosX = (double) input.readDouble();
			double playerPosY = (double) input.readDouble();
			double playerPosZ = (double) input.readDouble();
			double[] newPos = new double[]{playerPosX,playerPosY,playerPosZ};
			guard.setPlayerPosition(newPos);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

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
