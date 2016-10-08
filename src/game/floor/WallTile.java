package game.floor;
/**
*
* @author Stefan Vrecic
*
*/
public class WallTile extends EmptyTile implements Tile  {

	Location location;
	private final boolean isOccupied = false; // wall tiles can NEVER be occupied
	private boolean containsCamera = false;
	private boolean cameraActivated = false;

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

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "W";
	}

	public String toString() {
		return this.getClass().getSimpleName() + " " + location.toString();
	}

	public void setCamera() {
		this.containsCamera = true;
	}

	public Object getObjectonTile(){
		return null;
	}
	public boolean cameraIsActivated() {
		return this.cameraActivated;
	}
	public boolean containsCamera() {
		return containsCamera;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "W";
	}

	@Override
	public void setOccupied() {
	}

	@Override
	public void setUnoccupied() {
	}

}