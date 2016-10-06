package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.SliderUI;

import characters.Player;
import game.floor.Floor;
import game.floor.FloorMap;
import game.floor.Location;
import game.floor.Room;
import game.floor.TileMap;
import items.Door;
import model.Game;

/**
*
* @author Stefan Vrecic
*
*/
public class MakeFloorTest {

	public static void main(String[] args) throws IOException {


		// make a floor
		/// load the rooms and add it.
		// get file path
		// make a new room
		// put the file path into room.setTileMap(filePath)
		// this will delegate the work to TileMap.java

		List<gui.Room> floorRooms = new ArrayList<gui.Room>();
        Game game = new Game();
		int nextX = 0;
		int nextY = 0;
		final int ADJACENT = 1; // adjacent rooms, add extra wall

		//Door d = new Door(0000, "0001",0, null);

		String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";
		String co238 = System.getProperty("user.dir") + "/src/game/floor/co238";
		String co243 = System.getProperty("user.dir") + "/src/game/floor/co243";



		gui.Room room_co237 = new gui.Room("co237",0,0);
		gui.Room room_co238 = new gui.Room("co238",0,0);
		gui.Room room_co243 = new gui.Room("co243",0,0);

//		Door door_co237 = new Door(0000, "237",0,room_co237);
//		Door door_co238 = new Door(0000, "238",0,room_co238);
//		Door door_co243 = new Door(0000, "243",0,room_co243);

		floorRooms.add(room_co237);
		floorRooms.add(room_co238);
		floorRooms.add(room_co243);

		FloorMap floorMap = new FloorMap(null); // creates empty floor map
		floorMap.setFloorMap(floorMap.createFloorMap()); // creates a 2d array or tiles and adds it to FloorMap

		Floor floor = new Floor(floorRooms, null, floorMap);
		TileMap tileMap = room_co237.getTileMap();


		int mapWidth = tileMap.getMapWidth();
		int mapHeight = tileMap.getMapHeight();

		System.out.println("bounding co238 " + mapWidth + " ht " + mapHeight);
		room_co238.setBoundingBox(nextX, nextY, mapWidth, mapHeight);

		nextX += mapWidth; // + ADJACENT;
		System.out.println("nextX " + nextX + " width" + mapWidth);
	//	nextY += 11;

		room_co237.setBoundingBox(nextX, nextY, mapWidth, mapHeight);

		nextY += mapHeight + 5; // corridor 5 tiles
		room_co243.setBoundingBox(0, nextY,mapWidth, mapHeight);

		for (gui.Room room : floorRooms) {

			int[] bounds = room.getBoundingBox();
			floor.addRoom(room, bounds[0], bounds[1], bounds[2], bounds[3], tileMap.getDoors());

			//			for (int sy = room.sy; sy <= sy+room.h; sy++) {
			//			for (int sx = room.sx; sx <= sx+room.w; sx++) {
			//
			//			}
			//		}
		}


		Player p = new Player(0000, "Stefan", null, 0);
		p.setCharacterLocation(19,27);
		Location pL = p.getCharacterLocation();
		floor.getFloorMap().getFloorTiles()[pL.row()][pL.column()].setOccupied();

		int playerLoop = 0;

		String s = "";
		//while (playerLoop < 100) {
		for (int h = 0; h<floor.getFloorMap().FLOOR_HEIGHT; h++) {
			for (int w = 0; w<floor.getFloorMap().FLOOR_WIDTH; w++) {
					s = s + (floor.getFloorMap().getFloorTiles()[w][h].name());
			}
			s = s + "\n";
		}

		System.out.println("convert here .. convert s");

		System.out.println(s);

		try {Thread.sleep(700);	}
		catch (InterruptedException e) {e.printStackTrace();}

		floor.getFloorMap().getFloorTiles()[pL.row()][pL.column()].setUnoccupied();
		pL.modX(-1);
		floor.getFloorMap().getFloorTiles()[pL.row()][pL.column()].setOccupied();

		}




	}