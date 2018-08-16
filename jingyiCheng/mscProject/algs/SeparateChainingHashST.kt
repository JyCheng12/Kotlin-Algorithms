/******************************************************************************
 * Compilation:  javac SeparateChainingHashST.java
 * Execution:    java SeparateChainingHashST < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/34hash/tinyST.txt
 *
 * A symbol table implemented with a separate-chaining hash table.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `SeparateChainingHashST` class represents a symbol table of generic
 * key-value pairs.
 * It supports the usual *put*, *get*, *contains*,
 * *delete*, *size*, and *is-empty* methods.
 * It also provides a *keys* method for iterating over all of the keys.
 * A symbol table implements the *associative array* abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike [Map], this class uses the convention that
 * values cannot be `null`—setting the
 * value associated with a key to `null` is equivalent to deleting the key
 * from the symbol table.
 *
 *
 * This implementation uses a separate chaining hash table. It requires that
 * the key type overrides the `equals()` and `hashCode()` methods.
 * The expected time per *put*, *contains*, or *remove*
 * operation is constant, subject to the uniform hashing assumption.
 * The *size*, and *is-empty* operations take constant time.
 * Construction takes constant time.
 *
 *
 * For additional documentation, see [Section 3.4](https://algs4.cs.princeton.edu/34hash) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 * For other implementations, see [ST], [BinarySearchST],
 * [SequentialSearchST], [BST], [RedBlackBST], and
 * [LinearProbingHashST],
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class SeparateChainingHashST<Key : Comparable<Key>, Value>
/**
 * Initializes an empty symbol table with `m` chains.
 * @param m the initial number of chains
 */
@JvmOverloads constructor(private var m: Int = INIT_CAPACITY                                // hash table size
) {
    var size: Int = 0 // number of key-value pairs
        private set
    private var st: Array<SequentialSearchST<Key, Value>> = Array(m) { SequentialSearchST<Key, Value>() } // array of linked-list symbol tables

    /**
     * Returns true if this symbol table is empty.
     *
     * @return `true` if this symbol table is empty;
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // resize the hash table to have the given number of chains,
    // rehashing all of the keys
    private fun resize(chains: Int) {
        val temp = SeparateChainingHashST<Key, Value>(chains)
        for (i in 0 until m)
            for (key in st[i].keys())
                temp.put(key, st[i][key])
        this.m = temp.m
        this.size = temp.size
        this.st = temp.st
    }

    // hash value between 0 and m-1
    private fun hash(key: Key?): Int {
        if (key == null) throw IllegalArgumentException("argument to contains() is null")
        return (key.hashCode() and 0x7fffffff) % m
    }

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
     * Returns the value associated with the specified key in this symbol table.
     *
     * @param  key the key
     * @return the value associated with `key` in the symbol table;
     * `null` if no such value
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun get(key: Key?): Value? {
        val i = hash(key ?: throw IllegalArgumentException("argument to get() is null"))
        return st[i][key]
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

        // double table size if average length of list >= 10
        if (size >= 10 * m) resize(2 * m)
        val i = hash(key)
        if (!st[i].contains(key)) size++
        st[i].put(key, `val`)
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

        val i = hash(key)
        if (st[i].contains(key)) size--
        st[i].delete(key)

        // halve table size if average length of list <= 2
        if (m > INIT_CAPACITY && size <= 2 * m) resize(m / 2)
    }

    // return keys in symbol table as an Iterable
    fun keys(): Iterable<Key> {
        val queue = nnQueue<Key>()
        for (i in 0 until m)
            for (key in st[i].keys())
                queue.enqueue(key)
        return queue
    }

    companion object {
        private const val INIT_CAPACITY = 4

        /**
         * Unit tests the `SeparateChainingHashST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val st = SeparateChainingHashST<String, Int>()
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

            StdOut.println("Checking put('A', 10) ... \nOutput in keysLevelOrder() ...")
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