/******************************************************************************
 * Compilation:  javac BST.java
 * Execution:    java BST
 * Dependencies: StdIn.kt StdOut.kt Queue.kt
 * Data files:   https://algs4.cs.princeton.edu/32bst/tinyST.txt
 *
 * A symbol table implemented with a binary search tree.
 *
 * % more tinyST.txt
 * S E A R C H E X A M P L E
 *
 * % java BST < tinyST.txt
 * A 8
 * C 4
 * E 12
 * H 5
 * L 11
 * M 9
 * P 10
 * R 3
 * S 0
 * X 7
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `BST` class represents an ordered symbol table of generic
 * key-value pairs.
 * It supports the usual *put*, *get*, *contains*,
 * *delete*, *size*, and *is-empty* methods.
 * It also provides ordered methods for finding the *minimum*,
 * *maximum*, *floor*, *select*, *ceiling*.
 * It also provides a *keys* method for iterating over all of the keys.
 * A symbol table implements the *associative array* abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike [java.util.Map], this class uses the convention that
 * values cannot be `null`—setting the
 * value associated with a key to `null` is equivalent to deleting the key
 * from the symbol table.
 *
 *
 * This implementation uses an (unbalanced) binary search tree. It requires that
 * the key type implements the `Comparable` interface and calls the
 * `compareTo()` and method to compare two keys. It does not call either
 * `equals()` or `hashCode()`.
 * The *put*, *contains*, *remove*, *minimum*,
 * *maximum*, *ceiling*, *floor*, *select*, and
 * *rank*  operations each take
 * linear time in the worst case, if the tree becomes unbalanced.
 * The *size*, and *is-empty* operations take constant time.
 * Construction takes constant time.
 *
 *
 * For additional documentation, see [Section 3.2](https://algs4.cs.princeton.edu/32bst) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 * For other implementations, see [ST], [BinarySearchST],
 * [SequentialSearchST], [RedBlackBST],
 * [SeparateChainingHashST], and [LinearProbingHashST],
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
/**
 * Initializes an empty symbol table.
 */
class BST<Key : Comparable<Key>, Value> {
    private var root: Node? = null             // root of BST
    var size : Int = 0
        get() = size(root)
        private set

    /**
     * Returns true if this symbol table is empty.
     * @return `true` if this symbol table is empty; `false` otherwise
     */
    val isEmpty: Boolean
        get() = size == 0

