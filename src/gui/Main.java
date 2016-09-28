package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

/**
 * This is the main class that runs the game
 */
public class Main {
    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) throws IOException {
        new GameController();
    }


}