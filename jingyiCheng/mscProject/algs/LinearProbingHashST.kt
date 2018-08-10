/******************************************************************************
 * Compilation:  javac LinearProbingHashST.java
 * Execution:    java LinearProbingHashST < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/34hash/tinyST.txt
 *
 * Symbol-table implementation with linear-probing hash table.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LinearProbingHashST` class represents a symbol table of generic
 * key-value pairs.
 * It supports the usual *put*, *get*, *contains*,
 * *delete*, *size*, and *is-empty* methods.
 * It also provides a *keys* method for iterating over all of the keys.
 * A symbol table implements the *associative array* abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike [java.util.Map], this class uses the convention that
 * values cannot be `null`—setting the
 * value associated with a key to `null` is equivalent to deleting the key
 * from the symbol table.
 *
 *
 * This implementation uses a linear probing hash table. It requires that
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
 * [SeparateChainingHashST],
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
@Suppress("UNCHECKED_CAST")
class LinearProbingHashST<Key : Comparable<Key>, Value>
/**
 * Initializes an empty symbol table with the specified initial capacity.
 *
 * @param m the initial capacity
 */
@JvmOverloads constructor(private var m: Int = INIT_CAPACITY           // size of linear probing table
) {
    private var size: Int = 0           // number of key-value pairs in the symbol table
    private var keys = arrayOfNulls<Comparable<Key>?>(m)     // the keys
    private var vals= arrayOfNulls<Any?>(m)   // the values

    /**
     * Returns true if this symbol table is empty.
     *
     * @return `true` if this symbol table is empty;
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

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

    // hash function for keys - returns value between 0 and M-1
    private fun hash(key: Key?): Int {
        if (key == null) throw IllegalArgumentException("argument to hash() is null")
        return (key.hashCode() and 0x7fffffff) % m
    }

    // resizes the hash table to the given capacity by re-hashing all of the keys
    private fun resize(capacity: Int) {
        val temp = LinearProbingHashST<Key, Value>(capacity)
        for (i in 0 until m) {
            if (keys[i] != null) {
                temp.put(keys[i] as Key, vals[i] as Value)
            }
        }
        keys = temp.keys
        vals = temp.vals
        m = temp.m
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

        // double table size if 50% full
        if (size >= m / 2) resize(2 * m)

        var i = hash(key)
        //for (i = 0; i < keys.size; i++){
        //while(i<keys.size){
        while (vals[i] != null) {
            if (keys[i] == key) {
                vals[i] = `val`
                return
            }
            i = (i + 1) % m
        }
        keys[i] = key
        vals[i] = `val`
        size++
    }

    /**
     * Returns the value associated with the specified key.
     * @param key the key
     * @return the value associated with `key`;
     * `null` if no such value
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun get(key: Key?): Value? {
        if (key == null) throw IllegalArgumentException("argument to get() is null")
        var i = hash(key)
        while (vals[i] != null) {
            if (keys[i] == key)
                return vals[i] as Value?
            i = (i + 1) % m
        }
        return null
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
        if (!contains(key)) return

        // find position i of key
        var i = hash(key)
        while (key != keys[i])
            i = (i + 1) % m

        // delete key and associated value
        vals[i] = null

        // rehash all keys in same cluster
        i = (i + 1) % m
        while (vals[i] != null) {
            // delete keys[i] an vals[i] and reinsert
            val keyToRehash = keys[i]
            val valToRehash = vals[i]
            vals[i] = null
            size--
            put(keyToRehash as Key, valToRehash as Value)
            i = (i + 1) % m
        }
        size--

        // halves size of array if it's 12.5% full or less
        if (size > 0 && size <= m / 8) resize(m / 2)
        assert(check())
    }

    /**
     * Returns all keys in this symbol table as an `Iterable`.
     * To iterate over all of the keys in the symbol table named `st`,
     * use the foreach notation: `for (Key key : st.keys())`.
     *
     * @return all keys in this symbol table
     */
    fun keys(): Iterable<Key> {
        val queue = nnQueue<Key>()
        for (i in 0 until m)
            if (vals[i] != null) queue.enqueue(keys[i] as Key)
        return queue
    }

    // integrity check - don't check after each put() because
    // integrity not maintained during a delete()
    private fun check(): Boolean {
        // check that hash table is at most 50% full
        if (m < 2 * size) {
            System.err.println("Hash table size m = $m; array size n = $size")
            return false
        }

        // check that each key in table can be found by get()
        for (i in 0 until m)
            if (vals[i] == null)
                continue
            else if (get(keys[i] as Key) !== vals[i]) {
                System.err.println("get[${keys[i]}] = ${get(keys[i] as Key)}; vals[i] = ${vals[i]}")
                return false
            }
        return true
    }

    companion object {
        private const val INIT_CAPACITY = 4

        /**
         * Unit tests the `LinearProbingHashST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val st = LinearProbingHashST<String, Int>()
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
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
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
 ******************************************************************************/
