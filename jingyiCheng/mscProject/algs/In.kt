/******************************************************************************
 * Compilation:  javac In.java
 * Execution:    java In   (basic test --- see source for required files)
 * Dependencies: none
 *
 * Reads in data of various types from standard input, files, and URLs.
 *
 */

package jingyiCheng.mscProject.algs

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URL
import java.net.Socket
import java.util.ArrayList
import java.util.InputMismatchException
import java.util.Locale
import java.util.Scanner
import java.util.regex.Pattern

/**
 * *Input*. This class provides methods for reading strings
 * and numbers from standard input, file input, URLs, and sockets.
 *
 *
 * The Locale used is: language = English, country = US. This is consistent
 * with the formatting conventions with Java floating-point literals,
 * command-line arguments (via [Double.parseDouble])
 * and standard output.
 *
 *
 * For additional documentation, see
 * [Section 3.1](https://introcs.cs.princeton.edu/31datatype) of
 * *Computer Science: An Interdisciplinary Approach*
 * by Robert Sedgewick and Kevin Wayne.
 *
 *
 * Like [Scanner], reading a token also consumes preceding Java
 * whitespace, reading a full line consumes
 * the following end-of-line delimeter, while reading a character consumes
 * nothing extra.
 *
 *
 * Whitespace is defined in [Character.isWhitespace]. Newlines
 * consist of \n, \r, \r\n, and Unicode hex code points 0x2028, 0x2029, 0x0085;
 * see [
 * Scanner.java](http://www.docjar.com/html/api/java/util/Scanner.java.html) (NB: Java 6u23 and earlier uses only \r, \r, \r\n).
 *
 * @author David Pritchard
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class In {
    //// end: section (1 of 2) of code duplicated from In to StdIn.

    private var scanner: Scanner? = null

    ////  begin: section (2 of 2) of code duplicated from In to StdIn,
    ////  with all methods changed from "public" to "public static".
    /**
     * Returns true if input stream is empty (except possibly whitespace).
     * Use this to know whether the next call to [.readString],
     * [.readDouble], etc will succeed.
     *
     * @return `true` if this input stream is empty (except possibly whitespace);
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = !scanner!!.hasNext()

    /**
     * Initializes an input stream from standard input.
     */
    constructor() {
        scanner = Scanner(BufferedInputStream(System.`in`), CHARSET_NAME)
        scanner!!.useLocale(LOCALE)
    }

    /**
     * Initializes an input stream from a socket.
     *
     * @param  socket the socket
     * @throws IllegalArgumentException if cannot open `socket`
     * @throws IllegalArgumentException if `socket` is `null`
     */
    constructor(socket: Socket?) {
        if (socket == null) throw IllegalArgumentException("socket argument is null")
        try {
            val `is` = socket.getInputStream()
            scanner = Scanner(BufferedInputStream(`is`), CHARSET_NAME)
            scanner!!.useLocale(LOCALE)
        } catch (ioe: IOException) {
            throw IllegalArgumentException("Could not open $socket", ioe)
        }
    }

    /**
     * Initializes an input stream from a URL.
     *
     * @param  url the URL
     * @throws IllegalArgumentException if cannot open `url`
     * @throws IllegalArgumentException if `url` is `null`
     */
    constructor(url: URL?) {
        if (url == null) throw IllegalArgumentException("url argument is null")
        try {
            val site = url.openConnection()
            val `is` = site.getInputStream()
            scanner = Scanner(BufferedInputStream(`is`), CHARSET_NAME)
            scanner!!.useLocale(LOCALE)
        } catch (ioe: IOException) {
            throw IllegalArgumentException("Could not open $url", ioe)
        }
    }

    /**
     * Initializes an input stream from a file.
     *
     * @param  file the file
     * @throws IllegalArgumentException if cannot open `file`
     * @throws IllegalArgumentException if `file` is `null`
     */
    constructor(file: File?) {
        if (file == null) throw IllegalArgumentException("file argument is null")
        try {
            // for consistency with StdIn, wrap with BufferedInputStream instead of use
            // file as argument to Scanner
            val fis = FileInputStream(file)
            scanner = Scanner(BufferedInputStream(fis), CHARSET_NAME)
            scanner!!.useLocale(LOCALE)
        } catch (ioe: IOException) {
            throw IllegalArgumentException("Could not open $file", ioe)
        }
    }

    /**
     * Initializes an input stream from a filename or web page name.
     *
     * @param  name the filename or web page name
     * @throws IllegalArgumentException if cannot open `name` as
     * a file or URL
     * @throws IllegalArgumentException if `name` is `null`
     */
    constructor(name: String?) {
        if (name == null) throw IllegalArgumentException("argument is null")
        try {
            // first try to read file from local file system
            val file = File(name)
            if (file.exists()) {
                // for consistency with StdIn, wrap with BufferedInputStream instead of use
                // file as argument to Scanner
                val fis = FileInputStream(file)
                scanner = Scanner(BufferedInputStream(fis), CHARSET_NAME)
                scanner!!.useLocale(LOCALE)
                return
            }

            // next try for files included in jar
            var url: URL? = javaClass.getResource(name)
            // try this as well
            url = url ?: javaClass.classLoader.getResource(name)
            // or URL from web
            url = url ?: URL(name)

            val site = url.openConnection()

            // in order to set User-Agent, replace above line with these two
            // HttpURLConnection site = (HttpURLConnection) url.openConnection();
            // site.addRequestProperty("User-Agent", "Mozilla/4.76");

            val `is` = site.getInputStream()
            scanner = Scanner(BufferedInputStream(`is`), CHARSET_NAME)
            scanner!!.useLocale(LOCALE)
        } catch (ioe: IOException) {
            throw IllegalArgumentException("Could not open $name", ioe)
        }
    }

    /**
     * Initializes an input stream from a given [Scanner] source; use with
     * `new Scanner(String)` to read from a string.
     *
     * Note that this does not create a defensive copy, so the
     * scanner will be mutated as you read on.
     *
     * @param  scanner the scanner
     * @throws IllegalArgumentException if `scanner` is `null`
     */
    constructor(scanner: Scanner?) {
        if (scanner == null) throw IllegalArgumentException("scanner argument is null")
        this.scanner = scanner
    }

    /**
     * Returns true if this input stream exists.
     *
     * @return `true` if this input stream exists; `false` otherwise
     */
    fun exists() = scanner != null

    /**
     * Returns true if this input stream has a next line.
     * Use this method to know whether the
     * next call to [.readLine] will succeed.
     * This method is functionally equivalent to [.hasNextChar].
     *
     * @return `true` if this input stream has more input (including whitespace);
     * `false` otherwise
     */
    fun hasNextLine() = scanner!!.hasNextLine()

    /**
     * Returns true if this input stream has more input (including whitespace).
     * Use this method to know whether the next call to [.readChar] will succeed.
     * This method is functionally equivalent to [.hasNextLine].
     *
     * @return `true` if this input stream has more input (including whitespace);
     * `false` otherwise
     */
    fun hasNextChar(): Boolean {
        scanner!!.useDelimiter(EMPTY_PATTERN)
        val result = scanner!!.hasNext()
        scanner!!.useDelimiter(WHITESPACE_PATTERN)
        return result
    }

    /**
     * Reads and returns the next line in this input stream.
     *
     * @return the next line in this input stream; `null` if no such line
     */
    fun readLine(): String? = try {
        scanner!!.nextLine()
    } catch (e: NoSuchElementException) {
        null
    }

    /**
     * Reads and returns the next character in this input stream.
     *
     * @return the next `char` in this input stream
     * @throws NoSuchElementException if the input stream is empty
     */
    fun readChar(): Char {
        scanner!!.useDelimiter(EMPTY_PATTERN)
        try {
            val ch = scanner!!.next()
            assert(ch.length == 1) { "Internal (Std)In.readChar() error!" + " Please contact the authors." }
            scanner!!.useDelimiter(WHITESPACE_PATTERN)
            return ch[0]
        } catch (e: NoSuchElementException) {
            throw NoSuchElementException("attempts to read a 'char' value from input stream, but there are no more tokens available")
        }

    }

    /**
     * Reads and returns the remainder of this input stream, as a string.
     *
     * @return the remainder of this input stream, as a string
     */
    fun readAll(): String {
        if (!scanner!!.hasNextLine()) return ""
        val result = scanner!!.useDelimiter(EVERYTHING_PATTERN).next()
        // not that important to reset delimeter, since now scanner is empty
        scanner!!.useDelimiter(WHITESPACE_PATTERN) // but let's do it anyway
        return result
    }

    /**
     * Reads the next token from this input stream and returns it as a `String`.
     *
     * @return the next `String` in this input stream
     * @throws NoSuchElementException if the input stream is empty
     */
    fun readString() = try {
        scanner!!.next()
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'String' value from input stream, but there are no more tokens available")
    }

    /**
     * Reads the next token from this input stream, parses it as a `int`,
     * and returns the `int`.
     *
     * @return the next `int` in this input stream
     * @throws NoSuchElementException if the input stream is empty
     * @throws InputMismatchException if the next token cannot be parsed as an `int`
     */
    fun readInt() = try {
        scanner!!.nextInt()
    } catch (e: InputMismatchException) {
        val token = scanner!!.next()
        throw InputMismatchException("attempts to read an 'int' value from input stream, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read an 'int' value from input stream, but there are no more tokens available")
    }

    /**
     * Reads the next token from this input stream, parses it as a `double`,
     * and returns the `double`.
     *
     * @return the next `double` in this input stream
     * @throws NoSuchElementException if the input stream is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `double`
     */
    fun readDouble() = try {
        scanner!!.nextDouble()
    } catch (e: InputMismatchException) {
        val token = scanner!!.next()
        throw InputMismatchException("attempts to read a 'double' value from input stream, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'double' value from input stream, but there are no more tokens available")
    }

    /**
     * Reads the next token from this input stream, parses it as a `float`,
     * and returns the `float`.
     *
     * @return the next `float` in this input stream
     * @throws NoSuchElementException if the input stream is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `float`
     */
    fun readFloat() = try {
        scanner!!.nextFloat()
    } catch (e: InputMismatchException) {
        val token = scanner!!.next()
        throw InputMismatchException("attempts to read a 'float' value from input stream, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'float' value from input stream, but there are no more tokens available")
    }

    /**
     * Reads the next token from this input stream, parses it as a `long`,
     * and returns the `long`.
     *
     * @return the next `long` in this input stream
     * @throws NoSuchElementException if the input stream is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `long`
     */
    fun readLong() = try {
        scanner!!.nextLong()
    } catch (e: InputMismatchException) {
        val token = scanner!!.next()
        throw InputMismatchException("attempts to read a 'long' value from input stream, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'long' value from input stream, but there are no more tokens available")
    }

    /**
     * Reads the next token from this input stream, parses it as a `short`,
     * and returns the `short`.
     *
     * @return the next `short` in this input stream
     * @throws NoSuchElementException if the input stream is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `short`
     */
    fun readShort() = try {
        scanner!!.nextShort()
    } catch (e: InputMismatchException) {
        val token = scanner!!.next()
        throw InputMismatchException("attempts to read a 'short' value from input stream, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'short' value from input stream, but there are no more tokens available")
    }

    /**
     * Reads the next token from this input stream, parses it as a `byte`,
     * and returns the `byte`.
     *
     *
     * To read binary data, use [BinaryIn].
     *
     * @return the next `byte` in this input stream
     * @throws NoSuchElementException if the input stream is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `byte`
     */
    fun readByte() = try {
        scanner!!.nextByte()
    } catch (e: InputMismatchException) {
        val token = scanner!!.next()
        throw InputMismatchException("attempts to read a 'byte' value from input stream, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'byte' value from input stream, but there are no more tokens available")
    }

    /**
     * Reads the next token from this input stream, parses it as a `boolean`
     * (interpreting either `"true"` or `"1"` as `true`,
     * and either `"false"` or `"0"` as `false`).
     *
     * @return the next `boolean` in this input stream
     * @throws NoSuchElementException if the input stream is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `boolean`
     */
    fun readBoolean(): Boolean {
        try {
            val token = readString()
            if ("true".equals(token, ignoreCase = true)) return true
            if ("false".equals(token, ignoreCase = true)) return false
            if ("1" == token) return true
            if ("0" == token) return false
            throw InputMismatchException("attempts to read a 'boolean' value from input stream, but the next token is \"$token\"")
        } catch (e: NoSuchElementException) {
            throw NoSuchElementException("attempts to read a 'boolean' value from input stream, but there are no more tokens available")
        }
    }

    /**
     * Reads all remaining tokens from this input stream and returns them as
     * an array of strings.
     *
     * @return all remaining tokens in this input stream, as an array of strings
     */
    fun readAllStrings(): Array<String> {
        // we could use readAll.trim().split(), but that's not consistent
        // since trim() uses characters 0x00..0x20 as whitespace
        val tokens = WHITESPACE_PATTERN.split(readAll())
        if (tokens.isNotEmpty())
            return tokens

        val decapitokens = Array(tokens.size - 1) { tokens[it + 1] }
        return decapitokens
    }

    /**
     * Reads all remaining lines from this input stream and returns them as
     * an array of strings.
     *
     * @return all remaining lines in this input stream, as an array of strings
     */
    fun readAllLines(): Array<String> {
        val lines = ArrayList<String>()
        while (hasNextLine()) {
            val next = readLine()
            if (next != null) lines.add(next)
        }
        return lines.toTypedArray()
    }


    /**
     * Reads all remaining tokens from this input stream, parses them as integers,
     * and returns them as an array of integers.
     *
     * @return all remaining lines in this input stream, as an array of integers
     */
    fun readAllInts(): IntArray {
        val fields = readAllStrings()
        return IntArray(fields.size) { Integer.parseInt(fields[it]) }
    }

    /**
     * Reads all remaining tokens from this input stream, parses them as longs,
     * and returns them as an array of longs.
     *
     * @return all remaining lines in this input stream, as an array of longs
     */
    fun readAllLongs(): LongArray {
        val fields = readAllStrings()
        return LongArray(fields.size) { fields[it].toLong() }
    }

    /**
     * Reads all remaining tokens from this input stream, parses them as doubles,
     * and returns them as an array of doubles.
     *
     * @return all remaining lines in this input stream, as an array of doubles
     */
    fun readAllDoubles(): DoubleArray {
        val fields = readAllStrings()
        return DoubleArray(fields.size) { fields[it].toDouble() }
    }

    /**
     * Closes this input stream.
     */
    fun close() = scanner!!.close()

    companion object {
        // assume Unicode UTF-8 encoding
        private const val CHARSET_NAME = "UTF-8"

        // assume language = English, country = US for consistency with System.out.
        private val LOCALE = Locale.US

        // the default token separator; we maintain the invariant that this value
        // is held by the scanner's delimiter between calls
        private val WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+")

        // makes whitespace characters significant
        private val EMPTY_PATTERN = Pattern.compile("")

        // used to read the entire input. source:
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        private val EVERYTHING_PATTERN = Pattern.compile("\\A")

        /**
         * Reads all integers from a file and returns them as
         * an array of integers.
         *
         * @param      filename the name of the file
         * @return     the integers in the file
         */
        @Deprecated("Replaced by {@code new In(filename)}.{@link #readAllInts()}.")
        fun readInts(filename: String) = In(filename).readAllInts()

        /**
         * Reads all doubles from a file and returns them as
         * an array of doubles.
         *
         * @param      filename the name of the file
         * @return     the doubles in the file
         */
        @Deprecated("Replaced by {@code new In(filename)}.{@link #readAllDoubles()}.")
        fun readDoubles(filename: String) = In(filename).readAllDoubles()

        /**
         * Reads all strings from a file and returns them as
         * an array of strings.
         *
         * @param      filename the name of the file
         * @return     the strings in the file
         */
        @Deprecated("Replaced by {@code new In(filename)}.{@link #readAllStrings()}.")
        fun readStrings(filename: String) = In(filename).readAllStrings()

        /**
         * Reads all integers from standard input and returns them
         * an array of integers.
         *
         * @return     the integers on standard input
         */
        @Deprecated("Replaced by {@link StdIn#readAllInts()}.")
        fun readInts() = In().readAllInts()

        /**
         * Reads all doubles from standard input and returns them as
         * an array of doubles.
         *
         * @return     the doubles on standard input
         */
        @Deprecated("Replaced by {@link StdIn#readAllDoubles()}.")
        fun readDoubles() = In().readAllDoubles()

        /**
         * Reads all strings from standard input and returns them as
         * an array of strings.
         *
         * @return     the strings on standard input
         */
        @Deprecated("Replaced by {@link StdIn#readAllStrings()}.")
        fun readStrings() = In().readAllStrings()

        /**
         * Unit tests the `In` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            var `in`: In
            val urlName = "https://introcs.cs.princeton.edu/stdlib/InTest.txt"

            // read from a URL
            println("readAll() from URL $urlName")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In(urlName)
                println(`in`.readAll())
            } catch (e: IllegalArgumentException) {
                println(e)
            }
            println()

            // read one line at a time from URL
            println("readLine() from URL $urlName")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In(urlName)
                while (!`in`.isEmpty) {
                    val s = `in`.readLine()
                    println(s)
                }
            } catch (e: IllegalArgumentException) {
                println(e)
            }
            println()

            // read one string at a time from URL
            println("readString() from URL $urlName")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In(urlName)
                while (!`in`.isEmpty) {
                    val s = `in`.readString()
                    println(s)
                }
            } catch (e: IllegalArgumentException) {
                println(e)
            }
            println()

            // read one line at a time from file in current directory
            println("readLine() from current directory")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In(".\\InTest.txt")
                while (!`in`.isEmpty) {
                    val s = `in`.readLine()
                    println(s)
                }
            } catch (e: IllegalArgumentException) {
                println(e)
            }
            println()

            // read one line at a time from file using relative path
            println("readLine() from relative path")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In("..\\InTest.txt")
                while (!`in`.isEmpty) {
                    val s = `in`.readLine()
                    println(s)
                }
            } catch (e: IllegalArgumentException) {
                println(e)
            }
            println()

            // read one char at a time
            println("readChar() from file")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In("C:\\Users\\53171\\IdeaProjects\\MscProject\\src\\InTest.txt")
                while (!`in`.isEmpty) {
                    val c = `in`.readChar()
                    print(c)
                }
            } catch (e: IllegalArgumentException) {
                println(e)
            }
            println()
            println()

            // read one line at a time from absolute OS X / Linux path
            println("readLine() from absolute OS X / Linux path")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In("\\n\\fs\\introcs\\www\\java\\stdlib\\InTest.txt")
                while (!`in`.isEmpty) {
                    val s = `in`.readLine()
                    println(s)
                }
            } catch (e: IllegalArgumentException) {
                println(e)
            }
            println()

            // read one line at a time from absolute Windows path
            println("readLine() from absolute Windows path")
            println("---------------------------------------------------------------------------")
            try {
                `in` = In("C:\\Users\\53171\\IdeaProjects\\MscProject\\src\\InTest.txt")
                while (!`in`.isEmpty) {
                    val s = `in`.readLine()
                    println(s)
                }
                println()
            } catch (e: IllegalArgumentException) {
                println(e)
            }
            println()
        }
    }
}

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
