package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * This stores a polygon, its colour, the lighting and whether is should be drawn, its transparency and if its visible
 */
public class PolygonObject {
    Polygon polygon;
    Color color;
    boolean draw = true;
    boolean visible = true;
    boolean seeThrough = false;
    double lighting = 1;

    /**
     * Create a new polygon
     * @param x
     * @param y
     * @param c
     * @param seeThrough
     */
    public PolygonObject(double[] x, double[] y, Color c, boolean seeThrough) {
        polygon = new Polygon();
        for (int i = 0; i < x.length; i++)
            polygon.addPoint((int) x[i], (int) y[i]);
        this.color = c;
        this.seeThrough = seeThrough;
    }

    /**
     * Update the polygons x and y points
     * @param x
     * @param y
     */
    void updatePolygon(double[] x, double[] y) {
        polygon.reset();
        for (int i = 0; i < x.length; i++) {
            polygon.xpoints[i] = (int) x[i];
            polygon.ypoints[i] = (int) y[i];
            polygon.npoints = x.length;
        }
    }

    /**
     * Draw the polygon onto the canvas
     * @param g
     */
    void drawPolygon(Graphics g) {
        if (draw && visible) {
            g.setColor(new Color((int) (color.getRed() * lighting), (int) (color.getGreen() * lighting), (int) (color.getBlue() * lighting)));
            if (seeThrough)
                g.drawPolygon(polygon);
            else
                g.fillPolygon(polygon);
            if (Screen.drawOutlines) {
                g.setColor(new Color(0, 0, 0));
                g.drawPolygon(polygon);
            }

            if (Screen.polygonOver == this) {
                g.setColor(new Color(255, 255, 255, 100));
                g.fillPolygon(polygon);
            }
        }
    }

    /**
     * Is the mouse over the currently selected polygon?
     * @return
     */
    public boolean mouseOver() {
        return polygon.contains(Main.ScreenSize.getWidth() / 2, Main.ScreenSize.getHeight() / 2);
    }
}
