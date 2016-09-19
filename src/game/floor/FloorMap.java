package game.floor;

public class FloorMap {

	Tile[][] floorTiles;
	final int FLOOR_WIDTH = 50;
	final int FLOOR_HEIGHT = 50;

	public FloorMap(Tile[][] floorTiles) {
		this.floorTiles = floorTiles;
	}

	public Tile[][] createFloorMap() {
		Tile[][] emptyMap;
		emptyMap = new Tile[FLOOR_WIDTH][FLOOR_HEIGHT];

		for (int h = 0; h < FLOOR_HEIGHT; h++) {
			for (int w = 0; w < FLOOR_WIDTH; w++) {

				EmptyTile t = new EmptyTile();
				t.setLocation(new Location(w, h));
				emptyMap[h][w]  = t;

			}
		}

		return emptyMap;
	}

	public void setFloorMap(Tile[][] FloorMap) {
		this.floorTiles = FloorMap;
	}


}
