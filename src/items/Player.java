package items;

import gui.Cube;
import gui.Drawable;
import gui.Polygon;

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

        // First make the legs
        int legWidth = (int) (width / 2.5);
        int legHeight = (int) (height / 2);
        // first leg
        cubes.add(new Cube(x, y + (legWidth / 2), z, legWidth, legWidth, legHeight, c));
        // second leg
        cubes.add(new Cube(x + width - legWidth, y + (legWidth / 2), z, legWidth, legWidth, legHeight, c));

        // body
        cubes.add(new Cube(x, y, z + legHeight, width, width / 1.5, legHeight, c));

        // arms
        cubes.add(new Cube(x + width, y, z + legHeight + (legHeight / 2), width, width / 1.5, legHeight / 3, c));
        cubes.add(new Cube(x - width, y, z + legHeight + (legHeight / 2), width, width / 1.5, legHeight / 3, c));

        // head
        cubes.add(new Cube(x + (width / 4), y, z + (legHeight * 2), width / 2, width / 1.5, legHeight / 3, c));
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

    @Override
    public void setRotAdd() {
        cubes.forEach(i -> i.setRotAdd());
    }

    @Override
    public void updateDirection(double toX, double toY) {
        cubes.forEach(i -> i.updateDirection(toX, toY));
    }

    @Override
    public void updatePoly() {
        cubes.forEach(i -> i.updatePoly());
    }

    @Override
    public void removeCube() {
        cubes.forEach(i -> i.removeCube());
    }

    @Override
    public boolean containsPoint(int x, int y, int z) {
        return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y;
        //  && (this.z + this.height) > z && this.z > z;
    }

    @Override
    public java.util.List<Polygon> getPolygons() {
        java.util.List<Polygon> allPolys = new ArrayList<>();
        // Add all the cubes cubes
        cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
        return allPolys;
    }
    public double getZ() {
        return z;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

	public List<Item> getInventory() {
		return inventory;
	}

	public void removeFromInventory(Item item) {
		this.inventory.remove(item);
	}

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
            @Override
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
}
