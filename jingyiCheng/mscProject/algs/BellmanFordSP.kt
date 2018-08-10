/******************************************************************************
 * Compilation:  javac BellmanFordSP.java
 * Execution:    java BellmanFordSP filename.txt s
 * Dependencies: EdgeWeightedDigraph.kt DirectedEdge.kt Queue.kt
 * EdgeWeightedDirectedCycle.kt
 * Data files:   https://algs4.cs.princeton.edu/44sp/tinyEWDn.txt
 * https://algs4.cs.princeton.edu/44sp/mediumEWDnc.txt
 *
 * Bellman-Ford shortest path algorithm. Computes the shortest path tree in
 * edge-weighted digraph G from vertex s, or finds a negative cost cycle
 * reachable from s.
 *
 * % java BellmanFordSP tinyEWDn.txt 0
 * 0 to 0 ( 0.00)
 * 0 to 1 ( 0.93)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   6->4 -1.25   4->5  0.35   5->1  0.32
 * 0 to 2 ( 0.26)  0->2  0.26
 * 0 to 3 ( 0.99)  0->2  0.26   2->7  0.34   7->3  0.39
 * 0 to 4 ( 0.26)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   6->4 -1.25
 * 0 to 5 ( 0.61)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52   6->4 -1.25   4->5  0.35
 * 0 to 6 ( 1.51)  0->2  0.26   2->7  0.34   7->3  0.39   3->6  0.52
 * 0 to 7 ( 0.60)  0->2  0.26   2->7  0.34
 *
 * % java BellmanFordSP tinyEWDnc.txt 0
 * 4->5  0.35
 * 5->4 -0.66
 *
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `BellmanFordSP` class represents a data type for solving the
 * single-source shortest paths problem in edge-weighted digraphs with
 * no negative cycles.
 * The edge weights can be positive, negative, or zero.
 * This class finds either a shortest path from the source vertex *s*
 * to every other vertex or a negative cycle reachable from the source vertex.
 *
 *
 * This implementation uses the Bellman-Ford-Moore algorithm.
 * The constructor takes time proportional to *V* (*V* + *E*)
 * in the worst case, where *V* is the number of vertices and *E*
 * is the number of edges.
 * Each call to `distTo(int)` and `hasPathTo(int)`,
 * `hasNegativeCycle` takes constant time;
 * each call to `pathTo(int)` and `negativeCycle()`
 * takes time proportional to length of the path returned.
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
class BellmanFordSP
/**
 * Computes a shortest paths tree from `s` to every other vertex in
 * the edge-weighted digraph `G`.
 * @param G the acyclic digraph
 * @param s the source vertex
 * @throws IllegalArgumentException unless `0 <= s < V`
 */
