/******************************************************************************
 * Compilation:  javac InsertionX.java
 * Execution:    java InsertionX < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/21elementary/tiny.txt
 * https://algs4.cs.princeton.edu/21elementary/words3.txt
 *
 * Sorts a sequence of strings from standard input using an optimized
 * version of insertion sort that uses half exchanges instead of
 * full exchanges to reduce data movement..
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java InsertionX < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java InsertionX < words3.txt
 * all bad bed bug dad ... yes yet zoo   [ one string per line ]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `InsertionX` class provides static methods for sorting
 * an array using an optimized version of insertion sort (with half exchanges
 * and a sentinel).
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

object InsertionX {
    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T : Comparable<T>> sort(a: Array<T>) {
        val n = a.size

        // put smallest element in position to serve as sentinel
        var exchanges = 0
        for (i in n - 1 downTo 1)
            if (less(a[i], a[i - 1])) {
                exch(a, i, i - 1)
                exchanges++
            }
        if (exchanges == 0) return

        // insertion sort with half-exchanges
        for (i in 2 until n) {
            val v = a[i]
            var j = i
            while (less(v, a[j - 1])) {
                a[j] = a[j - 1]
                j--
            }
            a[j] = v
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
        val a = StdIn.readAllStrings()
        InsertionX.sort(a as Array<String>)
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