package game.floor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.SliderUI;

import characters.Player;
import items.Door;

/**
*
* @author Stefan Vrecic
*
*/
public class makeFloorTest {

	public static void main(String[] args) throws IOException {

		// make a floor
		/// load the rooms and add it.
		// get file path
		// make a new room
		// put the file path into room.setTileMap(filePath)
		// this will delegate the work to TileMap.java


		List<Room> floorRooms = new ArrayList<Room>();

		int nextX = 0;
		int nextY = 0;
		final int ADJACENT = 1; // adjacent rooms, add extra wall

		Door d = new Door(0000, "0001");
		Room r = new Room(null, d);

		String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";
		String co238 = System.getProperty("user.dir") + "/src/game/floor/co238";
		String co243 = System.getProperty("user.dir") + "/src/game/floor/co243";

		Door door_co237 = new Door(0000, "237");
		Door door_co238 = new Door(0000, "238");
		Door door_co243 = new Door(0000, "243");

		Room room_co237 = new Room(null, door_co237);
		Room room_co238 = new Room(null, door_co238);
		Room room_co243 = new Room(null, door_co243);

		room_co237.setTileMap(co237);
		room_co238.setTileMap(co238);
		room_co243.setTileMap(co243);

		floorRooms.add(room_co237);
		floorRooms.add(room_co238);
		floorRooms.add(room_co243);

		FloorMap floorMap = new FloorMap(null); // creates empty floor map
		floorMap.setFloorMap(floorMap.createFloorMap()); // creates a 2d array or tiles and adds it to FloorMap

		Floor floor = new Floor(floorRooms, null, floorMap);

		System.out.println("bounding co238 " + room_co238.roomTileMap.getMapWidth() + " ht " + room_co238.roomTileMap.getMapHeight());
		room_co238.setBoundingBox(nextX, nextY, room_co238.roomTileMap.getMapWidth(), room_co238.roomTileMap.getMapHeight());

		nextX += room_co238.roomTileMap.getMapWidth(); // + ADJACENT;
		System.out.println("nextX " + nextX + " width" + room_co237.roomTileMap.getMapWidth());
		nextY += 11;

		room_co237.setBoundingBox(nextX, nextY, room_co237.roomTileMap.getMapWidth(), room_co237.roomTileMap.getMapHeight());

		nextY += room_co237.roomTileMap.getMapHeight() + 5; // corridor 5 tiles
		room_co243.setBoundingBox(0, nextY, room_co243.roomTileMap.getMapWidth(), room_co243.roomTileMap.getMapHeight());

		for (Room room : floorRooms) {

			int[] bounds = room.getBoundingBox();
			floor.addRoom(room, bounds[0], bounds[1], bounds[2], bounds[3], room.roomTileMap.getDoors());

			//			for (int sy = room.sy; sy <= sy+room.h; sy++) {
			//			for (int sx = room.sx; sx <= sx+room.w; sx++) {
			//
			//			}
			//		}
		}

		Player p = new Player(0000, "Stefan");
		p.setCharacterLocation(19,27);
		Location pL = p.getCharacterLocation();
		floor.floorMap.floorTiles[pL.row()][pL.column()].setOccupied();

		int playerLoop = 0;

		while (playerLoop < 100) {
		String s = "";
		for (int h = 0; h<floor.floorMap.FLOOR_HEIGHT; h++) {
			for (int w = 0; w<floor.floorMap.FLOOR_WIDTH; w++) {
					s = s + (floor.floorMap.floorTiles[w][h].name());
			}
			s = s + "\n";
		}

		System.out.println(s);
		playerLoop++;

		try {Thread.sleep(700);	}
		catch (InterruptedException e) {e.printStackTrace();}

		floor.floorMap.floorTiles[pL.row()][pL.column()].setUnoccupied();
		pL.modX(-1);
		floor.floorMap.floorTiles[pL.row()][pL.column()].setOccupied();

		}




	}
}
