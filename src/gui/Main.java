package gui;

<<<<<<< HEAD

=======
//import sun.audio.AudioData;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;
//import sun.audio.ContinuousAudioDataStream;
>>>>>>> 29405cf59c178a9307e71e345c0c646631833a3e

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * This is the main class that runs the game
 */
public class Main {
    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.createNewGame();
    }

    /**
     * Create a new game and launch in full screen
     * @throws IOException
     */
    public void createNewGame() throws IOException {
        JFrame F = new JFrame();
        Screen ScreenObject = new Screen();
        F.add(ScreenObject);
        F.setUndecorated(true);
        F.setSize(ScreenSize);
        F.setVisible(true);
        F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}