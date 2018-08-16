/******************************************************************************
 * Compilation:  javac GREP.java
 * Execution:    java GREP regexp < input.txt
 * Dependencies: NFA.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/54regexp/tinyL.txt
 *
 * This program takes an RE as a command-line argument and prints
 * the lines from standard input having some substring that
 * is in the language described by the RE.
 *
 * % more tinyL.txt
 * AC
 * AD
 * AAA
 * ABD
 * ADD
 * BCD
 * ABCCBD
 * BABAAA
 * BABBAAA
 *
 * %  java GREP "(A*B|AC)D" < tinyL.txt
 * ABD
 * ABCCBD
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `GREP` class provides a client for reading in a sequence of
 * lines from standard input and printing to standard output those lines
 * that contain a substring matching a specified regular expression.
 *
 *
 * For additional documentation, see [Section 3.1](https://algs4.cs.princeton.edu/31elementary) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object GREP {

    /**
     * Interprets the command-line argument as a regular expression
     * (supporting closure, binary or, parentheses, and wildcard)
     * reads in lines from standard input; writes to standard output
     * those lines that contain a substring matching the regular
     * expression.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val regexp = "(.*${args[0]}.*)"
        val nfa = NFA(regexp)
        while (StdIn.hasNextLine()) {
            val line = StdIn.readLine()!!
            if (nfa.recognizes(line))
                StdOut.println(line)
        }
    }
}// do not instantiate

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