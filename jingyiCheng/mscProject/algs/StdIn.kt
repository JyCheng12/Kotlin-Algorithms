package jingyiCheng.mscProject.algs

import java.io.BufferedInputStream
import java.util.InputMismatchException
import java.util.Locale
import java.util.Scanner
import java.util.regex.Pattern

/**
 * The `StdIn` class provides static methods for reading strings
 * and numbers from standard input.
 * These functions fall into one of four categories:
 *
 *  * those for reading individual tokens from standard input, one at a time,
 * and converting each to a number, string, or boolean
 *  * those for reading characters from standard input, one at a time
 *  * those for reading lines from standard input, one at a time
 *  * those for reading a sequence of values of the same type from standard input,
 * and returning the values in an array
 *
 *
 *
 * Generally, it is best not to mix functions from the different
 * categories in the same program.
 *
 *
 * **Reading tokens from standard input and converting to numbers and strings.**
 * You can use the following methods to read numbers, strings, and booleans
 * from standard input one at a time:
 *
 *  *  [.isEmpty]
 *  *  [.readInt]
 *  *  [.readDouble]
 *  *  [.readString]
 *  *  [.readShort]
 *  *  [.readLong]
 *  *  [.readFloat]
 *  *  [.readByte]
 *  *  [.readBoolean]
 *
 *
 *
 * The first method returns true if standard input has more tokens.
 * Each other method skips over any input that is whitespace. Then, it reads
 * the next token and attempts to convert it into a value of the specified
 * type. If it succeeds, it returns that value; otherwise, it
 * throws an [InputMismatchException].
 *
 *
 * *Whitespace* includes spaces, tabs, and newlines; the full definition
 * is inherited from [Character.isWhitespace].
 * A *token* is a maximal sequence of non-whitespace characters.
 * The precise rules for describing which tokens can be converted to
 * integers and floating-point numbers are inherited from
 * [Scanner](http://docs.oracle.com/javase/7/docs/api/java/util/Scanner.html#number-syntax),
 * using the locale [Locale.US]; the rules
 * for floating-point numbers are slightly different
 * from those in [Double.valueOf],
 * but unlikely to be of concern to most programmers.
 *
 *
 * As an example, the following code fragment reads integers from standard input,
 * one at a time, and prints them one per line.
 * <pre>
 * while (!StdIn.isEmpty()) {
 * double value = StdIn.readDouble();
 * StdOut.println(value);
 * }
 * StdOut.println(sum);
</pre> *
 *
 *
 * **Reading characters from standard input.**
 * You can use the following two methods to read characters from standard input one at a time:
 *
 *  *  [.hasNextChar]
 *  *  [.readChar]
 *
 *
 *
 * The first method returns true if standard input has more input (including whitespace).
 * The second method reads and returns the next character of input on standard
 * input (possibly a whitespace character).
 *
 *
 * As an example, the following code fragment reads characters from standard input,
 * one character at a time, and prints it to standard output.
 * <pre>
 * while (StdIn.hasNextChar()) {
 * char c = StdIn.readChar();
 * StdOut.print(c);
 * }
</pre> *
 *
 *
 * **Reading lines from standard input.**
 * You can use the following two methods to read lines from standard input:
 *
 *  *  [.hasNextLine]
 *  *  [.readLine]
 *
 *
 *
 * The first method returns true if standard input has more input (including whitespace).
 * The second method reads and returns the remaining portion of
 * the next line of input on standard input (possibly whitespace),
 * discarding the trailing line separator.
 *
 *
 * A *line separator* is defined to be one of the following strings:
 * `\n` (Linux), `\r` (old Macintosh),
 * `\r\n` (Windows),
 * `\``u2028`, `\``u2029`, or `\``u0085`.
 *
 *
 * As an example, the following code fragment reads text from standard input,
 * one line at a time, and prints it to standard output.
 * <pre>
 * while (StdIn.hasNextLine()) {
 * String line = StdIn.readLine();
 * StdOut.println(line);
 * }
</pre> *
 *
 *
 * **Reading a sequence of values of the same type from standard input.**
 * You can use the following methods to read a sequence numbers, strings,
 * or booleans (all of the same type) from standard input:
 *
 *  *  [.readAllDoubles]
 *  *  [.readAllInts]
 *  *  [.readAllLongs]
 *  *  [.readAllStrings]
 *  *  [.readAllLines]
 *  *  [.readAll]
 *
 *
 *
 * The first three methods read of all of remaining token on standard input
 * and converts the tokens to values of
 * the specified type, as in the corresponding
 * `readDouble`, `readInt`, and `readString()` methods.
 * The `readAllLines()` method reads all remaining lines on standard
 * input and returns them as an array of strings.
 * The `readAll()` method reads all remaining input on standard
 * input and returns it as a string.
 *
 *
 * As an example, the following code fragment reads all of the remaining
 * tokens from standard input and returns them as an array of strings.
 * <pre>
 * String[] words = StdIn.readAllStrings();
</pre> *
 *
 *
 * **Differences with Scanner.**
 * `StdIn` and [Scanner] are both designed to parse
 * tokens and convert them to primitive types and strings.
 * The main differences are summarized below:
 *
 *  *  `StdIn` is a set of static methods and reads
 * reads input from only standard input. It is suitable for use before
 * a programmer knows about objects.
 * See [In] for an object-oriented version that handles
 * input from files, URLs,
 * and sockets.
 *  *  `StdIn` uses whitespace as the delimiter pattern
 * that separates tokens.
 * [Scanner] supports arbitrary delimiter patterns.
 *  *  `StdIn` coerces the character-set encoding to UTF-8,
 * which is the most widely used character encoding for Unicode.
 *  *  `StdIn` coerces the locale to [Locale.US],
 * for consistency with [StdOut], [Double.parseDouble],
 * and floating-point literals.
 *  *  `StdIn` has convenient methods for reading a single
 * character; reading in sequences of integers, doubles, or strings;
 * and reading in all of the remaining input.
 *
 *
 *
 * Historical note: `StdIn` preceded `Scanner`; when
 * `Scanner` was introduced, this class was re-implemented to use `Scanner`.
 *
 *
 * **Using standard input.**
 * Standard input is fundamental operating system abstraction, on Mac OS X,
 * Windows, and Linux.
 * The methods in `StdIn` are *blocking*, which means that they
 * will wait until you enter input on standard input.
 * If your program has a loop that repeats until standard input is empty,
 * you must signal that the input is finished.
 * To do so, depending on your operating system and IDE,
 * use either `<Ctrl-d>` or `<Ctrl-z>`, on its own line.
 * If you are redirecting standard input from a file, you will not need
 * to do anything to signal that the input is finished.
 *
 *
 * **Known bugs.**
 * Java's UTF-8 encoding does not recognize the optional
 * [byte-order mask](http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4508058).
 * If the input begins with the optional byte-order mask, `StdIn`
 * will have an extra character `\``uFEFF` at the beginning.
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
 * @author David Pritchard
 * @author Jingyi Cheng
 *
 */
