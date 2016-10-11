package model.floor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import model.items.Door;
import model.items.Item;
import model.rendering.Cube;
import model.rendering.Drawable;
import model.rendering.Polygon;

/**
 * this class renders a door object into the game world
 *
 * @author abubakhami
 *
 */
public class DoorDraw extends Item {

	private java.util.List<Cube> cubes;
	private double x;
	private double y;
	private double z;
	private double width;
	private double length;
	private double height;
	private Color color;
	private Door door;

	public String toString() {
		return x + " " + y + " " + z + " " + width + " " + length + " " + height + " " + color + " door " + door;
	}

	public DoorDraw(int itemID, String itemType, double x, double y, double z, double width, double length,
			double height, Color c, Door door) {
		super(itemID, itemType, x, y, z, width, length, height, c);

		System.out.println("height " + height);
		System.out.println("height " + height);
		System.out.println("height " + height);
		System.out.println("height " + height);

		cubes = new ArrayList<>();
		cubes.add(new Cube(x, y, z, width, length, height, c));

		this.color = c;
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.length = length;
		this.height = height;
		this.door = door;
	}

	@Override // see item.java
	public void setRotAdd() {
		cubes.forEach(i -> i.setRotAdd());
	}

	@Override // see item.java
	public void updateDirection(double toX, double toY) {
		cubes.forEach(i -> i.updateDirection(toX, toY));
	}

	@Override // see item.java
	public void updatePoly() {
		cubes.forEach(i -> i.updatePoly());
	}

	@Override // see item.java
	public void removeCube() {
		cubes.forEach(i -> i.removeCube());
	}

	@Override // see item.java
	public boolean containsPoint(int x, int y, int z) {
		return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y;
	}

	@Override // see item.java
	public List<model.rendering.Polygon> getPolygons() {
		List<Polygon> allPolys = new ArrayList<>();
		// Add all the cubes polygons
		cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
		return allPolys;
	}

	public boolean pointNearObject(double x, double y, int z) {
		if ((this.x + DETECT_PLAYER_BOUNDARY + this.width) > x && (this.y + DETECT_PLAYER_BOUNDARY + this.length) > y
				&& this.x - DETECT_PLAYER_BOUNDARY < x && this.y - DETECT_PLAYER_BOUNDARY < y) {
			return true;
		} else {
			return false;
		}
	}
}