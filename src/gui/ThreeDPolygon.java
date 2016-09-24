package gui;

import java.awt.Color;

public class ThreeDPolygon {
    Color c;
    double[] x, y, z;
    boolean draw = true, seeThrough = false;
    double[] CalcPos, newX, newY;
    PolygonObject drawablePolygon;
    double averageDistance;

    public ThreeDPolygon(double[] x, double[] y, double[] z, Color c, boolean seeThrough) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.c = c;
        this.seeThrough = seeThrough;
        createPolygon();
    }

    void createPolygon() {
        drawablePolygon = new PolygonObject(new double[x.length], new double[x.length], c, seeThrough);
    }

    void updatePolygon(Screen screen) {
        newX = new double[x.length];
        newY = new double[x.length];
        draw = true;
        for (int i = 0; i < x.length; i++) {
            CalcPos = Calculator.CalculatePositionP(screen.ViewFrom, screen.ViewTo, x[i], y[i], z[i]);
            newX[i] = (Main.ScreenSize.getWidth() / 2 - Calculator.CalcFocusPos[0]) + CalcPos[0] * Screen.zoom;
            newY[i] = (Main.ScreenSize.getHeight() / 2 - Calculator.CalcFocusPos[1]) + CalcPos[1] * Screen.zoom;
            if (Calculator.t < 0)
                draw = false;
        }

        calcLighting(screen);

        drawablePolygon.draw = draw;
        drawablePolygon.updatePolygon(newX, newY);
        averageDistance = GetDist(screen);
    }

    void calcLighting(Screen screen) {
        Plane lightingPlane = new Plane(this);
        double angle = Math.acos(((lightingPlane.NV.x * screen.LightDir[0]) +
                (lightingPlane.NV.y * screen.LightDir[1]) + (lightingPlane.NV.z * screen.LightDir[2]))
                / (Math.sqrt(screen.LightDir[0] * screen.LightDir[0] + screen.LightDir[1] * screen.LightDir[1] + screen.LightDir[2] *screen.LightDir[2])));

        drawablePolygon.lighting = 0.2 + 1 - Math.sqrt(Math.toDegrees(angle) / 180);

        if (drawablePolygon.lighting > 1)
            drawablePolygon.lighting = 1;
        if (drawablePolygon.lighting < 0)
            drawablePolygon.lighting = 0;
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
}
