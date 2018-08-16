/******************************************************************************
 * Compilation: javac IndexBinomialMinPQ.java
 * Execution:
 *
 * An index binomial heap.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The IndexBinomialMinPQ class represents an indexed priority queue of generic keys.
 * It supports the usual insert and delete-the-minimum operations,
 * along with delete and change-the-key methods.
 * In order to let the client refer to keys on the priority queue,
 * an integer between 0 and N-1 is associated with each key ; the client
 * uses this integer to specify which key to delete or change.
 * It also supports methods for peeking at the minimum key,
 * testing if the priority queue is empty, and iterating through
 * the keys.
 *
 * This implementation uses a binomial heap along with an array to associate
 * keys with integers in the given range.
 * The insert, delete-the-minimum, delete, change-key, decrease-key,
 * increase-key and size operations take logarithmic time.
 * The is-empty, min-index, min-key, and key-of operations take constant time.
 * Construction takes time proportional to the specified capacity.
 *
 * @author Tristan Claverie
 * @author Jingyi Cheng
 */
@Suppress("UNCHECKED_CAST")
class IndexBinomialMinPQ<Key : Any> : Iterable<Int> {
    private var head: Node<Key>? = null                //Head of the list of roots
    private var nodes: Array<Node<Key>?>? = null            //Array of indexed Nodes of the heap
    private var n: Int = 0                    //Maximum size of the tree
    private val comparator: Comparator<Key>    //Comparator over the keys

    /**
     * Whether the priority queue is empty
     * Worst case is O(1)
     * @return true if the priority queue is empty, false if not
     */
    val isEmpty: Boolean
        get() = head == null

    //Represents a node of a Binomial Tree
    internal inner class Node<Key : Any> {
        lateinit var key: Key                //Key contained by the Node
        var order: Int = 0                //The order of the Binomial Tree rooted by this Node
        var index: Int = 0                //Index associated with the Key
        var parent: Node<Key>? = null            //parent of this Node
        var child: Node<Key>? = null
        var sibling: Node<Key>? = null        //child and sibling of this Node
    }

    /**
     * Initializes an empty indexed priority queue with indices between `0` to `N-1`
     * Worst case is O(n)
     * @param N number of keys in the priority queue, index from `0` to `N-1`
     * @throws IllegalArgumentException if `N < 0`
     */
    constructor(N: Int) {
        if (N < 0) throw IllegalArgumentException("Cannot create a priority queue of negative size")
        comparator = MyComparator()
        nodes = arrayOfNulls<Node<Key>?>(N)
        this.n = N
    }

    /**
     * Initializes an empty indexed priority queue with indices between `0` to `N-1`
     * Worst case is O(n)
     * @param N number of keys in the priority queue, index from `0` to `N-1`
     * @param comparator a Comparator over the keys
     * @throws IllegalArgumentException if `N < 0`
     */
    constructor(N: Int, comparator: Comparator<Key>) {
        if (N < 0) throw IllegalArgumentException("Cannot create a priority queue of negative size")
        this.comparator = comparator
        nodes = arrayOfNulls<Node<Key>?>(N)
        this.n = N
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
        return nodes!![i] != null
    }

    /**
     * Number of elements currently on the priority queue
     * Worst case is O(log(n))
     * @return the number of elements on the priority queue
     */
    fun size(): Int {
        var result = 0
        var tmp: Int
        var node = head
        while (node != null) {
            if (node.order > 30) throw ArithmeticException("The number of elements cannot be evaluated, but the priority queue is still valid.")
            tmp = 1 shl node.order
            result = result or tmp
            node = node.sibling
        }
        return result
    }

    /**
     * Associates a key with an index
     * Worst case is O(log(n))
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
        x.order = 0
        nodes?.set(i, x)
        val H = IndexBinomialMinPQ<Key>()
        H.head = x
        head = union(H).head
    }

    /**
     * Gets the index associated with the minimum key
     * Worst case is O(log(n))
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the index associated with the minimum key
     */
    fun minIndex(): Int {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        var min = head
        var current = head
        while (current!!.sibling != null) {
            min = if (greater(min!!.key, current.sibling!!.key)) current.sibling else min
            current = current.sibling
        }
        return min!!.index
    }

    /**
     * Gets the minimum key currently in the queue
     * Worst case is O(log(n))
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the minimum key currently in the priority queue
     */
    fun minKey(): Key? {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        var min = head
        var current = head
        while (current!!.sibling != null) {
            min = if (greater(min!!.key, current.sibling!!.key)) current.sibling else min
            current = current.sibling
        }
        return min!!.key
    }

