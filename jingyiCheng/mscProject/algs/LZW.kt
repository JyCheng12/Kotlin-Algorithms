/******************************************************************************
 * Compilation:  javac LZW.java
 * Execution:    java LZW - < input.txt   (compress)
 * Execution:    java LZW + < input.txt   (expand)
 * Dependencies: BinaryIn.kt BinaryOut.kt
 * Data files:   https://algs4.cs.princeton.edu/55compression/abraLZW.txt
 * https://algs4.cs.princeton.edu/55compression/ababLZW.txt
 *
 * Compress or expand binary input from standard input using LZW.
 *
 * WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 * METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 * SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 * IMPLEMENTATIONS).
 *
 * See [this article](http://java-performance.info/changes-to-string-java-1-7-0_06/)
 * for more details.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LZW` class provides static methods for compressing
 * and expanding a binary input using LZW compression over the 8-bit extended
 * ASCII alphabet with 12-bit codewords.
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
object LZW {
    private const val R = 256        // number of input chars
    private const val L = 4096       // number of codewords = 2^W
    private const val W = 12         // codeword width

    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses
     * them using LZW compression with 12-bit codewords; and writes the results
     * to standard output.
     */
    fun compress() {
        var input = BinaryStdIn.readString()
        val st = TST<Int>()
        for (i in 0 until R)
            st.put("" + i.toChar(), i)
        var code = R + 1  // R is codeword for EOF

        while (input.isNotEmpty()) {
            val s = st.longestPrefixOf(input)  // Find max prefix match s.
            BinaryStdOut.write(st[s]!!, W)      // Print s's encoding.
            val t = s!!.length
            if (t < input.length && code < L)
            // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++)
            input = input.substring(t)            // Scan past s in input.
        }
        BinaryStdOut.write(R, W)
        BinaryStdOut.close()
    }

    /**
     * Reads a sequence of bit encoded using LZW compression with
     * 12-bit codewords from standard input; expands them; and writes
     * the results to standard output.
     */
    fun expand() {
        val st = Array(L){if(it<R) ""+it.toChar() else ""}
        var i: Int = 0 // next available codeword value

        var codeword = BinaryStdIn.readInt(W)
        if (codeword == R) return            // expanded message is empty string
        var `val` = st[codeword]

        while (true) {
            BinaryStdOut.write(`val`)
            codeword = BinaryStdIn.readInt(W)
            if (codeword == R) break
            var s = st[codeword]
            if (i == codeword) s = `val` + `val`[0]   // special case hack
            if (i < L) st[i++] = `val` + s[0]
            `val` = s
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
        when {
            args[0] == "-" -> compress()
            args[0] == "+" -> expand()
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