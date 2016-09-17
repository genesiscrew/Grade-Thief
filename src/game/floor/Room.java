package game.floor;

import java.io.File;

import items.Door;

public class Room {
	// a room should be laid out in a grid
	//Tile[][] roomTileMap; // this contains the arrangement of the room
	TileMap roomTileMap;
	Door door;
	int sx = -1;
	int sy = -1;
	int w = -1;
	int h = -1;

	public Room(TileMap roomTileMap, Door door) {
		this.roomTileMap = roomTileMap;
		this.door = door;
	}

	public void setTileMap(String f) {
			TileMap t = new TileMap(null);
			this.roomTileMap = t.createTileMap(f);
	}
	/**
	 * this sets the dimensions of the room RELATIVE to the floor
	 * for malleability; the room will know it's global position on the floor and the floor will also know the bounding
	 * box of the room.
	 * @param sx s -- start
	 * @param sy
	 * @param w
	 * @param h
	 */
	public void setBoundingBox(int sx, int sy, int w, int h) {
		this.sx = sx;
		this.sy = sy;
		this.w = w;
		this.h = h;
	}

	public int[] getBoundingBox() {
		return new int[] { sx, sy, w, h };
	}

	public int roomGetCode() {
		return this.door.code;
	}

}
