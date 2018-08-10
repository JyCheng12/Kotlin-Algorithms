/******************************************************************************
 * Compilation:  javac LinkedStack.java
 * Execution:    java LinkedStack < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * A generic stack, implemented using a linked list. Each stack
 * element is of type Item.
 *
 * % more tobe.txt
 * to be or not to - be - - that - - - is
 *
 * % java LinkedStack < tobe.txt
 * to be not that or be (2 left on stack)
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LinkedStack` class represents a last-in-first-out (LIFO) stack of
 * generic items.
 * It supports the usual *push* and *pop* operations, along with methods
 * for peeking at the top item, testing if the stack is empty, and iterating through
 * the items in LIFO order.
 *
 *
 * This implementation uses a singly linked list with a non-static nested class for
 * linked-list nodes. See [Stack] for a version that uses a static nested class.
 * The *push*, *pop*, *peek*, *size*, and *is-empty*
 * operations all take constant time in the worst case.
 *
 *
 * For additional documentation,
 * see [Section 1.3](https://algs4.cs.princeton.edu/13stacks) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class LinkedStack<Item> : Iterable<Item?> {
    var size: Int = 0          // size of the stack
        private set
    private var first: Node<Item>? = null     // top of stack

    /**
     * Is this stack empty?
     * @return true if this stack is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = first == null

    // helper linked list class
    internal class Node<Item> {
        var item: Item? = null
        var next: Node<Item>? = null
    }

    /**
     * Initializes an empty stack.
     */
    init {
        assert(check())
    }

    /**
     * Adds the item to this stack.
     * @param item the item to add
     */
    fun push(item: Item?) {
        val oldfirst = first
        first = Node()
        first!!.item = item
        first!!.next = oldfirst
        size++
        assert(check())
    }

    /**
     * Removes and returns the item most recently added to this stack.
     * @return the item most recently added
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    fun pop(): Item? {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        val item = first!!.item        // save item to return
        first = first!!.next            // delete first node
        size--
        assert(check())
        return item                   // return the saved item
    }

    /**
     * Returns (but does not remove) the item most recently added to this stack.
     * @return the item most recently added to this stack
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    fun peek(): Item? {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        return first!!.item
    }

    /**
     * Returns a string representation of this stack.
     * @return the sequence of items in the stack in LIFO order, separated by spaces
     */
    override fun toString(): String {
        val s = StringBuilder()
        for (item in this)
            s.append("$item ")
        return s.toString()
    }

    /**
     * Returns an iterator to this stack that iterates through the items in LIFO order.
     * @return an iterator to this stack that iterates through the items in LIFO order.
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

    // check internal invariants
    private fun check(): Boolean {
        val first = first
        // check a few properties of instance variable 'first'
        when {
            size < 0 -> return false
            size == 0 -> if (first != null) return false
            size == 1 -> {
                if (first == null) return false
                if (first.next != null) return false
            }
            else -> {
                if (first == null) return false
                if (first.next == null) return false
            }
        }

        // check internal consistency of instance variable n
        var numberOfNodes = 0
        var x = first
        while (x != null && numberOfNodes <= size) {
            numberOfNodes++
            x = x.next
        }
        return numberOfNodes == size
    }

    companion object {
        /**
         * Unit tests the `LinkedStack` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val stack = LinkedStack<String>()
            while (!StdIn.isEmpty) {
                val item = StdIn.readString()
                if (item != "-")
                    stack.push(item)
                else if (!stack.isEmpty)
                    StdOut.print("${stack.pop()} ")
            }
            StdOut.println("(${stack.size} left on stack)")
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
