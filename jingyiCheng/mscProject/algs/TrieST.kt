/******************************************************************************
 * Compilation:  javac TrieST.java
 * Execution:    java TrieST < words.txt
 * Dependencies: StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/52trie/shellsST.txt
 *
 * A string symbol table for extended ASCII strings, implemented
 * using a 256-way trie.
 *
 * % java TrieST < shellsST.txt
 * by 4
 * sea 6
 * sells 1
 * she 0
 * shells 3
 * shore 7
 * the 5
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `TrieST` class represents an symbol table of key-value
 * pairs, with string keys and generic values.
 * It supports the usual *put*, *get*, *contains*,
 * *delete*, *size*, and *is-empty* methods.
 * It also provides character-based methods for finding the string
 * in the symbol table that is the *longest prefix* of a given prefix,
 * finding all strings in the symbol table that *start with* a given prefix,
 * and finding all strings in the symbol table that *match* a given pattern.
 * A symbol table implements the *associative array* abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike [java.util.Map], this class uses the convention that
 * values cannot be `null`—setting the
 * value associated with a key to `null` is equivalent to deleting the key
 * from the symbol table.
 *
 *
 * This implementation uses a 256-way trie.
 * The *put*, *contains*, *delete*, and
 * *longest prefix* operations take time proportional to the length
 * of the key (in the worst case). Construction takes constant time.
 * The *size*, and *is-empty* operations take constant time.
 * Construction takes constant time.
 *
 *
 * For additional documentation, see [Section 5.2](https://algs4.cs.princeton.edu/52trie) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Jingyi Cheng
 *
 */

/**
 * Initializes an empty string symbol table.
 */
class TrieST<Value> {
    private var root: Node<Value>? = null      // root of trie
    var size: Int = 0          // number of keys in trie
        private set

    /**
     * Is this symbol table empty?
     * @return `true` if this symbol table is empty and `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // R-way trie node
    internal class Node<Value> {
        var `val`: Value? = null
        var next = arrayOfNulls<Node<Value>>(R)
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and `null` if the key is not in the symbol table
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun get(key: String?): Value? {
        if (key == null) throw IllegalArgumentException("argument to get() is null")
        val x = get(root, key, 0) ?: return null
        return x.`val`
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return `true` if this symbol table contains `key` and
     * `false` otherwise
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun contains(key: String?): Boolean {
        if (key == null) throw IllegalArgumentException("argument to contains() is null")
        return get(key) != null
    }

    private operator fun get(x: Node<Value>?, key: String, d: Int): Node<Value>? {
        if (x == null) return null
        if (d == key.length) return x
        val c = key[d]
        return get(x.next[c.toInt()], key, d + 1)
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is `null`, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun put(key: String?, `val`: Value?) {
        if (key == null) throw IllegalArgumentException("first argument to put() is null")
        if (`val` == null)
            delete(key)
        else
            root = put(root, key, `val`, 0)
    }

    private fun put(x: Node<Value>?, key: String, `val`: Value, d: Int): Node<Value> {
        var x = x
        if (x == null) x = Node<Value>()
        if (d == key.length) {
            if (x.`val` == null) size++
            x.`val` = `val`
            return x
        }
        val c = key[d]
        x.next[c.toInt()] = put(x.next[c.toInt()], key, `val`, d + 1)
        return x
    }

    /**
     * Returns all keys in the symbol table as an `Iterable`.
     * To iterate over all of the keys in the symbol table named `st`,
     * use the foreach notation: `for (Key key : st.keys())`.
     * @return all keys in the symbol table as an `Iterable`
     */
    fun keys() = keysWithPrefix("")

    /**
     * Returns all of the keys in the set that start with `prefix`.
     * @param prefix the prefix
     * @return all of the keys in the set that start with `prefix`,
     * as an iterable
     */
    fun keysWithPrefix(prefix: String): Iterable<String?> {
        val results = Queue<String>()
        val x = get(root, prefix, 0)
        collect(x, StringBuilder(prefix), results)
        return results
    }

    private fun collect(x: Node<Value>?, prefix: StringBuilder, results: Queue<String>) {
        if (x == null) return
        if (x.`val` != null) results.enqueue(prefix.toString())
        for (c in 0 until R) {
            prefix.append(c.toChar())
            collect(x.next[c], prefix, results)
            prefix.deleteCharAt(prefix.length - 1)
        }
    }

    /**
     * Returns all of the keys in the symbol table that match `pattern`,
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match `pattern`,
     * as an iterable, where . is treated as a wildcard character.
     */
    fun keysThatMatch(pattern: String): Iterable<String?> {
        val results = Queue<String>()
        collect(root, StringBuilder(), pattern, results)
        return results
    }

    private fun collect(x: Node<Value>?, prefix: StringBuilder, pattern: String, results: Queue<String>) {
        if (x == null) return
        val d = prefix.length
        if (d == pattern.length && x.`val` != null)
            results.enqueue(prefix.toString())
        if (d == pattern.length)
            return
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
     * Returns the string in the symbol table that is the longest prefix of `query`,
     * or `null`, if no such string.
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of `query`,
     * or `null` if no such string
     * @throws IllegalArgumentException if `query` is `null`
     */
    fun longestPrefixOf(query: String?): String? {
        if (query == null) throw IllegalArgumentException("argument to longestPrefixOf() is null")
        val length = longestPrefixOf(root, query, 0, -1)
        return if (length == -1) null
        else query.substring(0, length)
    }

    // returns the length of the longest string key in the subtrie
    // rooted at x that is a prefix of the query string,
    // assuming the first d character match and we have already
    // found a prefix match of given length (-1 if no such match)
    private fun longestPrefixOf(x: Node<Value>?, query: String, d: Int, length: Int): Int {
        var length = length
        if (x == null) return length
        if (x.`val` != null) length = d
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

    private fun delete(x: Node<Value>?, key: String, d: Int): Node<Value>? {
        if (x == null) return null
        if (d == key.length) {
            if (x.`val` != null) size--
            x.`val` = null
        } else {
            val c = key[d]
            x.next[c.toInt()] = delete(x.next[c.toInt()], key, d + 1)
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.`val` != null) return x
        for (c in 0 until R)
            if (x.next[c] != null)
                return x
        return null
    }

    companion object {
        private val R = 256        // extended ASCII

        /**
         * Unit tests the `TrieST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // build symbol table from standard input
            val st = TrieST<Int>()
            var i = 0

            while (!StdIn.isEmpty) {
                val key = StdIn.readString()
                st.put(key, i)
                i++
            }

            // print results
            if (st.size < 100) {
                StdOut.println("keys(\"\"):")
                for (key in st.keys())
                    StdOut.println("$key ${st[key]}")
                StdOut.println()
            }

            StdOut.println("longestPrefixOf(\"shellsort\"):")
            StdOut.println(st.longestPrefixOf("shellsort"))
            StdOut.println()

            StdOut.println("longestPrefixOf(\"quicksort\"):")
            StdOut.println(st.longestPrefixOf("quicksort"))
            StdOut.println()

            StdOut.println("keysWithPrefix(\"shor\"):")
            for (s in st.keysWithPrefix("shor"))
                StdOut.println(s)
            StdOut.println()

            StdOut.println("keysThatMatch(\".he.l.\"):")
            for (s in st.keysThatMatch(".he.l."))
                StdOut.println(s)
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