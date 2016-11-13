package model.items;

import model.items.Item.Interaction;
import model.rendering.Cube;
import model.rendering.Drawable;
import model.rendering.Polygon;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by wareinadam on 22/09/16.
 */
public class Laptop extends Item implements Drawable{

    private final int LAPTOP_THICKNESS = 1;
	private boolean locked;
	private int keyID;

    public String toString() {
    	return x + " " + y + " " + z + " " + width + " " + length + " " + height + " " + color;
    }

    public Laptop(int itemID, String itemType, int keyID, double x, double y, double z, double width, double length, double height, Color c) {
        super(itemID, "David's Laptop", x, y, z, width, length, height, c);
        this.keyID = keyID;
        // Screen
        cubes.add(new Cube(x, y+length, z, width, LAPTOP_THICKNESS, height, c));

        // The base of the laptop
       cubes.add(new Cube(x, y, z, width, length, LAPTOP_THICKNESS, c));
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
        return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y ;
    }

    @Override // see item.java
    public List<Polygon> getPolygons() {
        List<Polygon> allPolys = new ArrayList<>();
        // Add all the cubes polygons
        cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
        return allPolys;
    }

    @Override // see item.java
    public void addInteractions() {
        interactionsAvailable = new ArrayList<>();
        interactionsAvailable.add(Interaction.UNLOCK);
    ;
    }

    public void setLock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;

    }


	public int getKeyID() {
		// TODO Auto-generated method stub
		return this.keyID;
	}


}
