/******************************************************************************
 * Compilation:  javac Whitelist.java
 * Execution:    java Whitelist whitelist.txt < data.txt
 * Dependencies: StaticSetOfInts.kt In.kt StdOut.kt
 *
 * Data files:   https://algs4.cs.princeton.edu/11model/tinyW.txt
 * https://algs4.cs.princeton.edu/11model/tinyT.txt
 * https://algs4.cs.princeton.edu/11model/largeW.txt
 * https://algs4.cs.princeton.edu/11model/largeT.txt
 *
 * Whitelist filter.
 *
 *
 * % java Whitelist tinyW.txt < tinyT.txt
 * 50
 * 99
 * 13
 *
 * % java Whitelist largeW.txt < largeT.txt | more
 * 499569
 * 984875
 * 295754
 * 207807
 * 140925
 * 161828
 * [367,966 total values]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Whitelist` class provides a client for reading in
 * a set of integers from a file; reading in a sequence of integers
 * from standard input; and printing to standard output those
 * integers not in the whitelist.
 *
 *
 * For additional documentation, see [Section 1.2](https://algs4.cs.princeton.edu/12oop) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object Whitelist {
    /**
     * Reads in a sequence of integers from the whitelist file, specified as
     * a command-line argument. Reads in integers from standard input and
     * prints to standard output those integers that are not in the file.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val `in` = In(args[0])
        val white = `in`.readAllInts()
        val set = StaticSETofInts(white)

        // Read key, print if not in whitelist.
        while (!StdIn.isEmpty) {
            val key = StdIn.readInt()
            if (!set.contains(key))
                StdOut.println(key)
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