object StdIn {
    // assume Unicode UTF-8 encoding
    private const val CHARSET_NAME = "UTF-8"

    // assume language = English, country = US for consistency with System.out.
    private val LOCALE = Locale.US

    // the default token separator; we maintain the invariant that this value
    // is held by the scanner's delimiter between calls
    private val WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+")

    // makes whitespace significant
    private val EMPTY_PATTERN = Pattern.compile("")

    // used to read the entire input
    private val EVERYTHING_PATTERN = Pattern.compile("\\A")

    /*** end: section (1 of 2) of code duplicated from In to StdIn.  */

    private lateinit var scanner: Scanner

    /**
     * Returns true if standard input is empty (except possibly for whitespace).
     * Use this method to know whether the next call to [.readString],
     * [.readDouble], etc will succeed.
     *
     * @return `true` if standard input is empty (except possibly
     * for whitespace); `false` otherwise
     */
    val isEmpty: Boolean
        get() = !scanner.hasNext()

    /**
     * Returns true if standard input has a next line.
     * Use this method to know whether the
     * next call to [.readLine] will succeed.
     * This method is functionally equivalent to [.hasNextChar].
     *
     * @return `true` if standard input has more input (including whitespace);
     * `false` otherwise
     */
    fun hasNextLine() = scanner.hasNextLine()

    /**
     * Returns true if standard input has more input (including whitespace).
     * Use this method to know whether the next call to [.readChar] will succeed.
     * This method is functionally equivalent to [.hasNextLine].
     *
     * @return `true` if standard input has more input (including whitespace);
     * `false` otherwise
     */
    fun hasNextChar(): Boolean {
        scanner.useDelimiter(EMPTY_PATTERN)
        val result = scanner.hasNext()
        scanner.useDelimiter(WHITESPACE_PATTERN)
        return result
    }

