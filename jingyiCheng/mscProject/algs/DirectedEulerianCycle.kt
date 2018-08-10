/******************************************************************************
 * Compilation:  javac DirectedEulerianCycle.kt
 * Execution:    java DirectedEulerianCycle V E
 * Dependencies: Digraph.kt Stack.kt StdOut.kt
 * BreadthFirstPaths.kt
 * DigraphGenerator.kt StdRandom.kt
 *
 * Find an Eulerian cycle in a digraph, if one exists.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DirectedEulerianCycle` class represents a data type
 * for finding an Eulerian cycle or path in a digraph.
 * An *Eulerian cycle* is a cycle (not necessarily simple) that
 * uses every edge in the digraph exactly once.
 *
 *
 * This implementation uses a nonrecursive depth-first search.
 * The constructor runs in O(<Em>E</Em> + *V*) time,
 * and uses O(*V*) extra space, where *E* is the
 * number of edges and *V* the number of vertices
 * All other methods take O(1) time.
 *
 *
 * To compute Eulerian paths in digraphs, see [DirectedEulerianPath].
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
 *
 */
class DirectedEulerianCycle
/**
 * Computes an Eulerian cycle in the specified digraph, if one exists.
 *
 * @param G the digraph
 */
(G: Digraph) {
    var cycle: nnStack<Int>? = null  // Eulerian cycle; null if no such cylce
        private set

    init {
        run {
            // must have at least one edge
            if (G.E == 0) return@run

            // necessary condition: indegree(v) = outdegree(v) for each vertex v
            // (without this check, DFS might return a path instead of a cycle)
            for (v in 0 until G.V)
                if (G.outdegree(v) != G.indegree(v))
                    return@run

            // create local view of adjacency lists, to iterate one vertex at a time
            val adj = Array(G.V) { G.adj(it).iterator() } //as Array<Iterator<Int>>

            // initialize stack with any non-isolated vertex
            val s = nonIsolatedVertex(G)
            val stack = nnStack<Int>()
            stack.push(s)

            // greedily add to putative cycle, depth-first search style
            val cycle = nnStack<Int>()
            while (!stack.isEmpty) {
                var v = stack.pop()
                while (adj[v].hasNext()) {
                    stack.push(v)
                    v = adj[v].next()
                }
                // add vertex with no more leaving edges to cycle
                cycle.push(v)
            }

            // check if all edges have been used
            // (in case there are two or more vertex-disjoint Eulerian cycles)
            if (cycle.size != G.E + 1)
                this.cycle = null
            else this.cycle = cycle
            assert(certifySolution(G))
        }
    }

    /**
     * Returns true if the digraph has an Eulerian cycle.
     *
     * @return `true` if the digraph has an Eulerian cycle;
     * `false` otherwise
     */
    fun hasEulerianCycle() = cycle != null

    // check that solution is correct
    private fun certifySolution(G: Digraph): Boolean {
        // internal consistency check
        if (hasEulerianCycle() == (cycle == null)) return false

        // hashEulerianCycle() returns correct value
        if (hasEulerianCycle() != satisfiesNecessaryAndSufficientConditions(G)) return false

        // nothing else to check if no Eulerian cycle
        if (cycle == null) return true

        // check that cycle() uses correct number of edges
        return cycle!!.size == G.E + 1

        // check that cycle() is a directed cycle of G
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

        // Determines whether a digraph has an Eulerian cycle using necessary
        // and sufficient conditions (without computing the cycle itself):
        //    - at least one edge
        //    - indegree(v) = outdegree(v) for every vertex
        //    - the graph is connected, when viewed as an undirected graph
        //      (ignoring isolated vertices)
        private fun satisfiesNecessaryAndSufficientConditions(G: Digraph): Boolean {
            // Condition 0: at least 1 edge
            if (G.E == 0) return false

            // Condition 1: indegree(v) == outdegree(v) for every vertex
            for (v in 0 until G.V)
                if (G.outdegree(v) != G.indegree(v))
                    return false

            // Condition 2: graph is connected, ignoring isolated vertices
            val H = Graph(G.V)
            for (v in 0 until G.V)
                for (w in G.adj(v))
                    H.addEdge(v, w)

            // check that all non-isolated vertices are conneted
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

            val euler = DirectedEulerianCycle(G)
            StdOut.print("Eulerian cycle: ")
            if (euler.hasEulerianCycle()) {
                for (v in euler.cycle!!) {
                    StdOut.print("$v ")
                }
                StdOut.println()
            } else {
                StdOut.println("none")
            }
            StdOut.println()
        }

        /**
         * Unit tests the `DirectedEulerianCycle` data type.
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

            // empty digraph
            val G3 = Digraph(V)
            unitTest(G3, "empty digraph")

            // self loop
            val G4 = Digraph(V)
            val v4 = StdRandom.uniform(V)
            G4.addEdge(v4, v4)
            unitTest(G4, "single self loop")

            // union of two disjoint cycles
            val H1 = DigraphGenerator.eulerianCycle(V / 2, E / 2)
            val H2 = DigraphGenerator.eulerianCycle(V - V / 2, E - E / 2)
            val perm = IntArray(V) { it }
            StdRandom.shuffle(perm)
            val G5 = Digraph(V)
            for (v in 0 until H1.V)
                for (w in H1.adj(v))
                    G5.addEdge(perm[v], perm[w])
            for (v in 0 until H2.V)
                for (w in H2.adj(v))
                    G5.addEdge(perm[V / 2 + v], perm[V / 2 + w])
            unitTest(G5, "Union of two disjoint cycles")

            // random digraph
            val G6 = DigraphGenerator.simple(V, E)
            unitTest(G6, "simple digraph")

            // 4-vertex digraph
            val G7 = Digraph(In("eulerianD.txt"))
            unitTest(G7, "4-vertex Eulerian digraph")
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
