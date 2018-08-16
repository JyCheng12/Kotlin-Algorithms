/******************************************************************************
 * Compilation:  javac AVLTreeST.java
 * Execution:    java AVLTreeST < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/33balanced/tinyST.txt
 *
 * A symbol table implemented using an AVL tree.
 *
 * % more tinyST.txt
 * S E A R C H E X A M P L E
 *
 * % java AVLTreeST < tinyST.txt
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
 * The `AVLTreeST` class represents an ordered symbol table of
 * generic key-value pairs. It supports the usual *put*, *get*,
 * *contains*, *delete*, *size*, and *is-empty*
 * methods. It also provides ordered methods for finding the *minimum*,
 * *maximum*, *floor*, and *ceiling*. It also provides a
 * *keys* method for iterating over all of the keys. A symbol table
 * implements the *associative array* abstraction: when associating a
 * value with a key that is already in the symbol table, the convention is to
 * replace the old value with the new value. Unlike [java.util.Map], this
 * class uses the convention that values cannot be `null`
 * —setting the value associated with a key to `null` is
 * equivalent to deleting the key from the symbol table.
 *
 *
 * This symbol table implementation uses internally an
 * [ AVL tree ](https://en.wikipedia.org/wiki/AVL_tree) (Georgy
 * Adelson-Velsky and Evgenii Landis' tree) which is a self-balancing BST.
 * In an AVL tree, the heights of the two child subtrees of any
 * node differ by at most one; if at any time they differ by more than one,
 * rebalancing is done to restore this property.
 *
 *
 * This implementation requires that the key type implements the
 * `Comparable` interface and calls the `compareTo()` and
 * method to compare two keys. It does not call either `equals()` or
 * `hashCode()`. The *put*, *get*, *contains*,
 * *delete*, *minimum*, *maximum*, *ceiling*, and
 * *floor* operations each take logarithmic time in the worst case. The
 * *size*, and *is-empty* operations take constant time.
 * Construction also takes constant time.
 *
 * For other implementations of the same API, see [ST], [BinarySearchST],
 * [SequentialSearchST], [BST], [RedBlackBST],
 * [SeparateChainingHashST], and [LinearProbingHashST].
 *
 * @author Marcelo Silva
 * @author Jingyi Cheng
 *
 */

/**
 * Initializes an empty symbol table.
 */
class AVLTreeST<Key : Comparable<Key>, Value> {
    private var root: Node? = null            //The root node.

    /**
     * Checks if the symbol table is empty.
     *
     * @return `true` if the symbol table is empty.
     */
    val isEmpty: Boolean
        get() = root == null

    /**
     * Checks if AVL property is consistent.
     *
     * @return `true` if AVL property is consistent.
     */
    private val isAVL: Boolean
        get() = isAVL(root)

    /**
     * Checks if the symmetric order is consistent.
     *
     * @return `true` if the symmetric order is consistent
     */
    private val isBST: Boolean
        get() = isBST(root, null, null)

    /**
     * Checks if size is consistent.
     *
     * @return `true` if size is consistent
     */
    private val isSizeConsistent: Boolean
        get() = isSizeConsistent(root)

    /**
     * Checks if rank is consistent.
     *
     * @return `true` if rank is consistent
     */
    private val isRankConsistent: Boolean
        get() {
            for (i in 0 until size)
                if (i != rank(select(i))) return false
            for (key in keys())
                if (key.compareTo(select(rank(key))!!) != 0) return false
            return true
        }

    /**
     * This class represents an inner node of the AVL tree.
     */
    private inner class Node(val key: Key              // the key
                             , var `val`: Value?       // the associated value
                             , var height: Int         // height of the subtree
                             , var size: Int           // number of nodes in subtree
    ) {
        var left: Node? = null       // left subtree
        var right: Node? = null      // right subtree
    }

    var size = 0
        get() = size(root)

    /**
     * Returns the number of nodes in the subtree.
     *
     * @param x the subtree
     * @return the number of nodes in the subtree
     */
    private fun size(x: Node?) = x?.size ?: 0

    var height = 0
        get() = height(root)

    /**
     * Returns the height of the subtree.
     *
     * @param x the subtree
     * @return the height of the subtree.
     */
    private fun height(x: Node?) = x?.height ?: -1

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the
     * symbol table and `null` if the key is not in the
     * symbol table
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun get(key: Key?): Value? {
        if (key == null) throw IllegalArgumentException("argument to get() is null")
        val x = get(root, key) ?: return null
        return x.`val`
    }

    /**
     * Returns value associated with the given key in the subtree or
     * `null` if no such key.
     *
     * @param x the subtree
     * @param key the key
     * @return value associated with the given key in the subtree or
     * `null` if no such key
     */
    private operator fun get(x: Node?, key: Key): Node? = when {
        x == null -> null
        key < x.key -> get(x.left, key)
        key > x.key -> get(x.right, key)
        else -> x
    }

