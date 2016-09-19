package items;

import java.awt.Image;
/**
 * this class represents a map item,
 * @author abubakhami
 *
 */
public class Map extends Item implements Interactable, Movable {
	Image mapIMG;

	public Map(int itemID, Image mapIMG, String itemType) {
		super(itemID, itemType);
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
