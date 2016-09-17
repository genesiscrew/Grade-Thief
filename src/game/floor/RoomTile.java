package game.floor;

public class RoomTile implements Tile {

	Location location;

	@Override
	public Location tileLocation() {
		return location;
	}

	@Override
	public boolean occupied() {
		return false;
	}

	@Override
	public void setLocation(Location l) {
		this.location = l;

	}
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "R";
	}

	public String toString() {
		return this.getClass().getSimpleName() + " " + location.toString();
	}
}
