/******************************************************************************
 * Compilation: javac MSD.java
 * Execution:   java MSD < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/51radix/words3.txt
 * https://algs4.cs.princeton.edu/51radix/shells.txt
 *
 * Sort an array of strings or integers using MSD radix sort.
 *
 * % java MSD < shells.txt
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
 */

package jingyiCheng.mscProject.algs

/**
 * The `MSD` class provides static methods for sorting an
 * array of extended ASCII strings or integers using MSD radix sort.
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
object MSD {
    private const val BITS_PER_BYTE = 8
    private const val BITS_PER_INT = 32   // each Java int is 32 bits
    private const val R = 256   // extended ASCII alphabet size
    private const val CUTOFF = 15   // cutoff to insertion sort

    /**
     * Rearranges the array of extended ASCII strings in ascending order.
     *
     * @param a the array to be sorted
     */
    @JvmStatic
    fun sort(a: Array<String>) {
        val n = a.size
        val aux = Array(n){""}
        sort(a, 0, n - 1, 0, aux)
    }

    // return dth character of s, -1 if d = length of string
    private fun charAt(s: String, d: Int): Int {
        assert(d >= 0 && d <= s.length)
        return if (d == s.length) -1 else s[d].toInt()
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private fun sort(a: Array<String>, lo: Int, hi: Int, d: Int, aux: Array<String>) {
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d)
            return
        }

        // compute frequency counts
        val count = IntArray(R + 2)
        for (i in lo..hi) {
            val c = charAt(a[i], d)
            count[c + 2]++
        }

        // transform counts to indices
        for (r in 0 until R + 1)
            count[r + 1] += count[r]

        // distribute
        for (i in lo..hi) {
            val c = charAt(a[i], d)
            aux[count[c + 1]++] = a[i]
        }

        // copy back
        for (i in lo..hi)
            a[i] = aux[i - lo]


        // recursively sort for each character (excludes sentinel -1)
        for (r in 0 until R)
            sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux)
    }

    // insertion sort a[lo..hi], starting at dth character
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
        // assert v.substring(0, d).equals(w.substring(0, d));
        for (i in d until Math.min(v.length, w.length)) {
            if (v[i] < w[i]) return true
            if (v[i] > w[i]) return false
        }
        return v.length < w.length
    }

    /**
     * Rearranges the array of 32-bit integers in ascending order.
     * Currently assumes that the integers are nonnegative.
     *
     * @param a the array to be sorted
     */
    fun sort(a: IntArray) {
        val n = a.size
        val aux = IntArray(n)
        sort(a, 0, n - 1, 0, aux)
    }

    // MSD sort from a[lo] to a[hi], starting at the dth byte
    private fun sort(a: IntArray, lo: Int, hi: Int, d: Int, aux: IntArray) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d)
            return
        }

        // compute frequency counts (need R = 256)
        val count = IntArray(R + 1)
        val mask = R - 1   // 0xFF;
        val shift = BITS_PER_INT - BITS_PER_BYTE * d - BITS_PER_BYTE
        for (i in lo..hi) {
            val c = a[i] shr shift and mask
            count[c + 1]++
        }

        // transform counts to indicies
        for (r in 0 until R)
            count[r + 1] += count[r]

        // distribute
        for (i in lo..hi) {
            val c = a[i] shr shift and mask
            aux[count[c]++] = a[i]
        }

        // copy back
        for (i in lo..hi)
            a[i] = aux[i - lo]

        // no more bits
        if (d == 4) return

        // recursively sort for each character
        if (count[0] > 0)
            sort(a, lo, lo + count[0] - 1, d + 1, aux)
        for (r in 0 until R)
            if (count[r + 1] > count[r])
                sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux)
    }

    // TODO: insertion sort a[lo..hi], starting at dth character
    private fun insertion(a: IntArray, lo: Int, hi: Int, d: Int) {
        for (i in lo..hi) {
            var j = i
            while (j > lo && a[j] < a[j - 1]) {
                exch(a, j, j - 1)
                j--
            }
        }
    }

    // exchange a[i] and a[j]
    private fun exch(a: IntArray, i: Int, j: Int) {
        val temp = a[i]
        a[i] = a[j]
        a[j] = temp
    }

    /**
     * Reads in a sequence of extended ASCII strings from standard input;
     * MSD radix sorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = StdIn.readAllStrings()
        val n = a.size
        sort(a)
        for (i in 0 until n)
            StdOut.println(a[i])
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