    /**
     * Reads and returns the next line, excluding the line separator if present.
     *
     * @return the next line, excluding the line separator if present;
     * `null` if no such line
     */
    fun readLine() = try {
        scanner.nextLine()
    } catch (e: NoSuchElementException) {
        null
    }

    /**
     * Reads and returns the next character.
     *
     * @return the next `char`
     * @throws NoSuchElementException if standard input is empty
     */
    fun readChar() = try {
        scanner.useDelimiter(EMPTY_PATTERN)
        val ch = scanner.next()
        assert(ch.length == 1) { "Internal (Std)In.readChar() error!" + " Please contact the authors." }
        scanner.useDelimiter(WHITESPACE_PATTERN)
        ch[0]
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'char' value from standard input, but there are no more tokens available")
    }


    /**
     * Reads and returns the remainder of the input, as a string.
     *
     * @return the remainder of the input, as a string
     * @throws NoSuchElementException if standard input is empty
     */
    fun readAll(): String {
        if (!scanner.hasNextLine()) return ""

        val result = scanner.useDelimiter(EVERYTHING_PATTERN).next()
        // not that important to reset delimeter, since now scanner is empty
        scanner.useDelimiter(WHITESPACE_PATTERN) // but let's do it anyway
        return result
    }


    /**
     * Reads the next token  and returns the `String`.
     *
     * @return the next `String`
     * @throws NoSuchElementException if standard input is empty
     */
    fun readString(): String? = try {
        scanner.next()
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'String' value from standard input, but there are no more tokens available")
    }

    /**
     * Reads the next token from standard input, parses it as an integer, and returns the integer.
     *
     * @return the next integer on standard input
     * @throws NoSuchElementException if standard input is empty
     * @throws InputMismatchException if the next token cannot be parsed as an `int`
     */
    fun readInt() = try {
        scanner.nextInt()
    } catch (e: InputMismatchException) {
        val token = scanner.next()
        throw InputMismatchException("attempts to read an 'int' value from standard input, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read an 'int' value from standard input, but there are no more tokens available")
    }

    /**
     * Reads the next token from standard input, parses it as a double, and returns the double.
     *
     * @return the next double on standard input
     * @throws NoSuchElementException if standard input is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `double`
     */
    fun readDouble() = try {
        scanner.nextDouble()
    } catch (e: InputMismatchException) {
        val token = scanner.next()
        throw InputMismatchException("attempts to read a 'double' value from standard input, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'double' value from standard input, but there are no more tokens available")
    }

    /**
     * Reads the next token from standard input, parses it as a float, and returns the float.
     *
     * @return the next float on standard input
     * @throws NoSuchElementException if standard input is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `float`
     */
    fun readFloat() = try {
        scanner.nextFloat()
    } catch (e: InputMismatchException) {
        val token = scanner.next()
        throw InputMismatchException("attempts to read a 'float' value from standard input, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'float' value from standard input, but there are no more tokens available")
    }

    /**
     * Reads the next token from standard input, parses it as a long integer, and returns the long integer.
     *
     * @return the next long integer on standard input
     * @throws NoSuchElementException if standard input is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `long`
     */
    fun readLong() = try {
        scanner.nextLong()
    } catch (e: InputMismatchException) {
        val token = scanner.next()
        throw InputMismatchException("attempts to read a 'long' value from standard input, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'long' value from standard input, but there are no more tokens available")
    }

    /**
     * Reads the next token from standard input, parses it as a short integer, and returns the short integer.
     *
     * @return the next short integer on standard input
     * @throws NoSuchElementException if standard input is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `short`
     */
    fun readShort() = try {
        scanner.nextShort()
    } catch (e: InputMismatchException) {
        val token = scanner.next()
        throw InputMismatchException("attempts to read a 'short' value from standard input, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'short' value from standard input, but there are no more tokens available")
    }

    /**
     * Reads the next token from standard input, parses it as a byte, and returns the byte.
     *
     * @return the next byte on standard input
     * @throws NoSuchElementException if standard input is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `byte`
     */
    fun readByte() = try {
        scanner.nextByte()
    } catch (e: InputMismatchException) {
        val token = scanner.next()
        throw InputMismatchException("attempts to read a 'byte' value from standard input, but the next token is \"$token\"")
    } catch (e: NoSuchElementException) {
        throw NoSuchElementException("attempts to read a 'byte' value from standard input, but there are no more tokens available")
    }

