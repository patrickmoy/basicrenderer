/**
 * @author Patrick Moy
 * Created using starter code provided by Professor John Mayer.
 * Code for Bresenham's line algorithm borrowed from Wikipedia's derivation
 * and implementation.
 *
 * Implemented features: 3D rendering, translation/scaling/rotating, wireframe cubes, solid cubes, z-buffer)
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import javax.swing.*;

import java.io.*;
import java.util.*;

/**
 * Paint class that creates images based on input.
 */
public class BasicRenderer extends JPanel {

    /**
     * Width of image in pixels.
     */
    static int width;

    /**
     * Height of image in pixels.
     */
    static int height;

    /**
     * Constant points for creation of a basic cube (pre-transformation)
     */
    static final Point3D[] cubePoints = new Point3D[]{
            new Point3D(-0.5, 0.5, 0.5), new Point3D(0.5, 0.5, 0.5),
            new Point3D(-0.5, -0.5, 0.5), new Point3D(0.5, -0.5, 0.5),
            new Point3D(-0.5, 0.5, -0.5), new Point3D(0.5, 0.5, -0.5),
            new Point3D(-0.5, -0.5, -0.5), new Point3D(0.5, -0.5, -0.5)
    };

    /**
     * Image size (total pixels, or w * h).
     */
    int imageSize;

    /**
     * Array storing pixels generated from input.
     */
    int[] pixels;

    /**
     * Color values in RGB format (0-255).
     */
    int r, g, b;

    /**
     * zBuffer array.
     */
    double[] zBuffer;

    Matrix CTM = Matrix.IDENTITY();

    Matrix RTM = Matrix.IDENTITY();

    static final double POSITIVE_ANGLE = Math.toRadians(3.0);

    static final double NEGATIVE_ANGLE = Math.toRadians(-3.0);

    ArrayList<Point3D> scanLinePoints = new ArrayList<Point3D>();

    /**
     * Stores a pixel given the coordinates of the pixel and the color.
     * @param x x-coordinate of the pixel.
     * @param y y-coordinate of the pixel.
     * @param r Red value of pixel.
     * @param g Green value of pixel.
     * @param b Blue value of pixel.
     */
    void drawPixel(double x, double y, double z, int r, int g, int b) {
//        int xPixel = (int) Math.round(x);
//        int yPixel = (int) Math.round(y);


        // int zPixel = xPixel + width * yPixel;
        int zPixel = (int) Math.round(x + width * y);

        if (zBuffer[zPixel] < z) {
            zBuffer[zPixel] = z;


            pixels[(int) ((height - y - 1) * width * 3 + x * 3)] = r;
            pixels[(int) ((height - y - 1) * width * 3 + x * 3 + 1)] = g;
            pixels[(int) ((height - y - 1) * width * 3 + x * 3 + 2)] = b;
        }
    }

