/******************************************************************************
 * Compilation:  javac BinarySearch.java
 * Execution:    java BinarySearch whitelist.txt < input.txt
 * Dependencies: In.kt StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/11model/tinyW.txt
 * https://algs4.cs.princeton.edu/11model/tinyT.txt
 * https://algs4.cs.princeton.edu/11model/largeW.txt
 * https://algs4.cs.princeton.edu/11model/largeT.txt
 *
 * % java BinarySearch tinyW.txt < tinyT.txt
 * 50
 * 99
 * 13
 *
 * % java BinarySearch largeW.txt < largeT.txt | more
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
 * The `BinarySearch` class provides a static method for binary
 * searching for an integer in a sorted array of integers.
 *
 *
 * The *indexOf* operations takes logarithmic time in the worst case.
 *
 *
 * For additional documentation, see [Section 1.1](https://algs4.cs.princeton.edu/11model) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object BinarySearch {
    /**
     * Returns the index of the specified key in the specified array.
     *
     * @param  a the array of integers, must be sorted in ascending order
     * @param  key the search key
     * @return index of key in array `a` if present; `-1` otherwise
     */
    fun indexOf(a: IntArray, key: Int): Int {
        var lo = 0
        var hi = a.size - 1
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            val mid = (hi + lo) / 2
            when {
                key < a[mid] -> hi = mid - 1
                key > a[mid] -> lo = mid + 1
                else -> return mid
            }
        }
        return -1
    }

    /**
     * Returns the index of the specified key in the specified array.
     * This function is poorly named because it does not give the *rank*
     * if the array has duplicate keys or if the key is not in the array.
     *
     * @param  key the search key
     * @param  a the array of integers, must be sorted in ascending order
     * @return index of key in array `a` if present; `-1` otherwise
     */
    @Deprecated("Replaced by {@link #indexOf(int[], int)}.")
    fun rank(key: Int, a: IntArray) = indexOf(a, key)

    /**
     * Reads in a sequence of integers from the whitelist file, specified as
     * a command-line argument; reads in integers from standard input;
     * prints to standard output those integers that do *not* appear in the file.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {

        // read the integers from a file
        val `in` = In(args[0])
        val whitelist = `in`.readAllInts()

        // sort the array
        whitelist.sort()

        // read integer key from standard input; print if not in whitelist
        while (!StdIn.isEmpty) {
            val key = StdIn.readInt()
            if (BinarySearch.indexOf(whitelist, key) == -1)
                StdOut.println(key)
        }
    }
} //This class should not be instantiated.

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
