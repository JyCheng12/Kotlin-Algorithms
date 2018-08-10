/******************************************************************************
 * Compilation:  javac DepthFirstPaths.java
 * Execution:    java DepthFirstPaths G s
 * Dependencies: Graph.kt Stack.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/41graph/tinyCG.txt
 * https://algs4.cs.princeton.edu/41graph/tinyG.txt
 * https://algs4.cs.princeton.edu/41graph/mediumG.txt
 * https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 * Run depth first search on an undirected graph.
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
 * % java DepthFirstPaths tinyCG.txt 0
 * 0 to 0:  0
 * 0 to 1:  0-2-1
 * 0 to 2:  0-2
 * 0 to 3:  0-2-3
 * 0 to 4:  0-2-3-4
 * 0 to 5:  0-2-3-5
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DepthFirstPaths` class represents a data type for finding
 * paths from a source vertex *s* to every other vertex
 * in an undirected graph.
 *
 *
 * This implementation uses depth-first search.
 * The constructor takes time proportional to *V* + *E*,
 * where *V* is the number of vertices and *E* is the number of edges.
 * Each call to [.hasPathTo] takes constant time;
 * each call to [.pathTo] takes time proportional to the length
 * of the path.
 * It uses extra space (not including the graph) proportional to *V*.
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
class DepthFirstPaths
/**
 * Computes a path between `s` and every other vertex in graph `G`.
 * @param G the graph
 * @param s the source vertex
 * @throws IllegalArgumentException unless `0 <= s < V`
 */
(G: Graph, private val s: Int         // source vertex
) {
    private val marked: BooleanArray = BooleanArray(G.V)    // marked[v] = is there an s-v path?
    private val edgeTo: IntArray = IntArray(G.V)        // edgeTo[v] = last edge on s-v path

    init {
        validateVertex(s)
        dfs(G, s)
    }

    // depth first search from v
    private fun dfs(G: Graph, v: Int) {
        marked[v] = true
        for (w in G.adj(v))
            if (!marked[w]) {
                edgeTo[w] = v
                dfs(G, w)
            }
    }

    /**
     * Is there a path between the source vertex `s` and vertex `v`?
     * @param v the vertex
     * @return `true` if there is a path, `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun hasPathTo(v: Int): Boolean {
        validateVertex(v)
        return marked[v]
    }

    /**
     * Returns a path between the source vertex `s` and vertex `v`, or
     * `null` if no such path.
     * @param  v the vertex
     * @return the sequence of vertices on a path between the source vertex
     * `s` and vertex `v`, as an Iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun pathTo(v: Int): Iterable<Int> {
        validateVertex(v)
        val path = nnStack<Int>()
        var x = v
        while (x != s) {
            path.push(x)
            x = edgeTo[x]
        }
        path.push(s)
        return path
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = marked.size
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V-1}")
    }

    companion object {
        /**
         * Unit tests the `DepthFirstPaths` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Graph(`in`)
            val s = Integer.parseInt(args[1])
            val dfs = DepthFirstPaths(G, s)

            for (v in 0 until G.V)
                if (dfs.hasPathTo(v)) {
                    StdOut.printf("%d to %d:  ", s, v)
                    for (x in dfs.pathTo(v))
                        if (x == s)
                            StdOut.print(x)
                        else
                            StdOut.print("-$x")
                    StdOut.println()
                } else
                    StdOut.printf("%d to %d:  not connected\n", s, v)
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
