package model.floor;
/**
*
* @author Stefan Vrecic
*
*/
public class RoomTile extends EmptyTile implements Tile  {

	Location location;
	private boolean isOccupied = false;

	@Override
	public Location tileLocation() {
		return location;
	}
	public Object getObjectonTile(){
		return null;
	}
	@Override
	public boolean occupied() {
		return isOccupied;
	}

	@Override
	public void setLocation(Location l) {
		this.location = l;

	}
	@Override
	public String name() {
		if (occupied())
			return "a";
		return "R";
	}

	public String toString() {
		return this.getClass().getSimpleName() + " " + location.toString();
	}

	@Override
	public void setOccupied() {
		isOccupied = true;
	}

	@Override
	public void setUnoccupied() {

	}

	@Override
	public String getName() {
		return "R";
	}
}