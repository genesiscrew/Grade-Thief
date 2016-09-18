package items;

import java.util.List;

public  class Item extends GameObject {

	List<String> options; // items that are interactable may have a list of options to choose from
	// items should also have a GameWorld Position
	public int itemID;

	public Item( int itemID, String itemType ) {
		super(itemID, itemType);


	}


	// Location location;



	// Model file ?






}
