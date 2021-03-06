package model.items;

import java.awt.*;
import java.util.*;

import model.rendering.Cube;
import model.rendering.Drawable;
import model.rendering.Polygon;

/**
 * Created by wareinadam on 22/09/16.
 */
public class Chair extends Item {

    private final int TABLE_THICKNESS = 1;
    private java.util.List<Cube> cubes;

    public Chair(int itemID, String itemType, double x, double y, double z, double width, double length, double height, Color c) {
        super(itemID, itemType, x, y, z, width, length, height, c);
        cubes = new ArrayList<>();

        // First make the legs
        int legWidth = (int) (width / 4);
        int legHeight = (int) (height - TABLE_THICKNESS);
        // first leg
        cubes.add(new Cube(x, y, z, legWidth, legWidth, legHeight, c));
        // first leg + width
        cubes.add(new Cube(x + width - legWidth, y, z, legWidth, legWidth, legHeight, c));
        // first leg + length
        cubes.add(new Cube(x, y + length - legWidth, z, legWidth, legWidth, legHeight, c));
        // oposite corner to first leg
        cubes.add(new Cube(x + width - legWidth, y + length - legWidth, z, legWidth, legWidth, legHeight, c));

        // Draw the seat
        cubes.add(new Cube(x, y, z + legHeight, width, length, TABLE_THICKNESS, c));

        // Draw the back of the chair
        cubes.add(new Cube(x, y, z + legHeight + TABLE_THICKNESS, TABLE_THICKNESS, length, legHeight, c));
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

    public void updateDirection() {
        cubes.forEach(i -> i.updateDirection(60, 60));
    }

    @Override
    public boolean containsPoint(int x, int y, int z) {
        return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y;
    }

    @Override
    public java.util.List<Polygon> getPolygons() {
        java.util.List<Polygon> allPolys = new ArrayList<>();
        // Add all the cubes cubes
        cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
        return allPolys;
    }

	
}
