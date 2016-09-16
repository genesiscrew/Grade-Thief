package game.floor;

public class EmptyTile implements Tile {

	Location location;
	boolean occupied = false;
	Object o;

	@Override
	public Location tileLocation() {
		// TODO Auto-generated method stub
		return location;
	}

	@Override
	public boolean occupied() {
		return occupied;
	}



	@Override
	public void setLocation(Location l) {
		this.location = l;

	}

	public void addObjecttoTile(Object o){
		this.o = o;
		occupied = true;

	}

}
