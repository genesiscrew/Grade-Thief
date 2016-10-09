package items;

import gui.*;
import gui.Polygon;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * this class represents a map item,
 * @author abubakhami
 *
 */
public class Map extends Item implements Interactable, Movable {
	Image mapIMG;

	public Map(int itemID, Image mapIMG, String itemType, double x, double y, double z, double width, double length, double height, Color c) {
		super(itemID, itemType, x, y, z, width, length, height, c);
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

	@Override
	public void updateDirection(double toX, double toY) {

	}

	@Override
	public void updatePoly() {

	}

	@Override
	public void setRotAdd() {

	}

	@Override
	public void removeCube() {

	}

	@Override
	public boolean containsPoint(int x, int y, int z) {
		return false;
	}

	@Override
	public List<Polygon> getPolygons() {
		return null;
	}

	
}
