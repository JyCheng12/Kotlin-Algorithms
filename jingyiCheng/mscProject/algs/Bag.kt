/******************************************************************************
 * Compilation:  javac Bag.java
 * Execution:    java Bag < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 *
 * A generic bag or multiset, implemented using a singly linked list.
 *
 * % more tobe.txt
 * to be or not to - be - - that - - - is
 *
 * % java Bag < tobe.txt
 * size of bag = 14
 * is
 * -
 * -
 * -
 * that
 * -
 * -
 * be
 * -
 * to
 * not
 * or
 * be
 * to
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Bag` class represents a bag (or multiset) of
 * generic items. It supports insertion and iterating over the
 * items in arbitrary order.
 *
 *
 * This implementation uses a singly linked list with a static nested class Node.
 * See [LinkedBag] for the version from the
 * textbook that uses a non-static nested class.
 * See [ResizingArrayBag] for a version that uses a resizing array.
 * The *add*, *isEmpty*, and *size* operations
 * take constant time. Iteration takes time proportional to the number of items.
 *
 *
 * For additional documentation, see [Section 1.3](https://algs4.cs.princeton.edu/13stacks) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 * @param <Item> the generic type of an item in this bag
</Item> */
class Bag<Item> : Iterable<Item?> {
    private var first: Node<Item?>? = null    // beginning of bag
    var size: Int = 0               // number of elements in bag
        private set

    /**
     * Returns true if this bag is empty.
     *
     * @return `true` if this bag is empty;
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = first == null

    // helper linked list class
    internal class Node<Item> (var item: Item?, var next: Node<Item?>?)


    /**
     * Adds the item to this bag.
     *
     * @param  item the item to add to this bag
     */
    fun add(item: Item?) {
        val oldFirst = first
        first = Node(item, oldFirst)
        size++
    }

    /**
     * Returns an iterator that iterates over the items in this bag in arbitrary order.
     *
     * @return an iterator that iterates over the items in this bag in arbitrary order
     */
    override fun iterator(): Iterator<Item?> {
        return ListIterator(first)
    }

    // an iterator, doesn't implement remove() since it's optional
    private inner class ListIterator<Item>(private var current: Node<Item?>?) : Iterator<Item?> {

        override fun hasNext(): Boolean {
            return current != null
        }

        override fun next(): Item? {
            val current = current ?: throw NoSuchElementException()
            val item = current.item
            this.current = current.next
            return item
        }
    }

    companion object {

        /**
         * Unit tests the `Bag` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val bag = Bag<String>()
            StdOut.println("Checking isEmpty ... ${bag.isEmpty}")
            while (!StdIn.isEmpty) {
                val item = StdIn.readString()
                bag.add(item)
            }

            StdOut.println("Checking isEmpty ... ${bag.isEmpty}")

            StdOut.println("size of bag = ${bag.size}")
            for (s in bag) {
                StdOut.println(s)
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
