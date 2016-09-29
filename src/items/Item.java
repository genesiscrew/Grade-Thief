package items;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import gui.Drawable;
import gui.ThreeDPolygon;

@XmlRootElement(name = "Item")
@XmlAccessorType (XmlAccessType.FIELD)
public  class Item extends GameObject implements Drawable {


	List<String> options; // items that are interactable may have a list of options to choose from
	// items should also have a GameWorld Position
	@XmlElement
	public int itemID;


	public Item( int itemID, String itemType ) {
		super(itemID, itemType);




	}


	// Location location;

	@Override
	public String toString() {
		return "Item [options=" + options + ", itemID=" + itemID + ", itemType=" + itemType + ", objectLocation="
				+ objectLocation + ", getPolygons()=" + getPolygons() + ", itemType()=" + itemType()
				+ ", getGameObjectLocation()=" + getGameObjectLocation() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}


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
	public List<ThreeDPolygon> getPolygons() {
		// TODO Auto-generated method stub
		return null;
	}



	// Model file

}
