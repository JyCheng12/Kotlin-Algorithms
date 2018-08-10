/******************************************************************************
 * Compilation:  javac Queue.java
 * Execution:    java Queue < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * A generic queue, implemented using a linked list.
 *
 * % java Queue < tobe.txt
 * to be or not to be (2 left on queue)
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Queue` class represents a first-in-first-out (FIFO)
 * queue of generic items.
 * It supports the usual *enqueue* and *dequeue*
 * operations, along with methods for peeking at the first item,
 * testing if the queue is empty, and iterating through
 * the items in FIFO order.
 *
 *
 * This implementation uses a singly linked list with a static nested class for
 * linked-list nodes. See [LinkedQueue] for the version from the
 * textbook that uses a non-static nested class.
 * See [ResizingArrayQueue] for a version that uses a resizing array.
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
 * @param <Item> the generic type of an item in this queue
</Item> */
class Queue<Item : Any?> : Iterable<Item?> {
    private var first: Node<Item?>? = null    // beginning of queue
    private var last: Node<Item?>? = null     // end of queue
    var size: Int = 0               // number of elements on queue
        private set

    /**
     * Returns true if this queue is empty.
     *
     * @return `true` if this queue is empty; `false` otherwise
     */
    val isEmpty: Boolean
        get() = first == null

    // helper linked list class
    internal class Node<Item>(var item: Item){
        var next: Node<Item>? = null
    }

    /**
     * Returns the item least recently added to this queue.
     *
     * @return the item least recently added to this queue
     * @throws NoSuchElementException if this queue is empty
     */
    fun peek(): Item? {
        if (isEmpty) throw NoSuchElementException("Queue underflow")
        return first!!.item
    }

    /**
     * Adds the item to this queue.
     *
     * @param  item the item to add
     */
    fun enqueue(item: Item?) {
        val oldLast = last
        last = Node(item)
        if (oldLast == null)
            first = last
        else
            oldLast.next = last
        size++
    }

    /**
     * Removes and returns the item on this queue that was least recently added.
     *
     * @return the item on this queue that was least recently added
     * @throws NoSuchElementException if this queue is empty
     */
    fun dequeue(): Item? {
        //val first = this.first ?: throw NoSuchElementException("Queue underflow")
        if (size == 0) throw NoSuchElementException()
        val first = this.first!!
        this.first = first.next
        size--
        //if (isEmpty) last = null
        return first.item
    }

    /**
     * Returns a string representation of this queue.
     *
     * @return the sequence of items in FIFO order, separated by spaces
     */
    override fun toString(): String {
        val s = StringBuilder()
        for (item in this) {
            s.append(item)
            s.append(' ')
        }
        return s.toString()
    }

    /**
     * Returns an iterator that iterates over the items in this queue in FIFO order.
     *
     * @return an iterator that iterates over the items in this queue in FIFO order
     */
    override fun iterator(): Iterator<Item?> = ListIterator(first)

    // an iterator, doesn't implement remove() since it's optional
    private inner class ListIterator<Item>(private var current: Node<Item>?) : Iterator<Item?> {
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
         * Unit tests the `Queue` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val queue = Queue<String>()
            val items = "to be or not to - be - - that - - - is".split(" ")
            for (item in items) {
                if (item != "-")
                    queue.enqueue(item)
                else if (!queue.isEmpty)
                    StdOut.print("${queue.dequeue()} ")
            }
            StdOut.println()

            try {
                queue.enqueue(null)
            } catch (e: IllegalArgumentException) {
                StdOut.println(e.message)
            }

            StdOut.println("(${queue.size} left on queue)")
            for (i in queue) {
                StdOut.print("$i ")
            }
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
