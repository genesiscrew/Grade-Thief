package items;

import java.awt.*;
import java.util.List;

import gui.Drawable;
import gui.Polygon;


public abstract class Item extends GameObject implements Drawable {

    List<String> options; // items that are interactable may have a list of options to choose from
    public int itemID;
    protected double x;
    protected double y;
    protected double z;
    protected double width;
    protected double length;
    protected double height;
    protected Color color;

    public Item(int itemID, String itemType, double x, double y, double z, double width, double length, double height, Color c) {
        super(itemID, itemType);
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.length = length;
        this.height = height;
        this.color = c;
    }

    public String toString() {
        return "I";
    }
    // Location location;

    @Override
    public void setRotAdd() {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateDirection(double toX, double toY) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updatePoly() {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeCube() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean containsPoint(int x, int y, int z) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<Polygon> getPolygons() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean pointNearObject(int x, int y, int z) {
        return false;
    }


    // Model file

}
