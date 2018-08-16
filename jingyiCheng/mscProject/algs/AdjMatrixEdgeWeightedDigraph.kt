/******************************************************************************
 * Compilation:  javac AdjMatrixEdgeWeightedDigraph.kt
 * Execution:    java AdjMatrixEdgeWeightedDigraph V E
 * Dependencies: StdOut.kt
 *
 * An edge-weighted digraph, implemented using an adjacency matrix.
 * Parallel edges are disallowed; self-loops are allowed.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `AdjMatrixEdgeWeightedDigraph` class represents a edge-weighted
 * digraph of vertices named 0 through *V* - 1, where each
 * directed edge is of type [DirectedEdge] and has a real-valued weight.
 * It supports the following two primary operations: add a directed edge
 * to the digraph and iterate over all of edges incident from a given vertex.
 * It also provides
 * methods for returning the number of vertices *V* and the number
 * of edges *E*. Parallel edges are disallowed; self-loops are permitted.
 *
 *
 * This implementation uses an adjacency-matrix representation.
 * All operations take constant time (in the worst case) except
 * iterating over the edges incident from a given vertex, which takes
 * time proportional to *V*.
 *
 *
 * For additional documentation,
 * see [Section 4.4](https://algs4.cs.princeton.edu/44sp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class AdjMatrixEdgeWeightedDigraph
/**
 * Initializes an empty edge-weighted digraph with `V` vertices and 0 edges.
 * @param V the number of vertices
 * @throws IllegalArgumentException if `V < 0`
 */
(val V: Int) {
    var E: Int = 0
        private set
    private val adj: Array<Array<DirectedEdge?>>

    init {
        if (V < 0) throw IllegalArgumentException("number of vertices must be non-negative")
        this.E = 0
        this.adj = Array(V) { arrayOfNulls<DirectedEdge>(V) }
    }

    /**
     * Initializes a random edge-weighted digraph with `V` vertices and *E* edges.
     * @param V the number of vertices
     * @param E the number of edges
     * @throws IllegalArgumentException if `V < 0`
     * @throws IllegalArgumentException if `E < 0`
     */
    constructor(V: Int, E: Int) : this(V) {
        if (E < 0) throw IllegalArgumentException("number of edges must be non-negative")
        if (E > V * V) throw IllegalArgumentException("too many edges")

        // can be inefficient
        while (this.E != E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val weight = Math.round(100 * StdRandom.uniform()) / 100.0
            addEdge(DirectedEdge(v, w, weight))
        }
    }

    /**
     * Adds the directed edge `e` to the edge-weighted digraph (if there
     * is not already an edge with the same endpoints).
     * @param e the edge
     */
    fun addEdge(e: DirectedEdge) {
        val v = e.from
        val w = e.to
        validateVertex(v)
        validateVertex(w)
        if (adj[v][w] == null) {
            E++
            adj[v][w] = e
        }
    }

    /**
     * Returns the directed edges incident from vertex `v`.
     * @param v the vertex
     * @return the directed edges incident from vertex `v` as an Iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun adj(v: Int): Iterable<DirectedEdge> {
        validateVertex(v)
        return AdjIterator(v)
    }

    // support iteration over graph vertices
    private inner class AdjIterator(private val v: Int) : Iterator<DirectedEdge>, Iterable<DirectedEdge> {
        private var w = 0

        override fun iterator(): Iterator<DirectedEdge>  = this

        override fun hasNext(): Boolean {
            while (w < V) {
                if (adj[v][w] != null) return true
                w++
            }
            return false
        }

        override fun next(): DirectedEdge {
            if (!hasNext()) throw NoSuchElementException()
            return adj[v][w++]!!
        }
    }

    /**
     * Returns a string representation of the edge-weighted digraph. This method takes
     * time proportional to *V*<sup>2</sup>.
     * @return the number of vertices *V*, followed by the number of edges *E*,
     * followed by the *V* adjacency lists of edges
     */
    override fun toString(): String {
        val s = StringBuilder()
        s.append("$V $E$NEWLINE")
        for (v in 0 until V) {
            s.append("$v: ")
            for (e in adj(v))
                s.append("$e  ")
            s.append(NEWLINE)
        }
        return s.toString()
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {
        private val NEWLINE = System.getProperty("line.separator")

        /**
         * Unit tests the `AdjMatrixEdgeWeightedDigraph` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val V = Integer.parseInt(args[0])
            val E = Integer.parseInt(args[1])
            val G = AdjMatrixEdgeWeightedDigraph(V, E)
            StdOut.println(G)
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