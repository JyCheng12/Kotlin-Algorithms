/******************************************************************************
 * Compilation:  javac Huffman.java
 * Execution:    java Huffman - < input.txt   (compress)
 * Execution:    java Huffman + < input.txt   (expand)
 * Dependencies: BinaryIn.kt BinaryOut.kt
 * Data files:   https://algs4.cs.princeton.edu/55compression/abra.txt
 * https://algs4.cs.princeton.edu/55compression/tinytinyTale.txt
 * https://algs4.cs.princeton.edu/55compression/medTale.txt
 * https://algs4.cs.princeton.edu/55compression/tale.txt
 *
 * Compress or expand a binary input stream using the Huffman algorithm.
 *
 * % java Huffman - < abra.txt | java BinaryDump 60
 * 010100000100101000100010010000110100001101010100101010000100
 * 000000000000000000000000000110001111100101101000111110010100
 * 120 bits
 *
 * % java Huffman - < abra.txt | java Huffman +
 * ABRACADABRA!
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Huffman` class provides static methods for compressing
 * and expanding a binary input using Huffman codes over the 8-bit extended
 * ASCII alphabet.
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
object Huffman {
    // alphabet size of extended ASCII
    private const val R = 256

    // Huffman trie node
    internal class Node
    constructor(val ch: Char, val freq: Int, var left: Node?, var right: Node?) : Comparable<Node> {

        // is the node a leaf node?
        val isLeaf: Boolean
            get() {
                assert(left == null && right == null || left != null && right != null)
                return left == null && right == null
            }

        // compare, based on frequency
        override fun compareTo(other: Node) = this.freq - other.freq
    }

    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses them
     * using Huffman codes with an 8-bit alphabet; and writes the results
     * to standard output.
     */
    fun compress() {
        // read the input
        val s = BinaryStdIn.readString()
        val input = s.toCharArray()

        // tabulate frequency counts
        val freq = IntArray(R)
        for (i in input)
            freq[i.toInt()]++

        // build Huffman trie
        val root = buildTrie(freq)

        // build code table
        val st = Array(R) { "" }
        buildCode(st, root, "")

        // print trie for decoder
        writeTrie(root)

        // print number of bytes in original uncompressed message
        BinaryStdOut.write(input.size)

        // use Huffman code to encode input
        for (i in input) {
            val code = st[i.toInt()]
            for (j in 0 until code.length)
                when (code[j]) {
                    '0' -> BinaryStdOut.write(false)
                    '1' -> BinaryStdOut.write(true)
                    else -> throw IllegalStateException("Illegal state")
                }
        }

        // close output stream
        BinaryStdOut.close()
    }

    // build the Huffman trie given frequencies
    private fun buildTrie(freq: IntArray): Node {
        // initialze priority queue with singleton trees
        val pq = MinPQ<Node>()
        for (i in 0 until R)
            if (freq[i] > 0)
                pq.insert(Node(i.toChar(), freq[i], null, null))

        // special case in case there is only one character with a nonzero frequency
        if (pq.size == 1) {
            if (freq['\u0000'.toInt()] == 0)
                pq.insert(Node('\u0000', 0, null, null))
            else
                pq.insert(Node('\u0001', 0, null, null))
        }

        // merge two smallest trees
        while (pq.size > 1) {
            val left = pq.delMin()
            val right = pq.delMin()
            val parent = Node('\u0000', left.freq + right.freq, left, right)
            pq.insert(parent)
        }
        return pq.delMin()
    }

    // write bitstring-encoded trie to standard output
    private fun writeTrie(x: Node) {
        if (x.isLeaf) {
            BinaryStdOut.write(true)
            BinaryStdOut.write(x.ch, 8)
            return
        }
        BinaryStdOut.write(false)
        writeTrie(x.left!!)
        writeTrie(x.right!!)
    }

    // make a lookup table from symbols and their encodings
    private fun buildCode(st: Array<String>, x: Node, s: String) {
        if (!x.isLeaf) {
            buildCode(st, x.left!!, s + '0')
            buildCode(st, x.right!!, s + '1')
        } else
            st[x.ch.toInt()] = s
    }

    /**
     * Reads a sequence of bits that represents a Huffman-compressed message from
     * standard input; expands them; and writes the results to standard output.
     */
    fun expand() {
        // read in Huffman trie from input stream
        val root = readTrie()

        // number of bytes to write
        val length = BinaryStdIn.readInt()

        // decode using the Huffman trie
        for (i in 0 until length) {
            var x: Node? = root
            while (!x!!.isLeaf) {
                val bit = BinaryStdIn.readBoolean()
                x = if (bit) x.right
                else x.left
            }
            BinaryStdOut.write(x.ch, 8)
        }
        BinaryStdOut.close()
    }

    private fun readTrie(): Node {
        val isLeaf = BinaryStdIn.readBoolean()
        return if (isLeaf)
            Node(BinaryStdIn.readChar(), -1, null, null)
         else
            Node('\u0000', -1, readTrie(), readTrie())
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