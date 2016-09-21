package game.floor;
/**
*
* @author Stefan Vrecic
*
*/
public class FloorMap {

	private Tile[][] floorTiles;
	// final int COTTON_FLOOR_WIDTH = 112; // DO NOT EDIT
	// final int COTTON_FLOOR_HEIGHT = 67; // DO NOT EDIT
	// grid size = 67 * 112 = 7504.
	final int FLOOR_WIDTH = 50;
	final int FLOOR_HEIGHT = 50;

	public FloorMap(Tile[][] floorTiles) {
		this.floorTiles = floorTiles;
	}
/**
 * @return -- an (EmptyTile) 2d array of tiles at specified width and height.
 */
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

	public Tile[][] getFloorTiles() {
		return this.floorTiles;
	}

	public void setFloorMap(Tile[][] FloorMap) {
		this.floorTiles = FloorMap;
	}


}
