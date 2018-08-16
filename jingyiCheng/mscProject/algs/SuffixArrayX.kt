/******************************************************************************
 * Compilation:  javac SuffixArrayX.java
 * Execution:    java SuffixArrayX < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/63suffix/abra.txt
 *
 * A data type that computes the suffix array of a string using 3-way
 * radix quicksort.
 *
 * % java SuffixArrayX < abra.txt
 * i ind lcp rnk    select
 * ---------------------------
 * 0  11   -   0    "!"
 * 1  10   0   1    "A!"
 * 2   7   1   2    "ABRA!"
 * 3   0   4   3    "ABRACADABRA!"
 * 4   3   1   4    "ACADABRA!"
 * 5   5   1   5    "ADABRA!"
 * 6   8   0   6    "BRA!"
 * 7   1   3   7    "BRACADABRA!"
 * 8   4   0   8    "CADABRA!"
 * 9   6   0   9    "DABRA!"
 * 10  9   0  10    "RA!"
 * 11  2   2  11    "RACADABRA!"
 *
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `SuffixArrayX` class represents a suffix array of a string of
 * length *n*.
 * It supports the *selecting* the *i*th smallest suffix,
 * getting the *index* of the *i*th smallest suffix,
 * computing the length of the *longest common prefix* between the
 * *i*th smallest suffix and the *i*-1st smallest suffix,
 * and determining the *rank* of a query string (which is the number
 * of suffixes strictly less than the query string).
 *
 *
 * This implementation uses 3-way radix quicksort to sort the array of suffixes.
 * For a simpler (but less efficient) implementations of the same API, see
 * [SuffixArray].
 * The *index* and *length* operations takes constant time
 * in the worst case. The *lcp* operation takes time proportional to the
 * length of the longest common prefix.
 * The *select* operation takes time proportional
 * to the length of the suffix and should be used primarily for debugging.
 *
 *
 * This implementation uses '\0' as a sentinel and assumes that the charater
 * '\0' does not appear in the text.
 *
 *
 * In practice, this algorithm runs very fast. However, in the worst-case
 * it can be very poor (e.g., a string consisting of N copies of the same
 * character. We do not shuffle the array of suffixes before sorting because
 * shuffling is relatively expensive and a pathologial input for which
 * the suffixes start out in a bad order (e.g., sorted) is likely to be
 * a bad input for this algorithm with or without the shuffle.
 *
 *
 * For additional documentation, see [Section 6.3](https://algs4.cs.princeton.edu/63suffix) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Jingyi Cheng
 *
 */
class SuffixArrayX
/**
 * Initializes a suffix array for the given `text` string.
 * @param text the input string
 */
