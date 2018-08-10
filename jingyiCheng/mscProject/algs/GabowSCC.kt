/******************************************************************************
 * Compilation:  javac GabowSCC.java
 * Execution:    java GabowSCC V E
 * Dependencies: Digraph.kt Stack.kt TransitiveClosure.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 * https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 * https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 * Compute the strongly-connected components of a digraph using
 * Gabow's algorithm (aka Cheriyan-Mehlhorn algorithm).
 *
 * Runs in O(E + V) time.
 *
 * % java GabowSCC tinyDG.txt
 * 5 components
 * 1
 * 0 2 3 4 5
 * 9 10 11 12
 * 6 8
 * 7
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `GabowSCC` class represents a data type for
 * determining the strong components in a digraph.
 * The *id* operation determines in which strong component
 * a given vertex lies; the *areStronglyConnected* operation
 * determines whether two vertices are in the same strong component;
 * and the *count* operation determines the number of strong
 * components.
 *
 * The *component identifier* of a component is one of the
 * vertices in the strong component: two vertices have the same component
 * identifier if and only if they are in the same strong component.
 *
 *
 *
 * This implementation uses the Gabow's algorithm.
 * The constructor takes time proportional to *V* + *E*
 * (in the worst case),
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the *id*, *count*, and *areStronglyConnected*
 * operations take constant time.
 * For alternate implementations of the same API, see
 * [KosarajuSharirSCC] and [TarjanSCC].
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
class GabowSCC
/**
 * Computes the strong components of the digraph `G`.
 * @param G the digraph
 */
(G: Digraph) {
    private val marked: BooleanArray = BooleanArray(G.V)        // marked[v] = has v been visited?
    private val id: IntArray = IntArray(G.V) { -1 }                // id[v] = id of strong component containing v
    private val preorder: IntArray = IntArray(G.V)          // preorder[v] = preorder of v
    private var pre: Int = 0                 // preorder number counter
    var count: Int = 0               // number of strongly-connected components
        private set
    private val stack1: nnStack<Int> = nnStack()
    private val stack2: nnStack<Int> = nnStack()

    init {
        for (v in 0 until G.V) {
            if (!marked[v]) dfs(G, v)
        }
        // check that id[] gives strong components
        assert(check(G))
    }

    private fun dfs(G: Digraph, v: Int) {
        marked[v] = true
        preorder[v] = pre++
        stack1.push(v)
        stack2.push(v)
        for (w in G.adj(v)) {
            if (!marked[w])
                dfs(G, w)
            else if (id[w] == -1) {
                while (preorder[stack2.peek()] > preorder[w])
                    stack2.pop()
            }
        }

        // found strong component containing v
        if (stack2.peek() == v) {
            stack2.pop()
            var w: Int
            do {
                w = stack1.pop()
                id[w] = count
            } while (w != v)
            count++
        }
    }

    /**
     * Are vertices `v` and `w` in the same strong component?
     * @param  v one vertex
     * @param  w the other vertex
     * @return `true` if vertices `v` and `w` are in the same
     * strong component, and `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     * @throws IllegalArgumentException unless `0 <= w < V`
     */
    fun stronglyConnected(v: Int, w: Int): Boolean {
        validateVertex(v)
        validateVertex(w)
        return id[v] == id[w]
    }

    /**
     * Returns the component id of the strong component containing vertex `v`.
     * @param  v the vertex
     * @return the component id of the strong component containing vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun id(v: Int): Int {
        validateVertex(v)
        return id[v]
    }

    // does the id[] array contain the strongly connected components?
    private fun check(G: Digraph): Boolean {
        val tc = TransitiveClosure(G)
        for (v in 0 until G.V) {
            for (w in 0 until G.V) {
                if (stronglyConnected(v, w) != (tc.reachable(v, w) && tc.reachable(w, v)))
                    return false
            }
        }
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = marked.size
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {
        /**
         * Unit tests the `GabowSCC` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Digraph(`in`)
            val scc = GabowSCC(G)

            // number of connected components
            val m = scc.count
            StdOut.println("$m components")

            // compute list of vertices in each strong component
            val components = Array(m) { nnQueue<Int>() }
            for (v in 0 until G.V) {
                components[scc.id(v)].enqueue(v)
            }

            // print results
            for (i in 0 until m) {
                for (v in components[i]) {
                    StdOut.print("$v ")
                }
                StdOut.println()
            }
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
