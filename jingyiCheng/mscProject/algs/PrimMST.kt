/******************************************************************************
 * Compilation:  javac PrimMST.java
 * Execution:    java PrimMST filename.txt
 * Dependencies: EdgeWeightedGraph.kt Edge.kt Queue.kt
 * IndexMinPQ.kt UF.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 * https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 * https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 * Compute a minimum spanning forest using Prim's algorithm.
 *
 * %  java PrimMST tinyEWG.txt
 * 1-7 0.19000
 * 0-2 0.26000
 * 2-3 0.17000
 * 4-5 0.35000
 * 5-7 0.28000
 * 6-2 0.40000
 * 0-7 0.16000
 * 1.81000
 *
 * % java PrimMST mediumEWG.txt
 * 1-72   0.06506
 * 2-86   0.05980
 * 3-67   0.09725
 * 4-55   0.06425
 * 5-102  0.03834
 * 6-129  0.05363
 * 7-157  0.00516
 * ...
 * 10.46351
 *
 * % java PrimMST largeEWG.txt
 * ...
 * 647.66307
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `PrimMST` class represents a data type for computing a
 * *minimum spanning tree* in an edge-weighted graph.
 * The edge weights can be positive, zero, or negative and need not
 * be distinct. If the graph is not connected, it computes a *minimum
 * spanning forest*, which is the union of minimum spanning trees
 * in each connected component. The `weight()` method returns the
 * weight of a minimum spanning tree and the `edges()` method
 * returns its edges.
 *
 *
 * This implementation uses *Prim's algorithm* with an indexed
 * binary heap.
 * The constructor takes time proportional to *E* log *V*
 * and extra space (not including the graph) proportional to *V*,
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the `weight()` method takes constant time
 * and the `edges()` method takes time proportional to *V*.
 *
 *
 * For additional documentation,
 * see [Section 4.3](https://algs4.cs.princeton.edu/43mst) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 * For alternate implementations, see [LazyPrimMST], [KruskalMST],
 * and [BoruvkaMST].
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 */
class PrimMST
/**
 * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
 * @param G the edge-weighted graph
 */
(G: EdgeWeightedGraph) {
    private val edgeTo: Array<Edge?> = arrayOfNulls(G.V)        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private val distTo: DoubleArray = DoubleArray(G.V) { Double.POSITIVE_INFINITY }     // distTo[v] = weight of shortest such edge
    private val marked: BooleanArray = BooleanArray(G.V)     // marked[v] = true if v on tree, false otherwise
    private val pq: IndexMinPQ<Double> = IndexMinPQ(G.V)

    init {
        for (v in 0 until G.V)
        // run from each vertex to find
            if (!marked[v]) prim(G, v)      // minimum spanning forest

        // check optimality conditions
        //assert(check(G))
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private fun prim(G: EdgeWeightedGraph, s: Int) {
        distTo[s] = 0.0
        pq.insert(s, distTo[s])
        while (!pq.isEmpty) {
            val v = pq.delMin()
            scan(G, v)
        }
    }

    // scan vertex v
    private fun scan(G: EdgeWeightedGraph, v: Int) {
        marked[v] = true
        for (e in G.adj(v)) {
            val w = e.other(v)
            if (marked[w]) continue         // v-w is obsolete edge
            if (e.weight < distTo[w]) {
                distTo[w] = e.weight
                edgeTo[w] = e
                if (pq.contains(w))
                    pq.decreaseKey(w, distTo[w])
                else
                    pq.insert(w, distTo[w])
            }
        }
    }

    /**
     * Returns the edges in a minimum spanning tree (or forest).
     * @return the edges in a minimum spanning tree (or forest) as
     * an iterable of edges
     */
    fun edges(): Iterable<Edge> {
        val mst = nnQueue<Edge>()
        for (v in edgeTo)
            if (v != null)
                mst.enqueue(v)
        return mst
    }

    var weight: Double = 0.0
        get() = run {
            var weight = 0.0
            for (e in edges())
                weight += e.weight
            weight
        }

    // check optimality conditions (takes time proportional to E V lg* V)
    private fun check(G: EdgeWeightedGraph): Boolean {
        // check weight
        var totalWeight = 0.0
        for (e in edges())
            totalWeight += e.weight
        if (Math.abs(totalWeight - weight) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight: %f vs. %f\n", totalWeight, weight)
            return false
        }

        // check that it is acyclic
        var uf = UF(G.V)
        for (e in edges()) {
            val v = e.either
            val w = e.other(v)
            if (uf.connected(v, w)) {
                System.err.println("Not a forest")
                return false
            }
            uf.union(v, w)
        }

        // check that it is a spanning forest
        for (e in G.edges()) {
            val v = e.either
            val w = e.other(v)
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest")
                return false
            }
        }

        // check that it is a minimal spanning forest (cut optimality conditions)
        for (e in edges()) {
            // all edges in MST except e
            uf = UF(G.V)
            for (f in edges()) {
                val x = f.either
                val y = f.other(x)
                if (f !== e) uf.union(x, y)
            }

            // check that e is min weight edge in crossing cut
            for (f in G.edges()) {
                val x = f.either
                val y = f.other(x)
                if (!uf.connected(x, y))
                    if (f.weight < e.weight) {
                        System.err.println("Edge $f violates cut optimality conditions")
                        return false
                    }
            }
        }
        return true
    }

    companion object {
        private const val FLOATING_POINT_EPSILON = 1E-12

        /**
         * Unit tests the `PrimMST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = EdgeWeightedGraph(`in`)
            val mst = PrimMST(G)
            for (e in mst.edges())
                StdOut.println(e)
            StdOut.printf("%.5f\n", mst.weight)
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