package Tests;

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
import items.Door;
import model.Game;

public class PlayerInteractWithItem {

	public static void main(String[] args) throws IOException {

		Game game = new Game();
		// create room
		Room room = makeRoom();
		TileMap tileMap = room.getRoomTileMap();
		// add items to room
		tileMap.populateRoom(room, tileMap.getItems(), null);
		//create player
		Player p = new Player(0000, "H", game);
		p.setCharacterLocation(5,1);
		Location pL = p.getCharacterLocation();
		// add player to room
		System.out.println(p.getName());
		EmptyTile tile =  (EmptyTile) tileMap.getTileMap()[pL.row()][pL.column()];
		tile.addObjectToTile(p);
		// draw board
        drawRoom(tileMap);


	}



	private static Room makeRoom() throws IOException {
		// TODO Auto-generated method stub
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
		return r;
	}

  private static void drawRoom(TileMap tileMap) {
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
