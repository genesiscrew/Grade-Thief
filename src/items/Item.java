package items;

import java.util.List;

public  class Item extends GameObject {

	List<String> options; // items that are interactable may have a list of options to choose from
	// items should also have a GameWorld Position
	public int itemID;

<<<<<<< HEAD
	public Item( int itemID, String itemType) {
		super(itemID, itemType);

	}

=======

	public Item( int itemID, String itemType ) {
		super(itemID, itemType);

>>>>>>> c205558933e0cfa0a631cbd8a4d66be5234bba3d


	}

	public String toString() {
		return "I";
	}
	// Location location;



	// Model file

}
