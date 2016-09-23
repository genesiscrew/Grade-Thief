package items;

import gui.Cube;
import gui.Drawable;

import java.awt.*;
import java.util.*;

/**
 * Created by wareinadam on 22/09/16.
 */
public class Laptop implements Drawable{

    private final int TABLE_THICKNESS = 1;
    private java.util.List<Cube> polygons;

    public Laptop(double x, double y, double z, double width, double length, double height, Color c) {
        polygons = new ArrayList<>();

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
}
