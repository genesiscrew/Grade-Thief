package items;

import gui.*;
import gui.Polygon;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Stefan Vrecic on 22/09/16.
 */
public class MetalSheet extends Item {
    private java.util.List<Cube> cubes;


    public MetalSheet(int itemID, String itemType, double x, double y, double z, double width, double length, double height, Color c) {
        super(itemID, itemType, x, y, z, width, length, height, c);
        System.out.println("height " + height);
        System.out.println("height " + height);
        System.out.println("height " + height);
        System.out.println("height " + height);

        cubes = new ArrayList<>();


        cubes.add(new Cube(x, y, z, width, length, height, c));
        // Tube
        // cubes.add(new Cube(x, y, z, (9/10)*width, MARKER_THICKNESS, height, c));
        // ballpoint
        // cubes.add(new Cube(x, (y+length) + (9/10) * width, z, width, (1/10) * length, height, c));
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
    }

    @Override
    public List<gui.Polygon> getPolygons() {
        List<Polygon> allPolys = new ArrayList<>();
        // Add all the cubes polygons
        cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
        return allPolys;
    }

    public String toString() {
        return x + " " + y + " " + z + " " + width + " " + length + " " + height + " " + color;
    }

}
