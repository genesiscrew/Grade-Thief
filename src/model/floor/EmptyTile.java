package model.floor;

import model.characters.GuardBot;
import model.items.KeyDraw;
/**
*
* @author Stefan Vrecic
* EmptyTile.java instance of Tile which represents a blank tile in the game. Can be occupied by a player or an object
*
*/
public class EmptyTile implements Tile {

	Location location;
	Object o;
	private String name = "E";


	boolean isOccupied = false;

	@Override
	public Location tileLocation() {
		return location;
	}

	/**
	 * returns wheter empty tile occupied
	 */
	@Override
	public boolean occupied() {
		return isOccupied;
	}

	/**
	 * sets the tile to be occupied
	 */
	public void setOccupied() {
		this.isOccupied = true;
	}

	/**
	 * unoccupies the tile
	 */
	public void setUnoccupied() {
		this.isOccupied = false;
	}


/**
 * sets the location of the EmptyTile;
 */
	@Override
	public void setLocation(Location l) {
		this.location = l;

	}
/**
 * adds an object to the tile e.g. player, key etc.
 * @param any object
 */
	public void addObjectToTile(Object o){
		this.o = o;

		if (o instanceof GuardBot) {
			this.name = "G";
			isOccupied = true;
		}
		else if (o instanceof model.characters.Player) {
			this.name = "P";
			isOccupied = true;
		}
		else if (o instanceof KeyDraw) {
			this.name = ((KeyDraw) o).itemType();
			isOccupied = true;
		}



	}
	/**
	 * this is a helper method to debug game logic and print updated game board
	 * @return
	 */
	public String getName() {

		return this.name;
	}
	/**
	 * this method returns the object that is contained in tile
	 * @return
	 */
	public Object getObjectonTile(){

		return this.o;
	}
/*
 * this method removes all objects contained in tile
 */
	public void resetEmptyTile() {
		this.o = null;
		this.name = "e";
		isOccupied = false;

	}


	public String toString() {
        return this.name;
		//return this.getClass().getSimpleName() + " " + location.toString();
	}

	@Override
	public String name() {
		if (occupied())
			return this.name.substring(0, 1);
		return "E";
	}
}