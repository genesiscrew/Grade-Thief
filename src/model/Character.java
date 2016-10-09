package model;

import java.util.ArrayList;

import items.Item;

/**
 * This represents a character in the GameController
 */
public class Character {
	// player name
	private String name;
	// player item list


	public Character(String name) {
		this.name = name;

	}
	/**
	 * helper method to help with debugging game logic and printing players name on board
	 * @return
	 */
	public String getName() {

		return this.name;
	}
	public int getCharacterID() {
		// TODO Auto-generated method stub
		return 0;
	}





	}



