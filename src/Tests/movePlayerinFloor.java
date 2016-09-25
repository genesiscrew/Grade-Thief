package Tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import characters.Player;
import game.floor.EmptyTile;
import game.floor.Floor;
import game.floor.FloorMap;
import game.floor.Location;
import game.floor.Room;
import game.floor.Tile;
import game.floor.TileMap;
import items.Door;
import model.Game;

public class movePlayerinFloor {

	public static void main(String[] args) throws IOException {
		// create new game object
		Game game = null;
		try {
			game = new Game();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Player p = new Player(0000, "Stefan", game);
		p.setCharacterLocation(19,27);
		Location pL = p.getCharacterLocation();
		Floor floor = createFloor();
		EmptyTile tile =  (EmptyTile) floor.getFloorMap().getFloorTiles()[pL.row()][pL.column()];
		tile.addObjectToTile(p);
		






	}

	private static Floor createFloor() throws IOException {
		List<Room> floorRooms = new ArrayList<Room>();

		int nextX = 0;
		int nextY = 0;
		final int ADJACENT = 1; // adjacent rooms, add extra wall

		Door d = new Door(0000, "0001", 0);
		Room r = new Room(null, d);

		String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";
		String co238 = System.getProperty("user.dir") + "/src/game/floor/co238";
		String co243 = System.getProperty("user.dir") + "/src/game/floor/co243";

		Door door_co237 = new Door(0000, "237", 0);
		Door door_co238 = new Door(0000, "238", 0);
		Door door_co243 = new Door(0000, "243", 0);

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
		floorMap.setFloorMap(floorMap.createFloorMap()); // creates a 2d array
															// or tiles and adds
															// it to FloorMap

		Floor floor = new Floor(floorRooms, null, floorMap);

		TileMap tileMap = room_co238.getRoomTileMap();
		int mapWidth = tileMap.getMapWidth();
		int mapHeight = tileMap.getMapHeight();

		System.out.println("bounding co238 " + mapWidth + " ht " + mapHeight);
		room_co238.setBoundingBox(nextX, nextY, mapWidth, mapHeight);

		nextX += mapWidth; // + ADJACENT;
		System.out.println("nextX " + nextX + " width" + mapWidth);
		nextY += 11;

		tileMap = room_co237.getRoomTileMap();

		mapWidth = tileMap.getMapWidth();
		mapHeight = tileMap.getMapHeight();
		room_co237.setBoundingBox(nextX, nextY, mapWidth, mapHeight);

		nextY += mapHeight + 5; // corridor 5 tiles

		mapWidth = room_co243.getRoomTileMap().getMapWidth();
		mapHeight = room_co243.getRoomTileMap().getMapHeight();

		room_co243.setBoundingBox(0, nextY, mapWidth, mapHeight);

		for (Room room : floorRooms) {

			int[] bounds = room.getBoundingBox();
			// System.out.println(room.getBoundingBox().toString());
			floor.addRoom(room, bounds[0], bounds[1], bounds[2], bounds[3], tileMap.getDoors());

			// for (int sy = room.sy; sy <= sy+room.h; sy++) {
			// for (int sx = room.sx; sx <= sx+room.w; sx++) {
			//
			// }
			// }
		}
		return floor;
	}


	public static Room createRoom() throws IOException{
		Floor floor;
		List<Room> floorRooms = new ArrayList<Room>();

		int nextX = 0;
		int nextY = 0;
		final int ADJACENT = 1; // adjacent rooms, add extra wall

		Door d = new Door(0000, "0001",0);
		Room r = new Room(null, d);

		String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";

		Room room_co237 = new Room(null, null);

		room_co237.setTileMap(co237);

		r.setTileMap(co237);

		//		System.out.println("optional code " + r.roomTileMap.getOptionalCode());
		//		System.out.println("height " + r.roomTileMap.getMapHeight());
		//		System.out.println("width " + r.roomTileMap.getMapWidth());

		TileMap tileMap = r.getRoomTileMap();
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
		System.out.println(r.getRoomTileMap().getItems());

		tileMap.populateRoom(r, tileMap.getItems(), null);

		return r;

	}
	/**
	 * draws the room tilemap into console
	 * @param tileMap
	 */
	public static void drawRoom(TileMap tileMap){
		String s = "";
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
