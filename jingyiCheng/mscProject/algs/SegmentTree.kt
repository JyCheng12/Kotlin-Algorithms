/******************************************************************************
 * Compilation:  javac SegmentTree.java
 * Execution:    java SegmentTree
 *
 * A segment tree data structure.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `SegmentTree` class is an structure for efficient search of cummulative data.
 * It performs  Range Minimum Query and Range Sum Query in O(log(n)) time.
 * It can be easily customizable to support Range Max Query, Range Multiplication Query etc.
 *
 *
 * Also it has been develop with  `LazyPropagation` for range updates, which means
 * when you perform update operations over a range, the update process affects the least nodes as possible
 * so that the bigger the range you want to update the less time it consumes to update it. Eventually those changes will be propagated
 * to the children and the whole array will be up to date.
 *
 *
 * Example:
 *
 *
 * SegmentTreeHeap st = new SegmentTreeHeap(new Integer[]{1,3,4,2,1, -2, 4});
 * st.update(0,3, 1)
 * In the above case only the node that represents the range [0,3] will be updated (and not their children) so in this case
 * the update task will be less than n*log(n)
 *
 * Memory usage:  O(n)
 *
 * @author Ricardo Pacheco
 */
class SegmentTree
/**
 * Time-Complexity:  O(n*log(n))
 *
 * @param array the Initialization array
 */
