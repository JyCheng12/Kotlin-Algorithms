/******************************************************************************
 * Compilation:  javac EdgeWeightedDigraph.java
 * Execution:    java EdgeWeightedDigraph digraph.txt
 * Dependencies: Bag.kt DirectedEdge.kt
 * Data files:   https://algs4.cs.princeton.edu/44st/tinyEWD.txt
 * https://algs4.cs.princeton.edu/44st/mediumEWD.txt
 * https://algs4.cs.princeton.edu/44st/largeEWD.txt
 *
 * An edge-weighted digraph, implemented using adjacency lists.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `EdgeWeightedDigraph` class represents a edge-weighted
 * digraph of vertices named 0 through *V* - 1, where each
 * directed edge is of type [DirectedEdge] and has a real-valued weight.
 * It supports the following two primary operations: add a directed edge
 * to the digraph and iterate over all of edges incident from a given vertex.
 * It also provides
 * methods for returning the number of vertices *V* and the number
 * of edges *E*. Parallel edges and self-loops are permitted.
 *
 *
 * This implementation uses an adjacency-lists representation, which
 * is a vertex-indexed array of [Bag] objects.
 * All operations take constant time (in the worst case) except
 * iterating over the edges incident from a given vertex, which takes
 * time proportional to the number of such edges.
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
class EdgeWeightedDigraph {

    val V: Int                // number of vertices in this digraph
    var E: Int = 0                      // number of edges in this digraph
        private set
    private val adj: Array<nnBag<DirectedEdge>>    // adj[v] = adjacency list for vertex v
    private val indegree: IntArray             // indegree[v] = indegree of vertex v

    /**
     * Initializes an empty edge-weighted digraph with `V` vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if `V < 0`
     */
    constructor(V: Int) {
        if (V < 0) throw IllegalArgumentException("Number of vertices in a Digraph must be non-negative")
        this.V = V
        this.indegree = IntArray(V)
        adj = Array(V) { nnBag<DirectedEdge>() }
    }

    /**
     * Initializes a random edge-weighted digraph with `V` vertices and *E* edges.
     *
     * @param  V the number of vertices
     * @param  E the number of edges
     * @throws IllegalArgumentException if `V < 0`
     * @throws IllegalArgumentException if `E < 0`
     */
    constructor(V: Int, E: Int) : this(V) {
        if (E < 0) throw IllegalArgumentException("Number of edges in a Digraph must be nonnegative")
        for (i in 0 until E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val weight = 0.01 * StdRandom.uniform(100)
            val e = DirectedEdge(v, w, weight)
            addEdge(e)
        }
    }

    /**
     * Initializes an edge-weighted digraph from the specified input stream.
     * The format is the number of vertices *V*,
     * followed by the number of edges *E*,
     * followed by *E* pairs of vertices and edge weights,
     * with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    constructor(`in`: In) : this(`in`.readInt()) {
        val E = `in`.readInt()
        if (E < 0) throw IllegalArgumentException("Number of edges must be non-negative")
        for (i in 0 until E) {
            val v = `in`.readInt()
            val w = `in`.readInt()
            validateVertex(v)
            validateVertex(w)
            val weight = `in`.readDouble()
            addEdge(DirectedEdge(v, w, weight))
        }
    }

    /**
     * Initializes a new edge-weighted digraph that is a deep copy of `G`.
     *
     * @param  G the edge-weighted digraph to copy
     */
    constructor(G: EdgeWeightedDigraph) : this(G.V) {
        this.E = G.E
        for (v in 0 until G.V)
            this.indegree[v] = G.indegree(v)
        for (v in 0 until G.V) {
            // reverse so that adjacency list is in same order as original
            val reverse = Stack<DirectedEdge>()
            for (e in G.adj[v])
                reverse.push(e)
            for (e in reverse)
                adj[v].add(e)
        }
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    /**
     * Adds the directed edge `e` to this edge-weighted digraph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between `0`
     * and `V-1`
     */
    fun addEdge(e: DirectedEdge) {
        val v = e.from
        val w = e.to
        validateVertex(v)
        validateVertex(w)
        adj[v].add(e)
        indegree[w]++
        E++
    }

    /**
     * Returns the directed edges incident from vertex `v`.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex `v` as an Iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun adj(v: Int): Iterable<DirectedEdge> {
        validateVertex(v)
        return adj[v]
    }

    /**
     * Returns the number of directed edges incident from vertex `v`.
     * This is known as the *outdegree* of vertex `v`.
     *
     * @param  v the vertex
     * @return the outdegree of vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun outdegree(v: Int): Int {
        validateVertex(v)
        return adj[v].size
    }

    /**
     * Returns the number of directed edges incident to vertex `v`.
     * This is known as the *indegree* of vertex `v`.
     *
     * @param  v the vertex
     * @return the indegree of vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun indegree(v: Int): Int {
        validateVertex(v)
        return indegree[v]
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * `for (DirectedEdge e : G.edges())`.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    fun edges(): Iterable<DirectedEdge> {
        val list = nnBag<DirectedEdge>()
        for (v in 0 until V)
            for (e in adj(v))
                list.add(e)
        return list
    }

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices *V*, followed by the number of edges *E*,
     * followed by the *V* adjacency lists of edges
     */
    override fun toString(): String {
        val s = StringBuilder()
        s.append("$V $E$NEWLINE")
        for (v in 0 until V) {
            s.append("$v: ")
            for (e in adj[v])
                s.append("$e  ")
            s.append(NEWLINE)
        }
        return s.toString()
    }

    companion object {
        private val NEWLINE = System.getProperty("line.separator")

        /**
         * Unit tests the `EdgeWeightedDigraph` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = EdgeWeightedDigraph(`in`)
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