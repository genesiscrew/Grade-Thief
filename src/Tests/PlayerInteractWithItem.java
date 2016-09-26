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
import items.Direction;
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
		p.setCharacterLocation(5,2);
		Location pL = p.getCharacterLocation();
		// set user direction facing item
		p.setDirection(Direction.Dir.EAST);
		// add player to room
		System.out.println(p.getName());
		EmptyTile tile =  (EmptyTile) tileMap.getTileMap()[pL.row()][pL.column()];
		tile.addObjectToTile(p);
		// draw board
        game.drawRoom(tileMap);
        Thread drawThread = game.drawGameThread(700);
        game.display.setVisible(true);
        drawThread.start();


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




}
