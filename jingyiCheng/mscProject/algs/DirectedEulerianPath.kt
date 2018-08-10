/******************************************************************************
 * Compilation:  javac DirectedEulerianPath.java
 * Execution:    java DirectedEulerianPath V E
 * Dependencies: Digraph.kt Stack.kt StdOut.kt
 * BreadthFirstPaths.kt
 * DigraphGenerator.kt StdRandom.kt
 *
 * Find an Eulerian path in a digraph, if one exists.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DirectedEulerianPath` class represents a data type
 * for finding an Eulerian path in a digraph.
 * An *Eulerian path* is a path (not necessarily simple) that
 * uses every edge in the digraph exactly once.
 *
 *
 * This implementation uses a nonrecursive depth-first search.
 * The constructor runs in O(E + V) time, and uses O(V) extra space,
 * where E is the number of edges and V the number of vertices
 * All other methods take O(1) time.
 *
 *
 * To compute Eulerian cycles in digraphs, see [DirectedEulerianCycle].
 * To compute Eulerian cycles and paths in undirected graphs, see
 * [EulerianCycle] and [EulerianPath].
 *
 *
 * For additional documentation,
 * see [Section 4.2](https://algs4.cs.princeton.edu/42digraph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Nate Liu
 * @author Jingyi Cheng
 */
class DirectedEulerianPath
/**
 * Computes an Eulerian path in the specified digraph, if one exists.
 *
 * @param G the digraph
 */
(G: Digraph) {
    var path: nnStack<Int>? = null   // Eulerian path; null if no suh path
        private set

    init {
        run {
            // find vertex from which to start potential Eulerian path:
            // a vertex v with outdegree(v) > indegree(v) if it exits;
            // otherwise a vertex with outdegree(v) > 0
            var deficit = 0
            var s = nonIsolatedVertex(G)
            for (v in 0 until G.V) {
                if (G.outdegree(v) > G.indegree(v)) {
                    deficit += G.outdegree(v) - G.indegree(v)
                    s = v
                }
            }

            // digraph can't have an Eulerian path
            // (this condition is needed)
            if (deficit > 1) return@run

            // special case for digraph with zero edges (has a degenerate Eulerian path)
            if (s == -1) s = 0

            // create local view of adjacency lists, to iterate one vertex at a time
            val adj = Array(G.V) { G.adj(it).iterator() }

            // greedily add to cycle, depth-first search style
            val stack = nnStack<Int>()
            stack.push(s)
            path = nnStack()
            while (!stack.isEmpty) {
                var v = stack.pop()
                while (adj[v].hasNext()) {
                    stack.push(v)
                    v = adj[v].next()
                }
                // push vertex with no more available edges to path
                path!!.push(v)
            }

            // check if all edges have been used
            if (path!!.size != G.E + 1)
                path = null

            assert(check(G))
        }
    }

    /**
     * Returns true if the digraph has an Eulerian path.
     *
     * @return `true` if the digraph has an Eulerian path;
     * `false` otherwise
     */
    fun hasEulerianPath() = path != null

    private fun check(G: Digraph): Boolean {
        // internal consistency check
        if (hasEulerianPath() == (path == null)) return false

        // hashEulerianPath() returns correct value
        if (hasEulerianPath() != satisfiesNecessaryAndSufficientConditions(G)) return false

        // nothing else to check if no Eulerian path
        if (path == null) return true

        // check that path() uses correct number of edges
        return path!!.size == G.E + 1

        // check that path() is a directed path in G
        // TODO
    }

    companion object {
        // returns any non-isolated vertex; -1 if no such vertex
        private fun nonIsolatedVertex(G: Digraph): Int {
            for (v in 0 until G.V)
                if (G.outdegree(v) > 0)
                    return v
            return -1
        }

        // Determines whether a digraph has an Eulerian path using necessary
        // and sufficient conditions (without computing the path itself):
        //    - indegree(v) = outdegree(v) for every vertex,
        //      except one vertex v may have outdegree(v) = indegree(v) + 1
        //      (and one vertex v may have indegree(v) = outdegree(v) + 1)
        //    - the graph is connected, when viewed as an undirected graph
        //      (ignoring isolated vertices)
        private fun satisfiesNecessaryAndSufficientConditions(G: Digraph): Boolean {
            if (G.E == 0) return true

            // Condition 1: indegree(v) == outdegree(v) for every vertex,
            // except one vertex may have outdegree(v) = indegree(v) + 1
            var deficit = 0
            for (v in 0 until G.V)
                if (G.outdegree(v) > G.indegree(v))
                    deficit += G.outdegree(v) - G.indegree(v)
            if (deficit > 1) return false

            // Condition 2: graph is connected, ignoring isolated vertices
            val H = Graph(G.V)
            for (v in 0 until G.V)
                for (w in G.adj(v))
                    H.addEdge(v, w)

            // check that all non-isolated vertices are connected
            val s = nonIsolatedVertex(G)
            val bfs = BreadthFirstPaths(H, s)
            for (v in 0 until G.V)
                if (H.degree(v) > 0 && !bfs.hasPathTo(v))
                    return false

            return true
        }


        private fun unitTest(G: Digraph, description: String) {
            StdOut.println(description)
            StdOut.println("-------------------------------------")
            StdOut.print(G)
            val euler = DirectedEulerianPath(G)

            StdOut.print("Eulerian path:  ")
            if (euler.hasEulerianPath()) {
                for (v in euler.path!!) {
                    StdOut.print(v.toString() + " ")
                }
                StdOut.println()
            } else {
                StdOut.println("none")
            }
            StdOut.println()
        }

        /**
         * Unit tests the `DirectedEulerianPath` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val V = Integer.parseInt(args[0])
            val E = Integer.parseInt(args[1])

            // Eulerian cycle
            val G1 = DigraphGenerator.eulerianCycle(V, E)
            unitTest(G1, "Eulerian cycle")

            // Eulerian path
            val G2 = DigraphGenerator.eulerianPath(V, E)
            unitTest(G2, "Eulerian path")

            // add one random edge
            val G3 = Digraph(G2)
            G3.addEdge(StdRandom.uniform(V), StdRandom.uniform(V))
            unitTest(G3, "one random edge added to Eulerian path")

            // self loop
            val G4 = Digraph(V)
            val v4 = StdRandom.uniform(V)
            G4.addEdge(v4, v4)
            unitTest(G4, "single self loop")

            // single edge
            val G5 = Digraph(V)
            G5.addEdge(StdRandom.uniform(V), StdRandom.uniform(V))
            unitTest(G5, "single edge")

            // empty digraph
            val G6 = Digraph(V)
            unitTest(G6, "empty digraph")

            // random digraph
            val G7 = DigraphGenerator.simple(V, E)
            unitTest(G7, "simple digraph")

            // 4-vertex digraph
            val G8 = Digraph(In("eulerianD.txt"))
            unitTest(G8, "4-vertex Eulerian digraph")
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
