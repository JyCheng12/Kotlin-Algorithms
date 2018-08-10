/******************************************************************************
 * Compilation:  javac TrieSET.java
 * Execution:    java TrieSET < words.txt
 * Dependencies: StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/52trie/shellsST.txt
 *
 * An set for extended ASCII strings, implemented  using a 256-way trie.
 *
 * Sample client reads in a list of words from standard input and
 * prints out each word, removing any duplicates.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `TrieSET` class represents an ordered set of strings over
 * the extended ASCII alphabet.
 * It supports the usual *add*, *contains*, and *delete*
 * methods. It also provides character-based methods for finding the string
 * in the set that is the *longest prefix* of a given prefix,
 * finding all strings in the set that *start with* a given prefix,
 * and finding all strings in the set that *match* a given pattern.
 *
 *
 * This implementation uses a 256-way trie.
 * The *add*, *contains*, *delete*, and
 * *longest prefix* methods take time proportional to the length
 * of the key (in the worst case). Construction takes constant time.
 *
 *
 * For additional documentation, see
 * [Section 5.2](https://algs4.cs.princeton.edu/52trie) of
 * *Algorithms in Java, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
/**
 * Initializes an empty set of strings.
 */
class TrieSET : Iterable<String> {
    private var root: Node? = null      // root of trie
    var size: Int = 0          // number of keys in trie
        private set

    /**
     * Is the set empty?
     * @return `true` if the set is empty, and `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // R-way trie node
    internal class Node {
        val next = arrayOfNulls<Node>(R)
        var isString: Boolean = false
    }

    /**
     * Does the set contain the given key?
     * @param key the key
     * @return `true` if the set contains `key` and
     * `false` otherwise
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun contains(key: String?): Boolean {
        if (key == null) throw IllegalArgumentException("argument to contains() is null")
        val x = get(root, key, 0) ?: return false
        return x.isString
    }

    private operator fun get(x: Node?, key: String, d: Int): Node? {
        if (x == null) return null
        if (d == key.length) return x
        val c = key[d]
        return get(x.next[c.toInt()], key, d + 1)
    }

    /**
     * Adds the key to the set if it is not already present.
     * @param key the key to add
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun add(key: String?) {
        if (key == null) throw IllegalArgumentException("argument to add() is null")
        root = add(root, key, 0)
    }

    private fun add(x: Node?, key: String, d: Int): Node {
        var x = x
        if (x == null) x = Node()
        if (d == key.length) {
            if (!x.isString) size++
            x.isString = true
        } else {
            val c = key[d]
            x.next[c.toInt()] = add(x.next[c.toInt()], key, d + 1)
        }
        return x
    }

    /**
     * Returns all of the keys in the set, as an iterator.
     * To iterate over all of the keys in a set named `set`, use the
     * foreach notation: `for (Key key : set)`.
     * @return an iterator to all of the keys in the set
     */
    override fun iterator() = keysWithPrefix("").iterator()

    /**
     * Returns all of the keys in the set that start with `prefix`.
     * @param prefix the prefix
     * @return all of the keys in the set that start with `prefix`,
     * as an iterable
     */
    fun keysWithPrefix(prefix: String): Iterable<String> {
        val results = nnQueue<String>()
        val x = get(root, prefix, 0)
        collect(x, StringBuilder(prefix), results)
        return results
    }

    private fun collect(x: Node?, prefix: StringBuilder, results: nnQueue<String>) {
        if (x == null) return
        if (x.isString) results.enqueue(prefix.toString())
        for (c in 0 until R) {
            prefix.append(c.toChar())
            collect(x.next[c], prefix, results)
            prefix.deleteCharAt(prefix.length - 1)
        }
    }

    /**
     * Returns all of the keys in the set that match `pattern`,
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the set that match `pattern`,
     * as an iterable, where . is treated as a wildcard character.
     */
    fun keysThatMatch(pattern: String): Iterable<String> {
        val results = nnQueue<String>()
        val prefix = StringBuilder()
        collect(root, prefix, pattern, results)
        return results
    }

    private fun collect(x: Node?, prefix: StringBuilder, pattern: String, results: nnQueue<String>) {
        if (x == null) return
        val d = prefix.length
        if (d == pattern.length && x.isString)
            results.enqueue(prefix.toString())
        if (d == pattern.length) return
        val c = pattern[d]
        if (c == '.')
            for (ch in 0 until R) {
                prefix.append(ch.toChar())
                collect(x.next[ch], prefix, pattern, results)
                prefix.deleteCharAt(prefix.length - 1)
            }
        else {
            prefix.append(c)
            collect(x.next[c.toInt()], prefix, pattern, results)
            prefix.deleteCharAt(prefix.length - 1)
        }
    }

    /**
     * Returns the string in the set that is the longest prefix of `query`,
     * or `null`, if no such string.
     * @param query the query string
     * @return the string in the set that is the longest prefix of `query`,
     * or `null` if no such string
     * @throws IllegalArgumentException if `query` is `null`
     */
    fun longestPrefixOf(query: String?): String? {
        if (query == null) throw IllegalArgumentException("argument to longestPrefixOf() is null")
        val length = longestPrefixOf(root, query, 0, -1)
        return if (length == -1) null else query.substring(0, length)
    }

    // returns the length of the longest string key in the subtrie
    // rooted at x that is a prefix of the query string,
    // assuming the first d character match and we have already
    // found a prefix match of length length
    private fun longestPrefixOf(x: Node?, query: String, d: Int, length: Int): Int {
        var length = length
        if (x == null) return length
        if (x.isString) length = d
        if (d == query.length) return length
        val c = query[d]
        return longestPrefixOf(x.next[c.toInt()], query, d + 1, length)
    }

    /**
     * Removes the key from the set if the key is present.
     * @param key the key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun delete(key: String?) {
        if (key == null) throw IllegalArgumentException("argument to delete() is null")
        root = delete(root, key, 0)
    }

    private fun delete(x: Node?, key: String, d: Int): Node? {
        if (x == null) return null
        if (d == key.length) {
            if (x.isString) size--
            x.isString = false
        } else {
            val c = key[d].toInt()
            x.next[c] = delete(x.next[c], key, d + 1)
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.isString) return x
        for (c in 0 until R)
            if (x.next[c] != null)
                return x
        return null
    }

    companion object {
        private const val R = 256        // extended ASCII

        /**
         * Unit tests the `TrieSET` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val set = TrieSET()
            while (!StdIn.isEmpty) {
                val key = StdIn.readString()
                set.add(key)
            }

            // print results
            if (set.size < 100) {
                StdOut.println("keys(\"\"):")
                for (key in set)
                    StdOut.println(key)
                StdOut.println()
            }

            StdOut.println("longestPrefixOf(\"shellsort\"):")
            StdOut.println(set.longestPrefixOf("shellsort"))
            StdOut.println()

            StdOut.println("longestPrefixOf(\"xshellsort\"):")
            StdOut.println(set.longestPrefixOf("xshellsort"))
            StdOut.println()

            StdOut.println("keysWithPrefix(\"shor\"):")
            for (s in set.keysWithPrefix("shor"))
                StdOut.println(s)
            StdOut.println()

            StdOut.println("keysWithPrefix(\"shortening\"):")
            for (s in set.keysWithPrefix("shortening"))
                StdOut.println(s)
            StdOut.println()

            StdOut.println("keysThatMatch(\".he.l.\"):")
            for (s in set.keysThatMatch(".he.l."))
                StdOut.println(s)
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
