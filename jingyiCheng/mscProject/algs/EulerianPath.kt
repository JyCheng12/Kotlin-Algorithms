/******************************************************************************
 * Compilation:  javac EulerianPath.java
 * Execution:    java EulerianPath V E
 * Dependencies: Graph.kt Stack.kt StdOut.kt
 *
 * Find an Eulerian path in a graph, if one exists.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `EulerianPath` class represents a data type
 * for finding an Eulerian path in a graph.
 * An *Eulerian path* is a path (not necessarily simple) that
 * uses every edge in the graph exactly once.
 *
 *
 * This implementation uses a nonrecursive depth-first search.
 * The constructor runs in O(*E* + *V*) time,
 * and uses O(*E* + *V*) extra space,
 * where *E* is the number of edges and *V* the number of vertices
 * All other methods take O(1) time.
 *
 *
 * To compute Eulerian cycles in graphs, see [EulerianCycle].
 * To compute Eulerian cycles and paths in digraphs, see
 * [DirectedEulerianCycle] and [DirectedEulerianPath].
 *
 *
 * For additional documentation,
 * see [Section 4.1](https://algs4.cs.princeton.edu/41graph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Nate Liu
 * @author Jingyi Cheng
 *
 */
class EulerianPath
/**
 * Computes an Eulerian path in the specified graph, if one exists.
 *
 * @param G the graph
 */
(G: Graph) {
    private var path: nnStack<Int>? = null   // Eulerian path; null if no suh path

    // an undirected edge, with a field to indicate whether the edge has already been used
    internal class Edge(private val v: Int, private val w: Int) {
        var isUsed: Boolean = false

        // returns the other vertex of the edge
        fun other(vertex: Int): Int {
            return when (vertex) {
                v -> w
                w -> v
                else -> throw IllegalArgumentException("Illegal endpoint")
            }
        }
    }

    init {
        // find vertex from which to start potential Eulerian path:
        // a vertex v with odd degree(v) if it exits;
        // otherwise a vertex with degree(v) > 0
        var oddDegreeVertices = 0
        var s = nonIsolatedVertex(G)
        for (v in 0 until G.V)
            if (G.degree(v) % 2 != 0) {
                oddDegreeVertices++
                s = v
            }

        run{
            // graph can't have an Eulerian path
            // (this condition is needed for correctness)
            if (oddDegreeVertices > 2) return@run

            // special case for graph with zero edges (has a degenerate Eulerian path)
            if (s == -1) s = 0

            // create local view of adjacency lists, to iterate one vertex at a time
            // the helper Edge data type is used to avoid exploring both copies of an edge v-w
            val adj = Array(G.V) {nnQueue<Edge>()}

            for (v in 0 until G.V) {
                var selfLoops = 0
                for (w in G.adj(v))
                    // careful with self loops
                    if (v == w) {
                        if (selfLoops % 2 == 0) {
                            val e = Edge(v, w)
                            adj[v].enqueue(e)
                            adj[w].enqueue(e)
                        }
                        selfLoops++
                    } else if (v < w) {
                        val e = Edge(v, w)
                        adj[v].enqueue(e)
                        adj[w].enqueue(e)
                    }
            }

            // initialize stack with any non-isolated vertex
            val stack = nnStack<Int>()
            stack.push(s)

            // greedily search through edges in iterative DFS style
            val path = nnStack<Int>()
            while (!stack.isEmpty) {
                var v = stack.pop()
                while (!adj[v].isEmpty) {
                    val edge = adj[v].dequeue()
                    if (edge.isUsed) continue
                    edge.isUsed = true
                    stack.push(v)
                    v = edge.other(v)
                }
                // push vertex with no more leaving edges to path
                path.push(v)
            }

            // check if all edges are used
            if (path.size != G.E + 1)
                this.path = null
            else this.path = path
            assert(certifySolution(G))
        }

    }

    /**
     * Returns true if the graph has an Eulerian path.
     *
     * @return `true` if the graph has an Eulerian path;
     * `false` otherwise
     */
    fun hasEulerianPath() = path != null

    // check that solution is correct
    private fun certifySolution(G: Graph): Boolean {

        // internal consistency check
        if (hasEulerianPath() == (path == null)) return false

        // hashEulerianPath() returns correct value
        if (hasEulerianPath() != satisfiesNecessaryAndSufficientConditions(G)) return false

        // nothing else to check if no Eulerian path
        //if (path == null) return true
        val path = path ?: return true

        // check that path() uses correct number of edges
        return path.size == G.E + 1

        // check that path() is a path in G
        // TODO

    }

    companion object {
        // returns any non-isolated vertex; -1 if no such vertex
        private fun nonIsolatedVertex(G: Graph): Int {
            for (v in 0 until G.V)
                if (G.degree(v) > 0)
                    return v
            return -1
        }

        // Determines whether a graph has an Eulerian path using necessary
        // and sufficient conditions (without computing the path itself):
        //    - degree(v) is even for every vertex, except for possibly two
        //    - the graph is connected (ignoring isolated vertices)
        // This method is solely for unit testing.
        private fun satisfiesNecessaryAndSufficientConditions(G: Graph): Boolean {
            if (G.E == 0) return true

            // Condition 1: degree(v) is even except for possibly two
            var oddDegreeVertices = 0
            for (v in 0 until G.V)
                if (G.degree(v) % 2 != 0)
                    oddDegreeVertices++
            if (oddDegreeVertices > 2) return false

            // Condition 2: graph is connected, ignoring isolated vertices
            val s = nonIsolatedVertex(G)
            val bfs = BreadthFirstPaths(G, s)
            for (v in 0 until G.V)
                if (G.degree(v) > 0 && !bfs.hasPathTo(v))
                    return false
            return true
        }

        private fun unitTest(G: Graph, description: String) {
            StdOut.println(description)
            StdOut.println("-------------------------------------")
            StdOut.print(G)
            val euler = EulerianPath(G)

            StdOut.print("Eulerian path:  ")
            if (euler.hasEulerianPath()) {
                for (v in euler.path!!)
                    StdOut.print("$v ")
                StdOut.println()
            } else
                StdOut.println("none")
            StdOut.println()
        }

        /**
         * Unit tests the `EulerianPath` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val V = Integer.parseInt(args[0])
            val E = Integer.parseInt(args[1])

            // Eulerian cycle
            val G1 = GraphGenerator.eulerianCycle(V, E)
            unitTest(G1, "Eulerian cycle")

            // Eulerian path
            val G2 = GraphGenerator.eulerianPath(V, E)
            unitTest(G2, "Eulerian path")

            // add one random edge
            val G3 = Graph(G2)
            G3.addEdge(StdRandom.uniform(V), StdRandom.uniform(V))
            unitTest(G3, "one random edge added to Eulerian path")

            // self loop
            val G4 = Graph(V)
            val v4 = StdRandom.uniform(V)
            G4.addEdge(v4, v4)
            unitTest(G4, "single self loop")

            // single edge
            val G5 = Graph(V)
            G5.addEdge(StdRandom.uniform(V), StdRandom.uniform(V))
            unitTest(G5, "single edge")

            // empty graph
            val G6 = Graph(V)
            unitTest(G6, "empty graph")

            // random graph
            val G7 = GraphGenerator.simple(V, E)
            unitTest(G7, "simple graph")
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
