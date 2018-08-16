/******************************************************************************
 * Compilation:  javac Shell.java
 * Execution:    java Shell < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/21elementary/tiny.txt
 * https://algs4.cs.princeton.edu/21elementary/words3.txt
 *
 * Sorts a sequence of strings from standard input using shellsort.
 *
 * Uses increment sequence proposed by Sedgewick and Incerpi.
 * The nth element of the sequence is the smallest integer >= 2.5^n
 * that is relatively prime to all previous terms in the sequence.
 * For example, incs[4] is 41 because 2.5^4 = 39.0625 and 41 is
 * the next integer that is relatively prime to 3, 7, and 16.
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java Shell < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java Shell < words3.txt
 * all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Shell` class provides static methods for sorting an
 * array using Shellsort with Knuth's increment sequence (1, 4, 13, 40, ...).
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
object Shell {
    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T : Comparable<T>> sort(a: Array<T>) {
        val n = a.size

        // 3x+1 increment sequence:  1, 4, 13, 40, 121, 364, 1093, ...
        var h = 1
        while (h < n / 3) h = 3 * h + 1

        while (h >= 1) {
            // h-sort the array
            for (i in h until n) {
                var j = i
                while (j >= h && less(a[j], a[j - h])) {
                    exch(a, j, j - h)
                    j -= h
                }
            }
            assert(isHsorted(a, h))
            h /= 3
        }
        assert(isSorted(a))
    }

    // is v < w ?
    private fun <T : Comparable<T>> less(v: T, w: T) = v < w

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

    // is the array h-sorted?
    private fun <T : Comparable<T>> isHsorted(a: Array<T>, h: Int): Boolean {
        for (i in h until a.size)
            if (less(a[i], a[i - h])) return false
        return true
    }

    // print array to standard output
    private fun <T> show(a: Array<T>) {
        for (i in a)
            StdOut.println(i)
    }

    /**
     * Reads in a sequence of strings from standard input; Shellsorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = StdIn.readAllStrings()
        Shell.sort(a)
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