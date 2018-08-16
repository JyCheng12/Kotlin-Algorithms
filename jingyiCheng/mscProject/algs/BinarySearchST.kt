/******************************************************************************
 * Compilation:  javac BinarySearchST.java
 * Execution:    java BinarySearchST
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/31elementary/tinyST.txt
 *
 * Symbol table implementation with binary search in an ordered array.
 *
 * % more tinyST.txt
 * S E A R C H E X A M P L E
 *
 * % java BinarySearchST < tinyST.txt
 * A 8
 * C 4
 * E 12
 * H 5
 * L 11
 * M 9
 * P 10
 * R 3
 * S 0
 * X 7
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `BST` class represents an ordered symbol table of generic
 * key-value pairs.
 * It supports the usual *put*, *get*, *contains*,
 * *delete*, *size*, and *is-empty* methods.
 * It also provides ordered methods for finding the *minimum*,
 * *maximum*, *floor*, *select*, and *ceiling*.
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
 * This implementation uses a sorted array. It requires that
 * the key type implements the `Comparable` interface and calls the
 * `compareTo()` and method to compare two keys. It does not call either
 * `equals()` or `hashCode()`.
 * The *put* and *remove* operations each take linear time in
 * the worst case; the *contains*, *ceiling*, *floor*,
 * and *rank* operations take logarithmic time; the *size*,
 * *is-empty*, *minimum*, *maximum*, and *select*
 * operations take constant time. Construction takes constant time.
 *
 *
 * For additional documentation, see [Section 3.1](https://algs4.cs.princeton.edu/31elementary) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 * For other implementations, see [ST], [BST],
 * [SequentialSearchST], [RedBlackBST],
 * [SeparateChainingHashST], and [LinearProbingHashST],
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Jingyi Cheng
 *
 */

@Suppress("UNCHECKED_CAST")
class BinarySearchST<Key : Comparable<Key>, Value>
/**
 * Initializes an empty symbol table with the specified initial capacity.
 * @param capacity the maximum capacity
 */
