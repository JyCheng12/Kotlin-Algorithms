/******************************************************************************
 * Compilation:  javac KMP.java
 * Execution:    java KMP pattern text
 * Dependencies: StdOut.kt
 *
 * Reads in two strings, the pattern and the input text, and
 * searches for the pattern in the input text using the
 * KMP algorithm.
 *
 * % java KMP abracadabra abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern:               abracadabra
 *
 * % java KMP rab abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern:         rab
 *
 * % java KMP bcara abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern:                                   bcara
 *
 * % java KMP rabrabracad abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern:                        rabrabracad
 *
 * % java KMP abacad abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern: abacad
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `KMP` class finds the first occurrence of a pattern string
 * in a text string.
 *
 *
 * This implementation uses a version of the Knuth-Morris-Pratt substring search
 * algorithm. The version takes time proportional to *n* + *m R*
 * in the worst case, where *n* is the length of the text string,
 * *m* is the length of the pattern, and *R* is the alphabet size.
 * It uses extra space proportional to *m R*.
 *
 *
 * For additional documentation,
 * see [Section 5.3](https://algs4.cs.princeton.edu/53substring) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Jingyi Cheng
 */
class KMP {
    private val R: Int       // the radix
    private var dfa: Array<IntArray>      // the KMP automoton
    private val pattern: CharArray    // either the character array for the pattern
    private val pat: String        // or the pattern string

    /**
     * Preprocesses the pattern string.
     *
     * @param pat the pattern string
     */
    constructor(pat: String) {
        this.R = 256
        this.pat = pat

        // build DFA from pattern
        val m = pat.length
        dfa = Array(R) { IntArray(m) }
        dfa[pat[0].toInt()][0] = 1
        var x = 0
        var j = 1
        while (j < m) {
            for (c in 0 until R)
                dfa[c][j] = dfa[c][x]     // Copy mismatch cases.
            dfa[pat[j].toInt()][j] = j + 1   // Set match case.
            x = dfa[pat[j].toInt()][x]     // Update restart state.
            j++
        }
        pattern = pat.toCharArray()
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

        // build DFA from pattern
        val m = pattern.size
        dfa = Array(R) { IntArray(m) }
        dfa[pattern[0].toInt()][0] = 1
        var x = 0
        var j = 1
        while (j < m) {
            for (c in 0 until R)
                dfa[c][j] = dfa[c][x]     // Copy mismatch cases.
            dfa[pattern[j].toInt()][j] = j + 1      // Set match case.
            x = dfa[pattern[j].toInt()][x]        // Update restart state.
            j++
        }
        pat = pattern.toString()
    }

    /**
     * Returns the index of the first occurrrence of the pattern string
     * in the text string.
     *
     * @param  txt the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; N if no such match
     */
    fun search(txt: String): Int {
        // simulate operation of DFA on text
        val m = pat.length
        val n = txt.length
        var i = 0
        var j = 0
        while (i < n && j < m) {
            j = dfa[txt[i].toInt()][j]
            i++
        }
        return if (j == m) i - m else n
    }

    /**
     * Returns the index of the first occurrrence of the pattern string
     * in the text string.
     *
     * @param  text the text string
     * @return the index of the first occurrence of the pattern string
     * in the text string; N if no such match
     */
    fun search(text: CharArray): Int {
        // simulate operation of DFA on text
        val m = pattern.size
        val n = text.size
        var i = 0
        var j = 0
        while (i < n && j < m) {
            j = dfa[text[i].toInt()][j]
            i++
        }
        return if (j == m) i - m else n
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

            val kmp1 = KMP(pat)
            val offset1 = kmp1.search(txt)

            val kmp2 = KMP(pattern, 256)
            val offset2 = kmp2.search(text)

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