/******************************************************************************
 * Compilation:  javac FlowNetwork.java
 * Execution:    java FlowNetwork V E
 * Dependencies: Bag.kt FlowEdge.kt
 *
 * A capacitated flow network, implemented using adjacency lists.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `FlowNetwork` class represents a capacitated network
 * with vertices named 0 through *V* - 1, where each directed
 * edge is of type [FlowEdge] and has a real-valued capacity
 * and flow.
 * It supports the following two primary operations: add an edge to the network,
 * iterate over all of the edges incident to or from a vertex. It also provides
 * methods for returning the number of vertices *V* and the number
 * of edges *E*. Parallel edges and self-loops are permitted.
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
 * see [Section 6.4](https://algs4.cs.princeton.edu/64maxflow) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class FlowNetwork {
    val V: Int
    var E: Int = 0
        private set
    private val adj: Array<nnBag<FlowEdge>>

    /**
     * Initializes an empty flow network with `V` vertices and 0 edges.
     * @param V the number of vertices
     * @throws IllegalArgumentException if `V < 0`
     */
    constructor(V: Int) {
        if (V < 0) throw IllegalArgumentException("Number of vertices in a Graph must be non-negative")
        this.V = V
        adj = Array(V) { nnBag<FlowEdge>() }
    }

    /**
     * Initializes a random flow network with `V` vertices and *E* edges.
     * The capacities are integers between 0 and 99 and the flow values are zero.
     * @param V the number of vertices
     * @param E the number of edges
     * @throws IllegalArgumentException if `V < 0`
     * @throws IllegalArgumentException if `E < 0`
     */
    constructor(V: Int, E: Int) : this(V) {
        if (E < 0) throw IllegalArgumentException("Number of edges must be non-negative")
        for (i in 0 until E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val capacity = StdRandom.uniform(100).toDouble()
            addEdge(FlowEdge(v, w, capacity))
        }
    }

    /**
     * Initializes a flow network from an input stream.
     * The format is the number of vertices *V*,
     * followed by the number of edges *E*,
     * followed by *E* pairs of vertices and edge capacities,
     * with each entry separated by whitespace.
     * @param in the input stream
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     */
    constructor(`in`: In) : this(`in`.readInt()) {
        val E = `in`.readInt()
        if (E < 0) throw IllegalArgumentException("number of edges must be non-negative")
        for (i in 0 until E) {
            val v = `in`.readInt()
            val w = `in`.readInt()
            validateVertex(v)
            validateVertex(w)
            val capacity = `in`.readDouble()
            addEdge(FlowEdge(v, w, capacity))
        }
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    /**
     * Adds the edge `e` to the network.
     * @param e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between
     * `0` and `V-1`
     */
    fun addEdge(e: FlowEdge) {
        val v = e.from
        val w = e.to
        validateVertex(v)
        validateVertex(w)
        adj[v].add(e)
        adj[w].add(e)
        E++
    }

    /**
     * Returns the edges incident on vertex `v` (includes both edges pointing to
     * and from `v`).
     * @param v the vertex
     * @return the edges incident on vertex `v` as an Iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun adj(v: Int): Iterable<FlowEdge> {
        validateVertex(v)
        return adj[v]
    }

    // return list of all edges - excludes self loops
    fun edges(): Iterable<FlowEdge> {
        val list = nnBag<FlowEdge>()
        for (v in 0 until V)
            for (e in adj(v))
                if (e.to != v)
                    list.add(e)
        return list
    }

    /**
     * Returns a string representation of the flow network.
     * This method takes time proportional to *E* + *V*.
     * @return the number of vertices *V*, followed by the number of edges *E*,
     * followed by the *V* adjacency lists
     */
    override fun toString(): String {
        val s = StringBuilder()
        s.append("$V $E$NEWLINE")
        for (v in 0 until V) {
            s.append("$v:  ")
            for (e in adj[v])
                if (e.to != v) s.append("$e  ")
            s.append(NEWLINE)
        }
        return s.toString()
    }

    companion object {
        private val NEWLINE = System.getProperty("line.separator")

        /**
         * Unit tests the `FlowNetwork` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = FlowNetwork(`in`)
            StdOut.println(G)
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