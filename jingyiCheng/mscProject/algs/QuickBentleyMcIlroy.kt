/******************************************************************************
 * Compilation:  javac QuickBentleyMcIlroy.java
 * Execution:    java QuickBentleyMcIlroy < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/23quicksort/tiny.txt
 * https://algs4.cs.princeton.edu/23quicksort/words3.txt
 *
 * Uses the Bentley-McIlroy 3-way partitioning scheme,
 * chooses the partitioning element using Tukey's ninther,
 * and cuts off to insertion sort.
 *
 * Reference: Engineering a Sort Function by Jon L. Bentley
 * and M. Douglas McIlroy. Softwae-Practice and Experience,
 * Vol. 23 (11), 1249-1265 (November 1993).
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `QuickBentleyMcIlroy` class provides static methods for sorting
 * an array using an optimized version of quicksort (using Bentley-McIlroy
 * 3-way partitioning, Tukey's ninther, and cutoff to insertion sort).
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
object QuickBentleyMcIlroy {

    // cutoff to insertion sort, must be >= 1
    private const val INSERTION_SORT_CUTOFF = 8

    // cutoff to median-of-3 partitioning
    private const val MEDIAN_OF_3_CUTOFF = 40

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T : Comparable<T>> sort(a: Array<T>) = sort(a, 0, a.size - 1)

    private fun <T : Comparable<T>> sort(a: Array<T>, lo: Int, hi: Int) {
        val n = hi - lo + 1

        // cutoff to insertion sort
        when {
            n <= INSERTION_SORT_CUTOFF -> {
                insertionSort(a, lo, hi)
                return
            }
            n <= MEDIAN_OF_3_CUTOFF -> {
                val m = median3(a, lo, lo + n / 2, hi)
                exch(a, m, lo)
            }
            else -> {
                val eps = n / 8
                val mid = lo + n / 2
                val m1 = median3(a, lo, lo + eps, lo + eps + eps)
                val m2 = median3(a, mid - eps, mid, mid + eps)
                val m3 = median3(a, hi - eps - eps, hi - eps, hi)
                val ninther = median3(a, m1, m2, m3)
                exch(a, ninther, lo)
            }
        }// use Tukey ninther as partitioning element
        // use median-of-3 as partitioning element

        // Bentley-McIlroy 3-way partitioning
        var i = lo
        var j = hi + 1
        var p = lo
        var q = hi + 1
        val v = a[lo]
        while (true) {
            while (less(a[++i], v))
                if (i == hi) break
            while (less(v, a[--j]))
                if (j == lo) break

            // pointers cross
            if (i == j && eq(a[i], v))
                exch(a, ++p, i)
            if (i >= j) break

            exch(a, i, j)
            if (eq(a[i], v)) exch(a, ++p, i)
            if (eq(a[j], v)) exch(a, --q, j)
        }


        i = j + 1
        for (k in lo..p) exch(a, k, j--)
        for (k in hi downTo q) exch(a, k, i++)

        sort(a, lo, j)
        sort(a, i, hi)
    }

    // sort from a[lo] to a[hi] using insertion sort
    private fun <T : Comparable<T>> insertionSort(a: Array<T>, lo: Int, hi: Int) {
        for (i in lo..hi) {
            var j = i
            while (j > lo && less(a[j], a[j - 1])) {
                exch(a, j, j - 1)
                j--
            }
        }
    }

    // return the index of the median element among a[i], a[j], and a[k]
    private fun <T : Comparable<T>> median3(a: Array<T>, i: Int, j: Int, k: Int) = if (less(a[i], a[j]))
        if (less(a[j], a[k])) j else if (less(a[i], a[k])) k else i
    else
        if (less(a[k], a[j])) j else if (less(a[k], a[i])) k else i

    // is v < w ?
    private fun <T : Comparable<T>> less(v: T, w: T) = if (v === w) false else v < w    // optimization when reference equal

    // does v == w ?
    private fun <T : Comparable<T>> eq(v: T, w: T) = if (v === w) true else v == w    // optimization when reference equal

    // exchange a[i] and a[j]
    private fun <T> exch(a: Array<T>, i: Int, j: Int) {
        val swap = a[i]
        a[i] = a[j]
        a[j] = swap
    }

    private fun <T : Comparable<T>> isSorted(a: Array<T>): Boolean {
        for (i in 1 until a.size)
            if (less(a[i], a[i - 1])) return false
        return true
    }

    // print array to standard output
    private fun <T : Comparable<T>> show(a: Array<T>) {
        for (i in a)
            StdOut.println(i)
    }

    /**
     * Reads in a sequence of strings from standard input; quicksorts them
     * (using an optimized version of quicksort);
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = StdIn.readAllStrings()
        QuickBentleyMcIlroy.sort(a)
        assert(isSorted(a))
        show(a)
    }

}// This class should not be instantiated.

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