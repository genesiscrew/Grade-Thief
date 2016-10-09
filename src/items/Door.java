package items;

import gui.Cube;
import gui.Polygon;

import java.awt.*;
import java.util.List;

public class Door extends Item {
	public int code;
	private String itemType = "Door";
	private int pass;
	int i;
	private double[][] locations = new double[2][2];

	public Door(int itemID, String itemType, double x, double y, double z, double width, double length, double height,
			Color c) {
		super(itemID, itemType, x, y, z, width, length, height, c);
		cubes.add(new Cube(x, y, z, width, length, height, c));
		this.draw = true;
	}

	public String itemType() {
		return itemType;
	}

	public static Door getDoor(int doorCode) {
		// read map for door Code
		// return door
		return null;
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
		if (!draw)
			return false;
		return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y;
	}

	@Override
	public List<Polygon> getPolygons() {
		return cubes.get(0).getPolygons();
	}

	public void changeState() {
		draw = !draw;
	}

	/**
	 * this method checks whether player actually passed through a door or not
	 *
	 * @return
	 */
	public boolean passedThrough() {
		double xOffset = Math.abs(locations[0][0] - locations[1][0]);
		double yOffset = Math.abs(locations[0][1] - locations[1][1]);
		if (xOffset + x > Math.max(locations[0][0], locations[1][0])
				|| yOffset + y > Math.max(locations[0][1], locations[1][1])) {
			return true;

		}
		return false;

	}

	public void setLocations(double X, double Y) {
		if (i < 2) {
			this.locations[i][0] = X;
			this.locations[i][1] = Y;
			i++;
		} else {
			i = 0;
		}
	}

	public boolean isDraw() {
		return draw;
	}

}