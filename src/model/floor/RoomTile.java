package model.floor;
/**
*
* @author Stefan Vrecic
*
*/
public class RoomTile extends EmptyTile implements Tile  {

	Location location;
	private boolean isOccupied = false;

	/**
	 * gets the location of the roomTile
	 */
	@Override
	public Location tileLocation() {
		return location;
	}
	/**
	 * returns the object on the tile -- not used since 3D
	 */
	public Object getObjectonTile(){
		return null;
	}

	/**
	 * sets the roomTile to be occupied
	 * @return
     */
	@Override
	public boolean occupied() {
		return isOccupied;
	}

	/*
	 * sets the location of the roomTile
	 * @param l
    */
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

	/**
	 * sets the tile to be occupied
	 */
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