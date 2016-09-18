package items;

import game.floor.Location;
import game.floor.TileMap;

public abstract class GameObject {

	public String itemType;
	public int itemID;

	public Location objectLocation;


	public GameObject(int itemID, String itemType) {
	    this.itemType = itemType;
		this.itemID = itemID;
	}

	public String itemType() {
		return this.itemType;

	}

	public Location getGameObjectLocation() {

		return objectLocation;
	}

	public void setGameObjectLocation(int x, int y) {
		objectLocation = new Location(x, y);

	}



}
