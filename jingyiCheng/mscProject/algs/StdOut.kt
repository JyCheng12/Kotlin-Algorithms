package jingyiCheng.mscProject.algs

import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.io.UnsupportedEncodingException
import java.util.Locale

/**
 * This class provides methods for printing strings and numbers to standard output.
 *
 *
 * Here is an example program that uses `StdOut`:
 * <pre>
 * public class TestStdOut {
 * public static void main(String[] args) {
 * int a = 17;
 * int b = 23;
 * int sum = a + b;
 * StdOut.println("Hello, World");
 * StdOut.printf("%d + %d = %d\n", a, b, sum);
 * }
 * }
</pre> *
 *
 *
 * **Differences with System.out.**
 * The behavior of `StdOut` is similar to that of [System.out],
 * but there are a few subtle differences:
 *
 *  *  `StdOut` coerces the character-set encoding to UTF-8,
 * which is a standard character encoding for Unicode.
 *  *  `StdOut` coerces the locale to [Locale.US],
 * for consistency with [StdIn], [Double.parseDouble],
 * and floating-point literals.
 *  *  `StdOut` *flushes* standard output after each call to
 * `print()` so that text will appear immediately in the terminal.
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
object StdOut {
    // force Unicode UTF-8 encoding; otherwise it's system dependent
    private const val CHARSET_NAME = "UTF-8"

    // assume language = English, country = US for consistency with StdIn
    private val LOCALE = Locale.US

    // send output here
    private lateinit var out: PrintWriter

    // this is called before invoking any methods
    init {
        try {
            out = PrintWriter(OutputStreamWriter(System.out, CHARSET_NAME), true)
        } catch (e: UnsupportedEncodingException) {
            println(e)
        }
    }

    /**
     * Closes standard output.
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
     * Prints a boolean to standard output and then terminates the line.
     *
     * @param x the boolean to print
     */
    fun println(x: Boolean) = out.println(x)

    /**
     * Prints a character to standard output and then terminates the line.
     *
     * @param x the character to print
     */
    fun println(x: Char) = out.println(x)

    /**
     * Prints a double to standard output and then terminates the line.
     *
     * @param x the double to print
     */
    fun println(x: Double) = out.println(x)

    /**
     * Prints an integer to standard output and then terminates the line.
     *
     * @param x the integer to print
     */
    fun println(x: Float) = out.println(x)

    /**
     * Prints an integer to standard output and then terminates the line.
     *
     * @param x the integer to print
     */
    fun println(x: Int) = out.println(x)

    /**
     * Prints a long to standard output and then terminates the line.
     *
     * @param x the long to print
     */
    fun println(x: Long) = out.println(x)

    /**
     * Prints a short integer to standard output and then terminates the line.
     *
     * @param x the short to print
     */
    fun println(x: Short) = out.println(x.toInt())

    /**
     * Prints a byte to standard output and then terminates the line.
     *
     *
     * To write binary data, see [BinaryStdOut].
     *
     * @param x the byte to print
     */
    fun println(x: Byte) = out.println(x.toInt())

    /**
     * Flushes standard output.
     */
    fun print() = out.flush()

    /**
     * Prints an object to standard output and flushes standard output.
     *
     * @param x the object to print
     */
    fun <T> print(x: T) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a boolean to standard output and flushes standard output.
     *
     * @param x the boolean to print
     */
    fun print(x: Boolean) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a character to standard output and flushes standard output.
     *
     * @param x the character to print
     */
    fun print(x: Char) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a double to standard output and flushes standard output.
     *
     * @param x the double to print
     */
    fun print(x: Double) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a float to standard output and flushes standard output.
     *
     * @param x the float to print
     */
    fun print(x: Float) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints an integer to standard output and flushes standard output.
     *
     * @param x the integer to print
     */
    fun print(x: Int) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a long integer to standard output and flushes standard output.
     *
     * @param x the long integer to print
     */
    fun print(x: Long) {
        out.print(x)
        out.flush()
    }

    /**
     * Prints a short integer to standard output and flushes standard output.
     *
     * @param x the short integer to print
     */
    fun print(x: Short) {
        out.print(x.toInt())
        out.flush()
    }

    /**
     * Prints a byte to standard output and flushes standard output.
     *
     * @param x the byte to print
     */
    fun print(x: Byte) {
        out.print(x.toInt())
        out.flush()
    }

    /**
     * Prints a formatted string to standard output, using the specified format
     * string and arguments, and then flushes standard output.
     *
     *
     * @param format the [format string](http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax)
     * @param args   the arguments accompanying the format string
     */
    fun <T> printf(format: String, vararg args: T) {
        out.printf(LOCALE, format, *args)
        out.flush()
    }

    /**
     * Prints a formatted string to standard output, using the locale and
     * the specified format string and arguments; then flushes standard output.
     *
     * @param locale the locale
     * @param format the [format string](http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax)
     * @param args   the arguments accompanying the format string
     */
    fun <T> printf(locale: Locale, format: String, vararg args: T) {
        out.printf(locale, format, *args)
        out.flush()
    }

    /**
     * Unit tests some of the methods in `StdOut`.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = "qqq"
        // write to stdout
        println(a)
        println("Test")
        println(17)
        println(true)
        printf("%.6f\n", 1.0 / 7.0)
    }

}// don't instantiate

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
