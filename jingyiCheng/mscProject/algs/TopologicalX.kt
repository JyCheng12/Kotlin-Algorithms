/******************************************************************************
 * Compilation:  javac TopologicalX.java
 * Execution:    java TopologicalX V E F
 * Dependencies: Queue.kt Digraph.kt
 *
 * Compute topological ordering of a DAG using queue-based algorithm.
 * Runs in O(E + V) time.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `TopologicalX` class represents a data type for
 * determining a topological order of a directed acyclic graph (DAG).
 * Recall, a digraph has a topological order if and only if it is a DAG.
 * The *hasOrder* operation determines whether the digraph has
 * a topological order, and if so, the *order* operation
 * returns one.
 *
 *
 * This implementation uses a nonrecursive, queue-based algorithm.
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
 * See [Topological] for a recursive version that uses depth-first search.
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
class TopologicalX {
    var order: nnQueue<Int>?     // vertices in topological order
        private set
    private var ranks: IntArray              // ranks[v] = order where vertex v appers in order

    /**
     * Determines whether the digraph `G` has a topological order and, if so,
     * finds such a topological order.
     * @param G the digraph
     */
    constructor(G: Digraph) {
        // indegrees of remaining vertices
        val indegree = IntArray(G.V) { G.indegree(it) }

        // initialize
        ranks = IntArray(G.V)
        val order = nnQueue<Int>()
        var count = 0

        // initialize queue to contain all vertices with indegree = 0
        val queue = nnQueue<Int>()
        for (v in 0 until G.V)
            if (indegree[v] == 0) queue.enqueue(v)

        while (!queue.isEmpty) {
            val v = queue.dequeue()
            order.enqueue(v)
            ranks[v] = count++
            for (w in G.adj(v)) {
                indegree[w]--
                if (indegree[w] == 0) queue.enqueue(w)
            }
        }

        // there is a directed cycle in subgraph of vertices with indegree >= 1.
        this.order = if (count != G.V) null else order
        assert(check(G))
    }

    /**
     * Determines whether the edge-weighted digraph `G` has a
     * topological order and, if so, finds such a topological order.
     * @param G the digraph
     */
    constructor(G: EdgeWeightedDigraph) {
        // indegrees of remaining vertices
        val indegree = IntArray(G.V) { G.indegree(it) }

        // initialize
        ranks = IntArray(G.V)
        val order = nnQueue<Int>()
        var count = 0

        // initialize queue to contain all vertices with indegree = 0
        val queue = nnQueue<Int>()
        for (v in 0 until G.V)
            if (indegree[v] == 0) queue.enqueue(v)

        while (!queue.isEmpty) {
            val v = queue.dequeue()
            order.enqueue(v)
            ranks[v] = count++
            for (e in G.adj(v)) {
                val w = e.to
                indegree[w]--
                if (indegree[w] == 0) queue.enqueue(w)
            }
        }

        // there is a directed cycle in subgraph of vertices with indegree >= 1.
        this.order = if (count != G.V) null else order
        assert(check(G))
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
     * @param v vertex
     * @return the position of vertex `v` in a topological order
     * of the digraph; -1 if the digraph is not a DAG
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun rank(v: Int): Int {
        validateVertex(v)
        return if (hasOrder) ranks[v]
        else -1
    }

    // certify that digraph is acyclic
    private fun check(G: Digraph): Boolean {
        // digraph is acyclic
        if (hasOrder) {
            // check that ranks are a permutation of 0 to V-1
            val found = BooleanArray(G.V)
            for (i in 0 until G.V) {
                found[rank(i)] = true
            }
            for (i in 0 until G.V)
                if (!found[i]) {
                    System.err.println("No vertex with rank $i")
                    return false
                }

            // check that ranks provide a valid topological order
            for (v in 0 until G.V)
                for (w in G.adj(v))
                    if (rank(v) > rank(w)) {
                        System.err.printf("%d-%d: rank(%d) = %d, rank(%d) = %d\n", v, w, v, rank(v), w, rank(w))
                        return false
                    }

            // check that order() is consistent with rank()
            for ((r, v) in order!!.withIndex())
                if (rank(v) != r) {
                    System.err.println("order() and rank() inconsistent")
                    return false
                }
        }
        return true
    }

    // certify that digraph is acyclic
    private fun check(G: EdgeWeightedDigraph): Boolean {

        // digraph is acyclic
        if (hasOrder) {
            // check that ranks are a permutation of 0 to V-1
            val found = BooleanArray(G.V)
            for (i in 0 until G.V)
                found[rank(i)] = true
            for (i in 0 until G.V)
                if (!found[i]) {
                    System.err.println("No vertex with rank $i")
                    return false
                }

            // check that ranks provide a valid topological order
            for (v in 0 until G.V)
                for (e in G.adj(v)) {
                    val w = e.to
                    if (rank(v) > rank(w)) {
                        System.err.printf("%d-%d: rank(%d) = %d, rank(%d) = %d\n", v, w, v, rank(v), w, rank(w))
                        return false
                    }
                }

            // check that order() is consistent with rank()
            for ((r, v) in order!!.withIndex())
                if (rank(v) != r) {
                    System.err.println("order() and rank() inconsistent")
                    return false
                }
        }
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        if (v < 0 || v >= ranks.size) throw IllegalArgumentException("vertex $v is not between 0 and ${ranks.size - 1}")
    }

    companion object {

        /**
         * Unit tests the `TopologicalX` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {

            // create random DAG with V vertices and E edges; then add F random edges
            val V = Integer.parseInt(args[0])
            val E = Integer.parseInt(args[1])
            val F = Integer.parseInt(args[2])

            val G1 = DigraphGenerator.dag(V, E)

            // corresponding edge-weighted digraph
            val G2 = EdgeWeightedDigraph(V)
            for (v in 0 until G1.V)
                for (w in G1.adj(v))
                    G2.addEdge(DirectedEdge(v, w, 0.0))

            // add F extra edges
            for (i in 0 until F) {
                val v = StdRandom.uniform(V)
                val w = StdRandom.uniform(V)
                G1.addEdge(v, w)
                G2.addEdge(DirectedEdge(v, w, 0.0))
            }

            StdOut.println("$G1\n$G2")

            // find a directed cycle
            val topological1 = TopologicalX(G1)
            if (!topological1.hasOrder)
                StdOut.println("Not a DAG")
            else {
                StdOut.print("Topological order: ")
                for (v in topological1.order!!)
                    StdOut.print("$v ")
                StdOut.println()
            }// or give topologial sort

            // find a directed cycle
            val topological2 = TopologicalX(G2)
            if (!topological2.hasOrder)
                StdOut.println("Not a DAG")
            else {
                StdOut.print("Topological order: ")
                for (v in topological2.order!!)
                    StdOut.print("$v ")
                StdOut.println()
            }// or give topologial sort
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