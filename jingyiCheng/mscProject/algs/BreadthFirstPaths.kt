/******************************************************************************
 * Compilation:  javac BreadthFirstPaths.java
 * Execution:    java BreadthFirstPaths G s
 * Dependencies: Graph.kt Queue.kt Stack.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/41graph/tinyCG.txt
 * https://algs4.cs.princeton.edu/41graph/tinyG.txt
 * https://algs4.cs.princeton.edu/41graph/mediumG.txt
 * https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 * Run breadth first search on an undirected graph.
 * Runs in O(E + V) time.
 *
 * %  java Graph tinyCG.txt
 * 6 8
 * 0: 2 1 5
 * 1: 0 2
 * 2: 0 1 3 4
 * 3: 5 4 2
 * 4: 3 2
 * 5: 3 0
 *
 * %  java BreadthFirstPaths tinyCG.txt 0
 * 0 to 0 (0):  0
 * 0 to 1 (1):  0-1
 * 0 to 2 (1):  0-2
 * 0 to 3 (2):  0-2-3
 * 0 to 4 (2):  0-2-4
 * 0 to 5 (1):  0-5
 *
 * %  java BreadthFirstPaths largeG.txt 0
 * 0 to 0 (0):  0
 * 0 to 1 (418):  0-932942-474885-82707-879889-971961-...
 * 0 to 2 (323):  0-460790-53370-594358-780059-287921-...
 * 0 to 3 (168):  0-713461-75230-953125-568284-350405-...
 * 0 to 4 (144):  0-460790-53370-310931-440226-380102-...
 * 0 to 5 (566):  0-932942-474885-82707-879889-971961-...
 * 0 to 6 (349):  0-932942-474885-82707-879889-971961-...
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `BreadthFirstPaths` class represents a data type for finding
 * shortest paths (number of edges) from a source vertex *s*
 * (or a set of source vertices)
 * to every other vertex in an undirected graph.
 *
 *
 * This implementation uses breadth-first search.
 * The constructor takes time proportional to *V* + *E*,
 * where *V* is the number of vertices and *E* is the number of edges.
 * Each call to [.distTo] and [.hasPathTo] takes constant time;
 * each call to [.pathTo] takes time proportional to the length
 * of the path.
 * It uses extra space (not including the graph) proportional to *V*.
 *
 *
 * For additional documentation,
 * see [Section 4.1](https://algs4.cs.princeton.edu/41graph)
 * of *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class BreadthFirstPaths {
    private val marked: BooleanArray  // marked[v] = is there an s-v path
    private val edgeTo: IntArray      // edgeTo[v] = previous edge on shortest s-v path
    private val distTo: IntArray       // distTo[v] = number of edges shortest s-v path

    /**
     * Computes the shortest path between the source vertex `s`
     * and every other vertex in the graph `G`.
     * @param G the graph
     * @param s the source vertex
     * @throws IllegalArgumentException unless `0 <= s < V`
     */
    constructor(G: Graph, s: Int) {
        marked = BooleanArray(G.V)
        distTo = IntArray(G.V)
        edgeTo = IntArray(G.V)
        validateVertex(s)
        bfs(G, s)

        assert(check(G, s))
    }

    /**
     * Computes the shortest path between any one of the source vertices in `sources`
     * and every other vertex in graph `G`.
     * @param G the graph
     * @param sources the source vertices
     * @throws IllegalArgumentException unless `0 <= s < V` for each vertex
     * `s` in `sources`
     */
    constructor(G: Graph, sources: Iterable<Int>) {
        marked = BooleanArray(G.V)
        distTo = IntArray(G.V)
        edgeTo = IntArray(G.V)
        for (v in 0 until G.V)
            distTo[v] = INFINITY
        validateVertices(sources)
        bfs(G, sources)
    }


    // breadth-first search from a single source
    private fun bfs(G: Graph, s: Int) {
        val q = nnQueue<Int>()
        for (v in 0 until G.V)
            distTo[v] = INFINITY
        distTo[s] = 0
        marked[s] = true
        q.enqueue(s)

        while (!q.isEmpty) {
            val v = q.dequeue()
            for (w in G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v
                    distTo[w] = distTo[v] + 1
                    marked[w] = true
                    q.enqueue(w)
                }
            }
        }
    }

    // breadth-first search from multiple sources
    private fun bfs(G: Graph, sources: Iterable<Int>) {
        val q = nnQueue<Int>()
        for (s in sources) {
            marked[s] = true
            distTo[s] = 0
            q.enqueue(s)
        }
        while (!q.isEmpty) {
            val v = q.dequeue()
            for (w in G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v
                    distTo[w] = distTo[v] + 1
                    marked[w] = true
                    q.enqueue(w)
                }
            }
        }
    }

    /**
     * Is there a path between the source vertex `s` (or sources) and vertex `v`?
     * @param v the vertex
     * @return `true` if there is a path, and `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun hasPathTo(v: Int): Boolean {
        validateVertex(v)
        return marked[v]
    }

    /**
     * Returns the number of edges in a shortest path between the source vertex `s`
     * (or sources) and vertex `v`?
     * @param v the vertex
     * @return the number of edges in a shortest path
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun distTo(v: Int): Int {
        validateVertex(v)
        return distTo[v]
    }

    /**
     * Returns a shortest path between the source vertex `s` (or sources)
     * and `v`, or `null` if no such path.
     * @param  v the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun pathTo(v: Int): Iterable<Int>? {
        validateVertex(v)
        if (!hasPathTo(v)) return null
        val path = nnStack<Int>()
        var x: Int = v
        while (distTo[x] != 0) {
            path.push(x)
            x = edgeTo[x]
        }
        path.push(x)
        return path
    }


    // check optimality conditions for single source
    private fun check(G: Graph, s: Int): Boolean {

        // check that the distance of s = 0
        if (distTo[s] != 0) {
            StdOut.println("distance of source $s to itself = ${distTo[s]}")
            return false
        }

        // check that for each edge v-w dist[w] <= dist[v] + 1
        // provided v is reachable from s
        for (v in 0 until G.V) {
            for (w in G.adj(v)) {
                if (hasPathTo(v) != hasPathTo(w)) {
                    StdOut.println("edge $v-$w")
                    StdOut.println("hasPathTo($v) = ${hasPathTo(v)}")
                    StdOut.println("hasPathTo($w) = ${hasPathTo(w)}")
                    return false
                }
                if (hasPathTo(v) && distTo[w] > distTo[v] + 1) {
                    StdOut.println("edge $v-$w")
                    StdOut.println("distTo[$v] = ${distTo[v]}")
                    StdOut.println("distTo[$w] = ${distTo[w]}")
                    return false
                }
            }
        }

        // check that v = edgeTo[w] satisfies distTo[w] = distTo[v] + 1
        // provided v is reachable from s
        for (w in 0 until G.V) {
            if (!hasPathTo(w) || w == s) continue
            val v = edgeTo[w]
            if (distTo[w] != distTo[v] + 1) {
                StdOut.println("shortest path edge $v-$w")
                StdOut.println("distTo[$v] = ${distTo[v]}")
                StdOut.println("distTo[$w] = ${distTo[w]}")
                return false
            }
        }
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = marked.size
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertices(vertices: Iterable<Int>?) {
        if (vertices == null) throw IllegalArgumentException("argument is null")
        val V = marked.size
        for (v in vertices)
            if (v < 0 || v >= V)  throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {
        private const val INFINITY = Integer.MAX_VALUE

        /**
         * Unit tests the `BreadthFirstPaths` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Graph(`in`)
            val s = Integer.parseInt(args[1])
            val bfs = BreadthFirstPaths(G, s)

            for (v in 0 until G.V) {
                if (bfs.hasPathTo(v)) {
                    StdOut.println("$s to $v (${bfs.distTo(v)}):  ")
                    for (x in bfs.pathTo(v)!!) {
                        if (x == s)
                            StdOut.print(x)
                        else
                            StdOut.print("-$x")
                    }
                    StdOut.println()
                } else {
                    StdOut.println("$s to $v (-):  not connected.")
                }

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
