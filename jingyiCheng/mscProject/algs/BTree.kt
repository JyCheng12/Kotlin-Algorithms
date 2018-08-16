/******************************************************************************
 * Compilation:  javac BTree.java
 * Execution:    java BTree
 * Dependencies: StdOut.kt
 *
 * B-tree.
 *
 * cs.princeton.edu:  128.112.136.12
 * hardvardsucks.com: null
 * simpsons.com:      209.052.165.60
 * apple.com:         17.112.152.32
 * ebay.com:          66.135.192.87
 * dell.com:          143.166.224.230
 *
 * size:    17
 * height:  2
 * www.amazon.com 207.171.182.16
 * www.apple.com 17.112.152.32
 * www.cnn.com 64.236.16.20
 * (www.cs.princeton.edu)
 * www.cs.princeton.edu 128.112.136.12
 * www.cs.princeton.edu 128.112.136.11
 * www.dell.com 143.166.224.230
 * (www.ebay.com)
 * www.ebay.com 66.135.192.87
 * www.espn.com 199.181.135.201
 * www.google.com 216.239.41.99
 * (www.microsoft.com)
 * www.microsoft.com 207.126.99.140
 * www.nytimes.com 199.239.136.200
 * (www.princeton.edu)
 * www.princeton.edu 128.112.128.15
 * www.simpsons.com 209.052.165.60
 * (www.slashdot.org)
 * www.slashdot.org 66.35.250.151
 * www.weather.com 63.111.66.11
 * (www.yahoo.com)
 * www.yahoo.com 216.109.118.65
 * www.yale.edu 130.132.143.21
 *
 * Limitations
 * -----------
 * -  Assumes M is even and M >= 4
 * -  should b be an array of children or list (it would help with
 * casting to make it a list)
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `BTree` class represents an ordered symbol table of generic
 * key-value pairs.
 * It supports the *put*, *get*, *contains*,
 * *size*, and *is-empty* methods.
 * A symbol table implements the *associative array* abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike [java.util.Map], this class uses the convention that
 * values cannot be `null`—setting the
 * value associated with a key to `null` is equivalent to deleting the key
 * from the symbol table.
 *
 *
 * This implementation uses a B-tree. It requires that
 * the key type implements the `Comparable` interface and calls the
 * `compareTo()` and method to compare two keys. It does not call either
 * `equals()` or `hashCode()`.
 * The *get*, *put*, and *contains* operations
 * each make log<sub>*m*</sub>(*n*) probes in the worst case,
 * where *n* is the number of key-value pairs
 * and *m* is the branching factor.
 * The *size*, and *is-empty* operations take constant time.
 * Construction takes constant time.
 *
 *
 * For additional documentation, see
 * [Section 6.2](https://algs4.cs.princeton.edu/62btree) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Jingyi Cheng
 *
 */
class BTree<Key : Comparable<Key>, Value> {

    private var root: Node<Key, Value> = Node(0)      // root of the B-tree
    var height: Int = 0      // height of the B-tree
        private set
    var size: Int = 0           // number of key-value pairs in the B-tree
        private set

