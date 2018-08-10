package jingyiCheng.mscProject.algs

import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.util.Locale

/**
 * This class provides methods for writing strings and numbers to
 * various output streams, including standard output, file, and sockets.
 *
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
class Out {
    private lateinit var out: PrintWriter

    /**
     * Initializes an output stream from a [OutputStream].
     *
     * @param  os the `OutputStream`
     */
    @JvmOverloads constructor(os: OutputStream = System.out) {
        try {
            val osw = OutputStreamWriter(os, CHARSET_NAME)
            out = PrintWriter(osw, true)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * Initializes an output stream from a socket.
     *
     * @param  socket the socket
     */
    constructor(socket: Socket) {
        try {
            val os = socket.getOutputStream()
            val osw = OutputStreamWriter(os, CHARSET_NAME)
            out = PrintWriter(osw, true)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * Initializes an output stream from a file.
     *
     * @param  filename the name of the file
     */
    constructor(filename: String) {
        try {
            val os = FileOutputStream(filename)
            val osw = OutputStreamWriter(os, CHARSET_NAME)
            out = PrintWriter(osw, true)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Closes the output stream.
     */
    fun close() = out.close()

    /**
     * Terminates the current line by printing the line-separator string.
     */
    fun println() = out.println()

    /**
     * Prints an object to this output stream and then terminates the line.
     *
     * @param x the object to print
     */
    fun <T> println(x: T) = out.println(x)

    /**
     * Prints a boolean to this output stream and then terminates the line.
     *
     * @param x the boolean to print
     */
    fun println(x: Boolean) = out.println(x)

    /**
     * Prints a character to this output stream and then terminates the line.
     *
     * @param x the character to print
     */
    fun println(x: Char) = out.println(x)

    /**
     * Prints a double to this output stream and then terminates the line.
     *
     * @param x the double to print
     */
    fun println(x: Double) = out.println(x)

    /**
     * Prints a float to this output stream and then terminates the line.
     *
     * @param x the float to print
     */
    fun println(x: Float) = out.println(x)

    /**
     * Prints an integer to this output stream and then terminates the line.
     *
     * @param x the integer to print
     */
    fun println(x: Int) = out.println(x)

    /**
     * Prints a long to this output stream and then terminates the line.
     *
     * @param x the long to print
     */
    fun println(x: Long) = out.println(x)

    /**
     * Prints a byte to this output stream and then terminates the line.
     * To write binary data, see [BinaryOut].
     *
     * @param x the byte to print
     */
    fun println(x: Byte) = out.println(x.toInt())


    /**
     * Flushes this output stream.
     */
    fun print() = out.flush()

    /**
     * Prints an object to this output stream and flushes this output stream.
     *
     * @param x the object to print
     */
    fun <T> print(x: T) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a boolean to this output stream and flushes this output stream.
     *
     * @param x the boolean to print
     */
    fun print(x: Boolean) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a character to this output stream and flushes this output stream.
     *
     * @param x the character to print
     */
    fun print(x: Char) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a double to this output stream and flushes this output stream.
     *
     * @param x the double to print
     */
    fun print(x: Double) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a float to this output stream and flushes this output stream.
     *
     * @param x the float to print
     */
    fun print(x: Float) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints an integer to this output stream and flushes this output stream.
     *
     * @param x the integer to print
     */
    fun print(x: Int) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a long integer to this output stream and flushes this output stream.
     *
     * @param x the long integer to print
     */
    fun print(x: Long) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a byte to this output stream and flushes this output stream.
     *
     * @param x the byte to print
     */
    fun print(x: Byte) {
        out.print(x.toInt())
        out.flush()
    }

    /**
     * Prints a formatted string to this output stream, using the specified format
     * string and arguments, and then flushes this output stream.
     *
     * @param format the format string
     * @param args   the arguments accompanying the format string
     */
    fun <T> printf(format: String, vararg args: T) {
        out.printf(LOCALE, format, *args)
        out.flush()
    }

    /**
     * Prints a formatted string to this output stream, using the specified
     * locale, format string, and arguments, and then flushes this output stream.
     *
     * @param locale the locale
     * @param format the format string
     * @param args   the arguments accompanying the format string
     */
    fun <T> printf(locale: Locale, format: String, vararg args: T) {
        out.printf(locale, format, *args)
        out.flush()
    }

    companion object {
        // force Unicode UTF-8 encoding; otherwise it's system dependent
        private const val CHARSET_NAME = "UTF-8"

        // assume language = English, country = US for consistency with In
        private val LOCALE = Locale.US

        /**
         * A test client.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // write to stdout
            var out = Out()
            out.println("Test 1")
            out.close()

            // write to a file
            out = Out("test.txt")
            out.println("Test 2")
            out.close()
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