    /**
     * Checks if the symbol table contains the given key.
     *
     * @param key the key
     * @return `true` if the symbol table contains `key`
     * and `false` otherwise
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun contains(key: Key) = get(key) != null

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting
     * the old value with the new value if the symbol table already contains the
     * specified key. Deletes the specified key (and its associated value) from
     * this symbol table if the specified value is `null`.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun put(key: Key?, `val`: Value?) {
        if (key == null) throw IllegalArgumentException("first argument to put() is null")
        if (`val` == null) {
            delete(key)
            return
        }
        root = put(root, key, `val`)
        assert(check())
    }

    /**
     * Inserts the key-value pair in the subtree. It overrides the old value
     * with the new value if the symbol table already contains the specified key
     * and deletes the specified key (and its associated value) from this symbol
     * table if the specified value is `null`.
     *
     * @param x the subtree
     * @param key the key
     * @param val the value
     * @return the subtree
     */
    private fun put(x: Node?, key: Key, `val`: Value): Node {
        if (x == null) return Node(key, `val`, 0, 1)
        when {
            key < x.key -> x.left = put(x.left, key, `val`)
            key > x.key -> x.right = put(x.right, key, `val`)
            else -> {
                x.`val` = `val`
                return x
            }
        }
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        return balance(x)
    }

    /**
     * Restores the AVL tree property of the subtree.
     *
     * @param x the subtree
     * @return the subtree with restored AVL property
     */
    private fun balance(x: Node): Node {
        var x = x
        if (balanceFactor(x) < -1) {
            if (balanceFactor(x.right!!) > 0) {
                x.right = rotateRight(x.right!!)
            }
            x = rotateLeft(x)
        } else if (balanceFactor(x) > 1) {
            if (balanceFactor(x.left!!) < 0) {
                x.left = rotateLeft(x.left!!)
            }
            x = rotateRight(x)
        }
        return x
    }

    /**
     * Returns the balance factor of the subtree. The balance factor is defined
     * as the difference in height of the left subtree and right subtree, in
     * this order. Therefore, a subtree with a balance factor of -1, 0 or 1 has
     * the AVL property since the heights of the two child subtrees differ by at
     * most one.
     *
     * @param x the subtree
     * @return the balance factor of the subtree
     */
    private fun balanceFactor(x: Node) = height(x.left) - height(x.right)

    /**
     * Rotates the given subtree to the right.
     *
     * @param x the subtree
     * @return the right rotated subtree
     */
    private fun rotateRight(x: Node): Node {
        val y = x.left ?: throw IllegalArgumentException("The current node has no left subtree.")
        x.left = y.right
        y.right = x
        y.size = x.size
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        y.height = 1 + Math.max(height(y.left), height(y.right))
        return y
    }

    /**
     * Rotates the given subtree to the left.
     *
     * @param x the subtree
     * @return the left rotated subtree
     */
    private fun rotateLeft(x: Node): Node {
        val y = x.right ?: throw IllegalArgumentException("The current node has no right subtree.")
        x.right = y.left
        y.left = x
        y.size = x.size
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        y.height = 1 + Math.max(height(y.left), height(y.right))
        return y
    }

    /**
     * Removes the specified key and its associated value from the symbol table
     * (if the key is in the symbol table).
     *
     * @param key the key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun delete(key: Key?) {
        if (key == null) throw IllegalArgumentException("argument to delete() is null")
        if (!contains(key)) return
        root = delete(root!!, key)
        assert(check())
    }

    /**
     * Removes the specified key and its associated value from the given
     * subtree.
     *
     * @param x the subtree
     * @param key the key
     * @return the updated subtree
     */
    private fun delete(x: Node, key: Key): Node? {
        var x = x
        when {
            key < x.key -> x.left = delete(x.left!!, key)
            key > x.key -> x.right = delete(x.right!!, key)
            else -> {
                val left = x.left ?: return x.right
                val right = x.right ?: return x.left
                x = min(right)
                x.right = deleteMin(right)
                x.left = left
            }
        }
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        return balance(x)
    }

    /**
     * Removes the smallest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun deleteMin() {
        if (isEmpty) throw NoSuchElementException("called deleteMin() with empty symbol table")
        root = deleteMin(root!!)
        assert(check())
    }

    /**
     * Removes the smallest key and associated value from the given subtree.
     *
     * @param x the subtree
     * @return the updated subtree
     */
    private fun deleteMin(x: Node): Node? {
        val left = x.left ?: return x.right
        x.left = deleteMin(left)
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        return balance(x)
    }

