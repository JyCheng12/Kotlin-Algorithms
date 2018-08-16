/******************************************************************************
 * Compilation:  javac LongestCommonSubstring.java
 * Execution:    java  LongestCommonSubstring file1.txt file2.txt
 * Dependencies: SuffixArray.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/63suffix/tale.txt
 * https://algs4.cs.princeton.edu/63suffix/mobydick.txt
 *
 * Read in two text files and find the longest substring that
 * appears in both texts.
 *
 * % java LongestCommonSubstring tale.txt mobydick.txt
 * ' seemed on the point of being '
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LongestCommonSubstring` class provides a [SuffixArray]
 * client for computing the longest common substring that appears in two
 * given strings.
 *
 *
 * This implementation computes the suffix array of each string and applies a
 * merging operation to determine the longest common substring.
 * For an alternate implementation, see
 * [LongestCommonSubstringConcatenate.java](https://algs4.cs.princeton.edu/63suffix/LongestCommonSubstringConcatenate.java.html).
 *
 *
 * For additional documentation,
 * see [Section 6.3](https://algs4.cs.princeton.edu/63suffix) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object LongestCommonSubstring {
    // return the longest common prefix of suffix s[p..] and suffix t[q..]
    private fun lcp(s: String, p: Int, t: String, q: Int): String {
        val n = Math.min(s.length - p, t.length - q)
        for (i in 0 until n)
            if (s[p + i] != t[q + i])
                return s.substring(p, p + i)
        return s.substring(p, p + n)
    }

    // compare suffix s[p..] and suffix t[q..]
    private fun compare(s: String, p: Int, t: String, q: Int): Int {
        val n = Math.min(s.length - p, t.length - q)
        for (i in 0 until n)
            if (s[p + i] != t[q + i])
                return s[p + i] - t[q + i]
        return when {
            s.length - p < t.length - q -> -1
            s.length - p > t.length - q -> +1
            else -> 0
        }
    }

    /**
     * Returns the longest common string of the two specified strings.
     *
     * @param  s one string
     * @param  t the other string
     * @return the longest common string that appears as a substring
     * in both `s` and `t`; the empty string
     * if no such string
     */
    fun lcs(s: String, t: String): String {
        val suffix1 = SuffixArray(s)
        val suffix2 = SuffixArray(t)

        // find longest common substring by "merging" sorted suffixes
        var lcs = ""
        var i = 0
        var j = 0
        while (i < s.length && j < t.length) {
            val p = suffix1.index(i)
            val q = suffix2.index(j)
            val x = lcp(s, p, t, q)
            if (x.length > lcs.length) lcs = x
            if (compare(s, p, t, q) < 0)
                i++
            else
                j++
        }
        return lcs
    }

    /**
     * Unit tests the `lcs()` method.
     * Reads in two strings from files specified as command-line arguments;
     * computes the longest common substring; and prints the results to
     * standard output.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val in1 = In(args[0])
        val in2 = In(args[1])
        val s = in1.readAll().trim().replace("\\s+", " ")
        val t = in2.readAll().trim().replace("\\s+", " ")
        StdOut.println("'${lcs(s, t)}'")
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