package game.floor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import items.Door;

public class makeRoomTest {

	public static void main(String[] args) throws IOException {

		// make a floor

		/// load the rooms and add it.
		// get file path
		// make a new room
		// put the file path into room.setTileMap(filePath)
		// this will delegate the work to TileMap.java

		Floor floor;
		List<Room> floorRooms = new ArrayList<Room>();

		int nextX = 0;
		int nextY = 0;
		final int ADJACENT = 1; // adjacent rooms, add extra wall

		Door d = new Door(0000, "0001");
		Room r = new Room(null, d);

		String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";

		Room room_co237 = new Room(null, null);

		room_co237.setTileMap(co237);

		r.setTileMap(co237);

		//		System.out.println("optional code " + r.roomTileMap.getOptionalCode());
		//		System.out.println("height " + r.roomTileMap.getMapHeight());
		//		System.out.println("width " + r.roomTileMap.getMapWidth());

		for (int y = 0 ; y < r.roomTileMap.getMapHeight(); y++ ) {
			for (int x = 0 ; x < r.roomTileMap.getMapWidth() ; x++) {
				Tile t = r.roomTileMap.TileMap[x][y];
				if (t != null)
					System.out.println(r.roomTileMap.TileMap[x][y]);
			}
		}

	}

}