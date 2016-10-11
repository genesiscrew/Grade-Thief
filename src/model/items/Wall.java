package model.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import model.rendering.Cube;
import model.rendering.Polygon;

/**
 * Created by wareinadam on 29/09/16.
 */
public class Wall extends Item {
    private Cube cube;

    public Wall(int itemID, String itemType, double x, double y, double z, double width, double length, double height, Color c) {
        super(itemID, itemType, x, y, z, width, length, height, c);
        cube = new Cube(x,y,z,width,length,height,c);
    }


    @Override // see item.java
    public void setRotAdd() {
        cube.setRotAdd();
    }

    @Override // see item.java
    public void updateDirection(double toX, double toY) {
        cube.updateDirection(toX, toY);
    }

    @Override // see item.java
    public void updatePoly() {
        cube.updatePoly();
    }

    @Override // see item.java
    public void removeCube() {
        cube.removeCube();
    }

    @Override // see item.java
    public boolean containsPoint(int x, int y, int z) {
        return (this.x + this.width) > x && (this.y + this.length) > y && this.x < x && this.y < y ;
    }

    @Override // see item.java
    public List<Polygon> getPolygons() {
        List<Polygon> allPolys = new ArrayList<>();
        // Add all the cubes polygons
        allPolys.addAll(cube.getPolygons());
        return allPolys;
    }


}
