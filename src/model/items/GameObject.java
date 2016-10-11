package model.items;

import model.floor.Location;
import model.floor.TileMap;

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

	/**
	 * returns the location of the gfame object
	 * @return
	 */
	public Location getGameObjectLocation() {

		return objectLocation;
	}

	public void setGameObjectLocation(int x, int y) {
		objectLocation = new Location(x, y);

	}
/**
 * gets the item type -- redundant since 3D
 * @return
 */
	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	/**
	 * gets the item ID of the object -- useful for testing
	 * @return
	 */
	public int getItemID() {
		return itemID;
	}

	/**
	 * sets the item Id of the object
	 * @param itemID
	 */
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	/**
	 * gets the location of the object
	 * @return
	 */
	public Location getObjectLocation() {
		return objectLocation;
	}

	/**
	 * sets the location of the object
	 * @param objectLocation
	 */
	public void setObjectLocation(Location objectLocation) {
		this.objectLocation = objectLocation;
	}



}
