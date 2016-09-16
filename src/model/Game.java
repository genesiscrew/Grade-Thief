package model;

import java.util.ArrayList;

import game.floor.TileMap;
import items.Item;

/**
 *
 * @author Hamid Abubakr
 *this class contains the game logic ie multiplayer , player interaction with game objects
 */
public class Game {

	public TileMap board;
	private ArrayList<Character> players = new ArrayList<Character>();


	public TileMap getGameMap() {
		return board;

	}




}
