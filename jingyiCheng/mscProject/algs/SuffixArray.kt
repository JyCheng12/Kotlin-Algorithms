/******************************************************************************
 * Compilation:  javac SuffixArray.java
 * Execution:    java SuffixArray < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/63suffix/abra.txt
 *
 * A data type that computes the suffix array of a string.
 *
 * % java SuffixArray < abra.txt
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
 * See SuffixArrayX.kt for an optimized version that uses 3-way
 * radix quicksort and does not use the nested class Suffix.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `SuffixArray` class represents a suffix array of a string of
 * length *n*.
 * It supports the *selecting* the *i*th smallest suffix,
 * getting the *index* of the *i*th smallest suffix,
 * computing the length of the *longest common prefix* between the
 * *i*th smallest suffix and the *i*-1st smallest suffix,
 * and determining the *rank* of a query string (which is the number
 * of suffixes strictly less than the query string).
 *
 *
 * This implementation uses a nested class `Suffix` to represent
 * a suffix of a string (using constant time and space) and
 * `Arrays.sort()` to sort the array of suffixes.
 * The *index* and *length* operations takes constant time
 * in the worst case. The *lcp* operation takes time proportional to the
 * length of the longest common prefix.
 * The *select* operation takes time proportional
 * to the length of the suffix and should be used primarily for debugging.
 *
 *
 * For alternate implementations of the same API, see
 * [SuffixArrayX], which is faster in practice (uses 3-way radix quicksort)
 * and uses less memory (does not create `Suffix` objects)
 * and [SuffixArrayJava6.java](https://algs4.cs.princeton.edu/63suffix/SuffixArrayJava6.java.html),
 * which relies on the constant-time substring extraction method that existed
 * in Java 6.
 *
 *
 * For additional documentation, see [Section 6.3](https://algs4.cs.princeton.edu/63suffix) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Jingyi Cheng
 *
 */
class SuffixArray
/**
 * Initializes a suffix array for the given `text` string.
 * @param text the input string
 */
(text: String) {
    private val suffixes: Array<Suffix> = Array(text.length) { Suffix(text, it) }

    init {
        suffixes.sort()
    }

    internal class Suffix
    constructor(val text: String, val index: Int) : Comparable<Suffix> {
        fun length() = text.length - index

        fun charAt(i: Int) = text[index + i]

        override fun compareTo(other: Suffix): Int {
            if (this === other) return 0  // optimization
            val n = Math.min(this.length(), other.length())
            for (i in 0 until n) {
                if (this.charAt(i) < other.charAt(i)) return -1
                if (this.charAt(i) > other.charAt(i)) return +1
            }
            return this.length() - other.length()
        }

        override fun toString() = text.substring(index)
    }

    /**
     * Returns the length of the input string.
     * @return the length of the input string
     */
    var length: Int = text.length
        get() = suffixes.size

    /**
     * Returns the index into the original string of the *i*th smallest suffix.
     * That is, `text.substring(sa.index(i))` is the *i*th smallest suffix.
     * @param i an integer between 0 and *n*-1
     * @return the index into the original string of the *i*th smallest suffix
     * @throws IllegalArgumentException unless `0 <= i < n`
     */
    fun index(i: Int): Int {
        if (i < 0 || i >= suffixes.size) throw IllegalArgumentException()
        return suffixes[i].index
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
        if (i < 1 || i >= suffixes.size) throw IllegalArgumentException()
        return lcpSuffix(suffixes[i], suffixes[i - 1])
    }

    /**
     * Returns the *i*th smallest suffix as a string.
     * @param i the index
     * @return the *i* smallest suffix as a string
     * @throws IllegalArgumentException unless `0 <= i < n`
     */
    fun select(i: Int): String {
        if (i < 0 || i >= suffixes.size) throw IllegalArgumentException()
        return suffixes[i].toString()
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
        var hi = suffixes.size - 1
        while (lo <= hi) {
            val mid = lo + (hi - lo) / 2
            when {
                compare(query, suffixes[mid]) < 0 -> hi = mid - 1
                compare(query, suffixes[mid]) > 0 -> lo = mid + 1
                else -> return mid
            }
        }
        return lo
    }

    companion object {

        // longest common prefix of s and t
        private fun lcpSuffix(s: Suffix, t: Suffix): Int {
            val n = Math.min(s.length(), t.length())
            for (i in 0 until n) {
                if (s.charAt(i) != t.charAt(i)) return i
            }
            return n
        }

        // compare query string to suffix
        private fun compare(query: String, suffix: Suffix): Int {
            val n = Math.min(query.length, suffix.length())
            for (i in 0 until n) {
                if (query[i] < suffix.charAt(i)) return -1
                if (query[i] > suffix.charAt(i)) return +1
            }
            return query.length - suffix.length()
        }

        /**
         * Unit tests the `SuffixArray` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val s = StdIn.readAll().replace("\\s+", " ").trim()
            val suffix = SuffixArray(s)

            StdOut.println("  i ind lcp rnk select")
            StdOut.println("---------------------------")

            for (i in 0 until s.length) {
                val index = suffix.index(i)
                val ith = "\"" + s.substring(index, Math.min(index + 50, s.length)) + "\""
                assert(s.substring(index) == suffix.select(i))
                val rank = suffix.rank(s.substring(index))
                if (i == 0) {
                    StdOut.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank, ith)
                } else {
                    val lcp = suffix.lcp(i)
                    StdOut.printf("%3d %3d %3d %3d %s\n", i, index, lcp, rank, ith)
                }
            }
        }
    }
}

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
