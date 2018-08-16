/******************************************************************************
 * Compilation:  javac DirectedCycle.java
 * Execution:    java DirectedCycle input.txt
 * Dependencies: Digraph.kt Stack.kt StdOut.kt In.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 * https://algs4.cs.princeton.edu/42digraph/tinyDAG.txt
 *
 * Finds a directed cycle in a digraph.
 * Runs in O(E + V) time.
 *
 * % java DirectedCycle tinyDG.txt
 * Directed cycle: 3 5 4 3
 *
 * %  java DirectedCycle tinyDAG.txt
 * No directed cycle
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DirectedCycle` class represents a data type for
 * determining whether a digraph has a directed cycle.
 * The *hasCycle* operation determines whether the digraph has
 * a directed cycle and, and of so, the *cycle* operation
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
 * See [Topological] to compute a topological order if the
 * digraph is acyclic.
 *
 *
 * For additional documentation,
 * see [Section 4.2](https://algs4.cs.princeton.edu/42digraph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class DirectedCycle
/**
 * Determines whether the digraph `G` has a directed cycle and, if so,
 * finds such a cycle.
 * @param G the digraph
 */
(G: Digraph) {
    private val marked: BooleanArray = BooleanArray(G.V)        // marked[v] = has vertex v been marked?
    private val edgeTo: IntArray = IntArray(G.V)            // edgeTo[v] = previous vertex on path to v
    private val onStack: BooleanArray = BooleanArray(G.V)       // onStack[v] = is vertex on the stack?
    var cycle: nnStack<Int>? = null    // directed cycle (or null if no such cycle)
        private set

    init {
        for (v in 0 until G.V)
            if (!marked[v] && cycle == null) dfs(G, v)
    }

    // check that algorithm computes either the topological order or finds a directed cycle
    private fun dfs(G: Digraph, v: Int) {
        onStack[v] = true
        marked[v] = true
        for (w in G.adj(v)) {
            // short circuit if directed cycle found
            if (cycle != null)
                return
            else if (!marked[w]) {
                edgeTo[w] = v
                dfs(G, w)
            } else if (onStack[w]) {
                val cycle = nnStack<Int>()
                var x = v
                while (x != w) {
                    cycle.push(x)
                    x = edgeTo[x]
                }
                cycle.push(w)
                cycle.push(v)
                this.cycle = cycle
                assert(check())
            }// trace back directed cycle
            // found new vertex, so recur
        }
        onStack[v] = false
    }

    /**
     * Does the digraph have a directed cycle?
     * @return `true` if the digraph has a directed cycle, `false` otherwise
     */
    fun hasCycle() = cycle != null

    // certify that digraph has a directed cycle if it reports one
    private fun check(): Boolean {
        if (hasCycle()) {
            // verify cycle
            var first = -1
            var last = -1
            for (v in cycle!!) {
                if (first == -1) first = v
                last = v
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last)
                return false
            }
        }
        return true
    }

    companion object {

        /**
         * Unit tests the `DirectedCycle` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Digraph(`in`)

            val finder = DirectedCycle(G)
            if (finder.hasCycle()) {
                StdOut.print("Directed cycle: ")
                for (v in finder.cycle!!)
                    StdOut.print("$v ")
                StdOut.println()
            } else
                StdOut.println("No directed cycle")
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