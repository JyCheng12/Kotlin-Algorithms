/******************************************************************************
 * Compilation:  javac DoublingTest.java
 * Execution:    java DoublingTest
 * Dependencies: ThreeSum.kt Stopwatch.tk StdRandom.kt StdOut.kt
 *
 * % java DoublingTest
 * 250     0.0
 * 500     0.0
 * 1000     0.1
 * 2000     0.6
 * 4000     4.5
 * 8000    35.7
 * ...
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DoublingTest` class provides a client for measuring
 * the running time of a method using a doubling test.
 *
 *
 * For additional documentation, see [Section 1.4](https://algs4.cs.princeton.edu/14analysis)
 * of *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object DoublingTest {
    private const val MAXIMUM_INTEGER = 1000000

    /**
     * Returns the amount of time to call `ThreeSum.count()` with *n*
     * random 6-digit integers.
     * @param n the number of integers
     * @return amount of time (in seconds) to call `ThreeSum.count()`
     * with *n* random 6-digit integers
     */
    fun timeTrial(n: Int): Double {
        val a = IntArray(n) { StdRandom.uniform(-MAXIMUM_INTEGER, MAXIMUM_INTEGER) }
        val timer = Stopwatch()
        ThreeSum.count(a)
        return timer.elapsedTime()
    }

    /**
     * Prints table of running times to call `ThreeSum.count()`
     * for arrays of size 250, 500, 1000, 2000, and so forth.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        var n = 250
        while (true) {
            val time = timeTrial(n)
            StdOut.printf("%7d %7.1f\n", n, time)
            n += n
        }
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
