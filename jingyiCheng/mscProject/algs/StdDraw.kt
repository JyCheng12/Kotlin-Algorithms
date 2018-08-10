package jingyiCheng.mscProject.algs

import java.awt.BasicStroke
import java.awt.Color
import java.awt.FileDialog
import java.awt.Font
import java.awt.Graphics2D
import java.awt.Image
import java.awt.MediaTracker
import java.awt.RenderingHints
import java.awt.Toolkit

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

import java.awt.geom.Arc2D
import java.awt.geom.Ellipse2D
import java.awt.geom.GeneralPath
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D

import java.awt.image.BufferedImage
import java.awt.image.DirectColorModel
import java.awt.image.WritableRaster

import java.io.File
import java.io.IOException

import java.net.MalformedURLException
import java.net.URL

import java.util.LinkedList
import java.util.TreeSet
import javax.imageio.ImageIO

import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.KeyStroke

/**
 * The `StdDraw` class provides a basic capability for
 * creating drawings with your programs. It uses a simple graphics model that
 * allows you to create drawings consisting of points, lines, squares,
 * circles, and other geometric shapes in a window on your computer and
 * to save the drawings to a file. Standard drawing also includes
 * facilities for text, color, pictures, and animation, along with
 * user interaction via the keyboard and mouse.
 *
 *
 * Now, type the following short program into your editor:
 * <pre>
 * public class TestStdDraw {
 * public static void main(String[] args) {
 * StdDraw.setPenRadius(0.05);
 * StdDraw.setPenColor(StdDraw.BLUE);
 * StdDraw.point(0.5, 0.5);
 * StdDraw.setPenColor(StdDraw.MAGENTA);
 * StdDraw.line(0.2, 0.2, 0.8, 0.2);
 * }
 * }
</pre> *
 * If you compile and execute the program, you should see a window
 * appear with a thick magenta line and a blue point.
 * This program illustrates the two main types of methods in standard
 * drawing—methods that draw geometric shapes and methods that
 * control drawing parameters.
 * The methods `StdDraw.line()` and `StdDraw.point()`
 * draw lines and points; the methods `StdDraw.setPenRadius()`
 * and `StdDraw.setPenColor()` control the line thickness and color.
 *
 *
 * **Points and lines.**
 * You can draw points and line segments with the following methods:
 *
 *  *  [.point]
 *  *  [.line]
 *
 *
 *
 * The *x*- and *y*-coordinates must be in the drawing area
 * (between 0 and 1 and by default) or the points and lines will not be visible.
 *
 *
 * **Squares, circles, rectangles, and ellipses.**
 * You can draw squares, circles, rectangles, and ellipses using
 * the following methods:
 *
 *  *  [.circle]
 *  *  [.ellipse]
 *  *  [.square]
 *  *  [.rectangle]
 *
 *
 *
 * All of these methods take as arguments the location and size of the shape.
 * The location is always specified by the *x*- and *y*-coordinates
 * of its *center*.
 * The size of a circle is specified by its radius and the size of an ellipse is
 * specified by the lengths of its semi-major and semi-minor axes.
 * The size of a square or rectangle is specified by its half-width or half-height.
 * The convention for drawing squares and rectangles is parallel to those for
 * drawing circles and ellipses, but may be unexpected to the uninitiated.
 *
 *
 * The methods above trace outlines of the given shapes. The following methods
 * draw filled versions:
 *
 *  *  [.filledCircle]
 *  *  [.filledEllipse]
 *  *  [.filledSquare]
 *  *  [.filledRectangle]
 *
 *
 *
 * **Circular arcs.**
 * You can draw circular arcs with the following method:
 *
 *  *  [.arc]
 *
 *
 *
 * The arc is from the circle centered at (*x*, *y*) of the specified radius.
 * The arc extends from angle1 to angle2. By convention, the angles are
 * *polar* (counterclockwise angle from the *x*-axis)
 * and represented in degrees. For example, `StdDraw.arc(0.0, 0.0, 1.0, 0, 90)`
 * draws the arc of the unit circle from 3 o'clock (0 degrees) to 12 o'clock (90 degrees).
 *
 *
 * **Polygons.**
 * You can draw polygons with the following methods:
 *
 *  *  [.polygon]
 *  *  [.filledPolygon]
 *
 *
 *
 * The points in the polygon are (`x[i]`, `y[i]`).
 * For example, the following code fragment draws a filled diamond
 * with vertices (0.1, 0.2), (0.2, 0.3), (0.3, 0.2), and (0.2, 0.1):
 * <pre>
 * double[] x = { 0.1, 0.2, 0.3, 0.2 };
 * double[] y = { 0.2, 0.3, 0.2, 0.1 };
 * StdDraw.filledPolygon(x, y);
</pre> *
 *
 *
 * **Pen size.**
 * The pen is circular, so that when you set the pen radius to *r*
 * and draw a point, you get a circle of radius *r*. Also, lines are
 * of thickness 2*r* and have rounded ends. The default pen radius
 * is 0.005 and is not affected by coordinate scaling. This default pen
 * radius is about 1/200 the width of the default canvas, so that if
 * you draw 100 points equally spaced along a horizontal or vertical line,
 * you will be able to see individual circles, but if you draw 200 such
 * points, the result will look like a line.
 *
 *  *  [.setPenRadius]
 *
 *
 *
 * For example, `StdDraw.setPenRadius(0.025)` makes
 * the thickness of the lines and the size of the points to be five times
 * the 0.005 default.
 * To draw points with the minimum possible radius (one pixel on typical
 * displays), set the pen radius to 0.0.
 *
 *
 * **Pen color.**
 * All geometric shapes (such as points, lines, and circles) are drawn using
 * the current pen color. By default, it is black.
 * You can change the pen color with the following methods:
 *
 *  *  [.setPenColor]
 *  *  [.setPenColor]
 *
 *
 *
 * The first method allows you to specify colors using the RGB color system.
 * This [color picker](http://johndyer.name/lab/colorpicker/)
 * is a convenient way to find a desired color.
 * The second method allows you to specify colors using the
 * [Color] data type that is discussed in Chapter 3. Until then,
 * you can use this method with one of these predefined colors in standard drawing:
 * [.BLACK], [.BLUE], [.CYAN], [.DARK_GRAY], [.GRAY],
 * [.GREEN], [.LIGHT_GRAY], [.MAGENTA], [.ORANGE],
 * [.PINK], [.RED], [.WHITE], [.YELLOW],
 * [.BOOK_BLUE], [.BOOK_LIGHT_BLUE], [.BOOK_RED], and
 * [.PRINCETON_ORANGE].
 * For example, `StdDraw.setPenColor(StdDraw.MAGENTA)` sets the
 * pen color to magenta.
 *
 *
 * **Canvas size.**
 * By default, all drawing takes places in a 512-by-512 canvas.
 * The canvas does not include the window title or window border.
 * You can change the size of the canvas with the following method:
 *
 *  *  [.setCanvasSize]
 *
 *
 *
 * This sets the canvas size to be *width*-by-*height* pixels.
 * It also erases the current drawing and resets the coordinate system,
 * pen radius, pen color, and font back to their default values.
 * Ordinarly, this method is called once, at the very beginning of a program.
 * For example, `StdDraw.setCanvasSize(800, 800)`
 * sets the canvas size to be 800-by-800 pixels.
 *
 *
 * **Canvas scale and coordinate system.**
 * By default, all drawing takes places in the unit square, with (0, 0) at
 * lower left and (1, 1) at upper right. You can change the default
 * coordinate system with the following methods:
 *
 *  *  [.setXscale]
 *  *  [.setYscale]
 *  *  [.setScale]
 *
 *
 *
 * The arguments are the coordinates of the minimum and maximum
 * *x*- or *y*-coordinates that will appear in the canvas.
 * For example, if you  wish to use the default coordinate system but
 * leave a small margin, you can call `StdDraw.setScale(-.05, 1.05)`.
 *
 *
 * These methods change the coordinate system for subsequent drawing
 * commands; they do not affect previous drawings.
 * These methods do not change the canvas size; so, if the *x*-
 * and *y*-scales are different, squares will become rectangles
 * and circles will become ellipsoidal.
 *
 *
 * **Text.**
 * You can use the following methods to annotate your drawings with text:
 *
 *  *  [.text]
 *  *  [.text]
 *  *  [.textLeft]
 *  *  [.textRight]
 *
 *
 *
 * The first two methods write the specified text in the current font,
 * centered at (*x*, *y*).
 * The second method allows you to rotate the text.
 * The last two methods either left- or right-align the text at (*x*, *y*).
 *
 *
 * The default font is a Sans Serif font with point size 16.
 * You can use the following method to change the font:
 *
 *  *  [.setFont]
 *
 *
 *
 * You use the [Font] data type to specify the font. This allows you to
 * choose the face, size, and style of the font. For example, the following
 * code fragment sets the font to Arial Bold, 60 point.
 * <pre>
 * Font font = new Font("Arial", Font.BOLD, 60);
 * StdDraw.setFont(font);
 * StdDraw.text(0.5, 0.5, "Hello, World");
</pre> *
 *
 *
 * **Images.**
 * You can use the following methods to add images to your drawings:
 *
 *  *  [.picture]
 *  *  [.picture]
 *  *  [.picture]
 *  *  [.picture]
 *
 *
 *
 * These methods draw the specified image, centered at (*x*, *y*).
 * The supported image formats are JPEG, PNG, and GIF.
 * The image will display at its native size, independent of the coordinate system.
 * Optionally, you can rotate the image a specified number of degrees counterclockwise
 * or rescale it to fit snugly inside a width-by-height bounding box.
 *
 *
 * **Saving to a file.**
 * You save your image to a file using the *File → Save* menu option.
 * You can also save a file programatically using the following method:
 *
 *  *  [.save]
 *
 *
 *
 * The supported image formats are JPEG and PNG. The filename must have either the
 * extension .jpg or .png.
 * We recommend using PNG for drawing that consist solely of geometric shapes and JPEG
 * for drawings that contains pictures.
 *
 *
 * **Clearing the canvas.**
 * To clear the entire drawing canvas, you can use the following methods:
 *
 *  *  [.clear]
 *  *  [.clear]
 *
 *
 *
 * The first method clears the canvas to white; the second method
 * allows you to specify a color of your choice. For example,
 * `StdDraw.clear(StdDraw.LIGHT_GRAY)` clears the canvas to a shade
 * of gray.
 *
 *
 * **Computer animations and double buffering.**
 * Double buffering is one of the most powerful features of standard drawing,
 * enabling computer animations.
 * The following methods control the way in which objects are drawn:
 *
 *  *  [.enableDoubleBuffering]
 *  *  [.disableDoubleBuffering]
 *  *  [.show]
 *  *  [.pause]
 *
 *
 *
 * By default, double buffering is disabled, which means that as soon as you
 * call a drawing
 * method—such as `point()` or `line()`—the
 * results appear on the screen.
 *
 *
 * When double buffering is enabled by calling [.enableDoubleBuffering],
 * all drawing takes place on the *offscreen canvas*. The offscreen canvas
 * is not displayed. Only when you call
 * [.show] does your drawing get copied from the offscreen canvas to
 * the onscreen canvas, where it is displayed in the standard drawing window. You
 * can think of double buffering as collecting all of the lines, points, shapes,
 * and text that you tell it to draw, and then drawing them all
 * *simultaneously*, upon request.
 *
 *
 * The most important use of double buffering is to produce computer
 * animations, creating the illusion of motion by rapidly
 * displaying static drawings. To produce an animation, repeat
 * the following four steps:
 *
 *  *  Clear the offscreen canvas.
 *  *  Draw objects on the offscreen canvas.
 *  *  Copy the offscreen canvas to the onscreen canvas.
 *  *  Wait for a short while.
 *
 *
 *
 * The [.clear], [.show], and [.pause] methods
 * support the first, third, and fourth of these steps, respectively.
 *
 *
 * For example, this code fragment animates two balls moving in a circle.
 * <pre>
 * StdDraw.setScale(-2, +2);
 * StdDraw.enableDoubleBuffering();
 *
 * for (double t = 0.0; true; t += 0.02) {
 * double x = Math.sin(t);
 * double y = Math.cos(t);
 * StdDraw.clear();
 * StdDraw.filledCircle(x, y, 0.05);
 * StdDraw.filledCircle(-x, -y, 0.05);
 * StdDraw.show();
 * StdDraw.pause(20);
 * }
</pre> *
 *
 *
 * **Keyboard and mouse inputs.**
 * Standard drawing has very basic support for keyboard and mouse input.
 * It is much less powerful than most user interface libraries provide, but also much simpler.
 * You can use the following methods to intercept mouse events:
 *
 *  *  [.isMousePressed]
 *  *  [.mouseX]
 *  *  [.mouseY]
 *
 *
 *
 * The first method tells you whether a mouse button is currently being pressed.
 * The last two methods tells you the *x*- and *y*-coordinates of the mouse's
 * current position, using the same coordinate system as the canvas (the unit square, by default).
 * You should use these methods in an animation loop that waits a short while before trying
 * to poll the mouse for its current state.
 * You can use the following methods to intercept keyboard events:
 *
 *  *  [.hasNextKeyTyped]
 *  *  [.nextKeyTyped]
 *  *  [.isKeyPressed]
 *
 *
 *
 * If the user types lots of keys, they will be saved in a list until you process them.
 * The first method tells you whether the user has typed a key (that your program has
 * not yet processed).
 * The second method returns the next key that the user typed (that your program has
 * not yet processed) and removes it from the list of saved keystrokes.
 * The third method tells you whether a key is currently being pressed.
 *
 *
 * **Accessing control parameters.**
 * You can use the following methods to access the current pen color, pen radius,
 * and font:
 *
 *  *  [.getPenColor]
 *  *  [.getPenRadius]
 *  *  [.getFont]
 *
 *
 *
 * These methods are useful when you want to temporarily change a
 * control parameter and reset it back to its original value.
 *
 *
 * **Corner cases.**
 * To avoid clutter, the API doesn't explicitly refer to arguments that are
 * null, infinity, or NaN.
 *
 *  *  Any method that is passed a `null` argument will throw an
 * [IllegalArgumentException].
 *  *  Except as noted in the APIs, drawing an object outside (or partly outside)
 * the canvas is permitted—however, only the part of the object that
 * appears inside the canvas will be visible.
 *  *  Except as noted in the APIs, all methods accept [Double.NaN],
 * [Double.POSITIVE_INFINITY], and [Double.NEGATIVE_INFINITY]
 * as arugments. An object drawn with an *x*- or *y*-coordinate
 * that is NaN will behave as if it is outside the canvas, and will not be visible.
 *  *  Due to floating-point issues, an object drawn with an *x*- or
 * *y*-coordinate that is way outside the canvas (such as the line segment
 * from (0.5, –) to (0.5, ) may not be visible even in the
 * part of the canvas where it should be.
 *
 *
 *
 * **Performance tricks.**
 * Standard drawing is capable of drawing large amounts of data.
 * Here are a few tricks and tips:
 *
 *  *  Use *double buffering* for static drawing with a large
 * number of objects.
 * That is, call [.enableDoubleBuffering] before
 * the sequence of drawing commands and call [.show] afterwards.
 * Incrementally displaying a complex drawing while it is being
 * created can be intolerably inefficient on many computer systems.
 *  *  When drawing computer animations, call `show()`
 * only once per frame, not after drawing each individual object.
 *  *  If you call `picture()` multiple times with the same filename,
 * Java will cache the image, so you do not incur the cost of reading
 * from a file each time.
 *
 *
 *
 * **Known bugs and issues.**
 *
 *  *  The `picture()` methods may not draw the portion of the image that is
 * inside the canvas if the center point (*x*, *y*) is outside the
 * canvas.
 * This bug appears only on some systems.
 *  *  Some methods may not draw the portion of the geometric object that is inside the
 * canvas if the *x*- or *y*-coordinates are infinite.
 * This bug appears only on some systems.
 *
 *
 *
 * **Reference.**
 * For additional documentation,
 * see [Section 1.5](https://introcs.cs.princeton.edu/15inout) of
 * *Computer Science: An Interdisciplinary Approach*
 * by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class StdDraw// singleton pattern: client can't instantiate
private constructor() : ActionListener, MouseListener, MouseMotionListener, KeyListener {
    /**
     * This method cannot be called directly.
     */
    override fun actionPerformed(e: ActionEvent) {
        val chooser = FileDialog(frame, "Use a .png or .jpg extension", FileDialog.SAVE)
        chooser.isVisible = true
        val filename = chooser.file
        if (filename != null) {
            save(chooser.directory + File.separator + chooser.file)
        }
    }

    /**
     * This method cannot be called directly.
     */
    override fun mouseClicked(e: MouseEvent) {
        // this body is intentionally left empty
    }

    /**
     * This method cannot be called directly.
     */
    override fun mouseEntered(e: MouseEvent) {
        // this body is intentionally left empty
    }

    /**
     * This method cannot be called directly.
     */
    override fun mouseExited(e: MouseEvent) {
        // this body is intentionally left empty
    }

    /**
     * This method cannot be called directly.
     */
    override fun mousePressed(e: MouseEvent) {
        synchronized(mouseLock) {
            mouseX = userX(e.x.toDouble())
            mouseY = userY(e.y.toDouble())
            isMousePressed = true
        }
    }

    /**
     * This method cannot be called directly.
     */
    override fun mouseReleased(e: MouseEvent) {
        synchronized(mouseLock) {
            isMousePressed = false
        }
    }

    /**
     * This method cannot be called directly.
     */
    override fun mouseDragged(e: MouseEvent) {
        synchronized(mouseLock) {
            mouseX = userX(e.x.toDouble())
            mouseY = userY(e.y.toDouble())
        }
    }

    /**
     * This method cannot be called directly.
     */
    override fun mouseMoved(e: MouseEvent) {
        synchronized(mouseLock) {
            mouseX = userX(e.x.toDouble())
            mouseY = userY(e.y.toDouble())
        }
    }


    /**
     * This method cannot be called directly.
     */
    override fun keyTyped(e: KeyEvent) {
        synchronized(keyLock) {
            keysTyped.addFirst(e.keyChar)
        }
    }

    /**
     * This method cannot be called directly.
     */
    override fun keyPressed(e: KeyEvent) {
        synchronized(keyLock) {
            keysDown.add(e.keyCode)
        }
    }

    /**
     * This method cannot be called directly.
     */
    override fun keyReleased(e: KeyEvent) {
        synchronized(keyLock) {
            keysDown.remove(e.keyCode)
        }
    }

    companion object {

        /**
         * The color black.
         */
        val BLACK: Color = Color.BLACK

        /**
         * The color blue.
         */
        val BLUE: Color = Color.BLUE

        /**
         * The color cyan.
         */
        val CYAN: Color = Color.CYAN

        /**
         * The color dark gray.
         */
        val DARK_GRAY: Color = Color.DARK_GRAY

        /**
         * The color gray.
         */
        val GRAY: Color = Color.GRAY

        /**
         * The color green.
         */
        val GREEN: Color = Color.GREEN

        /**
         * The color light gray.
         */
        val LIGHT_GRAY: Color = Color.LIGHT_GRAY

        /**
         * The color magenta.
         */
        val MAGENTA: Color = Color.MAGENTA

        /**
         * The color orange.
         */
        val ORANGE: Color = Color.ORANGE

        /**
         * The color pink.
         */
        val PINK: Color = Color.PINK

        /**
         * The color red.
         */
        val RED: Color = Color.RED

        /**
         * The color white.
         */
        val WHITE: Color = Color.WHITE

        /**
         * The color yellow.
         */
        val YELLOW: Color = Color.YELLOW

        /**
         * Shade of blue used in *Introduction to Programming in Java*.
         * It is Pantone 300U. The RGB values are approximately (9, 90, 166).
         */
        val BOOK_BLUE: Color = Color(9, 90, 166)

        /**
         * Shade of light blue used in *Introduction to Programming in Java*.
         * The RGB values are approximately (103, 198, 243).
         */
        val BOOK_LIGHT_BLUE: Color = Color(103, 198, 243)

        /**
         * Shade of red used in *Algorithms, 4th edition*.
         * It is Pantone 1805U. The RGB values are approximately (150, 35, 31).
         */
        val BOOK_RED: Color = Color(150, 35, 31)

        /**
         * Shade of orange used in Princeton University's identity.
         * It is PMS 158. The RGB values are approximately (245, 128, 37).
         */
        val PRINCETON_ORANGE: Color = Color(245, 128, 37)

        // default colors
        private val DEFAULT_PEN_COLOR = BLACK
        private val DEFAULT_CLEAR_COLOR = WHITE


        var penColor: Color? = null
            /**
             * Sets the pen color to the specified color.
             *
             * The predefined pen colors are
             * `StdDraw.BLACK`, `StdDraw.BLUE`, `StdDraw.CYAN`,
             * `StdDraw.DARK_GRAY`, `StdDraw.GRAY`, `StdDraw.GREEN`,
             * `StdDraw.LIGHT_GRAY`, `StdDraw.MAGENTA`, `StdDraw.ORANGE`,
             * `StdDraw.PINK`, `StdDraw.RED`, `StdDraw.WHITE`, and
             * `StdDraw.YELLOW`.
             *
             * @param color the color to make the pen
             */
            set(color) {
                if (color == null) throw IllegalArgumentException()
                field = color
                offscreen.color = penColor
            }

        // default canvas size is DEFAULT_SIZE-by-DEFAULT_SIZE
        private const val DEFAULT_SIZE = 512
        private var width = DEFAULT_SIZE
        private var height = DEFAULT_SIZE

        // default pen radius
        private const val DEFAULT_PEN_RADIUS = 0.002


        // BasicStroke stroke = new BasicStroke(scaledPenRadius);
        var penRadius: Double = 0.toDouble()
            /**
             * Sets the radius of the pen to the specified size.
             * The pen is circular, so that lines have rounded ends, and when you set the
             * pen radius and draw a point, you get a circle of the specified radius.
             * The pen radius is not affected by coordinate scaling.
             *
             * @param  radius the radius of the pen
             * @throws IllegalArgumentException if `radius` is negative
             */
            set(radius) {
                if (radius < 0) throw IllegalArgumentException("pen radius must be non-negative")
                field = radius
                val scaledPenRadius = (radius * DEFAULT_SIZE).toFloat()
                val stroke = BasicStroke(scaledPenRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
                offscreen.stroke = stroke
            }

        // show we draw immediately or wait until next show?
        private var defer = false

        // boundary of drawing canvas, 0% border
        // private static final double BORDER = 0.05;
        private const val BORDER = 0.00
        private const val DEFAULT_XMIN = 0.0
        private const val DEFAULT_XMAX = 1.0
        private const val DEFAULT_YMIN = 0.0
        private const val DEFAULT_YMAX = 1.0
        private var xmin: Double = 0.0
        private var ymin: Double = 0.0
        private var xmax: Double = 0.0
        private var ymax: Double = 0.0

        // for synchronization
        private val mouseLock = Any()
        private val keyLock = Any()

        // default font
        private val DEFAULT_FONT = Font("SansSerif", Font.PLAIN, 16)

        // current font
        private var font: Font? = null

        // double buffered graphics
        private lateinit var offscreenImage: BufferedImage
        private lateinit var onscreenImage: BufferedImage
        private lateinit var offscreen: Graphics2D
        private lateinit var onscreen: Graphics2D

        // singleton for callbacks: avoids generation of extra .class files
        private val std = StdDraw()

        // the frame for drawing to the screen
        private lateinit var frame: JFrame

        // mouse state
        private var isMousePressed = false
        private var mouseX = 0.0
        private var mouseY = 0.0

        // queue of typed key characters
        private val keysTyped = LinkedList<Char>()

        // set of key codes currently pressed down
        private val keysDown = TreeSet<Int>()

        // static initializer
        init {
            init()
        }

        /**
         * Sets the canvas (drawing area) to be *width*-by-*height* pixels.
         * This also erases the current drawing and resets the coordinate system,
         * pen radius, pen color, and font back to their default values.
         * Ordinarly, this method is called once, at the very beginning
         * of a program.
         *
         * @param  canvasWidth the width as a number of pixels
         * @param  canvasHeight the height as a number of pixels
         * @throws IllegalArgumentException unless both `canvasWidth` and
         * `canvasHeight` are positive
         */
        @JvmOverloads
        fun setCanvasSize(canvasWidth: Int = DEFAULT_SIZE, canvasHeight: Int = DEFAULT_SIZE) {
            if (canvasWidth <= 0 || canvasHeight <= 0)
                throw IllegalArgumentException("width and height must be positive")
            width = canvasWidth
            height = canvasHeight
            init()
        }

        // init
        private fun init() {
            frame.isVisible = false
            frame = JFrame()
            offscreenImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            onscreenImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            offscreen = offscreenImage.createGraphics()
            onscreen = onscreenImage.createGraphics()
            setXscale()
            setYscale()
            offscreen.color = DEFAULT_CLEAR_COLOR
            offscreen.fillRect(0, 0, width, height)
            setPenColor()
            setPenRadius()
            setFont()
            clear()

            // add antialiasing
            val hints = RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON)
            hints[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY
            offscreen.addRenderingHints(hints)

            // frame stuff
            val icon = ImageIcon(onscreenImage)
            val draw = JLabel(icon)

            draw.addMouseListener(std)
            draw.addMouseMotionListener(std)

            frame.contentPane = draw
            frame.addKeyListener(std)    // JLabel cannot get keyboard focus
            frame.isResizable = false
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE            // closes all windows
            // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // closes only current window
            frame.title = "Standard Draw"
            frame.jMenuBar = createMenuBar()
            frame.pack()
            frame.requestFocusInWindow()
            frame.isVisible = true
        }

        // create the menu bar (changed to private)
        private fun createMenuBar(): JMenuBar {
            val menuBar = JMenuBar()
            val menu = JMenu("File")
            menuBar.add(menu)
            val menuItem1 = JMenuItem(" Save...   ")
            menuItem1.addActionListener(std)
            menuItem1.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    Toolkit.getDefaultToolkit().menuShortcutKeyMask)
            menu.add(menuItem1)
            return menuBar
        }

        /**
         * Sets the *x*-scale and *y*-scale to be the default
         * (between 0.0 and 1.0).
         */
        fun setScale() {
            setXscale()
            setYscale()
        }

        /**
         * Sets the *x*-scale to the specified range.
         *
         * @param  min the minimum value of the *x*-scale
         * @param  max the maximum value of the *x*-scale
         * @throws IllegalArgumentException if `(max == min)`
         */
        @JvmOverloads
        fun setXscale(min: Double = DEFAULT_XMIN, max: Double = DEFAULT_XMAX) {
            val size = max - min
            if (size == 0.0) throw IllegalArgumentException("the min and max are the same")
            synchronized(mouseLock) {
                xmin = min - BORDER * size
                xmax = max + BORDER * size
            }
        }

        /**
         * Sets the *y*-scale to the specified range.
         *
         * @param  min the minimum value of the *y*-scale
         * @param  max the maximum value of the *y*-scale
         * @throws IllegalArgumentException if `(max == min)`
         */
        @JvmOverloads
        fun setYscale(min: Double = DEFAULT_YMIN, max: Double = DEFAULT_YMAX) {
            val size = max - min
            if (size == 0.0) throw IllegalArgumentException("the min and max are the same")
            synchronized(mouseLock) {
                ymin = min - BORDER * size
                ymax = max + BORDER * size
            }
        }

        /**
         * Sets both the *x*-scale and *y*-scale to the (same) specified range.
         *
         * @param  min the minimum value of the *x*- and *y*-scales
         * @param  max the maximum value of the *x*- and *y*-scales
         * @throws IllegalArgumentException if `(max == min)`
         */
        fun setScale(min: Double, max: Double) {
            val size = max - min
            if (size == 0.0) throw IllegalArgumentException("the min and max are the same")
            synchronized(mouseLock) {
                xmin = min - BORDER * size
                xmax = max + BORDER * size
                ymin = min - BORDER * size
                ymax = max + BORDER * size
            }
        }

        // helper functions that scale from user coordinates to screen coordinates and back
        private fun scaleX(x: Double) = width * (x - xmin) / (xmax - xmin)

        private fun scaleY(y: Double) = height * (ymax - y) / (ymax - ymin)
        private fun factorX(w: Double) = w * width / Math.abs(xmax - xmin)
        private fun factorY(h: Double) = h * height / Math.abs(ymax - ymin)
        private fun userX(x: Double) = xmin + x * (xmax - xmin) / width
        private fun userY(y: Double) = ymax - y * (ymax - ymin) / height

        /**
         * Clears the screen to the specified color.
         *
         * @param color the color to make the background
         */
        @JvmOverloads
        fun clear(color: Color = DEFAULT_CLEAR_COLOR) {
            offscreen.color = color
            offscreen.fillRect(0, 0, width, height)
            offscreen.color = penColor
            draw()
        }

        /**
         * Sets the pen size to the default size (0.002).
         * The pen is circular, so that lines have rounded ends, and when you set the
         * pen radius and draw a point, you get a circle of the specified radius.
         * The pen radius is not affected by coordinate scaling.
         */
        //@JvmOverloads
        fun setPenRadius() {
            penRadius = DEFAULT_PEN_RADIUS
        }

        /**
         * Set the pen color to the default color (black).
         */
        fun setPenColor() {
            penColor = DEFAULT_PEN_COLOR
        }

        /**
         * Sets the pen color to the specified RGB color.
         *
         * @param  red the amount of red (between 0 and 255)
         * @param  green the amount of green (between 0 and 255)
         * @param  blue the amount of blue (between 0 and 255)
         * @throws IllegalArgumentException if `red`, `green`,
         * or `blue` is outside its prescribed range
         */
        fun setPenColor(red: Int, green: Int, blue: Int) {
            if (red < 0 || red >= 256) throw IllegalArgumentException("amount of red must be between 0 and 255")
            if (green < 0 || green >= 256) throw IllegalArgumentException("amount of green must be between 0 and 255")
            if (blue < 0 || blue >= 256) throw IllegalArgumentException("amount of blue must be between 0 and 255")
            penColor = Color(red, green, blue)
        }

        /**
         * Sets the font to default value.
         *
         */
        fun setFont() {
            font = DEFAULT_FONT
        }


        /***************************************************************************
         * Drawing geometric shapes.
         */

        /**
         * Draws a line segment between (*x*<sub>0</sub>, *y*<sub>0</sub>) and
         * (*x*<sub>1</sub>, *y*<sub>1</sub>).
         *
         * @param  x0 the *x*-coordinate of one endpoint
         * @param  y0 the *y*-coordinate of one endpoint
         * @param  x1 the *x*-coordinate of the other endpoint
         * @param  y1 the *y*-coordinate of the other endpoint
         */
        fun line(x0: Double, y0: Double, x1: Double, y1: Double) {
            offscreen.draw(Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)))
            draw()
        }

        /**
         * Draws one pixel at (*x*, *y*).
         * This method is private because pixels depend on the display.
         * To achieve the same effect, set the pen radius to 0 and call `point()`.
         *
         * @param  x the *x*-coordinate of the pixel
         * @param  y the *y*-coordinate of the pixel
         */
        private fun pixel(x: Double, y: Double) = offscreen.fillRect(Math.round(scaleX(x)).toInt(), Math.round(scaleY(y)).toInt(), 1, 1)

        /**
         * Draws a point centered at (*x*, *y*).
         * The point is a filled circle whose radius is equal to the pen radius.
         * To draw a single-pixel point, first set the pen radius to 0.
         *
         * @param x the *x*-coordinate of the point
         * @param y the *y*-coordinate of the point
         */
        fun point(x: Double, y: Double) {
            val xs = scaleX(x)
            val ys = scaleY(y)
            val r = penRadius
            val scaledPenRadius = (r * DEFAULT_SIZE).toFloat()

            // double ws = factorX(2*r);
            // double hs = factorY(2*r);
            // if (ws <= 1 && hs <= 1) pixel(x, y);
            if (scaledPenRadius <= 1)
                pixel(x, y)
            else
                offscreen.fill(Ellipse2D.Double(xs - scaledPenRadius / 2, ys - scaledPenRadius / 2,
                        scaledPenRadius.toDouble(), scaledPenRadius.toDouble()))
            draw()
        }

        /**
         * Draws a circle of the specified radius, centered at (*x*, *y*).
         *
         * @param  x the *x*-coordinate of the center of the circle
         * @param  y the *y*-coordinate of the center of the circle
         * @param  radius the radius of the circle
         * @throws IllegalArgumentException if `radius` is negative
         */
        fun circle(x: Double, y: Double, radius: Double) {
            if (radius < 0) throw IllegalArgumentException("radius must be non-negative")
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(2 * radius)
            val hs = factorY(2 * radius)
            if (ws <= 1 && hs <= 1) pixel(x, y)
            else offscreen.draw(Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
            draw()
        }

        /**
         * Draws a filled circle of the specified radius, centered at (*x*, *y*).
         *
         * @param  x the *x*-coordinate of the center of the circle
         * @param  y the *y*-coordinate of the center of the circle
         * @param  radius the radius of the circle
         * @throws IllegalArgumentException if `radius` is negative
         */
        fun filledCircle(x: Double, y: Double, radius: Double) {
            if (radius < 0) throw IllegalArgumentException("radius must be non-negative")
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(2 * radius)
            val hs = factorY(2 * radius)
            if (ws <= 1 && hs <= 1) pixel(x, y)
            else offscreen.fill(Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
            draw()
        }

        /**
         * Draws an ellipse with the specified semimajor and semiminor axes,
         * centered at (*x*, *y*).
         *
         * @param  x the *x*-coordinate of the center of the ellipse
         * @param  y the *y*-coordinate of the center of the ellipse
         * @param  semiMajorAxis is the semimajor axis of the ellipse
         * @param  semiMinorAxis is the semiminor axis of the ellipse
         * @throws IllegalArgumentException if either `semiMajorAxis`
         * or `semiMinorAxis` is negative
         */
        fun ellipse(x: Double, y: Double, semiMajorAxis: Double, semiMinorAxis: Double) {
            if (semiMajorAxis < 0) throw IllegalArgumentException("ellipse semimajor axis must be non-negative")
            if (semiMinorAxis < 0) throw IllegalArgumentException("ellipse semiminor axis must be non-negative")
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(2 * semiMajorAxis)
            val hs = factorY(2 * semiMinorAxis)
            if (ws <= 1 && hs <= 1) pixel(x, y)
            else offscreen.draw(Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
            draw()
        }

        /**
         * Draws an ellipse with the specified semimajor and semiminor axes,
         * centered at (*x*, *y*).
         *
         * @param  x the *x*-coordinate of the center of the ellipse
         * @param  y the *y*-coordinate of the center of the ellipse
         * @param  semiMajorAxis is the semimajor axis of the ellipse
         * @param  semiMinorAxis is the semiminor axis of the ellipse
         * @throws IllegalArgumentException if either `semiMajorAxis`
         * or `semiMinorAxis` is negative
         */
        fun filledEllipse(x: Double, y: Double, semiMajorAxis: Double, semiMinorAxis: Double) {
            if (semiMajorAxis < 0) throw IllegalArgumentException("ellipse semimajor axis must be non-negative")
            if (semiMinorAxis < 0) throw IllegalArgumentException("ellipse semiminor axis must be non-negative")
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(2 * semiMajorAxis)
            val hs = factorY(2 * semiMinorAxis)
            if (ws <= 1 && hs <= 1) pixel(x, y)
            else offscreen.fill(Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
            draw()
        }

        /**
         * Draws a circular arc of the specified radius,
         * centered at (*x*, *y*), from angle1 to angle2 (in degrees).
         *
         * @param  x the *x*-coordinate of the center of the circle
         * @param  y the *y*-coordinate of the center of the circle
         * @param  radius the radius of the circle
         * @param  angle1 the starting angle. 0 would mean an arc beginning at 3 o'clock.
         * @param  angle2 the angle at the end of the arc. For example, if
         * you want a 90 degree arc, then angle2 should be angle1 + 90.
         * @throws IllegalArgumentException if `radius` is negative
         */
        fun arc(x: Double, y: Double, radius: Double, angle1: Double, angle2: Double) {
            var angle2 = angle2
            if (radius < 0) throw IllegalArgumentException("arc radius must be non-negative")
            while (angle2 < angle1) angle2 += 360.0
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(2 * radius)
            val hs = factorY(2 * radius)
            if (ws <= 1 && hs <= 1) pixel(x, y)
            else offscreen.draw(Arc2D.Double(xs - ws / 2, ys - hs / 2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN))
            draw()
        }

        /**
         * Draws a square of side length 2r, centered at (*x*, *y*).
         *
         * @param  x the *x*-coordinate of the center of the square
         * @param  y the *y*-coordinate of the center of the square
         * @param  halfLength one half the length of any side of the square
         * @throws IllegalArgumentException if `halfLength` is negative
         */
        fun square(x: Double, y: Double, halfLength: Double) {
            if (halfLength < 0) throw IllegalArgumentException("half length must be non-negative")
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(2 * halfLength)
            val hs = factorY(2 * halfLength)
            if (ws <= 1 && hs <= 1) pixel(x, y)
            else offscreen.draw(Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
            draw()
        }

        /**
         * Draws a filled square of the specified size, centered at (*x*, *y*).
         *
         * @param  x the *x*-coordinate of the center of the square
         * @param  y the *y*-coordinate of the center of the square
         * @param  halfLength one half the length of any side of the square
         * @throws IllegalArgumentException if `halfLength` is negative
         */
        fun filledSquare(x: Double, y: Double, halfLength: Double) {
            if (halfLength < 0) throw IllegalArgumentException("half length must be non-negative")
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(2 * halfLength)
            val hs = factorY(2 * halfLength)
            if (ws <= 1 && hs <= 1) pixel(x, y)
            else offscreen.fill(Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
            draw()
        }


        /**
         * Draws a rectangle of the specified size, centered at (*x*, *y*).
         *
         * @param  x the *x*-coordinate of the center of the rectangle
         * @param  y the *y*-coordinate of the center of the rectangle
         * @param  halfWidth one half the width of the rectangle
         * @param  halfHeight one half the height of the rectangle
         * @throws IllegalArgumentException if either `halfWidth` or `halfHeight` is negative
         */
        fun rectangle(x: Double, y: Double, halfWidth: Double, halfHeight: Double) {
            if (halfWidth < 0) throw IllegalArgumentException("half width must be non-negative")
            if (halfHeight < 0) throw IllegalArgumentException("half height must be non-negative")
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(2 * halfWidth)
            val hs = factorY(2 * halfHeight)
            if (ws <= 1 && hs <= 1) pixel(x, y)
            else offscreen.draw(Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
            draw()
        }

        /**
         * Draws a filled rectangle of the specified size, centered at (*x*, *y*).
         *
         * @param  x the *x*-coordinate of the center of the rectangle
         * @param  y the *y*-coordinate of the center of the rectangle
         * @param  halfWidth one half the width of the rectangle
         * @param  halfHeight one half the height of the rectangle
         * @throws IllegalArgumentException if either `halfWidth` or `halfHeight` is negative
         */
        fun filledRectangle(x: Double, y: Double, halfWidth: Double, halfHeight: Double) {
            if (halfWidth < 0) throw IllegalArgumentException("half width must be non-negative")
            if (halfHeight < 0) throw IllegalArgumentException("half height must be non-negative")
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(2 * halfWidth)
            val hs = factorY(2 * halfHeight)
            if (ws <= 1 && hs <= 1) pixel(x, y)
            else offscreen.fill(Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
            draw()
        }


        /**
         * Draws a polygon with the vertices
         * (*x*<sub>0</sub>, *y*<sub>0</sub>),
         * (*x*<sub>1</sub>, *y*<sub>1</sub>), ...,
         * (*x*<sub>*n*–1</sub>, *y*<sub>*n*–1</sub>).
         *
         * @param  x an array of all the *x*-coordinates of the polygon
         * @param  y an array of all the *y*-coordinates of the polygon
         * @throws IllegalArgumentException unless `x[]` and `y[]`
         * are of the same length
         */
        fun polygon(x: DoubleArray?, y: DoubleArray?) {
            if (x == null) throw IllegalArgumentException("x-coordinate array is null")
            if (y == null) throw IllegalArgumentException("y-coordinate array is null")
            val n1 = x.size
            val n2 = y.size
            if (n1 != n2) throw IllegalArgumentException("arrays must be of the same length")
            if (n1 == 0) return

            val path = GeneralPath()
            path.moveTo(scaleX(x[0]).toFloat(), scaleY(y[0]).toFloat())
            for (i in 0 until n1)
                path.lineTo(scaleX(x[i]).toFloat(), scaleY(y[i]).toFloat())
            path.closePath()
            offscreen.draw(path)
            draw()
        }

        /**
         * Draws a polygon with the vertices
         * (*x*<sub>0</sub>, *y*<sub>0</sub>),
         * (*x*<sub>1</sub>, *y*<sub>1</sub>), ...,
         * (*x*<sub>*n*–1</sub>, *y*<sub>*n*–1</sub>).
         *
         * @param  x an array of all the *x*-coordinates of the polygon
         * @param  y an array of all the *y*-coordinates of the polygon
         * @throws IllegalArgumentException unless `x[]` and `y[]`
         * are of the same length
         */
        fun filledPolygon(x: DoubleArray?, y: DoubleArray?) {
            if (x == null) throw IllegalArgumentException("x-coordinate array is null")
            if (y == null) throw IllegalArgumentException("y-coordinate array is null")
            val n1 = x.size
            val n2 = y.size
            if (n1 != n2) throw IllegalArgumentException("arrays must be of the same length")
            if (n1 == 0) return

            val path = GeneralPath()
            path.moveTo(scaleX(x[0]).toFloat(), scaleY(y[0]).toFloat())
            for (i in 0 until n1)
                path.lineTo(scaleX(x[i]).toFloat(), scaleY(y[i]).toFloat())
            path.closePath()
            offscreen.fill(path)
            draw()
        }


        /***************************************************************************
         * Drawing images.
         */
        // get an image from the given filename
        private fun getImage(filename: String?): Image {
            if (filename == null) throw IllegalArgumentException()

            // to read from file
            var icon: ImageIcon? = ImageIcon(filename)

            // try to read from URL
            if (icon == null || icon.imageLoadStatus != MediaTracker.COMPLETE)
                try {
                    val url = URL(filename)
                    icon = ImageIcon(url)
                } catch (e: MalformedURLException) {
                    /* not a url */
                }

            // in case file is inside a .jar (classpath relative to StdDraw)
            if (icon == null || icon.imageLoadStatus != MediaTracker.COMPLETE) {
                val url = StdDraw::class.java.getResource(filename)
                if (url != null)
                    icon = ImageIcon(url)
            }

            // in case file is inside a .jar (classpath relative to root of jar)
            if (icon == null || icon.imageLoadStatus != MediaTracker.COMPLETE) {
                val url = StdDraw::class.java.getResource("/$filename")
                        ?: throw IllegalArgumentException("image $filename not found")
                icon = ImageIcon(url)
            }
            return icon.image
        }

        /**
         * Draws the specified image centered at (*x*, *y*).
         * The supported image formats are JPEG, PNG, and GIF.
         * As an optimization, the picture is cached, so there is no performance
         * penalty for redrawing the same image multiple times (e.g., in an animation).
         * However, if you change the picture file after drawing it, subsequent
         * calls will draw the original picture.
         *
         * @param  x the center *x*-coordinate of the image
         * @param  y the center *y*-coordinate of the image
         * @param  filename the name of the image/picture, e.g., "ball.gif"
         * @throws IllegalArgumentException if the image filename is invalid
         */
        fun picture(x: Double, y: Double, filename: String) {
            // BufferedImage image = getImage(filename);
            val image = getImage(filename)
            val xs = scaleX(x)
            val ys = scaleY(y)
            // int ws = image.getWidth();    // can call only if image is a BufferedImage
            // int hs = image.getHeight();
            val ws = image.getWidth(null)
            val hs = image.getHeight(null)
            if (ws < 0 || hs < 0) throw IllegalArgumentException("image $filename is corrupt")

            offscreen.drawImage(image, Math.round(xs - ws / 2.0).toInt(), Math.round(ys - hs / 2.0).toInt(), null)
            draw()
        }

        /**
         * Draws the specified image centered at (*x*, *y*),
         * rotated given number of degrees.
         * The supported image formats are JPEG, PNG, and GIF.
         *
         * @param  x the center *x*-coordinate of the image
         * @param  y the center *y*-coordinate of the image
         * @param  filename the name of the image/picture, e.g., "ball.gif"
         * @param  degrees is the number of degrees to rotate counterclockwise
         * @throws IllegalArgumentException if the image filename is invalid
         */
        fun picture(x: Double, y: Double, filename: String, degrees: Double) {
            // BufferedImage image = getImage(filename);
            val image = getImage(filename)
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = image.getWidth(null)
            val hs = image.getHeight(null)
            if (ws < 0 || hs < 0) throw IllegalArgumentException("image $filename is corrupt")

            offscreen.rotate(Math.toRadians(-degrees), xs, ys)
            offscreen.drawImage(image, Math.round(xs - ws / 2.0).toInt(), Math.round(ys - hs / 2.0).toInt(), null)
            offscreen.rotate(Math.toRadians(+degrees), xs, ys)
            draw()
        }

        /**
         * Draws the specified image centered at (*x*, *y*),
         * rescaled to the specified bounding box.
         * The supported image formats are JPEG, PNG, and GIF.
         *
         * @param  x the center *x*-coordinate of the image
         * @param  y the center *y*-coordinate of the image
         * @param  filename the name of the image/picture, e.g., "ball.gif"
         * @param  scaledWidth the width of the scaled image (in screen coordinates)
         * @param  scaledHeight the height of the scaled image (in screen coordinates)
         * @throws IllegalArgumentException if either `scaledWidth`
         * or `scaledHeight` is negative
         * @throws IllegalArgumentException if the image filename is invalid
         */
        fun picture(x: Double, y: Double, filename: String, scaledWidth: Double, scaledHeight: Double) {
            val image = getImage(filename)
            if (scaledWidth < 0) throw IllegalArgumentException("width  is negative: $scaledWidth")
            if (scaledHeight < 0) throw IllegalArgumentException("height is negative: $scaledHeight")
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(scaledWidth)
            val hs = factorY(scaledHeight)
            if (ws < 0 || hs < 0) throw IllegalArgumentException("image $filename is corrupt")
            if (ws <= 1 && hs <= 1)
                pixel(x, y)
            else {
                offscreen.drawImage(image, Math.round(xs - ws / 2.0).toInt(),
                        Math.round(ys - hs / 2.0).toInt(),
                        Math.round(ws).toInt(),
                        Math.round(hs).toInt(), null)
            }
            draw()
        }


        /**
         * Draws the specified image centered at (*x*, *y*), rotated
         * given number of degrees, and rescaled to the specified bounding box.
         * The supported image formats are JPEG, PNG, and GIF.
         *
         * @param  x the center *x*-coordinate of the image
         * @param  y the center *y*-coordinate of the image
         * @param  filename the name of the image/picture, e.g., "ball.gif"
         * @param  scaledWidth the width of the scaled image (in screen coordinates)
         * @param  scaledHeight the height of the scaled image (in screen coordinates)
         * @param  degrees is the number of degrees to rotate counterclockwise
         * @throws IllegalArgumentException if either `scaledWidth`
         * or `scaledHeight` is negative
         * @throws IllegalArgumentException if the image filename is invalid
         */
        fun picture(x: Double, y: Double, filename: String, scaledWidth: Double, scaledHeight: Double, degrees: Double) {
            if (scaledWidth < 0) throw IllegalArgumentException("width is negative: $scaledWidth")
            if (scaledHeight < 0) throw IllegalArgumentException("height is negative: $scaledHeight")
            val image = getImage(filename)
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = factorX(scaledWidth)
            val hs = factorY(scaledHeight)
            if (ws < 0 || hs < 0) throw IllegalArgumentException("image $filename is corrupt")
            if (ws <= 1 && hs <= 1) pixel(x, y)

            offscreen.rotate(Math.toRadians(-degrees), xs, ys)
            offscreen.drawImage(image, Math.round(xs - ws / 2.0).toInt(),
                    Math.round(ys - hs / 2.0).toInt(),
                    Math.round(ws).toInt(),
                    Math.round(hs).toInt(), null)
            offscreen.rotate(Math.toRadians(+degrees), xs, ys)
            draw()
        }

        /**
         * Write the given text string in the current font, centered at (*x*, *y*).
         *
         * @param  x the center *x*-coordinate of the text
         * @param  y the center *y*-coordinate of the text
         * @param  text the text to write
         */
        fun text(x: Double, y: Double, text: String?) {
            if (text == null) throw IllegalArgumentException()
            offscreen.font = font
            val metrics = offscreen.fontMetrics
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = metrics.stringWidth(text)
            val hs = metrics.descent
            offscreen.drawString(text, (xs - ws / 2.0).toFloat(), (ys + hs).toFloat())
            draw()
        }

        /**
         * Write the given text string in the current font, centered at (*x*, *y*) and
         * rotated by the specified number of degrees.
         * @param  x the center *x*-coordinate of the text
         * @param  y the center *y*-coordinate of the text
         * @param  text the text to write
         * @param  degrees is the number of degrees to rotate counterclockwise
         */
        fun text(x: Double, y: Double, text: String?, degrees: Double) {
            if (text == null) throw IllegalArgumentException()
            val xs = scaleX(x)
            val ys = scaleY(y)
            offscreen.rotate(Math.toRadians(-degrees), xs, ys)
            text(x, y, text)
            offscreen.rotate(Math.toRadians(+degrees), xs, ys)
        }

        /**
         * Write the given text string in the current font, left-aligned at (*x*, *y*).
         * @param  x the *x*-coordinate of the text
         * @param  y the *y*-coordinate of the text
         * @param  text the text
         */
        fun textLeft(x: Double, y: Double, text: String?) {
            if (text == null) throw IllegalArgumentException()
            offscreen.font = font
            val metrics = offscreen.fontMetrics
            val xs = scaleX(x)
            val ys = scaleY(y)
            val hs = metrics.descent
            offscreen.drawString(text, xs.toFloat(), (ys + hs).toFloat())
            draw()
        }

        /**
         * Write the given text string in the current font, right-aligned at (*x*, *y*).
         *
         * @param  x the *x*-coordinate of the text
         * @param  y the *y*-coordinate of the text
         * @param  text the text to write
         */
        fun textRight(x: Double, y: Double, text: String?) {
            if (text == null) throw IllegalArgumentException()
            offscreen.font = font
            val metrics = offscreen.fontMetrics
            val xs = scaleX(x)
            val ys = scaleY(y)
            val ws = metrics.stringWidth(text)
            val hs = metrics.descent
            offscreen.drawString(text, (xs - ws).toFloat(), (ys + hs).toFloat())
            draw()
        }

        /**
         * Copies the offscreen buffer to the onscreen buffer, pauses for t milliseconds
         * and enables double buffering.
         * @param t number of milliseconds
         */
        @Deprecated("replaced by {@link #enableDoubleBuffering()}, {@link #show()}, and {@link #pause(int t)}")
        fun show(t: Int) {
            show()
            pause(t)
            enableDoubleBuffering()
        }

        /**
         * Pause for t milliseconds. This method is intended to support computer animations.
         * @param t number of milliseconds
         */
        fun pause(t: Int) = try {
                Thread.sleep(t.toLong())
            } catch (e: InterruptedException) {
                println("Error sleeping")
            }


        /**
         * Copies offscreen buffer to onscreen buffer. There is no reason to call
         * this method unless double buffering is enabled.
         */
        fun show() {
            onscreen.drawImage(offscreenImage, 0, 0, null)
            frame.repaint()
        }

        // draw onscreen if defer is false
        private fun draw() {
            if (!defer) show()
        }

        /**
         * Enable double buffering. All subsequent calls to
         * drawing methods such as `line()`, `circle()`,
         * and `square()` will be deffered until the next call
         * to show(). Useful for animations.
         */
        fun enableDoubleBuffering() {
            defer = true
        }

        /**
         * Disable double buffering. All subsequent calls to
         * drawing methods such as `line()`, `circle()`,
         * and `square()` will be displayed on screen when called.
         * This is the default.
         */
        fun disableDoubleBuffering() {
            defer = false
        }

        /**
         * Saves the drawing to using the specified filename.
         * The supported image formats are JPEG and PNG;
         * the filename suffix must be `.jpg` or `.png`.
         *
         * @param  filename the name of the file with one of the required suffixes
         */
        fun save(filename: String?) {
            if (filename == null) throw IllegalArgumentException()
            val file = File(filename)
            val suffix = filename.substring(filename.lastIndexOf('.') + 1)

            // png files
            when {
                suffix.equals("png", ignoreCase = true) -> try {
                    ImageIO.write(onscreenImage, suffix, file)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                suffix.equals("jpg", ignoreCase = true) -> {
                    val raster = onscreenImage.raster
                    val newRaster: WritableRaster
                    newRaster = raster.createWritableChild(0, 0, width, height, 0, 0, intArrayOf(0, 1, 2))
                    val cm = onscreenImage.colorModel as DirectColorModel
                    val newCM = DirectColorModel(cm.pixelSize,
                            cm.redMask,
                            cm.greenMask,
                            cm.blueMask)
                    val rgbBuffer = BufferedImage(newCM, newRaster, false, null)
                    try {
                        ImageIO.write(rgbBuffer, suffix, file)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
                else -> println("Invalid image file type: $suffix")
            }// need to change from ARGB to RGB for JPEG
            // reference: http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727
        }

        /**
         * Returns true if the mouse is being pressed.
         *
         * @return `true` if the mouse is being pressed; `false` otherwise
         */
        @Deprecated("replaced by {@link #isMousePressed()}")
        fun mousePressed(): Boolean {
            synchronized(mouseLock) {
                return isMousePressed
            }
        }

        /**
         * Returns the *x*-coordinate of the mouse.
         *
         * @return the *x*-coordinate of the mouse
         */
        fun mouseX(): Double {
            synchronized(mouseLock) {
                return mouseX
            }
        }

        /**
         * Returns the *y*-coordinate of the mouse.
         *
         * @return *y*-coordinate of the mouse
         */
        fun mouseY(): Double {
            synchronized(mouseLock) {
                return mouseY
            }
        }

        /**
         * Returns true if the user has typed a key (that has not yet been processed).
         *
         * @return `true` if the user has typed a key (that has not yet been processed
         * by [.nextKeyTyped]; `false` otherwise
         */
        fun hasNextKeyTyped(): Boolean {
            synchronized(keyLock) {
                return !keysTyped.isEmpty()
            }
        }

        /**
         * Returns the next key that was typed by the user (that your program has not already processed).
         * This method should be preceded by a call to [.hasNextKeyTyped] to ensure
         * that there is a next key to process.
         * This method returns a Unicode character corresponding to the key
         * typed (such as `'a'` or `'A'`).
         * It cannot identify action keys (such as F1 and arrow keys)
         * or modifier keys (such as control).
         *
         * @return the next key typed by the user (that your program has not already processed).
         * @throws NoSuchElementException if there is no remaining key
         */
        fun nextKeyTyped(): Char {
            synchronized(keyLock) {
                if (keysTyped.isEmpty()) {
                    throw NoSuchElementException("your program has already processed all keystrokes")
                }
                return keysTyped.removeAt(keysTyped.size - 1)
            }
        }

        /**
         * Returns true if the given key is being pressed.
         *
         *
         * This method takes the keycode (corresponding to a physical key)
         * as an argument. It can handle action keys
         * (such as F1 and arrow keys) and modifier keys (such as shift and control).
         * See [KeyEvent] for a description of key codes.
         *
         * @param  keycode the key to check if it is being pressed
         * @return `true` if `keycode` is currently being pressed;
         * `false` otherwise
         */
        fun isKeyPressed(keycode: Int): Boolean {
            synchronized(keyLock) {
                return keysDown.contains(keycode)
            }
        }

        /**
         * Test client.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            square(0.2, 0.8, 0.1)
            filledSquare(0.8, 0.8, 0.2)
            circle(0.8, 0.2, 0.2)

            penColor = BOOK_RED
            penRadius = 0.02
            arc(0.8, 0.2, 0.1, 200.0, 45.0)

            // draw a blue diamond
            setPenRadius()
            penColor = BOOK_BLUE
            val x = doubleArrayOf(0.1, 0.2, 0.3, 0.2)
            val y = doubleArrayOf(0.2, 0.3, 0.2, 0.1)
            filledPolygon(x, y)

            // text
            penColor = BLACK
            text(0.2, 0.5, "black text")
            penColor = WHITE
            text(0.8, 0.8, "white text")
        }
    }
}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
