package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Game;
import model.GameLoader;

import java.io.File;
import java.io.IOException;

import characters.Guard;
import game.floor.EmptyTile;
import items.Direction;
import items.Direction.Dir;
import items.Distance;
import items.GameObject;


/**
 * @Author Adam Wareing
 * This is the controller that handles logic from when buttons are clicked
 */
public class Controller {


    /**
     * Called when the NewGame button is selected
     *
     * @param event
     */
    public void setupGame(javafx.event.ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = new FXMLLoader().load(getClass().getResource("/scenes/new_game.fxml"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Called when the LoadGame button is selected
     *
     * @param event
     */
    public void loadGame(javafx.event.ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        // add .txt extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        // Set title and launch
        fileChooser.setTitle("Open Resource File");
        File gameFile = fileChooser.showOpenDialog(null);
        if (gameFile != null) {
            new GameLoader(gameFile);
        }
    }


    /**
     * Called when the Settings button is selected
     *
     * @param event
     */
    public void settings(javafx.event.ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = new FXMLLoader().load(getClass().getResource("/scenes/settings.fxml"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Called when the BackButton button is selected from the settings view
     *
     * @param event
     */
    public void backClickedFromSettings(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = new FXMLLoader().load(getClass().getResource("/scenes/home_screen.fxml"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void newGame(ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Parent root = null;
        try {
            root = new FXMLLoader().load(getClass().getResource("/scenes/game.fxml"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

	public static void main(String[] args) {
		// create new game object
		Game game = new Game();

		//load game map from text file


		// create new gaurd object
				Distance dist = new Distance(1);
				Guard gaurd1 = new Guard(0, "guard1");
				// set gaurd's location on map
				gaurd1.setCharacterLocation(0, 7);
				((EmptyTile)game.getGameMap().getTileMap()[0][7]).addObjecttoTile(gaurd1);


				// create a thread for the guard, so that he can move within map independent of player

				Thread guardThread = new Thread() {
				    public void run() {
				    	// move the guard in a fixed loop, once he reaches certain coordinate on the Map, change destination
				    	//if () {}
				    	// gaurd will keep moving
				    	while (gaurd1.checkforIntruder(game)){
				    	// update direction of guard based on hardcoded route through Tilemap
				    	Direction dir = new Direction(Dir.EAST);
				    	gaurd1.move(dir, dist);
				    	game.drawBoard();
				    	}



				    }
				};
          // start the guard movement, thread stops running when intruder caught
          guardThread.start();

	}

}
