package gui;

import javax.swing.*;
import java.awt.*;


/**
 * Created by wareinadam on 28/09/16.
 */
public class GameController {

    // Players
    private Screen player;

    // Position is stored using x, y, z
    private double[] playerPosition = new double[]{15, 5, 10};
    private double[] guardPosition = new double[]{100, 100, 10};

    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private boolean isGuard;

    public GameController(boolean isGuard) {
        this.isGuard = isGuard;
        player = createNewGame(false);
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
        onScreenText.setFont(new Font("Courier New", Font.BOLD,12));
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

    //This is for server
    public double[] getPlayerPosition() {
        if (isGuard)
            return guardPosition;
        return playerPosition;
    }

    public void setPlayerPosition(double[] playerPosition) {
        if (isGuard)
            this.guardPosition = playerPosition;
        else
            this.playerPosition = playerPosition;
    }
}
