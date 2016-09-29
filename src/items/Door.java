package items;

import game.floor.Room;

public class Door extends GameObject {
	public int code;
	public int keyID;
	public String itemType = "Door";
	public int itemID = 1111;
	private gui.Room room;

	public Door(int itemID, String itemType, int keyID, gui.Room room) { //, int code, int keyID) {
		super(itemID, itemType);
		this.itemType = itemType;
		this.itemID = itemID;
		this.code = code;
		this.keyID = keyID;
		this.room = room;
	}

	public String itemType() {
		return itemType;
	}

	public static Door getDoor(int doorCode) {
		// read map for door Code
		// return door
		return null;
	}

	public gui.Room getRoom() {
		return this.room;
	}

}