    /**
     * Removes the largest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun deleteMax() {
        root = deleteMax(root ?: throw NoSuchElementException("called deleteMax() with empty symbol table"))
        assert(check())
    }

    /**
     * Removes the largest key and associated value from the given subtree.
     *
     * @param x the subtree
     * @return the updated subtree
     */
    private fun deleteMax(x: Node): Node? {
        val right = x.right ?: return x.left
        x.right = deleteMax(right)
        x.size = 1 + size(x.left) + size(x.right)
        x.height = 1 + Math.max(height(x.left), height(x.right))
        return balance(x)
    }

    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun min(): Key {
        if (isEmpty) throw NoSuchElementException("called min() with empty symbol table")
        return min(root!!).key
    }

    /**
     * Returns the node with the smallest key in the subtree.
     *
     * @param x the subtree
     * @return the node with the smallest key in the subtree
     */
    private fun min(x: Node): Node = if (x.left == null) x else min(x.left!!)

    /**
     * Returns the largest key in the symbol table.
     *
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun max() = max(root ?: throw NoSuchElementException("called max() with empty symbol table")).key

    /**
     * Returns the node with the largest key in the subtree.
     *
     * @param x the subtree
     * @return the node with the largest key in the subtree
     */
    private fun max(x: Node): Node = if (x.right == null) x else max(x.right!!)

