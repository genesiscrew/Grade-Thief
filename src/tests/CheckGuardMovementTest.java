package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import characters.GuardBot;
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
		gui.Room room = createRoom();
		game.addFloor(room);
		int[] dist = {6,6};
//		GuardBot gaurd1 = new GuardBot(0, "guard1", 1, dist, 0);
//		int[] dist1 = {6,3};
//		GuardBot guard2 = new GuardBot(1, "guard2",5,dist1, 0);
//		int[] dist2 = {6,1,6,1};
//		GuardBot guard3 = new GuardBot(1, "guard3",13,dist2, 0);
		// set gaurd's location
//		gaurd1.setCharacterLocation(14, 0);
//		guard2.setCharacterLocation(49, 0);
//		guard3.setCharacterLocation(14, 7);
//
//		 ((EmptyTile) game.getRoom(0).getTileMap().getTileMap()[14][0]).addObjectToTile(gaurd1);
//		 ((EmptyTile)
//		 game.getRoom(0).getTileMap().getTileMap()[49][0]).addObjectToTile(guard2);
//		 ((EmptyTile)
//				 game.getRoom(0).getTileMap().getTileMap()[14][7]).addObjectToTile(guard3);
//
//		 game.drawBoard(0);
//         Thread guardThread1 = game.createGuardThread(gaurd1,0);
//         Thread drawThread = game.drawFloorThread(700);
//		 Thread guardThread2 = game.createGuardThread(guard2, 0);
//		 Thread guardThread3 = game.createGuardThread(guard3, 0);

//		// start the guard movement, thread stops running when intruder caught
//         game.display.setVisible(true);
//		 guardThread2.start();
//
//
//        guardThread1.start();
//        guardThread3.start();
//		drawThread.start();

	}




	static private gui.Room createRoom() throws IOException {
		// TODO Auto-generated method stub
		List<gui.Room> floorRooms = new ArrayList<gui.Room>();
        Game game = new Game();
		int nextX = 0;
		int nextY = 0;
		final int ADJACENT = 1; // adjacent rooms, add extra wall

		//Door d = new Door(0000, "0001",0, null);

		String co237 = System.getProperty("user.dir") + "/src/game/floor/level";



		gui.Room room_co237 = new gui.Room("level", 0, 0);

		//Door door_co237 = new Door(0000, "237",0,room_co237);

		floorRooms.add(room_co237);


		TileMap tileMap = room_co237.getTileMap();


		int mapWidth = tileMap.getMapWidth();
		int mapHeight = tileMap.getMapHeight();
String s = "";

		for (int y = 0; y < mapHeight; y++) {
			for (int x = 0; x < mapWidth; x++) {
				s = s + tileMap.getTileMap()[x][y].getName();
			}
			s = s + "\n";
		}
System.out.println(s);

	return room_co237;
		}


}
