/******************************************************************************
 * Compilation:  javac TST.java
 * Execution:    java TST < words.txt
 * Dependencies: StdIn.kt
 * Data files:   https://algs4.cs.princeton.edu/52trie/shellsST.txt
 *
 * Symbol table with string keys, implemented using a ternary search
 * trie (TST).
 *
 *
 * % java TST < shellsST.txt
 * keys(""):
 * by 4
 * sea 6
 * sells 1
 * she 0
 * shells 3
 * shore 7
 * the 5
 *
 * longestPrefixOf("shellsort"):
 * shells
 *
 * keysWithPrefix("shor"):
 * shore
 *
 * keysThatMatch(".he.l."):
 * shells
 *
 * % java TST
 * theory the now is the time for all good men
 *
 * Remarks
 * --------
 * - can't use a key that is the empty string ""
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `TST` class represents an symbol table of key-value
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
 * This implementation uses a ternary search trie.
 *
 *
 * For additional documentation, see [Section 5.2](https://algs4.cs.princeton.edu/52trie) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 */
/**
 * Initializes an empty string symbol table.
 */
class TST<Value> {
    var size: Int = 0              // size
        private set
    private var root: Node<Value>? = null   // root of TST

    internal class Node<Value> {
        var c: Char = ' '                        // character
        var left: Node<Value>? = null
        var mid: Node<Value>? = null
        var right: Node<Value>? = null  // left, middle, and right subtries
        var `val`: Value? = null                     // value associated with string
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

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and `null` if the key is not in the symbol table
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun get(key: String?): Value? {
        if (key == null) throw IllegalArgumentException("calls get() with null argument")
        if (key.isEmpty()) throw IllegalArgumentException("key must have length >= 1")
        val x = get(root, key, 0) ?: return null
        return x.`val`
    }

    // return subtrie corresponding to given key
    private operator fun get(x: Node<Value>?, key: String, d: Int): Node<Value>? {
        if (x == null) return null
        if (key.isEmpty()) throw IllegalArgumentException("key must have length >= 1")
        val c = key[d]
        return when {
            c < x.c -> get(x.left, key, d)
            c > x.c -> get(x.right, key, d)
            d < key.length - 1 -> get(x.mid, key, d + 1)
            else -> x
        }
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is `null`, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun put(key: String?, `val`: Value) {
        if (key == null) throw IllegalArgumentException("calls put() with null key")
        if (!contains(key)) size++
        root = put(root, key, `val`, 0)
    }

    private fun put(x: Node<Value>?, key: String, `val`: Value, d: Int): Node<Value> {
        var x = x
        val c = key[d]
        if (x == null) {
            x = Node()
            x.c = c
        }
        when {
            c < x.c -> x.left = put(x.left, key, `val`, d)
            c > x.c -> x.right = put(x.right, key, `val`, d)
            d < key.length - 1 -> x.mid = put(x.mid, key, `val`, d + 1)
            else -> x.`val` = `val`
        }
        return x
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
        if (query == null) throw IllegalArgumentException("calls longestPrefixOf() with null argument")
        if (query.isEmpty()) return null
        var length = 0
        var x = root
        var i = 0
        while (x != null && i < query.length) {
            val c = query[i]
            when {
                c < x.c -> x = x.left
                c > x.c -> x = x.right
                else -> {
                    i++
                    if (x.`val` != null) length = i
                    x = x.mid
                }
            }
        }
        return query.substring(0, length)
    }

    /**
     * Returns all keys in the symbol table as an `Iterable`.
     * To iterate over all of the keys in the symbol table named `st`,
     * use the foreach notation: `for (Key key : st.keys())`.
     * @return all keys in the symbol table as an `Iterable`
     */
    fun keys(): Iterable<String> {
        val queue = nnQueue<String>()
        collect(root, StringBuilder(), queue)
        return queue
    }

    /**
     * Returns all of the keys in the set that start with `prefix`.
     * @param prefix the prefix
     * @return all of the keys in the set that start with `prefix`,
     * as an iterable
     * @throws IllegalArgumentException if `prefix` is `null`
     */
    fun keysWithPrefix(prefix: String?): Iterable<String> {
        if (prefix == null) throw IllegalArgumentException("calls keysWithPrefix() with null argument")
        val queue = nnQueue<String>()
        val x = get(root, prefix, 0) ?: return queue
        if (x.`val` != null) queue.enqueue(prefix)
        collect(x.mid, StringBuilder(prefix), queue)
        return queue
    }

    // all keys in subtrie rooted at x with given prefix
    private fun collect(x: Node<Value>?, prefix: StringBuilder, queue: nnQueue<String>) {
        if (x == null) return
        collect(x.left, prefix, queue)
        if (x.`val` != null) queue.enqueue("$prefix${x.c}")
        collect(x.mid, prefix.append(x.c), queue)
        prefix.deleteCharAt(prefix.length - 1)
        collect(x.right, prefix, queue)
    }

    /**
     * Returns all of the keys in the symbol table that match `pattern`,
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match `pattern`,
     * as an iterable, where . is treated as a wildcard character.
     */
    fun keysThatMatch(pattern: String): Iterable<String> {
        val queue = nnQueue<String>()
        collect(root, StringBuilder(), 0, pattern, queue)
        return queue
    }

    private fun collect(x: Node<Value>?, prefix: StringBuilder, i: Int, pattern: String, queue: nnQueue<String>) {
        if (x == null) return
        val c = pattern[i]
        if (c == '.' || c < x.c) collect(x.left, prefix, i, pattern, queue)
        if (c == '.' || c == x.c) {
            if (i == pattern.length - 1 && x.`val` != null) queue.enqueue("$prefix${x.c}")
            if (i < pattern.length - 1) {
                collect(x.mid, prefix.append(x.c), i + 1, pattern, queue)
                prefix.deleteCharAt(prefix.length - 1)
            }
        }
        if (c == '.' || c > x.c) collect(x.right, prefix, i, pattern, queue)
    }

    companion object {
        /**
         * Unit tests the `TST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // build symbol table from standard input
            val st = TST<Int>()
            var i = 0
            while (!StdIn.isEmpty) {
                val key = StdIn.readString()
                st.put(key, i)
                i++
            }

            // print results
            if (st.size < 100) {
                StdOut.println("keys(\"\"):")
                for (key in st.keys()) {
                    StdOut.println("$key ${st[key]}")
                }
                StdOut.println()
            }

            StdOut.println("longestPrefixOf(\"shellsort\"):")
            StdOut.println(st.longestPrefixOf("shellsort"))
            StdOut.println()

            StdOut.println("longestPrefixOf(\"shell\"):")
            StdOut.println(st.longestPrefixOf("shell"))
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
