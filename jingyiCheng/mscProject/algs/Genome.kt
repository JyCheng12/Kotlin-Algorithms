/******************************************************************************
 * Compilation:  javac Genome.java
 * Execution:    java Genome - < input.txt   (compress)
 * Execution:    java Genome + < input.txt   (expand)
 * Dependencies: BinaryIn.kt BinaryOut.kt
 * Data files:   https://algs4.cs.princeton.edu/55compression/genomeTiny.txt
 *
 * Compress or expand a genomic sequence using a 2-bit code.
 *
 * % more genomeTiny.txt
 * ATAGATGCATAGCGCATAGCTAGATGTGCTAGC
 *
 * % java Genome - < genomeTiny.txt | java Genome +
 * ATAGATGCATAGCGCATAGCTAGATGTGCTAGC
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Genome` class provides static methods for compressing
 * and expanding a genomic sequence using a 2-bit code.
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
object Genome {
    /**
     * Reads a sequence of 8-bit extended ASCII characters over the alphabet
     * { A, C, T, G } from standard input; compresses them using two bits per
     * character; and writes the results to standard output.
     */
    fun compress() {
        val DNA = Alphabet.DNA
        val s = BinaryStdIn.readString()
        val n = s.length
        BinaryStdOut.write(n)

        // Write two-bit code for char.
        for (i in 0 until n) {
            val d = DNA.toIndex(s[i])
            BinaryStdOut.write(d, 2)
        }
        BinaryStdOut.close()
    }

    /**
     * Reads a binary sequence from standard input; converts each two bits
     * to an 8-bit extended ASCII character over the alphabet { A, C, T, G };
     * and writes the results to standard output.
     */
    fun expand() {
        val DNA = Alphabet.DNA
        val n = BinaryStdIn.readInt()
        // Read two bits; write char.
        for (i in 0 until n) {
            val c = BinaryStdIn.readChar(2)
            BinaryStdOut.write(DNA.toChar(c.toInt()), 8)
        }
        BinaryStdOut.close()
    }

    /**
     * Sample client that calls `compress()` if the command-line
     * argument is "-" an `expand()` if it is "+".
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        when (args[0]) {
            "-" -> compress()
            "+" -> expand()
            else -> throw IllegalArgumentException("Illegal command line argument")
        }
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