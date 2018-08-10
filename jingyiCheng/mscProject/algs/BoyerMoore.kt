/******************************************************************************
 * Compilation:  javac BoyerMoore.java
 * Execution:    java BoyerMoore pattern text
 * Dependencies: StdOut.kt
 *
 * Reads in two strings, the pattern and the input text, and
 * searches for the pattern in the input text using the
 * bad-character rule part of the Boyer-Moore algorithm.
 * (does not implement the strong good suffix rule)
 *
 * % java BoyerMoore abracadabra abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern:               abracadabra
 *
 * % java BoyerMoore rab abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern:         rab
 *
 * % java BoyerMoore bcara abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern:                                   bcara
 *
 * % java BoyerMoore rabrabracad abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern:                        rabrabracad
 *
 * % java BoyerMoore abacad abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern: abacad
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `BoyerMoore` class finds the first occurrence of a pattern string
 * in a text string.
 *
 *
 * This implementation uses the Boyer-Moore algorithm (with the bad-character
 * rule, but not the strong good suffix rule).
 *
 *
 * For additional documentation,
 * see [Section 5.3](https://algs4.cs.princeton.edu/53substring) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Jingyi Cheng
 */
class BoyerMoore {
    private val R: Int     // the radix
    private var right: IntArray     // the bad-character skip array
    private val pattern: CharArray  // store the pattern as a character array
    private val pat: String      // or as a string

    /**
     * Preprocesses the pattern string.
     *
     * @param pat the pattern string
     */
    constructor(pat: String) {
        this.R = 256
        this.pat = pat
        this.pattern = pat.toCharArray()

        // position of rightmost occurrence of c in the pattern
        right = IntArray(R) { -1 }
        for (j in 0 until pat.length)
            right[pat[j].toInt()] = j
    }

    /**
     * Preprocesses the pattern string.
     *
     * @param pattern the pattern string
     * @param R the alphabet size
     */
    constructor(pattern: CharArray, R: Int = 256) {
        this.R = R
        this.pattern = pattern
        this.pat = pattern.toString()

        // position of rightmost occurrence of c in the pattern
        right = IntArray(R) { -1 }
        for (j in pattern.indices)
            right[pattern[j].toInt()] = j
    }

    /**
     * Returns the index of the first occurrrence of the pattern string
     * in the text string.
     *
     * @param  txt the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; n if no such match
     */
    fun search(txt: String): Int {
        val m = pat.length
        val n = txt.length
        var skip: Int
        var i = 0
        while (i <= n - m) {
            skip = 0
            for (j in m - 1 downTo 0)
                if (pat[j] != txt[i + j]) {
                    skip = Math.max(1, j - right[txt[i + j].toInt()])
                    break
                }
            if (skip == 0) return i    // found
            i += skip
        }
        return n                       // not found
    }

    /**
     * Returns the index of the first occurrrence of the pattern string
     * in the text string.
     *
     * @param  text the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; n if no such match
     */
    fun search(text: CharArray): Int {
        val m = pattern.size
        val n = text.size
        var skip: Int
        var i = 0
        while (i <= n - m) {
            skip = 0
            for (j in m - 1 downTo 0)
                if (pattern[j] != text[i + j]) {
                    skip = Math.max(1, j - right[text[i + j].toInt()])
                    break
                }
            if (skip == 0) return i    // found
            i += skip
        }
        return n                       // not found
    }

    companion object {
        /**
         * Takes a pattern string and an input string as command-line arguments;
         * searches for the pattern string in the text string; and prints
         * the first occurrence of the pattern string in the text string.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val pat = args[0]
            val txt = args[1]
            val pattern = pat.toCharArray()
            val text = txt.toCharArray()

            val boyermoore1 = BoyerMoore(pat)
            val boyermoore2 = BoyerMoore(pattern, 256)
            val offset1 = boyermoore1.search(txt)
            val offset2 = boyermoore2.search(text)

            // print results
            StdOut.println("text:    $txt")
            StdOut.print("pattern: ")
            for (i in 0 until offset1)
                StdOut.print(" ")
            StdOut.println(pat)

            StdOut.print("pattern: ")
            for (i in 0 until offset2)
                StdOut.print(" ")
            StdOut.println(pat)
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
