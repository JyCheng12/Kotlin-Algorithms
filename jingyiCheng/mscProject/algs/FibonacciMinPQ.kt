/******************************************************************************
 * Compilation: javac FibonacciMinPQ.java
 * Execution:
 *
 * A Fibonacci heap.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The FibonacciMinPQ class represents a priority queue of generic keys.
 * It supports the usual insert and delete-the-minimum operations,
 * along with the merging of two heaps together.
 * It also supports methods for peeking at the minimum key,
 * testing if the priority queue is empty, and iterating through
 * the keys.
 * It is possible to build the priority queue using a Comparator.
 * If not, the natural order relation between the keys will be used.
 *
 * This implementation uses a Fibonacci heap.
 * The delete-the-minimum operation takes amortized logarithmic time.
 * The insert, min-key, is-empty, size, union and constructor take constant time.
 *
 *  @author Tristan Claverie
 *  @author Jingyi Cheng
 *
 */
class FibonacciMinPQ<Key : Any> : Iterable<Key> {
    private var head: Node? = null                    //Head of the circular root list
    private var min: Node? = null                    //Minimum Node of the root list
    var size: Int = 0                    //Number of keys in the heap
        private set
    private val comp: Comparator<Key>    //Comparator over the keys
    private val table = HashMap<Int, Node?>() //Used for the consolidate operation

    /**
     * Whether the priority queue is empty
     * Worst case is O(1)
     * @return true if the priority queue is empty, false if not
     */
    val isEmpty: Boolean
        get() = size == 0

    //Represents a Node of a tree
    private inner class Node {
        internal lateinit var key: Key                        //Key of this Node
        internal var order: Int = 0                        //Order of the tree rooted by this Node
        internal var prev: Node? = null
        internal var next: Node? = null                //Siblings of this Node
        internal var child: Node? = null                        //Child of this Node
    }

    /**
     * Initializes an empty priority queue
     * Worst case is O(1)
     * @param C a Comparator over the Keys
     */
    constructor(C: Comparator<Key>) {
        comp = C
    }

    /**
     * Initializes an empty priority queue
     * Worst case is O(1)
     */
    constructor() {
        comp = MyComparator()
    }

    /**
     * Initializes a priority queue with given keys
     * Worst case is O(n)
     * @param a an array of keys
     */
    constructor(a: Array<Key>) {
        comp = MyComparator()
        for (k in a) insert(k)
    }

    /**
     * Initializes a priority queue with given keys
     * Worst case is O(n)
     * @param C a comparator over the keys
     * @param a an array of keys
     */
    constructor(C: Comparator<Key>, a: Array<Key>) {
        comp = C
        for (k in a) insert(k)
    }

    /**
     * Insert a key in the queue
     * Worst case is O(1)
     * @param key a Key
     */
    fun insert(key: Key?) {
        if (key == null) throw IllegalArgumentException("Argument to insert() is null")
        val x = Node()
        x.key = key
        size++
        head = insert(x, head)
        min = when {
            min == null -> head
            greater(min!!.key, key) -> head
            else -> min
        }
    }

    /**
     * Gets the minimum key currently in the queue
     * Worst case is O(1)
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the minimum key currently in the priority queue
     */
    fun minKey(): Key {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        return min!!.key
    }

    /**
     * Deletes the minimum key
     * Worst case is O(log(n)) (amortized)
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the minimum key
     */
    fun delMin(): Key {
        val min = this.min ?: throw NoSuchElementException("Priority queue is empty")
        head = cut(min, head)
        val x = min.child
        val key = min.key
        if (x != null) {
            head = meld(head, x)
            min.child = null
        }
        size--
        if (!isEmpty)
            consolidate()
        else
            this.min = null
        return key
    }

