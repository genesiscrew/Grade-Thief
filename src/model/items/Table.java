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
public class Table extends Item {

    private final int TABLE_THICKNESS = 1;

    public Table(int itemID, String itemType, double x, double y, double z, double width, double length, double height, Color c) {
        super(itemID, itemType, x, y, z, width, length, height, c);

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

        // Draw the table top
        cubes.add(new Cube(x, y, z + legHeight, width, length, TABLE_THICKNESS, c));
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

    @Override
    public void addInteractions() {
        interactionsAvaliable = new ArrayList<>();
        interactionsAvaliable.add(Interaction.SIT);
    }
}
