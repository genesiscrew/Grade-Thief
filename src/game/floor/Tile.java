package game.floor;

public interface Tile {
	//public Tile() { // constructor could include player, object, it contains?


	public Location tileLocation();
	public boolean occupied();
	public void setOccupied();
	public void setUnoccupied();
	public void setLocation(Location l);
	public String getName();
	public String name(); // testing purposes



}