constructor(capacity: Int = INIT_CAPACITY) {
    private var keys: Array<Comparable<Key>?> = arrayOfNulls(capacity)
    private var values: Array<Any?> = arrayOfNulls(capacity)
    var size = 0
        private set

    /**
     * Returns true if this symbol table is empty.
     *
     * @return `true` if this symbol table is empty;
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // are the items in the array in ascending order?
    private val isSorted: Boolean
        get() {
            for (i in 1 until size)
                if ((keys[i] as Key) < (keys[i - 1] as Key)) return false
            return true
        }

    // resize the underlying arrays
    private fun resize(capacity: Int) {
        assert(capacity >= size)
        values = values.copyOf(capacity)
        keys = keys.copyOf(capacity)
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param  key the key
     * @return `true` if this symbol table contains `key` and
     * `false` otherwise
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun contains(key: Key?) = get(key ?: throw IllegalArgumentException("argument to contains() is null")) != null

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
        if (isEmpty) return null
        val i = rank(key)
        return if (i < size && keys[i] == key) values[i] as Value? else null
    }

    /**
     * Returns the number of keys in this symbol table strictly less than `key`.
     *
     * @param  key the key
     * @return the number of keys in the symbol table strictly less than `key`
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun rank(key: Key?): Int {
        if (key == null) throw IllegalArgumentException("argument to rank() is null")

        var lo = 0
        var hi = size - 1
        while (lo <= hi) {
            val mid = (hi + lo) / 2
            when {
                key < (keys[mid] as Key) -> hi = mid - 1
                key > (keys[mid] as Key) -> lo = mid + 1
                else -> return mid
            }
        }
        return lo
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is `null`.
     *
     * @param  key the key
     * @param  value the value
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun put(key: Key?, value: Value?) {
        if (key == null) throw IllegalArgumentException("first argument to put() is null")

        if (value == null) {
            delete(key)
            return
        }

        val i = rank(key)      // key is already in table
        if (i < size && keys[i] == key) {
            values[i] = value
            return
        }

        // insert new key-value pair
        if (size == keys.size) resize(2 * keys.size)
        for (j in size downTo i+1) {
            keys[j] = keys[j - 1]
            values[j] = values[j - 1]
        }
        keys[i] = key
        values[i] = value
        size++
        assert(check())
    }

    /**
     * Removes the specified key and associated value from this symbol table
     * (if the key is in the symbol table).
     *
     * @param  key the key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun delete(key: Key?) {
        if (key == null) throw IllegalArgumentException("argument to delete() is null")
        if (isEmpty) return

        // compute rank
        val i = rank(key)

        // key not in table
        if (i == size || keys[i] != key) return

        (i..size - 2).forEach { j ->
            keys[j] = keys[j + 1]
            values[j] = values[j + 1]
        }

        size--
        values[size] = null

        // resize if 1/4 full
        if (size > 0 && size == keys.size / 4) resize(keys.size / 2)
        assert(check())
    }

    /**
     * Removes the smallest key and associated value from this symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun deleteMin() {
        if (isEmpty) throw NoSuchElementException("Symbol table underflow error")
        delete(min())
    }

    /**
     * Removes the largest key and associated value from this symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun deleteMax() {
        if (isEmpty) throw NoSuchElementException("Symbol table underflow error")
        delete(max())
    }

    /**
     * Returns the smallest key in this symbol table.
     *
     * @return the smallest key in this symbol table
     * @throws NoSuchElementException if this symbol table is empty
     */
    fun min(): Key {
        if (isEmpty) throw NoSuchElementException("called min() with empty symbol table")
        return keys[0] as Key
    }

    /**
     * Returns the largest key in this symbol table.
     *
     * @return the largest key in this symbol table
     * @throws NoSuchElementException if this symbol table is empty
     */
    fun max(): Key {
        if (isEmpty) throw NoSuchElementException("called max() with empty symbol table")
        return keys[size - 1] as Key
    }

    /**
     * Return the kth smallest key in this symbol table.
     *
     * @param  k the order statistic
     * @return the `k`th smallest key in this symbol table
     * @throws IllegalArgumentException unless `k` is between 0 and
     * *n*–1
     */
    fun select(k: Int): Key {
        if (k < 0 || k >= size) throw IllegalArgumentException("called select() with invalid argument: $k")
        return keys[k] as Key
    }

    /**
     * Returns the largest key in this symbol table less than or equal to `key`.
     *
     * @param  key the key
     * @return the largest key in this symbol table less than or equal to `key`
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun floor(key: Key?): Key? {
        if (key == null) throw IllegalArgumentException("argument to floor() is null")
        val i = rank(key)
        if (i < size && key == keys[i]) return keys[i] as Key
        return if (i == 0)
            null
        else
            keys[i - 1] as Key
    }

    /**
     * Returns the smallest key in this symbol table greater than or equal to `key`.
     *
     * @param  key the key
     * @return the smallest key in this symbol table greater than or equal to `key`
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun ceiling(key: Key?): Key? {
        if (key == null) throw IllegalArgumentException("argument to ceiling() is null")
        val i = rank(key)
        return if (i == size)
            null
        else
            keys[i] as Key
    }

    /**
     * Returns the number of keys in this symbol table in the specified range.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return the number of keys in this symbol table between `lo`
     * (inclusive) and `hi` (inclusive)
     * @throws IllegalArgumentException if either `lo` or `hi`
     * is `null`
     */
    fun size(lo: Key?, hi: Key?): Int {
        if (lo == null) throw IllegalArgumentException("first argument to size() is null")
        if (hi == null) throw IllegalArgumentException("second argument to size() is null")

        if (lo > hi) return 0
        return if (contains(hi))
            rank(hi) - rank(lo) + 1
        else
            rank(hi) - rank(lo)
    }

    /**
     * Returns all the keys in this symbol table as a `nnQueue`
     *
     * @return all keys in this symbol table, an empty queue if
     * the table is empty
     */
    fun keys(): nnQueue<Key> = try {
        keys(min(), max())
    } catch (e: NoSuchElementException) {
        nnQueue()
    }

    /**
     * Returns all keys in this symbol table in the given range,
     * as a `nnQueue`.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return all keys in this symbol table between `lo`
     * (inclusive) and `hi` (inclusive)
     * @throws IllegalArgumentException if either `lo` or `hi`
     * is `null`
     */
    fun keys(lo: Key?, hi: Key?): nnQueue<Key> {
        if (lo == null) throw IllegalArgumentException("first argument to keys() is null")
        if (hi == null) throw IllegalArgumentException("second argument to keys() is null")

        val queue = nnQueue<Key>()
        if (lo > hi) return queue
        for (i in rank(lo) until rank(hi))
            queue.enqueue(keys[i] as Key)
        if (contains(hi)) queue.enqueue(keys[rank(hi)] as Key)
        return queue
    }

    fun check(): Boolean = isSorted && rankCheck()

    // check that rank(select(i)) = i
    private fun rankCheck(): Boolean {
        for (i in 0 until size)
            if (i != rank(select(i))) return false
        for (i in 0 until size)
            if (keys[i] != select(rank(keys[i] as Key))) return false
        return true
    }

    companion object {
        private const val INIT_CAPACITY = 2

        /**
         * Unit tests the `BinarySearchST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val st = BinarySearchST<String, Int>()
            StdOut.println("Checking isEmpty ... " + st.isEmpty)
            val items = arrayOf("A", "B", "C", "D", "E", "F", null, "G")
            try {
                for ((i, key) in items.withIndex())
                    st.put(key, i)
            } catch (e: IllegalArgumentException) {
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

            StdOut.print("Checking size('B', 'D') ... ")
            StdOut.println(st.size("B", "D"))

            StdOut.print("Checking size('B', 'F') ... ")
            StdOut.println(st.size("B", "F"))

            StdOut.print("Checking ceiling('D') ... ")
            StdOut.println(st.ceiling("D"))

            StdOut.print("Checking floor('E') ... ")
            StdOut.println(st.floor("E"))

            StdOut.print("Checking ceiling('E') ... ")
            StdOut.println(st.ceiling("E"))

            StdOut.print("Checking rank('E') ... ")
            StdOut.println(st.rank("E"))

            StdOut.print("Checking select(2) ... ")
            StdOut.println(st.select(2))

            StdOut.println("Checking isEmpty ... " + st.isEmpty)


            StdOut.println("Deleting max... ${st.max()}")
            st.deleteMax()

            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Deleting min... ${st.min()}")
            st.deleteMin()

            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Deleting max and min...${st.max()} and ${st.min()}")
            st.delete(st.max())
            st.delete(st.min())
            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Deleting last element in table ... ${st.min()}")
            st.put("C", null)
            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")
            StdOut.println("Checking isEmpty ... " + st.isEmpty)

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