    /**
     * Deletes the minimum key
     * Worst case is O(log(n))
     * @throws java.util.NoSuchElementException if the priority queue is empty
     * @return the index associated with the minimum key
     */
    fun delMin(): Int {
        if (isEmpty) throw NoSuchElementException("Priority queue is empty")
        val min = eraseMin()
        var x: Node<Key>? = if (min.child == null) min else min.child
        if (min.child != null) {
            min.child = null
            var prevx: Node<Key>? = null
            var nextx = x!!.sibling
            while (nextx != null) {
                x!!.parent = null // for garbage collection
                x.sibling = prevx
                prevx = x
                x = nextx
                nextx = nextx.sibling
            }
            x!!.parent = null
            x.sibling = prevx
            val H = IndexBinomialMinPQ<Key>()
            H.head = x
            head = union(H).head
        }
        return min.index
    }

    /**
     * Gets the key associated with index i
     * Worst case is O(1)
     * @param i an index
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws IllegalArgumentException if the index is not in the queue
     * @return the key associated with index i
     */
    fun keyOf(i: Int): Key {
        if (i < 0 || i >= n) throw IllegalArgumentException()
        if (!contains(i)) throw IllegalArgumentException("Specified index is not in the queue")
        return nodes!![i]!!.key
    }

    /**
     * Changes the key associated with index i to the given key
     * Worst case is O(log(n))
     * @param i an index
     * @param key the key to associate with i
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws IllegalArgumentException if the index has no key associated with
     */
    fun changeKey(i: Int, key: Key) {
        if (i < 0 || i >= n) throw IllegalArgumentException()
        if (!contains(i)) throw IllegalArgumentException("Specified index is not in the queue")
        if (greater(nodes!![i]?.key, key))
            decreaseKey(i, key)
        else
            increaseKey(i, key)
    }

    /**
     * Decreases the key associated with index i to the given key
     * Worst case is O(log(n))
     * @param i an index
     * @param key the key to associate with i
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws java.util.NoSuchElementException if the index has no key associated with
     * @throws IllegalArgumentException if the given key is greater than the current key
     */
    fun decreaseKey(i: Int, key: Key) {
        if (i < 0 || i >= n) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        val x = nodes!![i] ?: throw NoSuchElementException("Specified index is not in the queue")
        if (greater(key, x.key)) throw IllegalArgumentException("Calling with this argument would not decrease the key")
        x.key = key
        swim(i)
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
        if (greater(nodes!![i]!!.key, key)) throw IllegalArgumentException("Calling with this argument would not increase the key")
        delete(i)
        insert(i, key)
    }

    /**
     * Deletes the key associated the given index
     * Worst case is O(log(n))
     * @param i an index
     * @throws IllegalArgumentException if the specified index is invalid
     * @throws java.util.NoSuchElementException if the given index has no key associated with
     */
    fun delete(i: Int) {
        if (i < 0 || i >= n) throw IllegalArgumentException()
        if (!contains(i)) throw NoSuchElementException("Specified index is not in the queue")
        toTheRoot(i)
        var x: Node<Key>? = erase(i)
        if (x!!.child != null) {
            val y = x
            x = x.child
            y.child = null
            var prevx: Node<Key>? = null
            var nextx = x!!.sibling
            while (nextx != null) {
                x!!.parent = null
                x.sibling = prevx
                prevx = x
                x = nextx
                nextx = nextx.sibling
            }
            x!!.parent = null
            x.sibling = prevx
            val H = IndexBinomialMinPQ<Key>()
            H.head = x
            head = union(H).head
        }
    }

    //Compares two keys
    private fun greater(n: Key?, m: Key?): Boolean = when {
        n == null -> false
        m == null -> true
        else -> comparator.compare(n, m) > 0
    }

    //Exchanges the positions of two nodes
    private fun exchange(x: Node<Key>, y: Node<Key>) {
        val tempKey = x.key
        x.key = y.key
        y.key = tempKey
        val tempInt = x.index
        x.index = y.index
        y.index = tempInt
        nodes?.set(x.index, x)
        nodes?.set(y.index, y)
    }

    //Assuming root1 holds a greater key than root2, root2 becomes the new root
    private fun link(root1: Node<Key>, root2: Node<Key>) {
        root1.sibling = root2.child
        root1.parent = root2
        root2.child = root1
        root2.order++
    }

    //Moves a Node upward
    private fun swim(i: Int) {
        val x = nodes!![i] ?: throw NoSuchElementException("Specified index is not in the queue")
        val parent = x.parent
        if (parent != null && greater(parent.key, x.key)) {
            exchange(x, parent)
            swim(i)
        }
    }

    //The key associated with i becomes the root of its Binomial Tree,
    //regardless of the order relation defined for the keys
    private fun toTheRoot(i: Int) {
        val x = nodes!![i] ?: throw NoSuchElementException("Specified index is not in the queue")
        val parent = x.parent
        if (parent != null) {
            exchange(x, parent)
            toTheRoot(i)
        }
    }

