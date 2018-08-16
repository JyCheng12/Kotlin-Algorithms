/******************************************************************************
 * Compilation:  javac Inversions.java
 * Execution:    java Inversions < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 *
 * Read array of n integers and count number of inversions in n log n time.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Inversions` class provides static methods to count the
 * number of *inversions* in either an array of integers or comparables.
 * An inversion in an array `a[]` is a pair of indicies `i` and
 * `j` such that `i < j` and `a[i] > a[j]`.
 *
 *
 * This implementation uses a generalization of mergesort. The *count*
 * operation takes time proportional to *n* log *n*,
 * where *n* is the number of keys in the array.
 *
 *
 * For additional documentation, see [Section 2.2](https://algs4.cs.princeton.edu/22mergesort)
 * of *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object Inversions {
    // merge and count
    private fun merge(a: IntArray, aux: IntArray, lo: Int, mid: Int, hi: Int): Long {
        var inversions: Long = 0

        // copy to aux[]
        for (k in lo..hi) aux[k] = a[k]

        // merge back to a[]
        var i = lo
        var j = mid + 1
        for (k in lo..hi)
            when {
                i > mid -> a[k] = aux[j++]
                j > hi -> a[k] = aux[i++]
                aux[j] < aux[i] -> {
                    a[k] = aux[j++]
                    inversions += (mid - i + 1).toLong()
                }
                else -> a[k] = aux[i++]
            }
        return inversions
    }

    // return the number of inversions in the subarray b[lo..hi]
    // side effect b[lo..hi] is rearranged in ascending order
    private fun count(a: IntArray, b: IntArray, aux: IntArray, lo: Int, hi: Int): Long {
        var inversions: Long = 0
        if (hi <= lo) return 0
        val mid = lo + (hi - lo) / 2
        inversions += count(a, b, aux, lo, mid)
        inversions += count(a, b, aux, mid + 1, hi)
        inversions += merge(b, aux, lo, mid, hi)
        assert(inversions == brute(a, lo, hi))
        return inversions
    }

    /**
     * Returns the number of inversions in the integer array.
     * The argument array is not modified.
     * @param  a the array
     * @return the number of inversions in the array. An inversion is a pair of
     * indicies `i` and `j` such that `i < j`
     * and `a[i] > a[j]`.
     */
    fun count(a: IntArray): Long {
        val b = IntArray(a.size) { a[it] }
        val aux = IntArray(a.size)
        val inversions = count(a, b, aux, 0, a.size - 1)
        return count(a, b, aux, 0, a.size - 1)
    }

    // merge and count (Comparable version)
    private fun <Key : Comparable<Key>> merge(a: Array<Key>, aux: Array<Key>, lo: Int, mid: Int, hi: Int): Long {
        var inversions: Long = 0

        // copy to aux[]
        for (k in lo..hi)
            aux[k] = a[k]

        // merge back to a[]
        var i = lo
        var j = mid + 1
        for (k in lo..hi)
            when {
                i > mid -> a[k] = aux[j++]
                j > hi -> a[k] = aux[i++]
                less(aux[j], aux[i]) -> {
                    a[k] = aux[j++]
                    inversions += (mid - i + 1).toLong()
                }
                else -> a[k] = aux[i++]
            }
        return inversions
    }

    // return the number of inversions in the subarray b[lo..hi]
    // side effect b[lo..hi] is rearranged in ascending order
    private fun <Key : Comparable<Key>> count(a: Array<Key>, b: Array<Key>, aux: Array<Key>, lo: Int, hi: Int): Long {
        var inversions: Long = 0
        if (hi <= lo) return 0
        val mid = (hi + lo) / 2
        inversions += count(a, b, aux, lo, mid)
        inversions += count(a, b, aux, mid + 1, hi)
        inversions += merge(b, aux, lo, mid, hi)
        assert(inversions == brute(a, lo, hi))
        return inversions
    }

    /**
     * Returns the number of inversions in the comparable array.
     * The argument array is not modified.
     * @param  a the array
     * @param <Key> the inferred type of the elements in the array
     * @return the number of inversions in the array. An inversion is a pair of
     * indicies `i` and `j` such that `i < j`
     * and `a[i].compareTo(a[j]) > 0`.
    </Key> */
    fun <Key : Comparable<Key>> count(a: Array<Key>): Long {
        val b = a.clone()
        val aux = a.clone()
        val inversions = count(a, b, aux, 0, a.size - 1)
        return count(a, b, aux, 0, a.size - 1)
    }

    // is v < w ?
    private fun <Key : Comparable<Key>> less(v: Key, w: Key) = v < w

    // count number of inversions in a[lo..hi] via brute force (for debugging only)
    private fun <Key : Comparable<Key>> brute(a: Array<Key>, lo: Int, hi: Int): Long {
        var inversions: Long = 0
        for (i in lo..hi)
            for (j in i + 1..hi)
                if (less(a[j], a[i])) inversions++
        return inversions
    }

    // count number of inversions in a[lo..hi] via brute force (for debugging only)
    private fun brute(a: IntArray, lo: Int, hi: Int): Long {
        var inversions: Long = 0
        for (i in lo..hi)
            for (j in i + 1..hi)
                if (a[j] < a[i]) inversions++
        return inversions
    }

    /**
     * Reads a sequence of integers from standard input and
     * prints the number of inversions to standard output.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = StdIn.readAllInts()
        val n = a.size
        val b = Array(n) { a[it] }
        StdOut.println(Inversions.count(a))
        StdOut.println(Inversions.count(b))
    }
}// do not instantiate

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