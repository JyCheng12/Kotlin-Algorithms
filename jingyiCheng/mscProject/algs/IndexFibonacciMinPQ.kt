/******************************************************************************
 * Compilation: javac IndexFibonacciMinPQ.java
 * Execution:
 *
 * An index Fibonacci heap.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The IndexFibonacciMinPQ class represents an indexed priority queue of generic keys.
 * It supports the usual insert and delete-the-minimum operations,
 * along with delete and change-the-key methods.
 * In order to let the client refer to keys on the priority queue,
 * an integer between 0 and N-1 is associated with each key ; the client
 * uses this integer to specify which key to delete or change.
 * It also supports methods for peeking at the minimum key,
 * testing if the priority queue is empty, and iterating through
 * the keys.
 *
 * This implementation uses a Fibonacci heap along with an array to associate
 * keys with integers in the given range.
 * The insert, size, is-empty, contains, minimum-index, minimum-key
 * and key-of take constant time.
 * The decrease-key operation takes amortized constant time.
 * The delete, increase-key, delete-the-minimum, change-key take amortized logarithmic time.
 * Construction takes time proportional to the specified capacity
 *
 * @author Tristan Claverie
 * @author Jingyi Cheng
 */

class IndexFibonacciMinPQ<Key : Any> : Iterable<Int> {
    private var nodes: Array<Node<Key>?> = arrayOfNulls(0)          //Array of Nodes in the heap
    private var head: Node<Key>? = null                             //Head of the circular root list
    private var min: Node<Key>? = null                              //Minimum Node in the heap
    var size: Int = 0                                               //Number of keys in the heap
        private set
    private var n: Int = 0                                          //Maximum number of elements in the heap
    private val comp: Comparator<Key>                               //Comparator over the keys
    private val table = HashMap<Int, Node<Key>>()                   //Used for the consolidate operation

    val isEmpty: Boolean
        get() = size == 0

    //Represents a Node of a tree
    internal inner class Node<Key : Any> {
        lateinit var key: Key                   //Key of the Node
        var order: Int = 0                      //The order of the tree rooted by this Node
        var index: Int = 0                      //Index associated with the key
        var prev: Node<Key>? = null
        var next: Node<Key>? = null             //siblings of the Node
        var parent: Node<Key>? = null
        var child: Node<Key>? = null            //parent and child of this Node
        var mark: Boolean = false               //Indicates if this Node already lost a child
    }

    /**
     * Initializes an empty indexed priority queue with indices between `0` and `N-1`
     * Worst case is O(n)
     * @param N number of keys in the priority queue, index from `0` to `N-1`
     * @throws IllegalArgumentException if `N < 0`
     */
    constructor(N: Int) {
        if (N < 0) throw IllegalArgumentException("Cannot create a priority queue of negative size")
        n = N
        nodes = arrayOfNulls<Node<Key>?>(n)
        comp = MyComparator()
    }

    /**
     * Initializes an empty indexed priority queue with indices between `0` and `N-1`
     * Worst case is O(n)
     * @param N number of keys in the priority queue, index from `0` to `N-1`
     * @param C a Comparator over the keys
     * @throws IllegalArgumentException if `N < 0`
     */
    constructor(C: Comparator<Key>, N: Int) {
        if (N < 0) throw IllegalArgumentException("Cannot create a priority queue of negative size")
        n = N
        nodes = arrayOfNulls<Node<Key>?>(n)
        comp = C
    }

    /**
     * Does the priority queue contains the index i ?
     * Worst case is O(1)
     * @param i an index
     * @throws IllegalArgumentException if the specified index is invalid
     * @return true if i is on the priority queue, false if not
     */
    operator fun contains(i: Int): Boolean {
        if (i < 0 || i >= n) throw IllegalArgumentException()
        return nodes[i] != null
    }

    /**
     * Associates a key with an index
     * Worst case is O(1)
     * @param i an index
     * @param key a Key associated with i
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws IllegalArgumentException if the index is already in the queue
     */
    fun insert(i: Int, key: Key?) {
        if (key == null) throw IllegalArgumentException("Argument to insert() is null")
        if (i < 0 || i >= n) throw IllegalArgumentException()
        if (contains(i)) throw IllegalArgumentException("Specified index is already in the queue")
        val x = Node<Key>()
        x.key = key
        x.index = i
        nodes[i] = x
        size++
        head = insert(x, head)
        val min = this.min
        this.min = when {
            min == null -> head
            greater(min.key, key) -> head
            else -> min
        }
    }

