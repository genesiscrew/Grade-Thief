package view;

import model.rendering.Plane;
import model.rendering.Vector;

/**
 * This performs calculations for the 3D graphics using vectors.
 *
 */
public class Calculator {
	public static double t = 0;
    private static Vector w1;
    private static Vector w2;
    private static Vector viewVector; // The vector we are looking along (viewTo - viewFrom)
    private static Vector rotationVector;
    private static Vector directionVector = new Vector(1, 1, 1);
    private static Plane plane;
    public static double[] calculatorFocusPosition = new double[2];

    /**
     * Get the projection for point x,y,z from where the player is viewing from
     * @param ViewFrom
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static double[] calculateProjectionFromPosition(double[] ViewFrom, double x, double y, double z) {
        double[] projP = getProjection(ViewFrom, x, y, z, plane);
        double[] drawP = getDrawP(projP[0], projP[1], projP[2]);
        return drawP;
    }

    /**
     * Get the vector the player is looking from to the
     *
     * @param viewFrom
     * @param x
     * @param y
     * @param z
     * @param plane
     * @return array of length 3 [x,y,z].
     */
    static double[] getProjection(double[] viewFrom, double x, double y, double z, Plane plane) {
        // Vector from current view to the point we are trying to calculate
        Vector viewToPoint = new Vector(x - viewFrom[0], y - viewFrom[1], z - viewFrom[2]);
        double[] viewTo = plane.getViewTo();

        // The distance from viewTo to the intersection point
        t = (plane.normalVector.x * viewTo[0] + plane.normalVector.y * viewTo[1] + plane.normalVector.z * viewTo[2]
                - (plane.normalVector.x * viewFrom[0] + plane.normalVector.y * viewFrom[1] + plane.normalVector.z * viewFrom[2]))
                / (plane.normalVector.x * viewToPoint.x + plane.normalVector.y * viewToPoint.y + plane.normalVector.z * viewToPoint.z);

        x = viewFrom[0] + viewToPoint.x * t;
        y = viewFrom[1] + viewToPoint.y * t;
        z = viewFrom[2] + viewToPoint.z * t;

        return new double[]{x, y, z};
    }

    /**
     * Convert a 3d point to 2D form (x and y point) by scaling it
     * @param x
     * @param y
     * @param z
     * @return [x,y] point
     */
    static double[] getDrawP(double x, double y, double z) {
        double DrawX = w2.x * x + w2.y * y + w2.z * z;
        double DrawY = w1.x * x + w1.y * y + w1.z * z;
        return new double[]{DrawX, DrawY};
    }

    /**
     * Calculates the rotation
     * @param viewFrom
     * @param viewTo
     * @return
     */
    public static Vector getRotationVector(double[] viewFrom, double[] viewTo) {
        double dx = Math.abs(viewFrom[0] - viewTo[0]);
        double dy = Math.abs(viewFrom[1] - viewTo[1]);
        double xRot, yRot;
        xRot = dy / (dx + dy);
        yRot = dx / (dx + dy);

        if (viewFrom[1] > viewTo[1])
            xRot = -xRot;
        if (viewFrom[0] < viewTo[0])
            yRot = -yRot;

        return new Vector(xRot, yRot, 0);
    }

    /**
     * Set the information from screen for the current view from and view to. This also adjusts the plane, rotation vector
     * and zoom levels
     *
     * @param screen
     */
    public static void setPredeterminedInfo(Screen screen) {

        viewVector = new Vector(screen.viewTo[0] - screen.viewFrom[0], screen.viewTo[1] - screen.viewFrom[1], screen.viewTo[2] - screen.viewFrom[2]);
        Vector planeVector1 = viewVector.crossProduct(directionVector);
        Vector planeVector2 = viewVector.crossProduct(planeVector1);
        plane = new Plane(planeVector1, planeVector2, screen.viewTo);

        rotationVector = Calculator.getRotationVector(screen.viewFrom, screen.viewTo);
        w1 = viewVector.crossProduct(rotationVector);
        w2 = viewVector.crossProduct(w1);

        calculatorFocusPosition = Calculator.calculateProjectionFromPosition(screen.viewFrom, screen.viewTo[0], screen.viewTo[1], screen.viewTo[2]);
        calculatorFocusPosition[0] = Screen.zoom * calculatorFocusPosition[0];
        calculatorFocusPosition[1] = Screen.zoom * calculatorFocusPosition[1];
    }

    /**
     * Sets the the new values for the light direction
     */
    public static double[] controlSunAndLight(double[] lightDir, double mapSize, double sunPos) {
        lightDir[0] = mapSize / 2 - (mapSize / 2 + Math.cos(sunPos) * mapSize * 10);
        lightDir[1] = mapSize / 2 - (mapSize / 2 + Math.sin(sunPos) * mapSize * 10);
        lightDir[2] = -200;
        return lightDir;
    }
}