(array: IntArray) {
    private val array = array.copyOf(array.size)
    private val size: Int = (2 * Math.pow(2.0, Math.floor(Math.log(array.size.toDouble()) / Math.log(2.0) + 1))).toInt()
    private val heap: Array<Node> = Array(size) { Node() }

    init {
        build(1, 0, array.size)
    }

    fun size() = array.size

    //Initialize the Nodes of the Segment tree
    private fun build(v: Int, from: Int, size: Int) {
        heap[v] = Node()
        heap[v].from = from
        heap[v].to = from + size - 1

        if (size == 1) {
            heap[v].sum = array[from]
            heap[v].min = array[from]
        } else {
            //Build childs
            build(2 * v, from, size / 2)
            build(2 * v + 1, from + size / 2, size - size / 2)

            heap[v].sum = heap[2 * v].sum + heap[2 * v + 1].sum
            //min = min of the children
            heap[v].min = Math.min(heap[2 * v].min, heap[2 * v + 1].min)
        }
    }

    /**
     * Range Sum Query
     *
     * Time-Complexity: O(log(n))
     *
     * @param  from from index
     * @param  to to index
     * @return sum
     */
    fun rsq(from: Int, to: Int) = rsq(1, from, to)

    private fun rsq(v: Int, from: Int, to: Int): Int {
        val n = heap[v]

        //If you did a range update that contained this node, you can infer the Sum without going down the tree
        if (n.pendingVal != null && contains(n.from, n.to, from, to))
            return (to - from + 1) * n.pendingVal!!

        if (contains(from, to, n.from, n.to))
            return heap[v].sum

        if (intersects(from, to, n.from, n.to)) {
            propagate(v)
            val leftSum = rsq(2 * v, from, to)
            val rightSum = rsq(2 * v + 1, from, to)
            return leftSum + rightSum
        }
        return 0
    }

    /**
     * Range Min Query
     *
     * Time-Complexity: O(log(n))
     *
     * @param  from from index
     * @param  to to index
     * @return min
     */
    fun rMinQ(from: Int, to: Int) = rMinQ(1, from, to)

    private fun rMinQ(v: Int, from: Int, to: Int): Int {
        val n = heap[v]

        //If you did a range update that contained this node, you can infer the Min value without going down the tree
        if (n.pendingVal != null && contains(n.from, n.to, from, to))
            return n.pendingVal!!

        if (contains(from, to, n.from, n.to))
            return heap[v].min

        if (intersects(from, to, n.from, n.to)) {
            propagate(v)
            val leftMin = rMinQ(2 * v, from, to)
            val rightMin = rMinQ(2 * v + 1, from, to)
            return Math.min(leftMin, rightMin)
        }
        return Integer.MAX_VALUE
    }


    /**
     * Range Update Operation.
     * With this operation you can update either one position or a range of positions with a given number.
     * The update operations will update the less it can to update the whole range (Lazy Propagation).
     * The values will be propagated lazily from top to bottom of the segment tree.
     * This behavior is really useful for updates on portions of the array
     *
     *
     * Time-Complexity: O(log(n))
     *
     * @param from  from index
     * @param to    to index
     * @param value value
     */
    fun update(from: Int, to: Int, value: Int) = update(1, from, to, value)

    private fun update(v: Int, from: Int, to: Int, value: Int) {
        //The Node of the heap tree represents a range of the array with bounds: [n.from, n.to]
        val n = heap[v]

        /**
         * If the updating-range contains the portion of the current Node  We lazily update it.
         * This means We do NOT update each position of the vector, but update only some temporal
         * values into the Node; such values into the Node will be propagated down to its children only when they need to.
         */
        if (contains(from, to, n.from, n.to))
            change(n, value)

        if (n.size() == 1) return

        if (intersects(from, to, n.from, n.to)) {
            /**
             * Before keeping going down to the tree We need to propagate the
             * the values that have been temporally/lazily saved into this Node to its children
             * So that when We visit them the values  are properly updated
             */
            propagate(v)

            update(2 * v, from, to, value)
            update(2 * v + 1, from, to, value)

            n.sum = heap[2 * v].sum + heap[2 * v + 1].sum
            n.min = Math.min(heap[2 * v].min, heap[2 * v + 1].min)
        }
    }

    //Propagate temporal values to children
    private fun propagate(v: Int) {
        val n = heap[v]

        if (n.pendingVal != null) {
            change(heap[2 * v], n.pendingVal!!)
            change(heap[2 * v + 1], n.pendingVal!!)
            n.pendingVal = null //unset the pending propagation value
        }
    }

    //Save the temporal values that will be propagated lazily
    private fun change(n: Node, value: Int) {
        n.pendingVal = value
        n.sum = n.size() * value
        n.min = value
        array[n.from] = value

    }

    //Test if the range1 contains range2
    private fun contains(from1: Int, to1: Int, from2: Int, to2: Int) = from2 >= from1 && to2 <= to1

    //check inclusive intersection, test if range1[from1, to1] intersects range2[from2, to2]
    private fun intersects(from1: Int, to1: Int, from2: Int, to2: Int) =
            (from2 in from1..to1   //  (.[..)..] or (.[...]..)
                    || from1 in from2..to2) // [.(..]..) or [..(..)..

    //The Node class represents a partition range of the array.
    internal class Node {
        var sum: Int = 0
        var min: Int = 0
        //Here We store the value that will be propagated lazily
        var pendingVal: Int? = null
        var from: Int = 0
        var to: Int = 0

        fun size() = to - from + 1
    }

    companion object {
        /**
         * Read the following commands:
         * init n v     Initializes the array of size n with all v's
         * set a b c... Initializes the array  with [a, b, c ...]
         * rsq a b      Range Sum Query for the range [a, b]
         * rmq a b      Range Min Query for the range [a, b]
         * up  a b v    Update the [a,b] portion of the array with value v.
         * exit
         *
         *
         * Example:
         * init
         * set 1 2 3 4 5 6
         * rsq 1 3
         * Sum from 1 to 3 = 6
         * rmq 1 3
         * Min from 1 to 3 = 1
         * input up 1 3
         * [3,2,3,4,5,6]
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            var st: SegmentTree? = null
            while (true) {
                val line = StdIn.readLine()!!.split(" ")
                if (line[0] == "exit") break

                var arg1 = 0
                var arg2 = 0
                var arg3 = 0

                if (line.size > 1)
                    arg1 = Integer.parseInt(line[1])
                if (line.size > 2)
                    arg2 = Integer.parseInt(line[2])
                if (line.size > 3)
                    arg3 = Integer.parseInt(line[3])

                if (line[0] != "set" && line[0] != "init" && st == null) {
                    StdOut.println("Segment Tree not initialized")
                    continue
                }
                val array: IntArray
                when (line[0]) {
                    "set" -> {
                        array = IntArray(line.size - 1) { line[it + 1].toInt() }
                        st = SegmentTree(array)
                    }
                    "init" -> {
                        array = IntArray(arg1) { arg2 }
                        st = SegmentTree(array)
                        for (i in 0 until st.size()) {
                            StdOut.print("${st.rsq(i, i)} ")
                        }
                        StdOut.println()
                    }
                    "up" -> {
                        st!!.update(arg1, arg2, arg3)
                        for (i in 0 until st.size()) {
                            StdOut.print("${st.rsq(i, i)} ")
                        }
                        StdOut.println()
                    }
                    "rsq" -> StdOut.printf("Sum from %d to %d = %d%n", arg1, arg2, st!!.rsq(arg1, arg2))
                    "rmq" -> StdOut.printf("Min from %d to %d = %d%n", arg1, arg2, st!!.rMinQ(arg1, arg2))
                    else -> StdOut.println("Invalid command")
                }
            }
            StdOut.close()
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
