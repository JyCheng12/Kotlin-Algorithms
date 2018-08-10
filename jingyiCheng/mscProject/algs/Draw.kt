package jingyiCheng.mscProject.algs

import java.awt.BasicStroke
import java.awt.Color
import java.awt.FileDialog
import java.awt.Font
import java.awt.Graphics2D
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
 * *Draw*. This class provides a basic capability for
 * creating drawings with your programs. It uses a simple graphics model that
 * allows you to create drawings consisting of points, lines, and curves
 * in a window on your computer and to save the drawings to a file.
 * This is the object-oriented version of standard draw; it supports
 * multiple indepedent drawing windows.
 *
 *
 * For additional documentation, see [Section 3.1](https://introcs.cs.princeton.edu/31datatype) of
 * *Computer Science: An Interdisciplinary Approach* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class Draw : ActionListener, MouseListener, MouseMotionListener, KeyListener {
    var penColor: Color? = null
        set(color) {
            field = color
            offscreen.color = this.penColor
        }
    // canvas size
    private var width = DEFAULT_SIZE
    private var height = DEFAULT_SIZE

    var penRadius: Double = 0.toDouble()
        set(r) {
            if (r < 0) throw IllegalArgumentException("pen radius must be positive")
            field = r * DEFAULT_SIZE
            val stroke = BasicStroke(this.penRadius.toFloat(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
            offscreen.stroke = stroke
        }

    // show we draw immediately or wait until next show?
    private var defer = false

    private var xmin: Double = 0.toDouble()
    private var ymin: Double = 0.toDouble()
    private var xmax: Double = 0.toDouble()
    private var ymax: Double = 0.toDouble()

    // name of window
    private var name = "Draw"

    // for synchronization
    private val mouseLock = Any()
    private val keyLock = Any()

    var font: Font? = null

    lateinit var jLabel: JLabel
        private set

    // double buffered graphics
    private lateinit var offscreenImage: BufferedImage
    private lateinit var onscreenImage: BufferedImage
    private lateinit var offscreen: Graphics2D
    private lateinit var onscreen: Graphics2D

    // the frame for drawing to the screen
    private var frame: JFrame? = JFrame()

    // mouse state
    private var isMousePressed = false
    private var mouseX = 0.0
    private var mouseY = 0.0

    // keyboard state
    private val keysTyped = LinkedList<Char>()
    private val keysDown = TreeSet<Int>()

    // event-based listeners
    private val listeners = ArrayList<DrawListener>()

    /**
     * Initializes an empty drawing object with the given name.
     *
     * @param name the title of the drawing window.
     */
    constructor(name: String) {
        this.name = name
        init()
    }

    /**
     * Initializes an empty drawing object.
     */
    constructor() {
        init()
    }

    private fun init() {
        if (frame != null) frame!!.isVisible = false
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
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        offscreen.addRenderingHints(hints)

        // frame stuff
        val icon = ImageIcon(onscreenImage)
        jLabel = JLabel(icon)

        jLabel.addMouseListener(this)
        jLabel.addMouseMotionListener(this)

        frame!!.contentPane = jLabel
        frame!!.addKeyListener(this)    // JLabel cannot get keyboard focus
        frame!!.isResizable = false
        frame!!.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE      // closes only current window
        frame!!.title = name
        frame!!.jMenuBar = createMenuBar()
        frame!!.pack()
        frame!!.requestFocusInWindow()
        frame!!.isVisible = true
    }


    /**
     * Sets the upper-left hand corner of the drawing window to be (x, y), where (0, 0) is upper left.
     *
     * @param  x the number of pixels from the left
     * @param  y the number of pixels from the top
     * @throws IllegalArgumentException if the width or height is 0 or negative
     */
    fun setLocationOnScreen(x: Int, y: Int) {
        if (x <= 0 || y <= 0) throw IllegalArgumentException()
        frame!!.setLocation(x, y)
    }

    /**
     * Sets the default close operation.
     *
     * @param  value the value, typically `JFrame.EXIT_ON_CLOSE`
     * (close all windows) or `JFrame.DISPOSE_ON_CLOSE`
     * (close current window)
     */
    fun setDefaultCloseOperation(value: Int) {
        frame!!.defaultCloseOperation = value
    }

    /**
     * Sets the canvas (drawing area) to be *width*-by-*height* pixels.
     * This also erases the current drawing and resets the coordinate system, pen radius,
     * pen color, and font back to their default values.
     * Ordinarly, this method is called once, at the very beginning of a program.
     *
     * @param  canvasWidth the width as a number of pixels
     * @param  canvasHeight the height as a number of pixels
     * @throws IllegalArgumentException unless both `canvasWidth`
     * and `canvasHeight` are positive
     */
    fun setCanvasSize(canvasWidth: Int, canvasHeight: Int) {
        if (canvasWidth < 1 || canvasHeight < 1) {
            throw IllegalArgumentException("width and height must be positive")
        }
        width = canvasWidth
        height = canvasHeight
        init()
    }


    // create the menu bar (changed to private)
    private fun createMenuBar(): JMenuBar {
        val menuBar = JMenuBar()
        val menu = JMenu("File")
        menuBar.add(menu)
        val menuItem1 = JMenuItem(" Save...   ")
        menuItem1.addActionListener(this)
        menuItem1.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().menuShortcutKeyMask)
        menu.add(menuItem1)
        return menuBar
    }

    /**
     * Sets the x-scale.
     *
     * @param min the minimum value of the x-scale
     * @param max the maximum value of the x-scale
     */
    @JvmOverloads
    fun setXscale(min: Double = DEFAULT_XMIN, max: Double = DEFAULT_XMAX) {
        val size = max - min
        xmin = min - BORDER * size
        xmax = max + BORDER * size
    }

    /**
     * Sets the y-scale.
     *
     * @param min the minimum value of the y-scale
     * @param max the maximum value of the y-scale
     */
    @JvmOverloads
    fun setYscale(min: Double = DEFAULT_YMIN, max: Double = DEFAULT_YMAX) {
        val size = max - min
        ymin = min - BORDER * size
        ymax = max + BORDER * size
    }

    // helper functions that scale from user coordinates to screen coordinates and back
    private fun scaleX(x: Double) = width * (x - xmin) / (xmax - xmin)

    private fun scaleY(y: Double) = height * (ymax - y) / (ymax - ymin)

    private fun factorX(w: Double) = w * width / Math.abs(xmax - xmin)

    private fun factorY(h: Double) = h * height / Math.abs(ymax - ymin)

    private fun userX(x: Double) = xmin + x * (xmax - xmin) / width

    private fun userY(y: Double) = ymax - y * (ymax - ymin) / height

    /**
     * Clears the screen to the given color.
     *
     * @param color the color to make the background
     */
    @JvmOverloads
    fun clear(color: Color = DEFAULT_CLEAR_COLOR) {
        offscreen.color = color
        offscreen.fillRect(0, 0, width, height)
        offscreen.color = this.penColor
        draw()
    }

    /**
     * Sets the pen size to the default (.002).
     */
    fun setPenRadius() {
        penRadius = DEFAULT_PEN_RADIUS
    }

    /**
     * Sets the pen color to the default color (black).
     */
    fun setPenColor() {
        penColor = DEFAULT_PEN_COLOR
    }

    /**
     * Sets the pen color to the given RGB color.
     *
     * @param  red the amount of red (between 0 and 255)
     * @param  green the amount of green (between 0 and 255)
     * @param  blue the amount of blue (between 0 and 255)
     * @throws IllegalArgumentException if the amount of red, green, or blue are outside prescribed range
     */
    fun setPenColor(red: Int, green: Int, blue: Int) {
        if (red < 0 || red >= 256) throw IllegalArgumentException("amount of red must be between 0 and 255")
        if (green < 0 || green >= 256) throw IllegalArgumentException("amount of red must be between 0 and 255")
        if (blue < 0 || blue >= 256) throw IllegalArgumentException("amount of red must be between 0 and 255")
        penColor = Color(red, green, blue)
    }


    /**
     * Turns on xor mode.
     */
    fun xorOn() {
        offscreen.setXORMode(DEFAULT_CLEAR_COLOR)
    }

    /**
     * Turns off xor mode.
     */
    fun xorOff() {
        offscreen.setPaintMode()
    }

    /**
     * Sets the font to the default font (sans serif, 16 point).
     */
    fun setFont() {
        font = DEFAULT_FONT
    }


    /***************************************************************************
     * Drawing geometric shapes.
     */

    /**
     * Draws a line from (x0, y0) to (x1, y1).
     *
     * @param x0 the x-coordinate of the starting point
     * @param y0 the y-coordinate of the starting point
     * @param x1 the x-coordinate of the destination point
     * @param y1 the y-coordinate of the destination point
     */
    fun line(x0: Double, y0: Double, x1: Double, y1: Double) {
        offscreen.draw(Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)))
        draw()
    }

    /**
     * Draws one pixel at (x, y).
     *
     * @param x the x-coordinate of the pixel
     * @param y the y-coordinate of the pixel
     */
    private fun pixel(x: Double, y: Double) {
        offscreen.fillRect(Math.round(scaleX(x)).toInt(), Math.round(scaleY(y)).toInt(), 1, 1)
    }

    /**
     * Draws a point at (x, y).
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     */
    fun point(x: Double, y: Double) {
        val xs = scaleX(x)
        val ys = scaleY(y)
        val r = this.penRadius
        if (r <= 1)
            pixel(x, y)
        else
            offscreen.fill(Ellipse2D.Double(xs - r / 2, ys - r / 2, r, r))
        draw()
    }

    /**
     * Draws a circle of radius r, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the circle
     * @param  y the y-coordinate of the center of the circle
     * @param  r the radius of the circle
     * @throws IllegalArgumentException if the radius of the circle is negative
     */
    fun circle(x: Double, y: Double, r: Double) {
        if (r < 0) throw IllegalArgumentException("circle radius can't be negative")
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(2 * r)
        val hs = factorY(2 * r)
        if (ws <= 1 && hs <= 1)
            pixel(x, y)
        else
            offscreen.draw(Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
        draw()
    }

    /**
     * Draws a filled circle of radius r, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the circle
     * @param  y the y-coordinate of the center of the circle
     * @param  r the radius of the circle
     * @throws IllegalArgumentException if the radius of the circle is negative
     */
    fun filledCircle(x: Double, y: Double, r: Double) {
        if (r < 0) throw IllegalArgumentException("circle radius can't be negative")
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(2 * r)
        val hs = factorY(2 * r)
        if (ws <= 1 && hs <= 1)
            pixel(x, y)
        else
            offscreen.fill(Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
        draw()
    }

    /**
     * Draws an ellipse with given semimajor and semiminor axes, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the ellipse
     * @param  y the y-coordinate of the center of the ellipse
     * @param  semiMajorAxis is the semimajor axis of the ellipse
     * @param  semiMinorAxis is the semiminor axis of the ellipse
     * @throws IllegalArgumentException if either of the axes are negative
     */
    fun ellipse(x: Double, y: Double, semiMajorAxis: Double, semiMinorAxis: Double) {
        if (semiMajorAxis < 0) throw IllegalArgumentException("ellipse semimajor axis can't be negative")
        if (semiMinorAxis < 0) throw IllegalArgumentException("ellipse semiminor axis can't be negative")
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(2 * semiMajorAxis)
        val hs = factorY(2 * semiMinorAxis)
        if (ws <= 1 && hs <= 1)
            pixel(x, y)
        else
            offscreen.draw(Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
        draw()
    }

    /**
     * Draws an ellipse with given semimajor and semiminor axes, centered on (x, y).
     * @param  x the x-coordinate of the center of the ellipse
     * @param  y the y-coordinate of the center of the ellipse
     * @param  semiMajorAxis is the semimajor axis of the ellipse
     * @param  semiMinorAxis is the semiminor axis of the ellipse
     * @throws IllegalArgumentException if either of the axes are negative
     */
    fun filledEllipse(x: Double, y: Double, semiMajorAxis: Double, semiMinorAxis: Double) {
        if (semiMajorAxis < 0) throw IllegalArgumentException("ellipse semimajor axis can't be negative")
        if (semiMinorAxis < 0) throw IllegalArgumentException("ellipse semiminor axis can't be negative")
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(2 * semiMajorAxis)
        val hs = factorY(2 * semiMinorAxis)
        if (ws <= 1 && hs <= 1)
            pixel(x, y)
        else
            offscreen.fill(Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
        draw()
    }

    /**
     * Draws an arc of radius r, centered on (x, y), from angle1 to angle2 (in degrees).
     *
     * @param  x the x-coordinate of the center of the circle
     * @param  y the y-coordinate of the center of the circle
     * @param  r the radius of the circle
     * @param  angle1 the starting angle. 0 would mean an arc beginning at 3 o'clock.
     * @param  angle2 the angle at the end of the arc. For example, if
     * you want a 90 degree arc, then angle2 should be angle1 + 90.
     * @throws IllegalArgumentException if the radius of the circle is negative
     */
    fun arc(x: Double, y: Double, r: Double, angle1: Double, angle2: Double) {
        var angle2 = angle2
        if (r < 0) throw IllegalArgumentException("arc radius can't be negative")
        while (angle2 < angle1) angle2 += 360.0
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(2 * r)
        val hs = factorY(2 * r)
        if (ws <= 1 && hs <= 1)
            pixel(x, y)
        else
            offscreen.draw(Arc2D.Double(xs - ws / 2, ys - hs / 2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN))
        draw()
    }

    /**
     * Draws a square of side length 2r, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the square
     * @param  y the y-coordinate of the center of the square
     * @param  r radius is half the length of any side of the square
     * @throws IllegalArgumentException if r is negative
     */
    fun square(x: Double, y: Double, r: Double) {
        if (r < 0) throw IllegalArgumentException("square side length can't be negative")
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(2 * r)
        val hs = factorY(2 * r)
        if (ws <= 1 && hs <= 1)
            pixel(x, y)
        else
            offscreen.draw(Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
        draw()
    }

    /**
     * Draws a filled square of side length 2r, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the square
     * @param  y the y-coordinate of the center of the square
     * @param  r radius is half the length of any side of the square
     * @throws IllegalArgumentException if r is negative
     */
    fun filledSquare(x: Double, y: Double, r: Double) {
        if (r < 0) throw IllegalArgumentException("square side length can't be negative")
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(2 * r)
        val hs = factorY(2 * r)
        if (ws <= 1 && hs <= 1)
            pixel(x, y)
        else
            offscreen.fill(Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
        draw()
    }


    /**
     * Draws a rectangle of given half width and half height, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the rectangle
     * @param  y the y-coordinate of the center of the rectangle
     * @param  halfWidth is half the width of the rectangle
     * @param  halfHeight is half the height of the rectangle
     * @throws IllegalArgumentException if halfWidth or halfHeight is negative
     */
    fun rectangle(x: Double, y: Double, halfWidth: Double, halfHeight: Double) {
        if (halfWidth < 0) throw IllegalArgumentException("half width can't be negative")
        if (halfHeight < 0) throw IllegalArgumentException("half height can't be negative")
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(2 * halfWidth)
        val hs = factorY(2 * halfHeight)
        if (ws <= 1 && hs <= 1)
            pixel(x, y)
        else
            offscreen.draw(Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
        draw()
    }

    /**
     * Draws a filled rectangle of given half width and half height, centered on (x, y).
     *
     * @param  x the x-coordinate of the center of the rectangle
     * @param  y the y-coordinate of the center of the rectangle
     * @param  halfWidth is half the width of the rectangle
     * @param  halfHeight is half the height of the rectangle
     * @throws IllegalArgumentException if halfWidth or halfHeight is negative
     */
    fun filledRectangle(x: Double, y: Double, halfWidth: Double, halfHeight: Double) {
        if (halfWidth < 0) throw IllegalArgumentException("half width can't be negative")
        if (halfHeight < 0) throw IllegalArgumentException("half height can't be negative")
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(2 * halfWidth)
        val hs = factorY(2 * halfHeight)
        if (ws <= 1 && hs <= 1)
            pixel(x, y)
        else
            offscreen.fill(Rectangle2D.Double(xs - ws / 2, ys - hs / 2, ws, hs))
        draw()
    }

    /**
     * Draws a polygon with the given (x[i], y[i]) coordinates.
     *
     * @param x an array of all the x-coordindates of the polygon
     * @param y an array of all the y-coordindates of the polygon
     */
    fun polygon(x: DoubleArray, y: DoubleArray) {
        val n = x.size
        val path = GeneralPath()
        path.moveTo(scaleX(x[0]).toFloat(), scaleY(y[0]).toFloat())
        for (i in 0 until n)
            path.lineTo(scaleX(x[i]).toFloat(), scaleY(y[i]).toFloat())
        path.closePath()
        offscreen.draw(path)
        draw()
    }

    /**
     * Draws a filled polygon with the given (x[i], y[i]) coordinates.
     *
     * @param x an array of all the x-coordindates of the polygon
     * @param y an array of all the y-coordindates of the polygon
     */
    fun filledPolygon(x: DoubleArray, y: DoubleArray) {
        val n = x.size
        val path = GeneralPath()
        path.moveTo(scaleX(x[0]).toFloat(), scaleY(y[0]).toFloat())
        for (i in 0 until n)
            path.lineTo(scaleX(x[i]).toFloat(), scaleY(y[i]).toFloat())
        path.closePath()
        offscreen.fill(path)
        draw()
    }

    /**
     * Draws picture (gif, jpg, or png) centered on (x, y).
     *
     * @param  x the center x-coordinate of the image
     * @param  y the center y-coordinate of the image
     * @param  filename the name of the image/picture, e.g., "ball.gif"
     * @throws IllegalArgumentException if the image is corrupt
     * @throws IllegalArgumentException if `filename` is `null`
     */
    fun picture(x: Double, y: Double, filename: String?) {
        if (filename == null) throw IllegalArgumentException("filename argument is null")
        val image = getImage(filename)
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = image.width
        val hs = image.height
        if (ws < 0 || hs < 0) throw IllegalArgumentException("image $filename is corrupt")

        offscreen.drawImage(image, Math.round(xs - ws / 2.0).toInt(), Math.round(ys - hs / 2.0).toInt(), null)
        draw()
    }

    /**
     * Draws picture (gif, jpg, or png) centered on (x, y),
     * rotated given number of degrees.
     *
     * @param  x the center x-coordinate of the image
     * @param  y the center y-coordinate of the image
     * @param  filename the name of the image/picture, e.g., "ball.gif"
     * @param  degrees is the number of degrees to rotate counterclockwise
     * @throws IllegalArgumentException if the image is corrupt
     * @throws IllegalArgumentException if `filename` is `null`
     */
    fun picture(x: Double, y: Double, filename: String?, degrees: Double) {
        if (filename == null) throw IllegalArgumentException("filename argument is null")
        val image = getImage(filename)
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = image.width
        val hs = image.height
        if (ws < 0 || hs < 0) throw IllegalArgumentException("image $filename is corrupt")

        offscreen.rotate(Math.toRadians(-degrees), xs, ys)
        offscreen.drawImage(image, Math.round(xs - ws / 2.0).toInt(), Math.round(ys - hs / 2.0).toInt(), null)
        offscreen.rotate(Math.toRadians(+degrees), xs, ys)
        draw()
    }

    /**
     * Draws picture (gif, jpg, or png) centered on (x, y), rescaled to w-by-h.
     *
     * @param  x the center x coordinate of the image
     * @param  y the center y coordinate of the image
     * @param  filename the name of the image/picture, e.g., "ball.gif"
     * @param  w the width of the image
     * @param  h the height of the image
     * @throws IllegalArgumentException if the image is corrupt
     * @throws IllegalArgumentException if `filename` is `null`
     */
    fun picture(x: Double, y: Double, filename: String?, w: Double, h: Double) {
        if (filename == null) throw IllegalArgumentException("filename argument is null")
        val image = getImage(filename)
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(w)
        val hs = factorY(h)
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
     * Draws picture (gif, jpg, or png) centered on (x, y), rotated
     * given number of degrees, rescaled to w-by-h.
     *
     * @param  x the center x-coordinate of the image
     * @param  y the center y-coordinate of the image
     * @param  filename the name of the image/picture, e.g., "ball.gif"
     * @param  w the width of the image
     * @param  h the height of the image
     * @param  degrees is the number of degrees to rotate counterclockwise
     * @throws IllegalArgumentException if the image is corrupt
     * @throws IllegalArgumentException if `filename` is `null`
     */
    fun picture(x: Double, y: Double, filename: String?, w: Double, h: Double, degrees: Double) {
        if (filename == null) throw IllegalArgumentException("filename argument is null")
        val image = getImage(filename)
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = factorX(w)
        val hs = factorY(h)
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
     * Writes the given text string in the current font, centered on (x, y).
     *
     * @param x the center x-coordinate of the text
     * @param y the center y-coordinate of the text
     * @param s the text
     */
    fun text(x: Double, y: Double, s: String) {
        offscreen.font = font
        val metrics = offscreen.fontMetrics
        val xs = scaleX(x)
        val ys = scaleY(y)
        val ws = metrics.stringWidth(s)
        val hs = metrics.descent
        offscreen.drawString(s, (xs - ws / 2.0).toFloat(), (ys + hs).toFloat())
        draw()
    }

    /**
     * Writes the given text string in the current font, centered on (x, y) and
     * rotated by the specified number of degrees.
     *
     * @param x the center x-coordinate of the text
     * @param y the center y-coordinate of the text
     * @param s the text
     * @param degrees is the number of degrees to rotate counterclockwise
     */
    fun text(x: Double, y: Double, s: String, degrees: Double) {
        val xs = scaleX(x)
        val ys = scaleY(y)
        offscreen.rotate(Math.toRadians(-degrees), xs, ys)
        text(x, y, s)
        offscreen.rotate(Math.toRadians(+degrees), xs, ys)
    }

    /**
     * Writes the given text string in the current font, left-aligned at (x, y).
     *
     * @param x the x-coordinate of the text
     * @param y the y-coordinate of the text
     * @param s the text
     */
    fun textLeft(x: Double, y: Double, s: String) {
        offscreen.font = font
        val metrics = offscreen.fontMetrics
        val xs = scaleX(x)
        val ys = scaleY(y)
        // int ws = metrics.stringWidth(s);
        val hs = metrics.descent
        offscreen.drawString(s, xs.toFloat(), (ys + hs).toFloat())
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
        frame!!.repaint()
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
     * Saves this drawing to a file.
     *
     * @param  filename the name of the file (with suffix png, jpg, or gif)
     */
    fun save(filename: String) {
        val file = File(filename)
        val suffix = filename.substring(filename.lastIndexOf('.') + 1)

        // png files
        when {
            suffix.equals("png", ignoreCase = true) -> try {
                ImageIO.write(offscreenImage, suffix, file)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            suffix.equals("jpg", ignoreCase = true) -> {
                val raster = offscreenImage.raster
                val newRaster: WritableRaster
                newRaster = raster.createWritableChild(0, 0, width, height, 0, 0, intArrayOf(0, 1, 2))
                val cm = offscreenImage.colorModel as DirectColorModel
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
        }// need to change from ARGB to RGB for jpeg
        // reference: http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727
    }


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


    /***************************************************************************
     * Event-based interactions.
     */

    /**
     * Adds a [DrawListener] to listen to keyboard and mouse events.
     *
     * @param listener the {\tt DrawListener} argument
     */
    fun addListener(listener: DrawListener) {
        // ensure there is a window for listenting to events
        show()
        listeners.add(listener)
        frame!!.addKeyListener(this)
        frame!!.addMouseListener(this)
        frame!!.addMouseMotionListener(this)
        frame!!.isFocusable = true
    }

    /**
     * Returns true if the mouse is being pressed.
     *
     * @return `true` if the mouse is being pressed;
     * `false` otherwise
     */
    fun isMousePressed(): Boolean = synchronized(mouseLock) { return isMousePressed }

    /**
     * Returns true if the mouse is being pressed.
     *
     * @return `true` if the mouse is being pressed;
     * `false` otherwise
     */
    @Deprecated("replaced by {@link #isMousePressed()}")
    fun mousePressed(): Boolean = synchronized(mouseLock) { return isMousePressed }

    /**
     * Returns the x-coordinate of the mouse.
     * @return the x-coordinate of the mouse
     */
    fun mouseX(): Double = synchronized(mouseLock) { return mouseX }

    /**
     * Returns the y-coordinate of the mouse.
     *
     * @return the y-coordinate of the mouse
     */
    fun mouseY(): Double = synchronized(mouseLock) { return mouseY }

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
        if (e.button == MouseEvent.BUTTON1) {
            for (listener in listeners)
                listener.mousePressed(userX(e.x.toDouble()), userY(e.y.toDouble()))
        }
    }

    /**
     * This method cannot be called directly.
     */
    override fun mouseReleased(e: MouseEvent) {
        synchronized(mouseLock) {
            isMousePressed = false
        }
        if (e.button == MouseEvent.BUTTON1) {
            for (listener in listeners)
                listener.mouseReleased(userX(e.x.toDouble()), userY(e.y.toDouble()))
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
        // doesn't seem to work if a button is specified
        for (listener in listeners)
            listener.mouseDragged(userX(e.x.toDouble()), userY(e.y.toDouble()))
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
     * Returns true if the user has typed a key.
     *
     * @return `true` if the user has typed a key; `false` otherwise
     */
    fun hasNextKeyTyped(): Boolean = synchronized(keyLock) { return !keysTyped.isEmpty() }

    /**
     * The next key typed by the user.
     *
     * @return the next key typed by the user
     */
    fun nextKeyTyped(): Char = synchronized(keyLock) { return keysTyped.removeLast() }

    /**
     * Returns true if the keycode is being pressed.
     *
     *
     * This method takes as an argument the keycode (corresponding to a physical key).
     * It can handle action keys (such as F1 and arrow keys) and modifier keys
     * (such as shift and control).
     * See [KeyEvent] for a description of key codes.
     *
     * @param  keycode the keycode to check
     * @return `true` if `keycode` is currently being pressed;
     * `false` otherwise
     */
    fun isKeyPressed(keycode: Int): Boolean = synchronized(keyLock) { return keysDown.contains(keycode) }

    /**
     * This method cannot be called directly.
     */
    override fun keyTyped(e: KeyEvent) {
        synchronized(keyLock) {
            keysTyped.addFirst(e.keyChar)
        }
        // notify all listeners
        for (listener in listeners)
            listener.keyTyped(e.keyChar)
    }

    /**
     * This method cannot be called directly.
     */
    override fun keyPressed(e: KeyEvent) {
        synchronized(keyLock) {
            keysDown.add(e.keyCode)
        }
        // notify all listeners
        for (listener in listeners)
            listener.keyPressed(e.keyCode)
    }

    /**
     * This method cannot be called directly.
     */
    override fun keyReleased(e: KeyEvent) {
        synchronized(keyLock) {
            keysDown.remove(e.keyCode)
        }
        // notify all listeners
        for (listener in listeners)
            listener.keyPressed(e.keyCode)
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
         * Shade of blue used in Introduction to Programming in Java.
         * It is Pantone 300U. The RGB values are approximately (9, 90, 166).
         */
        val BOOK_BLUE: Color = Color(9, 90, 166)

        /**
         * Shade of light blue used in Introduction to Programming in Java.
         * The RGB values are approximately (103, 198, 243).
         */
        val BOOK_LIGHT_BLUE: Color = Color(103, 198, 243)

        /**
         * Shade of red used in *Algorithms, 4th edition*.
         * It is Pantone 1805U. The RGB values are approximately (150, 35, 31).
         */
        val BOOK_RED: Color = Color(150, 35, 31)

        /**
         * Shade of orange used in Princeton's identity.
         * It is PMS 158. The RGB values are approximately (245, 128, 37).
         */
        val PRINCETON_ORANGE: Color = Color(245, 128, 37)

        // default colors
        private val DEFAULT_PEN_COLOR = BLACK
        private val DEFAULT_CLEAR_COLOR = WHITE

        // boundary of drawing canvas, 0% border
        private const val BORDER = 0.0
        private const val DEFAULT_XMIN = 0.0
        private const val DEFAULT_XMAX = 1.0
        private const val DEFAULT_YMIN = 0.0
        private const val DEFAULT_YMAX = 1.0

        // default canvas size is SIZE-by-SIZE
        private const val DEFAULT_SIZE = 512

        // default pen radius
        private const val DEFAULT_PEN_RADIUS = 0.002

        // default font
        private val DEFAULT_FONT = Font("SansSerif", Font.PLAIN, 16)

        private fun getImage(filename: String): BufferedImage {

            // from a file or URL
            try {
                val url = URL(filename)
                return ImageIO.read(url)
            } catch (e: IOException) {
                // ignore
            }

            // in case file is inside a .jar (classpath relative to StdDraw)
            try {
                val url = StdDraw::class.java.getResource(filename)
                return ImageIO.read(url)
            } catch (e: IOException) {
                // ignore
            }

            // in case file is inside a .jar (classpath relative to root of jar)
            try {
                val url = StdDraw::class.java.getResource("/$filename")
                return ImageIO.read(url)
            } catch (e: IOException) {
                // ignore
            }

            throw IllegalArgumentException("image $filename not found")
        }


        /**
         * Test client.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // create one drawing window
            val draw1 = Draw("Test client 1")
            draw1.square(.2, .8, .1)
            draw1.filledSquare(.8, .8, .2)
            draw1.circle(.8, .2, .2)
            draw1.penColor = Draw.MAGENTA
            draw1.penRadius = .02
            draw1.arc(.8, .2, .1, 200.0, 45.0)

            // create another one
            val draw2 = Draw("Test client 2")
            draw2.setCanvasSize(900, 200)
            // draw a blue diamond
            draw2.setPenRadius()
            draw2.penColor = Draw.BLUE
            val x = doubleArrayOf(.1, .2, .3, .2)
            val y = doubleArrayOf(.2, .3, .2, .1)
            draw2.filledPolygon(x, y)

            // text
            draw2.penColor = Draw.BLACK
            draw2.text(0.2, 0.5, "bdfdfdfdlack text")
            draw2.penColor = Draw.WHITE
            draw2.text(0.8, 0.8, "white text")
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
