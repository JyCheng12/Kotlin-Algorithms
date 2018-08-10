/******************************************************************************
 * Compilation:  javac BinaryStdOut.java
 * Execution:    java BinaryStdOut
 * Dependencies: none
 *
 * Write binary data to standard output, either one 1-bit boolean,
 * one 8-bit char, one 32-bit int, one 64-bit double, one 32-bit float,
 * or one 64-bit long at a time.
 *
 * The bytes written are not aligned.
 *
 */

package jingyiCheng.mscProject.algs

import java.io.BufferedOutputStream
import java.io.IOException

/**
 * *Binary standard output*. This class provides methods for converting
 * primtive type variables (`boolean`, `byte`, `char`,
 * `int`, `long`, `float`, and `double`)
 * to sequences of bits and writing them to standard output.
 * Uses big-endian (most-significant byte first).
 *
 *
 * The client must `flush()` the output stream when finished writing bits.
 *
 *
 * The client should not intermix calls to `BinaryStdOut` with calls
 * to `StdOut` or `System.out`; otherwise unexpected behavior
 * will result.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object BinaryStdOut {
    private lateinit var out: BufferedOutputStream  // output stream (standard output)
    private var buffer: Int = 0                // 8-bit buffer of bits to write
    private var n: Int = 0                     // number of bits remaining in buffer
    private var isInitialized: Boolean = false     // has BinaryStdOut been called for first time?

    // initialize BinaryStdOut
    private fun initialize() {
        out = BufferedOutputStream(System.out)
        buffer = 0
        n = 0
        isInitialized = true
    }

    /**
     * Writes the specified bit to standard output.
     */
    private fun writeBit(bit: Boolean) {
        if (!isInitialized) initialize()

        // add bit to buffer
        buffer = buffer shl 1
        if (bit) buffer = buffer or 1

        // if buffer is full (8 bits), write out as a single byte
        n++
        if (n == 8) clearBuffer()
    }

    /**
     * Writes the 8-bit byte to standard output.
     */
    private fun writeByte(x: Int) {
        if (!isInitialized) initialize()

        assert(x in 0..255)

        // optimized if byte-aligned
        if (n == 0) {
            try {
                out.write(x)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return
        }

        // otherwise write one bit at a time
        for (i in 0..7) {
            val bit = x.ushr(8 - i - 1) and 1 == 1
            writeBit(bit)
        }
    }

    // write out any remaining bits in buffer to standard output, padding with 0s
    private fun clearBuffer() {
        if (!isInitialized) initialize()

        if (n == 0) return
        if (n > 0) buffer = buffer shl 8 - n
        try {
            out.write(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        n = 0
        buffer = 0
    }

    /**
     * Flushes standard output, padding 0s if number of bits written so far
     * is not a multiple of 8.
     */
    fun flush() {
        clearBuffer()
        try {
            out.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Flushes and closes standard output. Once standard output is closed, you can no
     * longer write bits to it.
     */
    fun close() {
        flush()
        try {
            out.close()
            isInitialized = false
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Writes the specified bit to standard output.
     * @param x the `boolean` to write.
     */
    fun write(x: Boolean) = writeBit(x)

    /**
     * Writes the 8-bit byte to standard output.
     * @param x the `byte` to write.
     */
    fun write(x: Byte) = writeByte(x.toInt() and 0xff)

    /**
     * Writes the 32-bit int to standard output.
     * @param x the `int` to write.
     */
    fun write(x: Int) {
        writeByte(x.ushr(24) and 0xff)
        writeByte(x.ushr(16) and 0xff)
        writeByte(x.ushr(8) and 0xff)
        writeByte(x.ushr(0) and 0xff)
    }

    /**
     * Writes the r-bit int to standard output.
     * @param x the `int` to write.
     * @param r the number of relevant bits in the char.
     * @throws IllegalArgumentException if `r` is not between 1 and 32.
     * @throws IllegalArgumentException if `x` is not between 0 and 2<sup>r</sup> - 1.
     */
    fun write(x: Int, r: Int) {
        if (r == 32) {
            write(x)
            return
        }
        if (r < 1 || r > 32) throw IllegalArgumentException("Illegal value for r = $r")
        if (x < 0 || x >= 1 shl r) throw IllegalArgumentException("Illegal $r-bit char = $x")
        for (i in 0 until r) {
            val bit = x.ushr(r - i - 1) and 1 == 1
            writeBit(bit)
        }
    }

    /**
     * Writes the 64-bit double to standard output.
     * @param x the `double` to write.
     */
    fun write(x: Double) = write(x.toRawBits())

    /**
     * Writes the 64-bit long to standard output.
     * @param x the `long` to write.
     */
    fun write(x: Long) {
        writeByte((x.ushr(56) and 0xff).toInt())
        writeByte((x.ushr(48) and 0xff).toInt())
        writeByte((x.ushr(40) and 0xff).toInt())
        writeByte((x.ushr(32) and 0xff).toInt())
        writeByte((x.ushr(24) and 0xff).toInt())
        writeByte((x.ushr(16) and 0xff).toInt())
        writeByte((x.ushr(8) and 0xff).toInt())
        writeByte((x.ushr(0) and 0xff).toInt())
    }

    /**
     * Writes the 32-bit float to standard output.
     * @param x the `float` to write.
     */
    fun write(x: Float) = write(x.toRawBits())

    /**
     * Writes the 16-bit int to standard output.
     * @param x the `short` to write.
     */
    fun write(x: Short) {
        writeByte(x.toInt().ushr(8) and 0xff)
        writeByte(x.toInt().ushr(0) and 0xff)
    }

    /**
     * Writes the 8-bit char to standard output.
     * @param x the `char` to write.
     * @throws IllegalArgumentException if `x` is not betwen 0 and 255.
     */
    fun write(x: Char) {
        if (x.toInt() < 0 || x.toInt() >= 256) throw IllegalArgumentException("Illegal 8-bit char = $x")
        writeByte(x.toInt())
    }

    /**
     * Writes the r-bit char to standard output.
     * @param x the `char` to write.
     * @param r the number of relevant bits in the char.
     * @throws IllegalArgumentException if `r` is not between 1 and 16.
     * @throws IllegalArgumentException if `x` is not between 0 and 2<sup>r</sup> - 1.
     */
    fun write(x: Char, r: Int) {
        if (r == 8) {
            write(x)
            return
        }
        if (r < 1 || r > 16) throw IllegalArgumentException("Illegal value for r = $r")
        if (x.toInt() >= 1 shl r) throw IllegalArgumentException("Illegal $r-bit char = $x")
        for (i in 0 until r) {
            val bit = x.toInt().ushr(r - i - 1) and 1 == 1
            writeBit(bit)
        }
    }

    /**
     * Writes the string of 8-bit characters to standard output.
     * @param s the `String` to write.
     * @throws IllegalArgumentException if any character in the string is not
     * between 0 and 255.
     */
    fun write(s: String) {
        for (i in 0 until s.length)
            write(s[i])
    }

    /**
     * Writes the string of r-bit characters to standard output.
     * @param s the `String` to write.
     * @param r the number of relevants bits in each character.
     * @throws IllegalArgumentException if r is not between 1 and 16.
     * @throws IllegalArgumentException if any character in the string is not
     * between 0 and 2<sup>r</sup> - 1.
     */
    fun write(s: String, r: Int) {
        for (i in 0 until s.length)
            write(s[i], r)
    }

    /**
     * Tests the methods in this class.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val m = Integer.parseInt(args[0])

        // write n integers to binary standard output
        for (i in 0 until m)
            BinaryStdOut.write(i)
        BinaryStdOut.flush()
    }
}// don't instantiate

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
