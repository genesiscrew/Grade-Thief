package view;

/**
 * This is a simple plane which consists of the two vectors that make up the plane.
 * @Author Adam Wareing
 */
public class Plane {
    private Vector vector1, vector2;
    public Vector normalVector;
    private double[] viewTo = new double[3];

    /**
     * Create a new plane
     * @param polygon
     */
    public Plane(Polygon polygon) {
        viewTo[0] = polygon.x[0];
        viewTo[1] = polygon.y[0];
        viewTo[2] = polygon.z[0];

        vector1 = new Vector(polygon.x[1] - polygon.x[0],
                polygon.y[1] - polygon.y[0],
                polygon.z[1] - polygon.z[0]);

        vector2 = new Vector(polygon.x[2] - polygon.x[0],
                polygon.y[2] - polygon.y[0],
                polygon.z[2] - polygon.z[0]);

        normalVector = vector1.crossProduct(vector2);
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
        normalVector = vector1.crossProduct(vector2);
    }

    public double[] getViewTo() {
        return viewTo;
    }
}
