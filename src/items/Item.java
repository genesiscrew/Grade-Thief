package items;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import gui.Cube;
import gui.Drawable;

/**
 * @Author Adam Wareing
 */
public abstract class Item extends GameObject implements Drawable {

	public final double DETECT_PLAYER_BOUNDARY = 10;


	protected double x;
	protected double y;
	protected double z;
	protected double width;
	protected double length;
	protected double height;
	protected Color color;
	protected List<Interaction> interactionsAvaliable;
	protected boolean draw = true;
	protected java.util.List<Cube> cubes;


	private double DETECT_PLAYER_BOUNDARY2 = 5;

	public void canDraw() {
		draw = !draw;
	}

	public boolean isDraw() {
		return draw;
	}

	public Item(int itemID, String itemType, double x, double y, double z, double width, double length, double height,
			Color c) {
		super(itemID, itemType);
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.length = length;
		this.height = height;
		this.color = c;
		this.cubes = new ArrayList<>();
		addInteractions();
	}

	public void moveItemBy(double dx, double dy, double dz) {
		this.x += dx;
		this.y += dy;
		this.z += dz;

		cubes.forEach(c -> c.updatePosition(dx, dy, dz));
	}

	public void addInteractions() {
		interactionsAvaliable = new ArrayList<>();
		interactionsAvaliable.add(Interaction.OPEN);
		interactionsAvaliable.add(Interaction.CLOSE);
	}

	public boolean pointNearObject(double x, double y, double z) {
		if ((this.x + DETECT_PLAYER_BOUNDARY + this.width) > x && (this.y + DETECT_PLAYER_BOUNDARY + this.length) > y
				&& this.x - DETECT_PLAYER_BOUNDARY < x && this.y - DETECT_PLAYER_BOUNDARY < y) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean pointNearObject2(double x, double y, double z) {
		if ((this.x + DETECT_PLAYER_BOUNDARY2 + this.width) > x && (this.y + DETECT_PLAYER_BOUNDARY2 + this.length) > y
				&& this.x - DETECT_PLAYER_BOUNDARY2 < x && this.y - DETECT_PLAYER_BOUNDARY2 < y) {
			return true;
		} else {
			return false;
		}
	}

	public void performAction(Interaction interaction) {
		switch (interaction) {
		case PICK_UP:
			draw = false;
			break;
		case OPEN:
			draw = false;
			break;
		case CLOSE:
			draw = true;
			break;
		case SIT:
			System.out.println("sit down");
			break;
		case TAKE:
			draw = false;
			break;
		}
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}


	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getLength() {
		return length;
	}

//	public int getItemID() {
//		return this.itemID;
//	}
	public void setLength(double length) {
		this.length = length;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public java.util.List<Cube> getCubes() {
		return cubes;
	}

	public void setCubes(java.util.List<Cube> cubes) {
		this.cubes = cubes;
	}

	public double getDETECT_PLAYER_BOUNDARY() {
		return DETECT_PLAYER_BOUNDARY;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setInteractionsAvaliable(List<Interaction> interactionsAvaliable) {
		this.interactionsAvaliable = interactionsAvaliable;
	}

	public void setDraw(boolean draw) {
		this.draw = draw;
	}

	// Model file
	public enum Interaction {
		OPEN, UNLOCK, CLOSE, PICK_UP, SIT, TAKE
	}

	public List<Interaction> getInteractionsAvaliable() {
		return interactionsAvaliable;
	}
}
