import java.util.Comparator;

/**
 * Point3D is a class that stores a 3D point and supports comparison/sorting.
 */
public class Point3D {

    /**
     * The x, y, z coordinates of the point
     */
    double x, y, z;

    /**
     * Constructor takes x, y, and z coordinates and returns a Point3D object
     * @param x X-coordinate
     * @param y Y-coordinate
     * @param z Z-coordinate
     */
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the Point3D as an array storing the coordinates in x, y, z order.
     * @param coordinates Returns an array of doubles [x, y, z]
     */
    public Point3D(double[] coordinates) {
        this.x = coordinates[0];
        this.y = coordinates[1];
        this.z = coordinates[2];
    }

    /**
     * Getter for the x coordinate
     * @return Returns x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Getter for the y coordinate
     * @return Returns y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Getter for the z coordinate
     * @return Returns z-coordinate
     */
    public double getZ() {
        return z;
    }

    @Override
    public String toString() {
        return this.getX() + ", " + this.getY() + ", " + this.getZ();
    }

    /**
     * Comparator for comparing points by x.
     * @return Returns 0 if they are equal in x, negative if pointOne has a smaller x, and positive if pointOne has a
     * larger x
     */
    static Comparator<Point3D> compareX() {
        return new Comparator<Point3D>() {
            public int compare(Point3D pointOne, Point3D pointTwo) {
                return Double.compare(pointOne.getX(), pointTwo.getX());
            }
        };
    }

    /**
     * Comparator for comparing points by y
     * @return Returns 0 if they are equal in y, negative if pointOne has a smaller y, and positive if pointOne has a
     * larger y
     */
    static Comparator<Point3D> compareY() {
        return new Comparator<Point3D>() {
            public int compare(Point3D pointOne, Point3D pointTwo) {
                return Double.compare(pointOne.getY(), pointTwo.getY());
            }
        };
    }
}
