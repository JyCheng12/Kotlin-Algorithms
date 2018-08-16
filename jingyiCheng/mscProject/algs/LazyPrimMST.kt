/******************************************************************************
 * Compilation:  javac LazyPrimMST.java
 * Execution:    java LazyPrimMST filename.txt
 * Dependencies: EdgeWeightedGraph.kt Edge.kt Queue.kt
 * MinPQ.kt UF.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 * https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 * https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 * Compute a minimum spanning forest using a lazy version of Prim's
 * algorithm.
 *
 * %  java LazyPrimMST tinyEWG.txt
 * 0-7 0.16000
 * 1-7 0.19000
 * 0-2 0.26000
 * 2-3 0.17000
 * 5-7 0.28000
 * 4-5 0.35000
 * 6-2 0.40000
 * 1.81000
 *
 * % java LazyPrimMST mediumEWG.txt
 * 0-225   0.02383
 * 49-225  0.03314
 * 44-49   0.02107
 * 44-204  0.01774
 * 49-97   0.03121
 * 202-204 0.04207
 * 176-202 0.04299
 * 176-191 0.02089
 * 68-176  0.04396
 * 58-68   0.04795
 * 10.46351
 *
 * % java LazyPrimMST largeEWG.txt
 * ...
 * 647.66307
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LazyPrimMST` class represents a data type for computing a
 * *minimum spanning tree* in an edge-weighted graph.
 * The edge weights can be positive, zero, or negative and need not
 * be distinct. If the graph is not connected, it computes a *minimum
 * spanning forest*, which is the union of minimum spanning trees
 * in each connected component. The `weight()` method returns the
 * weight of a minimum spanning tree and the `edges()` method
 * returns its edges.
 *
 *
 * This implementation uses a lazy version of *Prim's algorithm*
 * with a binary heap of edges.
 * The constructor takes time proportional to *E* log *E*
 * and extra space (not including the graph) proportional to *E*,
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the `weight()` method takes constant time
 * and the `edges()` method takes time proportional to *V*.
 *
 *
 * For additional documentation,
 * see [Section 4.3](https://algs4.cs.princeton.edu/43mst) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 * For alternate implementations, see [PrimMST], [KruskalMST],
 * and [BoruvkaMST].
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class LazyPrimMST
/**
 * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
 * @param G the edge-weighted graph
 */
(G: EdgeWeightedGraph) {
    var weight: Double = 0.toDouble()       // total weight of MST
        private set
    private val edges: nnQueue<Edge> = nnQueue()     // edges in the MST
    private val marked: BooleanArray = BooleanArray(G.V)    // marked[v] = true if v on tree
    private val pq: MinPQ<Edge> = MinPQ()      // edges with one endpoint in tree

    init {
        for (v in 0 until G.V)
        // run Prim from all vertices to
            if (!marked[v]) prim(G, v)     // get a minimum spanning forest
        // check optimality conditions
        //assert(check(G))
    }

    // run Prim's algorithm
    private fun prim(G: EdgeWeightedGraph, s: Int) {
        scan(G, s)
        while (!pq.isEmpty) {                        // better to stop when mst has V-1 edges
            val e = pq.delMin()                      // smallest edge on pq
            val v = e.either
            val w = e.other(v)        // two endpoints
            //assert(marked[v] || marked[w])
            if (marked[v] && marked[w]) continue      // lazy, both v and w already scanned
            edges.enqueue(e)                            // add e to MST
            weight += e.weight
            if (!marked[v]) scan(G, v)               // v becomes part of tree
            if (!marked[w]) scan(G, w)               // w becomes part of tree
        }
    }

    // add all edges e incident to v onto pq if the other endpoint has not yet been scanned
    private fun scan(G: EdgeWeightedGraph, v: Int) {
        //assert(!marked[v])
        marked[v] = true
        for (e in G.adj(v))
            if (!marked[e.other(v)]) pq.insert(e)
    }

    // check optimality conditions (takes time proportional to E V lg* V)
    private fun check(G: EdgeWeightedGraph): Boolean {

        // check weight
        var totalWeight = 0.0
        for (e in edges)
            totalWeight += e.weight
        if (Math.abs(totalWeight - weight) > FLOATING_POINT_EPSILON) {
            System.err.printf("Weight of edges does not equal weight: %f vs. %f\n", totalWeight, weight)
            return false
        }

        // check that it is acyclic
        var uf = UF(G.V)
        for (e in edges) {
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
        for (e in edges) {

            // all edges in MST except e
            uf = UF(G.V)
            for (f in edges) {
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
         * Unit tests the `LazyPrimMST` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = EdgeWeightedGraph(`in`)
            val mst = LazyPrimMST(G)
            for (e in mst.edges)
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