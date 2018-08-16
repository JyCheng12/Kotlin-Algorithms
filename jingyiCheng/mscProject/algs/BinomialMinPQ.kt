/******************************************************************************
 * Compilation: javac BinomialMinPQ.java
 * Execution:
 *
 * A binomial heap.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The BinomialMinPQ class represents a priority queue of generic keys.
 * It supports the usual insert and delete-the-minimum operations,
 * along with the merging of two heaps together.
 * It also supports methods for peeking at the minimum key,
 * testing if the priority queue is empty, and iterating through
 * the keys.
 * It is possible to build the priority queue using a Comparator.
 * If not, the natural order relation between the keys will be used.
 *
 * This implementation uses a binomial heap.
 * The insert, delete-the-minimum, union, min-key
 * and size operations take logarithmic time.
 * The is-empty and constructor operations take constant time.
 *
 * @author Tristan Claverie
 * @author Jingyi Cheng
 *
 */
class BinomialMinPQ<Key : Any> : Iterable<Key> {
    private var head: Node? = null                    //head of the list of roots
    private val comp: Comparator<Key>    //Comparator over the keys
    var size = 0
        /**
         * Number of elements currently on the priority queue
         * Worst case is O(log(n))
         * @throws ArithmeticException if there are more than 2^63-1 elements in the queue
         * @return the number of elements on the priority queue
         */
        get() {
            var result = 0
            var tmp: Int
            var node = head
            while (node != null) {
                if (node.order > 30) {
                    throw ArithmeticException("The number of elements cannot be evaluated, but the priority queue is still valid.")
                }
                tmp = 1 shl node.order
                result = result or tmp
                node = node.sibling
            }
            return result
        }
        private set

    /**
     * Whether the priority queue is empty
     * Worst case is O(1)
     * @return true if the priority queue is empty, false if not
     */
    val isEmpty = head == null

    //Represents a Node of a Binomial Tree
    internal inner class Node {
        lateinit var key : Key                        //Key contained by the Node
        var order: Int = 0                        //The order of the Binomial Tree rooted by this Node
        var child: Node? = null
        var sibling: Node? = null            //child and sibling of this Node
    }

    /**
     * Initializes an empty priority queue
     * Worst case is O(1)
     */
    constructor() {
        comp = MyComparator()
    }

    /**
     * Initializes an empty priority queue using the given Comparator
     * Worst case is O(1)
     * @param C a comparator over the keys
     */
    constructor(C: Comparator<Key>) {
        comp = C
    }

    /**
     * Initializes a priority queue with given keys
     * Worst case is O(n*log(n))
     * @param a an array of keys
     */
    constructor(a: Array<Key>) {
        comp = MyComparator()
        for (k in a) insert(k)
    }

    /**
     * Initializes a priority queue with given keys using the given Comparator
     * Worst case is O(n*log(n))
     * @param C a comparator over the keys
     * @param a an array of keys
     */
    constructor(C: Comparator<Key>, a: Array<Key>) {
        comp = C
        for (k in a) insert(k)
    }

    /**
     * Puts a Key in the heap
     * Worst case is O(log(n))
     * @param key a Key
     */
    fun insert(key: Key?) {
        if (key == null) throw IllegalArgumentException("Argument to insert() is null")
        val x = Node()
        x.key = key
        x.order = 0
        val H = BinomialMinPQ<Key>() //The Comparator oh the H heap is not used
        H.head = x
        this.head = this.union(H).head
    }

    /**
     * Get the minimum key currently in the queue
     * Worst case is O(log(n))
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the minimum key currently in the priority queue
     */
    fun minKey(): Key {
        //if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        var min = head ?: throw NoSuchElementException("Priority queue is empty")
        var current = min
        while (current.sibling != null) {
            min = if (greater(min.key, current.sibling!!.key)) current else min
            current = current.sibling!!
        }
        return min.key
    }

    /**
     * Deletes the minimum key
     * Worst case is O(log(n))
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the minimum key
     */
    fun delMin(): Key {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        val min = eraseMin()
        var x = min.child ?: min
        if (min.child != null) {
            min.child = null
            var prevx: Node? = null
            var nextx = x.sibling
            while (nextx != null) {
                x.sibling = prevx
                prevx = x
                x = nextx
                nextx = nextx.sibling
            }
            x.sibling = prevx
            val H = BinomialMinPQ<Key>()
            H.head = x
            head = union(H).head
        }
        return min.key
    }