    /**
     * Returns the largest key in the symbol table less than or equal to
     * `key`.
     *
     * @param key the key
     * @return the largest key in the symbol table less than or equal to
     * `key`
     * @throws NoSuchElementException if the symbol table is empty
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun floor(key: Key?): Key? {
        if (key == null) throw IllegalArgumentException("argument to floor() is null")
        if (isEmpty) throw NoSuchElementException("called floor() with empty symbol table")
        val x = floor(root, key)
        return x?.key
    }

    /**
     * Returns the node in the subtree with the largest key less than or equal
     * to the given key.
     *
     * @param x the subtree
     * @param key the key
     * @return the node in the subtree with the largest key less than or equal
     * to the given key
     */
    private fun floor(x: Node?, key: Key): Node? = when {
        x == null -> null
        key == x.key -> x
        key < x.key -> floor(x.left, key)
        else ->
            floor(x.right, key) ?: x
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to
     * `key`.
     *
     * @param key the key
     * @return the smallest key in the symbol table greater than or equal to
     * `key`
     * @throws NoSuchElementException if the symbol table is empty
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun ceiling(key: Key?): Key? {
        if (key == null) throw IllegalArgumentException("argument to ceiling() is null")
        if (isEmpty) throw NoSuchElementException("called ceiling() with empty symbol table")
        val x = ceiling(root, key)
        return x?.key
    }

    /**
     * Returns the node in the subtree with the smallest key greater than or
     * equal to the given key.
     *
     * @param x the subtree
     * @param key the key
     * @return the node in the subtree with the smallest key greater than or
     * equal to the given key
     */
    private fun ceiling(x: Node?, key: Key): Node? = when {
        x == null -> null
        key == x.key -> x
        key > x.key -> ceiling(x.right, key)
        else -> ceiling(x.left, key) ?: x
    }

    /**
     * Returns the kth smallest key in the symbol table.
     *
     * @param k the order statistic
     * @return the kth smallest key in the symbol table
     * @throws IllegalArgumentException unless `k` is between 0 and
     * `size() -1 `
     */
    fun select(k: Int): Key? {
        if (k < 0 || k >= size) throw IllegalArgumentException("k is not in range 0-${size - 1}")
        val x = select(root, k)
        return x?.key
    }

    /**
     * Returns the node with key the kth smallest key in the subtree.
     *
     * @param x the subtree
     * @param k the kth smallest key in the subtree
     * @return the node with key the kth smallest key in the subtree
     */
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
     * Returns the number of keys in the symbol table strictly less than
     * `key`.
     *
     * @param key the key
     * @return the number of keys in the symbol table strictly less than
     * `key`
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun rank(key: Key?): Int {
        if (key == null) throw IllegalArgumentException("argument to rank() is null")
        return rank(key, root)
    }

    /**
     * Returns the number of keys in the subtree less than key.
     *
     * @param key the key
     * @param x the subtree
     * @return the number of keys in the subtree less than key
     */
    private fun rank(key: Key, x: Node?): Int = when {
        x == null -> 0
        key < x.key -> rank(key, x.left)
        key > x.key -> 1 + size(x.left) + rank(key, x.right)
        else -> size(x.left)
    }

    /**
     * Returns all keys in the symbol table.
     *
     * @return all keys in the symbol table
     */
    fun keys(): nnQueue<Key> = keysInOrder()

    /**
     * Returns all keys in the symbol table following an in-order traversal.
     *
     * @return all keys in the symbol table following an in-order traversal
     */
    fun keysInOrder(): nnQueue<Key> {
        val queue = nnQueue<Key>()
        keysInOrder(root, queue)
        return queue
    }

    /**
     * Adds the keys in the subtree to queue following an in-order traversal.
     *
     * @param x the subtree
     * @param queue the queue
     */
    private fun keysInOrder(x: Node?, queue: nnQueue<Key>) {
        if (x == null) return
        keysInOrder(x.left, queue)
        queue.enqueue(x.key)
        keysInOrder(x.right, queue)
    }

    /**
     * Returns all keys in the symbol table following a level-order traversal.
     *
     * @return all keys in the symbol table following a level-order traversal.
     */
    fun keysLevelOrder(): Iterable<Key> {
        val queue = nnQueue<Key>()
        if (!isEmpty) {
            val queue2 = nnQueue<Node>()
            queue2.enqueue(root)
            while (!queue2.isEmpty) {
                val x = queue2.dequeue()
                queue.enqueue(x.key)
                x.left?.let { queue2.enqueue(x.left) }
                x.right?.let { queue2.enqueue(x.right) }
            }
        }
        return queue
    }

    /**
     * Returns all keys in the symbol table in the given range.
     *
     * @param lo the lowest key
     * @param hi the highest key
     * @return all keys in the symbol table between `lo` (inclusive)
     * and `hi` (exclusive)
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

    /**
     * Adds the keys between `lo` and `hi` in the subtree
     * to the `queue`.
     *
     * @param x the subtree
     * @param queue the queue
     * @param lo the lowest key
     * @param hi the highest key
     */
    private fun keys(x: Node?, queue: nnQueue<Key>, lo: Key, hi: Key) {
        if (x == null) return
        if (x.key > lo) keys(x.left, queue, lo, hi)
        if (x.key in lo..hi) queue.enqueue(x.key)
        if (x.key < hi) keys(x.right, queue, lo, hi)
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return the number of keys in the symbol table between `lo`
     * (inclusive) and `hi` (exclusive)
     * @throws IllegalArgumentException if either `lo` or `hi`
     * is `null`
     */
    fun size(lo: Key?, hi: Key?): Int {
        if (lo == null) throw IllegalArgumentException("first argument to size() is null")
        if (hi == null) throw IllegalArgumentException("second argument to size() is null")
        if (lo > hi) return 0
        return if (contains(hi))
            rank(hi) - rank(lo) + 1
        else
            rank(hi) - rank(lo)
    }

    /**
     * Checks if the AVL tree invariants are fine.
     *
     * @return `true` if the AVL tree invariants are fine
     */
    private fun check(): Boolean {
        if (!isBST) StdOut.println("Symmetric order not consistent")
        if (!isAVL) StdOut.println("AVL property not consistent")
        if (!isSizeConsistent) StdOut.println("Subtree counts not consistent")
        if (!isRankConsistent) StdOut.println("Ranks not consistent")
        return isBST && isAVL && isSizeConsistent && isRankConsistent
    }

    /**
     * Checks if AVL property is consistent in the subtree.
     *
     * @param x the subtree
     * @return `true` if AVL property is consistent in the subtree
     */
    private fun isAVL(x: Node?): Boolean {
        val bf = balanceFactor(x ?: return true)
        return if (bf > 1 || bf < -1) false else isAVL(x.left) && isAVL(x.right)
    }

    /**
     * Checks if the tree rooted at x is a BST with all keys strictly between
     * min and max (if min or max is null, treat as empty constraint) Credit:
     * Bob Dondero's elegant solution
     *
     * @param x the subtree
     * @param min the minimum key in subtree
     * @param max the maximum key in subtree
     * @return `true` if if the symmetric order is consistent
     */
    private fun isBST(x: Node?, min: Key?, max: Key?): Boolean {
        return when {
            x == null -> true
            min != null && x.key <= min -> false
            max != null && x.key >= max -> false
            else -> isBST(x.left, min, x.key) && isBST(x.right, x.key, max)
        }
    }

    /**
     * Checks if the size of the subtree is consistent.
     *
     * @return `true` if the size of the subtree is consistent
     */
    private fun isSizeConsistent(x: Node?): Boolean {
        if (x == null) return true
        return if (x.size != size(x.left) + size(x.right) + 1) false else isSizeConsistent(x.left) && isSizeConsistent(x.right)
    }

    companion object {

        /**
         * Unit tests the `AVLTreeST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val st = AVLTreeST<String, Int>()
            StdOut.println("Checking isEmpty ... " + st.isEmpty)
            val items = arrayOf("A", "B", "C", "D", "E", "F", null, "G")
            try {
                for ((i, key) in items.withIndex())
                    st.put(key, i)
            } catch (e: IllegalArgumentException) {
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

            StdOut.println("Checking isEmpty ... " + st.isEmpty)

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
            StdOut.println("Checking isEmpty ... " + st.isEmpty)

            StdOut.println("End of test")
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