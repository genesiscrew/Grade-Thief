package items;

import java.awt.Image;

public class Map extends Item implements Interactable, Movable {
	Image mapIMG;

	public Map(int itemID, Image mapIMG) {
		super(itemID);
		this.mapIMG = mapIMG;
	}

	@Override
	public void pickUp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void useItem() {
		// this will display the map

	}

	@Override
	public void useItem(GameObject j) {
		useItem();
	}

	@Override
	public void move(Direction dir, Distance d) {
		// TODO Auto-generated method stub

	}

}
