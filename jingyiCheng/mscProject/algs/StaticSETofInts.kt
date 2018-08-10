/******************************************************************************
 * Compilation:  javac StaticSetOfInts.java
 * Execution:    none
 * Dependencies: StdOut.kt
 *
 * Data type to store a set of integers.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `StaticSETofInts` class represents a set of integers.
 * It supports searching for a given integer is in the set. It accomplishes
 * this by keeping the set of integers in a sorted array and using
 * binary search to find the given integer.
 *
 *
 * The *rank* and *contains* operations take
 * logarithmic time in the worst case.
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
class StaticSETofInts
/**
 * Initializes a set of integers specified by the integer array.
 * @param keys the array of integers
 * @throws IllegalArgumentException if the array contains duplicate integers
 */
(keys: IntArray) {
    private val a: IntArray = IntArray(keys.size) { keys[it] }

    init {
        // sort the integers
        a.sort()

        // check for duplicates
        for (i in 1 until a.size)
            if (a[i] == a[i - 1])
                throw IllegalArgumentException("Argument arrays contains duplicate keys.")
    }

    /**
     * Is the key in this set of integers?
     * @param key the search key
     * @return true if the set of integers contains the key; false otherwise
     */
    operator fun contains(key: Int) = rank(key) != -1

    /**
     * Returns either the index of the search key in the sorted array
     * (if the key is in the set) or -1 (if the key is not in the set).
     * @param key the search key
     * @return the number of keys in this set less than the key (if the key is in the set)
     * or -1 (if the key is not in the set).
     */
    fun rank(key: Int): Int {
        var lo = 0
        var hi = a.size - 1
        while (lo <= hi) {
            // Key is in a[lo..hi] or not present.
            val mid = lo + (hi - lo) / 2
            when {
                key < a[mid] -> hi = mid - 1
                key > a[mid] -> lo = mid + 1
                else -> return mid
            }
        }
        return -1
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
