package game.floor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gui.Drawable;
import items.Door;
/**
*
* @author Stefan Vrecic
*
*/
public class Room {

	// a room should be laid out in a grid
	//Tile[][] roomTileMap; // this contains the arrangement of the room
	private TileMap roomTileMap;
	private Door door;
	private int sx = -1;
	private int sy = -1;
	private int w = -1;
	private int h = -1;
	List<Location> doorLocations = new ArrayList<Location>();
	private List<Drawable> drawableItems = new ArrayList<Drawable>();


	public Room(TileMap roomTileMap, Door door) {
		this.roomTileMap = roomTileMap;
		this.door = door;
	}

	public void setTileMap(String f)  throws IOException {
		//TileMap t = new TileMap(null, this);     
		//this.tileMap = t.createTileMap(f);
	}
	/**

	 * @param sx -- start x,y
	 * @param sy
	 * @param w -- width, height
	 * @param h
	 *  this sets the dimensions of the room RELATIVE to the floor
	 * for malleability; the room will know it's global position on the floor and the floor will also know the bounding
	 * box of the room.
	 */
	public void setBoundingBox(int sx, int sy, int w, int h) {
		this.sx = sx;
		this.sy = sy;
		this.w = w;
		this.h = h;
	}

	public List<Location> getDoorLocations() {
		return this.doorLocations;
	}

	public void addDoorLocation(Location loc) {
		this.doorLocations.add(loc);
	}

	public int[] getBoundingBox() {
		return new int[] { sx, sy, w, h };
	}

	public int roomGetCode() {
		return this.door.code;
	}

	public TileMap getRoomTileMap() {
		return roomTileMap;
	}

	public Door getDoor() {
		return door;
	}

	public int getSx() {
		return sx;
	}

	public int getSy() {
		return sy;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}

	public List<Drawable> getDrawableItems() {
		return drawableItems;
	}

	public void addDrawableItems(Drawable drawableItems) {
		this.drawableItems.add(drawableItems);
	}


}