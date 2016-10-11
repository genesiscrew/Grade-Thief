package model.characters;

import model.floor.Location;
import model.items.Item;
import model.rendering.Cube;
import model.rendering.Drawable;
import model.rendering.Polygon;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by wareinadam on 24/09/16.
 */
public class Player implements Drawable {
	private java.util.List<Cube> cubes;
	protected double x;
	protected double y;
	protected double z;
	protected double width;
	protected double length;
	protected double height;
	protected Color color;
	protected boolean jumping;

	private List<Item> inventory; // = new ArrayList<Item> ();
	private String levelName;
	private boolean inRoom;
	private int roomCounter;
	private Location location;
	protected final static Color TOP_COLOR = new Color(102, 204, 204);
	protected final static Color SKIN_COLOR = new Color(204, 153, 102);
	protected final static Color PANTS_COLOR = new Color(10, 10, 255);
	protected final static Color DARK_BROWN = new Color(103, 51, 0);

	public Player(double x, double y, double z, double width, double length, double height, Color c) {
		inventory = new ArrayList<Item>();
		cubes = new ArrayList<>();
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.length = length;
		this.height = height;
		this.color = c;

		height *= 0.8;

		// First make the legs
		int legAndArmWidth = (int) (width * 0.25); // 25 %
		int halfWidth = (int) (width * 0.5); // 25 %
		int bodyLength = (int)(length * 0.7);
		int legHeight = (int) (height * 0.5); // 50 %
		int armHeight = (int) (height * 0.40); // 40 %
		int torsoAboveArmHeight = (int) (height * 0.15); // 40 %
		int headHeight = (int) (height * 0.22); // 17 %

		// first leg
		cubes.add(new Cube(x, y + (legAndArmWidth / 2), z, legAndArmWidth, bodyLength, legHeight, PANTS_COLOR));

		// second leg
		cubes.add(new Cube(x + legAndArmWidth, y + (legAndArmWidth / 2), z, legAndArmWidth, bodyLength, legHeight, PANTS_COLOR));

		// body
		cubes.add(new Cube(x, y, z + legHeight, halfWidth, bodyLength, legHeight, TOP_COLOR));

		// arms
		cubes.add(new Cube(x + halfWidth, y, z + legHeight , legAndArmWidth, bodyLength, armHeight, SKIN_COLOR));
		cubes.add(new Cube(x - legAndArmWidth, y, z + legHeight , legAndArmWidth, bodyLength, armHeight, SKIN_COLOR));

		// above arm torso
		cubes.add(new Cube(x + halfWidth, y, z + legHeight+armHeight , legAndArmWidth, bodyLength, torsoAboveArmHeight, TOP_COLOR));
		cubes.add(new Cube(x - legAndArmWidth, y, z + legHeight +armHeight, legAndArmWidth, bodyLength, torsoAboveArmHeight, TOP_COLOR));

		// head
		cubes.add(new Cube(x, y, z + (legHeight * 2), bodyLength, bodyLength, headHeight, SKIN_COLOR));

		// above head
		cubes.add(new Cube(x, y, z + (legHeight * 2) + headHeight, bodyLength, bodyLength, headHeight*0.3, DARK_BROWN));

		// Eyes
	}

	/**
	 * Moves the player the specified amount. 0 means no change will be made.
	 *
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void updatePosition(double dx, double dy, double dz) {
		this.x += dx;
		this.y += dy;
		this.z += dz;

		cubes.forEach(c -> c.updatePosition(dx, dy, dz));
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
		//  && (this.z + this.height) > z && this.z > z;
	}

	@Override // see item.java
	public java.util.List<Polygon> getPolygons() {
		java.util.List<Polygon> allPolys = new ArrayList<>();
		// Add all the cubes cubes
		cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
		return allPolys;
	}
	/**
	 * returns the onscreen zo f the player
	 * @return
	 */
	public double getZ() {
		return z;
	}

	/**
	 * returns the onscreen y of the player
	 * @return
	 */
	public double getY() {
		return y;
	}

	/**
	 * returns the onscreen x of the player
	 * @return
	 */
	public double getX() {
		return x;
	}

	/**
	 * returns the arraylist of the items in the inventory
	 * @return
	 */
	public List<Item> getInventory() {
		return inventory;
	}

	public boolean containsKeyInInventory(int id){
		for(Item i : inventory){
			if(i.getItemID() == id)
				return true;
		}

		return false;
	}

	/**
	 * removes an item from the inventory by removing it from the arrayList
	 * @param item
	 */
	public void removeFromInventory(Item item) {
		this.inventory.remove(item);
	}

	/**
	 * adds the item to the inventory by adding to the ArrayList
	 * @param item
	 */
	public void addToInventory(Item item) {
		this.inventory.add(item);
	}

	/**
	 * This makes the player jump in the game. It uses a seperate thread to simulate the jumping so we can continue
	 * updating the display throughout the jump.
	 */
	public void jump(double[] viewFrom) {
		if (jumping)
			return;
		Thread jumpingThread = new Thread() {
			@Override // see item.java
			public void run() {
				for (int i = 0; i < 5; i++) {
					viewFrom[2] += 2;
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for (int i = 0; i < 5; i++) {
					viewFrom[2] -= 2;
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				jumping = false;
			}
		};
		jumpingThread.start();
	}

	public void setRoom(String roomName) {
		this.levelName = roomName;

	}

	public String getLevelName() {
		return this.levelName;
	}

	public void inRoom(boolean b) {
		this.inRoom = b;

	}

	public boolean isInRoom() {
		// TODO Auto-generated method stub
		return inRoom;
	}

	public void incrementRoomCounter() {
		this.roomCounter++;

	}
	public void resetRoomCounter() {
		this.roomCounter = 0;

	}
	public int getRoomCounter() {
		return this.roomCounter;

	}

	public Location getLocation() {
		return this.location;
	}
	public void setLocation(Location location) {
		this.location = location;

	}
}
