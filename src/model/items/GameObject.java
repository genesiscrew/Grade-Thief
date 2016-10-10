package model.items;

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

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public Location getObjectLocation() {
		return objectLocation;
	}

	public void setObjectLocation(Location objectLocation) {
		this.objectLocation = objectLocation;
	}



}
