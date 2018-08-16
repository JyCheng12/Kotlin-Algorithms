/******************************************************************************
 * Compilation:  javac NonrecursiveDFS.java
 * Execution:    java NonrecursiveDFS graph.txt s
 * Dependencies: Graph.kt Queue.kt Stack.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/41graph/tinyCG.txt
 * https://algs4.cs.princeton.edu/41graph/tinyG.txt
 * https://algs4.cs.princeton.edu/41graph/mediumG.txt
 *
 * Run nonrecurisve depth-first search on an undirected graph.
 * Runs in O(E + V) time.
 *
 * Explores the vertices in exactly the same order as DepthFirstSearch.kt.
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
 * %  java NonrecursiveDFS tinyCG.txt 0
 * 0 to 0 (0):  0
 * 0 to 1 (1):  0-1
 * 0 to 2 (1):  0-2
 * 0 to 3 (2):  0-2-3
 * 0 to 4 (2):  0-2-4
 * 0 to 5 (1):  0-5
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `NonrecursiveDFS` class represents a data type for finding
 * the vertices connected to a source vertex *s* in the undirected
 * graph.
 *
 *
 * This implementation uses a nonrecursive version of depth-first search
 * with an explicit stack.
 * The constructor takes time proportional to *V* + *E*,
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
class NonrecursiveDFS
/**
 * Computes the vertices connected to the source vertex `s` in the graph `G`.
 * @param G the graph
 * @param s the source vertex
 * @throws IllegalArgumentException unless `0 <= s < V`
 */
(G: Graph, s: Int) {
    private val marked: BooleanArray = BooleanArray(G.V)  // marked[v] = is there an s-v path?

    init {
        validateVertex(s)

        // to be able to iterate over each adjacency list, keeping track of which
        // vertex in each adjacency list needs to be explored next
        val adj = Array(G.V){ G.adj(it).iterator() }

        // depth-first search using an explicit stack
        val stack = nnStack<Int>()
        marked[s] = true
        stack.push(s)
        while (!stack.isEmpty) {
            val v = stack.peek()
            if (adj[v].hasNext()) {
                val w = adj[v].next()
                if (!marked[w]) {
                    // discovered vertex w for the first time
                    marked[w] = true
                    stack.push(w)
                }
            } else
                stack.pop()
        }
    }

    /**
     * Is vertex `v` connected to the source vertex `s`?
     * @param v the vertex
     * @return `true` if vertex `v` is connected to the source vertex `s`,
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
         * Unit tests the `NonrecursiveDFS` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Graph(`in`)
            val s = Integer.parseInt(args[1])
            val dfs = NonrecursiveDFS(G, s)
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