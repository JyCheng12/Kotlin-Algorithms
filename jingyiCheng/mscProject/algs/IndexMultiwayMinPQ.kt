/******************************************************************************
 * Compilation: javac IndexMultiwayMinPQ.java
 * Execution:
 *
 * An indexed multiway heap.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The IndexMultiwayMinPQ class represents an indexed priority queue of generic keys.
 * It supports the usual insert and delete-the-minimum operations,
 * along with delete and change-the-key methods.
 * In order to let the client refer to keys on the priority queue,
 * an integer between 0 and N-1 is associated with each key ; the client
 * uses this integer to specify which key to delete or change.
 * It also supports methods for peeking at the minimum key,
 * testing if the priority queue is empty, and iterating through
 * the keys.
 *
 * This implementation uses a multiway heap along with an array to associate
 * keys with integers in the given range.
 * For simplified notations, logarithm in base d will be referred as log-d
 * The delete-the-minimum, delete, change-key and increase-key operations
 * take time proportional to d*log-d(n)
 * The insert and decrease-key take time proportional to log-d(n)
 * The is-empty, min-index, min-key, size, contains and key-of operations take constant time.
 * Construction takes time proportional to the specified capacity.
 *
 * The arrays used in this structure have the first d indices empty,
 * it apparently helps with caching effects.
 *
 * @author Tristan Claverie
 * @author Jingyi Cheng
 *
 */
@Suppress("UNCHECKED_CAST")
class IndexMultiwayMinPQ<Key> : Iterable<Int> {
    private val d: Int                //Dimension of the heap
    var size: Int = 0                        //Number of keys currently in the queue
        private set
    private var nmax: Int = 0                    //Maximum number of items in the queue
    private var pq: IntArray                   //Multiway heap
    private var qp: IntArray                 //Inverse of pq : qp[pq[i]] = pq[qp[i]] = i
    private var keys: Array<Any?>                   //keys[i] = priority of i
    private val comp: Comparator<Key> //Comparator over the keys

    /**
     * Whether the priority queue is empty
     * Worst case is O(1)
     * @return true if the priority queue is empty, false if not
     */
    val isEmpty: Boolean
        get() = size == 0

    /**
     * Initializes an empty indexed priority queue with indices between `0` to `N-1`
     * Worst case is O(n)
     * @param N number of keys in the priority queue, index from `0` to `N-1`
     * @param D dimension of the heap
     * @throws IllegalArgumentException if `N < 0`
     * @throws IllegalArgumentException if `D < 2`
     */
    constructor(N: Int, D: Int) {
        if (N < 0) throw IllegalArgumentException("Maximum number of elements cannot be negative")
        if (D < 2) throw IllegalArgumentException("Dimension should be 2 or over")
        this.d = D
        nmax = N
        pq = IntArray(nmax + D)
        qp = IntArray(nmax + D) { -1 }
        keys = arrayOfNulls(nmax + D)
        comp = MyComparator()
    }

    /**
     * Initializes an empty indexed priority queue with indices between `0` to `N-1`
     * Worst case is O(n)
     * @param N number of keys in the priority queue, index from `0` to `N-1`
     * @param D dimension of the heap
     * @param C a Comparator over the keys
     * @throws IllegalArgumentException if `N < 0`
     * @throws IllegalArgumentException if `D < 2`
     */
    constructor(N: Int, C: Comparator<Key>, D: Int) {
        if (N < 0) throw IllegalArgumentException("Maximum number of elements cannot be negative")
        if (D < 2) throw IllegalArgumentException("Dimension should be 2 or over")
        this.d = D
        nmax = N
        pq = IntArray(nmax + D)
        qp = IntArray(nmax + D) { -1 }
        keys = arrayOfNulls(nmax + D)
        comp = C
    }

    /**
     * Does the priority queue contains the index i ?
     * Worst case is O(1)
     * @param i an index
     * @throws IllegalArgumentException if the specified index is invalid
     * @return true if i is on the priority queue, false if not
     */
    operator fun contains(i: Int): Boolean {
        if (i < 0 || i >= nmax) throw IllegalArgumentException()
        return qp[i + d] != -1
    }

    /**
     * Associates a key with an index
     * Worst case is O(log-d(n))
     * @param i an index
     * @param key a Key associated with i
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws IllegalArgumentException if the index is already in the queue
     */
    fun insert(i: Int, key: Key) {
        if (i < 0 || i >= nmax) throw IllegalArgumentException()
        if (contains(i)) throw IllegalArgumentException("Index already there")
        keys[i + d] = key
        pq[size + d] = i
        qp[i + d] = size
        swim(size++)
    }

    /**
     * Gets the index associated with the minimum key
     * Worst case is O(1)
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the index associated with the minimum key
     */
    fun minIndex(): Int {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        return pq[d]
    }

    /**
     * Gets the minimum key currently in the queue
     * Worst case is O(1)
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the minimum key currently in the priority queue
     */
    fun minKey(): Key {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        return keys[pq[d] + d] as Key
    }

