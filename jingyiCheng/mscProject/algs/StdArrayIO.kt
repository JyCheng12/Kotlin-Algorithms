/******************************************************************************
 *  Compilation:  javac StdArrayIO.java
 *  Execution:    java StdArrayIO < input.txt
 *  Dependencies: StdOut.kt
 *  Data files:    https://introcs.cs.princeton.edu/java/22library/tinyDouble1D.txt
 *                 https://introcs.cs.princeton.edu/java/22library/tinyDouble2D.txt
 *                 https://introcs.cs.princeton.edu/java/22library/tinyBoolean2D.txt
 *
 *  A library for reading in 1D and 2D arrays of integers, doubles,
 *  and booleans from standard input and printing them out to
 *  standard output.
 *
 *  % more tinyDouble1D.txt
 *  4
 *    .000  .246  .222  -.032
 *
 *  % more tinyDouble2D.txt
 *  4 3
 *    .000  .270  .000
 *    .246  .224 -.036
 *    .222  .176  .0893
 *   -.032  .739  .270
 *
 *  % more tinyBoolean2D.txt
 *  4 3
 *    1 1 0
 *    0 0 0
 *    0 1 1
 *    1 1 1
 *
 *  % cat tinyDouble1D.txt tinyDouble2D.txt tinyBoolean2D.txt | java StdArrayIO
 *  4
 *    0.00000   0.24600   0.22200  -0.03200
 *
 *  4 3
 *    0.00000   0.27000   0.00000
 *    0.24600   0.22400  -0.03600
 *    0.22200   0.17600   0.08930
 *    0.03200   0.73900   0.27000
 *
 *  4 3
 *  1 1 0
 *  0 0 0
 *  0 1 1
 *  1 1 1
 *
 ******************************************************************************/

package jingyiCheng.mscProject.algs


/**
 *  <i>Standard array IO</i>. This class provides methods for reading
 *  in 1D and 2D arrays from standard input and printing out to
 *  standard output.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://introcs.cs.princeton.edu/22libary">Section 2.2</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Jingyi Cheng
 *
 */
object StdArrayIO {

    /**
     * Reads a 1D array of doubles from standard input and returns it.
     *
     * @return the 1D array of doubles
     */
    fun readDouble1D(): DoubleArray {
        print("Please enter the number of doubles:")
        val n = StdIn.readInt()
        print("Please enter the array elements:")
        return DoubleArray(n) { StdIn.readDouble() }
    }

    /**
     * Prints an array of doubles to standard output.
     *
     * @param a the 1D array of doubles
     */
    fun print(a: DoubleArray) {
        for (i in a)
            StdOut.printf("\n%9.5f \n", i)
    }

    /**
     * Reads a 2D array of doubles from standard input and returns it.
     *
     * @return the 2D array of doubles
     */
    fun readDouble2D(): Array<DoubleArray> {
        print("please enter the number of rows:")
        val m = StdIn.readInt()
        print("Please enter the number of columns:")
        val n = StdIn.readInt()
        print("Please enter the array elements:")
        return Array(m) { DoubleArray(n) { StdIn.readDouble() } }
    }

    /**
     * Prints the 2D array of doubles to standard output.
     *
     * @param a the 2D array of doubles
     */
    fun print(a: Array<DoubleArray>) {
        val m = a.size
        val n = a[0].size
        StdOut.println("$m $n")
        for (i in 0 until m) {
            for (j in 0 until n)
                StdOut.printf("%9.5f ", a[i][j])
            StdOut.println()
        }
    }


    /**
     * Reads a 1D array of integers from standard input and returns it.
     *
     * @return the 1D array of integers
     */
    fun readInt1D(): IntArray {
        print("Please enter the number of integers:")
        val n = StdIn.readInt()
        print("Please enter the array elements:")
        return IntArray(n) { StdIn.readInt() }
    }

    /**
     * Prints an array of integers to standard output.
     *
     * @param a the 1D array of integers
     */
    fun print(a: IntArray) {
        for (i in a)
            StdOut.printf("\n%9d \n", i)
    }


    /**
     * Reads a 2D array of integers from standard input and returns it.
     *
     * @return the 2D array of integers
     */
    fun readInt2D(): Array<IntArray> {
        print("Please enter the number of rows:")
        val m = StdIn.readInt()
        print("Please enter the number of columns:")
        val n = StdIn.readInt()
        print("Please enter the array elements:")
        return Array(m) { IntArray(n) { StdIn.readInt() } }
    }

    /**
     * Print a 2D array of integers to standard output.
     *
     * @param a the 2D array of integers
     */
    fun print(a: Array<IntArray>) {
        val m = a.size
        val n = a[0].size
        StdOut.println("$m $n")
        for (i in 0 until m) {
            for (j in 0 until n)
                StdOut.printf("%9d ", a[i][j])
            StdOut.println()
        }
    }


    /**
     * Reads a 1D array of booleans from standard input and returns it.
     *
     * @return the 1D array of booleans
     */
    fun readBoolean1D(): BooleanArray {
        print("Please enter the number of booleans:")
        val n = StdIn.readInt()
        print("Please enter the array elements:")
        return BooleanArray(n) { StdIn.readBoolean() }
    }

    /**
     * Prints a 1D array of booleans to standard output.
     *
     * @param a the 1D array of booleans
     */
    fun print(a: BooleanArray) {
        StdOut.println(a.size)
        for (i in a)
            if (i) StdOut.print("1 ")
            else StdOut.print("0 ")
        StdOut.println()
    }

    /**
     * Reads a 2D array of booleans from standard input and returns it.
     *
     * @return the 2D array of booleans
     */
    fun readBoolean2D(): Array<BooleanArray> {
        print("Please enter the number of rows:")
        val m = StdIn.readInt()
        print("Please enter the number of columns:")
        val n = StdIn.readInt()
        print("Please enter the array elements:")
        return Array(m) { BooleanArray(n) { StdIn.readBoolean() } }
    }

    /**
     * Prints a 2D array of booleans to standard output.
     *
     * @param a the 2D array of booleans
     */
    fun print(a: Array<BooleanArray>) {
        val m = a.size
        val n = a[0].size
        StdOut.println("$m $n")
        for (i in 0 until m) {
            for (j in 0 until n)
                if (a[i][j]) StdOut.print("1 ")
                else StdOut.print("0 ")
            StdOut.println()
        }
    }

    /**
     * Unit tests `StdArrayIO`.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        // read and print an array of doubles
        val a = readDouble1D()
        print(a)
        StdOut.println("------------")

        // read and print a matrix of doubles
        val b = readDouble2D()
        print(b)
        StdOut.println("------------")

        // read and print a matrix of doubles
        val d = readBoolean2D()
        print(d)
        StdOut.println("------------")
    }
}// it doesn't make sense to instantiate this class

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