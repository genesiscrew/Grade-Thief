package items;

import gui.Cube;
import gui.Polygon;

import java.awt.*;
import java.util.List;

public class Door extends Item {
    public int code;
    private String itemType = "Door";
    private Cube cube;

    public Door(int itemID, String itemType, double x, double y, double z, double width, double length, double height, Color c) {
        super(itemID, itemType, x, y, z, width, length, height, c);
        this.cube = new Cube(x, y, z, width, length, height, c);
        this.draw = true;
    }

    public String itemType() {
        return itemType;
    }

    public static Door getDoor(int doorCode) {
        // read map for door Code
        // return door
        return null;
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
        return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y;
    }

    @Override
    public List<Polygon> getPolygons() {
        return cube.getPolygons();
    }

    public void changeState(){
        draw = !draw;
    }

    public boolean isDraw() {
        return draw;
    }
}