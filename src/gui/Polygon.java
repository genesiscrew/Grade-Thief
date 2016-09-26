package gui;

import java.awt.*;

public class Polygon {
    public double[] x, y, z;
    boolean draw = true;
    boolean seeThrough = false;
    double[] CalcPos, newX, newY;
   // UnusedPolygonObject drawablePolygon;
    double averageDistance;
    java.awt.Polygon polygon;
    Color color;
    boolean visible = true;
    double lighting = 1;

    public Polygon(double[] x, double[] y, double[] z, Color c, boolean seeThrough) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = c;
        this.seeThrough = seeThrough;
        createPolygon();
    }

    void createPolygon() {
        polygon = new java.awt.Polygon();
        for (int i = 0; i < x.length; i++)
            polygon.addPoint((int) x[i], (int) y[i]);
    }

    void updatePolygon(Screen screen) {
        newX = new double[x.length];
        newY = new double[x.length];
        draw = true;
        for (int i = 0; i < x.length; i++) {
            CalcPos = Calculator.CalculatePositionP(screen.ViewFrom, x[i], y[i], z[i]);
            newX[i] = (Main.ScreenSize.getWidth() / 2 - Calculator.calculatorFocusPosition[0]) + CalcPos[0] * Screen.zoom;
            newY[i] = (Main.ScreenSize.getHeight() / 2 - Calculator.calculatorFocusPosition[1]) + CalcPos[1] * Screen.zoom;
            if (Calculator.t < 0)
                draw = false;
        }

        calcLighting(screen);
        updatePolygon(newX, newY);
        averageDistance = GetDist(screen);
    }

    void calcLighting(Screen screen) {
        Plane lightingPlane = new Plane(this);
        double angle = Math.acos(((lightingPlane.normalVector.x * screen.LightDir[0]) +
                (lightingPlane.normalVector.y * screen.LightDir[1]) + (lightingPlane.normalVector.z * screen.LightDir[2]))
                / (Math.sqrt(screen.LightDir[0] * screen.LightDir[0] + screen.LightDir[1] * screen.LightDir[1] + screen.LightDir[2] *screen.LightDir[2])));

        lighting = 0.2 + 1 - Math.sqrt(Math.toDegrees(angle) / 180);

        if (lighting > 1)
            lighting = 1;
        if (lighting < 0)
            lighting = 0;
    }

    /**
     * Calculates the average distance to the viewer
     * @return
     */
    double GetDist(Screen screen) {
        double total = 0;
        for (int i = 0; i < x.length; i++)
            total += GetDistanceToP(i, screen);
        return total / x.length;
    }

    double GetDistanceToP(int i, Screen screen) {
        return Math.sqrt((screen.ViewFrom[0] - x[i]) * (screen.ViewFrom[0] - x[i]) +
                (screen.ViewFrom[1] - y[i]) * (screen.ViewFrom[1] - y[i]) +
                (screen.ViewFrom[2] - z[i]) * (screen.ViewFrom[2] - z[i]));
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