(text: String) {
    private val text: CharArray = (text + '\u0000').toCharArray()
    val length: Int = text.length        // number of characters in text

    private val index: IntArray = IntArray(text.length) { it }   // index[i] = j means text.substring(j) is ith largest suffix


    init {
        sort(0, length - 1, 0)
    }

    // 3-way string quicksort lo..hi starting at dth character
    private fun sort(lo: Int, hi: Int, d: Int) {
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d)
            return
        }

        var lt = lo
        var gt = hi
        val v = text[index[lo] + d]
        var i = lo + 1
        while (i <= gt) {
            val t = text[index[i] + d]
            when {
                t < v -> exch(lt++, i++)
                t > v -> exch(i, gt--)
                else -> i++
            }
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(lo, lt - 1, d)
        if (v.toInt() > 0) sort(lt, gt, d + 1)
        sort(gt + 1, hi, d)
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private fun insertion(lo: Int, hi: Int, d: Int) {
        for (i in lo..hi) {
            var j = i
            while (j > lo && less(index[j], index[j - 1], d)) {
                exch(j, j - 1)
                j--
            }
        }
    }

    // is text[i+d..n) < text[j+d..n) ?
    private fun less(i: Int, j: Int, d: Int): Boolean {
        var i = i
        var j = j
        if (i == j) return false
        i += d
        j += d
        while (i < length && j < length) {
            if (text[i] < text[j]) return true
            if (text[i] > text[j]) return false
            i++
            j++
        }
        return i > j
    }

    // exchange index[i] and index[j]
    private fun exch(i: Int, j: Int) {
        val swap = index[i]
        index[i] = index[j]
        index[j] = swap
    }

    /**
     * Returns the index into the original string of the *i*th smallest suffix.
     * That is, `text.substring(sa.index(i))` is the *i* smallest suffix.
     * @param i an integer between 0 and *n*-1
     * @return the index into the original string of the *i*th smallest suffix
     * @throws IllegalArgumentException unless `0 <=i < n`
     */
    fun index(i: Int): Int {
        if (i < 0 || i >= length) throw IllegalArgumentException()
        return index[i]
    }

    /**
     * Returns the length of the longest common prefix of the *i*th
     * smallest suffix and the *i*-1st smallest suffix.
     * @param i an integer between 1 and *n*-1
     * @return the length of the longest common prefix of the *i*th
     * smallest suffix and the *i*-1st smallest suffix.
     * @throws IllegalArgumentException unless `1 <= i < n`
     */
    fun lcp(i: Int): Int {
        if (i < 1 || i >= length) throw IllegalArgumentException()
        return lcp(index[i], index[i - 1])
    }

    // longest common prefix of text[i..n) and text[j..n)
    private fun lcp(i: Int, j: Int): Int {
        var i = i
        var j = j
        var length = 0
        while (i < length && j < length) {
            if (text[i] != text[j]) return length
            i++
            j++
            length++
        }
        return length
    }

    /**
     * Returns the *i*th smallest suffix as a string.
     * @param i the index
     * @return the *i* smallest suffix as a string
     * @throws IllegalArgumentException unless `0 <= i < n`
     */
    fun select(i: Int): String {
        if (i < 0 || i >= length) throw IllegalArgumentException()
        return String(text, index[i], length - index[i])
    }

    /**
     * Returns the number of suffixes strictly less than the `query` string.
     * We note that `rank(select(i))` equals `i` for each `i`
     * between 0 and *n*-1.
     * @param query the query string
     * @return the number of suffixes strictly less than `query`
     */
    fun rank(query: String): Int {
        var lo = 0
        var hi = length - 1
        while (lo <= hi) {
            val mid = lo + (hi - lo) / 2
            when {
                compare(query, index[mid]) < 0 -> hi = mid - 1
                compare(query, index[mid]) > 0 -> lo = mid + 1
                else -> return mid
            }
        }
        return lo
    }

    // is query < text[i..n) ?
    private fun compare(query: String, i: Int): Int {
        var i = i
        val m = query.length
        var j = 0
        while (i < length && j < m) {
            if (query[j] != text[i]) return query[j] - text[i]
            i++
            j++
        }
        if (i < length) return -1
        return if (j < m) +1 else 0
    }

    companion object {
        private const val CUTOFF = 5   // cutoff to insertion sort (any value between 0 and 12)

        /**
         * Unit tests the `SuffixArrayx` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val s = StdIn.readAll().replace("\n", " ").trim()
            val suffix1 = SuffixArrayX(s)
            val suffix2 = SuffixArray(s)
            var check = true
            run {
                var i = 0
                while (check && i < s.length) {
                    if (suffix1.index(i) != suffix2.index(i)) {
                        StdOut.println("suffix1($i) = ${suffix1.index(i)}")
                        StdOut.println("suffix2($i) = ${suffix2.index(i)}")
                        val ith = "\"" + s.substring(suffix1.index(i), Math.min(suffix1.index(i) + 50, s.length)) + "\""
                        val jth = "\"" + s.substring(suffix2.index(i), Math.min(suffix2.index(i) + 50, s.length)) + "\""
                        StdOut.println(ith)
                        StdOut.println(jth)
                        check = false
                    }
                    i++
                }
            }

            StdOut.println("  i ind lcp rnk  select")
            StdOut.println("---------------------------")

            for (i in 0 until s.length) {
                val index = suffix2.index(i)
                val ith = "\"" + s.substring(index, Math.min(index + 50, s.length)) + "\""
                val rank = suffix2.rank(s.substring(index))
                assert(s.substring(index) == suffix2.select(i))
                if (i == 0)
                    StdOut.printf("%3d %3d %3s %3d  %s\n", i, index, "-", rank, ith)
                else {
                    val lcp = suffix2.lcp(i)
                    StdOut.printf("%3d %3d %3d %3d  %s\n", i, index, lcp, rank, ith)
                }
            }
        }
    }
}

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