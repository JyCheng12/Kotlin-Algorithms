/******************************************************************************
 * Compilation:  javac BipartiteX.java
 * Execution:    java  Bipartite V E F
 * Dependencies: Graph.kt
 *
 * Given a graph, find either (i) a bipartition or (ii) an odd-length cycle.
 * Runs in O(E + V) time.
 *
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `BipartiteX` class represents a data type for
 * determining whether an undirected graph is bipartite or whether
 * it has an odd-length cycle.
 * The *isBipartite* operation determines whether the graph is
 * bipartite. If so, the *color* operation determines a
 * bipartition; if not, the *oddCycle* operation determines a
 * cycle with an odd number of edges.
 *
 *
 * This implementation uses breadth-first search and is nonrecursive.
 * The constructor takes time proportional to *V* + *E*
 * (in the worst case),
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the *isBipartite* and *color* operations
 * take constant time; the *oddCycle* operation takes time proportional
 * to the length of the cycle.
 * See [Bipartite] for a recursive version that uses depth-first search.
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
class BipartiteX
/**
 * Determines whether an undirected graph is bipartite and finds either a
 * bipartition or an odd-length cycle.
 *
 * @param  G the graph
 */
(G: Graph) {
    /**
     * Returns true if the graph is bipartite.
     *
     * @return `true` if the graph is bipartite; `false` otherwise
     */
    var isBipartite: Boolean = true
        private set   // is the graph bipartite?
    private val color: BooleanArray = BooleanArray(G.V)       // color[v] gives vertices on one side of bipartition
    private val marked: BooleanArray = BooleanArray(G.V)      // marked[v] = true if v has been visited in DFS
    private val edgeTo: IntArray = IntArray(G.V)          // edgeTo[v] = last edge on path to v
    var cycle: nnQueue<Int>? = null  // odd-length cycle
        private set

    init {
        var v = 0
        while (v < G.V && isBipartite) {
            if (!marked[v])
                bfs(G, v)
            v++
        }
        assert(check(G))
    }

    private fun bfs(G: Graph, s: Int) {
        val q = nnQueue<Int>()
        color[s] = WHITE
        marked[s] = true
        q.enqueue(s)

        while (!q.isEmpty) {
            val v = q.dequeue()
            for (w in G.adj(v))
                if (!marked[w]) {
                    marked[w] = true
                    edgeTo[w] = v
                    color[w] = !color[v]
                    q.enqueue(w)
                } else if (color[w] == color[v]) {
                    isBipartite = false

                    // to form odd cycle, consider s-v path and s-w path
                    // and let x be closest node to v and w common to two paths
                    // then (w-x path) + (x-v path) + (edge v-w) is an odd-length cycle
                    // Note: distTo[v] == distTo[w];
                    cycle = nnQueue()
                    val stack = Stack<Int>()
                    var x = v
                    var y = w
                    while (x != y) {
                        stack.push(x)
                        cycle!!.enqueue(y)
                        x = edgeTo[x]
                        y = edgeTo[y]
                    }
                    stack.push(x)
                    while (!stack.isEmpty)
                        cycle!!.enqueue(stack.pop())
                    cycle!!.enqueue(w)
                    return
                }
        }
    }

    /**
     * Returns the side of the bipartite that vertex `v` is on.
     *
     * @param  v the vertex
     * @return the side of the bipartition that vertex `v` is on; two vertices
     * are in the same side of the bipartition if and only if they have the
     * same color
     * @throws IllegalArgumentException unless `0 <= v < V`
     * @throws UnsupportedOperationException if this method is called when the graph
     * is not bipartite
     */
    fun color(v: Int): Boolean {
        validateVertex(v)
        if (!isBipartite) throw UnsupportedOperationException("Graph is not bipartite")
        return color[v]
    }

    private fun check(G: Graph): Boolean {
        // graph is bipartite
        if (isBipartite)
            for (v in 0 until G.V)
                for (w in G.adj(v))
                    if (color[v] == color[w]) {
                        System.err.printf("edge %d-%d with %d and %d in same side of bipartition\n", v, w, v, w)
                        return false
                    }
        else {
            // verify cycle
            var first: Int? = null
            var last: Int? = null
            for (v in cycle!!) {
                if (first == null) first = v
                last = v
            }
            if (first != last) {
                System.err.println("cycle begins with $first and ends with $last.")
                return false
            }
        }// graph has an odd-length cycle
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = marked.size
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {
        private const val WHITE = false
        private const val BLACK = true

        /**
         * Unit tests the `BipartiteX` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val V1 = Integer.parseInt(args[0])
            val V2 = Integer.parseInt(args[1])
            val E = Integer.parseInt(args[2])
            val F = Integer.parseInt(args[3])

            // create random bipartite graph with V1 vertices on left side,
            // V2 vertices on right side, and E edges; then add F random edges
            val G = GraphGenerator.bipartite(V1, V2, E)
            for (i in 0 until F)
                G.addEdge(StdRandom.uniform(V1 + V2), StdRandom.uniform(V1 + V2))
            StdOut.println(G)

            val b = BipartiteX(G)
            if (b.isBipartite) {
                StdOut.println("Graph is bipartite")
                for (v in 0 until G.V)
                    StdOut.println("$v: ${b.color(v)}")
            } else {
                StdOut.print("Graph has an odd-length cycle: ")
                for (x in b.cycle!!)
                    StdOut.print("$x ")
                StdOut.println()
            }
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