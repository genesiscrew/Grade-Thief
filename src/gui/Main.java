package gui;

import java.awt.*;

/**
 * This is the main class that runs the game
 */
public class Main {
    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {
        new GameController(false);
    }


}