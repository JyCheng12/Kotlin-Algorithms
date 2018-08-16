/******************************************************************************
 * Compilation:  javac ThreeSum.java
 * Execution:    java ThreeSum input.txt
 * Dependencies: In.kt StdOut.kt Stopwatch.kt
 * Data files:   https://algs4.cs.princeton.edu/14analysis/1Kints.txt
 * https://algs4.cs.princeton.edu/14analysis/2Kints.txt
 * https://algs4.cs.princeton.edu/14analysis/4Kints.txt
 * https://algs4.cs.princeton.edu/14analysis/8Kints.txt
 * https://algs4.cs.princeton.edu/14analysis/16Kints.txt
 * https://algs4.cs.princeton.edu/14analysis/32Kints.txt
 * https://algs4.cs.princeton.edu/14analysis/1Mints.txt
 *
 * A program with cubic running time. Reads n integers
 * and counts the number of triples that sum to exactly 0
 * (ignoring integer overflow).
 *
 * % java ThreeSum 1Kints.txt
 * 70
 *
 * % java ThreeSum 2Kints.txt
 * 528
 *
 * % java ThreeSum 4Kints.txt
 * 4039
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `ThreeSum` class provides static methods for counting
 * and printing the number of triples in an array of integers that sum to 0
 * (ignoring integer overflow).
 *
 *
 * This implementation uses a triply nested loop and takes proportional to n^3,
 * where n is the number of integers.
 *
 *
 * For additional documentation, see [Section 1.4](https://algs4.cs.princeton.edu/14analysis) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object ThreeSum {
    /**
     * Prints to standard output the (i, j, k) with `i < j < k`
     * such that `a[i] + a[j] + a[k] == 0`.
     *
     * @param a the array of integers
     */
    fun printAll(a: IntArray) {
        val n = a.size
        for (i in 0 until n)
            for (j in i + 1 until n)
                for (k in j + 1 until n)
                    if (a[i] + a[j] + a[k] == 0)
                        StdOut.println("${a[i]} ${a[j]} ${a[k]}")
    }

    /**
     * Returns the number of triples (i, j, k) with `i < j < k`
     * such that `a[i] + a[j] + a[k] == 0`.
     *
     * @param  a the array of integers
     * @return the number of triples (i, j, k) with `i < j < k`
     * such that `a[i] + a[j] + a[k] == 0`
     */
    fun count(a: IntArray): Int {
        val n = a.size
        var count = 0
        for (i in 0 until n)
            for (j in i + 1 until n)
                for (k in j + 1 until n)
                    if (a[i] + a[j] + a[k] == 0)
                        count++
        return count
    }

    /**
     * Reads in a sequence of integers from a file, specified as a command-line argument;
     * counts the number of triples sum to exactly zero; prints out the time to perform
     * the computation.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val `in` = In(args[0])
        val a = `in`.readAllInts()

        val timer = Stopwatch()
        val count = count(a)
        StdOut.println("elapsed time = " + timer.elapsedTime())
        StdOut.println(count)
    }
}// Do not instantiate.

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