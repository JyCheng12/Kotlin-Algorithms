/******************************************************************************
 * Compilation:  javac PatriciaST.java
 * Execution:    java PatriciaST
 * Dependencies: StdOut.kt StdRandom.kt Queue.kt
 * Data files:   n/a
 *
 * A symbol table implementation based on PATRICIA.
 *
 * % java PatriciaST
 * Creating dataset (1000000 items)...
 * Shuffling...
 * Adding (1000000 items)...
 * Iterating...
 * 1000000 items iterated
 * Shuffling...
 * Deleting (500000 items)...
 * Iterating...
 * 500000 items iterated
 * Checking...
 * 500000 items found and 500000 (deleted) items missing
 * Deleting the rest (500000 items)...
 * PASS 1 TESTS SUCCEEDED
 * %
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `PatriciaST` class provides an implementation of an unordered
 * symbol table of key-value pairs, with the restriction that the key is of
 * class [String]. It supports the usual *put*,
 * *get*, *contains*, *delete*, *size*, and
 * *is-empty* methods. It also provides a *keys* method for
 * iterating over all of the keys. A symbol table implements the
 * *associative array* abstraction: when associating a value with a key
 * that is already in the symbol table, the convention is to replace the old
 * value with the new value. Unlike [java.util.Map], this class uses the
 * convention that values cannot be `null`—setting the value
 * associated with a key to `null` is equivalent to deleting the key
 * from the symbol table.
 *
 *
 * This unordered symbol table class implements PATRICIA (Practical Algorithm
 * to Retrieve Information Coded In Alphanumeric). In spite of the acronym,
 * string keys are not limited to alphanumeric content. A key may possess any
 * string value, except for the string of zero length (the empty string).
 *
 *
 * Unlike other generic symbol table implementations that can accept a
 * parameterized key type, this symbol table class can only accommodate keys
 * of class [String]. This unfortunate restriction stems from a
 * limitation in Java. Although Java provides excellent support for generic
 * programming, the current infrastructure somewhat limits generic collection
 * implementations to those that employ comparison-based or hash-based methods.
 * PATRICIA does not employ comparisons or hashing; instead, it relies on
 * bit-test operations. Because Java does not furnish any generic abstractions
 * (or implementations) for bit-testing the contents of an object, providing
 * support for generic keys using PATRICIA does not seem practical.
 *
 *
 * PATRICIA is a variation of a trie, and it is often classified as a
 * space-optimized trie. In a classical trie, each level represents a
 * subsequent digit in a key. In PATRICIA, nodes only exist to identify the
 * digits (bits) that distinguish the individual keys within the trie. Because
 * PATRICIA uses a radix of two, each node has only two children, like a binary
 * tree. Also like a binary tree, the number of nodes, within the trie, equals
 * the number of keys. Consequently, some classify PATRICIA as a tree.
 *
 *
 * The analysis of PATRICIA is complicated. The theoretical wost-case
 * performance for a *get*, *put*, or *delete* operation
 * is **O(N)**, when **N** is less than
 * **W** (where **W** is the length in bits of the
 * longest key), and **O(W)**, when **N** is greater
 * than **W**. However, the worst case is unlikely to occur with
 * typical use. The average (and usual) performance of PATRICIA is
 * approximately **~lg N** for each *get*, *put*, or
 * *delete* operation. Although this appears to put PATRICIA on the same
 * footing as binary trees, this time complexity represents the number of
 * single-bit test operations (under PATRICIA), and not full-key comparisons
 * (as required by binary trees). After the single-bit tests conclude, PATRICIA
 * requires just one full-key comparison to confirm the existence (or absence)
 * of the key (per *get*, *put*, or *delete* operation).
 *
 *
 * In practice, decent implementations of PATRICIA can often outperform
 * balanced binary trees, and even hash tables. Although this particular
 * implementation performs well, the source code was written with an emphasis
 * on clarity, and not performance. PATRICIA performs admirably when its
 * bit-testing loops are well tuned. Consider using the source code as a guide,
 * should you need to produce an optimized implementation, for anther key type,
 * or in another programming language.
 *
 *
 * Other resources for PATRICIA:<br></br>
 * Sedgewick, R. (1990) *Algorithms in C*, Addison-Wesley<br></br>
 * Knuth, D. (1973) *The Art of Computer Programming*, Addison-Wesley<br></br>
 *
 * @author John Hentosh (based on an implementation by Robert Sedgewick)
 * @author Jingyi Cheng
 */
