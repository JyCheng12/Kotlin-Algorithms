/******************************************************************************
 * Compilation:  javac TransitiveClosure.java
 * Execution:    java TransitiveClosure filename.txt
 * Dependencies: Digraph.kt DepthFirstDirectedPaths.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *
 * Compute transitive closure of a digraph and support
 * reachability queries.
 *
 * Preprocessing time: O(V(E + V)) time.
 * Query time: O(1).
 * Space: O(V^2).
 *
 * % java TransitiveClosure tinyDG.txt
 *       0  1  2  3  4  5  6  7  8  9 10 11 12
 * --------------------------------------------
 * 0:    T  T  T  T  T  T
 * 1:       T
 * 2:    T  T  T  T  T  T
 * 3:    T  T  T  T  T  T
 * 4:    T  T  T  T  T  T
 * 5:    T  T  T  T  T  T
 * 6:    T  T  T  T  T  T  T     T  T  T  T  T
 * 7:    T  T  T  T  T  T  T  T  T  T  T  T  T
 * 8:    T  T  T  T  T  T  T     T  T  T  T  T
 * 9:    T  T  T  T  T  T           T  T  T  T
 * 10:   T  T  T  T  T  T           T  T  T  T
 * 11:   T  T  T  T  T  T           T  T  T  T
 * 12:   T  T  T  T  T  T           T  T  T  T
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `TransitiveClosure` class represents a data type for
 * computing the transitive closure of a digraph.
 *
 *
 * This implementation runs depth-first search from each vertex.
 * The constructor takes time proportional to *V*(*V* + *E*)
 * (in the worst case) and uses space proportional to *V*<sup>2</sup>,
 * where *V* is the number of vertices and *E* is the number of edges.
 *
 *
 * For large digraphs, you may want to consider a more sophisticated algorithm.
 * [Nuutila](http://www.cs.hut.fi/~enu/thesis.html) proposes two
 * algorithm for the problem (based on strong components and an interval representation)
 * that runs in *E* + *V* time on typical digraphs.
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
class TransitiveClosure
/**
 * Computes the transitive closure of the digraph `G`.
 * @param G the digraph
 */
(G: Digraph) {
    private val tc: Array<DirectedDFS> = Array(G.V) { DirectedDFS(G, it) }  // tc[v] = reachable from v

    /**
     * Is there a directed path from vertex `v` to vertex `w` in the digraph?
     * @param  v the source vertex
     * @param  w the target vertex
     * @return `true` if there is a directed path from `v` to `w`,
     * `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     * @throws IllegalArgumentException unless `0 <= w < V`
     */
    fun reachable(v: Int, w: Int): Boolean {
        validateVertex(v)
        validateVertex(w)
        return tc[v].marked(w)
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        if (v < 0 || v >= tc.size) throw IllegalArgumentException("vertex $v is not between 0 and ${tc.size - 1}")
    }

    companion object {

        /**
         * Unit tests the `TransitiveClosure` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Digraph(`in`)

            val tc = TransitiveClosure(G)

            // print header
            StdOut.print("     ")
            for (v in 0 until G.V)
                StdOut.printf("%3d", v)
            StdOut.println("\n--------------------------------------------")

            // print transitive closure
            for (v in 0 until G.V) {
                StdOut.printf("%3d: ", v)
                for (w in 0 until G.V)
                    if (tc.reachable(v, w))
                        StdOut.print("  T")
                    else
                        StdOut.print("   ")
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
