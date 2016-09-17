package game.floor;

import java.util.List;

public class Floor {
	List<Room> rooms;
	List<Camera> cameras;
	 FloorMap floorMap;
	// rooms should probably be ordered in some arrangement.
	public Floor(List<Room> rooms, List<Camera> cameras, FloorMap floorMap) {
		this.rooms = rooms;
		this.cameras = cameras;
		this.floorMap = floorMap;
	}

	public void addRoom(Room r, int sx, int sy, int w, int h) {
		// here we need to add the room on the floormap at sx, sy, spanning across and up w,h
		// only walls and doors are added to the floor. Nothing inside the room is relevant.

//		if (boundingBox.length != 4)
//				return;

		int startX = sx; int startY = sy;
		System.out.println("room " + r.toString() + " sx " + sx + " sy " + sy + " w " + w + " h " + h);

		while (startY <= sy + h) {
			while (startX <= sx + w) {
				//  - Add wall on edges
				if (startY == sy || startX == sx) {
					WallTile wallTile = new WallTile();
					wallTile.setLocation(new Location(startX, startY));
					floorMap.floorTiles[startX][startY] = wallTile;
					System.out.println(wallTile.location);
				}

				else if (startY == sy + h || startX == sx + w) {
					WallTile wallTile= new WallTile();
					wallTile.setLocation(new Location(startX, startY));
					floorMap.floorTiles[startX][startY] = wallTile;
					System.out.println(wallTile.location);

				}
				else {
				RoomTile roomTile = new RoomTile();
					roomTile.setLocation(new Location(startX, startY));
					floorMap.floorTiles[startX][startY] = roomTile;


					System.out.println(roomTile);
				}

					startX++; // increase every iteration


					//TODO : add doors in

			}
			startX = sx; // resets x counter
			startY++; // next column
		}

System.out.println("Done");




}

}