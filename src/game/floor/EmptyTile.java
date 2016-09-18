package game.floor;

public class EmptyTile implements Tile {

	Location location;
	boolean isOccupied = false;

	@Override
	public Location tileLocation() {
		return location;
	}

	@Override
	public boolean occupied() {
		return isOccupied;
	}

	public void setOccupied() {
		this.isOccupied = true;
	}

	public void setUnoccupied() {
		this.isOccupied = false;
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
			return "a";
		return "E";
	}
}
