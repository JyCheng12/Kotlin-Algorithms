/******************************************************************************
 * Compilation:  javac LongestRepeatedSubstring.java
 * Execution:    java LongestRepeatedSubstring < file.txt
 * Dependencies: StdIn.kt SuffixArray.kt
 * Data files:   https://algs4.cs.princeton.edu/63suffix/tale.txt
 * https://algs4.cs.princeton.edu/63suffix/tinyTale.txt
 * https://algs4.cs.princeton.edu/63suffix/mobydick.txt
 *
 * Reads a text string from stdin, replaces all consecutive blocks of
 * whitespace with a single space, and then computes the longest
 * repeated substring in that text using a suffix array.
 *
 * % java LongestRepeatedSubstring < tinyTale.txt
 * 'st of times it was the '
 *
 * % java LongestRepeatedSubstring < mobydick.txt
 * ',- Such a funny, sporty, gamy, jesty, joky, hoky-poky lad, is the Ocean, oh! Th'
 *
 * % java LongestRepeatedSubstring
 * aaaaaaaaa
 * 'aaaaaaaa'
 *
 * % java LongestRepeatedSubstring
 * abcdefg
 * ''
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LongestRepeatedSubstring` class provides a [SuffixArray]
 * client for computing the longest repeated substring of a string that
 * appears at least twice. The repeated substrings may overlap (but must
 * be distinct).
 *
 *
 * For additional documentation,
 * see [Section 6.3](https://algs4.cs.princeton.edu/63suffix) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 *
 * See also [LongestCommonSubstring].
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object LongestRepeatedSubstring {
    /**
     * Returns the longest repeated substring of the specified string.
     *
     * @param  text the string
     * @return the longest repeated substring that appears in `text`;
     * the empty string if no such string
     */
    fun lrs(text: String): String {
        val n = text.length
        val sa = SuffixArray(text)
        var lrs = ""
        for (i in 1 until n) {
            val length = sa.lcp(i)
            if (length > lrs.length) {
                lrs = text.substring(sa.index(i), sa.index(i) + length)
            }
        }
        return lrs
    }

    /**
     * Unit tests the `lrs()` method.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val text = StdIn.readAll().replace("\\s+", " ")
        StdOut.println("'${lrs(text)}'")
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
