package items;

import java.util.List;

import gui.Drawable;
import gui.Polygon;

public  class Item extends GameObject implements Drawable {

	List<String> options; // items that are interactable may have a list of options to choose from
	// items should also have a GameWorld Position
	public int itemID;


	public Item( int itemID, String itemType ) {
		super(itemID, itemType);




	}

	public String toString() {
		return "I";
	}
	// Location location;

	@Override
	public void setRotAdd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDirection(double toX, double toY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePoly() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCube() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsPoint(int x, int y, int z) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Polygon> getPolygons() {
		// TODO Auto-generated method stub
		return null;
	}



	// Model file

}
