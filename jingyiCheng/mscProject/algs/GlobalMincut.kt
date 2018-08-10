/******************************************************************************
 * Compilation:  javac GlobalMincut.java
 * Execution:    java  GlobalMincut filename.txt
 * Dependencies: EdgeWeightedGraph.kt Edge.kt UF.kt
 * IndexMaxPQ.kt FlowNetwork.kt FlowEdge.kt
 * FordFulkerson.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 * https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 *
 * Computes a minimum cut using Stoer-Wagner's algorithm.
 *
 * % java GlobalMincut tinyEWG.txt
 * Min cut: 5
 * Min cut weight = 0.9500000000000001
 *
 * % java GlobalMincut mediumEWG.txt
 * Min cut: 25 60 63 96 199 237
 * Min cut weight = 0.14021
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `GlobalMincut` class represents a data type for computing a
 * *global minimum cut* in an edge-weighted graph where the edge
 * weights are nonnegative. A *cut* is a partition of the set
 * of vertices of a graph into two nonempty subsets. An edge that has one
 * endpoint in each subset of a cut is a *crossing edge*. The weight
 * of a cut is the sum of the weights of its crossing edges.
 * A *global minimum cut* is a cut for which the weight is not
 * larger than the weight of any other cut.
 *
 *
 * The `weight()` method returns the weight of the minimum cut and the
 * `cut(int v)` method determines if a vertex `v` is on the first or
 * on the second subset of vertices of the minimum cut.
 *
 *
 * This is an implementation of *Stoer–Wagner's algorithm* using an index
 * priority queue and the union-find data type in order to simplify dealing with
 * contracting edges. Precisely, the index priority queue is an instance of
 * [IndexMaxPQ] which is based on a binary heap. As a consequence, the
 * constructor takes *O*(*V* (*V* + * E* ) log *
 * V *) time and *O*(*V*) extra space (not including the
 * graph), where *V* is the number of vertices and *E* is the
 * number of edges. However, this time can be reduced to *O*(*V E*
 * + * V<sup>2</sup>* log *V*) by using an index priority queue
 * implemented using Fibonacci heaps.
 *
 *
 * Afterwards, the `weight()` and `cut(int v)` methods take constant
 * time.
 *
 *
 * For additional documentation, see
 *
 *  * M. Stoer and F. Wagner (1997). A simple min-cut algorithm. *Journal of
 * the ACM *, 44(4):585-591.
 *
 *
 * @author Marcelo Silva
 */
class GlobalMincut
/**
 * Computes a minimum cut of an edge-weighted graph.
 *
 * @param G the edge-weighted graph
 * @throws IllegalArgumentException if the number of vertices of `G`
 * is less than `2` or if anny edge weight is negative
 */