    /**
     * Get the index associated with the minimum key
     * Worst case is O(1)
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the index associated with the minimum key
     */
    fun minIndex(): Int {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        return min!!.index
    }

    /**
     * Get the minimum key currently in the queue
     * Worst case is O(1)
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the minimum key currently in the priority queue
     */
    fun minKey(): Key? {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        return min!!.key
    }

    /**
     * Delete the minimum key
     * Worst case is O(log(n)) (amortized)
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the index associated with the minimum key
     */
    fun delMin(): Int {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        val min = this.min!!
        head = cut(min, head)
        var x = min.child
        val index = min.index
        if (x != null) {
            do {
                x!!.parent = null
                x = x.next
            } while (x !== min.child)
            head = meld(head, x)
        }
        size--
        if (!isEmpty)
            consolidate()
        else
            this.min = null
        nodes[index] = null
        return index
    }

    /**
     * Get the key associated with index i
     * Worst case is O(1)
     * @param i an index
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws java.util.NoSuchElementException if the index is not in the queue
     * @return the key associated with index i
     */
    fun keyOf(i: Int): Key {
        if (i < 0 || i >= n) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        return nodes[i]!!.key
    }

    /**
     * Changes the key associated with index i to the given key
     * If the given key is greater, Worst case is O(log(n))
     * If the given key is lower, Worst case is O(1) (amortized)
     * @param i an index
     * @param key the key to associate with i
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws java.util.NoSuchElementException if the index has no key associated with
     */
    fun changeKey(i: Int, key: Key) {
        if (i < 0 || i >= n) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        if (greater(key, nodes[i]!!.key))
            increaseKey(i, key)
        else
            decreaseKey(i, key)
    }

    /**
     * Decreases the key associated with index i to the given key
     * Worst case is O(1) (amortized).
     * @param i an index
     * @param key the key to associate with i
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws java.util.NoSuchElementException if the index has no key associated with
     * @throws IllegalArgumentException if the given key is greater than the current key
     */
    fun decreaseKey(i: Int, key: Key) {
        if (i < 0 || i >= n) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        val x = nodes[i] ?: throw NoSuchElementException("Specified index is not in the queue")
        if (greater(key, x.key)) throw IllegalArgumentException("Calling with this argument would not decrease the key")
        x.key = key
        if (greater(min!!.key, key))
            min = x
        if (x.parent != null && greater(x.parent!!.key, key)) {
            cut(i)
        }
    }

    /**
     * Increases the key associated with index i to the given key
     * Worst case is O(log(n))
     * @param i an index
     * @param key the key to associate with i
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws java.util.NoSuchElementException if the index has no key associated with
     * @throws IllegalArgumentException if the given key is lower than the current key
     */
    fun increaseKey(i: Int, key: Key) {
        if (i < 0 || i >= n) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        if (greater(nodes[i]!!.key, key)) throw IllegalArgumentException("Calling with this argument would not increase the key")
        delete(i)
        insert(i, key)
    }

    /**
     * Deletes the key associated the given index
     * Worst case is O(log(n)) (amortized)
     * @param i an index
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws java.util.NoSuchElementException if the given index has no key associated with
     */
    fun delete(i: Int) {
        if (i < 0 || i >= n) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        var x: Node<Key>? = nodes[i]
        if (x?.parent != null) cut(i)
        head = cut(x!!, head)
        if (x.child != null) {
            var child = x.child
            x = x.child
            do {
                child?.parent = null
                child = child?.next
            } while (child !== x)
            head = meld(head, child)
        }
        if (!isEmpty) consolidate()
        else min = null
        nodes[i] = null
        size--
    }

    //Compares two keys
    private fun greater(n: Key?, m: Key?): Boolean {
        if (n == null) return false
        return if (m == null) true else comp.compare(n, m) > 0
    }

    //Assuming root1 holds a greater key than root2, root2 becomes the new root
    private fun link(root1: Node<Key>, root2: Node<Key>) {
        root1.parent = root2
        root2.child = insert(root1, root2.child)
        root2.order++
    }

    //Removes a Node from its parent's child list and insert it in the root list
    //If the parent Node already lost a child, reshapes the heap accordingly
    private fun cut(i: Int) {
        val x = nodes[i] ?: throw NoSuchElementException("Specified index is not in the queue")
        val parent = x.parent
        parent!!.child = cut(x, parent.child)
        x.parent = null
        parent.order--
        head = insert(x, head)
        parent.mark = !parent.mark
        if (!parent.mark && parent.parent != null)
            cut(parent.index)
    }

