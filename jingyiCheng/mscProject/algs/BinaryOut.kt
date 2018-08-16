/******************************************************************************
 * Compilation:  javac BinaryOut.java
 * Execution:    java BinaryOut
 * Dependencies: none
 *
 * Write binary data to an output stream, either one 1-bit boolean,
 * one 8-bit char, one 32-bit int, one 64-bit double, one 32-bit float,
 * or one 64-bit long at a time. The output stream can be standard
 * output, a file, an OutputStream or a Socket.
 *
 * The bytes written are not aligned.
 *
 */

package jingyiCheng.mscProject.algs

import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.Socket

/**
 * *Binary output*. This class provides methods for converting
 * primtive type variables (`boolean`, `byte`, `char`,
 * `int`, `long`, `float`, and `double`)
 * to sequences of bits and writing them to an output stream.
 * The output stream can be standard output, a file, an OutputStream or a Socket.
 * Uses big-endian (most-significant byte first).
 *
 *
 * The client must `flush()` the output stream when finished writing bits.
 *
 *
 * The client should not intermix calls to `BinaryOut` with calls
 * to `Out`; otherwise unexpected behavior will result.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class BinaryOut {
    private lateinit var out: BufferedOutputStream // the output stream
    private var buffer: Int = 0                // 8-bit buffer of bits to write out
    private var n: Int = 0                     // number of bits remaining in buffer

    /**
     * Initializes a binary output stream from standard output.
     */
    constructor() {
        out = BufferedOutputStream(System.out)
    }

    /**
     * Initializes a binary output stream from an `OutputStream`.
     * @param os the `OutputStream`
     */
    constructor(os: OutputStream) {
        out = BufferedOutputStream(os)
    }

    /**
     * Initializes a binary output stream from a file.
     * @param filename the name of the file
     */
    constructor(filename: String) {
        try {
            val os = FileOutputStream(filename)
            out = BufferedOutputStream(os)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Initializes a binary output stream from a socket.
     * @param socket the socket
     */
    constructor(socket: Socket) {
        try {
            val os = socket.getOutputStream()
            out = BufferedOutputStream(os)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    /**
     * Writes the specified bit to the binary output stream.
     * @param x the bit
     */
    private fun writeBit(x: Boolean) {
        // add bit to buffer
        buffer = buffer shl 1
        if (x) buffer = buffer or 1

        // if buffer is full (8 bits), write out as a single byte
        n++
        if (n == 8) clearBuffer()
    }

    /**
     * Writes the 8-bit byte to the binary output stream.
     * @param x the byte
     */
    private fun writeByte(x: Int) {
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

    // write out any remaining bits in buffer to the binary output stream, padding with 0s
    private fun clearBuffer() {
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
     * Flushes the binary output stream, padding 0s if number of bits written so far
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
     * Flushes and closes the binary output stream.
     * Once it is closed, bits can no longer be written.
     */
    fun close() {
        flush()
        try {
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    /**
     * Writes the specified bit to the binary output stream.
     * @param x the `boolean` to write
     */
    fun write(x: Boolean) = writeBit(x)

    /**
     * Writes the 8-bit byte to the binary output stream.
     * @param x the `byte` to write.
     */
    fun write(x: Byte) = writeByte(x.toInt() and 0xff)

    /**
     * Writes the 32-bit int to the binary output stream.
     * @param x the `int` to write
     */
    fun write(x: Int) {
        writeByte(x.ushr(24) and 0xff)
        writeByte(x.ushr(16) and 0xff)
        writeByte(x.ushr(8) and 0xff)
        writeByte(x.ushr(0) and 0xff)
    }

    /**
     * Writes the r-bit int to the binary output stream.
     *
     * @param  x the `int` to write
     * @param  r the number of relevant bits in the char
     * @throws IllegalArgumentException unless `r` is between 1 and 32
     * @throws IllegalArgumentException unless `x` is between 0 and 2<sup>r</sup> - 1
     */
    fun write(x: Int, r: Int) {
        if (r == 32) {
            write(x)
            return
        }
        if (r < 1 || r > 32) throw IllegalArgumentException("Illegal value for r = $r")
        if (x >= 1 shl r) throw IllegalArgumentException("Illegal $r-bit char = $x")
        for (i in 0 until r) {
            val bit = x.ushr(r - i - 1) and 1 == 1
            writeBit(bit)
        }
    }


    /**
     * Writes the 64-bit double to the binary output stream.
     * @param x the `double` to write
     */
    fun write(x: Double) = write(x.toRawBits())

    /**
     * Writes the 64-bit long to the binary output stream.
     * @param x the `long` to write
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
     * Writes the 32-bit float to the binary output stream.
     * @param x the `float` to write
     */
    fun write(x: Float) = write(x.toRawBits())

    /**
     * Write the 16-bit int to the binary output stream.
     * @param x the `short` to write.
     */
    fun write(x: Short) {
        writeByte(x.toInt().ushr(8) and 0xff)
        writeByte(x.toInt().ushr(0) and 0xff)
    }

    /**
     * Writes the 8-bit char to the binary output stream.
     *
     * @param  x the `char` to write
     * @throws IllegalArgumentException unless `x` is betwen 0 and 255
     */
    fun write(x: Char) {
        if (x.toInt() < 0 || x.toInt() >= 256) throw IllegalArgumentException("Illegal 8-bit char = $x")
        writeByte(x.toInt())
    }

    /**
     * Writes the r-bit char to the binary output stream.
     *
     * @param  x the `char` to write
     * @param  r the number of relevant bits in the char
     * @throws IllegalArgumentException unless `r` is between 1 and 16
     * @throws IllegalArgumentException unless `x` is between 0 and 2<sup>r</sup> - 1
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
     * Writes the string of 8-bit characters to the binary output stream.
     *
     * @param  s the `String` to write
     * @throws IllegalArgumentException if any character in the string is not
     * between 0 and 255
     */
    fun write(s: String) {
        for (i in 0 until s.length)
            write(s[i])
    }


    /**
     * Writes the string of r-bit characters to the binary output stream.
     * @param  s the `String` to write
     * @param  r the number of relevants bits in each character
     * @throws IllegalArgumentException unless r is between 1 and 16
     * @throws IllegalArgumentException if any character in the string is not
     * between 0 and 2<sup>r</sup> - 1
     */
    fun write(s: String, r: Int) {
        for (i in 0 until s.length)
            write(s[i], r)
    }

    companion object {
        /**
         * Test client. Read bits from standard input and write to the file
         * specified on command line.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // create binary output stream to write to file
            val filename = args[0]
            val out = BinaryOut(filename)
            val `in` = BinaryIn()

            // read from standard input and write to file
            while (!`in`.isEmpty) {
                val c = `in`.readChar()
                out.write(c)
            }
            out.flush()
        }
    }
}

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