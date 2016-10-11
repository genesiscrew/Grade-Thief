package model.items;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import model.rendering.Cube;
import model.rendering.Drawable;

/**
 * @Author Adam Wareing
 * This is an item in the game, it provides util methods that all items in the game world utilise.
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
	protected List<Interaction> interactionsAvailable;
	protected boolean draw = true;
	protected java.util.List<Cube> cubes;

	/**
	 * This sets up the itemID, itemType, position and size of the item to be rendered in the 3D world.
	 * @param itemID
	 * @param itemType
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param length
     * @param height
     * @param c
     */
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

	/**
	 * Move an item by the specified amount.
	 * @param dx
	 * @param dy
     * @param dz
     */
	public void moveItemBy(double dx, double dy, double dz) {
		this.x += dx;
		this.y += dy;
		this.z += dz;

		cubes.forEach(c -> c.updatePosition(dx, dy, dz));
	}

	/**
	 * This adds all the interactions possible for an item. They should only be added here, so we can easily
	 * determine what interactions an item can perform.
	 */
	public void addInteractions() {
		interactionsAvailable = new ArrayList<>();
		interactionsAvailable.add(Interaction.OPEN);
		interactionsAvailable.add(Interaction.CLOSE);
	}

	/**
	 * Is the specified point near an object? NEAR is specified by 'DETECT_PLAYER_BOUNDARY' which is the tolerance of
	 * the boundary.
	 *
	 * @param x
	 * @param y
	 * @param z
     * @return true if the player is near, false otherwise.
     */
	public boolean pointNearObject(double x, double y, double z) {
		if ((this.x + DETECT_PLAYER_BOUNDARY + this.width) > x && (this.y + DETECT_PLAYER_BOUNDARY + this.length) > y
				&& this.x - DETECT_PLAYER_BOUNDARY < x && this.y - DETECT_PLAYER_BOUNDARY < y) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Perform an interaction on an item. This is declared final because ALL interaction logic should be performed here
	 * @param interaction
     */
	public final void performAction(Interaction interaction) {
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
			case ACCESS:
				//draw = false;
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

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void canDraw() {
		draw = !draw;
	}

	public boolean isDraw() {
		return draw;
	}

	public void setDraw(boolean draw) {
		this.draw = draw;
	}

	public String getName() {
		return getClass().getName().toLowerCase();
	}

	public List<Interaction> getInteractionsAvailable() {
		return interactionsAvailable;
	}



	/**
	 * These are all the interactions that can be performed by items.
	 * Not all items can perform all actions. What action an item can perform is determined by whats added to the
	 * interactionsAvaliable field.
	 * @see Item.addInteractions()
	 */
	public enum Interaction {
		OPEN, CLOSE, PICK_UP, SIT, TAKE, DROP, ACCESS
	}
}
