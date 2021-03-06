/******************************************************************************
 * Compilation:  javac MinPQ.java
 * Execution:    java MinPQ < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/24pq/tinyPQ.txt
 *
 * Generic min priority queue implementation with a binary heap.
 * Can be used with a comparator instead of the natural order.
 *
 * % java MinPQ < tinyPQ.txt
 * E A E (6 left on pq)
 *
 * We use a one-based array to simplify parent and child calculations.
 *
 * Can be optimized by replacing full exchanges with half exchanges
 * (ala insertion sort).
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `MinPQ` class represents a priority queue of generic keys.
 * It supports the usual *insert* and *delete-the-minimum*
 * operations, along with methods for peeking at the minimum key,
 * testing if the priority queue is empty, and iterating through
 * the keys.
 *
 *
 * This implementation uses a binary heap.
 * The *insert* and *delete-the-minimum* operations take
 * logarithmic amortized time.
 * The *min*, *size*, and *is-empty* operations take constant time.
 * Construction takes time proportional to the specified capacity or the number of
 * items used to initialize the data structure.
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
@Suppress("UNCHECKED_CAST")
class MinPQ<Key : Comparable<Key>> : Iterable<Key> {
    private var pq: Array<Key?> = arrayOfNulls<Comparable<Any>>(2) as Array<Key?>                  // store items at indices 1 to n
    var size: Int = 0                       // number of items on priority queue
        private set
    private var comparator: Comparator<Key>? = null  // optional comparator

    /**
     * Returns true if this priority queue is empty.
     *
     * @return `true` if this priority queue is empty;
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // is pq[1..N] a min heap?
    private val isMinHeap
        get() = isMinHeap(1)

    /**
     * Initializes an empty priority queue with the given initial capacity.
     *
     * @param  initCapacity the initial capacity of this priority queue
     */
    @JvmOverloads constructor(initCapacity: Int = 1) {
        pq = arrayOfNulls<Comparable<Any>>(initCapacity + 1) as Array<Key?>
    }

    /**
     * Initializes an empty priority queue with the given initial capacity,
     * using the given comparator.
     *
     * @param  initCapacity the initial capacity of this priority queue
     * @param  comparator the order in which to compare the keys
     */
    constructor(initCapacity: Int, comparator: Comparator<Key>) {
        this.comparator = comparator
        pq = arrayOfNulls<Comparable<Any>>(initCapacity + 1) as Array<Key?>
    }

    /**
     * Initializes an empty priority queue using the given comparator.
     *
     * @param  comparator the order in which to compare the keys
     */
    constructor(comparator: Comparator<Key>) : this(1, comparator)

    /**
     * Initializes a priority queue from the array of keys.
     *
     *
     * Takes time proportional to the number of keys, using sink-based heap construction.
     *
     * @param  keys the array of keys
     */
    constructor(keys: Array<Key>) {
        this.size = keys.size
        pq = arrayOfNulls<Comparable<Any>>(keys.size + 1) as Array<Key?>
        for (i in 0 until size)
            pq[i + 1] = keys[i]
        for (k in size / 2 downTo 1)
            sink(k)
        assert(isMinHeap)
    }

    /**
     * Returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    fun min() = pq[1] ?: throw NoSuchElementException("Priority queue underflow")

    // helper function to double the size of the heap array
    private fun resize(capacity: Int) {
        //assert(capacity > size)
        pq[0] = null
        pq = pq.copyOf(capacity)
    }

    /**
     * Adds a new key to this priority queue.
     *
     * @param  x the key to add to this priority queue
     */
    fun insert(x: Key?) {
        //if (x == null) throw IllegalArgumentException("Argument to insert() is null")
        // double size of array if necessary
        if (size == pq.size - 1) resize(2 * pq.size)

        // add x, and percolate it up to maintain heap invariant
        pq[++size] = x
        swim(size)
        //assert(isMinHeap)
    }

    /**
     * Removes and returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    fun delMin(): Key {
        val min = pq[1] ?: throw NoSuchElementException("Priority queue underflow")
        exch(1, size--)
        sink(1)
        if (size > 0 && size == (pq.size - 1) / 4) resize(pq.size / 2)
        //assert(isMinHeap)
        return min
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

    private fun greater(i: Int, j: Int): Boolean = if (comparator == null)
        pq[i] as Comparable<Key> > pq[j] as Key
    else
        comparator!!.compare(pq[i], pq[j]) > 0

    private fun exch(i: Int, j: Int) {
        val swap = pq[i]
        pq[i] = pq[j]
        pq[j] = swap
    }

    // is subtree of pq[1..n] rooted at k a min heap?
    private fun isMinHeap(k: Int): Boolean {
        if (k > size) return true
        val left = 2 * k
        val right = 2 * k + 1
        if (left <= size && greater(k, left)) return false
        return if (right <= size && greater(k, right)) false else isMinHeap(left) && isMinHeap(right)
    }

    /**
     * Returns an iterator that iterates over the keys on this priority queue
     * in ascending order.
     *
     * The iterator doesn't implement `remove()` since it's optional.
     *
     * @return an iterator that iterates over the keys in ascending order
     */
    override fun iterator(): Iterator<Key> = HeapIterator()

    private inner class HeapIterator : Iterator<Key> {
        // create a new pq
        private var copy: MinPQ<Key> = if (comparator == null) MinPQ(size) else MinPQ(size, comparator!!)

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        init {
            for (i in 1..size)
                copy.insert(pq[i])
        }

        override fun hasNext() = !copy.isEmpty

        override fun next(): Key {
            if (!hasNext()) throw NoSuchElementException()
            return copy.delMin()
        }
    }

    companion object {
        /**
         * Unit tests the `MinPQ` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val pq = MinPQ<String>()
            StdOut.println("Checking isEmpty ... " + pq.isEmpty)
            StdOut.println("(${pq.size} left on pq)")
            val items = arrayOf("A", "B", "C", "D", "E", "F", null, "G")
            try {
                for (x in items)
                    pq.insert(x)
            } catch (e: IllegalArgumentException) {
                StdOut.println(e.message)
            }

            for ((i, x) in pq.withIndex())
                StdOut.println("$i -> $x")
            StdOut.println("(${pq.size} left on pq)")
            StdOut.println("Checking isEmpty ... ${pq.isEmpty}")

            StdOut.println("Deleting 4 elements with highest priority ...")
            for (i in 0..3)
                pq.delMin()
            for ((i, x) in pq.withIndex())
                StdOut.println("$i -> $x")
            StdOut.println("(${pq.size} left on pq)")

            StdOut.println("Checking MinPQ(Array(3){'A'} ...")
            val pq2 = MinPQ(Array(3) { "A" })
            for ((i, x) in pq2.withIndex())
                StdOut.println("$i -> $x")

            StdOut.println("Deleting all elements ...")
            for (i in 1..pq2.size)
                pq2.delMin()
            for ((i, x) in pq2.withIndex())
                StdOut.println("$i -> $x")
            StdOut.println("(${pq2.size} left on pq)")
            StdOut.println("Checking isEmpty ... ${pq.isEmpty}")

            StdOut.println("End of test.")
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