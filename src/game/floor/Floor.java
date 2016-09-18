package game.floor;

import java.util.List;

public class Floor {
	List<Room> rooms;
	List<Camera> cameras;
	// rooms should probably be ordered in some arrangement.
	public Floor(List<Room> rooms, List<Camera> cameras) {
		this.rooms = rooms;
		this.cameras = cameras;
	}


}
