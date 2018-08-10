/******************************************************************************
 * Compilation:  javac Quick.java
 * Execution:    java Quick < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/23quicksort/tiny.txt
 * https://algs4.cs.princeton.edu/23quicksort/words3.txt
 *
 * Sorts a sequence of strings from standard input using quicksort.
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java Quick < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java Quick < words3.txt
 * all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 *
 * Remark: For a type-safe version that uses static generics, see
 *
 * https://algs4.cs.princeton.edu/23quicksort/QuickPedantic.java
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Quick` class provides static methods for sorting an
 * array and selecting the ith smallest element in an array using quicksort.
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
object Quick {
    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T : Comparable<T>> sort(a: Array<T>) {
        StdRandom.shuffle(a)
        sort(a, 0, a.size - 1)
        assert(isSorted(a))
    }

    // quicksort the subarray from a[lo] to a[hi]
    private fun <T : Comparable<T>> sort(a: Array<T>, lo: Int, hi: Int) {
        if (hi <= lo) return
        val j = partition(a, lo, hi)
        sort(a, lo, j - 1)
        sort(a, j + 1, hi)
        assert(isSorted(a, lo, hi))
    }

    // partition the subarray a[lo..hi] so that a[lo..j-1] <= a[j] <= a[j+1..hi]
    // and return the index j.
    private fun <T : Comparable<T>> partition(a: Array<T>, lo: Int, hi: Int): Int {
        var i = lo
        var j = hi + 1
        val v = a[lo]
        while (true) {
            // find item on lo to swap
            while (less(a[++i], v)) {
                if (i == hi) break
            }

            // find item on hi to swap
            while (less(v, a[--j])) {
                if (j == lo) break      // redundant since a[lo] acts as sentinel
            }

            // check if pointers cross
            if (i >= j) break
            exch(a, i, j)
        }
        // put partitioning item v at a[j]
        exch(a, lo, j)

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j
    }

    /**
     * Rearranges the array so that `a[k]` contains the kth smallest key;
     * `a[0]` through `a[k-1]` are less than (or equal to) `a[k]`; and
     * `a[k+1]` through `a[n-1]` are greater than (or equal to) `a[k]`.
     *
     * @param  a the array
     * @param  k the rank of the key
     * @return the key of rank `k`
     * @throws IllegalArgumentException unless `0 <= k < a.length`
     */
    fun <T : Comparable<T>> select(a: Array<T>, k: Int): Comparable<T> {
        if (k < 0 || k >= a.size) throw IllegalArgumentException("index is not between 0 and ${a.size}: $k")
        StdRandom.shuffle(a)
        var lo = 0
        var hi = a.size - 1
        while (hi > lo) {
            val i = partition(a, lo, hi)
            when {
                i > k -> hi = i - 1
                i < k -> lo = i + 1
                else -> return a[i]
            }
        }
        return a[lo]
    }

    // is v < w ?
    private fun <T : Comparable<T>> less(v: T, w: T) = if (v === w) false else v < w   // optimization when reference equals

    // exchange a[i] and a[j]
    private fun <T> exch(a: Array<T>, i: Int, j: Int) {
        val swap = a[i]
        a[i] = a[j]
        a[j] = swap
    }

    private fun <T : Comparable<T>> isSorted(a: Array<T>, lo: Int = 0, hi: Int = a.size - 1): Boolean {
        for (i in lo + 1..hi)
            if (less(a[i], a[i - 1])) return false
        return true
    }

    // print array to standard output
    private fun <T> show(a: Array<T>) {
        for (i in a) {
            StdOut.println(i)
        }
    }

    /**
     * Reads in a sequence of strings from standard input; quicksorts them;
     * and prints them to standard output in ascending order.
     * Shuffles the array and then prints the strings again to
     * standard output, but this time, using the select method.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = arrayOf("S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E")
        Quick.sort(a)
        show(a)
        assert(isSorted(a))

        // shuffle
        StdRandom.shuffle(a)

        // display results again using select
        StdOut.println()
        for (i in a.indices) {
            val ith = Quick.select(a, i) as String
            StdOut.println(ith)
        }
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
