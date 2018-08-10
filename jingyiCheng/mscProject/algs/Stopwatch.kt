/******************************************************************************
 * Compilation:  javac Stopwatch.java
 * Execution:    java Stopwatch n
 * Dependencies: none
 *
 * A utility class to measure the running time (wall clock) of a program.
 *
 * % java8 Stopwatch 100000000
 * 6.666667e+11  0.5820 seconds
 * 6.666667e+11  8.4530 seconds
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Stopwatch` data type is for measuring
 * the time that elapses between the start and end of a
 * programming task (wall-clock time).
 *
 * See [StopwatchCPU] for a version that measures CPU time.
 * For additional documentation,
 * see [Section 1.4](https://algs4.cs.princeton.edu/14analysis) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */


class Stopwatch {
    private val start: Long = System.currentTimeMillis()

    /**
     * Returns the elapsed CPU time (in seconds) since the stopwatch was created.
     *
     * @return elapsed CPU time (in seconds) since the stopwatch was created
     */
    fun elapsedTime(): Double {
        val now = System.currentTimeMillis()
        return (now - start) / 1000.0
    }

    companion object {
        /**
         * Unit tests the `Stopwatch` data type.
         * Takes a command-line argument `n` and computes the
         * sum of the square roots of the first `n` positive integers,
         * first using `Math.sqrt()`, then using `Math.pow()`.
         * It prints to standard output the sum and the amount of time to
         * compute the sum. Note that the discrete sum can be approximated by
         * an integral - the sum should be approximately 2/3 * (n^(3/2) - 1).
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val n = Integer.parseInt(args[0])

            // sum of square roots of integers from 1 to n using Math.sqrt(x).
            val timer1 = Stopwatch()
            var sum1 = 0.0
            for (i in 1..n)
                sum1 += Math.sqrt(i.toDouble())
            val time1 = timer1.elapsedTime()
            StdOut.printf("%e (%.2f seconds)\n", sum1, time1)

            // sum of square roots of integers from 1 to n using Math.pow(x, 0.5).
            val timer2 = Stopwatch()
            var sum2 = 0.0
            for (i in 1..n)
                sum2 += Math.pow(i.toDouble(), 0.5)
            val time2 = timer2.elapsedTime()
            StdOut.printf("%e (%.2f seconds)\n", sum2, time2)
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
