/******************************************************************************
 * Compilation:  javac Cat.java
 * Execution:    java Cat input0.txt input1.txt ... output.txt
 * Dependencies: In.kt Out.kt
 * Data files:   https://algs4.cs.princeton.edu/11model/in1.txt
 * https://algs4.cs.princeton.edu/11model/in2.txt
 *
 * Reads in text files specified as the first command-line
 * arguments, concatenates them, and writes the result to
 * filename specified as the last command-line arguments.
 *
 * % more in1.txt
 * This is
 *
 * % more in2.txt
 * a tiny
 * test.
 *
 * % java Cat in1.txt in2.txt out.txt
 *
 * % more out.txt
 * This is
 * a tiny
 * test.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Cat` class provides a client for concatenating the results
 * of several text files.
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
object Cat {

    /**
     * Reads in a sequence of text files specified as the first command-line
     * arguments, concatenates them, and writes the results to the file
     * specified as the last command-line argument.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val out = Out(args[args.size - 1])
        for (i in 0 until args.size - 1) {
            val `in` = In(args[i])
            val s = `in`.readAll()
            out.println(s)
            `in`.close()
        }
        out.close()
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