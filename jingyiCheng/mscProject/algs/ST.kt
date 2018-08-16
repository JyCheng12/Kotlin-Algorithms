/******************************************************************************
 * Compilation:  javac ST.java
 * Execution:    java ST < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/35applications/tinyST.txt
 *
 * Sorted symbol table implementation using a java.util.TreeMap.
 * Does not allow duplicates.
 *
 */

package jingyiCheng.mscProject.algs

import java.util.TreeMap

/**
 * The `ST` class represents an ordered symbol table of generic
 * key-value pairs.
 * It supports the usual *put*, *get*, *contains*,
 * *delete*, *size*, and *is-empty* methods.
 * It also provides ordered methods for finding the *minimum*,
 * *maximum*, *floor*, and *ceiling*.
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
 * This implementation uses a balanced binary search tree. It requires that
 * the key type implements the `Comparable` interface and calls the
 * `compareTo()` and method to compare two keys. It does not call either
 * `equals()` or `hashCode()`.
 * The *put*, *contains*, *remove*, *minimum*,
 * *maximum*, *ceiling*, and *floor* operations each take
 * logarithmic time in the worst case.
 * The *size*, and *is-empty* operations take constant time.
 * Construction takes constant time.
 *
 *
 * For additional documentation, see [Section 3.5](https://algs4.cs.princeton.edu/35applications) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 *
 * @param <Key> the generic type of keys in this symbol table
 * @param <Value> the generic type of values in this symbol table
</Value></Key> */
class ST<Key : Comparable<Key>, Value> {
    private val st: TreeMap<Key, Value> = TreeMap()
    var size = 0
        get() = st.size
        private set

    /**
     * Returns true if this symbol table is empty.
     *
     * @return `true` if this symbol table is empty and `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    /**
     * Returns the value associated with the given key in this symbol table.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in this symbol table;
     * `null` if the key is not in this symbol table
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun get(key: Key?): Value {
        if (key == null) throw IllegalArgumentException("calls get() with null key")
        else return st[key] ?: throw NoSuchElementException("Key not contained in this table")
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
        if (key == null) throw IllegalArgumentException("calls put() with null key")
        if (`val` == null)
            st.remove(key)
        else
            st[key] = `val`
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param  key the key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun delete(key: Key?) {
        if (key == null) throw IllegalArgumentException("calls delete() with null key")
        st.remove(key)
    }

    /**
     * Returns true if this symbol table contain the given key.
     *
     * @param  key the key
     * @return `true` if this symbol table contains `key` and
     * `false` otherwise
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun contains(key: Key?): Boolean {
        if (key == null) throw IllegalArgumentException("calls contains() with null key")
        return st.containsKey(key)
    }

    /**
     * Returns all keys in this symbol table.
     *
     * To iterate over all of the keys in the symbol table named `st`,
     * use the foreach notation: `for (Key key : st.keys())`.
     *
     * @return all keys in this symbol table
     */
    fun keys() = st.keys

    /**
     * Returns the smallest key in this symbol table.
     *
     * @return the smallest key in this symbol table
     * @throws NoSuchElementException if this symbol table is empty
     */
    fun min(): Key {
        if (isEmpty) throw NoSuchElementException("calls min() with empty symbol table")
        return st.firstKey()
    }

    /**
     * Returns the largest key in this symbol table.
     *
     * @return the largest key in this symbol table
     * @throws NoSuchElementException if this symbol table is empty
     */
    fun max(): Key {
        if (isEmpty) throw NoSuchElementException("calls max() with empty symbol table")
        return st.lastKey()
    }

    /**
     * Returns the smallest key in this symbol table greater than or equal to `key`.
     *
     * @param  key the key
     * @return the smallest key in this symbol table greater than or equal to `key`
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun ceiling(key: Key?): Key {
        if (key == null) throw IllegalArgumentException("argument to ceiling() is null")
        return st.ceilingKey(key) ?: throw NoSuchElementException("all keys are less than $key")
    }

    /**
     * Returns the largest key in this symbol table less than or equal to `key`.
     *
     * @param  key the key
     * @return the largest key in this symbol table less than or equal to `key`
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun floor(key: Key?): Key {
        if (key == null) throw IllegalArgumentException("argument to floor() is null")
        return st.floorKey(key) ?: throw NoSuchElementException("all keys are greater than $key")
    }

    companion object {
        /**
         * Unit tests the `ST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val st = ST<String, Int>()
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

            val key = st.keys()
            StdOut.println(key)
            st.keys().forEach {
                StdOut.println(it)
                st.delete(it)
                StdOut.println("$it deleted")
            }
            StdOut.println("Checking isEmpty ... ${st.isEmpty}")
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