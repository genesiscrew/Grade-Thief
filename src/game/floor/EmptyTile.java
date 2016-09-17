package game.floor;

import characters.Guard;
import characters.Player;
import items.Keys;
import model.Character;

public class EmptyTile implements Tile {

	Location location;
	boolean occupied = false;
	Object o;
	private String name = "-";

	@Override
	public Location tileLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public boolean occupied() {
		return occupied;
	}



	@Override
	public void setLocation(Location l) {
		this.location = l;

	}
/**
 * adds an object to the tile e.g. player, key etc.
 * @param any object
 */
	public void addObjecttoTile(Object o){
		this.o = o;

		if (o instanceof Guard) {
			this.name = "G";
			occupied = true;
		}
		else if (o instanceof Player) {
			this.name = ((Player) o).getName();
			occupied = true;
		}
		else if (o instanceof Keys) {
			this.name = ((Keys) o).itemType();
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
		this.name = "-";
		occupied = false;

	}

}