    /**
     * Creates image from input.
     */
    void createImage() {

        // Input from file.
        Scanner input = getFile();

        CTM = Matrix.IDENTITY(); // Reset CTM to identity matrix before redrawing an image.

        while (input.hasNext()) {


            String command = input.next();
            scanLinePoints.clear();
            if (command.equals("DIM")) {
                width = input.nextInt();
                height = input.nextInt();

                imageSize = width * height;
                zBuffer = new double[imageSize];
                pixels = new int[imageSize * 3];

                // Color background white.
                for (int i = 0; i < pixels.length; i++) {
                    pixels[i] = 255;
                }

                for (int i = 0; i < zBuffer.length; i++) {
                    zBuffer[i] = Double.NEGATIVE_INFINITY;
                }

            } else if (command.equals("LINE")) {

                Point3D pointOne = getNextPoint(input);
                Point3D pointTwo = getNextPoint(input);
                Point3D newOne = newLineRawHelper(pointOne);
                Point3D newTwo = newLineRawHelper(pointTwo);
                newLine(newOne, newTwo);

            } else if (command.equals("TRI")) {


                Point3D pointOne, pointTwo, pointThree;
                pointOne = getNextPoint(input);
                pointTwo = getNextPoint(input);
                pointThree = getNextPoint(input);
                newTriangle(pointOne, pointTwo, pointThree);


            } else if (command.equals("RGB")) {

                // Stores colors from input.
                r = (int) Math.round(input.nextDouble() * 255);
                g = (int) Math.round(input.nextDouble() * 255);
                b = (int) Math.round(input.nextDouble() * 255);

            } else if (command.equals("LOAD_IDENTITY_MATRIX")) {
                CTM = Matrix.IDENTITY();

            } else if (command.equals("TRANSLATE")) {

                double x = input.nextDouble();
                double y = input.nextDouble();
                double z = input.nextDouble();
                CTM = Matrix.translation(x, y, z).multiplyMatrix(CTM);

            } else if (command.equals("ROTATEX")) {

                double angle = input.nextDouble();
                angle = Math.toRadians(angle);
                CTM = Matrix.rotateX(angle).multiplyMatrix(CTM);

            } else if (command.equals("ROTATEY")) {

                double angle = input.nextDouble();
                angle = Math.toRadians(angle);
                CTM = Matrix.rotateY(angle).multiplyMatrix(CTM);

            } else if (command.equals("ROTATEZ")) {

                double angle = input.nextDouble();
                angle = Math.toRadians(angle);
                CTM = Matrix.rotateZ(angle).multiplyMatrix(CTM);

            } else if (command.equals("SCALE")) {
                double x = input.nextDouble();
                double y = input.nextDouble();
                double z = input.nextDouble();
                CTM = Matrix.scaling(x, y, z).multiplyMatrix(CTM);

            } else if (command.equals("WIREFRAME_CUBE")) {
                Point3D[] processedCubes = new Point3D[8];
                for (int i = 0; i < processedCubes.length; i++) {
                    processedCubes[i] = newLineRawHelper(cubePoints[i]);
                }

                newLine(processedCubes[0], processedCubes[1]);
                newLine(processedCubes[0], processedCubes[2]);
                newLine(processedCubes[0], processedCubes[4]);

                newLine(processedCubes[1], processedCubes[3]);
                newLine(processedCubes[1], processedCubes[5]);

                newLine(processedCubes[2], processedCubes[3]);
                newLine(processedCubes[2], processedCubes[6]);

                newLine(processedCubes[3], processedCubes[7]);

                newLine(processedCubes[4], processedCubes[5]);
                newLine(processedCubes[4], processedCubes[6]);

                newLine(processedCubes[5], processedCubes[7]);

                newLine(processedCubes[6], processedCubes[7]);

            } else if (command.equals("SOLID_CUBE")) {

                newTriangle(cubePoints[0], cubePoints[1], cubePoints[2]);
                newTriangle(cubePoints[0], cubePoints[2], cubePoints[4]);
                newTriangle(cubePoints[0], cubePoints[1], cubePoints[4]);

                newTriangle(cubePoints[1], cubePoints[2], cubePoints[3]);
                newTriangle(cubePoints[1], cubePoints[3], cubePoints[5]);
                newTriangle(cubePoints[1], cubePoints[4], cubePoints[5]);

                newTriangle(cubePoints[2], cubePoints[3], cubePoints[6]);
                newTriangle(cubePoints[2], cubePoints[4], cubePoints[6]);

                newTriangle(cubePoints[3], cubePoints[5], cubePoints[7]);
                newTriangle(cubePoints[3], cubePoints[6], cubePoints[7]);

                newTriangle(cubePoints[4], cubePoints[5], cubePoints[6]);

                newTriangle(cubePoints[5], cubePoints[6], cubePoints[7]);

            }
        }
    }
    /**
     * Substituted/overriden (I surmise?) method to draw on JPanel. Part of starter code.
     *
     * @param g Graphics object G.
     */
    public void paintComponent(Graphics g) {
        createImage();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster wr_raster = image.getRaster();
        wr_raster.setPixels(0, 0, width, height, pixels);
        g.drawImage(image, 0, 0, null);
    }

    /**
     * Draws a line between two points, given x and y coordinates for both points.
     * Based on Bresenham's algorithm, borrowed from Wikipedia's page on algorithm.
     *
     */
    public void newLine(Point3D pointOne, Point3D pointTwo) {

        if (Math.abs(pointTwo.getY() - pointOne.getY()) < Math.abs(pointTwo.getX() - pointOne.getX())) {
            if (pointOne.getX() > pointTwo.getX()) {
                drawLineGradual(pointTwo, pointOne);
            } else {
                drawLineGradual(pointOne, pointTwo);
            }
        } else {
            if (pointOne.getY() > pointTwo.getY()) {
                drawLineSteep(pointTwo, pointOne);
            } else {
                drawLineSteep(pointOne, pointTwo);
            }
        }
    }

    /**
     * Converts and transforms world points before passing to newLine method.
     *
     * @param point Point to be matrix multiplied and then converted to screen coordinates
     * @return Returns Point3D object that is in screen coordinates and has been transformed.
     */
    public Point3D newLineRawHelper(Point3D point) {
        Matrix newRTM = RTM.multiplyMatrix(CTM);
        Point3D newPoint = newRTM.multiplyPoint(point);
        newPoint = remapPoint(newPoint);
        return newPoint;
    }

