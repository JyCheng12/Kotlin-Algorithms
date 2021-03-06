/******************************************************************************
 * Compilation:  javac WhiteFilter.java
 * Execution:    java WhiteFilter whitelist.txt < input.txt
 * Dependencies: SET In.kt StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/35applications/tinyTale.txt
 * https://algs4.cs.princeton.edu/35applications/list.txt
 *
 * Read in a whitelist of words from a file. Then read in a list of
 * words from standard input and print out all those words that
 * are in the first file.
 *
 * % more tinyTale.txt
 * it was the best of times it was the worst of times
 * it was the age of wisdom it was the age of foolishness
 * it was the epoch of belief it was the epoch of incredulity
 * it was the season of light it was the season of darkness
 * it was the spring of hope it was the winter of despair
 *
 * % more list.txt
 * was it the of
 *
 * % java WhiteFilter list.txt < tinyTale.txt
 * it was the of it was the of
 * it was the of it was the of
 * it was the of it was the of
 * it was the of it was the of
 * it was the of it was the of
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `WhiteFilter` class provides a client for reading in a *whitelist*
 * of words from a file; then, reading in a sequence of words from standard input,
 * printing out each word that appears in the file.
 * It is useful as a test client for various symbol table implementations.
 *
 *
 * For additional documentation, see [Section 3.5](https://algs4.cs.princeton.edu/35applications) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object WhiteFilter {
    @JvmStatic
    fun main(args: Array<String>) {
        val set = SET<String>()

        // read in strings and add to set
        val `in` = In(args[0])
        while (!`in`.isEmpty) {
            val word = `in`.readString()
            set.add(word)
        }

        // read in string from standard input, printing out all exceptions
        while (!StdIn.isEmpty) {
            val word = StdIn.readString()
            if (set.contains(word))
                StdOut.println(word)
        }
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