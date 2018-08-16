/******************************************************************************
 * Compilation:  javac BinaryStdIn.java
 * Execution:    java BinaryStdIn < input > output
 * Dependencies: none
 *
 * Supports reading binary data from standard input.
 *
 * % java BinaryStdIn < input.jpg > output.jpg
 * % diff input.jpg output.jpg
 *
 */

package jingyiCheng.mscProject.algs

import java.io.BufferedInputStream
import java.io.IOException

/**
 * *Binary standard input*. This class provides methods for reading
 * in bits from standard input, either one bit at a time (as a `boolean`),
 * 8 bits at a time (as a `byte` or `char`),
 * 16 bits at a time (as a `short`), 32 bits at a time
 * (as an `int` or `float`), or 64 bits at a time (as a
 * `double` or `long`).
 *
 *
 * All primitive types are assumed to be represented using their
 * standard Java representations, in big-endian (most significant
 * byte first) order.
 *
 *
 * The client should not intermix calls to `BinaryStdIn` with calls
 * to `StdIn` or `System.in`;
 * otherwise unexpected behavior will result.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object BinaryStdIn {
    private val EOF = -1      // end of file
    private var `in`: BufferedInputStream? = null  // input stream
    private var buffer: Int = 0              // one character buffer
    private var n: Int = 0                   // number of bits left in buffer
    private var isInitialized: Boolean = false   // has BinaryStdIn been called for first time?

    /**
     * Returns true if standard input is empty.
     * @return true if and only if standard input is empty
     */
    val isEmpty: Boolean
        get() {
            if (!isInitialized) initialize()
            return buffer == EOF
        }

    // fill buffer
    private fun initialize() {
        `in` = BufferedInputStream(System.`in`)
        buffer = 0
        n = 0
        fillBuffer()
        isInitialized = true
    }

    private fun fillBuffer() {
        try {
            buffer = `in`!!.read()
            n = 8
        } catch (e: IOException) {
            println("EOF")
            buffer = EOF
            n = -1
        }
    }

    /**
     * Close this input stream and release any associated system resources.
     */
    fun close() {
        if (!isInitialized) initialize()
        try {
            `in`!!.close()
            isInitialized = false
        } catch (ioe: IOException) {
            throw IllegalStateException("Could not close BinaryStdIn", ioe)
        }
    }

    /**
     * Reads the next bit of data from standard input and return as a boolean.
     *
     * @return the next bit of data from standard input as a `boolean`
     * @throws NoSuchElementException if standard input is empty
     */
    fun readBoolean(): Boolean {
        if (isEmpty) throw NoSuchElementException("Reading from empty input stream")
        n--
        val bit = buffer shr n and 1 == 1
        if (n == 0) fillBuffer()
        return bit
    }

    /**
     * Reads the next 8 bits from standard input and return as an 8-bit char.
     * Note that `char` is a 16-bit type;
     * to read the next 16 bits as a char, use `readChar(16)`.
     *
     * @return the next 8 bits of data from standard input as a `char`
     * @throws NoSuchElementException if there are fewer than 8 bits available on standard input
     */
    fun readChar(): Char {
        if (isEmpty) throw NoSuchElementException("Reading from empty input stream")

        // special case when aligned byte
        if (n == 8) {
            fillBuffer()
            return (buffer and 0xff).toChar()
        }

        // combine last n bits of current buffer with first 8-n bits of new buffer
        var x = buffer
        x = x shl 8 - n
        val oldN = n
        fillBuffer()
        if (isEmpty) throw NoSuchElementException("Reading from empty input stream")
        n = oldN
        x = x or buffer.ushr(n)
        return (x and 0xff).toChar()
        // the above code doesn't quite work for the last character if n = 8
        // because buffer will be -1, so there is a special case for aligned byte
    }

    /**
     * Reads the next r bits from standard input and return as an r-bit character.
     *
     * @param  r number of bits to read.
     * @return the next r bits of data from standard input as a `char`
     * @throws NoSuchElementException if there are fewer than `r` bits available on standard input
     * @throws IllegalArgumentException unless `1 <= r <= 16`
     */
    fun readChar(r: Int): Char {
        if (r < 1 || r > 16) throw IllegalArgumentException("Illegal value of r = $r")

        // optimize r = 8 case
        if (r == 8) return readChar()
        var x = 0.toChar()
        for (i in 0 until r) {
            x = (x.toInt() shl 1).toChar()
            val bit = readBoolean()
            if (bit) x = (x.toInt() or 1).toChar()
        }
        return x
    }

    /**
     * Reads the remaining bytes of data from standard input and return as a string.
     *
     * @return the remaining bytes of data from standard input as a `String`
     * @throws NoSuchElementException if standard input is empty or if the number of bits
     * available on standard input is not a multiple of 8 (byte-aligned)
     */
    fun readString(): String {
        if (isEmpty) throw NoSuchElementException("Reading from empty input stream")

        val sb = StringBuilder()
        while (!isEmpty) {
            val c = readChar()
            sb.append(c)
        }
        return sb.toString()
    }


    /**
     * Reads the next 16 bits from standard input and return as a 16-bit short.
     *
     * @return the next 16 bits of data from standard input as a `short`
     * @throws NoSuchElementException if there are fewer than 16 bits available on standard input
     */
    fun readShort(): Short {
        var x: Short = 0
        for (i in 0..1) {
            val c = readChar()
            x = (x.toInt() shl 8).toShort()
            x = (x.toInt() or c.toInt()).toShort()
        }
        return x
    }

    /**
     * Reads the next 32 bits from standard input and return as a 32-bit int.
     *
     * @return the next 32 bits of data from standard input as a `int`
     * @throws NoSuchElementException if there are fewer than 32 bits available on standard input
     */
    fun readInt(): Int {
        var x = 0
        for (i in 0..3) {
            val c = readChar()
            x = x shl 8
            x = x or c.toInt()
        }
        return x
    }

    /**
     * Reads the next r bits from standard input and return as an r-bit int.
     *
     * @param  r number of bits to read.
     * @return the next r bits of data from standard input as a `int`
     * @throws NoSuchElementException if there are fewer than `r` bits available on standard input
     * @throws IllegalArgumentException unless `1 <= r <= 32`
     */
    fun readInt(r: Int): Int {
        if (r < 1 || r > 32) throw IllegalArgumentException("Illegal value of r = $r")

        // optimize r = 32 case
        if (r == 32) return readInt()
        var x = 0
        for (i in 0 until r) {
            x = x shl 1
            val bit = readBoolean()
            if (bit) x = x or 1
        }
        return x
    }

    /**
     * Reads the next 64 bits from standard input and return as a 64-bit long.
     *
     * @return the next 64 bits of data from standard input as a `long`
     * @throws NoSuchElementException if there are fewer than 64 bits available on standard input
     */
    fun readLong(): Long {
        var x: Long = 0
        for (i in 0..7) {
            val c = readChar()
            x = x shl 8
            x = x or c.toLong()
        }
        return x
    }


    /**
     * Reads the next 64 bits from standard input and return as a 64-bit double.
     *
     * @return the next 64 bits of data from standard input as a `double`
     * @throws NoSuchElementException if there are fewer than 64 bits available on standard input
     */
    fun readDouble() = readLong().toDouble()

    /**
     * Reads the next 32 bits from standard input and return as a 32-bit float.
     *
     * @return the next 32 bits of data from standard input as a `float`
     * @throws NoSuchElementException if there are fewer than 32 bits available on standard input
     */
    fun readFloat() = readInt().toFloat()

    /**
     * Reads the next 8 bits from standard input and return as an 8-bit byte.
     *
     * @return the next 8 bits of data from standard input as a `byte`
     * @throws NoSuchElementException if there are fewer than 8 bits available on standard input
     */
    fun readByte(): Byte = (readChar().toInt() and 0xff).toByte()

    /**
     * Test client. Reads in a binary input file from standard input and writes
     * it to standard output.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {

        // read one 8-bit char at a time
        while (!BinaryStdIn.isEmpty) {
            val c = BinaryStdIn.readChar()
            BinaryStdOut.write(c)
        }
        BinaryStdOut.flush()
    }
}// don't instantiate

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