/******************************************************************************
 * Compilation:  javac IndexMinPQ.java
 * Execution:    java IndexMinPQ
 * Dependencies: StdOut.kt
 *
 * Minimum-oriented indexed PQ implementation using a binary heap.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `IndexMinPQ` class represents an indexed priority queue of generic keys.
 * It supports the usual *insert* and *delete-the-minimum*
 * operations, along with *delete* and *change-the-key*
 * methods. In order to let the client refer to keys on the priority queue,
 * an integer between `0` and `maxN - 1`
 * is associated with each key—the client uses this integer to specify
 * which key to delete or change.
 * It also supports methods for peeking at the minimum key,
 * testing if the priority queue is empty, and iterating through
 * the keys.
 *
 *
 * This implementation uses a binary heap along with an array to associate
 * keys with integers in the given range.
 * The *insert*, *delete-the-minimum*, *delete*,
 * *change-key*, *decrease-key*, and *increase-key*
 * operations take logarithmic time.
 * The *is-empty*, *size*, *min-index*, *min-key*,
 * and *key-of* operations take constant time.
 * Construction takes time proportional to the specified capacity.
 *
 *
 * For additional documentation, see [Section 2.4](https://algs4.cs.princeton.edu/24pq) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 * @param <Key> the generic type of key on this priority queue
</Key> */

class IndexMinPQ<Key : Comparable<Key>>
/**
 * Initializes an empty indexed priority queue with indices between `0`
 * and `maxN - 1`.
 * @param  maxN the keys on this priority queue are index from `0`
 * `maxN - 1`
 * @throws IllegalArgumentException if `maxN < 0`
 */
