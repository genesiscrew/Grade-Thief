package game.floor;

public interface Tile {
	//public Tile() { // constructor could include player, object, it contains?


	public Location tileLocation();
	public boolean occupied();
	public void setLocation(Location l);
	public String name(); // testing purposes



}
