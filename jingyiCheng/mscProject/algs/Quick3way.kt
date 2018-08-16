/******************************************************************************
 * Compilation:  javac Quick3way.java
 * Execution:    java Quick3way < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/23quicksort/tiny.txt
 * https://algs4.cs.princeton.edu/23quicksort/words3.txt
 *
 * Sorts a sequence of strings from standard input using 3-way quicksort.
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java Quick3way < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java Quick3way < words3.txt
 * all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Quick3way` class provides static methods for sorting an
 * array using quicksort with 3-way partitioning.
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
object Quick3way {
    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T : Comparable<T>> sort(a: Array<T>) {
        StdRandom.shuffle(a)
        sort(a, 0, a.size - 1)
        assert(isSorted(a))
    }

    // quicksort the subarray a[lo .. hi] using 3-way partitioning
    private fun <T : Comparable<T>> sort(a: Array<T>, lo: Int, hi: Int) {
        if (hi <= lo) return
        var lt = lo
        var gt = hi
        val v = a[lo]
        var i = lo + 1
        while (i <= gt) {
            val cmp = a[i].compareTo(v)
            when {
                cmp < 0 -> exch(a, lt++, i++)
                cmp > 0 -> exch(a, i, gt--)
                else -> i++
            }
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(a, lo, lt - 1)
        sort(a, gt + 1, hi)
        assert(isSorted(a, lo, hi))
    }

    // is v < w ?
    private fun <T : Comparable<T>> less(v: T, w: T) = v < w

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
    private fun <T : Comparable<T>> show(a: Array<T>) {
        for (i in a)
            StdOut.println(i)
    }

    /**
     * Reads in a sequence of strings from standard input; 3-way
     * quicksorts them; and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = arrayOf("S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E")
        Quick3way.sort(a)
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