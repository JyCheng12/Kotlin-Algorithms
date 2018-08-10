/******************************************************************************
 * Compilation:  javac ResizingArrayQueue.java
 * Execution:    java ResizingArrayQueue < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * Queue implementation with a resizing array.
 *
 * % java ResizingArrayQueue < tobe.txt
 * to be or not to be (2 left on queue)
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `ResizingArrayQueue` class represents a first-in-first-out (FIFO)
 * queue of generic items.
 * It supports the usual *enqueue* and *dequeue*
 * operations, along with methods for peeking at the first item,
 * testing if the queue is empty, and iterating through
 * the items in FIFO order.
 *
 *
 * This implementation uses a resizing array, which double the underlying array
 * when it is full and halves the underlying array when it is one-quarter full.
 * The *enqueue* and *dequeue* operations take constant amortized time.
 * The *size*, *peek*, and *is-empty* operations takes
 * constant time in the worst case.
 *
 *
 * For additional documentation, see [Section 1.3](https://algs4.cs.princeton.edu/13stacks) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class ResizingArrayQueue<Item> : Iterable<Item?> {
    private var q: Array<Item?> = arrayOfNulls<Any>(2) as Array<Item?>       // queue elements
    var size: Int = 0          // number of elements on queue
        private set
    private var first: Int = 0      // index of first element of queue
    private var last: Int = 0       // index of next available slot

    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // resize the underlying array
    private fun resize(capacity: Int) {
        assert(capacity >= size)
        val temp = arrayOfNulls<Any>(capacity) as Array<Item?>
        for (i in 0 until size) {
            temp[i] = q[(this.first + i) % q.size]
        }
        q = temp
        first = 0
        last = size
    }

    /**
     * Adds the item to this queue.
     * @param item the item to add
     */
    fun enqueue(item: Item) {
        // double size of array if necessary and recopy to front of array
        if (size == q.size) resize(2 * q.size)   // double size of array if necessary
        q[last++] = item                        // add item
        if (last == q.size) last = 0          // wrap-around
        size++
    }

    /**
     * Removes and returns the item on this queue that was least recently added.
     * @return the item on this queue that was least recently added
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    fun dequeue(): Item? {
        if (isEmpty) throw NoSuchElementException("Queue underflow")
        val item = q[first]
        q[first] = null                            // to avoid loitering
        size--
        first++
        if (first == q.size) first = 0           // wrap-around
        // shrink size of array if necessary
        if (size > 0 && size == q.size / 4) resize(q.size / 2)
        return item
    }

    /**
     * Returns the item least recently added to this queue.
     * @return the item least recently added to this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    fun peek(): Item? {
        if (isEmpty) throw NoSuchElementException("Queue underflow")
        return q[first]
    }

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    override fun iterator(): Iterator<Item?> = ArrayIterator()

    // an iterator, doesn't implement remove() since it's optional
    private inner class ArrayIterator : Iterator<Item?> {
        private var i = 0

        override fun hasNext() = i < size

        override fun next(): Item? {
            if (!hasNext()) throw NoSuchElementException()
            val item = q[(i + first) % q.size]
            i++
            return item
        }
    }

    companion object {
        /**
         * Unit tests the `ResizingArrayQueue` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val queue = ResizingArrayQueue<String>()
            while (!StdIn.isEmpty) {
                val item = StdIn.readString()!!
                if (item != "-")
                    queue.enqueue(item)
                else if (!queue.isEmpty) StdOut.print("${queue.dequeue()} ")
            }
            StdOut.println("(${queue.size} left on queue)")
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
