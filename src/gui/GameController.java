package gui;

import javax.swing.*;

import characters.GuardBot;
import model.Game;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by wareinadam on 28/09/16.
 */
public class GameController {

    // Players

    private static Screen player;

    //private Screen guard;
    // Position is stored using x, y, z
    private static double[] playerPosition = new double[]{50, 100, 10};
    private static double[] guardPosition = new double[]{100, 100, 10};

    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static boolean isGuard;


    public GameController(boolean isGuard) {
        this.isGuard = isGuard;
        player = createNewGame(isGuard);

        //guard = createNewGame(!isGuard);

    	InputStream is = getClass().getClassLoader().getResourceAsStream("bg-music.wav");
  	MakeSound ms = new MakeSound();
    	ms.playSound("src/bg-music.wav");
    }

    /**
     * Create a new game and launch in full screen
     */
    public Screen createNewGame(boolean guard) {
        JFrame frame = new JFrame();
        frame.setTitle("Grade Thief");
        Screen screenObject = new Screen(this, guard);
        frame.add(screenObject);
        JLabel onScreenText = new JLabel("This is some example text");
        onScreenText.setFont(new Font("Courier New", Font.BOLD, 12));
        //frame.add(onScreenText, SwingConstants.CENTER);
        frame.setUndecorated(true);
        frame.setSize(ScreenSize);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return screenObject;
    }

    /**
     * Update the controllers understanding of the players
     *
     * @param isGuard  true: updates guard pos, false: updates players pos.
     * @param viewFrom [x,y,z] position of the player
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



	/*========================================================================*
	 *                                                                        *
	 * 						SETERS AND GETTERS								  *
	 *                                                                        *
	 *========================================================================*/


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


					// update direction of guard based on hardcoded route
					// through Tilemap

				try {
					Thread.sleep(delay);
				//	gaurd.move();



				} catch(InterruptedException e) {
					// should never happen
				}
					// draw board intp console for debugging purposes
					//game.drawBoard(gaurd.getFloorNo());

			}
		};
		return guardThread;
	}
}
