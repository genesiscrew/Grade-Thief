package game.floor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import items.Door;
import model.Game;

public class movePlayerinRoom {

	public static void main(String[] args) throws IOException {
		// create new game object
		Game game;
		try {
			game = new Game();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// create a room
		Room testRoom = createRoom();
		
		// get the tilemap of room
		TileMap tileMap = testRoom.getRoomTileMap();
		// draw tilemap of room
		drawRoom(tileMap);
		
		
		


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
