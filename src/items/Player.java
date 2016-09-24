package items;

import gui.Cube;
import gui.Drawable;
import gui.ThreeDPolygon;

import java.awt.*;
import java.util.*;

/**
 * Created by wareinadam on 24/09/16.
 */
public class Player implements Drawable{
    private final int TABLE_THICKNESS = 1;
    private java.util.List<Cube> cubes;
    private double x;
    private double y;
    private double z;
    private double width;
    private double length;
    private double height;
    private Color color;

    public Player(double x, double y, double z, double width, double length, double height, Color c) {
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
        int legHeight = (int) (height/2);
        // first leg
        cubes.add(new Cube(x, y + (legWidth/2), z, legWidth, legWidth, legHeight, c));
        // second leg
        cubes.add(new Cube(x + width - legWidth, y+(legWidth/2), z, legWidth, legWidth, legHeight, c));

        // body
        cubes.add(new Cube(x, y, z+legHeight, width, width/1.5, legHeight, c));

        // arms
        cubes.add(new Cube(x+width, y, z+legHeight+(legHeight/2), width, width/1.5, legHeight/3, c));
        cubes.add(new Cube(x-width, y, z+legHeight+(legHeight/2), width, width/1.5, legHeight/3, c));

        // head
        cubes.add(new Cube(x+(width/4), y, z+(legHeight*2), width/2, width/1.5, legHeight/3, c));
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
    public java.util.List<ThreeDPolygon> getPolygons() {
        java.util.List<ThreeDPolygon> allPolys = new ArrayList<>();
        // Add all the cubes cubes
        cubes.forEach(c -> allPolys.addAll(c.getPolygons()));
        return allPolys;
    }
}
