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
		gaurd1 = new GuardBot(12, "guard1", 16, dist, 0, 2 * 10, 8 * 10, 0, 5, 3, 12, 5, new Color(0, 0, 0));
		gaurd1.setScreen(screen);
		int[] dist2 = { 35, 25, 10};
		guard2 = new GuardBot(12, "guard2", 17, dist2, 0, 83 * 10, 6 * 10, 0, 5, 3, 12, 4, new Color(0, 0, 0));
		guard2.setScreen(screen);
		guardList1.add(gaurd1);
		guardList1.add(guard2);

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