    /**
     * Returns true if this symbol table is empty.
     * @return `true` if this symbol table is empty; `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // helper B-tree node data type
    internal class Node<Key, Value>(var m: Int ) {                           // number of children
        val children = arrayOfNulls<Entry<Key, Value>>(M) as Array<Entry<Key,Value>>   // the array of children
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    internal class Entry<Key, Value>(var key: Key, var `val`: Value?, var next: Node<Key, Value>?)     // helper field to iterate over array entries

    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and `null` if the key is not in the symbol table
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun get(key: Key?): Value? {
        if (key == null) throw IllegalArgumentException("argument to get() is null")
        return search(root, key, height)
    }

    private fun search(x: Node<Key, Value>, key: Key, ht: Int): Value? {
        val children = x.children

        // external node
        if (ht == 0) {
            for (j in 0 until x.m)
                if (eq(key, children[j].key)) return children[j].`val`
        } else
            for (j in 0 until x.m)
                if (j + 1 == x.m || less(key, children[j + 1].key))
                    return search(children[j].next!!, key, ht - 1)
        // internal node
        return null
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is `null`, this effectively deletes the key from the symbol table.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun put(key: Key?, `val`: Value) {
        if (key == null) throw IllegalArgumentException("argument key to put() is null")
        val u = insert(root, key, `val`, height)
        size++
        if (u == null) return

        // need to split root
        val t = Node<Key,Value>(2)
        t.children[0] = Entry(root.children[0].key, null, root)
        t.children[1] = Entry(u.children[0].key, null, u)
        root = t
        height++
    }

    private fun insert(h: Node<Key,Value>?, key: Key, `val`: Value, ht: Int): Node<Key,Value>? {
        var j: Int
        val t = Entry(key, `val`, null)

        // external node
        if (ht == 0) {
            j = 0
            while (j < h!!.m) {
                if (less(key, h.children[j].key)) break
                j++
            }
        } else {
            j = 0
            while (j < h!!.m) {
                if (j + 1 == h.m || less(key, h.children[j + 1].key)) {
                    val u = insert(h.children[j++].next, key, `val`, ht - 1) ?: return null
                    t.key = u.children[0].key
                    t.next = u
                    break
                }
                j++
            }
        }// internal node

        for (i in h.m downTo j + 1)
            h.children[i] = h.children[i - 1]
        h.children[j] = t
        h.m++
        return if (h.m < M)
            null
        else
            split(h)
    }

    // split node in half
    private fun split(h: Node<Key,Value>): Node<Key,Value> {
        val t = Node<Key,Value>(M / 2)
        h.m = M / 2
        for (j in 0 until M / 2)
            t.children[j] = h.children[M / 2 + j]
        return t
    }

    /**
     * Returns a string representation of this B-tree (for debugging).
     *
     * @return a string representation of this B-tree.
     */
    override fun toString() = toString(root, height, "") + "\n"

    private fun toString(h: Node<Key,Value>, ht: Int, indent: String): String {
        val s = StringBuilder()
        val children = h.children

        if (ht == 0) {
            for (j in 0 until h.m)
                s.append(indent + children[j].key + " " + children[j].`val` + "\n")
        } else
            for (j in 0 until h.m) {
                if (j > 0) s.append("$indent(${children[j].key})\n")
                s.append(toString(children[j].next!!, ht - 1, "$indent     "))
            }
        return s.toString()
    }

    // comparison functions - make Comparable instead of Key to avoid casts
    private fun less(k1: Key, k2: Key) = k1 < k2

    private fun eq(k1: Key, k2: Key) = k1 == k2

    companion object {
        // max children per B-tree node = M-1
        // (must be even and greater than 2)
        private const val M = 4

        /**
         * Unit tests the `BTree` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val st = BTree<String, String>()

            st.put("www.cs.princeton.edu", "128.112.136.12")
            st.put("www.cs.princeton.edu", "128.112.136.11")
            st.put("www.princeton.edu", "128.112.128.15")
            st.put("www.yale.edu", "130.132.143.21")
            st.put("www.simpsons.com", "209.052.165.60")
            st.put("www.apple.com", "17.112.152.32")
            st.put("www.amazon.com", "207.171.182.16")
            st.put("www.ebay.com", "66.135.192.87")
            st.put("www.cnn.com", "64.236.16.20")
            st.put("www.google.com", "216.239.41.99")
            st.put("www.nytimes.com", "199.239.136.200")
            st.put("www.microsoft.com", "207.126.99.140")
            st.put("www.dell.com", "143.166.224.230")
            st.put("www.slashdot.org", "66.35.250.151")
            st.put("www.espn.com", "199.181.135.201")
            st.put("www.weather.com", "63.111.66.11")
            st.put("www.yahoo.com", "216.109.118.65")

            StdOut.println("cs.princeton.edu:  " + st["www.cs.princeton.edu"])
            StdOut.println("hardvardsucks.com: " + st["www.harvardsucks.com"])
            StdOut.println("simpsons.com:      " + st["www.simpsons.com"])
            StdOut.println("apple.com:         " + st["www.apple.com"])
            StdOut.println("ebay.com:          " + st["www.ebay.com"])
            StdOut.println("dell.com:          " + st["www.dell.com"])
            StdOut.println()

            StdOut.println("size:    " + st.size)
            StdOut.println("height:  " + st.height)
            StdOut.println(st)
            StdOut.println()
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