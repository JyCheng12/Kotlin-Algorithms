/******************************************************************************
 * Compilation: javac MultiwayMinPQ.java
 * Execution:
 *
 * A multiway heap.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The MultiwayMinPQ class represents a priority queue of generic keys.
 * It supports the usual insert and delete-the-minimum operations.
 * It also supports methods for peeking at the minimum key,
 * testing if the priority queue is empty, and iterating through
 * the keys.
 * It is possible to build the priority queue using a Comparator.
 * If not, the natural order relation between the keys will be used.
 *
 * This implementation uses a multiway heap.
 * For simplified notations, logarithm in base d will be referred as log-d
 * The delete-the-minimum operation takes time proportional to d*log-d(n)
 * The insert takes time proportional to log-d(n)
 * The is-empty, min-key and size operations take constant time.
 * Constructor takes time proportional to the specified capacity.
 *
 * @author Tristan Claverie
 * @author Jingyi Cheng
 *
 */

class MultiwayMinPQ<Key> : Iterable<Key> {
    private val d: Int                //Dimension of the heap
    var size: Int = 0                        //Number of keys currently in the heap
        private set
    private var order: Int = 0                    //Number of levels of the tree
    private var keys: Array<Key>                    //Array of keys
    private val comp: Comparator<Key>    //Comparator over the keys

    /**
     * Whether the priority queue is empty
     * Worst case is O(1)
     * @return true if the priority queue is empty, false if not
     */
    val isEmpty: Boolean
        get() = size == 0

    /**
     * Initializes an empty priority queue
     * Worst case is O(d)
     *
     * @param  d dimension of the heap
     * @throws IllegalArgumentException if `d < 2`
     */
     @PublishedApi internal constructor (d: Int) {
        if (d < 2) throw IllegalArgumentException("Dimension should be 2 or over")
        this.d = d
        order = 1
        keys = Array<Comparable<*>>(d shl 1,{""}) as Array<Key>
        comp = MyComparator()
    }

    /**
     * Initializes an empty priority queue
     * Worst case is O(d)
     *
     * @param  d dimension of the heap
     * @param  comparator a Comparator over the keys
     * @throws IllegalArgumentException if `d < 2`
     */
    constructor(comparator: Comparator<Key>, d: Int) {
        if (d < 2) throw IllegalArgumentException("Dimension should be 2 or over")
        this.d = d
        order = 1
        keys = Array<Comparable<*>>(d shl 1, {""}) as Array<Key>
        comp = comparator
    }

    /**
     * Initializes a priority queue with given indexes
     * Worst case is O(n*log-d(n))
     *
     * @param  d dimension of the heap
     * @param  a an array of keys
     * @throws IllegalArgumentException if `d < 2`
     */
    constructor(a: Array<Key>, d: Int) {
        if (d < 2) throw IllegalArgumentException("Dimension should be 2 or over")
        this.d = d
        order = 1
        keys = Array<Comparable<*>>(d shl 1, {""}) as Array<Key>
        comp = MyComparator()
        for (key in a) insert(key)
    }

    /**
     * Initializes a priority queue with given indexes
     * Worst case is O(a*log-d(n))
     *
     * @param  d dimension of the heap
     * @param  comparator a Comparator over the keys
     * @param  a an array of keys
     * @throws IllegalArgumentException if `d < 2`
     */
    constructor(comparator: Comparator<Key>, a: Array<Key>, d: Int) {
        if (d < 2) throw IllegalArgumentException("Dimension should be 2 or over")
        this.d = d
        order = 1
        keys = Array<Comparable<*>>(d shl 1, {""}) as Array<Key>
        comp = comparator
        for (key in a) insert(key)
    }

    /**
     * Puts a Key on the priority queue
     * Worst case is O(log-d(n))
     * @param key a Key
     */
    fun insert(key: Key) {
        keys[size + d] = key
        swim(size++)
        if (size == keys.size - d) {
            resize(getN(order + 1) + d)
            order++
        }
    }

    /**
     * Gets the minimum key currently in the queue
     * Worst case is O(1)
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the minimum key currently in the priority queue
     */
    fun minKey(): Key {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        return keys[d]
    }

    /**
     * Deletes the minimum key
     * Worst case is O(d*log-d(n))
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the minimum key
     */
    fun delMin(): Key {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        exch(0, --size)
        sink(0)
        val min = keys[size + d]
        val number = getN(order - 2)
        if (order > 1 && size == number) {
            resize(number + Math.pow(d.toDouble(), (order - 1).toDouble()).toInt() + d)
            order--
        }
        return min
    }

    //Compares two keys
    private fun greater(x: Int, y: Int): Boolean {
        val i = x + d
        val j = y + d
        return comp.compare(keys[i], keys[j]) > 0
    }

    //Exchanges the position of two keys
    private fun exch(x: Int, y: Int) {
        val i = x + d
        val j = y + d
        val swap = keys[i]
        keys[i] = keys[j]
        keys[j] = swap
    }

    //Gets the maximum number of keys in the heap, given the number of levels of the tree
    private fun getN(order: Int) = (1 - Math.pow(d.toDouble(), (order + 1).toDouble()).toInt()) / (1 - d)

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
        val child = d * i + 1
        if (child >= size) return
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
        for (cur in loBound..hiBound) {
            if (cur < size && greater(min, cur)) min = cur
        }
        return min
    }

    //Resizes the array containing the keys
    //If the heap is full, it adds one floor
    //If the heap has two floors empty, it removes one
    private fun resize(N: Int) {
        val array = arrayOfNulls<Comparable<*>>(N) as Array<Key>
        for (i in 0 until Math.min(keys.size, array.size)) {
            array[i] = keys[i]
        }
        keys = array
    }

    /**
     * Gets an Iterator over the keys in the priority queue in ascending order
     * The Iterator does not implement the remove() method
     * iterator() : Worst case is O(n)
     * next() : 	Worst case is O(d*log-d(n))
     * hasNext() : 	Worst case is O(1)
     * @return an Iterator over the keys in the priority queue in ascending order
     */
    override fun iterator(): Iterator<Key> = MyIterator()

    //Constructs an Iterator over the keys in linear time
    private inner class MyIterator : Iterator<Key> {
        internal var data: MultiwayMinPQ<Key> = MultiwayMinPQ(comp, d)

        init {
            data.keys = this@MultiwayMinPQ.keys
            data.size = size
            for (i in keys.indices) {
                data.keys[i] = keys[i]
            }
        }

        override fun hasNext() = !data.isEmpty

        override fun next(): Key {
            if (!hasNext()) throw NoSuchElementException()
            return data.delMin()
        }
    }

    //default Comparator
    private inner class MyComparator : Comparator<Key> {
        override fun compare(key1: Key, key2: Key): Int {
            return (key1 as Comparable<Key>).compareTo(key2)
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
