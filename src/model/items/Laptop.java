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

    public String toString() {
    	return x + " " + y + " " + z + " " + width + " " + length + " " + height + " " + color;
    }

    public Laptop(int itemID, String itemType, double x, double y, double z, double width, double length, double height, Color c) {
        super(itemID, itemType, x, y, z, width, length, height, c);

        // Screen
        cubes.add(new Cube(x, y+length, z, width, LAPTOP_THICKNESS, height, c));

        // The base of the laptop
       cubes.add(new Cube(x, y, z, width, length, LAPTOP_THICKNESS, c));
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
        return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y ;
    }

    @Override
    public List<Polygon> getPolygons() {
        List<Polygon> allPolys = new ArrayList<>();
        // Add all the cubes polygons
        cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
        return allPolys;
    }

    @Override
    public void addInteractions() {
        interactionsAvaliable = new ArrayList<>();
    }


}
