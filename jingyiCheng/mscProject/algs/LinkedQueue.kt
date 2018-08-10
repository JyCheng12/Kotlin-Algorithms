/******************************************************************************
 * Compilation:  javac LinkedQueue.java
 * Execution:    java LinkedQueue < input.txt
 * Dependencies: StdIn.tk StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * A generic queue, implemented using a singly linked list.
 *
 * % java Queue < tobe.txt
 * to be or not to be (2 left on queue)
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LinkedQueue` class represents a first-in-first-out (FIFO)
 * queue of generic items.
 * It supports the usual *enqueue* and *dequeue*
 * operations, along with methods for peeking at the first item,
 * testing if the queue is empty, and iterating through
 * the items in FIFO order.
 *
 *
 * This implementation uses a singly linked list with a non-static nested class
 * for linked-list nodes.  See [Queue] for a version that uses a static nested class.
 * The *enqueue*, *dequeue*, *peek*, *size*, and *is-empty*
 * operations all take constant time in the worst case.
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
class LinkedQueue<Item> : Iterable<Item?> {
    var size: Int = 0         // number of elements on queue
        private set
    private var first: Node? = null    // beginning of queue
    private var last: Node? = null     // end of queue

    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = first == null

    // helper linked list class
    internal inner class Node {
        var item: Item? = null
        var next: Node? = null
    }

    /**
     * Initializes an empty queue.
     */
    init {
        assert(check())
    }

    /**
     * Returns the item least recently added to this queue.
     * @return the item least recently added to this queue
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    fun peek(): Item? {
        if (isEmpty) throw NoSuchElementException("Queue underflow")
        return first!!.item
    }

    /**
     * Adds the item to this queue.
     * @param item the item to add
     */
    fun enqueue(item: Item) {
        val oldlast = last
        last = Node()
        last!!.item = item
        last!!.next = null
        if (isEmpty)
            first = last
        else
            oldlast!!.next = last
        size++
        assert(check())
    }

    /**
     * Removes and returns the item on this queue that was least recently added.
     * @return the item on this queue that was least recently added
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    fun dequeue(): Item? {
        if (isEmpty) throw NoSuchElementException("Queue underflow")
        val item = first!!.item
        first = first!!.next
        size--
        if (isEmpty) last = null   // to avoid loitering
        assert(check())
        return item
    }

    /**
     * Returns a string representation of this queue.
     * @return the sequence of items in FIFO order, separated by spaces
     */
    override fun toString(): String {
        val s = StringBuilder()
        for (item in this)
            s.append(item.toString() + " ")
        return s.toString()
    }

    // check internal invariants
    private fun check(): Boolean {
        when {
            size < 0 -> return false
            size == 0 -> {
                if (first != null) return false
                if (last != null) return false
            }
            size == 1 -> {
                if (first == null || last == null) return false
                if (first !== last) return false
                if (first!!.next != null) return false
            }
            else -> {
                if (first == null || last == null) return false
                if (first === last) return false
                if (first!!.next == null) return false
                if (last!!.next != null) return false

                // check internal consistency of instance variable n
                var numberOfNodes = 0
                var x = first
                while (x != null && numberOfNodes <= size) {
                    numberOfNodes++
                    x = x.next
                }
                if (numberOfNodes != size) return false

                // check internal consistency of instance variable last
                var lastNode = first
                while (lastNode!!.next != null) {
                    lastNode = lastNode.next
                }
                if (last !== lastNode) return false
            }
        }
        return true
    }

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    override fun iterator(): Iterator<Item?> = ListIterator()

    // an iterator, doesn't implement remove() since it's optional
    private inner class ListIterator : Iterator<Item?> {
        private var current = first

        override fun hasNext() = current != null

        override fun next(): Item? {
            val current = current ?: throw NoSuchElementException()
            val item = current.item
            this.current = current.next
            return item
        }
    }

    companion object {
        /**
         * Unit tests the `LinkedQueue` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val queue = LinkedQueue<String>()
            while (!StdIn.isEmpty) {
                val item = StdIn.readString()!!
                if (item != "-")
                    queue.enqueue(item)
                else if (!queue.isEmpty)
                    StdOut.print(queue.dequeue() + " ")
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
