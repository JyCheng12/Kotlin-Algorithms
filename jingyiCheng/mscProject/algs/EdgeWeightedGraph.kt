/******************************************************************************
 * Compilation:  javac EdgeWeightedGraph.java
 * Execution:    java EdgeWeightedGraph filename.txt
 * Dependencies: Bag.kt Edge.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 * https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 * https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 * An edge-weighted undirected graph, implemented using adjacency lists.
 * Parallel edges and self-loops are permitted.
 *
 * % java EdgeWeightedGraph tinyEWG.txt
 * 8 16
 * 0: 6-0 0.58000  0-2 0.26000  0-4 0.38000  0-7 0.16000
 * 1: 1-3 0.29000  1-2 0.36000  1-7 0.19000  1-5 0.32000
 * 2: 6-2 0.40000  2-7 0.34000  1-2 0.36000  0-2 0.26000  2-3 0.17000
 * 3: 3-6 0.52000  1-3 0.29000  2-3 0.17000
 * 4: 6-4 0.93000  0-4 0.38000  4-7 0.37000  4-5 0.35000
 * 5: 1-5 0.32000  5-7 0.28000  4-5 0.35000
 * 6: 6-4 0.93000  6-0 0.58000  3-6 0.52000  6-2 0.40000
 * 7: 2-7 0.34000  1-7 0.19000  0-7 0.16000  5-7 0.28000  4-7 0.37000
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `EdgeWeightedGraph` class represents an edge-weighted
 * graph of vertices named 0 through *V* – 1, where each
 * undirected edge is of type [Edge] and has a real-valued weight.
 * It supports the following two primary operations: add an edge to the graph,
 * iterate over all of the edges incident to a vertex. It also provides
 * methods for returning the number of vertices *V* and the number
 * of edges *E*. Parallel edges and self-loops are permitted.
 * By convention, a self-loop *v*-*v* appears in the
 * adjacency list of *v* twice and contributes two to the degree
 * of *v*.
 *
 *
 * This implementation uses an adjacency-lists representation, which
 * is a vertex-indexed array of [Bag] objects.
 * All operations take constant time (in the worst case) except
 * iterating over the edges incident to a given vertex, which takes
 * time proportional to the number of such edges.
 *
 *
 * For additional documentation,
 * see [Section 4.3](https://algs4.cs.princeton.edu/43mst) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class EdgeWeightedGraph {
    val V: Int
    var E: Int = 0
    private set
    private val adj: Array<nnBag<Edge>>

    /**
     * Initializes an empty edge-weighted graph with `V` vertices and 0 edges.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if `V < 0`
     */
    constructor(V: Int) {
        if (V < 0) throw IllegalArgumentException("Number of vertices must be non-negative")
        this.V = V
        adj = Array(V){nnBag<Edge>()}
    }

    /**
     * Initializes a random edge-weighted graph with `V` vertices and *E* edges.
     *
     * @param  V the number of vertices
     * @param  E the number of edges
     * @throws IllegalArgumentException if `V < 0`
     * @throws IllegalArgumentException if `E < 0`
     */
    constructor(V: Int, E: Int) : this(V) {
        if (E < 0) throw IllegalArgumentException("Number of edges must be non-negative")
        for (i in 0 until E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val weight = Math.round(100 * StdRandom.uniform()) / 100.0
            val e = Edge(v, w, weight)
            addEdge(e)
        }
    }

    /**
     * Initializes an edge-weighted graph from an input stream.
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
            val e = Edge(v, w, weight)
            addEdge(e)
        }
    }

    /**
     * Initializes a new edge-weighted graph that is a deep copy of `G`.
     *
     * @param  G the edge-weighted graph to copy
     */
    constructor(G: EdgeWeightedGraph) : this(G.V) {
        this.E = G.E
        for (v in 0 until G.V) {
            // reverse so that adjacency list is in same order as original
            val reverse = Stack<Edge>()
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
     * Adds the undirected edge `e` to this edge-weighted graph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless both endpoints are between `0` and `V-1`
     */
    fun addEdge(e: Edge) {
        val v = e.either
        val w = e.other(v)
        validateVertex(v)
        validateVertex(w)
        adj[v].add(e)
        adj[w].add(e)
        E++
    }

    /**
     * Returns the edges incident on vertex `v`.
     *
     * @param  v the vertex
     * @return the edges incident on vertex `v` as an Iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun adj(v: Int): Iterable<Edge> {
        validateVertex(v)
        return adj[v]
    }

    /**
     * Returns the degree of vertex `v`.
     *
     * @param  v the vertex
     * @return the degree of vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun degree(v: Int): Int {
        validateVertex(v)
        return adj[v].size
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * `for (Edge e : G.edges())`.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
    fun edges(): Iterable<Edge> {
        val list = nnBag<Edge>()
        (0 until V).forEach { v ->
            var selfLoops = 0
            adj(v).forEach { e ->
                if (e.other(v) > v)
                    list.add(e)
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e)
                    selfLoops++
                }
            }// add only one copy of each self loop (self loops will be consecutive)
        }
        return list
    }

    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to *E* + *V*.
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
         * Unit tests the `EdgeWeightedGraph` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = EdgeWeightedGraph(`in`)
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