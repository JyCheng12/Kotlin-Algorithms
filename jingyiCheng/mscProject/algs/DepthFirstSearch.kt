/******************************************************************************
 * Compilation:  javac DepthFirstSearch.java
 * Execution:    java DepthFirstSearch filename.txt s
 * Dependencies: Graph.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/41graph/tinyG.txt
 * https://algs4.cs.princeton.edu/41graph/mediumG.txt
 *
 * Run depth first search on an undirected graph.
 * Runs in O(E + V) time.
 *
 * % java DepthFirstSearch tinyG.txt 0
 * 0 1 2 3 4 5 6
 * NOT connected
 *
 * % java DepthFirstSearch tinyG.txt 9
 * 9 10 11 12
 * NOT connected
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DepthFirstSearch` class represents a data type for
 * determining the vertices connected to a given source vertex *s*
 * in an undirected graph. For versions that find the paths, see
 * [DepthFirstPaths] and [BreadthFirstPaths].
 *
 *
 * This implementation uses depth-first search.
 * The constructor takes time proportional to *V* + *E*
 * (in the worst case),
 * where *V* is the number of vertices and *E* is the number of edges.
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
class DepthFirstSearch
/**
 * Computes the vertices in graph `G` that are
 * connected to the source vertex `s`.
 * @param G the graph
 * @param s the source vertex
 * @throws IllegalArgumentException unless `0 <= s < V`
 */
(G: Graph, s: Int) {
    private val marked: BooleanArray = BooleanArray(G.V)    // marked[v] = is there an s-v path?
    var count: Int = 0           // number of vertices connected to s
        private set

    init {
        validateVertex(s)
        dfs(G, s)
    }

    // depth first search from v
    private fun dfs(G: Graph, v: Int) {
        count++
        marked[v] = true
        for (w in G.adj(v))
            if (!marked[w])
                dfs(G, w)
    }

    /**
     * Is there a path between the source vertex `s` and vertex `v`?
     * @param v the vertex
     * @return `true` if there is a path, `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun marked(v: Int): Boolean {
        validateVertex(v)
        return marked[v]
    }

    /**
     * Returns the number of vertices connected to the source vertex `s`.
     * @return the number of vertices connected to the source vertex `s`
     */
    /*fun count(): Int {
        return count
    }*/

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = marked.size
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V-1}")
    }

    companion object {

        /**
         * Unit tests the `DepthFirstSearch` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Graph(`in`)
            val s = Integer.parseInt(args[1])
            val search = DepthFirstSearch(G, s)
            for (v in 0 until G.V)
                if (search.marked(v))
                    StdOut.print("$v ")

            StdOut.println()
            if (search.count != G.V)
                StdOut.println("NOT connected")
            else
                StdOut.println("connected")
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
