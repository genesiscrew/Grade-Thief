package model;

import java.util.ArrayList;

import items.Item;

/**
 * This represents a character in the Game
 */
public class Character {
	// player name
	private String name;
	// player item list
	private ArrayList<Item> items = new ArrayList<Item>();

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
	
	



	}



