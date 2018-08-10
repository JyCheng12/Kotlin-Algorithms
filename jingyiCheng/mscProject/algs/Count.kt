/******************************************************************************
 * Compilation:  javac Count.java
 * Execution:    java Count alpha < input.txt
 * Dependencies: Alphabet.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/50strings/abra.txt
 * https://algs4.cs.princeton.edu/50strings/pi.txt
 *
 * Create an alphabet specified on the command line, read in a
 * sequence of characters over that alphabet (ignoring characters
 * not in the alphabet), computes the frequency of occurrence of
 * each character, and print out the results.
 *
 * %  java Count ABCDR < abra.txt
 * A 5
 * B 2
 * C 1
 * D 1
 * R 2
 *
 * % java Count 0123456789 < pi.txt
 * 0 9999
 * 1 10137
 * 2 9908
 * 3 10026
 * 4 9971
 * 5 10026
 * 6 10028
 * 7 10025
 * 8 9978
 * 9 9902
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Count` class provides an [Alphabet] client for reading
 * in a piece of text and computing the frequency of occurrence of each
 * character over a given alphabet.
 *
 *
 * For additional documentation,
 * see [Section 5.5](https://algs4.cs.princeton.edu/55compress) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object Count {
    /**
     * Reads in text from standard input; calculates the frequency of
     * occurrence of each character over the alphabet specified as a
     * commmand-line argument; and prints the frequencies to standard
     * output.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val alphabet = Alphabet(args[0])
        val R = alphabet.R
        val count = IntArray(R)
        while (StdIn.hasNextChar()) {
            val c = StdIn.readChar()
            if (alphabet.contains(c))
                count[alphabet.toIndex(c)]++
        }
        for (c in 0 until R)
            StdOut.println("${alphabet.toChar(c)} ${count[c]}")
    }
}// Do not instantiate.


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
