package tests;

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

public class MovePlayerinFloor {

	public static void main(String[] args) throws IOException {
		// create new game object
		Game game = null;
		try {
			game = new Game();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Player p = new Player(0000, "Stefan", game, 0);
		p.setCharacterLocation(19,27);
		Location pL = p.getCharacterLocation();
		gui.Room room = createRoom();
		EmptyTile tile =  (EmptyTile) room.getTileMap().getTileMap()[pL.row()][pL.column()];
		tile.addObjectToTile(p);







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



		gui.Room room_co237 = new gui.Room("level");

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


//	public static Room createRoom() throws IOException{
//		Floor floor;
//		List<Room> floorRooms = new ArrayList<Room>();
//
//		int nextX = 0;
//		int nextY = 0;
//		final int ADJACENT = 1; // adjacent rooms, add extra wall
//
//		Door d = new Door(0000, "0001",0);
//		Room r = new Room(null, d);
//
//		String co237 = System.getProperty("user.dir") + "/src/game/floor/co237";
//
//		Room room_co237 = new Room(null, null);
//
//		room_co237.setTileMap(co237);
//
//		r.setTileMap(co237);
//
//		//		System.out.println("optional code " + r.roomTileMap.getOptionalCode());
//		//		System.out.println("height " + r.roomTileMap.getMapHeight());
//		//		System.out.println("width " + r.roomTileMap.getMapWidth());
//
//		TileMap tileMap = r.getRoomTileMap();
//		String s = "";
//		for (int y = 0 ; y < tileMap.getMapHeight(); y++ ) {
//			for (int x = 0 ; x < tileMap.getMapWidth() ; x++) {
//				Tile t = tileMap.getTileMap()[x][y];
//				if (t != null)
//					s = s + (tileMap.getTileMap()[x][y].getName());
//			}
//			s = s + "\n";
//		}
//
//		System.out.println(s);
//		System.out.println(r.getRoomTileMap().getItems());
//
//		tileMap.populateRoom(r, tileMap.getItems(), null);
//
//		return r;
//
//	}
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