    public void newTriangle(Point3D pointOne, Point3D pointTwo, Point3D pointThree) {
        Point3D newOne = newLineRawHelper(pointOne);
        Point3D newTwo = newLineRawHelper(pointTwo);
        Point3D newThree = newLineRawHelper(pointThree);
        newLine(newOne, newTwo);
        newLine(newTwo, newThree);
        newLine(newOne, newThree);
        scanLineFill(scanLinePoints);
    }

    /**
     * Helper method for Bresenham's line algorithm, drawing a line that is not steep,
     * or in other words, dx is greater than dy.
     *
     */
    public void drawLineGradual(Point3D pointOne, Point3D pointTwo) {
        double dx = pointTwo.getX() -  pointOne.getX();
        double dy = pointTwo.getY() -  pointOne.getY();
        int yIncrement = 1;
        if (dy < 0) {
            yIncrement = -1;
            dy*= -1;
        }
        double D = 2 * dy - dx;
        double y = pointOne.getY();

        double z = pointOne.getZ();
        double dz = pointTwo.getZ() - pointOne.getZ();
        double zIncrement = dz / dx;

        for (double i = pointOne.getX(); i <= pointTwo.getX(); i++) {
            drawPixel(i, y, z, r, g, b); // Draws pixel for that x.

            scanLinePoints.add(new Point3D(i, y, z));

            z = z + zIncrement;

            if (D > 0) {
                y+= yIncrement;
                D-= 2 * dx;
            }
            D+= 2 * dy;
        }
    }

    /**
     * Helper method for Bresenham's line algorithm, drawing a line that is steep,
     * or in other words, dy is greater than dx.
     *
     */
    public void drawLineSteep(Point3D pointOne, Point3D pointTwo) {
        double dx = pointTwo.getX() -  pointOne.getX();
        double dy = pointTwo.getY() -  pointOne.getY();
        int xIncrement = 1;
        if (dx < 0) {
            xIncrement = -1;
            dx*= -1;
        }
        double D = 2 * dx - dy;
        double x = pointOne.getX();

        double z = pointOne.getZ();
        double dz = pointTwo.getZ() - pointOne.getZ();
        double zIncrement = dz / (dy);

        for (double i = pointOne.getY(); i <= pointTwo.getY(); i++) {
            drawPixel(x, i, z, r, g, b); // Draws pixel for that y.
            scanLinePoints.add(new Point3D(x, i, z));

            z = z + zIncrement;
            if (D > 0) {
                x+= xIncrement;
                D-= 2 * dy;
            }
            D+= 2 * dx;
        }
    }

    /**
     * Fills triangle using scanline algorithm. Each array within the array contains
     * x values of points that are on the edges of the triangle. Iterating over values of y,
     * which are represented by i, draw a line between values of x.
     *
     * @param scanLinePoints Array of arrays of scanline points.
     */
    public void scanLineFill(ArrayList<Point3D> scanLinePoints) {
        if (!scanLinePoints.isEmpty()) {
            Collections.sort(scanLinePoints, Point3D.compareY());
            ArrayList<Point3D> currentRow = new ArrayList<Point3D>();
            double current = scanLinePoints.get(0).getY();
            for (int i = 0; i < scanLinePoints.size(); i++) {
                if (current != scanLinePoints.get(i).getY() || (i + 1 >= scanLinePoints.size())) {
                    if (currentRow.size() > 1) {
                        Collections.sort(currentRow, Point3D.compareX());
                        Point3D pointOne = currentRow.get(0);
                        Point3D pointTwo = currentRow.get(currentRow.size() - 1);
                        scanLineFillHelper(pointOne, pointTwo);
                        // Using the newLine method (line 388) seems to drastically slow rotation, presumably because
                        // the extra math checks to decide which sub-method of Bresenham's to use must be done for
                        // every single y-value? Using a "simpler" helper method that is similar to newLine seems to
                        // help with performance, but I'm not sure if that's problematic in terms of pixel accuracy.
                        // Will consider alternative solution but for now I'll settle for this.

                        // newLine(pointOne, pointTwo);
                    }
                    current = scanLinePoints.get(i).getY();
                    currentRow.clear();
                    currentRow.add(scanLinePoints.get(i));
                } else {
                    currentRow.add(scanLinePoints.get(i));
                }
            }
            scanLinePoints.clear();
        }
    }

