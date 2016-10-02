package gui;

import javax.swing.*;
import java.awt.*;


/**
 * Created by wareinadam on 28/09/16.
 */
public class GameController {

    // Players

    private static Screen player;

    //private Screen guard;
    // Position is stored using x, y, z
    private static double[] playerPosition = new double[]{15, 5, 10};
    private static double[] guardPosition = new double[]{100, 100, 10};

    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static boolean isGuard;

    public GameController(boolean isGuard) {
        this.isGuard = isGuard;
        player = createNewGame(isGuard);
        //guard = createNewGame(!isGuard);

    }

    /**
     * Create a new game and launch in full screen
     */
    public Screen createNewGame(boolean guard) {
        JFrame F = new JFrame();
        Screen screenObject = new Screen(this, guard);
        F.add(screenObject);
        F.setUndecorated(true);
        F.setSize(ScreenSize);
        F.setVisible(true);
        F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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


}
