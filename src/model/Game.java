package model;

import java.util.ArrayList;
import java.util.List;

import game.floor.Tile;
import game.floor.TileMap;
import items.GameObject;
import items.Item;

/**
 *
 * @author Hamid Abubakr
 *this class contains the game logic ie multiplayer , player interaction with game objects
 */
public class Game {
	// the tile map
	public TileMap board;
	public Tile[][] TileMap;
	// list of all game  objects
	static List<GameObject> containedItems = new ArrayList<GameObject>();
	private ArrayList<Character> players = new ArrayList<Character>();

    public Game() {
    	TileMap = new Tile[25][7];
    	board = new TileMap(TileMap);
    	board.createTileMap("user.dir" + "/src/map");
    }
	public TileMap getGameMap() {
		return board;

	}
	public void drawBoard() {
		
		
	}




}
