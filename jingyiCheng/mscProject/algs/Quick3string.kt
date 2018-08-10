/******************************************************************************
 * Compilation:  javac Quick3string.java
 * Execution:    java Quick3string < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/51radix/words3.txt
 * https://algs4.cs.princeton.edu/51radix/shells.txt
 *
 * Reads string from standard input and 3-way string quicksort them.
 *
 * % java Quick3string < shell.txt
 * are
 * by
 * sea
 * seashells
 * seashells
 * sells
 * sells
 * she
 * she
 * shells
 * shore
 * surely
 * the
 * the
 *
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Quick3string` class provides static methods for sorting an
 * array of strings using 3-way radix quicksort.
 *
 *
 * For additional documentation,
 * see [Section 5.1](https://algs4.cs.princeton.edu/51radix) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object Quick3string {
    private const val CUTOFF = 15   // cutoff to insertion sort

    /**
     * Rearranges the array of strings in ascending order.
     *
     * @param a the array to be sorted
     */
    @JvmStatic
    fun sort(a: Array<String>) {
        StdRandom.shuffle(a)
        sort(a, 0, a.size - 1, 0)
        assert(isSorted(a))
    }

    // return the dth character of s, -1 if d = length of s
    private fun charAt(s: String, d: Int): Int {
        assert(d >= 0 && d <= s.length)
        return if (d == s.length) -1 else s[d].toInt()
    }


    // 3-way string quicksort a[lo..hi] starting at dth character
    private fun sort(a: Array<String>, lo: Int, hi: Int, d: Int) {
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d)
            return
        }

        var lt = lo
        var gt = hi
        val v = charAt(a[lo], d)
        var i = lo + 1
        while (i <= gt) {
            val t = charAt(a[i], d)
            when {
                t < v -> exch(a, lt++, i++)
                t > v -> exch(a, i, gt--)
                else -> i++
            }
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(a, lo, lt - 1, d)
        if (v >= 0) sort(a, lt, gt, d + 1)
        sort(a, gt + 1, hi, d)
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private fun insertion(a: Array<String>, lo: Int, hi: Int, d: Int) {
        for (i in lo..hi) {
            var j = i
            while (j > lo && less(a[j], a[j - 1], d)) {
                exch(a, j, j - 1)
                j--
            }
        }
    }

    // exchange a[i] and a[j]
    private fun exch(a: Array<String>, i: Int, j: Int) {
        val temp = a[i]
        a[i] = a[j]
        a[j] = temp
    }

    // is v less than w, starting at character d
    private fun less(v: String, w: String, d: Int): Boolean {
        assert(v.substring(0, d) == w.substring(0, d))
        for (i in d until Math.min(v.length, w.length)) {
            if (v[i] < w[i]) return true
            if (v[i] > w[i]) return false
        }
        return v.length < w.length
    }

    // is the array sorted
    private fun isSorted(a: Array<String>): Boolean {
        for (i in 1 until a.size)
            if (a[i] < a[i - 1]) return false
        return true
    }

    /**
     * Reads in a sequence of fixed-length strings from standard input;
     * 3-way radix quicksorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        // read in the strings from standard input
        val a = StdIn.readAllStrings()

        // sort the strings
        sort(a)

        // print the results
        for (i in a) StdOut.println(i)
    }
}// do not instantiate

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