    //Coalesces the roots, thus reshapes the heap
    //Caching a HashMap improves greatly performances
    private fun consolidate() {
        table.clear()
        var x = head
        var maxOrder = 0
        min = head
        var y: Node<Key>?
        var z: Node<Key>?
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
            if (y.order > maxOrder)
                maxOrder = y.order
        } while (x !== head)
        head = null
        for (n in table.values) {
            this.min = if (greater(min?.key, n.key)) n else min
            head = insert(n, head)
        }
    }

    //Inserts a Node in a circular list containing head, returns a new head
    private fun insert(x: Node<Key>, head: Node<Key>?): Node<Key> {
        if (head == null) {
            x.prev = x
            x.next = x
        } else {
            head.prev?.next = x
            x.next = head
            x.prev = head.prev
            head.prev = x
        }
        return x
    }

    //Removes a tree from the list defined by the head pointer
    private fun cut(x: Node<Key>, head: Node<Key>?): Node<Key>? {
        return if (x.next === x) {
            x.next = null
            x.prev = null
            null
        } else {
            x.next?.prev = x.prev
            x.prev?.next = x.next
            val res = x.next
            x.next = null
            x.prev = null
            if (head === x) res
            else head
        }
    }

    //Merges two lists together.
    private fun meld(x: Node<Key>?, y: Node<Key>?): Node<Key>? {
        if (x == null) return y
        if (y == null) return x
        x.prev?.next = y.next
        y.next?.prev = x.prev
        x.prev = y
        y.next = x
        return x
    }

    /**
     * Get an Iterator over the indexes in the priority queue in ascending order
     * The Iterator does not implement the remove() method
     * iterator() : Worst case is O(n)
     * next() : 	Worst case is O(log(n)) (amortized)
     * hasNext() : 	Worst case is O(1)
     * @return an Iterator over the indexes in the priority queue in ascending order
     */
    override fun iterator(): Iterator<Int> = MyIterator()

    private inner class MyIterator : Iterator<Int> {
        private val copy: IndexFibonacciMinPQ<Key> = IndexFibonacciMinPQ(comp, n)

        //Constructor takes linear time
        init {
            for (x in nodes)
                if (x != null) copy.insert(x.index, x.key)
        }

        override fun hasNext() = !copy.isEmpty

        //Takes amortized logarithmic time
        override fun next(): Int {
            if (!hasNext()) throw NoSuchElementException()
            return copy.delMin()
        }
    }

    //default Comparator
    private inner class MyComparator : Comparator<Key> {
        override fun compare(key1: Key, key2: Key) = (key1 as Comparable<Key>).compareTo(key2)
    }

    companion object {
        /**
         * Unit tests the `MinPQ` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val pq = IndexFibonacciMinPQ<String>(6)
            StdOut.println("Checking isEmpty ... " + pq.isEmpty)
            StdOut.println("(${pq.size} left on pq)")
            val items = arrayOf("A", "B", "C", "D", "E", "F", null, "G")
            try {
                for ((i, x) in items.withIndex())
                    pq.insert(i, x)
            } catch (e: IllegalArgumentException) {
                StdOut.println(e.message)
            }

            var current = pq.head
            for (i in 1..pq.size) {
                StdOut.println("$i -> ${current?.key}")
                current = current?.next
            }
            StdOut.println("(${pq.size} left on pq)")
            StdOut.println("Checking isEmpty ... ${pq.isEmpty}")

            StdOut.println("Deleting 4 elements with highest priority ...")
            for (i in 0..3)
                pq.delMin()
            current = pq.head
            for (i in 1..pq.size) {
                StdOut.println("$i -> ${current?.key}")
                current = current?.child
            }
            StdOut.println("(${pq.size} left on pq)")

            StdOut.println("Deleting all elements ...")
            for (i in 1..pq.size) {
                StdOut.println(pq.minKey())
                pq.delMin()
            }
            for (i in 1..pq.size) {
                StdOut.println("$i -> ${current?.key}")
                current = current!!.next
            }
            StdOut.println("(${pq.size} left on pq)")
            StdOut.println("Checking isEmpty ... ${pq.isEmpty}")
            StdOut.println("End of test.")
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