(G: EdgeWeightedDigraph, s: Int) {
    private val distTo: DoubleArray = DoubleArray(G.V) { Double.NEGATIVE_INFINITY }               // distTo[v] = distance  of shortest s->v path
    private val edgeTo: Array<DirectedEdge?> = arrayOfNulls(G.V)        // edgeTo[v] = last edge on shortest s->v path
    private val onQueue: BooleanArray = BooleanArray(G.V)             // onQueue[v] = is v currently on the queue?
    private val queue: nnQueue<Int> = nnQueue()         // queue of vertices to relax
    private var cost: Int = 0                      // number of calls to relax()
    var cycle: Iterable<DirectedEdge>? = null  // negative cycle (or null if no such cycle)
        private set

    init {
        distTo[s] = 0.0

        // Bellman-Ford algorithm
        queue.enqueue(s)
        onQueue[s] = true
        while (!queue.isEmpty && !hasNegativeCycle()) {
            val v = queue.dequeue()
            onQueue[v] = false
            relax(G, v)
        }

        assert(check(G, s))
    }

    // relax vertex v and put other endpoints on queue if changed
    private fun relax(G: EdgeWeightedDigraph, v: Int) {
        for (e in G.adj(v)) {
            val w = e.to
            if (distTo[w] > distTo[v] + e.weight) {
                distTo[w] = distTo[v] + e.weight
                edgeTo[w] = e
                if (!onQueue[w]) {
                    queue.enqueue(w)
                    onQueue[w] = true
                }
            }
            if (cost++ % G.V == 0) {
                findNegativeCycle()
                if (hasNegativeCycle()) return   // found a negative cycle
            }
        }
    }

    /**
     * Is there a negative cycle reachable from the source vertex `s`?
     * @return `true` if there is a negative cycle reachable from the
     * source vertex `s`, and `false` otherwise
     */
    fun hasNegativeCycle() = cycle != null

    // by finding a cycle in predecessor graph
    private fun findNegativeCycle() {
        val V = edgeTo.size
        val spt = EdgeWeightedDigraph(V)
        for (v in 0 until V)
            if (edgeTo[v] != null)
                spt.addEdge(edgeTo[v]!!)

        val finder = EdgeWeightedDirectedCycle(spt)
        cycle = finder.cycle
    }

    /**
     * Returns the length of a shortest path from the source vertex `s` to vertex `v`.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex `s` to vertex `v`;
     * `Double.POSITIVE_INFINITY` if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     * from the source vertex `s`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun distTo(v: Int): Double {
        validateVertex(v)
        if (hasNegativeCycle()) throw UnsupportedOperationException("Negative cost cycle exists")
        return distTo[v]
    }

    /**
     * Is there a path from the source `s` to vertex `v`?
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
     * Returns a shortest path from the source `s` to vertex `v`.
     * @param  v the destination vertex
     * @return a shortest path from the source `s` to vertex `v`
     * as an iterable of edges, and `null` if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     * from the source vertex `s`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun pathTo(v: Int): Iterable<DirectedEdge>? {
        validateVertex(v)
        if (hasNegativeCycle()) throw UnsupportedOperationException("Negative cost cycle exists")
        if (!hasPathTo(v)) return null
        val path = nnStack<DirectedEdge>()
        var e: DirectedEdge? = edgeTo[v]
        while (e != null) {
            path.push(e)
            e = edgeTo[e.from]
        }
        return path
    }

    // check optimality conditions: either
    // (i) there exists a negative cycle reacheable from s
    //     or
    // (ii)  for all edges e = v->w:            distTo[w] <= distTo[v] + e.weight()
    // (ii') for all edges e = v->w on the SPT: distTo[w] == distTo[v] + e.weight()
    private fun check(G: EdgeWeightedDigraph, s: Int): Boolean {
        // has a negative cycle
        if (hasNegativeCycle()) {
            var weight = 0.0
            for (e in cycle!!)
                weight += e.weight
            if (weight >= 0.0) {
                System.err.println("error: weight of negative cycle = $weight")
                return false
            }
        } else {
            // check that distTo[v] and edgeTo[v] are consistent
            if (distTo[s] != 0.0 || edgeTo[s] != null) {
                System.err.println("distanceTo[s] and edgeTo[s] inconsistent")
                return false
            }
            for (v in 0 until G.V) {
                if (v == s) continue
                if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                    System.err.println("distTo[] and edgeTo[] inconsistent")
                    return false
                }
            }

            // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
            for (v in 0 until G.V)
                for (e in G.adj(v)) {
                    val w = e.to
                    if (distTo[v] + e.weight < distTo[w]) {
                        System.err.println("edge $e not relaxed")
                        return false
                    }
                }

            // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
            for (w in 0 until G.V) {
                val e = edgeTo[w] ?: break
                val v = e.from
                if (w != e.to) return false
                if (distTo[v] + e.weight != distTo[w]) {
                    System.err.println("edge $e on shortest path not tight")
                    return false
                }
            }
        }// no negative cycle reachable from source

        StdOut.println("Satisfies optimality conditions")
        StdOut.println()
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = distTo.size
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {

        /**
         * Unit tests the `BellmanFordSP` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val s = Integer.parseInt(args[1])
            val G = EdgeWeightedDigraph(`in`)
            val sp = BellmanFordSP(G, s)

            // print negative cycle
            if (sp.hasNegativeCycle()) {
                for (e in sp.cycle!!)
                    StdOut.println(e)
            } else {
                for (v in 0 until G.V)
                    if (sp.hasPathTo(v)) {
                        StdOut.printf("%d to %d (%5.2f)  ", s, v, sp.distTo(v))
                        for (e in sp.pathTo(v)!!)
                            StdOut.print("$e   ")
                        StdOut.println()
                    } else
                        StdOut.println("$s to $v           no path")
            }// print shortest paths
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