(private val maxN: Int) : Iterable<Int> {
    var size: Int = 0           // number of elements on PQ
        private set
    private val pq: IntArray        // binary heap using 1-based indexing
    private val qp: IntArray        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private val keys: Array<Key?>      // keys[i] = priority of i

    /**
     * Returns true if this priority queue is empty.
     *
     * @return `true` if this priority queue is empty;
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    init {
        if (maxN < 0) throw IllegalArgumentException()
        size = 0
        keys = arrayOfNulls<Comparable<Any>>(maxN + 1) as Array<Key?>    // make this of length maxN??
        pq = IntArray(maxN + 1)
        qp = IntArray(maxN + 1) { -1 }                 // make this of length maxN??
    }

    /**
     * Is `i` an index on this priority queue?
     *
     * @param  i an index
     * @return `true` if `i` is an index on this priority queue;
     * `false` otherwise
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     */
    operator fun contains(i: Int): Boolean {
        if (i < 0 || i >= maxN) throw IllegalArgumentException()
        return qp[i] != -1
    }

    /**
     * Associates key with index `i`.
     *
     * @param  i an index
     * @param  key the key to associate with index `i`
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws IllegalArgumentException if there already is an item associated
     * with index `i`
     */
    fun insert(i: Int, key: Key?) {
        if (key == null) throw IllegalArgumentException("Argument to insert() is null")
        if (i < 0 || i >= maxN) throw IllegalArgumentException()
        if (contains(i)) throw IllegalArgumentException("index is already in the priority queue")
        size++
        qp[i] = size
        pq[size] = i
        keys[i] = key
        swim(size)
    }

    /**
     * Returns an index associated with a minimum key.
     *
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    fun minIndex(): Int {
        if (size == 0) throw NoSuchElementException("Priority queue underflow")
        return pq[1]
    }

    /**
     * Returns a minimum key.
     *
     * @return a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    fun minKey(): Key {
        if (size == 0) throw NoSuchElementException("Priority queue underflow")
        return keys[pq[1]] as Key
    }

    /**
     * Removes a minimum key and returns its associated index.
     * @return an index associated with a minimum key
     * @throws NoSuchElementException if this priority queue is empty
     */
    fun delMin(): Int {
        if (size == 0) throw NoSuchElementException("Priority queue underflow")
        val min = pq[1]
        exch(1, size--)
        sink(1)
        //assert(min == pq[size + 1])
        qp[min] = -1        // delete
        pq[size + 1] = -1        // not needed
        return min
    }

    /**
     * Returns the key associated with index `i`.
     *
     * @param  i the index of the key to return
     * @return the key associated with index `i`
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws NoSuchElementException no key is associated with index `i`
     */
    fun keyOf(i: Int): Key {
        if (i < 0 || i >= maxN) throw IllegalArgumentException()
        return if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        else
            keys[i] as Key
    }

    /**
     * Change the key associated with index `i` to the specified value.
     *
     * @param  i the index of the key to change
     * @param  key change the key associated with index `i` to this key
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws NoSuchElementException no key is associated with index `i`
     */
    fun changeKey(i: Int, key: Key) {
        if (i < 0 || i >= maxN) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        keys[i] = key
        swim(qp[i])
        sink(qp[i])
    }

    /**
     * Change the key associated with index `i` to the specified value.
     *
     * @param  i the index of the key to change
     * @param  key change the key associated with index `i` to this key
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     */
    @Deprecated("Replaced by {@code changeKey(int, Key)}.")
    fun change(i: Int, key: Key) = changeKey(i, key)

    /**
     * Decrease the key associated with index `i` to the specified value.
     *
     * @param  i the index of the key to decrease
     * @param  key decrease the key associated with index `i` to this key
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws IllegalArgumentException if `key >= keyOf(i)`
     * @throws NoSuchElementException no key is associated with index `i`
     */
    fun decreaseKey(i: Int, key: Key) {
        if (i < 0 || i >= maxN) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        if (keys[i] as Key <= key) throw IllegalArgumentException("Calling decreaseKey() with given argument would not strictly decrease the key")
        keys[i] = key
        swim(qp[i])
    }

    /**
     * Increase the key associated with index `i` to the specified value.
     *
     * @param  i the index of the key to increase
     * @param  key increase the key associated with index `i` to this key
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws IllegalArgumentException if `key <= keyOf(i)`
     * @throws NoSuchElementException no key is associated with index `i`
     */
    fun increaseKey(i: Int, key: Key) {
        if (i < 0 || i >= maxN) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        if (keys[i] as Key >= key) throw IllegalArgumentException("Calling increaseKey() with given argument would not strictly increase the key")
        keys[i] = key
        sink(qp[i])
    }

    /**
     * Remove the key associated with index `i`.
     *
     * @param  i the index of the key to remove
     * @throws IllegalArgumentException unless `0 <= i < maxN`
     * @throws NoSuchElementException no key is associated with index `i`
     */
    fun delete(i: Int) {
        if (i < 0 || i >= maxN) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("index is not in the priority queue")
        val index = qp[i]
        exch(index, size--)
        swim(index)
        sink(index)
        qp[i] = -1
    }

    val comparator:Comparator<Key>? = null
    //private fun greater(i: Int, j: Int) = keys[pq[i]] as Key > keys[pq[j]] as Key
    private fun greater(x: Key, y: Key) = if (comparator != null)
            comparator.compare(x, y) > 0
        else {
            val left = x as Comparable<Key>
            left > y
        }

    private fun greater(i: Int, j: Int) = greater(keys[pq[i]]!!, keys[pq[j]]!!)

    private fun exch(i: Int, j: Int) {
        val swap = pq[i]
        pq[i] = pq[j]
        pq[j] = swap
        qp[pq[i]] = i
        qp[pq[j]] = j
    }

    private fun swim(k: Int) {
        var k = k
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2)
            k /= 2
        }
    }

    private fun sink(k: Int) {
        var k = k
        while (2 * k <= size) {
            var j = 2 * k
            if (j < size && greater(j, j + 1)) j++
            if (!greater(k, j)) break
            exch(k, j)
            k = j
        }
    }

    /**
     * Returns an iterator that iterates over the keys on the
     * priority queue in ascending order.
     * The iterator doesn't implement `remove()` since it's optional.
     *
     * @return an iterator that iterates over the keys in ascending order
     */
    override fun iterator(): Iterator<Int> = HeapIterator()

    private inner class HeapIterator : Iterator<Int> {
        // create a new pq
        private val copy: IndexMinPQ<Key> = IndexMinPQ(pq.size - 1)

        // add all elements to copy of heap
        // takes linear time since already in heap order so no keys move
        init {
            for (i in 1..size)
                copy.insert(pq[i], keys[pq[i]])
        }

        override fun hasNext() = !copy.isEmpty

        override fun next(): Int {
            if (!hasNext()) throw NoSuchElementException()
            return copy.delMin()
        }
    }

    companion object {
        /**
         * Unit tests the `IndexMinPQ` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // insert a bunch of strings
            val strings = arrayOf("it", "was", "the", "best", "of", "times", "it", "was", "the", null, "worst")

            val pq = IndexMinPQ<String>(strings.size)
            try {
                for (i in strings.indices)
                    pq.insert(i, strings[i])
            } catch (e: IllegalArgumentException) {
                StdOut.println(e.message)
            }

            // delete and print each key
            while (!pq.isEmpty) {
                val i = pq.delMin()
                StdOut.println("$i ${strings[i]}")
            }
            StdOut.println()

            // reinsert the same strings
            try {
                for (i in strings.indices)
                    pq.insert(i, strings[i])
            } catch (e: IllegalArgumentException) {
                StdOut.println(e.message)
            }

            // print each key using the iterator
            for (i in pq)
                StdOut.println("$i ${strings[i]}")
            while (!pq.isEmpty)
                pq.delMin()
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