/******************************************************************************
 * Compilation:  javac Merge.java
 * Execution:    java Merge < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/22mergesort/tiny.txt
 * https://algs4.cs.princeton.edu/22mergesort/words3.txt
 *
 * Sorts a sequence of strings from standard input using mergesort.
 *
 * % more tiny.txt
 * S O R T E X A M P L E
 *
 * % java Merge < tiny.txt
 * A E E L M O P R S T X                 [ one string per line ]
 *
 * % more words3.txt
 * bed bug dad yes zoo ... all bad yet
 *
 * % java Merge < words3.txt
 * all bad bed bug dad ... yes yet zoo    [ one string per line ]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Merge` class provides static methods for sorting an
 * array using mergesort.
 *
 *
 * For additional documentation, see [Section 2.2](https://algs4.cs.princeton.edu/22mergesort) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 * For an optimized version, see [MergeX].
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object Merge {
    // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
    private fun <T : Comparable<T>> merge(a: Array<T>, aux: Array<T>, lo: Int, mid: Int, hi: Int) {
        // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
        //assert(isSorted(a, lo, mid))
        //assert(isSorted(a, mid + 1, hi))

        // copy to aux[]
        for (k in lo..hi)
            aux[k] = a[k]

        // merge back to a[]
        var i = lo
        var j = mid + 1
        for (k in lo..hi)
            when {
                i > mid -> a[k] = aux[j++]
                j > hi -> a[k] = aux[i++]
                less(aux[j], aux[i]) -> a[k] = aux[j++]
                else -> a[k] = aux[i++]
            }
        // postcondition: a[lo .. hi] is sorted
        //assert(isSorted(a, lo, hi))
    }

    // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
    private fun <T : Comparable<T>> sort(a: Array<T>, aux: Array<T>, lo: Int, hi: Int) {
        if (hi <= lo) return
        val mid = lo + (hi - lo) / 2
        sort(a, aux, lo, mid)
        sort(a, aux, mid + 1, hi)
        this.merge(a, aux, lo, mid, hi)
    }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    fun <T : Comparable<T>> sort(a: Array<T>) {
        val aux = Array<Comparable<T>>(a.size) { a[0] } as Array<T>
        sort(a, aux, 0, a.size - 1)
        assert(isSorted(a))
    }

    /***************************************************************************
     * Helper sorting function.
     */

    // is v < w ?
    private fun <T : Comparable<T>> less(v: T, w: T) = v < w

    private fun <T : Comparable<T>> isSorted(a: Array<T>, lo: Int = 0, hi: Int = a.size - 1): Boolean {
        for (i in lo + 1..hi)
            if (less(a[i], a[i - 1])) return false
        return true
    }

    /***************************************************************************
     * Index mergesort.
     */
    // stably merge a[lo .. mid] with a[mid+1 .. hi] using aux[lo .. hi]
    private fun <T : Comparable<T>> merge(a: Array<T>, index: IntArray, aux: IntArray, lo: Int, mid: Int, hi: Int) {
        // copy to aux[]
        for (k in lo..hi)
            aux[k] = index[k]

        // merge back to a[]
        var i = lo
        var j = mid + 1
        for (k in lo..hi)
            when {
                i > mid -> index[k] = aux[j++]
                j > hi -> index[k] = aux[i++]
                less(a[aux[j]], a[aux[i]]) -> index[k] = aux[j++]
                else -> index[k] = aux[i++]
            }
    }

    /**
     * Returns a permutation that gives the elements in the array in ascending order.
     * @param a the array
     * @return a permutation `p[]` such that `a[p[0]]`, `a[p[1]]`,
     * ..., `a[p[N-1]]` are in ascending order
     */
    fun <T : Comparable<T>> indexSort(a: Array<T>): IntArray {
        val n = a.size
        val index = IntArray(n) { it }

        val aux = IntArray(n)
        sort(a, index, aux, 0, n - 1)
        return index
    }

    // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
    private fun <T : Comparable<T>> sort(a: Array<T>, index: IntArray, aux: IntArray, lo: Int, hi: Int) {
        if (hi <= lo) return
        val mid = lo + (hi - lo) / 2
        sort(a, index, aux, lo, mid)
        sort(a, index, aux, mid + 1, hi)
        merge(a, index, aux, lo, mid, hi)
    }

    // print array to standard output
    private fun <T> show(a: Array<T>) {
        for (i in a)
            StdOut.println()
    }

    /**
     * Reads in a sequence of strings from standard input; mergesorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = arrayOf("S", "O", "R", "T", "E", "X", "A", "M", "P", "L", "E")
        Merge.sort(a)
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