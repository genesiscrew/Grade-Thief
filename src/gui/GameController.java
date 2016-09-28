package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wareinadam on 28/09/16.
 */
public class GameController {

    private final int NUM_OF_PLAYERS = 2;

    // Players
    private Screen player;
    private Screen guard;

    // Position is stored using x, y, z
    private double[] playerPosition = new double[]{15, 5, 10};
    private double[] guardPosition = new double[]{100, 100, 10};

    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public GameController() {
        player = createNewGame(false);
        guard = createNewGame(true);
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
     * @param isGuard true: updates guard pos, false: updates players pos.
     * @param viewFrom [x,y,z] position of the player
     */
    public void updatePosition(boolean isGuard, double[] viewFrom) {
        if(isGuard){
            guardPosition = viewFrom;
        }else{
            playerPosition = viewFrom;
        }
    }
}
