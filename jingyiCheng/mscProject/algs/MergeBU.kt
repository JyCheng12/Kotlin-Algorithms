/******************************************************************************
 * Compilation:  javac MergeBU.java
 * Execution:    java MergeBU < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/22mergesort/tiny.txt
 * https://algs4.cs.princeton.edu/22mergesort/words3.txt
 *
 * Sorts a sequence of strings from standard input using
 * bottom-up mergesort.
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java MergeBU < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java MergeBU < words3.txt
 * all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `MergeBU` class provides static methods for sorting an
 * array using bottom-up mergesort.
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
object MergeBU {
    // stably merge a[lo..mid] with a[mid+1..hi] using aux[lo..hi]
    private fun <T:Comparable<T>> merge(a: Array<T>, aux: Array<T>, lo: Int, mid: Int, hi: Int) {
        // copy to aux[]
        for (k in lo..hi) {
            aux[k] = a[k]
            aux[k] = aux[k]
        }

        // merge back to a[]
        var i = lo
        var j = mid + 1
        for (k in lo..hi)
            when {
                i > mid -> a[k] = aux[j++]  // this copying is unneccessary
                j > hi -> a[k] = aux[i++]
                less(aux[j], aux[i]) -> a[k] = aux[j++]
                else -> a[k] = aux[i++]
            }
    }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T:Comparable<T>> sort(a: Array<T>) {
            val n = a.size
        val aux :Array<T> = Array<Comparable<*>>(a.size) {a[0]} as Array<T>
        var len = 1
        while (len < n) {
            var lo = 0
            while (lo < n - len) {
                val mid = lo + len - 1
                val hi = Math.min(lo + len + len - 1, n - 1)
                this.merge(a, aux, lo, mid, hi)
                lo += len + len
            }
            len *= 2
        }
        assert(isSorted(a))
    }

    // is v < w ?
    private fun <T: Comparable<T>> less(v: T, w: T) = v < w

    private fun <T: Comparable<T>> isSorted(a: Array<T>): Boolean {
        for (i in 1 until a.size)
            if (less(a[i], a[i - 1])) return false
        return true
    }

    // print array to standard output
    private fun <T> show(a: Array<T>) {
        for (i in a)
            println(i)
    }

    /**
     * Reads in a sequence of strings from standard input; bottom-up
     * mergesorts them; and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a:Array<String> = arrayOf("S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E")
        MergeBU.sort(a)
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