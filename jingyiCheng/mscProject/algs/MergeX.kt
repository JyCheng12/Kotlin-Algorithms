/******************************************************************************
 * Compilation:  javac MergeX.java
 * Execution:    java MergeX < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/22mergesort/tiny.txt
 * https://algs4.cs.princeton.edu/22mergesort/words3.txt
 *
 * Sorts a sequence of strings from standard input using an
 * optimized version of mergesort.
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java MergeX < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java MergeX < words3.txt
 * all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `MergeX` class provides static methods for sorting an
 * array using an optimized version of mergesort.
 *
 *
 * For additional documentation, see [Section 2.2](https://algs4.cs.princeton.edu/22mergesort) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object MergeX {
    private const val CUTOFF = 7  // cutoff to insertion sort

    private fun <T:Comparable<T>> merge(src: Array<T>, dst: Array<T>, lo: Int, mid: Int, hi: Int) {
        // precondition: src[lo .. mid] and src[mid+1 .. hi] are sorted subarrays
        //assert(isSorted(src, lo, mid))
        //assert(isSorted(src, mid + 1, hi))

        var i = lo
        var j = mid + 1
        for (k in lo..hi)
            when {
                i > mid -> dst[k] = src[j++]
                j > hi -> dst[k] = src[i++]
                less(src[j], src[i]) -> dst[k] = src[j++]   // to ensure stability
                else -> dst[k] = src[i++]
            }

        // postcondition: dst[lo .. hi] is sorted subarray
        //assert(isSorted(dst, lo, hi))
    }

    private fun <T:Comparable<T>> sort(src: Array<T>, dst: Array<T>, lo: Int, hi: Int) {
        if (hi <= lo + CUTOFF) {
            insertionSort(dst, lo, hi)
            return
        }
        val mid = lo + (hi - lo) / 2
        sort(dst, src, lo, mid)
        sort(dst, src, mid + 1, hi)

        // using System.arraycopy() is a bit faster than the above loop
        if (!less(src[mid + 1], src[mid])) {
            System.arraycopy(src, lo, dst, lo, hi - lo + 1)
            return
        }
        merge(src, dst, lo, mid, hi)
    }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T:Comparable<T>> sort(a: Array<T>) {
        val aux = a.clone()
        sort(aux, a, 0, a.size - 1)
        //assert(isSorted(a))
    }

    // sort from a[lo] to a[hi] using insertion sort
    private fun <T:Comparable<T>> insertionSort(a: Array<T>, lo: Int, hi: Int) {
        for (i in lo..hi) {
            var j = i
            while (j > lo && less(a[j], a[j - 1])) {
                exch(a, j, j - 1)
                j--
            }
        }
    }

    // exchange a[i] and a[j]
    private fun <T> exch(a: Array<T>, i: Int, j: Int) {
        val swap = a[i]
        a[i] = a[j]
        a[j] = swap
    }

    // is a[i] < a[j]?
    private fun <T:Comparable<T>> less(a: T, b: T) = a < b

    // is a[i] < a[j]?
    private fun <T> less(a: T, b: T, comparator: Comparator<T>) = comparator.compare(a, b) < 0

    /**
     * Rearranges the array in ascending order, using the provided order.
     *
     * @param a the array to be sorted
     * @param comparator the comparator that defines the total order
     */
    fun <T> sort(a: Array<T>, comparator: Comparator<T>) {
        val aux = a.clone()
        sort(aux, a, 0, a.size - 1, comparator)
        //assert(isSorted(a, comparator))
    }

    private fun <T> merge(src: Array<T>, dst: Array<T>, lo: Int, mid: Int, hi: Int, comparator: Comparator<T>) {
        // precondition: src[lo .. mid] and src[mid+1 .. hi] are sorted subarrays
        //assert(isSorted(src, lo, mid, comparator))
        //assert(isSorted(src, mid + 1, hi, comparator))

        var i = lo
        var j = mid + 1
        for (k in lo..hi)
            when {
                i > mid -> dst[k] = src[j++]
                j > hi -> dst[k] = src[i++]
                less(src[j], src[i], comparator) -> dst[k] = src[j++]
                else -> dst[k] = src[i++]
            }

        // postcondition: dst[lo .. hi] is sorted subarray
        //assert(isSorted(dst, lo, hi, comparator))
    }

    private fun <T> sort(src: Array<T>, dst: Array<T>, lo: Int, hi: Int, comparator: Comparator<T>) {
        // if (hi <= lo) return;
        if (hi <= lo + CUTOFF) {
            insertionSort(dst, lo, hi, comparator)
            return
        }
        val mid = lo + (hi - lo) / 2
        sort(dst, src, lo, mid, comparator)
        sort(dst, src, mid + 1, hi, comparator)

        // using System.arraycopy() is a bit faster than the above loop
        if (!less(src[mid + 1], src[mid], comparator)) {
            System.arraycopy(src, lo, dst, lo, hi - lo + 1)
            return
        }
        merge(src, dst, lo, mid, hi, comparator)
    }

    // sort from a[lo] to a[hi] using insertion sort
    private fun <T> insertionSort(a: Array<T>, lo: Int, hi: Int, comparator: Comparator<T>) {
        for (i in lo..hi) {
            var j = i
            while (j > lo && less(a[j], a[j - 1], comparator)) {
                exch(a, j, j - 1)
                j--
            }
        }
    }

    private fun <T:Comparable<T>> isSorted(a: Array<T>, lo: Int = 0, hi: Int = a.size - 1): Boolean {
        for (i in lo + 1..hi)
            if (less(a[i], a[i - 1])) return false
        return true
    }

    private fun <T> isSorted(a: Array<T>, comparator: Comparator<T>) = isSorted(a, 0, a.size - 1, comparator)

    private fun <T> isSorted(a: Array<T>, lo: Int, hi: Int, comparator: Comparator<T>): Boolean {
        for (i in lo + 1..hi)
            if (less(a[i], a[i - 1], comparator)) return false
        return true
    }

    // print array to standard output
    private fun <T> show(a: Array<T>) {
        for (i in a)
            StdOut.println(i)
    }

    /**
     * Reads in a sequence of strings from standard input; mergesorts them
     * (using an optimized version of mergesort);
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = arrayOf("S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E")
        MergeX.sort(a)
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