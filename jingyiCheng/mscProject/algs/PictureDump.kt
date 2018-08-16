/******************************************************************************
 * Compilation:  javac PictureDump.java
 * Execution:    java PictureDump width height < file
 * Dependencies: BinaryStdIn.kt Picture.kt
 * Data file:    http://introcs.cs.princeton.edu/stdlib/abra.txt
 *
 * Reads in a binary file and writes out the bits as w-by-h picture,
 * with the 1 bits in black and the 0 bits in white.
 *
 * % more abra.txt
 * ABRACADABRA!
 *
 * % java PictureDump 16 6 < abra.txt
 *
 */

package jingyiCheng.mscProject.algs

import java.awt.Color

/**
 * The `PictureDump` class provides a client for displaying the contents
 * of a binary file as a black-and-white picture.
 *
 *
 * For additional documentation,
 * see [Section 5.5](https://algs4.cs.princeton.edu/55compress) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 *
 * See also [BinaryDump] and [HexDump].
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object PictureDump {
    /**
     * Reads in a sequence of bytes from standard input and draws
     * them to standard drawing output as a width-by-height picture,
     * using black for 1 and white for 0 (and red for any leftover
     * pixels).
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val width = Integer.parseInt(args[0])
        val height = Integer.parseInt(args[1])
        val picture = Picture(width, height)
        for (row in 0 until height)
            for (col in 0 until width)
                if (!BinaryStdIn.isEmpty) {
                    val bit = BinaryStdIn.readBoolean()
                    if (bit)
                        picture[col, row] = Color.BLACK
                    else
                        picture[col, row] = Color.WHITE
                } else
                    picture[col, row] = Color.RED
        picture.show()
    }
}// Do not instantiate.

/******************************************************************************
 * This Kotlin file is automatically translated from Java using the
 * Java-to-Kotlin converter by JetBrains with manual adjustments.
 *
 * Following is the copyright contents of the original file:
 *
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This original file is part of algs4.jar, which accompanies the
 *  textbook
 *  Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *  Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *  http://algs4.cs.princeton.edu
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
 */