    /**
     * Merges two Binomial heaps together
     * This operation is destructive
     * Worst case is O(log(n))
     * @param heap a Binomial Heap to be merged with the current heap
     * @throws IllegalArgumentException if the heap in parameter is null
     * @return the union of two heaps
     */
    fun union(heap: BinomialMinPQ<Key>?): BinomialMinPQ<Key> {
        if (heap == null) throw IllegalArgumentException("Cannot merge a Binomial Heap with null")
        this.head = merge(Node(), this.head, heap.head).sibling
        var x = this.head!!
        var prevx: Node? = null
        var nextx = x.sibling
        while (nextx != null) {
            if (x.order < nextx.order || nextx.sibling != null && nextx.sibling!!.order == x.order) {
                prevx = x
                x = nextx
            } else if (greater(nextx.key, x.key)) {
                x.sibling = nextx.sibling
                link(nextx, x)
            } else {
                when (prevx) {
                    null -> this.head = nextx
                    else -> prevx.sibling = nextx
                }
                link(x, nextx)
                x = nextx
            }
            nextx = x.sibling
        }
        return this
    }

    //Compares two keys
    private fun greater(n: Key?, m: Key?): Boolean {
        if (n == null) return false
        return if (m == null) true else comp.compare(n, m) > 0
    }

    //Assuming root1 holds a greater key than root2, root2 becomes the new root
    private fun link(root1: Node, root2: Node) {
        root1.sibling = root2.child
        root2.child = root1
        root2.order++
    }

    //Deletes and return the node containing the minimum key
    private fun eraseMin(): Node {
        var min = head ?: throw NoSuchElementException("Priority queue is empty")
        var current = min
        lateinit var previous: Node
        if (min.sibling == null) return min
        var sibling = current.sibling
        while (sibling != null) {
            if (greater(min.key, sibling.key)) {
                previous = current
                min = sibling
            }
            current = sibling
            sibling = current.sibling
        }
        previous.sibling = min.sibling
        if (min === head) head = min.sibling
        return min
    }

    /**************************************************
     * Functions for inserting a key in the heap
     */

    //Merges two root lists into one, there can be up to 2 Binomial Trees of same order
    private fun merge(h: Node, x: Node?, y: Node?): Node {
        when {
            x == null && y == null -> return h
            x == null -> h.sibling = merge(y!!, x, y.sibling)
            y == null -> h.sibling = merge(x, x.sibling, y)
            x.order < y.order -> h.sibling = merge(x, x.sibling, y)
            else -> h.sibling = merge(y, x, y.sibling)
        }
        return h
    }

    /**
     * Gets an Iterator over the keys in the priority queue in ascending order
     * The Iterator does not implement the remove() method
     * iterator() : Worst case is O(n)
     * next() : 	Worst case is O(log(n))
     * hasNext() : 	Worst case is O(1)
     * @return an Iterator over the keys in the priority queue in ascending order
     */
    override fun iterator(): Iterator<Key> {
        return MyIterator()
    }

    private inner class MyIterator : Iterator<Key> {
        internal var data: BinomialMinPQ<Key> = BinomialMinPQ(comp)

        //Constructor clones recursively the elements in the queue
        //It takes linear time
        init {
            data.head = clone(head, null)
        }

        private fun clone(x: Node?, parent: Node?): Node? {
            if (x == null) return null
            val node = Node()
            node.key = x.key
            node.sibling = clone(x.sibling, parent)
            node.child = clone(x.child, node)
            return node
        }

        override fun hasNext(): Boolean {
            return !data.isEmpty
        }

        override fun next(): Key {
            if (!hasNext()) throw NoSuchElementException()
            return data.delMin()
        }
    }

    /***************************
     * Comparator
     */

    //default Comparator
    private inner class MyComparator : Comparator<Key> {
        override fun compare(key1: Key, key2: Key): Int {
            @Suppress("UNCHECKED_CAST")
            return (key1 as Comparable<Key>).compareTo(key2)
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
            val pq = BinomialMinPQ<String>()
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

            StdOut.println("Checking MinKey() ... ${pq.minKey()}")

            StdOut.println("Deleting 4 elements with highest priority ...")
            for (i in 0..3)
                pq.delMin()
            for ((i, x) in pq.withIndex())
                StdOut.println("$i -> $x")
            StdOut.println("(${pq.size} left on pq)")

            StdOut.println("Checking MinPQ(Array(3){'A'} ...")
            val pq2 = MinPQ(Array(3){"A"})
            for ((i, x) in pq2.withIndex())
                StdOut.println("$i -> $x")

            StdOut.println("Deleting all elements ...")
            for (i in 1..pq2.size)
                pq2.delMin()
            for ((i, x) in pq2.withIndex())
                StdOut.println("$i -> $x")
            StdOut.println("(${pq2.size} left on pq)")
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