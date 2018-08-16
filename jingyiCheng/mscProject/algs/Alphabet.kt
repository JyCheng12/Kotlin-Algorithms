/******************************************************************************
 * Compilation:  javac Alphabet.java
 * Execution:    java Alphabet
 * Dependencies: StdOut.kt
 *
 * A data type for alphabets, for use with string-processing code
 * that must convert between an alphabet of size R and the integers
 * 0 through R-1.
 *
 * Warning: supports only the basic multilingual plane (BMP), i.e,
 * Unicode characters between U+0000 and U+FFFF.
 *
 */

package jingyiCheng.mscProject.algs

/**
 *
 *
 * @author Jingyi Cheng
 */
class Alphabet {
    private var alphabet: CharArray     // the characters in the alphabet
    private var inverse: IntArray       // indices
    val R: Int         // the radix of the alphabet

    /**
     * Initializes a new alphabet from the given set of characters.
     *
     * @param alpha the set of characters
     */
    constructor(alpha: String) {
        // check that alphabet contains no duplicate chars
        val unicode = BooleanArray('\uFFFF'.toInt())
        for (i in 0 until alpha.length) {
            val c = alpha[i]
            if (unicode[c.toInt()])
                throw IllegalArgumentException("Illegal alphabet: repeated character = '$c'")
            unicode[c.toInt()] = true
        }
        alphabet = alpha.toCharArray()
        R = alpha.length
        inverse = IntArray('\uffff'.toInt()) { -1 }

        // can't use char since R can be as big as 65,536
        for (c in 0 until R)
            inverse[alphabet[c].toInt()] = c
    }

    /**
     * Initializes a new alphabet using characters 0 through R-1.
     *
     * @param radix the number of characters in the alphabet (the radix R)
     */
    private constructor(radix: Int) {
        this.R = radix
        alphabet = CharArray(R) { it.toChar() }
        inverse = IntArray(R) { it }
    }

    /**
     * Initializes a new alphabet using characters 0 through 255.
     */
    constructor() : this(256)

    /**
     * Returns true if the argument is a character in this alphabet.
     *
     * @param  c the character
     * @return `true` if `c` is a character in this alphabet;
     * `false` otherwise
     */
    operator fun contains(c: Char) = inverse[c.toInt()] != -1

    /**
     * Returns the binary logarithm of the number of characters in this alphabet.
     *
     * @return the binary logarithm (rounded up) of the number of characters in this alphabet
     */
    fun lgR(): Int {
        var lgR = 0
        var t = R - 1
        while (t >= 1) {
            lgR++
            t /= 2
        }
        return lgR
    }

    /**
     * Returns the index corresponding to the argument character.
     *
     * @param  c the character
     * @return the index corresponding to the character `c`
     * @throws IllegalArgumentException unless `c` is a character in this alphabet
     */
    fun toIndex(c: Char): Int {
        val _c = c.toInt()
        if (_c >= inverse.size || inverse[_c] == -1) throw IllegalArgumentException("Character $c not in alphabet")
        return inverse[_c]
    }

    /**
     * Returns the indices corresponding to the argument characters.
     *
     * @param  s the characters
     * @return the indices corresponding to the characters `s`
     * @throws IllegalArgumentException unless every character in `s`
     * is a character in this alphabet
     */
    fun toIndices(s: String): IntArray {
        val source = s.toCharArray()
        return IntArray(s.length) { toIndex(source[it]) }
    }

    /**
     * Returns the character corresponding to the argument index.
     *
     * @param  index the index
     * @return the character corresponding to the index `index`
     * @throws IllegalArgumentException unless `0 <= index < R`
     */
    fun toChar(index: Int): Char {
        if (index < 0 || index >= R) throw IllegalArgumentException("index must be between 0 and $R: $index")
        return alphabet[index]
    }

    /**
     * Returns the characters corresponding to the argument indices.
     *
     * @param  indices the indices
     * @return the characters corresponding to the indices `indices`
     * @throws IllegalArgumentException unless `0 < indices[i] < R`
     * for every `i`
     */
    fun toChars(indices: IntArray): String {
        val s = StringBuilder(indices.size)
        for (i in indices)
            s.append(toChar(i))
        return s.toString()
    }

    companion object {

        /**
         * The binary alphabet { 0, 1 }.
         */
        val BINARY = Alphabet("01")

        /**
         * The octal alphabet { 0, 1, 2, 3, 4, 5, 6, 7 }.
         */
        val OCTAL = Alphabet("01234567")

        /**
         * The decimal alphabet { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }.
         */
        val DECIMAL = Alphabet("0123456789")

        /**
         * The hexadecimal alphabet { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, A, B, C, D, E, F }.
         */
        val HEXADECIMAL = Alphabet("0123456789ABCDEF")

        /**
         * The DNA alphabet { A, C, T, G }.
         */
        val DNA = Alphabet("ACGT")

        /**
         * The lowercase alphabet { a, b, c, ..., z }.
         */
        val LOWERCASE = Alphabet("abcdefghijklmnopqrstuvwxyz")

        /**
         * The uppercase alphabet { A, B, C, ..., Z }.
         */

        val UPPERCASE = Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ")

        /**
         * The protein alphabet { A, C, D, E, F, G, H, I, K, L, M, N, P, Q, R, S, T, V, W, Y }.
         */
        val PROTEIN = Alphabet("ACDEFGHIKLMNPQRSTVWY")

        /**
         * The base-64 alphabet (64 characters).
         */
        val BASE64 = Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")

        /**
         * The ASCII alphabet (0-127).
         */
        val ASCII = Alphabet(128)

        /**
         * The extended ASCII alphabet (0-255).
         */
        val EXTENDED_ASCII = Alphabet(256)

        /**
         * The Unicode 16 alphabet (0-65,535).
         */
        val UNICODE16 = Alphabet(65536)

        /**
         * Unit tests the `Alphabet` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val encoded1 = Alphabet.BASE64.toIndices("NowIsTheTimeForAllGoodMen")
            val decoded1 = Alphabet.BASE64.toChars(encoded1)
            StdOut.println(decoded1)

            val encoded2 = Alphabet.DNA.toIndices("AACGAACGGTTTACCCCG")
            val decoded2 = Alphabet.DNA.toChars(encoded2)
            StdOut.println(decoded2)

            val encoded3 = Alphabet.DECIMAL.toIndices("01234567890123456789")
            val decoded3 = Alphabet.DECIMAL.toChars(encoded3)
            StdOut.println(decoded3)

            try {
                val error = Alphabet("0123451")
            } catch (e: IllegalArgumentException) {
                StdOut.println(e.message)
            }
        }
    }
}

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