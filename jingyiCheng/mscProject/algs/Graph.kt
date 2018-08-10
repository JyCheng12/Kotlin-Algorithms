/******************************************************************************
 * Compilation:  javac Graph.java
 * Execution:    java Graph input.txt
 * Dependencies: Bag.kt Stack.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/41graph/tinyG.txt
 * https://algs4.cs.princeton.edu/41graph/mediumG.txt
 * https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 * A graph, implemented using an array of sets.
 * Parallel edges and self-loops allowed.
 *
 * % java Graph tinyG.txt
 * 13 vertices, 13 edges
 * 0: 6 2 1 5
 * 1: 0
 * 2: 0
 * 3: 5 4
 * 4: 5 6 3
 * 5: 3 4 0
 * 6: 0 4
 * 7: 8
 * 8: 7
 * 9: 11 10 12
 * 10: 9
 * 11: 9 12
 * 12: 11 9
 *
 * % java Graph mediumG.txt
 * 250 vertices, 1273 edges
 * 0: 225 222 211 209 204 202 191 176 163 160 149 114 97 80 68 59 58 49 44 24 15
 * 1: 220 203 200 194 189 164 150 130 107 72
 * 2: 141 110 108 86 79 51 42 18 14
 * ...
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Graph` class represents an undirected graph of vertices
 * named 0 through *V* – 1.
 * It supports the following two primary operations: add an edge to the graph,
 * iterate over all of the vertices adjacent to a vertex. It also provides
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
 * iterating over the vertices adjacent to a given vertex, which takes
 * time proportional to the number of such vertices.
 *
 *
 * For additional documentation, see [Section 4.1](https://algs4.cs.princeton.edu/41graph)
 * of *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class Graph {
    val V: Int
    var E: Int = 0
        private set
    private var adj: Array<nnBag<Int>>

    /**
     * Initializes an empty graph with `V` vertices and 0 edges.
     * param V the number of vertices
     *
     * @param  V number of vertices
     * @throws IllegalArgumentException if `V < 0`
     */
    constructor(V: Int) {
        if (V < 0) throw IllegalArgumentException("Number of vertices must be non-negative")
        this.V = V
        adj = Array(V) { nnBag<Int>() }
    }

    /**
     * Initializes a graph from the specified input stream.
     * The format is the number of vertices *V*,
     * followed by the number of edges *E*,
     * followed by *E* pairs of vertices, with each entry separated by whitespace.
     *
     * @param  in the input stream
     * @throws IllegalArgumentException if the endpoints of any edge are not in prescribed range
     * @throws IllegalArgumentException if the number of vertices or edges is negative
     * @throws IllegalArgumentException if the input stream is in the wrong format
     */
    constructor(`in`: In) {
        try {
            this.V = `in`.readInt()
            if (V < 0) throw IllegalArgumentException("number of vertices in a Graph must be non-negative")
            adj = Array(V) { nnBag<Int>() }
            val E = `in`.readInt()
            if (E < 0) throw IllegalArgumentException("number of edges in a Graph must be non-negative")
            for (i in 0 until E) {
                val v = `in`.readInt()
                val w = `in`.readInt()
                validateVertex(v)
                validateVertex(w)
                addEdge(v, w)
            }
        } catch (e: NoSuchElementException) {
            throw IllegalArgumentException("invalid input format in Graph constructor", e)
        }
    }

    /**
     * Initializes a new graph that is a deep copy of `G`.
     *
     * @param  G the graph to copy
     */
    constructor(G: Graph) : this(G.V) {
        this.E = G.E
        for (v in 0 until G.V) {
            // reverse so that adjacency list is in same order as original
            val reverse = Stack<Int>()
            for (w in G.adj[v])
                reverse.push(w)
            for (w in reverse)
                adj[v].add(w)
        }
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    /**
     * Adds the undirected edge v-w to this graph.
     *
     * @param  v one vertex in the edge
     * @param  w the other vertex in the edge
     * @throws IllegalArgumentException unless both `0 <= v < V` and `0 <= w < V`
     */
    fun addEdge(v: Int, w: Int) {
        validateVertex(v)
        validateVertex(w)
        E++
        adj[v].add(w)
        adj[w].add(v)
    }

    /**
     * Returns the vertices adjacent to vertex `v`.
     *
     * @param  v the vertex
     * @return the vertices adjacent to vertex `v`, as an iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun adj(v: Int): Iterable<Int> {
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
     * Returns a string representation of this graph.
     *
     * @return the number of vertices *V*, followed by the number of edges *E*,
     * followed by the *V* adjacency lists
     */
    override fun toString(): String {
        val s = StringBuilder()
        s.append("$V vertices, $E edges $NEWLINE")
        for (v in 0 until V) {
            s.append("$v: ")
            for (w in adj[v])
                s.append("$w ")
            s.append(NEWLINE)
        }
        return s.toString()
    }

    companion object {
        private val NEWLINE = System.getProperty("line.separator")
        /**
         * Unit tests the `Graph` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Graph(`in`)
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
