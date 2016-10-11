package model.floor;
/**
*
* @author Stefan Vrecic
*
*/
public interface Tile {
	//public Tile() { // constructor could include player, object, it contains?


	public Location tileLocation(); // returns the location of the tile
	public boolean occupied(); // specifies whether the tile is occupied
	public void setOccupied(); // specifies that the tile IS occupied
	public void setUnoccupied();  // specifies that the tile IS NOT occupied
	public void setLocation(Location l); // Sets the location of the tile
	public String getName();
	public String name(); // testing purposes
	public Object getObjectonTile();



}