/******************************************************************************
 * Compilation:  javac DepthFirstDirectedPaths.java
 * Execution:    java DepthFirstDirectedPaths digraph.txt s
 * Dependencies: Digraph.kt Stack.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 * https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 * https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 * Determine reachability in a digraph from a given vertex using
 * depth-first search.
 * Runs in O(E + V) time.
 *
 * % java DepthFirstDirectedPaths tinyDG.txt 3
 * 3 to 0:  3-5-4-2-0
 * 3 to 1:  3-5-4-2-0-1
 * 3 to 2:  3-5-4-2
 * 3 to 3:  3
 * 3 to 4:  3-5-4
 * 3 to 5:  3-5
 * 3 to 6:  not connected
 * 3 to 7:  not connected
 * 3 to 8:  not connected
 * 3 to 9:  not connected
 * 3 to 10:  not connected
 * 3 to 11:  not connected
 * 3 to 12:  not connected
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DepthFirstDirectedPaths` class represents a data type for finding
 * directed paths from a source vertex *s* to every
 * other vertex in the digraph.
 *
 *
 * This implementation uses depth-first search.
 * The constructor takes time proportional to *V* + *E*,
 * where *V* is the number of vertices and *E* is the number of edges.
 * Each call to [.hasPathTo] takes constant time;
 * each call to [.pathTo] takes time proportional to the length
 * of the path returned.
 * It uses extra space (not including the graph) proportional to *V*.
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
class DepthFirstDirectedPaths
/**
 * Computes a directed path from `s` to every other vertex in digraph `G`.
 * @param  G the digraph
 * @param  s the source vertex
 * @throws IllegalArgumentException unless `0 <= s < V`
 */
(G: Digraph, private val s: Int       // source vertex
) {
    private val marked: BooleanArray = BooleanArray(G.V)  // marked[v] = true if v is reachable from s
    private val edgeTo: IntArray = IntArray(G.V)      // edgeTo[v] = last edge on path from s to v

    init {
        validateVertex(s)
        dfs(G, s)
    }

    private fun dfs(G: Digraph, v: Int) {
        marked[v] = true
        for (w in G.adj(v))
            if (!marked[w]) {
                edgeTo[w] = v
                dfs(G, w)
            }
    }

    /**
     * Is there a directed path from the source vertex `s` to vertex `v`?
     * @param  v the vertex
     * @return `true` if there is a directed path from the source
     * vertex `s` to vertex `v`, `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun hasPathTo(v: Int): Boolean {
        validateVertex(v)
        return marked[v]
    }

    /**
     * Returns a directed path from the source vertex `s` to vertex `v`, or
     * `null` if no such path.
     * @param  v the vertex
     * @return the sequence of vertices on a directed path from the source vertex
     * `s` to vertex `v`, as an Iterable
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun pathTo(v: Int): Iterable<Int>? {
        validateVertex(v)
        if (!hasPathTo(v)) return null
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
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {
        /**
         * Unit tests the `DepthFirstDirectedPaths` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Digraph(`in`)

            val s = Integer.parseInt(args[1])
            val dfs = DepthFirstDirectedPaths(G, s)

            for (v in 0 until G.V)
                if (dfs.hasPathTo(v)) {
                    StdOut.print("$s to $v:  ")
                    for (x in dfs.pathTo(v)!!)
                        if (x == s)
                            StdOut.print(x)
                        else
                            StdOut.print("-$x")
                    StdOut.println()
                } else
                    StdOut.println("$s to $v:  not connected")
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