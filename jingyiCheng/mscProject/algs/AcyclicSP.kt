/******************************************************************************
 * Compilation:  javac AcyclicSP.java
 * Execution:    java AcyclicSP V E
 * Dependencies: EdgeWeightedDigraph.kt DirectedEdge.kt Topological.kt
 * Data files:   https://algs4.cs.princeton.edu/44sp/tinyEWDAG.txt
 *
 * Computes shortest paths in an edge-weighted acyclic digraph.
 *
 * % java AcyclicSP tinyEWDAG.txt 5
 * 5 to 0 (0.73)  5->4  0.35   4->0  0.38
 * 5 to 1 (0.32)  5->1  0.32
 * 5 to 2 (0.62)  5->7  0.28   7->2  0.34
 * 5 to 3 (0.61)  5->1  0.32   1->3  0.29
 * 5 to 4 (0.35)  5->4  0.35
 * 5 to 5 (0.00)
 * 5 to 6 (1.13)  5->1  0.32   1->3  0.29   3->6  0.52
 * 5 to 7 (0.28)  5->7  0.28
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `AcyclicSP` class represents a data type for solving the
 * single-source shortest paths problem in edge-weighted directed acyclic
 * graphs (DAGs). The edge weights can be positive, negative, or zero.
 *
 *
 * This implementation uses a topological-sort based algorithm.
 * The constructor takes time proportional to *V* + *E*,
 * where *V* is the number of vertices and *E* is the number of edges.
 * Each call to `distTo(int)` and `hasPathTo(int)` takes constant time;
 * each call to `pathTo(int)` takes time proportional to the number of
 * edges in the shortest path returned.
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
class AcyclicSP
/**
 * Computes a shortest paths tree from `s` to every other vertex in
 * the directed acyclic graph `G`.
 * @param G the acyclic digraph
 * @param s the source vertex
 * @throws IllegalArgumentException if the digraph is not acyclic
 * @throws IllegalArgumentException unless `0 <= s < V`
 */
(G: EdgeWeightedDigraph, s: Int) {
    private val distTo: DoubleArray = DoubleArray(G.V) { Double.POSITIVE_INFINITY }        // distTo[v] = distance  of shortest s->v path
    private val edgeTo: Array<DirectedEdge?> = arrayOfNulls(G.V)   // edgeTo[v] = last edge on shortest s->v path


    init {
        validateVertex(s)
        distTo[s] = 0.0

        // visit vertices in toplogical order
        val topological = Topological(G)
        if (!topological.hasOrder)
            throw IllegalArgumentException("Digraph is not acyclic.")
        for (v in topological.order!!) {
            for (e in G.adj(v))
                relax(e)
        }
    }

    // relax edge e
    private fun relax(e: DirectedEdge) {
        val v = e.from
        val w = e.to
        if (distTo[w] > distTo[v] + e.weight) {
            distTo[w] = distTo[v] + e.weight
            edgeTo[w] = e
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex `s` to vertex `v`.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex `s` to vertex `v`;
     * `Double.POSITIVE_INFINITY` if no such path
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun distTo(v: Int): Double {
        validateVertex(v)
        return distTo[v]
    }

    /**
     * Is there a path from the source vertex `s` to vertex `v`?
     * @param  v the destination vertex
     * @return `true` if there is a path from the source vertex
     * `s` to vertex `v`, and `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun hasPathTo(v: Int): Boolean {
        validateVertex(v)
        return distTo[v] < Double.POSITIVE_INFINITY
    }

    /**
     * Returns a shortest path from the source vertex `s` to vertex `v`.
     * @param  v the destination vertex
     * @return a shortest path from the source vertex `s` to vertex `v`
     * as an iterable of edges, and `null` if no such path
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun pathTo(v: Int): Iterable<DirectedEdge>? {
        validateVertex(v)
        if (!hasPathTo(v)) return null
        val path = nnStack<DirectedEdge>()
        var e: DirectedEdge? = edgeTo[v]
        while (e != null) {
            path.push(e)
            e = edgeTo[e.from]
        }
        return path
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = distTo.size
        if (v < 0 || v >= V)
            throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {

        /**
         * Unit tests the `AcyclicSP` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val s = Integer.parseInt(args[1])
            val G = EdgeWeightedDigraph(`in`)

            // find shortest path from s to each other vertex in DAG
            val sp = AcyclicSP(G, s)
            for (v in 0 until G.V) {
                if (sp.hasPathTo(v)) {
                    StdOut.printf("%d to %d (%.2f)  ", s, v, sp.distTo(v))
                    for (e in sp.pathTo(v)!!) {
                        StdOut.print("$e   ")
                    }
                    StdOut.println()
                } else {
                    StdOut.println("$s to $v         no path")
                }
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