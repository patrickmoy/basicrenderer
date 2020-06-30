# BasicRenderer

Author: Patrick Moy

Developed for TCSS458 Computer Graphics at UWT with Professor John Mayer.

BasicRenderer takes text input with commands and renders 3D images based on input. It implements z-buffering and
supports scaling, translating, and rotating transfromations to modify basic geometric shapes.

# Commands
The commands for the input are listed as follows(number of parameters in parentheses):

DIM(2): Required. Takes dimensions of desired viewing window in pixels.
Example - DIM 512 1024

RGB(3): Color to use when drawing. Persists until another RGB command is given. Range from 0 to 1 (proportion of 255)
Example - RGB 1.0 0.0 0.0

LINE(6): Endpoints of a line to draw. Endpoints are 3D points, given as X, Y, Z, in world coordinates [-1, 1]
Example - LINE -1.0 -1.0 0 1.0 1.0 0

TRI (9): Like LINE, this takes the 3 vertices of a triangle as 3D points in world coordinates [-1, 1]
Example - TRI 0 0 0 0.1 0.1 0.1 0.1 0 0.1

LOAD_IDENTITY_MATRIX: Nullifies all previous transformation commands and allows for the creation of a new set.
Transformations apply only to objects created after matrices are input.

TRANSLATE (3): Applies a translation to the current transformation matrix, which will affect the objects created
afterwards. Takes 3 doubles giving the displacement along x, y, and z axes.
Example - TRANSLATE 0.3 0.4 0.1

ROTATEX (1): See also ROTATEY and ROTATEZ. Takes one argument - the degrees to rotate along the given axis (x, y, z).
Example - ROTATEX 45

ROTATEY (1): See ROTATEX and ROTATEZ

ROTATEZ (1): See ROTATEZ and ROTATEY

SCALE (3): Scaling transformation along the x, y, z axes. Takes 3 parameters - the factors for each axis.
Example - SCALE 0.5 0.5 0.5

WIREFRAME_CUBE: See SOLID_CUBE. Creates a cube centered at the origin, with edge length of 1. Solid color.

SOLID_CUBE: See WIREFRAME_CUBE. Creates a wire cube centered at the origin, with edge length of 1. Wireframe.

Here is a sample input file:
DIM 512 1024
RGB 1.0 0 0
LOAD_IDENTITY_MATRIX
SCALE 0.2 0.4 0.6
ROTATEX 45
WIREFRAME_CUBE
LINE 0 0 0 0.1 0.3 0.4
TRI 0 0 0 0.1 0.1 0.1 0.1 0 0.1
LOAD_IDENTITY _MATRIX
SCALE 0.5 0.5 0.5
TRANSLATE 0.2 0.2 0.3
WIREFRAME_CUBE

Some sample inputs are available in the inputs folder.

# HOW TO RUN

Download and run Renderer.jar. When prompted, select a text file with the desired input. Alternatively, open the src
directory in any Java IDE and run main in BasicRender.java.

# ACKNOWLEDGEMENTS

Most test files (Simple, Complex, temple, ROBOT) providied by instructor John Mayer.