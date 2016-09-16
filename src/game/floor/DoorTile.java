package game.floor;

import items.Door;
/**
*
* Author: Stefan
*
*/
public class DoorTile implements Tile  {

	Door door;
	Location location;
	private boolean isOccupied = false;


	public void setDoor(Door door) {
		this.door = door;
	}

	@Override
	public Location tileLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public boolean occupied() {
		return isOccupied;
	}

	@Override
	public void setLocation(Location l) {
		this.location = l;

	}

}