    /**
     * Merges two heaps together
     * This operation is destructive
     * Worst case is O(1)
     * @param that a Fibonacci heap
     * @return the union of the two heaps
     */
    fun union(that: FibonacciMinPQ<Key>): FibonacciMinPQ<Key> {
        this.head = meld(head, that.head)
        this.min = if (greater(this.min!!.key, that.min!!.key)) that.min else this.min
        this.size = this.size + that.size
        return this
    }

    /*************************************
     * General helper functions
     */

    //Compares two keys
    private fun greater(n: Key?, m: Key?): Boolean {
        if (n == null) return false
        return if (m == null) true else comp.compare(n, m) > 0
    }

    //Assuming root1 holds a greater key than root2, root2 becomes the new root
    private fun link(root1: Node, root2: Node) {
        root2.child = insert(root1, root2.child)
        root2.order++
    }

    /*************************************
     * Function for consolidating all trees in the root list
     */

    //Coalesce the roots, thus reshapes the tree
    private fun consolidate() {
        table.clear()
        var x = head
        var maxOrder = 0
        min = head
        var y: Node?
        var z: Node?
        do {
            y = x
            x = x!!.next
            z = table[y!!.order]
            while (z != null) {
                table.remove(y!!.order)
                if (greater(y.key, z.key)) {
                    link(y, z)
                    y = z
                } else
                    link(z, y)
                z = table[y.order]
            }
            table[y!!.order] = y
            if (y.order > maxOrder) maxOrder = y.order
        } while (x !== head)
        head = null
        for (n in table.values)
            if (n != null) {
                min = if (greater(min!!.key, n.key)) n else min
                head = insert(n, head)
            }
    }

    //Inserts a Node in a circular list containing head, returns a new head
    private fun insert(x: Node, head: Node?): Node {
        if (head == null) {
            x.prev = x
            x.next = x
        } else {
            head.prev!!.next = x
            x.next = head
            x.prev = head.prev
            head.prev = x
        }
        return x
    }

    //Removes a tree from the list defined by the head pointer
    private fun cut(x: Node, head: Node?): Node? {
        return if (x.next === x) {
            x.next = null
            x.prev = null
            null
        } else {
            x.next!!.prev = x.prev
            x.prev!!.next = x.next
            val res = x.next
            x.next = null
            x.prev = null
            if (head === x) res
            else head
        }
    }

    //Merges two root lists together
    private fun meld(x: Node?, y: Node?): Node? {
        if (x == null) return y
        if (y == null) return x
        x.prev!!.next = y.next
        y.next!!.prev = x.prev
        x.prev = y
        y.next = x
        return x
    }

    /**
     * Gets an Iterator over the Keys in the priority queue in ascending order
     * The Iterator does not implement the remove() method
     * iterator() : Worst case is O(n)
     * next() : 	Worst case is O(log(n)) (amortized)
     * hasNext() : 	Worst case is O(1)
     * @return an Iterator over the Keys in the priority queue in ascending order
     */

    override fun iterator(): Iterator<Key> = MyIterator()

    private inner class MyIterator : Iterator<Key> {
        private val copy: FibonacciMinPQ<Key> = FibonacciMinPQ(comp)

        //Constructor takes linear time
        init {
            insertAll(head)
        }

        private fun insertAll(head: Node?) {
            if (head == null) return
            var x = head
            do {
                copy.insert(x!!.key)
                insertAll(x.child)
                x = x.next
            } while (x !== head)
        }

        override fun hasNext() = !copy.isEmpty

        //Takes amortized logarithmic time
        override fun next(): Key {
            if (!hasNext()) throw NoSuchElementException()
            return copy.delMin()
        }
    }

    /*************************************
     * Comparator
     */

    //default Comparator
    private inner class MyComparator : Comparator<Key> {
        override fun compare(key1: Key, key2: Key) = (key1 as Comparable<Key>).compareTo(key2)
    }

    companion object {
        /**
         * Unit tests the `FibonacciMinPQ` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val pq = FibonacciMinPQ<String>()
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