(G: EdgeWeightedGraph) {
    // the weight of the minimum cut
    var weight = Double.POSITIVE_INFINITY
    private set

    // cut[v] = true if v is on the first subset of vertices of the minimum cut;
    // or false if v is on the second subset
    private lateinit var cut: BooleanArray

    // number of vertices
    private val V: Int = G.V

    /**
     * This helper class represents the *cut-of-the-phase*. The
     * cut-of-the-phase is a *minimum s-t-cut* in the current graph,
     * where `s` and `t` are the two vertices added last in the
     * phase.
     */
    internal inner class CutPhase(var weight: Double // the weight of the minimum s-t cut
                                 , var s: Int         // the vertex s
                                 , var t: Int         // the vertex t
    )

    init {
        validate(G)
        minCut(G, 0)
        assert(check(G))
    }

    /**
     * Validates the edge-weighted graph.
     *
     * @param G the edge-weighted graph
     * @throws IllegalArgumentException if the number of vertices of `G`
     * is less than `2` or if any edge weight is negative
     */
    private fun validate(G: EdgeWeightedGraph) {
        if (G.V < 2) throw IllegalArgumentException("number of vertices of G is less than 2")
        for (e in G.edges()) {
            if (e.weight < 0) throw IllegalArgumentException("edge $e has negative weight")
        }
    }

    /**
     * Returns `true` if the vertex `v` is on the first subset of
     * vertices of the minimum cut; or `false` if the vertex `v` is
     * on the second subset.
     *
     * @param v the vertex to check
     * @return `true` if the vertex `v` is on the first subset of
     * vertices of the minimum cut; or `false` if the vertex
     * `v` is on the second subset.
     * @throws IllegalArgumentException unless vertex `v` is between
     * `0` and `(G.V() - 1)`
     */
    fun cut(v: Int): Boolean {
        validateVertex(v)
        return cut[v]
    }

    /**
     * Makes a cut for the current edge-weighted graph by partitioning its set
     * of vertices into two nonempty subsets. The vertices connected to the
     * vertex `t` belong to the first subset. Other vertices not connected
     * to `t` belong to the second subset.
     *
     * @param t the vertex `t`
     * @param uf the union-find data type
     */
    private fun makeCut(t: Int, uf: UF) {
        for (v in cut.indices) {
            cut[v] = uf.connected(v, t)
        }
    }

    /**
     * Computes a minimum cut of the edge-weighted graph. Precisely, it computes
     * the lightest of the cuts-of-the-phase which yields the desired minimum
     * cut.
     *
     * @param G the edge-weighted graph
     * @param a the starting vertex
     */
    private fun minCut(G: EdgeWeightedGraph, a: Int) {
        var G = G
        val uf = UF(G.V)
        val marked = BooleanArray(G.V)
        cut = BooleanArray(G.V)
        var cp = CutPhase(0.0, a, a)
        for (v in G.V downTo 2) {
            cp = minCutPhase(G, marked, cp)
            if (cp.weight < weight) {
                weight = cp.weight
                makeCut(cp.t, uf)
            }
            G = contractEdge(G, cp.s, cp.t)
            marked[cp.t] = true
            uf.union(cp.s, cp.t)
        }
    }

    /**
     * Returns the cut-of-the-phase. The cut-of-the-phase is a minimum s-t-cut
     * in the current graph, where `s` and `t` are the two vertices
     * added last in the phase. This algorithm is known in the literature as
     * *maximum adjacency search* or *maximum cardinality search*.
     *
     * @param G the edge-weighted graph
     * @param marked the array of contracted vertices, where `marked[v]`
     * is `true` if the vertex `v` was already
     * contracted; or `false` otherwise
     * @param cp the previous cut-of-the-phase
     * @return the cut-of-the-phase
     */
    private fun minCutPhase(G: EdgeWeightedGraph, marked: BooleanArray, cp: CutPhase): CutPhase {
        val pq = IndexMaxPQ<Double>(G.V)
        for (v in 0 until G.V) {
            if (v != cp.s && !marked[v]) pq.insert(v, 0.0)
        }
        pq.insert(cp.s, Double.POSITIVE_INFINITY)
        while (!pq.isEmpty) {
            val v = pq.delMax()
            cp.s = cp.t
            cp.t = v
            for (e in G.adj(v)) {
                val w = e.other(v)
                if (pq.contains(w)) pq.increaseKey(w, pq.keyOf(w) + e.weight)
            }
        }
        cp.weight = 0.0
        for (e in G.adj(cp.t)) {
            cp.weight += e.weight
        }
        return cp
    }

    /**
     * Contracts the edges incidents on the vertices `s` and `t` of
     * the given edge-weighted graph.
     *
     * @param G the edge-weighted graph
     * @param s the vertex `s`
     * @param t the vertex `t`
     * @return a new edge-weighted graph for which the edges incidents on the
     * vertices `s` and `t` were contracted
     */
    private fun contractEdge(G: EdgeWeightedGraph, s: Int, t: Int): EdgeWeightedGraph {
        val H = EdgeWeightedGraph(G.V)
        for (v in 0 until G.V) {
            for (e in G.adj(v)) {
                val w = e.other(v)
                if (v == s && w == t || v == t && w == s) continue
                if (v < w) {
                    when {
                        w == t -> H.addEdge(Edge(v, s, e.weight))
                        v == t -> H.addEdge(Edge(w, s, e.weight))
                        else -> H.addEdge(Edge(v, w, e.weight))
                    }
                }
            }
        }
        return H
    }

    /**
     * Checks optimality conditions.
     *
     * @param G the edge-weighted graph
     * @return `true` if optimality conditions are fine
     */
    private fun check(G: EdgeWeightedGraph): Boolean {

        // compute min st-cut for all pairs s and t
        // shortcut: s must appear on one side of global mincut,
        // so it suffices to try all pairs s-v for some fixed s
        var value = Double.POSITIVE_INFINITY
        val s = 0
        var t = 1
        while (t < G.V) {
            val F = FlowNetwork(G.V)
            for (e in G.edges()) {
                val v = e.either
                val w = e.other(v)
                F.addEdge(FlowEdge(v, w, e.weight))
                F.addEdge(FlowEdge(w, v, e.weight))
            }
            val maxflow = FordFulkerson(F, s, t)
            value = Math.min(value, maxflow.value)
            t++
        }
        if (Math.abs(weight - value) > FLOATING_POINT_EPSILON) {
            System.err.println("Min cut weight = $weight , max flow value = $value")
            return false
        }
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1))
    }

    companion object {
        private const val FLOATING_POINT_EPSILON = 1E-11
        /**
         * Unit tests the `GlobalMincut` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = EdgeWeightedGraph(`in`)
            val mc = GlobalMincut(G)
            StdOut.print("Min cut: ")
            for (v in 0 until G.V) {
                if (mc.cut(v)) StdOut.print("$v ")
            }
            StdOut.println()
            StdOut.println("Min cut weight = ${mc.weight}")
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
