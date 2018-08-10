/******************************************************************************
 * Compilation:  javac Topological.java
 * Execution:    java  Topological filename.txt delimiter
 * Dependencies: Digraph.kt DepthFirstOrder.kt DirectedCycle.kt
 * EdgeWeightedDigraph.kt EdgeWeightedDirectedCycle.kt
 * SymbolDigraph.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/jobs.txt
 *
 * Compute topological ordering of a DAG or edge-weighted DAG.
 * Runs in O(E + V) time.
 *
 * % java Topological jobs.txt "/"
 * Calculus
 * Linear Algebra
 * Introduction to CS
 * Advanced Programming
 * Algorithms
 * Theoretical CS
 * Artificial Intelligence
 * Robotics
 * Machine Learning
 * Neural Networks
 * Databases
 * Scientific Computing
 * Computational Biology
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Topological` class represents a data type for
 * determining a topological order of a directed acyclic graph (DAG).
 * Recall, a digraph has a topological order if and only if it is a DAG.
 * The *hasOrder* operation determines whether the digraph has
 * a topological order, and if so, the *order* operation
 * returns one.
 *
 *
 * This implementation uses depth-first search.
 * The constructor takes time proportional to *V* + *E*
 * (in the worst case),
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the *hasOrder* and *rank* operations takes constant time;
 * the *order* operation takes time proportional to *V*.
 *
 *
 * See [DirectedCycle], [DirectedCycleX], and
 * [EdgeWeightedDirectedCycle] to compute a
 * directed cycle if the digraph is not a DAG.
 * See [TopologicalX] for a nonrecursive queue-based algorithm
 * to compute a topological order of a DAG.
 *
 *
 * For additional documentation,
 * see [Section 4.2](https://algs4.cs.princeton.edu/42digraph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class Topological {
    var order: Iterable<Int>? = null  // topological order
        private set
    private lateinit var rank: IntArray                // rank[v] = rank of vertex v in order

    /**
     * Determines whether the digraph `G` has a topological order and, if so,
     * finds such a topological order.
     * @param G the digraph
     */
    constructor(G: Digraph) {
        val finder = DirectedCycle(G)
        if (!finder.hasCycle()) {
            val dfs = DepthFirstOrder(G)
            order = dfs.reversePost()
            rank = IntArray(G.V)
            var i = 0
            for (v in order!!)
                rank[v] = i++
        }
    }

    /**
     * Determines whether the edge-weighted digraph `G` has a topological
     * order and, if so, finds such an order.
     * @param G the edge-weighted digraph
     */
    constructor(G: EdgeWeightedDigraph) {
        val finder = EdgeWeightedDirectedCycle(G)
        if (!finder.hasCycle) {
            val dfs = DepthFirstOrder(G)
            order = dfs.reversePost()
        }
    }

    /**
     * Does the digraph have a topological order?
     * @return `true` if the digraph has a topological order (or equivalently,
     * if the digraph is a DAG), and `false` otherwise
     */
    var hasOrder: Boolean = false
        get() = order != null


    /**
     * The the rank of vertex `v` in the topological order;
     * -1 if the digraph is not a DAG
     *
     * @param v the vertex
     * @return the position of vertex `v` in a topological order
     * of the digraph; -1 if the digraph is not a DAG
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun rank(v: Int): Int {
        validateVertex(v)
        return if (hasOrder) rank[v]
        else -1
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        if (v < 0 || v >= rank.size) throw IllegalArgumentException("vertex $v is not between 0 and ${rank.size - 1}")
    }

    companion object {
        /**
         * Unit tests the `Topological` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val filename = args[0]
            val delimiter = args[1]
            val sg = SymbolDigraph(filename, delimiter)
            val topological = Topological(sg.graph)
            for (v in topological.order!!)
                StdOut.println(sg.nameOf(v))
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
