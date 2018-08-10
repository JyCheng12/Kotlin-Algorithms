/******************************************************************************
 * Compilation:  javac ResizingArrayBag.java
 * Execution:    java ResizingArrayBag
 * Dependencies: StdIn.kt StdOut.kt
 *
 * Bag implementation with a resizing array.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `ResizingArrayBag` class represents a bag (or multiset) of
 * generic items. It supports insertion and iterating over the
 * items in arbitrary order.
 *
 *
 * This implementation uses a resizing array.
 * See [LinkedBag] for a version that uses a singly linked list.
 * The *add* operation takes constant amortized time; the
 * *isEmpty*, and *size* operations
 * take constant time. Iteration takes time proportional to the number of items.
 *
 *
 * For additional documentation, see [Section 1.3](https://algs4.cs.princeton.edu/13stacks) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class ResizingArrayBag<Item> : Iterable<Item?> {
    private var a: Array<Item?> = arrayOfNulls<Any>(2) as Array<Item?>         // array of items
    var size: Int = 0            // number of elements on bag
        private set

    /**
     * Is this bag empty?
     * @return true if this bag is empty; false otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // resize the underlying array holding the elements
    private fun resize(capacity: Int) {
        assert(capacity >= size)
        a = a.copyOf(capacity)
    }

    /**
     * Adds the item to this bag.
     * @param item the item to add to this bag
     */
    fun add(item: Item) {
        if (size == a.size) resize(2 * a.size)    // double size of array if necessary
        a[size++] = item                           // add item
    }

    /**
     * Returns an iterator that iterates over the items in the bag in arbitrary order.
     * @return an iterator that iterates over the items in the bag in arbitrary order
     */
    override fun iterator(): Iterator<Item?> = ArrayIterator()

    // an iterator, doesn't implement remove() since it's optional
    private inner class ArrayIterator : Iterator<Item?> {
        private var i = 0

        override fun hasNext() = i < size

        override fun next(): Item? {
            if (!hasNext()) throw NoSuchElementException()
            return a[i++]
        }
    }

    companion object {

        /**
         * Unit tests the `ResizingArrayBag` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val bag = ResizingArrayBag<String>()
            bag.add("Hello")
            bag.add("World")
            bag.add("how")
            bag.add("are")
            bag.add("you")

            bag.forEach { s -> StdOut.println(s) }
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
