/******************************************************************************
 * Compilation:  javac CC.java
 * Execution:    java CC filename.txt
 * Dependencies: Graph.kt StdOut.kt Queue.kt
 * Data files:   https://algs4.cs.princeton.edu/41graph/tinyG.txt
 * https://algs4.cs.princeton.edu/41graph/mediumG.txt
 * https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 * Compute connected components using depth first search.
 * Runs in O(E + V) time.
 *
 * % java CC tinyG.txt
 * 3 components
 * 0 1 2 3 4 5 6
 * 7 8
 * 9 10 11 12
 *
 * % java CC mediumG.txt
 * 1 components
 * 0 1 2 3 4 5 6 7 8 9 10 ...
 *
 * % java -Xss50m CC largeG.txt
 * 1 components
 * 0 1 2 3 4 5 6 7 8 9 10 ...
 *
 * Note: This implementation uses a recursive DFS. To avoid needing
 * a potentially very large stack size, replace with a non-recurisve
 * DFS ala NonrecursiveDFS.kt.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `CC` class represents a data type for
 * determining the connected components in an undirected graph.
 * The *id* operation determines in which connected component
 * a given vertex lies; the *connected* operation
 * determines whether two vertices are in the same connected component;
 * the *count* operation determines the number of connected
 * components; and the *size* operation determines the number
 * of vertices in the connect component containing a given vertex.
 *
 * The *component identifier* of a connected component is one of the
 * vertices in the connected component: two vertices have the same component
 * identifier if and only if they are in the same connected component.
 *
 *
 *
 * This implementation uses depth-first search.
 * The constructor takes time proportional to *V* + *E*
 * (in the worst case),
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the *id*, *count*, *connected*,
 * and *size* operations take constant time.
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
class CC {
    private var marked:BooleanArray  // marked[v] = has vertex v been marked?
    private var id: IntArray           // id[v] = id of connected component containing v
    private var size: IntArray         // size[id] = number of vertices in given component
    var count: Int = 0          // number of connected components
        private set

    /**
     * Computes the connected components of the undirected graph `G`.
     *
     * @param G the undirected graph
     */
    constructor(G: Graph) {
        marked = BooleanArray(G.V)
        id = IntArray(G.V)
        size = IntArray(G.V)
        for (v in 0 until G.V) {
            if (!marked[v]) {
                dfs(G, v)
                count++
            }
        }
    }

    /**
     * Computes the connected components of the edge-weighted graph `G`.
     *
     * @param G the edge-weighted graph
     */
    constructor(G: EdgeWeightedGraph) {
        marked = BooleanArray(G.V)
        id = IntArray(G.V)
        size = IntArray(G.V)
        for (v in 0 until G.V) {
            if (!marked[v]) {
                dfs(G, v)
                count++
            }
        }
    }

    // depth-first search for a Graph
    private fun dfs(G: Graph, v: Int) {
        marked[v] = true
        id[v] = count
        size[count]++
        for (w in G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w)
            }
        }
    }

    // depth-first search for an EdgeWeightedGraph
    private fun dfs(G: EdgeWeightedGraph, v: Int) {
        marked[v] = true
        id[v] = count
        size[count]++
        for (e in G.adj(v)) {
            val w = e.other(v)
            if (!marked[w]) {
                dfs(G, w)
            }
        }
    }

    /**
     * Returns the component id of the connected component containing vertex `v`.
     *
     * @param  v the vertex
     * @return the component id of the connected component containing vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun id(v: Int): Int {
        validateVertex(v)
        return id[v]
    }

    /**
     * Returns the number of vertices in the connected component containing vertex `v`.
     *
     * @param  v the vertex
     * @return the number of vertices in the connected component containing vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun size(v: Int): Int {
        validateVertex(v)
        return size[id[v]]
    }

    /**
     * Returns true if vertices `v` and `w` are in the same
     * connected component.
     *
     * @param  v one vertex
     * @param  w the other vertex
     * @return `true` if vertices `v` and `w` are in the same
     * connected component; `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     * @throws IllegalArgumentException unless `0 <= w < V`
     */
    fun connected(v: Int, w: Int): Boolean {
        validateVertex(v)
        validateVertex(w)
        return id(v) == id(w)
    }

    /**
     * Returns true if vertices `v` and `w` are in the same
     * connected component.
     *
     * @param  v one vertex
     * @param  w the other vertex
     * @return `true` if vertices `v` and `w` are in the same
     * connected component; `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     * @throws IllegalArgumentException unless `0 <= w < V`
     */
    @Deprecated("Replaced by {@link #connected(int, int)}.")
    fun areConnected(v: Int, w: Int): Boolean {
        validateVertex(v)
        validateVertex(w)
        return id(v) == id(w)
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = marked.size
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {

        /**
         * Unit tests the `CC` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Graph(`in`)
            val cc = CC(G)

            // number of connected components
            val m = cc.count
            StdOut.println("$m components")

            // compute list of vertices in each connected component
            val components = Array(m) { nnQueue<Int>() }
            for (v in 0 until G.V) {
                components[cc.id(v)].enqueue(v)
            }

            // print results
            for (i in 0 until m) {
                for (v in components[i]) {
                    StdOut.print("$v ")
                }
                StdOut.println()
            }
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
