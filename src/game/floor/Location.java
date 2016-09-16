package game.floor;

public class Location {
	private int x; // must be between 0 and 25
	private int y; // must be between 0 and 26

	public Location( int x, int y) {
		this.x = x;
		this.y =y;

	}

	public int row() {
		return x;
	}

	public int column() {
		return y;
	}

	public boolean isValid() {
		//return y >= 0 && y <= 26 && x >= 0 && x <= 25;
		return false;
	}

	public boolean equals(Object o) {
		if(o instanceof Location) {
			Location p = (Location) o;
			return x == p.row() && y == p.column();
		}
		return false;
	}

	public int hashCode() {
		return x ^ y;
	}

	public String toString() {
		return ((char)('a'+(y-1))) + Integer.toString(x);
	}

}
