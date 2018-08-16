/******************************************************************************
 * Compilation:  javac DirectedDFS.java
 * Execution:    java DirectedDFS digraph.txt s
 * Dependencies: Digraph.kt Bag.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 * https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 * https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 * Determine single-source or multiple-source reachability in a digraph
 * using depth first search.
 * Runs in O(E + V) time.
 *
 * % java DirectedDFS tinyDG.txt 1
 * 1
 *
 * % java DirectedDFS tinyDG.txt 2
 * 0 1 2 3 4 5
 *
 * % java DirectedDFS tinyDG.txt 1 2 6
 * 0 1 2 3 4 5 6 8 9 10 11 12
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DirectedDFS` class represents a data type for
 * determining the vertices reachable from a given source vertex *s*
 * (or set of source vertices) in a digraph. For versions that find the paths,
 * see [DepthFirstDirectedPaths] and [BreadthFirstDirectedPaths].
 *
 *
 * This implementation uses depth-first search.
 * The constructor takes time proportional to *V* + *E*
 * (in the worst case),
 * where *V* is the number of vertices and *E* is the number of edges.
 *
 *
 * For additional documentation,
 * see [Section 4.2](https://algs4.cs.princeton.edu/42digraph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 */
class DirectedDFS {
    private var marked: BooleanArray  // marked[v] = true if v is reachable
    var count: Int = 0         // number of vertices reachable from s
        private set

    /**
     * Computes the vertices in digraph `G` that are
     * reachable from the source vertex `s`.
     * @param G the digraph
     * @param s the source vertex
     * @throws IllegalArgumentException unless `0 <= s < V`
     */
    constructor(G: Digraph, s: Int) {
        marked = BooleanArray(G.V)
        validateVertex(s)
        dfs(G, s)
    }

    /**
     * Computes the vertices in digraph `G` that are
     * connected to any of the source vertices `sources`.
     * @param G the graph
     * @param sources the source vertices
     * @throws IllegalArgumentException unless `0 <= s < V`
     * for each vertex `s` in `sources`
     */
    constructor(G: Digraph, sources: Iterable<Int>) {
        marked = BooleanArray(G.V)
        validateVertices(sources)
        for (v in sources)
            if (!marked[v]) dfs(G, v)
    }

    private fun dfs(G: Digraph, v: Int) {
        count++
        marked[v] = true
        for (w in G.adj(v))
            if (!marked[w]) dfs(G, w)
    }

    /**
     * Is there a directed path from the source vertex (or any
     * of the source vertices) and vertex `v`?
     * @param  v the vertex
     * @return `true` if there is a directed path, `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun marked(v: Int): Boolean {
        validateVertex(v)
        return marked[v]
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
        /**
         * Unit tests the `DirectedDFS` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // read in digraph from command-line argument
            val `in` = In(args[0])
            val G = Digraph(`in`)

            // read in sources from command-line arguments
            val sources = nnBag<Int>()
            for (i in 1 until args.size) {
                val s = Integer.parseInt(args[i])
                sources.add(s)
            }

            // multiple-source reachability
            val dfs = DirectedDFS(G, sources)

            // print out vertices reachable from sources
            for (v in 0 until G.V)
                if (dfs.marked(v)) StdOut.print("$v ")
            StdOut.println()
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