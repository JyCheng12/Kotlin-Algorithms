/******************************************************************************
 * Compilation:  javac ResizingArrayStack.java
 * Execution:    java ResizingArrayStack < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 * Stack implementation with a resizing array.
 *
 * % more tobe.txt
 * to be or not to - be - - that - - - is
 *
 * % java ResizingArrayStack < tobe.txt
 * to be not that or be (2 left on stack)
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `ResizingArrayStack` class represents a last-in-first-out (LIFO) stack
 * of generic items.
 * It supports the usual *push* and *pop* operations, along with methods
 * for peeking at the top item, testing if the stack is empty, and iterating through
 * the items in LIFO order.
 *
 *
 * This implementation uses a resizing array, which double the underlying array
 * when it is full and halves the underlying array when it is one-quarter full.
 * The *push* and *pop* operations take constant amortized time.
 * The *size*, *peek*, and *is-empty* operations takes
 * constant time in the worst case.
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
class ResizingArrayStack<Item> : Iterable<Item?> {
    private var a: Array<Item?> = arrayOfNulls<Any>(2) as Array<Item?>         // array of items
    private var size: Int = 0            // number of elements on stack

    /**
     * Is this stack empty?
     * @return true if this stack is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // resize the underlying array holding the elements
    private fun resize(capacity: Int) {
        assert(capacity >= size)
        a = a.copyOf(capacity)
    }

    /**
     * Adds the item to this stack.
     * @param item the item to add
     */
    fun push(item: Item) {
        if (size == a.size) resize(2 * a.size)    // double size of array if necessary
        a[size++] = item                            // add item
    }

    /**
     * Removes and returns the item most recently added to this stack.
     * @return the item most recently added
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    fun pop(): Item? {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        val item = a[size - 1]
        a[size-1] = null                              // to avoid loitering
        size--
        // shrink size of array if necessary
        if (size > 0 && size == a.size / 4) resize(a.size / 2)
        return item
    }

    /**
     * Returns (but does not remove) the item most recently added to this stack.
     * @return the item most recently added to this stack
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    fun peek(): Item? {
        if (isEmpty) throw NoSuchElementException("Stack underflow")
        return a[size - 1]
    }

    /**
     * Returns an iterator to this stack that iterates through the items in LIFO order.
     * @return an iterator to this stack that iterates through the items in LIFO order.
     */
    override fun iterator(): Iterator<Item?> = ReverseArrayIterator()

    // an iterator, doesn't implement remove() since it's optional
    private inner class ReverseArrayIterator : Iterator<Item?> {
        private var i: Int = size - 1

        override fun hasNext() = i >= 0

        override fun next(): Item? {
            if (!hasNext()) throw NoSuchElementException()
            return a[i--]
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
            val stack = ResizingArrayStack<String>()
            while (!StdIn.isEmpty) {
                val item = StdIn.readString()!!
                if (item != "-")
                    stack.push(item)
                else if (!stack.isEmpty) StdOut.print("${stack.pop()} ")
            }
            StdOut.println("(${stack.size} left on stack)")
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