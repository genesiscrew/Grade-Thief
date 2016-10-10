package view;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;


/**
 * This is the main class that runs the game
 */
public class Main {
    static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {

        new GameController(false);
    }
}