class PatriciaST<Value> {
    private val head: Node = Node("", null, 0)
    var count: Int = 0
        private set

    /**
     * Returns `true` if the symbol table is empty.
     * @return `true` if this symbol table is empty and
     * `false` otherwise
     */
    internal val isEmpty: Boolean
        get() = count == 0

    /* An inner Node class specifies the objects that hold each key-value pair.
     * The b value indicates the relevant bit position.
     */
    internal inner class Node(val key: String, var `val`: Value?, var b: Int) {
        var left: Node? = null
        var right: Node? = null
    }

    /**
     * Initializes an empty PATRICIA-based symbol table.
     */
    /* The constructor creates a head (sentinel) node that contains a
     * zero-length string.
     */
    init {
        head.left = head
        head.right = head
    }

    /**
     * Places a key-value pair into the symbol table. If the table already
     * contains the specified key, then its associated value becomes updated.
     * If the value provided is `null`, then the key becomes removed
     * from the symbol table.
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if `key` is `null`
     * @throws IllegalArgumentException if `key` is the empty string.
     */
    fun put(key: String?, `val`: Value?) {
        if (key == null) throw IllegalArgumentException("called put(null)")
        if (key.isEmpty()) throw IllegalArgumentException("invalid key")
        if (`val` == null) delete(key)
        var p: Node?
        var x: Node? = head
        do {
            p = x
            x = if (safeBitTest(key, x!!.b))
                x.right
            else
                x.left
        } while (p!!.b < x!!.b)
        if (x.key != key) {
            val b = firstDifferingBit(x.key, key)
            x = head
            do {
                p = x
                x = if (safeBitTest(key, x!!.b))
                    x.right
                else
                    x.left
            } while (p!!.b < x!!.b && x.b < b)
            val t = Node(key, `val`, b)
            if (safeBitTest(key, b)) {
                t.left = x
                t.right = t
            } else {
                t.left = t
                t.right = x
            }
            if (safeBitTest(key, p.b))
                p.right = t
            else
                p.left = t
            count++
        } else
            x.`val` = `val`
    }

    /**
     * Retrieves the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the
     * symbol table and `null` if the key is not in the symbol table
     * @throws IllegalArgumentException if `key` is `null`
     * @throws IllegalArgumentException if `key` is the empty string.
     */
    operator fun get(key: String?): Value? {
        if (key == null) throw IllegalArgumentException("called get(null)")
        if (key.isEmpty()) throw IllegalArgumentException("invalid key")
        var p: Node?
        var x: Node? = head
        do {
            p = x
            x = if (safeBitTest(key, x!!.b)) x.right
            else x.left
        } while (p!!.b < x!!.b)
        return if (x.key == key) x.`val`
        else null
    }

