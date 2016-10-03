package gui;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

/**
 * This represents a 3D cube that is rendered onto the GUI
 */
public class Cube implements Drawable {

    double x, y, z, width, length, height, rotation = Math.PI * 0.75;
    double[] RotAdd = new double[4];
    Color c;
    double x1, x2, x3, x4, y1, y2, y3, y4;
    Polygon[] polygons = new Polygon[6];
    double[] angle;

    /**
     * Make a new cube at position x, y, z. with the specified width, length, height and colour
     *
     * @param x
     * @param y
     * @param z
     * @param width
     * @param length
     * @param height
     * @param c
     */
    public Cube(double x, double y, double z, double width, double length, double height, Color c) {
        polygons[0] = new Polygon(new double[]{x, x + width, x + width, x}, new double[]{y, y, y + length, y + length}, new double[]{z, z, z, z}, c, false);
        polygons[1] = new Polygon(new double[]{x, x + width, x + width, x}, new double[]{y, y, y + length, y + length}, new double[]{z + height, z + height, z + height, z + height}, c, false);
        polygons[2] = new Polygon(new double[]{x, x, x + width, x + width}, new double[]{y, y, y, y}, new double[]{z, z + height, z + height, z}, c, false);
        polygons[3] = new Polygon(new double[]{x + width, x + width, x + width, x + width}, new double[]{y, y, y + length, y + length}, new double[]{z, z + height, z + height, z}, c, false);
        polygons[4] = new Polygon(new double[]{x, x, x + width, x + width}, new double[]{y + length, y + length, y + length, y + length}, new double[]{z, z + height, z + height, z}, c, false);
        polygons[5] = new Polygon(new double[]{x, x, x, x}, new double[]{y, y, y + length, y + length}, new double[]{z, z + height, z + height, z}, c, false);

        this.c = c;
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.length = length;
        this.height = height;

        setRotAdd();
        updatePoly();
    }

    @Override
    public void setRotAdd() {
        angle = new double[4];

        double xdif = -width / 2 + 0.00001;
        double ydif = -length / 2 + 0.00001;

        angle[0] = Math.atan(ydif / xdif);

        if (xdif < 0)
            angle[0] += Math.PI;

        /////////
        xdif = width / 2 + 0.00001;
        ydif = -length / 2 + 0.00001;

        angle[1] = Math.atan(ydif / xdif);

        if (xdif < 0)
            angle[1] += Math.PI;
        /////////
        xdif = width / 2 + 0.00001;
        ydif = length / 2 + 0.00001;

        angle[2] = Math.atan(ydif / xdif);

        if (xdif < 0)
            angle[2] += Math.PI;

        /////////
        xdif = -width / 2 + 0.00001;
        ydif = length / 2 + 0.00001;

        angle[3] = Math.atan(ydif / xdif);

        if (xdif < 0)
            angle[3] += Math.PI;

        RotAdd[0] = angle[0] + 0.25 * Math.PI;
        RotAdd[1] = angle[1] + 0.25 * Math.PI;
        RotAdd[2] = angle[2] + 0.25 * Math.PI;
        RotAdd[3] = angle[3] + 0.25 * Math.PI;
    }

    /**
     * Update the cubes position by the amount specified.
     * @param dx
     * @param dy
     * @param dz
     */
    public void updatePosition(double dx, double dy, double dz){
        this.x += dx;
        this.y += dy;
        this.z += dz;

        for(Polygon p : polygons){
            p.updatePosition(dx, dy, dz);
        }
    }


    /**
     * Update the direction the polygon is facing
     *
     * @param toX
     * @param toY
     */
    @Override
    public void updateDirection(double toX, double toY) {
        double xdif = toX - (x + width / 2) + 0.00001;
        double ydif = toY - (y + length / 2) + 0.00001;

        double anglet = Math.atan(ydif / xdif) + 0.75 * Math.PI;

        if (xdif < 0)
            anglet += Math.PI;

        rotation = anglet;
        updatePoly();
    }

    @Override
    public void updatePoly() {
        double radius = Math.sqrt(width * width + length * length);

        x1 = x + width * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[0]);
        x2 = x + width * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[1]);
        x3 = x + width * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[2]);
        x4 = x + width * 0.5 + radius * 0.5 * Math.cos(rotation + RotAdd[3]);

        y1 = y + length * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[0]);
        y2 = y + length * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[1]);
        y3 = y + length * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[2]);
        y4 = y + length * 0.5 + radius * 0.5 * Math.sin(rotation + RotAdd[3]);

        polygons[0].x = new double[]{x1, x2, x3, x4};
        polygons[0].y = new double[]{y1, y2, y3, y4};
        polygons[0].z = new double[]{z, z, z, z};

        polygons[1].x = new double[]{x4, x3, x2, x1};
        polygons[1].y = new double[]{y4, y3, y2, y1};
        polygons[1].z = new double[]{z + height, z + height, z + height, z + height};

        polygons[2].x = new double[]{x1, x1, x2, x2};
        polygons[2].y = new double[]{y1, y1, y2, y2};
        polygons[2].z = new double[]{z, z + height, z + height, z};

        polygons[3].x = new double[]{x2, x2, x3, x3};
        polygons[3].y = new double[]{y2, y2, y3, y3};
        polygons[3].z = new double[]{z, z + height, z + height, z};

        polygons[4].x = new double[]{x3, x3, x4, x4};
        polygons[4].y = new double[]{y3, y3, y4, y4};
        polygons[4].z = new double[]{z, z + height, z + height, z};

        polygons[5].x = new double[]{x4, x4, x1, x1};
        polygons[5].y = new double[]{y4, y4, y1, y1};
        polygons[5].z = new double[]{z, z + height, z + height, z};
    }

    @Override
    public void removeCube() {
        // TODO
    }

    @Override
    public boolean containsPoint(int x, int y, int z) {
        return (this.x + this.width) > x && (this.y + this.length) > y && this.x > x && this.y > y &&
                (this.z + this.height) > z && this.z > z;
    }

    public List<Polygon> getPolygons() {
        return Arrays.asList(polygons);

    }
}