    /**
     * Reads the next token from standard input, parses it as a boolean,
     * and returns the boolean.
     *
     * @return the next boolean on standard input
     * @throws NoSuchElementException if standard input is empty
     * @throws InputMismatchException if the next token cannot be parsed as a `boolean`:
     * `true` or `1` for true, and `false` or `0` for false,
     * ignoring case
     */
    fun readBoolean(): Boolean {
        try {
            val token = readString()
            if ("true".equals(token, ignoreCase = true)) return true
            if ("false".equals(token, ignoreCase = true)) return false
            if ("1" == token) return true
            if ("0" == token) return false
            throw InputMismatchException("attempts to read a 'boolean' value from standard input, but the next token is \"$token\"")
        } catch (e: NoSuchElementException) {
            throw NoSuchElementException("attempts to read a 'boolean' value from standard input, but there are no more tokens available")
        }
    }

    /**
     * Reads all remaining tokens from standard input and returns them as an array of strings.
     *
     * @return all remaining tokens on standard input, as an array of strings
     */
    fun readAllStrings(): Array<String> {
        // we could use readAll.trim().split(), but that's not consistent
        // because trim() uses characters 0x00..0x20 as whitespace
        val tokens = WHITESPACE_PATTERN.split(readAll())
        if (tokens.isNotEmpty())
            return tokens

        // don't include first token if it is leading whitespace
        return Array(tokens.size - 1) { tokens[it + 1] }
    }

    /**
     * Reads all remaining lines from standard input and returns them as an array of strings.
     * @return all remaining lines on standard input, as an array of strings
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
     * Reads all remaining tokens from standard input, parses them as integers, and returns
     * them as an array of integers.
     * @return all remaining integers on standard input, as an array
     * @throws InputMismatchException if any token cannot be parsed as an `int`
     */
    fun readAllInts(): IntArray {
        val fields = readAllStrings()
        return IntArray(fields.size) { fields[it].toInt() }
    }

    /**
     * Reads all remaining tokens from standard input, parses them as longs, and returns
     * them as an array of longs.
     * @return all remaining longs on standard input, as an array
     * @throws InputMismatchException if any token cannot be parsed as a `long`
     */
    fun readAllLongs(): LongArray {
        val fields = readAllStrings()
        return LongArray(fields.size) { fields[it].toLong() }
    }

    /**
     * Reads all remaining tokens from standard input, parses them as doubles, and returns
     * them as an array of doubles.
     * @return all remaining doubles on standard input, as an array
     * @throws InputMismatchException if any token cannot be parsed as a `double`
     */
    fun readAllDoubles(): DoubleArray {
        val fields = readAllStrings()
        return DoubleArray(fields.size) { fields[it].toDouble() }
    }

    //// end: section (2 of 2) of code duplicated from In to StdIn


    // do this once when StdIn is initialized
    init {
        resync()
    }

    /**
     * If StdIn changes, use this to reinitialize the scanner.
     */
    private fun resync() = setScanner(Scanner(BufferedInputStream(System.`in`), CHARSET_NAME))

    private fun setScanner(scanner: Scanner) {
        StdIn.scanner = scanner
        StdIn.scanner.useLocale(LOCALE)
    }

    /**
     * Reads all remaining tokens, parses them as integers, and returns
     * them as an array of integers.
     * @return all remaining integers, as an array
     * @throws InputMismatchException if any token cannot be parsed as an `int`
     */
    @Deprecated("Replaced by {@link #readAllInts()}.")
    fun readInts() = readAllInts()

    /**
     * Reads all remaining tokens, parses them as doubles, and returns
     * them as an array of doubles.
     * @return all remaining doubles, as an array
     * @throws InputMismatchException if any token cannot be parsed as a `double`
     */
    @Deprecated("Replaced by {@link #readAllDoubles()}.")
    fun readDoubles() = readAllDoubles()

    /**
     * Reads all remaining tokens and returns them as an array of strings.
     * @return all remaining tokens, as an array of strings
     */
    @Deprecated("Replaced by {@link #readAllStrings()}.")
    fun readStrings() = readAllStrings()

    /**
     * Interactive test of basic functionality.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        StdOut.print("Type a string: ")
        val s = readString()
        StdOut.println("Your string was: $s")
        StdOut.println()

        StdOut.print("Type an int: ")
        val a = readInt()
        StdOut.println("Your int was: $a")
        StdOut.println()

        StdOut.print("Type a boolean: ")
        val b = readBoolean()
        StdOut.println("Your boolean was: $b")
        StdOut.println()

        StdOut.print("Type a double: ")
        val c = readDouble()
        StdOut.println("Your double was: $c")
        StdOut.println()
    }

}// it doesn't make sense to instantiate this class

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