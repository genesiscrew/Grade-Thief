package characters;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import game.floor.Location;
import items.Direction;
import items.Distance;
import items.GameObject;
import items.Item;
import model.Game;

public class Player extends Character implements KeyListener {

	Location characterLocation;
	private boolean inRoom = false;
	private ArrayList<Item> items = new ArrayList<Item>();



	public Player(int characterID, String characterName, Game game) {
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


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_KP_RIGHT) {			
			game.player(uid).moveRight();
		} else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_KP_LEFT) {
			game.player(uid).moveLeft();
		} else if(code == KeyEvent.VK_UP) {
			game.player(uid).moveUp();
		} else if(code == KeyEvent.VK_DOWN) {
			game.player(uid).moveDown();
		}
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



}
