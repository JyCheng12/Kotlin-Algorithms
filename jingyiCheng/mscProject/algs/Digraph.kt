/******************************************************************************
 * Compilation:  javac Digraph.java
 * Execution:    java Digraph filename.txt
 * Dependencies: Bag.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 * https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 * https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 * A graph, implemented using an array of lists.
 * Parallel edges and self-loops are permitted.
 *
 * % java Digraph tinyDG.txt
 * 13 vertices, 22 edges
 * 0: 5 1
 * 1:
 * 2: 0 3
 * 3: 5 2
 * 4: 3 2
 * 5: 4
 * 6: 9 4 8 0
 * 7: 6 9
 * 8: 6
 * 9: 11 10
 * 10: 12
 * 11: 4 12
 * 12: 9
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Digraph` class represents a directed graph of vertices
 * named 0 through *V* - 1.
 * It supports the following two primary operations: add an edge to the digraph,
 * iterate over all of the vertices adjacent from a given vertex.
 * Parallel edges and self-loops are permitted.
 *
 *
 * This implementation uses an adjacency-lists representation, which
 * is a vertex-indexed array of [nnBag] objects.
 * All operations take constant time (in the worst case) except
 * iterating over the vertices adjacent from a given vertex, which takes
 * time proportional to the number of such vertices.
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

class Digraph {
    val V: Int           // number of vertices in this digraph
    var E: Int = 0                 // number of edges in this digraph
        private set
    private val adj: Array<nnBag<Int>>    // adj[v] = adjacency list for vertex v
    private val indegree: IntArray        // indegree[v] = indegree of vertex v

    /**
     * Initializes an empty digraph with *V* vertices.
     *
     * @param  V the number of vertices
     * @throws IllegalArgumentException if `V < 0`
     */
    constructor(V: Int) {
        if (V < 0) throw IllegalArgumentException("Number of vertices in a Digraph must be nonnegative")
        this.V = V
        this.E = 0
        indegree = IntArray(V)
        adj = Array(V) { nnBag<Int>() }
    }

    /**
     * Initializes a digraph from the specified input stream.
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
            if (V < 0) throw IllegalArgumentException("number of vertices in a Digraph must be non-negative")
            indegree = IntArray(V)
            adj = Array(V) { nnBag<Int>() }
            val E = `in`.readInt()
            if (E < 0) throw IllegalArgumentException("number of edges in a Digraph must be non-negative")
            for (i in 0 until E)
                addEdge(`in`.readInt(), `in`.readInt())
        } catch (e: NoSuchElementException) {
            throw IllegalArgumentException("invalid input format in Digraph constructor", e)
        }
    }

    /**
     * Initializes a new digraph that is a deep copy of the specified digraph.
     *
     * @param  G the digraph to copy
     */
    constructor(G: Digraph) : this(G.V) {
        this.E = G.E
        for (v in 0 until V)
            this.indegree[v] = G.indegree(v)
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
     * Adds the directed edge v→w to this digraph.
     *
     * @param  v the tail vertex
     * @param  w the head vertex
     * @throws IllegalArgumentException unless both `0 <= v < V` and `0 <= w < V`
     */
    fun addEdge(v: Int, w: Int) {
        validateVertex(v)
        validateVertex(w)
        adj[v].add(w)
        indegree[w]++
        E++
    }

    /**
     * Returns the vertices adjacent from vertex `v` in this digraph.
     *
     * @param  v the vertex
     * @return the vertices adjacent from vertex `v` in this digraph, as an iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun adj(v: Int): Iterable<Int> {
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
     * Returns the reverse of the digraph.
     *
     * @return the reverse of the digraph
     */
    fun reverse(): Digraph {
        val reverse = Digraph(V)
        for (v in 0 until V)
            for (w in adj(v))
                reverse.addEdge(w, v)
        return reverse
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return the number of vertices *V*, followed by the number of edges *E*,
     * followed by the *V* adjacency lists
     */
    override fun toString(): String {
        val s = StringBuilder()
        s.append("$V vertices, $E edges $NEWLINE")
        for (v in 0 until V) {
            s.append(String.format("%d: ", v))
            for (w in adj[v])
                s.append(String.format("%d ", w))
            s.append(NEWLINE)
        }
        return s.toString()
    }

    companion object {
        private val NEWLINE = System.getProperty("line.separator")

        /**
         * Unit tests the `Digraph` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Digraph(`in`)
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