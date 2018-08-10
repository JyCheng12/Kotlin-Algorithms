/******************************************************************************
 * Compilation:  javac QuickX.java
 * Execution:    java QuickX < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/23quicksort/tiny.txt
 * https://algs4.cs.princeton.edu/23quicksort/words3.txt
 *
 * Uses the Hoare's 2-way partitioning scheme, chooses the partitioning
 * element using median-of-3, and cuts off to insertion sort.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `QuickX` class provides static methods for sorting an array
 * using an optimized version of quicksort (using Hoare's 2-way partitioning
 * algorithm, median-of-3 to choose the partitioning element, and cutoff
 * to insertion sort).
 *
 *
 * For additional documentation,
 * see [Section 2.3](https://algs4.cs.princeton.edu/23quick) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object QuickX {
    // cutoff to insertion sort, must be >= 1
    private const val INSERTION_SORT_CUTOFF = 8

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T:Comparable<T>> sort(a: Array<T>) {
        //StdRandom.shuffle(a);
        sort(a, 0, a.size - 1)
        //assert(isSorted(a))
    }

    // quicksort the subarray from a[lo] to a[hi]
    private fun <T:Comparable<T>> sort(a: Array<T>, lo: Int, hi: Int) {
        if (hi <= lo) return

        // cutoff to insertion sort (Insertion.sort() uses half-open intervals)
        val n = hi - lo + 1
        if (n <= INSERTION_SORT_CUTOFF) {
            Insertion.sort(a, lo, hi + 1)
            return
        }
        val j = partition(a, lo, hi)
        sort(a, lo, j - 1)
        sort(a, j + 1, hi)
    }

    // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
    // and return the index j.
    private fun <T:Comparable<T>> partition(a: Array<T>, lo: Int, hi: Int): Int {
        val n = hi - lo + 1
        val m = median3(a, lo, lo + n / 2, hi)
        exch(a, m, lo)

        var i = lo
        var j = hi + 1
        val v = a[lo]

        // a[lo] is unique largest element
        while (less(a[++i], v)) {
            if (i == hi) {
                exch(a, lo, hi)
                return hi
            }
        }

        // a[lo] is unique smallest element
        while (less(v, a[--j])) {
            if (j == lo + 1) return lo
        }

        // the main loop
        while (i < j) {
            exch(a, i, j)
            while (less(a[++i], v));
            while (less(v, a[--j]));
        }

        // put partitioning item v at a[j]
        exch(a, lo, j)

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j
    }

    // return the index of the median element among a[i], a[j], and a[k]
    private fun <T:Comparable<T>> median3(a: Array<T>, i: Int, j: Int, k: Int): Int {
        return if (less(a[i], a[j]))
            if (less(a[j], a[k])) j else if (less(a[i], a[k])) k else i
        else
            if (less(a[k], a[j])) j else if (less(a[k], a[i])) k else i
    }

    // is v < w ?
    private fun <T:Comparable<T>> less(v: T, w: T) = v < w

    // exchange a[i] and a[j]
    private fun <T> exch(a: Array<T>, i: Int, j: Int) {
        val swap = a[i]
        a[i] = a[j]
        a[j] = swap
    }

    private fun <T:Comparable<T>> isSorted(a: Array<T>): Boolean {
        for (i in 1 until a.size)
            if (less(a[i], a[i - 1])) return false
        return true
    }

    // print array to standard output
    private fun <T:Comparable<T>> show(a: Array<T>) {
        for (i in a)
            StdOut.println(i)
    }

    /**
     * Reads in a sequence of strings from standard input; quicksorts them
     * (using an optimized version of 2-way quicksort);
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = arrayOf("S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E")
        QuickX.sort(a)
        assert(isSorted(a))
        show(a)
    }

}// This class should not be instantiated.

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
