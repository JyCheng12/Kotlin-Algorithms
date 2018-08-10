/******************************************************************************
 * Compilation:  javac Cycle.java
 * Execution:    java  Cycle filename.txt
 * Dependencies: Graph.kt Stack.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/41graph/tinyG.txt
 * https://algs4.cs.princeton.edu/41graph/mediumG.txt
 * https://algs4.cs.princeton.edu/41graph/largeG.txt
 *
 * Identifies a cycle.
 * Runs in O(E + V) time.
 *
 * % java Cycle tinyG.txt
 * 3 4 5 3
 *
 * % java Cycle mediumG.txt
 * 15 0 225 15
 *
 * % java Cycle largeG.txt
 * 996673 762 840164 4619 785187 194717 996673
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Cycle` class represents a data type for
 * determining whether an undirected graph has a cycle.
 * The *hasCycle* operation determines whether the graph has
 * a cycle and, if so, the *cycle* operation returns one.
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
 * For additional documentation, see [Section 4.1](https://algs4.cs.princeton.edu/41graph)
 * of *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class Cycle
/**
 * Determines whether the undirected graph `G` has a cycle and,
 * if so, finds such a cycle.
 *
 * @param G the undirected graph
 */
(G: Graph) {
    private var marked: BooleanArray = BooleanArray(G.V)
    private val edgeTo: IntArray = IntArray(G.V)
    var cycle: nnStack<Int>? = null
        private set

    init {
        run {
            if (hasSelfLoop(G)) return@run
            if (hasParallelEdges(G)) return@run

            for (v in 0 until G.V)
                if (!marked[v])
                    dfs(G, -1, v)
        }
    }


    // does this graph have a self loop?
    // side effect: initialize cycle to be self loop
    private fun hasSelfLoop(G: Graph): Boolean {
        for (v in 0 until G.V)
            for (w in G.adj(v))
                if (v == w) {
                    val cycle = nnStack<Int>()
                    cycle.push(v)
                    cycle.push(v)
                    this.cycle = cycle
                    return true
                }
        return false
    }

    // does this graph have two parallel edges?
    // side effect: initialize cycle to be two parallel edges
    private fun hasParallelEdges(G: Graph): Boolean {
        marked = BooleanArray(G.V)
        for (v in 0 until G.V) {
            // check for parallel edges incident to v
            for (w in G.adj(v)) {
                if (marked[w]) {
                    val cycle = nnStack<Int>()
                    cycle.push(v)
                    cycle.push(w)
                    cycle.push(v)
                    this.cycle = cycle
                    return true
                }
                marked[w] = true
            }

            // reset so marked[v] = false for all v
            for (w in G.adj(v))
                marked[w] = false
        }
        return false
    }

    /**
     * Returns true if the graph `G` has a cycle.
     *
     * @return `true` if the graph has a cycle; `false` otherwise
     */
    var hasCycle: Boolean = false
        get() = cycle != null


    private fun dfs(G: Graph, u: Int, v: Int) {
        marked[v] = true
        for (w in G.adj(v)) {
            // short circuit if cycle already found
            cycle?.let { return }

            if (!marked[w]) {
                edgeTo[w] = v
                dfs(G, v, w)
            } else if (w != u) {
                cycle = nnStack()
                var x = v
                while (x != w) {
                    cycle?.push(x)
                    x = edgeTo[x]
                }
                cycle?.push(w)
                cycle?.push(v)
            }// check for cycle (but disregard reverse of edge leading to v)
        }
    }

    companion object {
        /**
         * Unit tests the `Cycle` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Graph(`in`)
            val finder = Cycle(G)
            val cycle = finder.cycle
            if (cycle != null) {
                for (v in cycle)
                    StdOut.print("$v ")
                StdOut.println()
            } else
                StdOut.println("Graph is acyclic")
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
