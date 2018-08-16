/******************************************************************************
 * Compilation:  javac RandomSeq.java
 * Execution:    java RandomSeq n lo hi
 * Dependencies: StdOut.kt
 *
 * Prints N numbers between lo and hi.
 *
 * % java RandomSeq 5 100.0 200.0
 * 123.43
 * 153.13
 * 144.38
 * 155.18
 * 104.02
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `RandomSeq` class is a client that prints out a pseudo-random
 * sequence of real numbers in a given range.
 *
 * For additional documentation, see [Section 1.1](https://algs4.cs.princeton.edu/11model) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object RandomSeq {
    /**
     * Reads in two command-line arguments lo and hi and prints n uniformly
     * random real numbers in [lo, hi) to standard output.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        // command-line arguments
        val n = Integer.parseInt(args[0])

        // for backward compatibility with Intro to Programming in Java version of RandomSeq
        when {
            args.size == 1 -> // generate and print n numbers between 0.0 and 1.0
                for (i in 0 until n) {
                    val x = StdRandom.uniform()
                    StdOut.println(x)
                }
            args.size == 3 -> {
                val lo = args[1].toDouble()
                val hi = args[2].toDouble()

                // generate and print n numbers between lo and hi
                for (i in 0 until n) {
                    val x = StdRandom.uniform(lo, hi)
                    StdOut.printf("%.2f\n", x)
                }
            }
            else -> throw IllegalArgumentException("Invalid number of arguments")
        }
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