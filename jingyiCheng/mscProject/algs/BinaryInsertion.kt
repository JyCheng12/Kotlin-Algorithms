/******************************************************************************
 * Compilation:  javac BinaryInsertion.java
 * Execution:    java BinaryInsertion < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/21elementary/tiny.txt
 * https://algs4.cs.princeton.edu/21elementary/words3.txt
 *
 * Sorts a sequence of strings from standard input using
 * binary insertion sort with half exchanges.
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java BinaryInsertion < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java BinaryInsertion < words3.txt
 * all bad bed bug dad ... yes yet zoo   [ one string per line ]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `BinaryInsertion` class provides a static method for sorting an
 * array using an optimized binary insertion sort with half exchanges.
 *
 *
 * This implementation makes ~ n lg n compares for any array of length n.
 * However, in the worst case, the running time is quadratic because the
 * number of array accesses can be proportional to n^2 (e.g, if the array
 * is reverse sorted). As such, it is not suitable for sorting large
 * arrays (unless the number of inversions is small).
 *
 *
 * The sorting algorithm is stable and uses O(1) extra memory.
 *
 *
 * For additional documentation, see [Section 2.1](https://algs4.cs.princeton.edu/21elementary) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Ivan Pesin
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object BinaryInsertion {

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T:Comparable<T>> sort(a: Array<T>) {
        val n = a.size
        for (i in 1 until n) {

            // binary search to determine index j at which to insert a[i]
            val v = a[i]
            var lo = 0
            var hi = i
            while (lo < hi) {
                val mid = lo + (hi - lo) / 2
                if (less(v, a[mid]))
                    hi = mid
                else
                    lo = mid + 1
            }

            // insetion sort with "half exchanges"
            // (insert a[i] at index j and shift a[j], ..., a[i-1] to right)
            for (j in i downTo lo+1) {
                a[j] = a[j - 1]
            }
            a[lo] = v
        }
        assert(isSorted(a))
    }

    // is v < w ?
    private fun <T:Comparable<T>> less(v: T, w: T) = v < w

    // is the array sorted from a[lo] to a[hi]
    private fun <T:Comparable<T>> isSorted(a: Array<T>, lo: Int = 0, hi: Int = a.size - 1): Boolean {
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
     * Reads in a sequence of strings from standard input; insertion sorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = StdIn.readAllStrings()
        BinaryInsertion.sort(a)
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