    /**
     * Removes a key and its associated value from the symbol table, if it
     * exists.
     * @param key the key
     * @throws IllegalArgumentException if `key` is `null`
     * @throws IllegalArgumentException if `key` is the empty string.
     */
    fun delete(key: String?) {
        if (key == null) throw IllegalArgumentException("called delete(null)")
        if (key.isEmpty()) throw IllegalArgumentException("invalid key")
        var g: Node?             // previous previous (grandparent)
        var p: Node? = head      // previous (parent)
        var x: Node? = head      // node to delete
        do {
            g = p
            p = x
            x = if (safeBitTest(key, x!!.b))
                x.right
            else
                x.left
        } while (p!!.b < x!!.b)
        if (x.key == key) {
            var z: Node?
            var y: Node? = head
            do {            // find the true parent (z) of x
                z = y
                y = if (safeBitTest(key, y!!.b))
                    y.right
                else
                    y.left
            } while (y !== x)
            if (x === p) {   // case 1: remove (leaf node) x
                val c = if (safeBitTest(key, x.b))
                    x.left
                else
                    x.right     // child of x
                if (safeBitTest(key, z!!.b))
                    z.right = c
                else
                    z.left = c
            } else {          // case 2: p replaces (internal node) x
                val c = if (safeBitTest(key, p.b))
                    p.left
                else
                    p.right     // child of p
                if (safeBitTest(key, g!!.b))
                    g.right = c
                else
                    g.left = c
                if (safeBitTest(key, z!!.b))
                    z.right = p
                else
                    z.left = p
                p.left = x.left
                p.right = x.right
                p.b = x.b
            }
            count--
        }
    }

    /**
     * Returns `true` if the key-value pair, specified by the given
     * key, exists within the symbol table.
     * @param key the key
     * @return `true` if this symbol table contains the given
     * `key` and `false` otherwise
     * @throws IllegalArgumentException if `key` is `null`
     * @throws IllegalArgumentException if `key` is the empty string.
     */
    operator fun contains(key: String) = get(key) != null

    /**
     * Returns all keys in the symbol table as an `Iterable`.
     * To iterate over all of the keys in the symbol table named
     * `st`, use the foreach notation:
     * `for (Key key : st.keys())`.
     * @return all keys in the symbol table as an `Iterable`
     */
    fun keys(): Iterable<String> {
        val queue = nnQueue<String>()
        if (head.left !== head) keys(head.left!!, 0, queue)
        if (head.right !== head) keys(head.right!!, 0, queue)
        return queue
    }

    private fun keys(x: Node, b: Int, queue: nnQueue<String>) {
        if (x.b > b) {
            keys(x.left!!, x.b, queue)
            queue.enqueue(x.key)
            keys(x.right!!, x.b, queue)
        }
    }

