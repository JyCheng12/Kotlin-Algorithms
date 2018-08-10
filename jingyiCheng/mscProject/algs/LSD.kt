/******************************************************************************
 * Compilation:  javac LSD.java
 * Execution:    java LSD < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/51radix/words3.txt
 *
 * LSD radix sort
 *
 * - Sort a String[] array of n extended ASCII strings (R = 256), each of length w.
 *
 * - Sort an int[] array of n 32-bit integers, treating each integer as
 * a sequence of w = 4 bytes (R = 256).
 *
 * Uses extra space proportional to n + R.
 *
 *
 * % java LSD < words3.txt
 * all
 * bad
 * bed
 * bug
 * dad
 * ...
 * yes
 * yet
 * zoo
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LSD` class provides static methods for sorting an
 * array of *w*-character strings or 32-bit integers using LSD radix sort.
 *
 *
 * For additional documentation,
 * see [Section 5.1](https://algs4.cs.princeton.edu/51radix) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object LSD {
    private const val BITS_PER_BYTE = 8

    /**
     * Rearranges the array of W-character strings in ascending order.
     *
     * @param a the array to be sorted
     * @param w the number of characters per string
     */
    fun sort(a: Array<String>, w: Int) {
        val n = a.size
        val R = 256   // extend ASCII alphabet size
        val aux = Array(n){""}

        for (d in w - 1 downTo 0) {
            // sort by key-indexed counting on dth character

            // compute frequency counts
            val count = IntArray(R + 1)
            for (i in 0 until n)
                count[a[i][d].toInt() + 1]++

            // compute cumulates
            for (r in 0 until R)
                count[r + 1] += count[r]

            // move data
            for (i in 0 until n)
                aux[count[a[i][d].toInt()]++] = a[i]

            // copy back
            for (i in 0 until n)
                a[i] = aux[i]
        }
    }

    /**
     * Rearranges the array of 32-bit integers in ascending order.
     * This is about 2-3x faster than Arrays.sort().
     *
     * @param a the array to be sorted
     */
    fun sort(a: IntArray) {
        val BITS = 32                 // each int is 32 bits
        val R = 1 shl BITS_PER_BYTE    // each bytes is between 0 and 255
        val MASK = R - 1              // 0xFF
        val w = BITS / BITS_PER_BYTE  // each int is 4 bytes

        val n = a.size
        val aux = IntArray(n)

        for (d in 0 until w) {

            // compute frequency counts
            val count = IntArray(R + 1)
            for (i in 0 until n) {
                val c = a[i] shr BITS_PER_BYTE * d and MASK
                count[c + 1]++
            }

            // compute cumulates
            for (r in 0 until R)
                count[r + 1] += count[r]

            // for most significant byte, 0x80-0xFF comes before 0x00-0x7F
            if (d == w - 1) {
                val shift1 = count[R] - count[R / 2]
                val shift2 = count[R / 2]
                for (r in 0 until R / 2)
                    count[r] += shift1
                for (r in R / 2 until R)
                    count[r] -= shift2
            }

            // move data
            for (i in 0 until n) {
                val c = a[i] shr BITS_PER_BYTE * d and MASK
                aux[count[c]++] = a[i]
            }

            // copy back
            for (i in 0 until n)
                a[i] = aux[i]
        }
    }

    /**
     * Reads in a sequence of fixed-length strings from standard input;
     * LSD radix sorts them;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = StdIn.readAllStrings()
        val n = a.size

        // check that strings have fixed length
        val w = a[0].length
        for (i in 0 until n)
            assert(a[i].length == w) { "Strings must have fixed length" }

        // sort the strings
        sort(a, w)

        // print results
        for (i in 0 until n)
            StdOut.println(a[i])
    }
}// do not instantiate

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
