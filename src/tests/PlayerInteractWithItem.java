package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import characters.Player;
import game.floor.EmptyTile;
import game.floor.Floor;
import game.floor.Location;
import game.floor.Room;
import game.floor.RoomTile;
import game.floor.Tile;
import game.floor.TileMap;
import items.Direction;
import items.Door;
import model.Game;

public class PlayerInteractWithItem {

	public static void main(String[] args) throws IOException {

		Game game = new Game();
		// create room
		// TODO
		gui.Room room = null;
		TileMap tileMap = room.getTileMap();
		// add items to room
		tileMap.populateRoom(room, tileMap.getItems(), null);
		//create player
		Player p = new Player(0000, "H", game, 0);
		game.addPlayer(p);
		p.setCharacterLocation(5,2);
		Location pL = p.getCharacterLocation();
		// set user direction facing item
		p.setDirection(Direction.Dir.EAST);
		// add player to room
		EmptyTile tile =  (EmptyTile) tileMap.getTileMap()[pL.row()][pL.column()];
		tile.addObjectToTile(p);
		// draw board
        game.drawRoom(tileMap);
        Thread drawThread = game.drawRoomThread(700, tileMap);
        game.display.setVisible(true);
        drawThread.start();


	}



	static private gui.Room createRoom() throws IOException {
		// TODO Auto-generated method stub
		List<gui.Room> floorRooms = new ArrayList<gui.Room>();
        Game game = new Game();
		int nextX = 0;
		int nextY = 0;
		final int ADJACENT = 1; // adjacent rooms, add extra wall

		Door d = new Door(0000, "0001",0, null);

		String co237 = System.getProperty("user.dir") + "/src/game/floor/level";



		gui.Room room_co237 = new gui.Room("level", 0 ,0 );

		Door door_co237 = new Door(0000, "237",0,room_co237);

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
