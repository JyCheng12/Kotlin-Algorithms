/******************************************************************************
 * Compilation:  javac Accumulator.java
 * Execution:    java Accumulator < input.txt
 * Dependencies: StdOut.kt StdIn.kt
 *
 * Mutable data type that calculates the mean, sample standard
 * deviation, and sample variance of a stream of real numbers
 * use a stable, one-pass algorithm.
 *
 */

package jingyiCheng.mscProject.algs


/**
 * The `Accumulator` class is a data type for computing the running
 * mean, sample standard deviation, and sample variance of a stream of real
 * numbers. It provides an example of a mutable data type and a streaming
 * algorithm.
 *
 *
 * This implementation uses a one-pass algorithm that is less susceptible
 * to floating-point roundoff error than the more straightforward
 * implementation based on saving the sum of the squares of the numbers.
 * This technique is due to
 * [B. P. Welford](https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#Online_algorithm).
 * Each operation takes constant time in the worst case.
 * The amount of memory is constant - the data values are not stored.
 *
 *
 * For additional documentation,
 * see [Section 1.2](https://algs4.cs.princeton.edu/12oop) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 */
/**
 * Initializes an accumulator.
 */
class Accumulator {
    var count = 0          // number of data values
        private set
    var sum = 0.0   // sample variance * (count-1)
        private set
    var mean = 0.0    // sample mean
        private set

    /**
     * Adds the specified data value to the accumulator.
     * @param  x the data value
     */
    fun addDataValue(x: Double) {
        count++
        val delta = x - mean
        mean += delta / count
        sum += (count - 1).toDouble() / count * delta * delta
    }

    /**
     * Returns the sample variance of the data values.
     * @return the sample variance of the data values
     */
    fun `var`(): Double {
        return if (count <= 1) Double.NaN else sum / (count - 1)
    }

    /**
     * Returns the sample standard deviation of the data values.
     * @return the sample standard deviation of the data values
     */
    fun stddev(): Double {
        return Math.sqrt(this.`var`())
    }

    companion object {

        /**
         * Unit tests the `Accumulator` data type.
         * Reads in a stream of real number from standard input;
         * adds them to the accumulator; and prints the mean,
         * sample standard deviation, and sample variance to standard
         * output.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val stats = Accumulator()
            while (!StdIn.isEmpty) {
                val x = StdIn.readDouble()
                stats.addDataValue(x)
            }

            StdOut.printf("count  = %d\n", stats.count)
            StdOut.printf("mean   = %.5f\n", stats.mean)
            StdOut.printf("stddev = %.5f\n", stats.stddev())
            StdOut.printf("var    = %.5f\n", stats.`var`())
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