package model.items;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import model.rendering.Cube;
import model.rendering.Polygon;

public class Door extends Item {
    private String itemType = "Door";
    private int pass;
    int i;
    private double[][] locations = new double[2][2];
    private boolean locked = true;

    public Door(int itemID, String itemType, double x, double y, double z, double width, double length, double height,
                Color c) {
        super(itemID, itemType, x, y, z, width, length, height, c);
        cubes.add(new Cube(x, y, z, width, length, height, c));
        this.draw = true;
    }

    public String itemType() {
        return itemType;
    }

    public void setLock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

    @Override
    public void addInteractions() {
        interactionsAvailable = new ArrayList<>();
        interactionsAvailable.add(Interaction.UNLOCK);
        interactionsAvailable.add(Interaction.OPEN);
        interactionsAvailable.add(Interaction.CLOSE);
    }

    @Override
    public void updateDirection(double toX, double toY) {

    }

    @Override
    public void updatePoly() {

    }

    @Override
    public void setRotAdd() {

    }

    @Override
    public void removeCube() {

    }

    @Override
    public boolean containsPoint(int x, int y, int z) {
        if (!draw)
            return false;
        return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y;
    }

    @Override
    public List<Polygon> getPolygons() {
        return cubes.get(0).getPolygons();
    }

    public void changeState() {
        draw = !draw;
    }

    /**
     * this method checks whether player actually passed through a door or not
     *
     * @return
     */
    public boolean passedThrough() {
        double xOffset = Math.abs(locations[0][0] - locations[1][0]);
        double yOffset = Math.abs(locations[0][1] - locations[1][1]);
        if (xOffset + x > Math.max(locations[0][0], locations[1][0])
                || yOffset + y > Math.max(locations[0][1], locations[1][1])) {
            return true;

        }
        return false;

    }

    public void setLocations(double X, double Y) {
        if (i < 2) {
            this.locations[i][0] = X;
            this.locations[i][1] = Y;
            i++;
        } else {
            i = 0;
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isDraw() {
        return draw;
    }
}