    /**
     * Deletes the minimum key
     * Worst case is O(d*log-d(n))
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the index associated with the minimum key
     */
    fun delMin(): Int {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        val min = pq[d]
        exch(0, --size)
        sink(0)
        qp[min + d] = -1
        pq[size + d] = -1
        return min
    }

    /**
     * Gets the key associated with index i
     * Worst case is O(1)
     * @param i an index
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws IllegalArgumentException if the index is not in the queue
     * @return the key associated with index i
     */
    fun keyOf(i: Int): Key {
        if (i < 0 || i >= nmax) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        return keys[i + d] as Key
    }

    /**
     * Changes the key associated with index i to the given key
     * If the given key is greater, Worst case is O(d*log-d(n))
     * If the given key is lower,   Worst case is O(log-d(n))
     * @param i an index
     * @param key the key to associate with i
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws IllegalArgumentException if the index has no key associated with
     */
    fun changeKey(i: Int, key: Key) {
        if (i < 0 || i >= nmax) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        val tmp = keys[i + d]
        keys[i + d] = key
        if (comp.compare(key, tmp as Key) <= 0) swim(qp[i + d])
        else sink(qp[i + d])
    }

    /**
     * Decreases the key associated with index i to the given key
     * Worst case is O(log-d(n))
     * @param i an index
     * @param key the key to associate with i
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws java.util.NoSuchElementException if the index has no key associated with
     * @throws IllegalArgumentException if the given key is greater than the current key
     */
    fun decreaseKey(i: Int, key: Key) {
        if (i < 0 || i >= nmax) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        if (comp.compare(keys[i + d] as Key, key) <= 0) throw IllegalArgumentException("Calling with this argument would not decrease the Key")
        keys[i + d] = key
        swim(qp[i + d])
    }

    /**
     * Increases the key associated with index i to the given key
     * Worst case is O(d*log-d(n))
     * @param i an index
     * @param key the key to associate with i
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws java.util.NoSuchElementException if the index has no key associated with
     * @throws IllegalArgumentException if the given key is lower than the current key
     */
    fun increaseKey(i: Int, key: Key) {
        if (i < 0 || i >= nmax) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        if (comp.compare(keys[i + d] as Key, key) >= 0) throw IllegalArgumentException("Calling with this argument would not increase the Key")
        keys[i + d] = key
        sink(qp[i + d])
    }

    /**
     * Deletes the key associated to the given index
     * Worst case is O(d*log-d(n))
     * @param i an index
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws java.util.NoSuchElementException if the given index has no key associated with
     */
    fun delete(i: Int) {
        if (i < 0 || i >= nmax) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        val idx = qp[i + d]
        exch(idx, --size)
        swim(idx)
        sink(idx)
        qp[i + d] = -1
    }

    //Compares two keys
    private fun greater(i: Int, j: Int) = comp.compare(keys[pq[i + d] + d] as Key, keys[pq[j + d] + d] as Key) > 0

    //Exchanges two keys
    private fun exch(x: Int, y: Int) {
        val i = x + d
        val j = y + d
        val swap = pq[i]
        pq[i] = pq[j]
        pq[j] = swap
        qp[pq[i] + d] = x
        qp[pq[j] + d] = y
    }

    //Moves upward
    private fun swim(i: Int) {
        if (i > 0 && greater((i - 1) / d, i)) {
            exch(i, (i - 1) / d)
            swim((i - 1) / d)
        }
    }

    //Moves downward
    private fun sink(i: Int) {
        var i = i
        if (d * i + 1 >= size) return
        var min = minChild(i)
        while (min < size && greater(i, min)) {
            exch(i, min)
            i = min
            min = minChild(i)
        }
    }

    //Return the minimum child of i
    private fun minChild(i: Int): Int {
        val loBound = d * i + 1
        val hiBound = d * i + d
        var min = loBound
        for (cur in loBound..hiBound)
            if (cur < size && greater(min, cur)) min = cur
        return min
    }

    /**
     * Gets an Iterator over the indexes in the priority queue in ascending order
     * The Iterator does not implement the remove() method
     * iterator() : Worst case is O(n)
     * next() : 	Worst case is O(d*log-d(n))
     * hasNext() : 	Worst case is O(1)
     * @return an Iterator over the indexes in the priority queue in ascending order
     */

    override fun iterator(): Iterator<Int> = MyIterator()

    //Constructs an Iterator over the indices in linear time
    private inner class MyIterator : Iterator<Int> {
        internal var clone: IndexMultiwayMinPQ<Key> = IndexMultiwayMinPQ(nmax, comp, d)

        init {
            for (i in 0 until size)
                clone.insert(pq[i + d], keys[pq[i + d] + d] as Key)
        }

        override fun hasNext() = !clone.isEmpty

        override fun next(): Int {
            if (!hasNext()) throw NoSuchElementException()
            return clone.delMin()
        }
    }

    //default Comparator
    private inner class MyComparator : Comparator<Key> {
        override fun compare(key1: Key, key2: Key) = (key1 as Comparable<Key>).compareTo(key2)
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
