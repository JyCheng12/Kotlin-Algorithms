/******************************************************************************
 * Compilation:  javac Selection.java
 * Execution:    java  Selection < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/21elementary/tiny.txt
 * https://algs4.cs.princeton.edu/21elementary/words3.txt
 *
 * Sorts a sequence of strings from standard input using selection sort.
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java Selection < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java Selection < words3.txt
 * all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Selection` class provides static methods for sorting an
 * array using selection sort.
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
object Selection {
    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T : Comparable<T>> sort(a: Array<T>) {
        val n = a.size
        for (i in 0 until n) {
            var min = i
            for (j in i + 1 until n) {
                if (less(a[j], a[min])) min = j
            }
            exch(a, i, min)
            assert(isSorted(a, 0, i))
        }
        assert(isSorted(a))
    }

    /**
     * Rearranges the array in ascending order, using a comparator.
     * @param a the array
     * @param comparator the comparator specifying the order
     */
    fun <T> sort(a: Array<T>, comparator: Comparator<T>) {
        val n = a.size
        for (i in 0 until n) {
            var min = i
            for (j in i + 1 until n) {
                if (less(a[j], a[min], comparator)) min = j
            }
            exch(a, i, min)
            assert(isSorted(a, comparator, 0, i))
        }
        assert(isSorted(a, comparator))
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

    // is the array sorted from a[lo] to a[hi]
    private fun <T : Comparable<T>> isSorted(a: Array<T>, lo: Int = 0, hi: Int = a.size - 1): Boolean {
        for (i in lo + 1..hi)
            if (less(a[i], a[i - 1])) return false
        return true
    }

    // is the array sorted from a[lo] to a[hi]
    private fun <T> isSorted(a: Array<T>, comparator: Comparator<T>, lo: Int = 0, hi: Int = a.size - 1): Boolean {
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
     * Reads in a sequence of strings from standard input; selection sorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = arrayOf("S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E")
        Selection.sort(a)
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
