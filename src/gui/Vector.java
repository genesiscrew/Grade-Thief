package gui;

/**
 * This is a single vector used to assist rendering, its made up of three values being x, y and z
 * @Author Adam Wareing
 */
public class Vector {
    double x, y, z;

    /**
     * Make a new Vector and normalise it so we can perform calculations on it later.
     * @param x
     * @param y
     * @param z
     */
    public Vector(double x, double y, double z) {
        double length = Math.sqrt(x * x + y * y + z * z);

        if (length > 0) {
            this.x = x / length;
            this.y = y / length;
            this.z = z / length;
        }
    }

    /**
     * Return the cross product of this and vector v.
     * The cross product gives us the normal of the two vectors.
     * @param v
     * @return
     */
    public Vector crossProduct(Vector v) {
        Vector crossVector = new Vector(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x);
        return crossVector;
    }
}
