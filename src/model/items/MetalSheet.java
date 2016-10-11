package model.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import model.rendering.Cube;
import model.rendering.Polygon;

/**
 * Created by Stefan Vrecic on 22/09/16.
 */
public class MetalSheet extends Item {

	public MetalSheet(int itemID, String itemType, double x, double y, double z, double width, double length,
			double height, Color c) {
		super(itemID, itemType, x, y, z, width, length, height, c);
		System.out.println("height " + height);
		System.out.println("height " + height);
		System.out.println("height " + height);
		System.out.println("height " + height);

		cubes.add(new Cube(x, y, z, width, length, height, c));
		// Tube
		// cubes.add(new Cube(x, y, z, (9/10)*width, MARKER_THICKNESS, height,
		// c));
		// ballpoint
		// cubes.add(new Cube(x, (y+length) + (9/10) * width, z, width, (1/10) *
		// length, height, c));
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

	public String toString() {
		return x + " " + y + " " + z + " " + width + " " + length + " " + height + " " + color;
	}

	@Override // see item.java
	public void addInteractions() {
		interactionsAvailable = new ArrayList<>();
		interactionsAvailable.add(Interaction.TAKE);
	}

}
