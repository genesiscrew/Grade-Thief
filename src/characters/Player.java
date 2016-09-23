package characters;

import java.util.ArrayList;

import game.floor.Location;
import items.Direction;
import items.Distance;
import items.GameObject;
import items.Item;

public class Player extends Character {

	Location characterLocation;
	private boolean inRoom = false;
	private ArrayList<Item> items = new ArrayList<Item>();



	public Player(int characterID, String characterName) {
		super(characterID, characterName);
		// TODO Auto-generated constructor stub
	}


	public void move(Direction dir, Distance d) {
		// altars details in tile in world
		// altars playerLocation

	}



	public Location getCharacterLocation() {
		return super.getCharacterLocation();
	}

	public void setCharacterLocation(int x, int y) {
		super.setCharacterLocation(x,y);
	}

	// characterLocation = new Location(x, y);
	@Override
	public void characterInteraction(Character c) {
		// TODO Auto-generated method stub

	}
  /**
   * this method specifies how the player can interact with his world
   * two parameters, the object it wants to interact with, and the type of interaction
   * e.g. player wants to view
   */
	@Override
	public void objectInteraction(GameObject c) {


	}
    /**
     *
     * @returns the name of the player
     */
	public String getName() {
		return super.characterName;
	}
	/**
	 * adds an item to player's inventory
	 * @param parameter is the item to add
	 */
	public void addToInventory(Item item){
		this.items.add(item);

	}
	/**
	 *
	 * @returns the player's inventory
	 */
	public ArrayList<Item> getInventory(){
		return this.items;

	}



}
