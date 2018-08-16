/******************************************************************************
 * Compilation:  javac Average.java
 * Execution:    java Average < data.txt
 * Dependencies: StdIn.kt StdOut.kt
 *
 * Reads in a sequence of real numbers, and computes their average.
 *
 * % java Average
 * 10.0 5.0 6.0
 * 3.0 7.0 32.0
 * [Ctrl-d]
 * Average is 10.5
 *
 * Note [Ctrl-d] signifies the end of file on Unix.
 * On windows use [Ctrl-z].
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Average` class provides a client for reading in a sequence
 * of real numbers and printing out their average.
 *
 *
 * For additional documentation, see [Section 1.1](https://algs4.cs.princeton.edu/11model) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object Average {

    /**
     * Reads in a sequence of real numbers from standard input and prints
     * out their average to standard output.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        var count = 0       // number input values
        var sum = 0.0    // sum of input values

        // read data and compute statistics
        while (!StdIn.isEmpty) {
            val value = StdIn.readDouble()
            sum += value
            count++
        }

        // compute the average
        val average = sum / count

        // print results
        StdOut.println("Average is $average")
    }
}// this class should not be instantiated

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