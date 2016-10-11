package model.items;

import java.awt.Color;
import java.awt.Image;
import java.util.List;

import model.rendering.Polygon;

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

	@Override // see item.java
	public void pickUp() {
		// TODO Auto-generated method stub

	}

	@Override // see item.java
	public void useItem() {
		// this will display the map

	}

	@Override // see item.java
	public void useItem(GameObject j) {
		useItem();
	}

	@Override // see item.java
	public void move(Direction dir, Distance d) {
		// TODO Auto-generated method stub

	}

	@Override // see item.java
	public void updateDirection(double toX, double toY) {

	}

	@Override // see item.java
	public void updatePoly() {

	}

	@Override // see item.java
	public void setRotAdd() {

	}

	@Override // see item.java
	public void removeCube() {

	}

	@Override // see item.java
	public boolean containsPoint(int x, int y, int z) {
		return false;
	}

	@Override // see item.java
	public List<Polygon> getPolygons() {
		return null;
	}


}
