/******************************************************************************
 * Compilation:  javac BreadthFirstDirectedPaths.java
 * Execution:    java BreadthFirstDirectedPaths digraph.txt s
 * Dependencies: Digraph.kt Queue.kt Stack.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 * https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 * https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 * Run breadth-first search on a digraph.
 * Runs in O(E + V) time.
 *
 * % java BreadthFirstDirectedPaths tinyDG.txt 3
 * 3 to 0 (2):  3->2->0
 * 3 to 1 (3):  3->2->0->1
 * 3 to 2 (1):  3->2
 * 3 to 3 (0):  3
 * 3 to 4 (2):  3->5->4
 * 3 to 5 (1):  3->5
 * 3 to 6 (-):  not connected
 * 3 to 7 (-):  not connected
 * 3 to 8 (-):  not connected
 * 3 to 9 (-):  not connected
 * 3 to 10 (-):  not connected
 * 3 to 11 (-):  not connected
 * 3 to 12 (-):  not connected
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `BreadthDirectedFirstPaths` class represents a data type for finding
 * shortest paths (number of edges) from a source vertex *s*
 * (or set of source vertices) to every other vertex in the digraph.
 *
 *
 * This implementation uses breadth-first search.
 * The constructor takes time proportional to *V* + *E*,
 * where *V* is the number of vertices and *E* is the number of edges.
 * Each call to [.distTo] and [.hasPathTo] takes constant time;
 * each call to [.pathTo] takes time proportional to the length
 * of the path.
 * It uses extra space (not including the digraph) proportional to *V*.
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
class BreadthFirstDirectedPaths {
    private var marked: BooleanArray  // marked[v] = is there an s->v path?
    private var edgeTo: IntArray      // edgeTo[v] = last edge on shortest s->v path
    private var distTo: IntArray      // distTo[v] = length of shortest s->v path

    /**
     * Computes the shortest path from `s` and every other vertex in graph `G`.
     * @param G the digraph
     * @param s the source vertex
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    constructor(G: Digraph, s: Int) {
        marked = BooleanArray(G.V)
        distTo = IntArray(G.V){ INFINITY }
        edgeTo = IntArray(G.V)
        validateVertex(s)
        bfs(G, s)
    }

    /**
     * Computes the shortest path from any one of the source vertices in `sources`
     * to every other vertex in graph `G`.
     * @param G the digraph
     * @param sources the source vertices
     * @throws IllegalArgumentException unless each vertex `v` in
     * `sources` satisfies `0 <= v < V`
     */
    constructor(G: Digraph, sources: Iterable<Int>) {
        marked = BooleanArray(G.V)
        distTo = IntArray(G.V) { INFINITY}
        edgeTo = IntArray(G.V)
        validateVertices(sources)
        bfs(G, sources)
    }

    // BFS from single source
    private fun bfs(G: Digraph, s: Int) {
        val q = nnQueue<Int>()
        marked[s] = true
        distTo[s] = 0
        q.enqueue(s)
        while (!q.isEmpty) {
            val v = q.dequeue()
            for (w in G.adj(v))
                if (!marked[w]) {
                    edgeTo[w] = v
                    distTo[w] = distTo[v] + 1
                    marked[w] = true
                    q.enqueue(w)
                }
        }
    }

    // BFS from multiple sources
    private fun bfs(G: Digraph, sources: Iterable<Int>) {
        val q = nnQueue<Int>()
        for (s in sources) {
            marked[s] = true
            distTo[s] = 0
            q.enqueue(s)
        }
        while (!q.isEmpty) {
            val v = q.dequeue()
            for (w in G.adj(v))
                if (!marked[w]) {
                    edgeTo[w] = v
                    distTo[w] = distTo[v] + 1
                    marked[w] = true
                    q.enqueue(w)
                }
        }
    }

    /**
     * Is there a directed path from the source `s` (or sources) to vertex `v`?
     * @param v the vertex
     * @return `true` if there is a directed path, `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun hasPathTo(v: Int): Boolean {
        validateVertex(v)
        return marked[v]
    }

    /**
     * Returns the number of edges in a shortest path from the source `s`
     * (or sources) to vertex `v`?
     * @param v the vertex
     * @return the number of edges in a shortest path
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun distTo(v: Int): Int {
        validateVertex(v)
        return distTo[v]
    }

    /**
     * Returns a shortest path from `s` (or sources) to `v`, or
     * `null` if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a shortest path, as an Iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun pathTo(v: Int): Iterable<Int>? {
        validateVertex(v)
        if (!hasPathTo(v)) return null
        val path = nnStack<Int>()
        var x: Int
        x = v
        while (distTo[x] != 0) {
            path.push(x)
            x = edgeTo[x]
        }
        path.push(x)
        return path
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
            if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {
        private val INFINITY = Integer.MAX_VALUE

        /**
         * Unit tests the `BreadthFirstDirectedPaths` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Digraph(`in`)

            val s = Integer.parseInt(args[1])
            val bfs = BreadthFirstDirectedPaths(G, s)

            for (v in 0 until G.V)
                if (bfs.hasPathTo(v)) {
                    StdOut.print("$s to $v (${bfs.distTo(v)}):  ")
                    for (x in bfs.pathTo(v)!!) {
                        if (x == s) StdOut.print(x)
                        else StdOut.print("->$x")
                    }
                    StdOut.println()
                } else
                    StdOut.println("$s to $v (-):  not connected")
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