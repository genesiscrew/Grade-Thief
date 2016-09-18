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

	public String toString() {
		return this.getClass().getSimpleName() + " " + location.toString();
	}


	@Override
	public String name() {
		if (occupied())
			return "P";
		return "D";
	}

	@Override
	public void setOccupied() {
		isOccupied = true;

	}

	@Override
	public void setUnoccupied() {
		isOccupied = false;

	}
}
