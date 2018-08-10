/******************************************************************************
 * Compilation:  javac Insertion.java
 * Execution:    java Insertion < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/21elementary/tiny.txt
 * https://algs4.cs.princeton.edu/21elementary/words3.txt
 *
 * Sorts a sequence of strings from standard input using insertion sort.
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java Insertion < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java Insertion < words3.txt
 * all bad bed bug dad ... yes yet zoo   [ one string per line ]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Insertion` class provides static methods for sorting an
 * array using insertion sort.
 *
 *
 * This implementation makes ~ 1/2 n^2 compares and exchanges in
 * the worst case, so it is not suitable for sorting large arbitrary arrays.
 * More precisely, the number of exchanges is exactly equal to the number
 * of inversions. So, for example, it sorts a partially-sorted array
 * in linear time.
 *
 *
 * The sorting algorithm is stable and uses O(1) extra memory.
 *
 *
 * See [InsertionPedantic.java](https://algs4.cs.princeton.edu/21elementary/InsertionPedantic.java.html)
 * for a version that eliminates the compiler warning.
 *
 *
 * For additional documentation, see [Section 2.1](https://algs4.cs.princeton.edu/21elementary) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object Insertion {
    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T : Comparable<T>> sort(a: Array<T>) {
        val n = a.size
        for (i in 0 until n) {
            var j = i
            while (j > 0 && less(a[j], a[j - 1])) {
                exch(a, j, j - 1)
                j--
            }
            assert(isSorted(a, 0, i))
        }
        assert(isSorted(a))
    }

    /**
     * Rearranges the subarray a[lo..hi) in ascending order, using the natural order.
     * @param a the array to be sorted
     * @param lo left endpoint (inclusive)
     * @param hi right endpoint (exclusive)
     */
    fun <T : Comparable<T>> sort(a: Array<T>, lo: Int, hi: Int) {
        for (i in lo until hi) {
            var j = i
            while (j > lo && less(a[j], a[j - 1])) {
                exch(a, j, j - 1)
                j--
            }
        }
        assert(isSorted(a, lo, hi))
    }

    /**
     * Rearranges the array in ascending order, using a comparator.
     * @param a the array
     * @param comparator the comparator specifying the order
     */
    fun <T> sort(a: Array<T>, comparator: Comparator<T>) {
        val n = a.size
        for (i in 0 until n) {
            var j = i
            while (j > 0 && less(a[j], a[j - 1], comparator)) {
                exch(a, j, j - 1)
                j--
            }
            assert(isSorted(a, 0, i, comparator))
        }
        assert(isSorted(a, comparator))
    }

    /**
     * Rearranges the subarray a[lo..hi) in ascending order, using a comparator.
     * @param a the array
     * @param lo left endpoint (inclusive)
     * @param hi right endpoint (exclusive)
     * @param comparator the comparator specifying the order
     */
    fun <T> sort(a: Array<T>, lo: Int, hi: Int, comparator: Comparator<T>) {
        for (i in lo until hi) {
            var j = i
            while (j > lo && less(a[j], a[j - 1], comparator)) {
                exch(a, j, j - 1)
                j--
            }
        }
        assert(isSorted(a, lo, hi, comparator))
    }

    /**
     * Returns a permutation that gives the elements in the array in ascending order.
     * @param a the array
     * @return a permutation `p[]` such that `a[p[0]]`, `a[p[1]]`,
     * ..., `a[p[n-1]]` are in ascending order
     */
    fun <T : Comparable<T>> indexSort(a: Array<T>): IntArray {
        val n = a.size
        val index = IntArray(n) { it }

        for (i in 0 until n) {
            var j = i
            while (j > 0 && less(a[index[j]], a[index[j - 1]])) {
                exch(index, j, j - 1)
                j--
            }
        }
        return index
    }

    // is v < w ?
    private fun <T : Comparable<T>> less(v: T, w: T) = v < w

    // is v < w ?
    private fun <T> less(v: T, w: T, comparator: Comparator<T>) = comparator.compare(v, w) < 0

    // exchange a[i] and a[j]
    private fun <T> exch(a: Array<T>, i: Int, j: Int) {
        val swap = a[i]
        a[i] = a[j]
        a[j] = swap
    }

    // exchange a[i] and a[j]  (for indirect sort)
    private fun exch(a: IntArray, i: Int, j: Int) {
        val swap = a[i]
        a[i] = a[j]
        a[j] = swap
    }

    // is the array a[lo..hi) sorted
    private fun <T : Comparable<T>> isSorted(a: Array<T>, lo: Int = 0, hi: Int = a.size): Boolean {
        for (i in lo + 1 until hi)
            if (less(a[i], a[i - 1])) return false
        return true
    }

    private fun <T> isSorted(a: Array<T>, comparator: Comparator<T>) = isSorted(a, 0, a.size, comparator)

    // is the array a[lo..hi) sorted
    private fun <T> isSorted(a: Array<T>, lo: Int, hi: Int, comparator: Comparator<T>): Boolean {
        for (i in lo + 1 until hi)
            if (less(a[i], a[i - 1], comparator)) return false
        return true
    }

    // print array to standard output
    private fun <T> show(a: Array<T>) {
        for (i in a)
            StdOut.println(i)
    }

    /**
     * Reads in a sequence of strings from standard input; insertion sorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = arrayOf("S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E")
        Insertion.sort(a)
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
