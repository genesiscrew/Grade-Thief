package items;

import gui.Cube;
import gui.Drawable;
import gui.Polygon;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by wareinadam on 22/09/16.
 */
public class Dog extends Item {

    private final int Dog_THICKNESS = 1;
    private java.util.List<Cube> cubes;


    public Dog(int itemID, String itemType, double x, double y, double z, double width, double length, double height, Color c) {
        super(itemID, itemType, x, y, z, width, length, height, c);
        cubes = new ArrayList<>();

        // First make the legs
        int legWidth = (int) (width / 4);
        int legHeight = (int) (height - Dog_THICKNESS);
        // first leg
        cubes.add(new Cube(x, y, z, legWidth, legWidth, legHeight, c));
        // first leg + width
        cubes.add(new Cube(x + width - legWidth, y, z, legWidth, legWidth, legHeight, c));
        // first leg + length
        cubes.add(new Cube(x, y + length - legWidth, z, legWidth, legWidth, legHeight, c));
        // oposite corner to first leg
        cubes.add(new Cube(x + width - legWidth, y + length - legWidth, z, legWidth, legWidth, legHeight, c));

        // add dog leg

        cubes.add(new Cube(x + width, y + length, z , legWidth, legHeight, Dog_THICKNESS, c));
        cubes.add(new Cube(x - legWidth, y + length, z , legWidth, legHeight, Dog_THICKNESS, c));
        // Draw the Dog top
        cubes.add(new Cube(x-legWidth, y, z + legHeight, width, length, Dog_THICKNESS, c));
        // Draw dog neck
      //  cubes.add(new Cube(x-legWidth, y, z))

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
    public List<Polygon> getPolygons() {
        java.util.List<Polygon> allPolys = new ArrayList<>();
        // Add all the cubes cubes
        cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
        return allPolys;
    }

}
