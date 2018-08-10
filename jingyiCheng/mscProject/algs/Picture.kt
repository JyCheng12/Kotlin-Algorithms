package jingyiCheng.mscProject.algs

import java.awt.Color
import java.awt.FileDialog
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.KeyStroke


/**
 * This class provides methods for manipulating individual pixels of
 * an image using the RGB color format. The alpha component (for transparency)
 * is not currently supported.
 * The original image can be read from a `PNG`, `GIF`,
 * or `JPEG` file or the user can create a blank image of a given dimension.
 * This class includes methods for displaying the image in a window on
 * the screen or saving it to a file.
 *
 *
 * Pixel (*col*, *row*) is column *col* and row *row*.
 * By default, the origin (0, 0) is the pixel in the top-left corner,
 * which is a common convention in image processing.
 * The method [.setOriginLowerLeft] change the origin to the lower left.
 *
 *
 * The `get()` and `set()` methods use [Color] objects to get
 * or set the color of the specified pixel.
 * The `getRGB()` and `setRGB()` methods use a 32-bit `int`
 * to encode the color, thereby avoiding the need to create temporary
 * `Color` objects. The red (R), green (G), and blue (B) components
 * are encoded using the least significant 24 bits.
 * Given a 32-bit `int` encoding the color, the following code extracts
 * the RGB components:
 * <blockquote><pre>
 * int r = (rgb >> 16) & 0xFF;
 * int g = (rgb >>  8) & 0xFF;
 * int b = (rgb >>  0) & 0xFF;
</pre></blockquote> *
 * Given the RGB components (8-bits each) of a color,
 * the following statement packs it into a 32-bit `int`:
 * <blockquote><pre>
 * int rgb = (r << 16) + (g << 8) + (b << 0);
</pre></blockquote> *
 *
 *
 * A *W*-by-<en>H picture uses ~ 4 *W H* bytes of memory,
 * since the color of each pixel is encoded as a 32-bit `int`.
</en> *
 *
 * For additional documentation, see
 * [Section 3.1](https://introcs.cs.princeton.edu/31datatype) of
 * *Computer Science: An Interdisciplinary Approach*
 * by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class Picture : ActionListener {
    private var image: BufferedImage?               // the rasterized image
    private var frame: JFrame? = null                      // on-screen view
    private var filename: String? = null                   // name of file
    private var isOriginUpperLeft = true  // location of origin
    val width: Int
    val height: Int           // width and height

    /**
     * Returns a [JLabel] containing this picture, for embedding in a [JPanel],
     * [JFrame] or other GUI widget.
     *
     * @return the `JLabel`
     */
    val jLabel: JLabel?
        get() = if (image == null) null
            else JLabel(ImageIcon(image!!))

    /**
     * Creates a `width`-by-`height` picture, with `width` columns
     * and `height` rows, where each pixel is black.
     *
     * @param width the width of the picture
     * @param height the height of the picture
     * @throws IllegalArgumentException if `width` is negative
     * @throws IllegalArgumentException if `height` is negative
     */
    constructor(width: Int, height: Int) {
        if (width < 0) throw IllegalArgumentException("width must be non-negative")
        if (height < 0) throw IllegalArgumentException("height must be non-negative")
        this.width = width
        this.height = height
        image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    }

    /**
     * Creates a new picture that is a deep copy of the argument picture.
     *
     * @param  picture the picture to copy
     * @throws IllegalArgumentException if `picture` is `null`
     */
    constructor(picture: Picture?) {
        if (picture == null) throw IllegalArgumentException("constructor argument is null")

        width = picture.width
        height = picture.height
        image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        filename = picture.filename
        isOriginUpperLeft = picture.isOriginUpperLeft
        for (col in 0 until width)
            for (row in 0 until height)
                image!!.setRGB(col, row, picture.image!!.getRGB(col, row))
    }

    /**
     * Creates a picture by reading an image from a file or URL.
     *
     * @param  filename the name of the file (.png, .gif, or .jpg) or URL.
     * @throws IllegalArgumentException if cannot read image
     * @throws IllegalArgumentException if `filename` is `null`
     */
    constructor(filename: String?) {
        if (filename == null) throw IllegalArgumentException("constructor argument is null")

        this.filename = filename
        try {
            // try to read from file in working directory
            val file = File(filename)
            if (file.isFile) {
                image = ImageIO.read(file)
            } else {
                var url: URL? = javaClass.getResource(filename)
                url = url ?: URL(filename)
                image = ImageIO.read(url)
            }// now try to read from file in same directory as this .class file

            if (image == null) throw IllegalArgumentException("could not read image file: $filename")
            width = image!!.getWidth(null)
            height = image!!.getHeight(null)
        } catch (ioe: IOException) {
            throw IllegalArgumentException("could not open image file: $filename", ioe)
        }
    }

    /**
     * Creates a picture by reading the image from a PNG, GIF, or JPEG file.
     *
     * @param file the file
     * @throws IllegalArgumentException if cannot read image
     * @throws IllegalArgumentException if `file` is `null`
     */
    constructor(file: File?) {
        if (file == null) throw IllegalArgumentException("constructor argument is null")
        try {
            image = ImageIO.read(file)
        } catch (ioe: IOException) {
            throw IllegalArgumentException("could not open file: $file", ioe)
        }

        if (image == null) throw IllegalArgumentException("could not read file: $file")
        width = image!!.getWidth(null)
        height = image!!.getHeight(null)
        filename = file.name
    }

    /**
     * Sets the origin to be the upper left pixel. This is the default.
     */
    fun setOriginUpperLeft() {
        isOriginUpperLeft = true
    }

    /**
     * Sets the origin to be the lower left pixel.
     */
    fun setOriginLowerLeft() {
        isOriginUpperLeft = false
    }

    /**
     * Displays the picture in a window on the screen.
     */
    fun show() {
        // create the GUI for viewing the image if needed

        if (frame == null) {
            frame = JFrame()

            val menuBar = JMenuBar()
            val menu = JMenu("File")
            menuBar.add(menu)
            val menuItem1 = JMenuItem(" Save...   ")
            menuItem1.addActionListener(this)
            menuItem1.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    Toolkit.getDefaultToolkit().menuShortcutKeyMask)
            menu.add(menuItem1)
            frame!!.jMenuBar = menuBar

            frame!!.contentPane = jLabel
            // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame!!.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            if (filename == null)
                frame!!.title = "$width-by-$height"
            else
                frame!!.title = filename
            frame!!.isResizable = false
            frame!!.pack()
            frame!!.isVisible = true
        }

        // draw
        frame!!.repaint()
    }

    private fun validateRowIndex(row: Int) {
        if (row < 0 || row >= height) throw IllegalArgumentException("row index must be between 0 and ${height - 1}: $row")
    }

    private fun validateColumnIndex(col: Int) {
        if (col < 0 || col >= width) throw IllegalArgumentException("column index must be between 0 and ${width - 1}: $col")
    }

    /**
     * Returns the color of pixel (`col`, `row`) as a [java.awt.Color].
     *
     * @param col the column index
     * @param row the row index
     * @return the color of pixel (`col`, `row`)
     * @throws IllegalArgumentException unless both `0 <= col < width` and `0 <= row < height`
     */
    operator fun get(col: Int, row: Int): Color {
        validateColumnIndex(col)
        validateRowIndex(row)
        val rgb = getRGB(col, row)
        return Color(rgb)
    }

    /**
     * Returns the color of pixel (`col`, `row`) as an `int`.
     * Using this method can be more efficient than [.get] because
     * it does not create a `Color` object.
     *
     * @param col the column index
     * @param row the row index
     * @return the integer representation of the color of pixel (`col`, `row`)
     * @throws IllegalArgumentException unless both `0 <= col < width` and `0 <= row < height`
     */
    fun getRGB(col: Int, row: Int): Int {
        validateColumnIndex(col)
        validateRowIndex(row)
        return if (isOriginUpperLeft)
            image!!.getRGB(col, row)
        else
            image!!.getRGB(col, height - row - 1)
    }

    /**
     * Sets the color of pixel (`col`, `row`) to given color.
     *
     * @param col the column index
     * @param row the row index
     * @param color the color
     * @throws IllegalArgumentException unless both `0 <= col < width` and `0 <= row < height`
     * @throws IllegalArgumentException if `color` is `null`
     */
    operator fun set(col: Int, row: Int, color: Color?) {
        validateColumnIndex(col)
        validateRowIndex(row)
        if (color == null) throw IllegalArgumentException("color argument is null")
        val rgb = color.rgb
        setRGB(col, row, rgb)
    }

    /**
     * Sets the color of pixel (`col`, `row`) to given color.
     *
     * @param col the column index
     * @param row the row index
     * @param rgb the integer representation of the color
     * @throws IllegalArgumentException unless both `0 <= col < width` and `0 <= row < height`
     */
    fun setRGB(col: Int, row: Int, rgb: Int) {
        validateColumnIndex(col)
        validateRowIndex(row)
        if (isOriginUpperLeft)
            image!!.setRGB(col, row, rgb)
        else
            image!!.setRGB(col, height - row - 1, rgb)
    }

    /**
     * Returns true if this picture is equal to the argument picture.
     *
     * @param other the other picture
     * @return `true` if this picture is the same dimension as `other`
     * and if all pixels have the same color; `false` otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other.javaClass != this.javaClass) return false
        val that = other as Picture?
        if (this.width != that!!.width) return false
        if (this.height != that.height) return false
        for (col in 0 until width)
            for (row in 0 until height)
                if (this.getRGB(col, row) != that.getRGB(col, row)) return false
        return true
    }

    /**
     * Returns a string representation of this picture.
     * The result is a `width`-by-`height` matrix of pixels,
     * where the color of a pixel is represented using 6 hex digits to encode
     * the red, green, and blue components.
     *
     * @return a string representation of this picture
     */
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("$width-by-$height picture (RGB values given in hex)\n")
        for (row in 0 until height) {
            for (col in 0 until width) {
                val rgb: Int = if (isOriginUpperLeft)
                    image!!.getRGB(col, row)
                else
                    image!!.getRGB(col, height - row - 1)
                sb.append(String.format("#%06X ", rgb and 0xFFFFFF))
            }
            sb.append("\n")
        }
        return sb.toString().trim { it <= ' ' }
    }

    /**
     * This operation is not supported because pictures are mutable.
     *
     * @return does not return a value
     * @throws UnsupportedOperationException if called
     */
    override fun hashCode() = throw UnsupportedOperationException("hashCode() is not supported because pictures are mutable")

    /**
     * Saves the picture to a file in a standard image format.
     * The filetype must be .png or .jpg.
     *
     * @param filename the name of the file
     * @throws IllegalArgumentException if `name` is `null`
     */
    fun save(filename: String?) {
        if (filename == null) throw IllegalArgumentException("argument to save() is null")
        save(File(filename))
    }

    /**
     * Saves the picture to a file in a PNG or JPEG image format.
     *
     * @param  file the file
     * @throws IllegalArgumentException if `file` is `null`
     */
    fun save(file: File?) {
        if (file == null) throw IllegalArgumentException("argument to save() is null")
        filename = file.name
        if (frame != null) frame!!.title = filename
        val suffix = filename!!.substring(filename!!.lastIndexOf('.') + 1)
        if ("jpg".equals(suffix, ignoreCase = true) || "png".equals(suffix, ignoreCase = true)) {
            try {
                ImageIO.write(image!!, suffix, file)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else
            println("Error: filename must end in .jpg or .png")
    }

    /**
     * Opens a save dialog box when the user selects "Save As" from the menu.
     */
    override fun actionPerformed(e: ActionEvent) {
        val chooser = FileDialog(frame, "Use a .png or .jpg extension", FileDialog.SAVE)
        chooser.isVisible = true
        if (chooser.file != null)
            save(chooser.directory + File.separator + chooser.file)
    }

    companion object {
        /**
         * Unit tests this `Picture` data type.
         * Reads a picture specified by the command-line argument,
         * and shows it in a window on the screen.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val picture = Picture(args[0])
            System.out.printf("%d-by-%d\n", picture.width, picture.height)
            picture.show()
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
