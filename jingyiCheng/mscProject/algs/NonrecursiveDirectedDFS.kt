/******************************************************************************
 * Compilation:  javac NonrecursiveDirectedDFS.java
 * Execution:    java NonrecursiveDirectedDFS digraph.txt s
 * Dependencies: Digraph.kt Queue.kt Stack.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 * https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 * https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 * Run nonrecurisve depth-first search on an directed graph.
 * Runs in O(E + V) time.
 *
 * Explores the vertices in exactly the same order as DirectedDFS.kt.
 *
 *
 * % java NonrecursiveDirectedDFS tinyDG.txt 1
 * 1
 *
 * % java NonrecursiveDirectedDFS tinyDG.txt 2
 * 0 1 2 3 4 5
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `NonrecursiveDirectedDFS` class represents a data type for finding
 * the vertices reachable from a source vertex *s* in the digraph.
 *
 *
 * This implementation uses a nonrecursive version of depth-first search
 * with an explicit stack.
 * The constructor takes time proportional to *V* + *E*,
 * where *V* is the number of vertices and *E* is the number of edges.
 * It uses extra space (not including the digraph) proportional to *V*.
 *
 *
 * For additional documentation,
 * see [Section 4.2](https://algs4.cs.princeton.edu/42digraph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class NonrecursiveDirectedDFS
/**
 * Computes the vertices reachable from the source vertex `s` in the digraph `G`.
 * @param  G the digraph
 * @param  s the source vertex
 * @throws IllegalArgumentException unless `0 <= s < V`
 */
(G: Digraph, s: Int) {
    private val marked: BooleanArray = BooleanArray(G.V)  // marked[v] = is there an s->v path?

    init {
        validateVertex(s)

        // to be able to iterate over each adjacency list, keeping track of which
        // vertex in each adjacency list needs to be explored next
        val adj = Array(G.V){G.adj(it).iterator()}

        // depth-first search using an explicit stack
        val stack = nnStack<Int>()
        marked[s] = true
        stack.push(s)
        while (!stack.isEmpty) {
            val v = stack.peek()
            if (adj[v].hasNext()) {
                val w = adj[v].next()
                if (!marked[w]) {
                    marked[w] = true
                    stack.push(w)
                }
            } else
                stack.pop()
        }
    }

    /**
     * Is vertex `v` reachable from the source vertex `s`?
     * @param  v the vertex
     * @return `true` if vertex `v` is reachable from the source vertex `s`,
     * and `false` otherwise
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

    companion object {
        /**
         * Unit tests the `NonrecursiveDirectedDFS` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Digraph(`in`)
            val s = Integer.parseInt(args[1])
            val dfs = NonrecursiveDirectedDFS(G, s)
            for (v in 0 until G.V)
                if (dfs.marked(v))
                    StdOut.print("$v ")
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