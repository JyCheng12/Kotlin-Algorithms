/******************************************************************************
 * Compilation:  javac Heap.java
 * Execution:    java Heap < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/24pq/tiny.txt
 * https://algs4.cs.princeton.edu/24pq/words3.txt
 *
 * Sorts a sequence of strings from standard input using heapsort.
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java Heap < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java Heap < words3.txt
 * all bad bed bug dad ... yes yet zoo   [ one string per line ]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Heap` class provides a static methods for heapsorting
 * an array.
 *
 *
 * For additional documentation, see [Section 2.4](https://algs4.cs.princeton.edu/24pq) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object Heap {
    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param pq the array to be sorted
     */
    fun <T:Comparable<T>> sort(pq: Array<T>) {
        var n = pq.size
        for (k in n / 2 downTo 1)
            sink(pq, k, n)
        while (n > 1) {
            exch(pq, 1, n--)
            sink(pq, 1, n)
        }
    }

    /***************************************************************************
     * Helper functions to restore the heap invariant.
     */

    private fun <T:Comparable<T>> sink(pq: Array<T>, k: Int, n: Int) {
        var k = k
        while (2 * k <= n) {
            var j = 2 * k
            if (j < n && less(pq, j, j + 1)) j++
            if (!less(pq, k, j)) break
            exch(pq, k, j)
            k = j
        }
    }

    private fun <T:Comparable<T>> less(pq: Array<T>, i: Int, j: Int) = pq[i - 1] < pq[j - 1]

    private fun <T> exch(pq: Array<T>, i: Int, j: Int) {
        val swap = pq[i - 1]
        pq[i - 1] = pq[j - 1]
        pq[j - 1] = swap
    }

    // print array to standard output
    private fun <T> show(a: Array<T>) {
        for (i in a) StdOut.println(i)
    }

    /**
     * Reads in a sequence of strings from standard input; heapsorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = arrayOf("S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E")
        Heap.sort(a)
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