    companion object {

        /* The safeBitTest function logically appends a terminating sequence (when
         * required) to extend (logically) the string beyond its length.
         *
         * The inner loops of the get and put methods flow much better when they
         * are not concerned with the lengths of strings, so a trick is employed to
         * allow the get and put methods to view every string as an "infinite"
         * sequence of bits. Logically, every string gets a '\uffff' character,
         * followed by an "infinite" sequence of '\u0000' characters, appended to
         * the end.
         *
         * Note that the '\uffff' character serves to mark the end of the string,
         * and it is necessary. Simply padding with '\u0000' is insufficient to
         * make all unique Unicode strings "look" unique to the get and put methods
         * (because these methods do not regard string lengths).
         */
        private fun safeBitTest(key: String, b: Int): Boolean {
            if (b < key.length * 16) return bitTest(key, b) != 0
            return b <= key.length * 16 + 15   // padding
        }

        private fun bitTest(key: String, b: Int) = key[b.ushr(4)].toInt().ushr(b and 0xf) and 1

        /* Like the safeBitTest function, the safeCharAt function makes every
         * string look like an "infinite" sequence of characters. Logically, every
         * string gets a '\uffff' character, followed by an "infinite" sequence of
         * '\u0000' characters, appended to the end.
         */
        private fun safeCharAt(key: String, i: Int): Int {
            if (i < key.length) return key[i].toInt()
            return if (i > key.length)
                0x0000            // padding
            else
                0xffff            // end marker
        }

        /* For efficiency's sake, the firstDifferingBit function compares entire
         * characters first, and then considers the individual bits (once it finds
         * two characters that do not match). Also, the least significant bits of
         * an individual character are examined first. There are many Unicode
         * alphabets where most (if not all) of the "action" occurs in the least
         * significant bits.
         *
         * Notice that the very first character comparison excludes the
         * least-significant bit. The firstDifferingBit function must never return
         * zero; otherwise, a node would become created as a child to the head
         * (sentinel) node that matches the bit-index value (zero) stored in the
         * head node. This would violate the invariant that bit-index values
         * increase as you descend into the trie.
         */
        private fun firstDifferingBit(k1: String, k2: String?): Int {
            var i = 0
            var c1 = safeCharAt(k1, 0) and 1.inv()
            var c2 = safeCharAt(k2!!, 0) and 1.inv()
            if (c1 == c2) {
                i = 1
                while (safeCharAt(k1, i) == safeCharAt(k2, i)) i++
                c1 = safeCharAt(k1, i)
                c2 = safeCharAt(k2, i)
            }
            var b = 0
            while (c1.ushr(b) and 1 == c2.ushr(b) and 1) b++
            return i * 16 + b
        }

        /**
         * Unit tests the `PatriciaST` data type.
         * This test fixture runs a series of tests on a randomly generated dataset.
         * You may specify up to two integer parameters on the command line. The
         * first parameter indicates the size of the dataset. The second parameter
         * controls the number of passes (a new random dataset becomes generated at
         * the start of each pass).
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val st = PatriciaST<Int>()
            var limitItem = 1000000
            var limitPass = 1
            var countPass = 0
            var ok = true

            if (args.isNotEmpty()) limitItem = Integer.parseInt(args[0])
            if (args.size > 1) limitPass = Integer.parseInt(args[1])

            do {
                val a = Array<String>(limitItem) { Integer.toString(it, 16) }
                val v = IntArray(limitItem) { it }

                StdOut.printf("Creating dataset (%d items)...\n", limitItem)

                StdOut.print("Shuffling...\n")
                StdRandom.shuffle(v)

                StdOut.printf("Adding (%d items)...\n", limitItem)
                for (i in 0 until limitItem)
                    st.put(a[v[i]], v[i])

                var countKeys = 0
                StdOut.print("Iterating...\n")
                for (key in st.keys()) countKeys++
                StdOut.printf("%d items iterated\n", countKeys)
                if (countKeys != limitItem) ok = false
                if (countKeys != st.count) ok = false

                StdOut.print("Shuffling...\n")
                StdRandom.shuffle(v)

                val limitDelete = limitItem / 2
                StdOut.printf("Deleting (%d items)...\n", limitDelete)
                for (i in 0 until limitDelete)
                    st.delete(a[v[i]])

                countKeys = 0
                StdOut.print("Iterating...\n")
                for (key in st.keys()) countKeys++
                StdOut.printf("%d items iterated\n", countKeys)
                if (countKeys != limitItem - limitDelete) ok = false
                if (countKeys != st.count) ok = false

                var countDelete = 0
                var countRemain = 0
                StdOut.print("Checking...\n")
                for (i in 0 until limitItem)
                    if (i < limitDelete)
                        if (!st.contains(a[v[i]])) countDelete++
                        else {
                            val `val` = st[a[v[i]]]!!
                            if (`val` == v[i]) countRemain++
                        }
                StdOut.printf("%d items found and %d (deleted) items missing\n",
                        countRemain, countDelete)
                if (countRemain + countDelete != limitItem) ok = false
                if (countRemain != st.count) ok = false
                if (st.isEmpty) ok = false

                StdOut.printf("Deleting the rest (%d items)...\n",
                        limitItem - countDelete)
                for (i in countDelete until limitItem)
                    st.delete(a[v[i]])
                if (!st.isEmpty) ok = false

                countPass++
                if (ok)
                    StdOut.printf("PASS %d TESTS SUCCEEDED\n", countPass)
                else
                    StdOut.printf("PASS %d TESTS FAILED\n", countPass)
            } while (ok && countPass < limitPass)

            if (!ok) throw RuntimeException("TESTS FAILED")
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