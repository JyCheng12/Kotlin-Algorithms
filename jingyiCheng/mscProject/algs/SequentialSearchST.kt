/******************************************************************************
 * Compilation:  javac SequentialSearchST.java
 * Execution:    java SequentialSearchST
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/31elementary/tinyST.txt
 *
 * Symbol table implementation with sequential search in an
 * unordered linked list of key-value pairs.
 *
 * % more tinyST.txt
 * S E A R C H E X A M P L E
 *
 * % java SequentialSearchST < tiny.txt
 * L 11
 * P 10
 * M 9
 * X 7
 * H 5
 * C 4
 * R 3
 * A 8
 * E 12
 * S 0
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `SequentialSearchST` class represents an (unordered)
 * symbol table of generic key-value pairs.
 * It supports the usual *put*, *get*, *contains*,
 * *delete*, *size*, and *is-empty* methods.
 * It also provides a *keys* method for iterating over all of the keys.
 * A symbol table implements the *associative array* abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * The class also uses the convention that values cannot be `null`. Setting the
 * value associated with a key to `null` is equivalent to deleting the key
 * from the symbol table.
 *
 *
 * This implementation uses a singly-linked list and sequential search.
 * It relies on the `equals()` method to test whether two keys
 * are equal. It does not call either the `compareTo()` or
 * `hashCode()` method.
 * The *put* and *delete* operations take linear time; the
 * *get* and *contains* operations takes linear time in the worst case.
 * The *size*, and *is-empty* operations take constant time.
 * Construction takes constant time.
 *
 *
 * For additional documentation, see [Section 3.1](https://algs4.cs.princeton.edu/31elementary) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
/**
 * Initializes an empty symbol table.
 */
class SequentialSearchST<Key : Comparable<Key>, Value> {
    var size: Int = 0           // number of key-value pairs
        private set
    private var first: Node? = null      // the linked list of key-value pairs

    /**
     * Returns true if this symbol table is empty.
     *
     * @return `true` if this symbol table is empty;
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // a helper linked list data type
    private inner class Node(val key: Key, var `val`: Value, var next: Node?)

    /**
     * Returns true if this symbol table contains the specified key.
     *
     * @param  key the key
     * @return `true` if this symbol table contains `key`;
     * `false` otherwise
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun contains(key: Key?): Boolean {
        if (key == null) throw IllegalArgumentException("argument to contains() is null")
        return get(key) != null
    }

    /**
     * Returns the value associated with the given key in this symbol table.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and `null` if the key is not in the symbol table
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun get(key: Key?): Value? {
        if (key == null) throw IllegalArgumentException("argument to get() is null")
        var x = first
        while (x != null) {
            if (key == x.key)
                return x.`val`
            x = x.next
        }
        return null
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is `null`.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun put(key: Key?, `val`: Value?) {
        if (key == null) throw IllegalArgumentException("first argument to put() is null")
        if (`val` == null) {
            delete(key)
            return
        }

        var x = first
        while (x != null) {
            if (key == x.key) {
                x.`val` = `val`
                return
            }
            x = x.next
        }
        first = Node(key, `val`, first)
        size++
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param  key the key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun delete(key: Key?) {
        if (key == null) throw IllegalArgumentException("argument to delete() is null")
        first = delete(first, key)
    }

    // delete key in linked list beginning at Node x
    // warning: function call stack too large if table is large
    private fun delete(x: Node?, key: Key): Node? {
        if (x == null) return null
        if (key == x.key) {
            size--
            return x.next
        }
        x.next = delete(x.next, key)
        return x
    }

    /**
     * Returns all keys in the symbol table as an `Iterable`.
     * To iterate over all of the keys in the symbol table named `st`,
     * use the foreach notation: `for (Key key : st.keys())`.
     *
     * @return all keys in the symbol table
     */
    fun keys(): Iterable<Key> {
        val queue = nnQueue<Key>()
        var x = first
        while (x != null) {
            queue.enqueue(x.key)
            x = x.next
        }
        return queue
    }

    companion object {
        /**
         * Unit tests the `SequentialSearchST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val st = SequentialSearchST<String, Int>()
            StdOut.println("Checking isEmpty ... "+st.isEmpty)
            val items = arrayOf("A", "B", "C", "D", "E", "F", null, "G")
            try {
                for ((i, key) in items.withIndex())
                    st.put(key, i)
            }catch(e:IllegalArgumentException){
                StdOut.println(e.message)
            }
            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Checking put('A', 10) ...")
            st.put("A", 10)
            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Checking delete('E') ...")
            st.delete("E")
            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Deleting all elements ...")
            for (s in st.keys())
                st.delete(s)

            StdOut.println("Checking isEmpty ... "+st.isEmpty)

            StdOut.println("End of test")
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