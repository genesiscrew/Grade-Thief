package game.floor;

public class WallTile implements Tile  {

	Location location;

	@Override
	public Location tileLocation() {
		// TODO Auto-generated method stub
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
	public String getName() {
		// TODO Auto-generated method stub
		return "*";
	}

}
