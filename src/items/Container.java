package items;

import java.util.List;

public class Container extends Item implements Interactable, Movable{

	List<GameObject> containedItems;
	int keyID;
	public Container(int itemID, List<GameObject> containedItems, String itemType, int keyID) {
		super(itemID, itemType);
		this.containedItems = containedItems;
        this.keyID = keyID;
	}

	public void setItems(List<GameObject> containedItems) {
		this.containedItems = containedItems;
	}

	public List<GameObject> getItems() {
		for (GameObject g : containedItems) {
			System.out.println(g.itemID);
		}
		return containedItems;
	}

	@Override
	public void pickUp() {


	}

	@Override
	public void useItem(GameObject j) {
		// TODO Auto-generated method stub

	}

	@Override
	public void useItem() {


	}

	@Override
	public void move(Direction dir, Distance d) {
		// TODO Auto-generated method stub

	}


}
