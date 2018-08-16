/******************************************************************************
 * Compilation:  javac Stack.java
 * Execution:    java Stack < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * A generic stack, implemented using a singly linked list.
 * Each stack element is of type Item.
 *
 * This version uses a static nested class Node (to save 8 bytes per
 * Node), whereas the version in the textbook uses a non-static nested
 * class (for simplicity).
 *
 * % more tobe.txt
 * to be or not to - be - - that - - - is
 *
 * % java Stack < tobe.txt
 * to be not that or be (2 left on stack)
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Stack` class represents a last-in-first-out (LIFO) stack of generic items.
 * It supports the usual *push* and *pop* operations, along with methods
 * for peeking at the top item, testing if the stack is empty, and iterating through
 * the items in LIFO order.
 *
 *
 * This implementation uses a singly linked list with a static nested class for
 * linked-list nodes. See [LinkedStack] for the version from the
 * textbook that uses a non-static nested class.
 * See [ResizingArrayStack] for a version that uses a resizing array.
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
 * @param <Item> the generic type of an item in this stack
</Item> */
class Stack<Item : Any?> : Iterable<Item?> {
    private var first: Node<Item?>? = null     // top of stack
    var size: Int = 0                // size of the stack
        private set

    /**
     * Returns true if this stack is empty.
     *
     * @return true if this stack is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = first == null

    // helper linked list class
    internal class Node<Item>(var item: Item?, var next: Node<Item?>?)

    /**
     * Adds the item to this stack.
     *
     * @param  item the item to add
     */
    fun push(item: Item?) {
        val oldFirst = first
        first = Node(item, oldFirst)
        size++
    }

    /**
     * Removes and returns the item most recently added to this stack.
     *
     * @return the item most recently added
     * @throws NoSuchElementException if this stack is empty
     */
    fun pop(): Item? {
        val first = this.first ?: throw NoSuchElementException("Stack underflow")
        this.first = first.next            // delete first node
        size--
        return first.item                // return the saved item
    }

    /**
     * Returns (but does not remove) the item most recently added to this stack.
     *
     * @return the item most recently added to this stack
     * @throws NoSuchElementException if this stack is empty
     */
    fun peek(): Item? {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        return first!!.item
    }

    /**
     * Returns a string representation of this stack.
     *
     * @return the sequence of items in this stack in LIFO order, separated by spaces
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
     * Returns an iterator to this stack that iterates through the items in LIFO order.
     *
     * @return an iterator to this stack that iterates through the items in LIFO order
     */
    override fun iterator(): Iterator<Item?> = ListIterator(first)

    // an iterator, doesn't implement remove() since it's optional
    private inner class ListIterator<Item>(private var current: Node<Item?>?) : Iterator<Item?> {

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
         * Unit tests the `Stack` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val stack = Stack<String>()
            val items = "to be or not to - be - - that - - - is".split(" ")
            for (item in items)
                if (item != "-")
                    stack.push(item)
                else if (!stack.isEmpty)
                    StdOut.print("${stack.pop()} ")
            StdOut.println()
            try{
                stack.push(null)
            }catch (e:IllegalArgumentException){
                StdOut.println(e.message)
            }

            StdOut.println("(${stack.size} left on stack)")
            for(i in stack)
                StdOut.print("$i ")
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