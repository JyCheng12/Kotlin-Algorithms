/******************************************************************************
 * Compilation:  javac FloydWarshall.java
 * Execution:    java FloydWarshall V E
 * Dependencies: AdjMatrixEdgeWeightedDigraph.kt
 *
 * Floyd-Warshall all-pairs shortest path algorithm.
 *
 * % java FloydWarshall 100 500
 *
 * Should check for negative cycles during triple loop; otherwise
 * intermediate numbers can get exponentially large.
 * Reference: "The Floyd-Warshall algorithm on graphs with negative cycles"
 * by Stefan Hougardy
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `FloydWarshall` class represents a data type for solving the
 * all-pairs shortest paths problem in edge-weighted digraphs with
 * no negative cycles.
 * The edge weights can be positive, negative, or zero.
 * This class finds either a shortest path between every pair of vertices
 * or a negative cycle.
 *
 *
 * This implementation uses the Floyd-Warshall algorithm.
 * The constructor takes time proportional to *V*<sup>3</sup> in the
 * worst case, where *V* is the number of vertices.
 * Afterwards, the `dist()`, `hasPath()`, and `hasNegativeCycle()`
 * methods take constant time; the `path()` and `negativeCycle()`
 * method takes time proportional to the number of edges returned.
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
class FloydWarshall
/**
 * Computes a shortest paths tree from each vertex to to every other vertex in
 * the edge-weighted digraph `G`. If no such shortest path exists for
 * some pair of vertices, it computes a negative cycle.
 * @param G the edge-weighted digraph
 */
