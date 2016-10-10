package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

import model.characters.GuardBot;
import view.Screen;

/**
 * Created by wareinadam on 28/09/16.
 */
public class GameController {

	// Players

	private static Screen player;

	// private Screen guard;
	// Position is stored using x, y, z
	private static double[] playerPosition = new double[] { 50, 100, 10 };
	private static double[] guardPosition = new double[] { 100, 100, 10 };
	GuardBot gaurd1;
	GuardBot guard2;

	private ArrayList<GuardBot> guardList1;
	private ArrayList<GuardBot> guardList2;


	private Screen screenObject;

	private JFrame frame;

    GuardBot guard3;

	private GuardBot guard4;

	private GuardBot guard5;

	static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static boolean isGuard;

	public GameController(boolean isGuard) {
		guardList1 = new ArrayList<>();
		this.isGuard = isGuard;
		player = createNewGame(isGuard);

		// guard = createNewGame(!isGuard);

		/*
		 * InputStream is =
		 * getClass().getClassLoader().getResourceAsStream("bg-music.wav");
		 * MakeSound ms = new MakeSound(); if (!isGuard) {
		 * ms.playSound("src/bg-music.wav");
		 */
	}

	public GuardBot getGuardBot(String guardName) {

		for (GuardBot g : guardList1) {
			if (g.getName().equals(guardName)) {
				// System.out.println(guardName);
				return g;
			}
		}
		return null;

	}

	/**
	 * Create a new game and launch in full screen
	 */
	public Screen createNewGame(boolean guard) {
		frame = new JFrame();
		frame.setTitle("Grade Thief");
		screenObject = new Screen(this, guard, "level");
		frame.add(screenObject);
		/// starts all guardbots movements

		JLabel onScreenText = new JLabel("This is some example text");
		onScreenText.setFont(new Font("Courier New", Font.BOLD, 12));
		// frame.add(onScreenText, SwingConstants.CENTER);
		frame.setUndecorated(true);
		frame.setSize(ScreenSize);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setupGuardbots("level", screenObject);
		return screenObject;
	}

	public void setupGuardbots(String map, Screen screen) {
		int[] dist = { 14, 45, 14, 45 };
		gaurd1 = new GuardBot(12, "guard1", "level", 16, dist, 0, 2 * 10, 8 * 10, 0, 5, 3, 12, 0.4, new Color(0, 0, 0));
		gaurd1.setScreen(screen);
		int[] dist2 = { 32, 29};
		guard2 = new GuardBot(12, "guard2", "level", 12, dist2, 0, 81 * 10, 6 * 10, 0, 5, 3, 12, 0.3, new Color(0, 0, 0));
		int[] dist3 = {35, 29};
		guard3 = new GuardBot(12, "guard3", "level", 10, dist3, 0, 81 * 10, 30 * 10, 0, 5, 3, 12, 0.3, new Color(0, 0, 0));
		int[] dist4 = {25, 25};
		guard4 = new GuardBot(12, "guard4", "level2", 4, dist4, 0, 48 * 10, 2 * 10, 0, 5, 3, 12, 0.1, new Color(0, 0, 0));
		int[] dist5 = {35, 35};
		guard5 = new GuardBot(12, "guard5", "level2", 1, dist5, 0, 2 * 10, 6 * 10, 0, 5, 3, 12, 0.1, new Color(0, 0, 0));
		guard2.setScreen(screen);
		guard3.setScreen(screen);
		guard4.setScreen(screen);
		guard5.setScreen(screen);
		guardList1.add(gaurd1);
		guardList1.add(guard2);
		guardList1.add(guard3);
		guardList1.add(guard4);
		guardList1.add(guard5);

	}

	/**
	 * Update the controllers understanding of the players
	 *
	 * @param isGuard
	 *            true: updates guard pos, false: updates players pos.
	 * @param viewFrom
	 *            [x,y,z] position of the player
	 */
	public void updatePosition(boolean isGuard, double[] viewFrom) {
		if (isGuard) {
			guardPosition = viewFrom;
		} else {
			playerPosition = viewFrom;
		}
	}

	public double[] getOtherPlayersPosition(boolean isGuard) {
		if (isGuard) {
			return playerPosition;
		} else {
			return guardPosition;
		}

	}

	/*
	 * ========================================================================*
	 * * SETERS AND GETTERS * *
	 * ========================================================================
	 */

	public static Screen getPlayer() {
		return player;
	}

	public void setPlayer(Screen player) {
		this.player = player;
	}

	public static double[] getPlayerPosition() {
		return playerPosition;
	}

	public static void setPlayerPosition(double[] playerPosition) {
		GameController.playerPosition = playerPosition;
	}

	public static double[] getGuardPosition() {
		return guardPosition;
	}

	public static void setGuardPosition(double[] guardPosition) {
		GameController.guardPosition = guardPosition;
	}

	public static Dimension getScreenSize() {
		return ScreenSize;
	}

	public static void setScreenSize(Dimension screenSize) {
		ScreenSize = screenSize;
	}

	public static boolean isGuard() {
		return isGuard;
	}

	public static void setGuard(boolean isGuard) {
		GameController.isGuard = isGuard;
	}

	public Thread createGuardThread(GuardBot gaurd, int delay) {
		Thread guardThread = new Thread() {
			public void run() {
				// move the guard in a fixed loop, once he reaches certain
				// coordinate on the Map, change destination
				// if () {}
				// gaurd will keep moving

			}

		};

		// return new double[]{g.getX(), g.getY(), g.getZ()};
		return null;
	}

	public ArrayList<GuardBot> getGuardList() {
		return this.guardList1;
	}

	public double[] getOtherBotPosition(String guardName) {
		return null;
	}

}
