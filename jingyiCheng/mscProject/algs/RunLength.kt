/******************************************************************************
 * Compilation:  javac RunLength.java
 * Execution:    java RunLength - < input.txt   (compress)
 * Execution:    java RunLength + < input.txt   (expand)
 * Dependencies: BinaryIn.kt BinaryOut.kt
 * Data files:   https://algs4.cs.princeton.edu/55compression/4runs.bin
 * https://algs4.cs.princeton.edu/55compression/q32x48.bin
 * https://algs4.cs.princeton.edu/55compression/q64x96.bin
 *
 * Compress or expand binary input from standard input using
 * run-length encoding.
 *
 * % java BinaryDump 40 < 4runs.bin
 * 0000000000000001111111000000011111111111
 * 40 bits
 *
 * This has runs of 15 0s, 7 1s, 7 0s, and 11 1s.
 *
 * % java RunLength - < 4runs.bin | java HexDump
 * 0f 07 07 0b
 * 4 bytes
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `RunLength` class provides static methods for compressing
 * and expanding a binary input using run-length coding with 8-bit
 * run lengths.
 *
 *
 * For additional documentation,
 * see [Section 5.5](https://algs4.cs.princeton.edu/55compress) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object RunLength {
    private const val R = 256
    private const val LG_R = 8

    /**
     * Reads a sequence of bits from standard input (that are encoded
     * using run-length encoding with 8-bit run lengths); decodes them;
     * and writes the results to standard output.
     */
    fun expand() {
        var b = false
        while (!BinaryStdIn.isEmpty) {
            val run = BinaryStdIn.readInt(LG_R)
            for (i in 0 until run)
                BinaryStdOut.write(b)
            b = !b
        }
        BinaryStdOut.close()
    }

    /**
     * Reads a sequence of bits from standard input; compresses
     * them using run-length coding with 8-bit run lengths; and writes the
     * results to standard output.
     */
    fun compress() {
        var run: Char = 0.toChar()
        var old = false
        while (!BinaryStdIn.isEmpty) {
            val b = BinaryStdIn.readBoolean()
            if (b != old) {
                BinaryStdOut.write(run, LG_R)
                run = 1.toChar()
                old = !old
            } else {
                if (run.toInt() == R - 1) {
                    BinaryStdOut.write(run, LG_R)
                    run = 0.toChar()
                    BinaryStdOut.write(run, LG_R)
                }
                run++
            }
        }
        BinaryStdOut.write(run, LG_R)
        BinaryStdOut.close()
    }

    /**
     * Sample client that calls `compress()` if the command-line
     * argument is "-" an `expand()` if it is "+".
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        when (args[0]) {
            "-" -> compress()
            "+" -> expand()
            else -> throw IllegalArgumentException("Illegal command line argument")
        }
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
