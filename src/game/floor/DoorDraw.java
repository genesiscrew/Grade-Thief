package game.floor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import gui.Cube;
import gui.Drawable;
import gui.Polygon;
import items.Door;

public class DoorDraw implements Drawable {
	private final int MARKER_THICKNESS = 5;

    private java.util.List<Cube> cubes;
    private double x;
    private double y;
    private double z;
    private double width;
    private double length;
    private double height;
    private Color color;
    private Door door;

    public String toString() {
    	return x + " " + y + " " + z + " " + width + " " + length + " " + height + " " + color + " door " + door;
    }

    public DoorDraw(double x, double y, double z, double width, double length, double height, Color c, Door door) {
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



        this.color = c;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.length = length;
        this.height = height;
        this.door = door;
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
    public List<gui.Polygon> getPolygons() {
        List<Polygon> allPolys = new ArrayList<>();
        // Add all the cubes polygons
        cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
        return allPolys;
    }
}