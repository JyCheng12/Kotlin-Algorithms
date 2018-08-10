/******************************************************************************
 * Compilation:  javac RedBlackBST.java
 * Execution:    java RedBlackBST < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/33balanced/tinyST.txt
 *
 * A symbol table implemented using a left-leaning red-black BST.
 * This is the 2-3 version.
 *
 * Note: commented out assertions because DrJava now enables assertions
 * by default.
 *
 * % more tinyST.txt
 * S E A R C H E X A M P L E
 *
 * % java RedBlackBST < tinyST.txt
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
 * *maximum*, *floor*, and *ceiling*.
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
 * This implementation uses a left-leaning red-black BST. It requires that
 * the key type implements the `Comparable` interface and calls the
 * `compareTo()` and method to compare two keys. It does not call either
 * `equals()` or `hashCode()`.
 * The *put*, *contains*, *remove*, *minimum*,
 * *maximum*, *ceiling*, and *floor* operations each take
 * logarithmic time in the worst case, if the tree becomes unbalanced.
 * The *size*, and *is-empty* operations take constant time.
 * Construction takes constant time.
 *
 *
 * For additional documentation, see [Section 3.3](https://algs4.cs.princeton.edu/33balanced) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 * For other implementations of the same API, see [ST], [BinarySearchST],
 * [SequentialSearchST], [BST],
 * [SeparateChainingHashST], [LinearProbingHashST], and [AVLTreeST].
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */

/**
 * Initializes an empty symbol table.
 */
class RedBlackBST<Key : Comparable<Key>, Value> {
    private var root: Node? = null     // root of the BST

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    var size = 0
        get() = size(root)
        private set

