package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import characters.Guard;
import characters.Player;
import game.floor.EmptyTile;
import game.floor.Floor;
import game.floor.FloorMap;
import game.floor.Room;
import game.floor.TileMap;
import items.Door;
import model.Game;

public class CheckGuardMovementTest {

	public static void main(String[] args) throws IOException {

		Game game = new Game();
		Floor floor = createFloor();
		game.addFloor(floor);
		int[] dist = {6,6};
		Guard gaurd1 = new Guard(0, "guard1", 1, dist, 0);
		int[] dist1 = {6,3};
		Guard guard2 = new Guard(1, "guard2",5,dist1, 0);
		int[] dist2 = {6,1,6,1};
		Guard guard3 = new Guard(1, "guard3",13,dist2, 0);
		// set gaurd's location
		gaurd1.setCharacterLocation(14, 0);
		guard2.setCharacterLocation(49, 0);
		guard3.setCharacterLocation(14, 7);

		 ((EmptyTile) game.getFloor(0).getFloorMap().getFloorTiles()[14][0]).addObjectToTile(gaurd1);
		 ((EmptyTile)
		 game.getFloor(0).getFloorMap().getFloorTiles()[49][0]).addObjectToTile(guard2);
		 ((EmptyTile)
				 game.getFloor(0).getFloorMap().getFloorTiles()[14][7]).addObjectToTile(guard3);

		 game.drawBoard(0);
         Thread guardThread1 = game.createGuardThread(gaurd1,0);
         Thread drawThread = game.drawFloorThread(700);
		 Thread guardThread2 = game.createGuardThread(guard2, 0);
		 Thread guardThread3 = game.createGuardThread(guard3, 0);

		// start the guard movement, thread stops running when intruder caught
         game.display.setVisible(true);
		 guardThread2.start();
		

        guardThread1.start();
        guardThread3.start();
		drawThread.start();

	}




	private static Floor createFloor() throws IOException {
		// TODO Auto-generated method stub
		List<Room> floorRooms = new ArrayList<Room>();

		int nextX = 0;
		int nextY = 0;
		final int ADJACENT = 1; // adjacent rooms, add extra wall

		Door d = new Door(0000, "0001", 0);
		gui.Room r = new Room(null, d);

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

}
