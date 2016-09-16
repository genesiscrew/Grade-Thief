package items;

public abstract class GameObject {

	public String itemType;
	public int itemID;

	public GameObject(int itemID) {
	//	this.itemType = itemType;
		this.itemID = itemID;
	}

	public String itemType() {
		return this.itemType;

	}

}
