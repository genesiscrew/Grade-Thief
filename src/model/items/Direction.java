package model.items;

public class Direction {

	private Dir dir;

	public enum Dir{
		NORTH, EAST, SOUTH, WEST
	}

		public Direction(Dir dir) {
			this.dir = dir;
		}
		
		public Dir getDirection() {
			return dir;
			
		}
}