(G: AdjMatrixEdgeWeightedDigraph) {
    var hasNegativeCycle: Boolean = false  // is there a negative cycle?
        private set
    private val distTo: Array<DoubleArray> = Array(G.V) { DoubleArray(G.V) }         // distTo[v][w] = length of shortest v->w path
    private val edgeTo: Array<Array<DirectedEdge?>> = Array(G.V) { arrayOfNulls<DirectedEdge>(G.V) }   // edgeTo[v][w] = last edge on shortest v->w path

    init {
        run{
            val V = G.V
            // initialize distances to infinity
            for (v in 0 until V) {
                for (w in 0 until V) {
                    distTo[v][w] = Double.POSITIVE_INFINITY
                }
            }

            // initialize distances using edge-weighted digraph's
            for (v in 0 until G.V) {
                for (e in G.adj(v)) {
                    distTo[e.from][e.to] = e.weight
                    edgeTo[e.from][e.to] = e
                }
                // in case of self-loops
                if (distTo[v][v] >= 0.0) {
                    distTo[v][v] = 0.0
                    edgeTo[v][v] = null
                }
            }

            // Floyd-Warshall updates
            for (i in 0 until V) {
                // compute shortest paths using only 0, 1, ..., i as intermediate vertices
                for (v in 0 until V) {
                    if (edgeTo[v][i] == null) continue  // optimization
                    for (w in 0 until V) {
                        if (distTo[v][w] > distTo[v][i] + distTo[i][w]) {
                            distTo[v][w] = distTo[v][i] + distTo[i][w]
                            edgeTo[v][w] = edgeTo[i][w]
                        }
                    }
                    // check for negative cycle
                    if (distTo[v][v] < 0.0) {
                        hasNegativeCycle = true
                        return@run
                    }
                }
            }
            assert(check(G))
        }
    }

    /**
     * Returns a negative cycle, or `null` if there is no such cycle.
     * @return a negative cycle as an iterable of edges,
     * or `null` if there is no such cycle
     */
    fun negativeCycle(): Iterable<DirectedEdge>? {
        for (v in distTo.indices) {
            // negative cycle in v's predecessor graph
            if (distTo[v][v] < 0.0) {
                val V = edgeTo.size
                val spt = EdgeWeightedDigraph(V)
                for (w in 0 until V)
                    edgeTo[v][w]?.let { spt.addEdge(edgeTo[v][w]!!) }

                val finder = EdgeWeightedDirectedCycle(spt)
                assert(finder.hasCycle)
                return finder.cycle
            }
        }
        return null
    }

    /**
     * Is there a path from the vertex `s` to vertex `t`?
     * @param  s the source vertex
     * @param  t the destination vertex
     * @return `true` if there is a path from vertex `s`
     * to vertex `t`, and `false` otherwise
     * @throws IllegalArgumentException unless `0 <= s < V`
     * @throws IllegalArgumentException unless `0 <= t < V`
     */
    fun hasPath(s: Int, t: Int): Boolean {
        validateVertex(s)
        validateVertex(t)
        return distTo[s][t] < Double.POSITIVE_INFINITY
    }

    /**
     * Returns the length of a shortest path from vertex `s` to vertex `t`.
     * @param  s the source vertex
     * @param  t the destination vertex
     * @return the length of a shortest path from vertex `s` to vertex `t`;
     * `Double.POSITIVE_INFINITY` if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun dist(s: Int, t: Int): Double {
        validateVertex(s)
        validateVertex(t)
        if (hasNegativeCycle)
            throw UnsupportedOperationException("Negative cost cycle exists")
        return distTo[s][t]
    }

    /**
     * Returns a shortest path from vertex `s` to vertex `t`.
     * @param  s the source vertex
     * @param  t the destination vertex
     * @return a shortest path from vertex `s` to vertex `t`
     * as an iterable of edges, and `null` if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun path(s: Int, t: Int): Iterable<DirectedEdge>? {
        validateVertex(s)
        validateVertex(t)
        if (hasNegativeCycle)
            throw UnsupportedOperationException("Negative cost cycle exists")
        if (!hasPath(s, t)) return null
        val path = nnStack<DirectedEdge>()
        var e: DirectedEdge? = edgeTo[s][t]
        while (e != null) {
            path.push(e)
            e = edgeTo[s][e.from]
        }
        return path
    }

    // check optimality conditions
    private fun check(G: AdjMatrixEdgeWeightedDigraph): Boolean {
        // no negative cycle
        if (!hasNegativeCycle) {
            for (v in 0 until G.V) {
                for (e in G.adj(v)) {
                    val w = e.to
                    for (i in 0 until G.V) {
                        if (distTo[i][w] > distTo[i][v] + e.weight) {
                            System.err.println("edge $e is eligible")
                            return false
                        }
                    }
                }
            }
        }
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = distTo.size
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {

        /**
         * Unit tests the `FloydWarshall` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {

            // random graph with V vertices and E edges, parallel edges allowed
            val V = Integer.parseInt(args[0])
            val E = Integer.parseInt(args[1])
            val G = AdjMatrixEdgeWeightedDigraph(V)
            for (i in 0 until E) {
                val v = StdRandom.uniform(V)
                val w = StdRandom.uniform(V)
                val weight = Math.round(100 * (StdRandom.uniform() - 0.15)) / 100.0
                if (v == w)
                    G.addEdge(DirectedEdge(v, w, Math.abs(weight)))
                else
                    G.addEdge(DirectedEdge(v, w, weight))
            }

            StdOut.println(G)

            // run Floyd-Warshall algorithm
            val spt = FloydWarshall(G)

            // print all-pairs shortest path distances
            StdOut.print("  ")
            for (v in 0 until G.V) {
                StdOut.printf("%6d ", v)
            }
            StdOut.println()
            for (v in 0 until G.V) {
                StdOut.printf("%3d: ", v)
                for (w in 0 until G.V) {
                    if (spt.hasPath(v, w))
                        StdOut.printf("%6.2f ", spt.dist(v, w))
                    else
                        StdOut.print("  Inf ")
                }
                StdOut.println()
            }

            // print negative cycle
            if (spt.hasNegativeCycle) {
                StdOut.println("Negative cost cycle:")
                for (e in spt.negativeCycle()!!)
                    StdOut.println(e)
                StdOut.println()
            } else {
                for (v in 0 until G.V) {
                    for (w in 0 until G.V) {
                        if (spt.hasPath(v, w)) {
                            StdOut.printf("%d to %d (%5.2f)  ", v, w, spt.dist(v, w))
                            for (e in spt.path(v, w)!!)
                                StdOut.print("$e  ")
                            StdOut.println()
                        } else {
                            StdOut.println("$v to $w no path")
                        }
                    }
                }
            }// print all-pairs shortest paths
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