    /**
     * Is this symbol table empty?
     * @return `true` if this symbol table is empty and `false` otherwise
     */
    val isEmpty: Boolean
        get() = root == null

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
                if (key.compareTo(select(rank(key))) != 0) return false
            return true
        }

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    private val is23: Boolean
        get() = is23(root)

    // do all paths from root to leaf have same number of black edges?
    private// number of black links on path from root to min
    val isBalanced: Boolean
        get() {
            var black = 0
            var x = root
            while (x != null) {
                if (!isRed(x)) black++
                x = x.left
            }
            return isBalanced(root, black)
        }

    // BST helper node data type
    internal inner class Node(val key: Key           // key
                             , var `val`: Value         // associated data
                             , var color: Boolean     // color of parent link
                             , var size: Int          // subtree count
    ) {
        var left: Node? = null
        var right: Node? = null  // links to left and right subtrees
    }

    /***************************************************************************
     * Node helper methods.
     */
    // is node x red; false if x is null ?
    private fun isRed(x: Node?) = if (x == null) false else x.color == RED

    // number of node in subtree rooted at x; 0 if x is null
    private fun size(x: Node?) = x?.size ?: 0

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and `null` if the key is not in the symbol table
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun get(key: Key?): Value? {
        if (key == null) throw IllegalArgumentException("argument to get() is null")
        return get(root, key)
    }

    // value associated with the given key in subtree rooted at x; null if no such key
    private operator fun get(x: Node?, key: Key): Value? {
        var x = x
        while (x != null)
            x = when {
                key < x.key -> x.left
                key > x.key -> x.right
                else -> return x.`val`
            }
        return null
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return `true` if this symbol table contains `key` and
     * `false` otherwise
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun contains(key: Key) = get(key) != null

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is `null`.
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
        root!!.color = BLACK
        // assert check();
    }

    // insert the key-value pair in the subtree rooted at h
    private fun put(h: Node?, key: Key, `val`: Value): Node {
        var h: Node = h ?: return Node(key, `val`, RED, 1)

        when {
            key < h.key -> h.left = put(h.left, key, `val`)
            key > h.key -> h.right = put(h.right, key, `val`)
            else -> h.`val` = `val`
        }

        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h)
        if (isRed(h.left) && isRed(h.left!!.left)) h = rotateRight(h)
        if (isRed(h.left) && isRed(h.right)) flipColors(h)
        h.size = size(h.left) + size(h.right) + 1

        return h
    }

    /***************************************************************************
     * Red-black tree deletion.
     */

    /**
     * Removes the smallest key and associated value from the symbol table.
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun deleteMin() {
        val root = root ?: throw NoSuchElementException("BST underflow")
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED

        this.root = deleteMin(root)
        if (!isEmpty) this.root!!.color = BLACK
    }

    // delete the key-value pair with the minimum key rooted at h
    private fun deleteMin(h: Node): Node? {
        var h = h
        val left = h.left ?: return null
        if (!isRed(left) && !isRed(left.left))
            h = moveRedLeft(h)

        h.left = deleteMin(h.left!!)
        return balance(h)
    }


    /**
     * Removes the largest key and associated value from the symbol table.
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun deleteMax() {
        val root = root ?: throw NoSuchElementException("BST underflow")
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED
        this.root = deleteMax(root)
        if (!isEmpty) this.root!!.color = BLACK
    }

    // delete the key-value pair with the maximum key rooted at h
    private fun deleteMax(h: Node): Node? {
        var h = h
        if (isRed(h.left)) h = rotateRight(h)
        if (h.right == null) return null
        if (!isRed(h.right) && !isRed(h.right!!.left)) h = moveRedRight(h)
        h.right = deleteMax(h.right!!)
        return balance(h)
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param  key the key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun delete(key: Key?) {
        if (key == null) throw IllegalArgumentException("argument to delete() is null")
        val root = root ?: return
        if (!contains(key)) return

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED
        this.root = delete(root, key)
        (this.root ?: return).color = BLACK
    }

    // delete the key-value pair with the given key rooted at h
    private fun delete(h: Node, key: Key): Node? {
        var h = h

        if (key < h.key) {
            if (!isRed(h.left) && !isRed(h.left!!.left))
                h = moveRedLeft(h)
            h.left = delete(h.left!!, key)
        } else {
            if (isRed(h.left))
                h = rotateRight(h)
            if (key.compareTo(h.key) == 0 && h.right == null)
                return null
            if (!isRed(h.right) && !isRed(h.right!!.left))
                h = moveRedRight(h)
            if (key.compareTo(h.key) == 0) {
                val x = min(h.right!!)
                val t = h
                h = x
                h.right = deleteMin(t.right!!)
            } else
                h.right = delete(h.right!!, key)
        }
        return balance(h)
    }

    // make a left-leaning link lean to the right
    private fun rotateRight(h: Node): Node {
        val x = h.left
        h.left = x!!.right
        x.right = h
        x.color = h.color//x.right.color
        x.right!!.color = RED
        x.size = h.size
        h.size = size(h.left) + size(h.right) + 1
        return x
    }

    // make a right-leaning link lean to the left
    private fun rotateLeft(h: Node): Node {
        val x = h.right
        h.right = x!!.left
        x.left = h
        x.color = h.color//x.left.color
        x.left!!.color = RED
        x.size = h.size
        h.size = size(h.left) + size(h.right) + 1
        return x
    }

    // flip the colors of a node and its two children
    private fun flipColors(h: Node) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
        h.color = !h.color
        h.left!!.color = !h.left!!.color
        h.right!!.color = !h.right!!.color
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private fun moveRedLeft(h: Node): Node {
        var h = h

        flipColors(h)
        if (isRed(h.right!!.left)) {
            h.right = rotateRight(h.right!!)
            h = rotateLeft(h)
            flipColors(h)
        }
        return h
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private fun moveRedRight(h: Node): Node {
        var h = h

        flipColors(h)
        if (isRed(h.left!!.left)) {
            h = rotateRight(h)
            flipColors(h)
        }
        return h
    }

    // restore red-black tree invariant
    private fun balance(h: Node): Node {
        var h = h

        if (isRed(h.right)) h = rotateLeft(h)
        if (isRed(h.left) && isRed(h.left!!.left)) h = rotateRight(h)
        if (isRed(h.left) && isRed(h.right)) flipColors(h)

        h.size = size(h.left) + size(h.right) + 1
        return h
    }

    /**
     * Returns the height of the BST (for debugging).
     * @return the height of the BST (a 1-node tree has height 0)
     */
    fun height() = height(root)

    private fun height(x: Node?): Int = if (x == null) -1 else 1 + Math.max(height(x.left), height(x.right))

    /**
     * Returns the smallest key in the symbol table.
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun min() = min(root ?: throw NoSuchElementException("calls min() with empty symbol table")).key

    // the smallest key in subtree rooted at x; null if no such key
    private fun min(x: Node): Node {
        val left = x.left
        return if (left == null) x else min(left)
    }

    /**
     * Returns the largest key in the symbol table.
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    fun max() = max(root ?: throw NoSuchElementException("calls max() with empty symbol table")).key

    // the largest key in the subtree rooted at x; null if no such key
    private fun max(x: Node): Node {
        val right = x.right
        return if (right == null) x else max(right)
    }

    /**
     * Returns the largest key in the symbol table less than or equal to `key`.
     * @param key the key
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

    // the largest key in the subtree rooted at x less than or equal to the given key
    private fun floor(x: Node?, key: Key) : Node? = when {
        x == null -> null
        key == x.key -> x
        key < x.key -> floor(x.left, key)
        else -> floor(x.right, key) ?: x
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to `key`.
     * @param key the key
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

    // the smallest key in the subtree rooted at x greater than or equal to the given key
    private fun ceiling(x: Node?, key: Key): Node? = when {
        x == null -> null
        key == x.key -> x
        key > x.key -> ceiling(x.right, key)
        else -> ceiling(x.left, key) ?: x
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
        if (k < 0 || k >= size) throw IllegalArgumentException("argument to select() is invalid: $k")
        val x = select(root!!, k)
        return x.key
    }

    // the key of rank k in the subtree rooted at x
    private fun select(x: Node, k: Int): Node {
        val t = size(x.left)
        return when {
            t > k -> select(x.left!!, k)
            t < k -> select(x.right!!, k - t - 1)
            else -> x
        }
    }

    /**
     * Return the number of keys in the symbol table strictly less than `key`.
     * @param key the key
     * @return the number of keys in the symbol table strictly less than `key`
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun rank(key: Key?) = rank(key ?: throw IllegalArgumentException("argument to rank() is null"), root)

    // number of keys less than key in the subtree rooted at x
    private fun rank(key: Key, x: Node?): Int = when {
        x == null -> 0
        key < x.key -> rank(key, x.left)
        key > x.key -> 1 + size(x.left) + rank(key, x.right)
        else -> size(x.left)
    }

    /**
     * Returns all keys in the symbol table as an `Iterable`.
     * To iterate over all of the keys in the symbol table named `st`,
     * use the foreach notation: `for (Key key : st.keys())`.
     * @return all keys in the symbol table as an `Iterable`
     */
    fun keys() = if (isEmpty) nnQueue() else keys(min(), max())

    /**
     * Returns all keys in the symbol table in the given range,
     * as an `Iterable`.
     *
     * @param  lo minimum endpoint
     * @param  hi maximum endpoint
     * @return all keys in the sybol table between `lo`
     * (inclusive) and `hi` (inclusive) as an `Iterable`
     * @throws IllegalArgumentException if either `lo` or `hi`
     * is `null`
     */
    fun keys(lo: Key?, hi: Key?): Iterable<Key> {
        if (lo == null) throw IllegalArgumentException("first argument to keys() is null")
        if (hi == null) throw IllegalArgumentException("second argument to keys() is null")

        val queue = nnQueue<Key>()
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, queue, lo, hi)
        return queue
    }

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
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
     * @return the number of keys in the sybol table between `lo`
     * (inclusive) and `hi` (inclusive)
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


    /***************************************************************************
     * Check integrity of red-black tree data structure.
     */
    private fun check(): Boolean {
        if (!isBST) StdOut.println("Not in symmetric order")
        if (!isSizeConsistent) StdOut.println("Subtree counts not consistent")
        if (!isRankConsistent) StdOut.println("Ranks not consistent")
        if (!is23) StdOut.println("Not a 2-3 tree")
        if (!isBalanced) StdOut.println("Not balanced")
        return isBST && isSizeConsistent && isRankConsistent && is23 && isBalanced
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private fun isBST(x: Node?, min: Key?, max: Key?): Boolean {
        if (x == null) return true
        if (min != null && x.key <= min) return false
        return if (max != null && x.key >= max) false else isBST(x.left, min, x.key) && isBST(x.right, x.key, max)
    }

    private fun isSizeConsistent(x: Node?): Boolean {
        if (x == null) return true
        return if (x.size != size(x.left) + size(x.right) + 1) false else isSizeConsistent(x.left) && isSizeConsistent(x.right)
    }

    private fun is23(x: Node?): Boolean {
        if (x == null) return true
        if (isRed(x.right)) return false
        return if (x !== root && isRed(x) && isRed(x.left)) false else is23(x.left) && is23(x.right)
    }

    // does every path from the root to a leaf have the given number of black links?
    private fun isBalanced(x: Node?, black: Int): Boolean {
        var black = black
        if (x == null) return black == 0
        if (!isRed(x)) black--
        return isBalanced(x.left, black) && isBalanced(x.right, black)
    }

    companion object {
        private const val RED = true
        private const val BLACK = false

        /**
         * Unit tests the `RedBlackBST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val st = RedBlackBST<String, Int>()
            StdOut.println("Checking isEmpty ... "+st.isEmpty)
            StdOut.println("Checking Check ... "+st.check())
            val items = arrayOf("A", "B", "C", "D", "E", "F", null, "G")
            try {
                for ((i, key) in items.withIndex())
                    st.put(key, i)
            }catch(e:IllegalArgumentException){
                StdOut.println(e.message)
            }
            for (s in st.keys())
                StdOut.println("$s -> ${st[s]}")

            StdOut.println("Checking put('A', 10) ... \nOutput in keysLevelOrder() ...")
            st.put("A", 10)
            for (s in st.keys())
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
            StdOut.println("Checking Check ... "+st.check())

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
            StdOut.println("Checking Check ... "+st.check())

            StdOut.println("End of test")
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
