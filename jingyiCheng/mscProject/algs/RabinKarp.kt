/******************************************************************************
 * Compilation:  javac RabinKarp.java
 * Execution:    java RabinKarp pat txt
 * Dependencies: StdOut.kt
 *
 * Reads in two strings, the pattern and the input text, and
 * searches for the pattern in the input text using the
 * Las Vegas version of the Rabin-Karp algorithm.
 *
 * % java RabinKarp abracadabra abacadabrabracabracadabrabrabracad
 * pattern: abracadabra
 * text:    abacadabrabracabracadabrabrabracad
 * match:                 abracadabra
 *
 * % java RabinKarp rab abacadabrabracabracadabrabrabracad
 * pattern: rab
 * text:    abacadabrabracabracadabrabrabracad
 * match:           rab
 *
 * % java RabinKarp bcara abacadabrabracabracadabrabrabracad
 * pattern: bcara
 * text:         abacadabrabracabracadabrabrabracad
 *
 * %  java RabinKarp rabrabracad abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern:                        rabrabracad
 *
 * % java RabinKarp abacad abacadabrabracabracadabrabrabracad
 * text:    abacadabrabracabracadabrabrabracad
 * pattern: abacad
 *
 */

package jingyiCheng.mscProject.algs

import java.math.BigInteger
import java.util.Random

/**
 * The `RabinKarp` class finds the first occurrence of a pattern string
 * in a text string.
 *
 *
 * This implementation uses the Rabin-Karp algorithm.
 *
 *
 * For additional documentation,
 * see [Section 5.3](https://algs4.cs.princeton.edu/53substring) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Jingyi Cheng
 */
class RabinKarp// save pattern (needed only for Las Vegas)

// precompute R^(m-1) % q for use in removing leading digit
/**
 * Preprocesses the pattern string.
 *
 * @param pat the pattern string
 */(private var pat: String) {
    private val patHash: Long    // pattern hash value
    private val m: Int           // pattern length
    private val q: Long          // a large prime, small enough to avoid long overflow
    private var R: Int = 0           // radix
    private var RM: Long = 0         // R^(M-1) % Q

    init {
        R = 256
        m = pat.length
        q = longRandomPrime()
        RM = 1
        for (i in 1 until m)
            RM = R * RM % q
        patHash = hash(pat, m)
    }

    // Compute hash for key[0..m-1].
    private fun hash(key: String, m: Int): Long {
        var h: Long = 0
        for (j in 0 until m)
            h = (R * h + key[j].toLong()) % q
        return h
    }

    // Las Vegas version: does pat[] match txt[i..i-m+1] ?
    private fun check(txt: String, i: Int): Boolean {
        for (j in 0 until m)
            if (pat[j] != txt[i + j])
                return false
        return true
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
        val n = txt.length
        if (n < m) return n
        var txtHash = hash(txt, m)

        // check for match at offset 0
        if (patHash == txtHash && check(txt, 0)) return 0

        // check for hash match; if hash match, check for exact match
        for (i in m until n) {
            // Remove leading digit, add trailing digit, check for match.
            txtHash = (txtHash + q - RM * txt[i - m].toLong() % q) % q
            txtHash = (txtHash * R + txt[i].toLong()) % q

            // match
            val offset = i - m + 1
            if (patHash == txtHash && check(txt, offset))
                return offset
        }
        // no match
        return n
    }

    companion object {
        // a random 31-bit prime
        private fun longRandomPrime(): Long {
            val prime = BigInteger.probablePrime(31, Random())
            return prime.toLong()
        }

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

            val searcher = RabinKarp(pat)
            val offset = searcher.search(txt)

            // print results
            StdOut.println("text:    $txt")

            // from brute force search method 1
            StdOut.print("pattern: ")
            for (i in 0 until offset)
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
