package items;

public class Direction {

	Dir dir;

	public enum Dir{
		NORTH, EAST, SOUTH, WEST
	}

		public Direction(Dir dir) {
			this.dir = dir;
		}
}

