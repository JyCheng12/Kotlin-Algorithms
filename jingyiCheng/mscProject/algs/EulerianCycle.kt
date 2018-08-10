/******************************************************************************
 * Compilation:  javac EulerianCycle.java
 * Execution:    java  EulerianCycle V E
 * Dependencies: Graph.kt Stack.kt StdOut.kt
 *
 * Find an Eulerian cycle in a graph, if one exists.
 *
 * Runs in O(E + V) time.
 *
 * This implementation is tricker than the one for digraphs because
 * when we use edge v-w from v's adjacency list, we must be careful
 * not to use the second copy of the edge from w's adjaceny list.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `EulerianCycle` class represents a data type
 * for finding an Eulerian cycle or path in a graph.
 * An *Eulerian cycle* is a cycle (not necessarily simple) that
 * uses every edge in the graph exactly once.
 *
 *
 * This implementation uses a nonrecursive depth-first search.
 * The constructor runs in O(<Em>E</Em> + *V*) time,
 * and uses O(*E* + *V*) extra space, where *E* is the
 * number of edges and *V* the number of vertices
 * All other methods take O(1) time.
 *
 *
 * To compute Eulerian paths in graphs, see [EulerianPath].
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
class EulerianCycle
/**
 * Computes an Eulerian cycle in the specified graph, if one exists.
 *
 * @param G the graph
 */
(G: Graph) {
    var cycle: nnStack<Int>? = nnStack()  // Eulerian cycle; null if no such cycle
        private set

    // an undirected edge, with a field to indicate whether the edge has already been used
    internal class Edge(private val v: Int, private val w: Int) {
        var isUsed: Boolean = false

        // returns the other vertex of the edge
        fun other(vertex: Int) = when (vertex) {
            v -> w
            w -> v
            else -> throw IllegalArgumentException("Illegal endpoint")
        }
    }

    init {
        run {
            // must have at least one edge
            if (G.E == 0) return@run

            // necessary condition: all vertices have even degree
            // (this test is needed or it might find an Eulerian path instead of cycle)
            for (v in 0 until G.V)
                if (G.degree(v) % 2 != 0)
                    return@run

            // create local view of adjacency lists, to iterate one vertex at a time
            // the helper Edge data type is used to avoid exploring both copies of an edge v-w
            val adj = Array(G.V) { nnQueue<Edge>() }

            for (v in 0 until G.V) {
                var selfLoops = 0
                for (w in G.adj(v))
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
            val s = nonIsolatedVertex(G)
            val stack = Stack<Int>()
            stack.push(s)

            // greedily search through edges in iterative DFS style
            val cycle = nnStack<Int>()
            while (!stack.isEmpty) {
                var v = stack.pop()!!
                while (!adj[v].isEmpty) {
                    val edge = adj[v].dequeue()
                    if (edge.isUsed) continue
                    edge.isUsed = true
                    stack.push(v)
                    v = edge.other(v)
                }
                // push vertex with no more leaving edges to cycle
                cycle.push(v)
            }

            // check if all edges are used
            if (cycle.size != G.E + 1)
                this.cycle = null
            else this.cycle = cycle
            assert(certifySolution(G))
        }
    }

    /**
     * Returns true if the graph has an Eulerian cycle.
     *
     * @return `true` if the graph has an Eulerian cycle;
     * `false` otherwise
     */
    fun hasEulerianCycle() = cycle != null

    // check that solution is correct
    private fun certifySolution(G: Graph): Boolean {

        // internal consistency check
        if (hasEulerianCycle() == (cycle == null)) return false

        // hashEulerianCycle() returns correct value
        if (hasEulerianCycle() != satisfiesNecessaryAndSufficientConditions(G)) return false

        // nothing else to check if no Eulerian cycle
        val cycle = this.cycle ?: return true

        // check that cycle() uses correct number of edges
        if (cycle.size != G.E + 1) return false

        // check that cycle() is a cycle of G
        // TODO

        // check that first and last vertices in cycle() are the same
        var first: Int? = -1
        var last: Int? = -1
        for (v in cycle) {
            if (first == -1) first = v
            last = v
        }
        return first == last

    }

    companion object {

        // returns any non-isolated vertex; -1 if no such vertex
        private fun nonIsolatedVertex(G: Graph): Int {
            for (v in 0 until G.V)
                if (G.degree(v) > 0)
                    return v
            return -1
        }

        // Determines whether a graph has an Eulerian cycle using necessary
        // and sufficient conditions (without computing the cycle itself):
        //    - at least one edge
        //    - degree(v) is even for every vertex v
        //    - the graph is connected (ignoring isolated vertices)
        private fun satisfiesNecessaryAndSufficientConditions(G: Graph): Boolean {
            // Condition 0: at least 1 edge
            if (G.E == 0) return false

            // Condition 1: degree(v) is even for every vertex
            for (v in 0 until G.V)
                if (G.degree(v) % 2 != 0)
                    return false

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

            val euler = EulerianCycle(G)

            StdOut.print("Eulerian cycle: ")
            if (euler.hasEulerianCycle()) {
                for (v in euler.cycle!!)
                    StdOut.print("$v ")
                StdOut.println()
            } else
                StdOut.println("none")
            StdOut.println()
        }

        /**
         * Unit tests the `EulerianCycle` data type.
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

            // empty graph
            val G3 = Graph(V)
            unitTest(G3, "empty graph")

            // self loop
            val G4 = Graph(V)
            val v4 = StdRandom.uniform(V)
            G4.addEdge(v4, v4)
            unitTest(G4, "single self loop")

            // union of two disjoint cycles
            val H1 = GraphGenerator.eulerianCycle(V / 2, E / 2)
            val H2 = GraphGenerator.eulerianCycle(V - V / 2, E - E / 2)
            val perm = IntArray(V) { it }
            StdRandom.shuffle(perm)
            val G5 = Graph(V)
            for (v in 0 until H1.V)
                for (w in H1.adj(v))
                    G5.addEdge(perm[v], perm[w])
            for (v in 0 until H2.V)
                for (w in H2.adj(v))
                    G5.addEdge(perm[V / 2 + v], perm[V / 2 + w])
            unitTest(G5, "Union of two disjoint cycles")

            // random digraph
            val G6 = GraphGenerator.simple(V, E)
            unitTest(G6, "simple graph")
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