    /**
     * Helper method for scanline algorithm. Draws a single line between two points using scanline algorithm.
     *
     * @param pointOne First point for the limit of a scanline
     * @param pointTwo Second point for the limit of a scanline
     */
    public void scanLineFillHelper(Point3D pointOne, Point3D pointTwo) {
        double z = pointOne.getZ();
        double dx = pointTwo.getX() -  pointOne.getX();
        // dy = 0;
        double dz = pointTwo.getZ() - z;
        double slope = dz / dx;

        for (double i = pointOne.getX(); i < pointTwo.getX(); i++) {
            drawPixel(i, pointOne.getY(), z, r, g, b);
            z += slope;
        }
    }

    /**
     * Main method to run TCSS458Paint program. Starter code.
     * @param args Command line arguments
     */
    public static void main(String args[]) {
        JFrame frame = new JFrame("3D RENDERER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        selectFile();

        BasicRenderer rootPane = new BasicRenderer();
        getDim(rootPane);
        rootPane.setPreferredSize(new Dimension(width, height));

        frame.getContentPane().add(rootPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // Do nothing
            }

            @Override
            public void keyPressed(KeyEvent e) {

                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    // Snippet here for diagnosing matrix math issues via keypress to print matrices to console.
//                    case KeyEvent.VK_R:
//                        System.out.println(rootPane.CTM);
//                        System.out.println(rootPane.RTM);
//                        break;
                    case KeyEvent.VK_UP:
                        rootPane.RTM = Matrix.rotateX(NEGATIVE_ANGLE).multiplyMatrix(rootPane.RTM);
                        rootPane.repaint();
                        break;
                    case KeyEvent.VK_DOWN:
                        rootPane.RTM = Matrix.rotateX(POSITIVE_ANGLE).multiplyMatrix(rootPane.RTM);
                        rootPane.repaint();
                        break;

                    case KeyEvent.VK_LEFT:
                        rootPane.RTM = Matrix.rotateY(NEGATIVE_ANGLE).multiplyMatrix(rootPane.RTM);
                        rootPane.repaint();
                        break;
                    case KeyEvent.VK_RIGHT:
                        rootPane.RTM = Matrix.rotateY(POSITIVE_ANGLE).multiplyMatrix(rootPane.RTM);
                        rootPane.repaint();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Do nothing
            }
        });
    }

    /**
     * File for input.
     */
    static File selectedFile = null;

    /**
     * Selects a file using JFileChooser. Starter code.
     */
    static private void selectFile() {
        int approve; //return value from JFileChooser indicates if the user hit cancel

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        approve = chooser.showOpenDialog(null);
        if (approve != JFileChooser.APPROVE_OPTION) {
            System.exit(0);
        } else {
            selectedFile = chooser.getSelectedFile();
        }
    }

    /**1
     * Method to get file. Starter code.
     *
     * @return Returns scanner object of file.
     */
    static private Scanner getFile() {
        Scanner input = null;
        try {
            input = new Scanner(selectedFile);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "There was an error with the file you chose.",
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return input;
    }

    /**
     * Gets dimensions of panel. Starter code.
     *
     * @param rootPane
     */
    static void getDim(JPanel rootPane) {
        Scanner input = getFile();

        String command = input.next();
        if (command.equals("DIM")) {
            width = input.nextInt();
            height = input.nextInt();
            rootPane.setPreferredSize(new Dimension(width, height));
        }
    }

    /**
     * Getter for width field of TCSS458Paint.
     *
     * @return Returns width field of TCSS458Paint.
     */
    static int getDimWidth() {
        return width;
    }

    /**
     * Getter for height field of TCSS458Paint.
     *
     * @return Returns height field of TCSS458Paint.
     */
    static int getDimHeight() {
        return height;
    }

    /**
     * Helper method to get next point from Scanner and turn into a Point3D object.
     *
     * @param theInput Scanner providing input for method
     * @return Returns a Point3D object created from a line of input
     */
    public Point3D getNextPoint(Scanner theInput) {
        return new Point3D(theInput.nextDouble(), theInput.nextDouble(), theInput.nextDouble());
    }

    /**
     * Helper function that maps world coordinate values to screen coordinates based on canvas dimensions.
     *
     * @param world World coordinate as double
     * @param dimension Dimensions of canvas
     * @return Screen coordinates as int (pixel position)
     */
    static int worldToScreen(double world, int dimension) {
        return (int) ((world + 1) * (dimension - 1) / 2);
    }

    /**
     * Method remaps points given in standard coordinates (-1 to 1) to screen coordinates(pixels)
     *
     * @param thePoint Point3D object with world coorddinates
     * @return Point3D object with screen coordinates
     */
    public Point3D remapPoint(Point3D thePoint) {
        return new Point3D(worldToScreen(thePoint.getX(), this.getDimWidth()), worldToScreen(thePoint.getY(),
                this.getDimHeight()), thePoint.getZ());
    }

}