package game.floor;

import java.util.List;
/**
 *
 * @author Stefan Vrecic
 * Floor.java arranges the floor by rooms and cameras.
 * Contains a floorMap which is Tile[][] 2d arrangement of the floor.
 *
 */
public class Floor {
	private List<gui.Room> rooms;
	private List<Camera> cameras;
	private FloorMap floorMap;

	public Floor(List<gui.Room> rooms, List<Camera> cameras, FloorMap floorMap) {
		this.rooms = rooms;
		this.cameras = cameras;
		this.floorMap = floorMap;
	}
/**
 *
 * @param list -- <Location>
 * @param l -- a location which is to be referenced with the list, to see if the list contains l
 * @return -- true if list contains location, false if not;
 *  	   -- NO INDEX RETURNED
 *
 *  Method checks to see if list contains the object, using the defined equals() method
 *
 */
	public boolean containsObject(List<Location> list, Location l) {
		for (Location loc : list) {
			if (loc.equals(l))
				return true;
		}
		return false;

	}

	/**
	 *
	 * @param r -- the Room to add
	 * @param sx -- starting position on the floor
	 * @param sy
	 * @param w -- width, height of the room
	 * @param h
	 * @param doorLocations -- locations relative TO THE ROOM which are doors
	 *
	 *  Method will add room at a given position on the floor and include doors at doorLocations.
	 */


	public FloorMap getFloorMap() {
		return this.floorMap;
	}
	public void addRoom(gui.Room r, int sx, int sy, int w, int h, List<Location> doorLocations) {
		// here we need to add the room on the floormap at sx, sy, spanning across and up w,h
		// only walls and doors are added to the floor. Nothing inside the room is relevant.


		int startX = sx; int startY = sy;

		while (startY < sy + h) {
			while (startX < sx + w) {
				Location globalLoc = new Location(startX, startY); // this is global position of tiles
				Location localLoc = new Location(startX-sx, startY-sy); // this is the local position of tiles in the ROOM


				if (containsObject(doorLocations, localLoc)) { // doorLocations represents doors LOCAL to the room, hence
					// the Location local
					DoorTile doorTile = new DoorTile();
					doorTile.setLocation(globalLoc);
					//doorTile.setLocation(new Location(startX, startY)); //
					floorMap.getFloorTiles()[startX][startY] = doorTile;
				}

				else if (startY == sy || startX == sx) { // the first tiles  of the room should always be walls
					WallTile wallTile = new WallTile();
					wallTile.setLocation(globalLoc);
					floorMap.getFloorTiles()[startX][startY] = wallTile;
				}

				else if (startY == sy + h -1 || startX == sx + w - 1) { // likewise the last tile should be walls
					WallTile wallTile= new WallTile();
					wallTile.setLocation(globalLoc);
					floorMap.getFloorTiles()[startX][startY] = wallTile;
				}

				else { // every tile WITHIN should be a RoomTile
					RoomTile roomTile = new RoomTile();
					roomTile.setLocation(globalLoc);
					floorMap.getFloorTiles()[startX][startY] = roomTile;
				}

				startX++; // increase every iteration

			}
			startX = sx; // resets x counter
			startY++; // next column
		}
		System.out.println("Done");
	}

	/**
	 * example of a floormap graphical representation with rooms included. This is only a portion of the standard floor --
	 * expected to be a 100 * 100 tile grid.
	 *
	WWWWWWWWWWWWWWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWEEEEEEEEEEEEEEEEEEEEEEEEEE
	WRRRRRRRRRRRRWWWWWWWWWWWWWWWWWWWWWWEEEEE
	WRRRRRRRRRRRRWWRRRRRRRRRRRRRRRRRRRWEEEEE
	WRRRRRRRRRRRRWWRRRRRRRRRRRRRRRRRRRWEEEEE
	WRRRRRRRRRRRRWWRRRRRRRRRRRRRRRRRRRWEEEEE
	WRRRRRRRRRRRRWWRRRRRRRRRRRRRRRRRRRWEEEEE
	WRRRRRRRRRRRRWWRRRRRRRRRRRRRRRRRRRWEEEEE
	WRRRRRRRRRRRRWWRRRRRRRRRRRRRRRRRRRWEEEEE
	WRRRRRRRRRRRRWWRRRRRRRRRRRRRRRRRRRWEEEEE
	WRRRRRRRRRRRRWWRRRRRRRRRRRRRRRRRRRWEEEEE
	WRRRRRRRRRRRRWWRRRRRRRRRRRRRRRRRRRWEEEEE
	WRRRRRRRRRRRRWWRRRRRRRRRRRRRRRRRRRWEEEEE
	WDDWWWWWWWWWWWWWWWWWWWWWWWWWWWWDDWWEEEEE
	EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
	 */


}