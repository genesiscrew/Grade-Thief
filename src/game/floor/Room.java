package game.floor;

import java.io.File;

import items.Door;

public class Room {
	// a room should be laid out in a grid
	Tile[][] roomTileMap; // this contains the arrangement of the room
	Door door;

	public Room(Tile[][] roomTileMap, Door door) {
		this.roomTileMap = roomTileMap;
		this.door = door;
	}

	public void setTileMap(String f) {
			TileMap t = new TileMap(null);
			this.roomTileMap = t.createTileMap(f);
	}


	public int roomGetCode() {
		return this.door.code;
	}

}