    // return number of key-value pairs in BST rooted at x
    private fun size(x: Node?): Int {
        return x?.size ?: 0
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private val isBST: Boolean
        get() = isBST(root, null, null)

    // are the size fields correct?
    private val isSizeConsistent: Boolean
        get() = isSizeConsistent(root)

    // check that ranks are consistent
    private val isRankConsistent: Boolean
        get() {
            for (i in 0 until size)
                if (i != rank(select(i))) return false
            for (key in keys())
                if (key != select(rank(key))) return false
            return true
        }

    private inner class Node( val key: Key            // sorted by key
                             , var `val`: Value       // associated data
                             , var size: Int          // number of nodes in subtree
    ) {
        var left: Node? = null
        var right: Node? = null  // left and right subtrees
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param  key the key
     * @return `true` if this symbol table contains `key` and
     * `false` otherwise
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun contains(key: Key?): Boolean {
        return get(key ?: throw IllegalArgumentException("argument to contains() is null")) != null
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and `null` if the key is not in the symbol table
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun get(key: Key?): Value? {
        return get(root, key)
    }

    private operator fun get(x: Node?, key: Key?): Value? {
        if (key == null) throw IllegalArgumentException("calls get() with a null key")
        if (x == null) return null
        return when {
            key < x.key -> get(x.left, key)
            key > x.key -> get(x.right, key)
            else -> x.`val`
        }
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is `null`.
     *
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun put(key: Key?, `val`: Value?) {
        if (key == null) throw IllegalArgumentException("calls put() with a null key")
        if (`val` == null) {
            delete(key)
            return
        }
        root = put(root, key, `val`)
        assert(check())
    }

    private fun put(x: Node?, key: Key, `val`: Value): Node {
        if (x == null) return Node(key, `val`, 1)

        when {
            key < x.key -> x.left = put(x.left, key, `val`)
            key > x.key -> x.right = put(x.right, key, `val`)
            else -> x.`val` = `val`
        }
        x.size = 1 + size(x.left) + size(x.right)
        return x
    }

    /**
     * Removes the smallest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun deleteMin() {
        if (isEmpty) throw NoSuchElementException("Symbol table underflow")
        root = deleteMin(root!!)
        assert(check())
    }

    private fun deleteMin(x: Node): Node? {
        x.left = deleteMin(x.left ?: return x.right)
        x.size = size(x.left) + size(x.right) + 1
        return x
    }

    /**
     * Removes the largest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun deleteMax() {
        if (isEmpty) throw NoSuchElementException("Symbol table underflow")
        root = deleteMax(root!!)
        assert(check())
    }

    private fun deleteMax(x: Node): Node? {
        x.right = deleteMax(x.right ?: return x.left)
        x.size = size(x.left) + size(x.right) + 1
        return x
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param  key the key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun delete(key: Key?) {
        if (key == null) throw IllegalArgumentException("calls delete() with a null key")
        root = delete(root, key)
        assert(check())
    }

    private fun delete(x: Node?, key: Key): Node? {
        var x: Node = x ?: return null
        when {
            key < x.key -> x.left = delete(x.left, key)
            key > x.key -> x.right = delete(x.right, key)
            else -> {
                val left = x.left ?: return x.right
                val right = x.right ?: return left
                x = min(right)
                x.right = deleteMin(right)
                x.left = left
            }
        }
        x.size = size(x.left) + size(x.right) + 1
        return x
    }

    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun min(): Key {
        return min(root ?: throw NoSuchElementException("calls min() with empty symbol table")).key
    }

    private fun min(x: Node): Node {
        val left = x.left
        return if (left == null) x else min(left)
    }

    /**
     * Returns the largest key in the symbol table.
     *
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun max(): Key {
        return max(root ?: throw NoSuchElementException("calls max() with empty symbol table")).key
    }

    private fun max(x: Node): Node {
        val right = x.right
        return if (right == null) x else max(right)
    }

    /**
     * Returns the largest key in the symbol table less than or equal to `key`.
     *
     * @param  key the key
     * @return the largest key in the symbol table less than or equal to `key`
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun floor(key: Key?): Key? {
        if (key == null) throw IllegalArgumentException("argument to floor() is null")
        if (isEmpty) throw NoSuchElementException("calls floor() with empty symbol table")
        val x = floor(root, key)
        return x?.key
    }

    private fun floor(x: Node?, key: Key): Node? {
        return when {
            x == null -> null
            key == x.key -> x
            key < x.key -> floor(x.left, key)
            else -> floor(x.right, key) ?: x
        }
    }

    fun floor2(key: Key): Key? {
        return floor2(root, key, null)
    }

    private fun floor2(x: Node?, key: Key, best: Key?): Key? {
        return when {
            x == null -> best
            key < x.key -> floor2(x.left, key, best)
            key > x.key -> floor2(x.right, key, x.key)
            else -> x.key
        }
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to `key`.
     *
     * @param  key the key
     * @return the smallest key in the symbol table greater than or equal to `key`
     * @throws NoSuchElementException if there is no such key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun ceiling(key: Key?): Key? {
        if (key == null) throw IllegalArgumentException("argument to ceiling() is null")
        if (isEmpty) throw NoSuchElementException("calls ceiling() with empty symbol table")
        val x = ceiling(root, key)
        return x?.key
    }

    private fun ceiling(x: Node?, key: Key): Node? {
        return when {
            x == null -> null
            key == x.key -> x
            key < x.key -> ceiling(x.left, key) ?: x
            else -> ceiling(x.right, key)
        }
    }

    /**
     * Return the key in the symbol table whose rank is `k`.
     * This is the (k+1)st smallest key in the symbol table.
     *
     * @param  k the order statistic
     * @return the key in the symbol table of rank `k`
     * @throws IllegalArgumentException unless `k` is between 0 and
     * *n*–1
     */
    fun select(k: Int): Key {
        if (k < 0 || k >= size) {
            throw IllegalArgumentException("argument to select() is invalid: $k")
        }
        val x = select(root, k)
        return x!!.key
    }

    // Return key of rank k.
    private fun select(x: Node?, k: Int): Node? {
        if (x == null) return null
        val t = size(x.left)
        return when {
            t > k -> select(x.left, k)
            t < k -> select(x.right, k - t - 1)
            else -> x
        }
    }

    /**
     * Return the number of keys in the symbol table strictly less than `key`.
     *
     * @param  key the key
     * @return the number of keys in the symbol table strictly less than `key`
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun rank(key: Key?): Int {
        return rank(key ?: throw IllegalArgumentException("argument to rank() is null"), root)
    }

    // Number of keys in the subtree less than key.
    private fun rank(key: Key, x: Node?): Int {
        return when {
            x == null -> 0
            key < x.key -> rank(key, x.left)
            key > x.key -> 1 + size(x.left) + rank(key, x.right)
            else -> size(x.left)
        }
    }

    /**
     * Returns all keys in the symbol table as an `Iterable`.
     * To iterate over all of the keys in the symbol table named `st`,
     * use the foreach notation: `for (Key key : st.keys())`.
     *
     * @return all keys in the symbol table
     */
    fun keys(): nnQueue<Key> {
        return try {
            keys(min(), max())
        }catch (e:NoSuchElementException){
            nnQueue()
        }
    }

    /**
     * Returns all keys in the symbol table in the given range,
     * as an `Iterable`.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return all keys in the symbol table between `lo`
     * (inclusive) and `hi` (inclusive)
     * @throws IllegalArgumentException if either `lo` or `hi`
     * is `null`
     */
    fun keys(lo: Key?, hi: Key?): nnQueue<Key> {
        if (lo == null) throw IllegalArgumentException("first argument to keys() is null")
        if (hi == null) throw IllegalArgumentException("second argument to keys() is null")
        val queue = nnQueue<Key>()
        keys(root, queue, lo, hi)
        return queue
    }

    private fun keys(x: Node?, queue: nnQueue<Key>, lo: Key, hi: Key) {
        if (x == null) return
        if (lo < x.key) keys(x.left, queue, lo, hi)
        if (x.key in lo..hi) queue.enqueue(x.key)
        if (hi > x.key) keys(x.right, queue, lo, hi)
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return the number of keys in the symbol table between `lo`
     * (inclusive) and `hi` (inclusive)
     * @throws IllegalArgumentException if either `lo` or `hi`
     * is `null`
     */
    fun size(lo: Key?, hi: Key?): Int {
        if (lo == null) throw IllegalArgumentException("first argument to size() is null")
        if (hi == null) throw IllegalArgumentException("second argument to size() is null")
        return when {
            lo > hi -> 0
            contains(hi) -> rank(hi) - rank(lo) + 1
            else -> rank(hi) - rank(lo)
        }
    }

    /**
     * Returns the height of the BST (for debugging).
     *
     * @return the height of the BST (a 1-node tree has height 0)
     */
    fun height() = height(root)


    private fun height(x: Node?): Int {
        return if (x == null) -1 else 1 + Math.max(height(x.left), height(x.right))
    }

    /**
     * Returns the keys in the BST in level order (for debugging).
     *
     * @return the keys in the BST in level order traversal
     */
    fun keysLevelOrder(): nnQueue<Key> {
        val keys = nnQueue<Key>()
        val queue = Queue<Node>()
        queue.enqueue(root)
        while (!queue.isEmpty) {
            val x = queue.dequeue() ?: continue
            keys.enqueue(x.key)
            queue.enqueue(x.left)
            queue.enqueue(x.right)
        }
        return keys
    }

    /*************************************************************************
     * Check integrity of BST data structure.
     */
    private fun check(): Boolean {
        if (!isBST) StdOut.println("Not in symmetric order")
        if (!isSizeConsistent) StdOut.println("Subtree counts not consistent")
        if (!isRankConsistent) StdOut.println("Ranks not consistent")
        return isBST && isSizeConsistent && isRankConsistent
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private fun isBST(x: Node?, min: Key?, max: Key?): Boolean {
        if (x == null) return true
        if (min != null && x.key <= min) return false
        return if (max != null && x.key >= max) false
        else isBST(x.left, min, x.key) && isBST(x.right, x.key, max)
    }

    private fun isSizeConsistent(x: Node?): Boolean {
        if (x == null) return true
        return if (x.size != size(x.left) + size(x.right) + 1) false
        else isSizeConsistent(x.left) && isSizeConsistent(x.right)
    }

    companion object {
        /**
         * Unit tests the `BST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val st = BST<String, Int>()
            StdOut.println("Checking isEmpty ... "+st.isEmpty)
            val items = arrayOf("A", "B", "C", "D", "E", "F", null, "G")
            try {
                for ((i, key) in items.withIndex()){
                    st.put(key, i)
                }
            }catch(e:IllegalArgumentException){
                StdOut.println(e.message)
            }
            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Checking put('A', 10) ... \nOutput in keysLevelOrder() ...")
            st.put("A", 10)
            for (s in st.keysLevelOrder())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Checking delete('E') ...")
            st.delete("E")
            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.print("Checking size('B', 'D') ... ")
            StdOut.println(st.size("B", "D"))

            StdOut.print("Checking size('B', 'F') ... ")
            StdOut.println(st.size("B", "F"))

            StdOut.print("Checking ceiling('D') ... ")
            StdOut.println(st.ceiling("D"))

            StdOut.print("Checking floor('E') ... ")
            StdOut.println(st.floor("E"))

            StdOut.print("Checking ceiling('E') ... ")
            StdOut.println(st.ceiling("E"))

            StdOut.print("Checking rank('E') ... ")
            StdOut.println(st.rank("E"))

            StdOut.print("Checking select(2) ... ")
            StdOut.println(st.select(2))

            StdOut.println("Checking isEmpty ... "+st.isEmpty)


            StdOut.println("Deleting max... ${st.max()}")
            st.deleteMax()

            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Deleting min... ${st.min()}")
            st.deleteMin()

            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Deleting max and min...${st.max()} and ${st.min()}")
            st.delete(st.max())
            st.delete(st.min())
            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Deleting last element in table ... ${st.min()}")
            st.put("C", null)
            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")
            StdOut.println("Checking isEmpty ... "+st.isEmpty)

            StdOut.println("End of test")
        }
    }
}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
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
 ******************************************************************************/
