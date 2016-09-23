package items;

import gui.Cube;
import gui.Drawable;

import java.awt.*;
import java.util.*;

/**
 * Created by wareinadam on 22/09/16.
 */
public class Chair implements Drawable{

    private final int TABLE_THICKNESS = 1;
    private java.util.List<Cube> polygons;
    private double x;
    private double y;
    private double z;
    private double width;
    private double length;
    private double height;
    private Color color;

    public Chair(double x, double y, double z, double width, double length, double height, Color c) {
        polygons = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.length = length;
        this.height = height;
        this.color = c;

        // First make the legs
        int legWidth = (int)(width / 4);
        int legHeight = (int) (height - TABLE_THICKNESS);
        // first leg
        polygons.add(new Cube(x, y, z, legWidth, legWidth, legHeight, c));
        // first leg + width
        polygons.add(new Cube(x+width-legWidth, y, z, legWidth, legWidth, legHeight, c));
        // first leg + length
        polygons.add(new Cube(x, y+length-legWidth, z, legWidth, legWidth, legHeight, c));
        // oposite corner to first leg
        polygons.add(new Cube(x+width-legWidth, y+length-legWidth,z, legWidth, legWidth, legHeight, c));

        // Draw the table top
        polygons.add(new Cube(x, y, z+legHeight, width, length, TABLE_THICKNESS, c));
    }

    @Override
    public void setRotAdd() {
        polygons.forEach(i -> i.setRotAdd());
    }

    @Override
    public void updateDirection(double toX, double toY) {
        polygons.forEach(i -> i.updateDirection(toX, toY));
    }

    @Override
    public void updatePoly() {
        polygons.forEach(i -> i.updatePoly());
    }

    @Override
    public void removeCube() {
        polygons.forEach(i -> i.removeCube());
    }

    public boolean containsPoint(int x, int y, int z){
        return (this.x + this.width) > x && (this.y + this.length) > y && this.x > x && this.y > y &&
                (this.z + this.height) > z && this.z > z;

    }
}