    //Assuming the key associated with i is in the root list,
    //deletes and return the node of index i
    private fun erase(i: Int): Node<Key> {
        val reference = nodes!![i] ?: throw NoSuchElementException("Specified index is not in the queue")
        var x = head ?: throw NoSuchElementException("Priority queue is empty")
        lateinit var previous: Node<Key>
        while (x !== reference) {
            previous = x
            x = x.sibling!!
        }
        previous.sibling = x.sibling
        if (x === head) head = head!!.sibling
        nodes?.set(i, null)
        return x
    }

    //Deletes and return the node containing the minimum key
    private fun eraseMin(): Node<Key> {
        var min = head
        var previous: Node<Key>? = null
        var current = head
        while (current!!.sibling != null) {
            if (greater(min!!.key, current.sibling!!.key)) {
                previous = current
                min = current.sibling
            }
            current = current.sibling
        }
        previous!!.sibling = min!!.sibling
        if (min === head) head = min.sibling
        nodes?.set(min.index, null)
        return min
    }

    //Merges two root lists into one, there can be up to 2 Binomial Trees of same order
    private fun merge(h: Node<Key>, x: Node<Key>?, y: Node<Key>?): Node<Key> {
        when {
            x == null && y == null -> return h
            x == null -> h.sibling = merge(y!!, null, y.sibling)
            y == null -> h.sibling = merge(x, x.sibling, null)
            x.order < y.order -> h.sibling = merge(x, x.sibling, y)
            else -> h.sibling = merge(y, x, y.sibling)
        }
        return h
    }

    //Merges two Binomial Heaps together and returns the resulting Binomial Heap
    //It destroys the two Heaps in parameter, which should not be used any after.
    //To guarantee logarithmic time, this function assumes the arrays are up-to-date
    private fun union(heap: IndexBinomialMinPQ<Key>): IndexBinomialMinPQ<Key> {
        this.head = merge(Node(), this.head, heap.head).sibling
        var x = this.head
        var prevx: Node<Key>? = null
        var nextx = x!!.sibling
        while (nextx != null) {
            if (x!!.order < nextx.order || nextx.sibling != null && nextx.sibling!!.order == x.order) {
                prevx = x
                x = nextx
            } else if (greater(nextx.key, x.key)) {
                x.sibling = nextx.sibling
                link(nextx, x)
            } else {
                if (prevx == null)
                    this.head = nextx
                else
                    prevx.sibling = nextx
                link(x, nextx)
                x = nextx
            }
            nextx = x.sibling
        }
        return this
    }

    //Creates an empty heap
    //The comparator is instanciated because it needs to,
    //but won't be used by any heap created by this constructor
    private constructor() {
        comparator = MyComparator()
    }

    /**
     * Gets an Iterator over the indexes in the priority queue in ascending order
     * The Iterator does not implement the remove() method
     * iterator() : Worst case is O(n)
     * next() : 	Worst case is O(log(n))
     * hasNext() : 	Worst case is O(1)
     * @return an Iterator over the indexes in the priority queue in ascending order
     */
    override fun iterator(): Iterator<Int> = MyIterator()

    private inner class MyIterator : Iterator<Int> {
        internal var data: IndexBinomialMinPQ<Key> = IndexBinomialMinPQ(n, comparator)

        //Constructor clones recursively the elements in the queue
        //It takes linear time
        init {
            data.head = clone(head, null)
        }

        private fun clone(x: Node<Key>?, parent: Node<Key>?): Node<Key>? {
            if (x == null) return null
            val node = Node<Key>()
            node.index = x.index
            node.key = x.key
            data.nodes?.set(node.index, node)
            node.parent = parent
            node.sibling = clone(x.sibling, parent)
            node.child = clone(x.child, node)
            return node
        }

        override fun hasNext() = !data.isEmpty

        override fun next(): Int {
            if (!hasNext()) throw NoSuchElementException()
            return data.delMin()
        }
    }

    /***************************
     * Comparator
     */

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
            val pq = IndexBinomialMinPQ<String>()
            StdOut.println("Checking isEmpty ... " + pq.isEmpty)
            StdOut.println("(${pq.size()} left on pq)")
            val items = arrayOf("A", "B", "C", "D", "E", "F", null, "G")
            try {
                for ((i, x) in items.withIndex())
                    pq.insert(i, x)
            } catch (e: IllegalArgumentException) {
                StdOut.println(e.message)
            }

            StdOut.println("Checking minIndex() and min() ... The minimum is ${pq.minKey()} at ${pq.minIndex()}")

            for ((i, x) in pq.withIndex())
                StdOut.println("$i -> $x")
            StdOut.println("(${pq.size()} left on pq)")

            StdOut.println("Deleting 4 elements with highest priority ...")
            for (i in 0..3)
                pq.delMin()
            for ((i, x) in pq.withIndex())
                StdOut.println("$i -> $x")
            StdOut.println("(${pq.size()} left on pq)")

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