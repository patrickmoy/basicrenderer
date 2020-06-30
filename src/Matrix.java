import java.util.Arrays;

/**
 * Matrix class created for BasicPaint program for Computer Graphics course. Stores a matrix and supports matrix
 * operations.
 */
public class Matrix {

    /**
     * Stores matrix as 2D array.
     */
    double[][] myMatrix;

    /**
     * Constructor Matrix takes 2D array
     *
     * @param theMatrix 2D array of doubles that represents Matrix data
     */
    private Matrix(double[][] theMatrix) {
        this.myMatrix = theMatrix;
    }

    /**
     * Method multiplies the matrix (this) with the given matrix (multiplier)
     *
     * @param theMatrix The multiplier matrix
     *
     * @return Returns a new matrix (product of the two)
     */
    public Matrix multiplyMatrix(Matrix theMatrix) {
        double[][] result = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = 0;
                    for (int m = 0; m < 4; m++) {
                    result[i][j] += this.myMatrix[i][m] * theMatrix.myMatrix[m][j];
                }
            }
        }
        return new Matrix(result);
    }

    /**
     * Generates an identity matrix for use in transformations
     *
     * @return Returns a new matrix equal to the identity matrix
     */
    public static Matrix IDENTITY() {
        double[][] result = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = 0;
            }
        }
        result[0][0] = 1;
        result[1][1] = 1;
        result[2][2] = 1;
        result[3][3] = 1;
        return new Matrix(result);
    }

    /**
     * Multiplies the current matrix by a Point3D object
     *
     * @param thePoint Point object to multiply
     * @return Returns a new Point3D as a product
     */
    public Point3D multiplyPoint(Point3D thePoint) {
        double[] result = new double[4];
        // 4D Point
        double[] vector = new double[]{thePoint.getX(), thePoint.getY(), thePoint.getZ(), 1};
        for (int i = 0; i < 4; i++) {
            result[i] = 0;
            for (int j = 0; j < vector.length; j++) {
                result[i] += this.myMatrix[i][j] * vector[j];
            }
        }
        return new Point3D(result);
    }

    /**
     * Generates a Matrix for rotations along the x-axis
     *
     * @param angle Angle of rotation
     * @return Returns a matrix that will transform with x-rotation at the given angle
     */
    public static Matrix rotateX(double angle) {
        Matrix result = Matrix.IDENTITY();
        result.myMatrix[1][1] = Math.cos(angle);
        result.myMatrix[1][2] = -Math.sin(angle);
        result.myMatrix[2][1] = Math.sin(angle);
        result.myMatrix[2][2] = Math.cos(angle);
        return result;
    }

    /**
     * Generates a Matrix for rotations along the y-axis
     *
     * @param angle Angle of rotation
     * @return Returns a matrix that will transform with y-rotation at the given angle
     */
    public static Matrix rotateY(double angle) {
        Matrix result = Matrix.IDENTITY();
        result.myMatrix[0][0] = Math.cos(angle);
        result.myMatrix[0][2] = Math.sin(angle);
        result.myMatrix[2][0] = -Math.sin(angle);
        result.myMatrix[2][2] = Math.cos(angle);
        return result;
    }

    /**
     * Generates a Matrix for rotations along the z-axis
     *
     * @param angle Angle of rotation
     * @return Returns a matrix that will transform with z-rotation at the given angle
     */
    public static Matrix rotateZ(double angle) {
        Matrix result = Matrix.IDENTITY();
        result.myMatrix[0][0] = Math.cos(angle);
        result.myMatrix[0][1] = -Math.sin(angle);
        result.myMatrix[1][0] = Math.sin(angle);
        result.myMatrix[1][1] = Math.cos(angle);
        return result;
    }

    /**
     * Generates a matrix for a translation
     * @param x Shift in x
     * @param y Shift in y
     * @param z Shift in z
     * @return Returns a transformation matrix that translates by the given x, y, z
     */
    public static Matrix translation(double x, double y, double z) {
        Matrix result = Matrix.IDENTITY();
        result.myMatrix[0][3] = x;
        result.myMatrix[1][3] = y;
        result.myMatrix[2][3] = z;
        return result;
    }

    /**
     * Generates a transformation matrix for a scaling transformation
     * @param x Factor for x scaling
     * @param y Factor for y scaling
     * @param z Factor for z scaling
     * @return Returns a transformation matrix for scaling by x, y, z
     */
    public static Matrix scaling(double x, double y, double z) {
        Matrix result = Matrix.IDENTITY();
        result.myMatrix[0][0] = x;
        result.myMatrix[1][1] = y;
        result.myMatrix[2][2] = z;
        return result;
    }

    @Override
    public String toString() {
        String output = "[";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                output += this.myMatrix[i][j] + " ";
            }
            output += "\n";
        }
        output += "]";
        return output;
    }
}
