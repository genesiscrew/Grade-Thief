package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import game.floor.Floor;
import game.floor.Room;
import game.floor.Tile;
import game.floor.TileMap;
import items.Container;
import items.Door;
import model.Game;
/**
 *
 * @author Stefan Vrecic
 *
 */
public class MakeRoomTest {

	public void createRoom(gui.Room r) throws IOException{

		// make a floor

		/// load the rooms and add it.
		// get file path
		// make a new room
		// put the file path into room.setTileMap(filePath)
		// this will delegate the work to TileMap.java
		Game game = new Game();

		Floor floor;
		List<Room> floorRooms = new ArrayList<Room>();

		int nextX = 0;
		int nextY = 0;
		final int ADJACENT = 1; // adjacent rooms, add extra wall

		Door d = new Door(0000, "0001",0);

		String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";

		Room room_co237 = new Room(null, null);

		room_co237.setTileMap(co237);

		r.setTileMap(co237);

		//		System.out.println("optional code " + r.roomTileMap.getOptionalCode());
		//		System.out.println("height " + r.roomTileMap.getMapHeight());
		//		System.out.println("width " + r.roomTileMap.getMapWidth());

		TileMap tileMap = r.getTileMap();
		String s = "";
		for (int y = 0 ; y < tileMap.getMapHeight(); y++ ) {
			for (int x = 0 ; x < tileMap.getMapWidth() ; x++) {
				Tile t = tileMap.getTileMap()[x][y];
				if (t != null)
					s = s + (tileMap.getTileMap()[x][y].getName());
			}
			s = s + "\n";
		}

		System.out.println(s);
		System.out.println(r.getTileMap().getItems());

		tileMap.populateRoom(r, tileMap.getItems(), null);

		System.out.println("got her hoooooooooooooooooooooooooooooooooooooooooorrrrrrrrrrrrray");
		s = "";
		for (int y = 0 ; y < tileMap.getMapHeight(); y++ ) {
			for (int x = 0 ; x < tileMap.getMapWidth() ; x++) {
				Tile t = tileMap.getTileMap()[x][y];
				if (t != null) {
					if (tileMap.getTileMap()[x][y].occupied())
						s = s  + (tileMap.getTileMap()[x][y].getObjectonTile().toString());
					else
						s = s + (tileMap.getTileMap()[x][y].getName());

				}

			}
			s = s + "\n";
		}
		System.out.println(s);
	}
}