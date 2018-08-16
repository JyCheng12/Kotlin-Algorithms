/******************************************************************************
 * Compilation:  javac EdgeWeightedDirectedCycle.java
 * Execution:    java EdgeWeightedDirectedCycle V E F
 * Dependencies: EdgeWeightedDigraph.kt DirectedEdge.kt Stack.kt
 *
 * Finds a directed cycle in an edge-weighted digraph.
 * Runs in O(E + V) time.
 *
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `EdgeWeightedDirectedCycle` class represents a data type for
 * determining whether an edge-weighted digraph has a directed cycle.
 * The *hasCycle* operation determines whether the edge-weighted
 * digraph has a directed cycle and, if so, the *cycle* operation
 * returns one.
 *
 *
 * This implementation uses depth-first search.
 * The constructor takes time proportional to *V* + *E*
 * (in the worst case),
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the *hasCycle* operation takes constant time;
 * the *cycle* operation takes time proportional
 * to the length of the cycle.
 *
 *
 * See [Topological] to compute a topological order if the edge-weighted
 * digraph is acyclic.
 *
 *
 * For additional documentation,
 * see [Section 4.4](https://algs4.cs.princeton.edu/44sp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class EdgeWeightedDirectedCycle
/**
 * Determines whether the edge-weighted digraph `G` has a directed cycle and,
 * if so, finds such a cycle.
 * @param G the edge-weighted digraph
 */
(G: EdgeWeightedDigraph) {
    private val marked: BooleanArray = BooleanArray(G.V)             // marked[v] = has vertex v been marked?
    private val edgeTo: Array<DirectedEdge?> = arrayOfNulls(G.V)        // edgeTo[v] = previous edge on path to v
    private val onStack: BooleanArray = BooleanArray(G.V)            // onStack[v] = is vertex on the stack?
    var cycle: nnStack<DirectedEdge>? = null    // directed cycle (or null if no such cycle)
        private set

    init {
        for (v in 0 until G.V)
            if (!marked[v]) dfs(G, v)

        // check that digraph has a cycle
        assert(check())
    }

    // check that algorithm computes either the topological order or finds a directed cycle
    private fun dfs(G: EdgeWeightedDigraph, v: Int) {
        onStack[v] = true
        marked[v] = true
        for (e in G.adj(v)) {
            val w = e.to

            // short circuit if directed cycle found
            if (cycle != null) return
            else if (!marked[w]) {
                edgeTo[w] = e
                dfs(G, w)
            } else if (onStack[w]) {
                val cycle = nnStack<DirectedEdge>()
                var f = e
                while (f.from != w) {
                    cycle.push(f)
                    f = edgeTo[f.from]!!
                }
                cycle.push(f)
                this.cycle = cycle
                return
            }// trace back directed cycle
            // found new vertex, so recur
        }
        onStack[v] = false
    }

    /**
     * Does the edge-weighted digraph have a directed cycle?
     * @return `true` if the edge-weighted digraph has a directed cycle,
     * `false` otherwise
     */
    var hasCycle: Boolean = false
        get() = cycle != null


    // certify that digraph is either acyclic or has a directed cycle
    private fun check(): Boolean {
        // edge-weighted digraph is cyclic
        if (hasCycle) {
            // verify cycle
            var first: DirectedEdge? = null
            var last: DirectedEdge? = null
            for (e in cycle!!) {
                if (first == null) first = e
                if (last != null) {
                    if (last.to != e.from) {
                        System.err.printf("cycle edges %s and %s not incident\n", last, e)
                        return false
                    }
                }
                last = e
            }

            if (last!!.to != first!!.from) {
                System.err.printf("cycle edges %s and %s not incident\n", last, first)
                return false
            }
        }
        return true
    }

    companion object {

        /**
         * Unit tests the `EdgeWeightedDirectedCycle` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            // create random DAG with V vertices and E edges; then add F random edges
            val V = Integer.parseInt(args[0])
            val E = Integer.parseInt(args[1])
            val F = Integer.parseInt(args[2])
            val G = EdgeWeightedDigraph(V)
            val vertices = IntArray(V) { it }
            StdRandom.shuffle(vertices)
            for (i in 0 until E) {
                var v: Int
                var w: Int
                do {
                    v = StdRandom.uniform(V)
                    w = StdRandom.uniform(V)
                } while (v >= w)
                val weight = StdRandom.uniform()
                G.addEdge(DirectedEdge(v, w, weight))
            }

            // add F extra edges
            for (i in 0 until F) {
                val v = StdRandom.uniform(V)
                val w = StdRandom.uniform(V)
                val weight = StdRandom.uniform(0.0, 1.0)
                G.addEdge(DirectedEdge(v, w, weight))
            }
            StdOut.println(G)

            // find a directed cycle
            val finder = EdgeWeightedDirectedCycle(G)
            if (finder.hasCycle) {
                StdOut.print("Cycle: ")
                for (e in finder.cycle!!)
                    StdOut.print("$e ")
                StdOut.println()
            } else
                StdOut.println("No directed cycle")  // or give topologial sort
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