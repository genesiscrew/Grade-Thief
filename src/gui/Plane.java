package gui;

/**
 * This is a plane which is used to assist with rendering.
 *
 */
public class Plane {
    private Vector vector1, vector2;
    public Vector normalVector;
    private double[] viewTo = new double[3];

    /**
     * Create a new plane
     * @param DP
     */
    public Plane(Polygon DP) {
        viewTo[0] = DP.x[0];
        viewTo[1] = DP.y[0];
        viewTo[2] = DP.z[0];

        vector1 = new Vector(DP.x[1] - DP.x[0],
                DP.y[1] - DP.y[0],
                DP.z[1] - DP.z[0]);

        vector2 = new Vector(DP.x[2] - DP.x[0],
                DP.y[2] - DP.y[0],
                DP.z[2] - DP.z[0]);

        normalVector = vector1.CrossProduct(vector2);
    }

    /**
     * Alternative constructor that takes in two vectors and array of doubles being the z co-ordinates
     * @param v1
     * @param v2
     * @param viewTo
     */
    public Plane(Vector v1, Vector v2, double[] viewTo) {
        this.viewTo = viewTo;
        vector1 = v1;
        vector2 = v2;
        normalVector = vector1.CrossProduct(vector2);
    }

    public double[] getViewTo() {
        return viewTo;
    }
}
