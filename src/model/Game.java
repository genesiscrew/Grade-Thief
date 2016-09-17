package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.floor.Tile;
import game.floor.TileMap;
import items.GameObject;
import items.Item;

/**
 *
 * @author Hamid Abubakr this class contains the game logic ie multiplayer ,
 *         player interaction with game objects
 */
public class Game {
	// the tile map
	public TileMap board;
	public Tile[][] TileMap;
	// list of all game objects
	static List<GameObject> containedItems = new ArrayList<GameObject>();
	private ArrayList<Character> players = new ArrayList<Character>();

	public Game() {
		TileMap = new Tile[25][7];
		board = new TileMap(TileMap);

		try {
			board.createTileMap(System.getProperty("user.dir") + "/src/map");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TileMap getGameMap() {
		return board;

	}
/**
 * for debugging purposes, draws map into console
 */
	public void drawBoard() {

		int count = 0;
		for (int y = 0; y!= this.board.getTileMap()[y].length; y++) {
			for (int x = 0; x != this.board.getTileMap().length; x++) {

				System.out.printf(this.board.getTileMap()[x][y].getName());
			}
		      System.out.println();

		}

	}

}
