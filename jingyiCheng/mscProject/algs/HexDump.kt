/******************************************************************************
 * Compilation:  javac HexDump.java
 * Execution:    java HexDump < file
 * Dependencies: BinaryStdIn.kt StdOut.kt
 * Data file:    https://algs4.cs.princeton.edu/55compression/abra.txt
 *
 * Reads in a binary file and writes out the bytes in hex, 16 per line.
 *
 * % more abra.txt
 * ABRACADABRA!
 *
 * % java HexDump 16 < abra.txt
 * 41 42 52 41 43 41 44 41 42 52 41 21
 * 96 bits
 *
 *
 * Remark
 * --------------------------
 * - Similar to the Unix utilities od (octal dump) or hexdump (hexadecimal dump).
 *
 * % od -t x1 < abra.txt
 * 0000000 41 42 52 41 43 41 44 41 42 52 41 21
 * 0000014
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `HexDump` class provides a client for displaying the contents
 * of a binary file in hexadecimal.
 *
 *
 * For additional documentation,
 * see [Section 5.5](https://algs4.cs.princeton.edu/55compress) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 *
 * See also [BinaryDump] and [PictureDump].
 * For more full-featured versions, see the Unix utilities
 * `od` (octal dump) and `hexdump` (hexadecimal dump).
 *
 *
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object HexDump {
    /**
     * Reads in a sequence of bytes from standard input and writes
     * them to standard output using hexademical notation, k hex digits
     * per line, where k is given as a command-line integer (defaults
     * to 16 if no integer is specified); also writes the number
     * of bits.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        var bytesPerLine = 16
        if (args.size == 1) {
            bytesPerLine = Integer.parseInt(args[0])
        }

        var i = 0
        while (!BinaryStdIn.isEmpty) {
            if (bytesPerLine == 0) {
                BinaryStdIn.readChar()
                i++
                continue
            }
            when {
                i == 0 -> StdOut.print("")
                i % bytesPerLine == 0 -> StdOut.printf("\n", i)
                else -> StdOut.print(" ")
            }
            val c = BinaryStdIn.readChar()
            StdOut.printf("%02x", c.toInt() and 0xff)
            i++
        }
        if (bytesPerLine != 0) StdOut.println()
        StdOut.println("${i * 8} bits")
    }
}// Do not instantiate.

/******************************************************************************
 * Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 * This file is part of algs4.jar, which accompanies the textbook
 *
 * Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 * Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 * http://algs4.cs.princeton.edu
 *
 *
 * algs4.jar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * algs4.jar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 */
