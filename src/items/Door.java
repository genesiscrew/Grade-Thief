package items;

public class Door extends GameObject {
	public int code;
	public int keyID;
	public String itemType = "Door";
	public int itemID = 1111;

	public Door(int itemID, int keyID) { //, int code, int keyID) {
		super(itemID);
		this.itemType = itemType;
		this.itemID = itemID;
		this.code = code;
		this.keyID = keyID;
	}

	public String itemType() {
		return itemType;
	}

	public static Door getDoor(int doorCode) {
		// read map for door Code
		// return door
		return null;
	}

}
