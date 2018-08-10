/******************************************************************************
 * Compilation:  javac DijkstraUndirectedSP.java
 * Execution:    java DijkstraUndirectedSP input.txt s
 * Dependencies: EdgeWeightedGraph.kt IndexMinPQ.kt Stack.kt Edge.kt
 * Data files:   https://algs4.cs.princeton.edu/43mst/tinyEWG.txt
 * https://algs4.cs.princeton.edu/43mst/mediumEWG.txt
 * https://algs4.cs.princeton.edu/43mst/largeEWG.txt
 *
 * Dijkstra's algorithm. Computes the shortest path tree.
 * Assumes all weights are nonnegative.
 *
 * % java DijkstraUndirectedSP tinyEWG.txt 6
 * 6 to 0 (0.58)  6-0 0.58000
 * 6 to 1 (0.76)  6-2 0.40000   1-2 0.36000
 * 6 to 2 (0.40)  6-2 0.40000
 * 6 to 3 (0.52)  3-6 0.52000
 * 6 to 4 (0.93)  6-4 0.93000
 * 6 to 5 (1.02)  6-2 0.40000   2-7 0.34000   5-7 0.28000
 * 6 to 6 (0.00)
 * 6 to 7 (0.74)  6-2 0.40000   2-7 0.34000
 *
 * % java DijkstraUndirectedSP mediumEWG.txt 0
 * 0 to 0 (0.00)
 * 0 to 1 (0.71)  0-44 0.06471   44-93  0.06793  ...   1-107 0.07484
 * 0 to 2 (0.65)  0-44 0.06471   44-231 0.10384  ...   2-42  0.11456
 * 0 to 3 (0.46)  0-97 0.07705   97-248 0.08598  ...   3-45  0.11902
 * ...
 *
 * % java DijkstraUndirectedSP largeEWG.txt 0
 * 0 to 0 (0.00)
 * 0 to 1 (0.78)  0-460790 0.00190  460790-696678 0.00173   ...   1-826350 0.00191
 * 0 to 2 (0.61)  0-15786  0.00130  15786-53370   0.00113   ...   2-793420 0.00040
 * 0 to 3 (0.31)  0-460790 0.00190  460790-752483 0.00194   ...   3-698373 0.00172
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DijkstraUndirectedSP` class represents a data type for solving
 * the single-source shortest paths problem in edge-weighted graphs
 * where the edge weights are nonnegative.
 *
 *
 * This implementation uses Dijkstra's algorithm with a binary heap.
 * The constructor takes time proportional to *E* log *V*,
 * where *V* is the number of vertices and *E* is the number of edges.
 * Each call to `distTo(int)` and `hasPathTo(int)` takes constant time;
 * each call to `pathTo(int)` takes time proportional to the number of
 * edges in the shortest path returned.
 *
 *
 * For additional documentation,
 * see [Section 4.4](https://algs4.cs.princeton.edu/44sp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 * See [DijkstraSP] for a version on edge-weighted digraphs.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Nate Liu
 * @author Jingyi Cheng
 *
 */
class DijkstraUndirectedSP
/**
 * Computes a shortest-paths tree from the source vertex `s` to every
 * other vertex in the edge-weighted graph `G`.
 *
 * @param  G the edge-weighted digraph
 * @param  s the source vertex
 * @throws IllegalArgumentException if an edge weight is negative
 * @throws IllegalArgumentException unless `0 <= s < V`
 */
(G: EdgeWeightedGraph, s: Int) {
    private val distTo = Array(G.V) {Double.POSITIVE_INFINITY}         // distTo[v] = distance  of shortest s->v path
    private val edgeTo: Array<Edge?> = arrayOfNulls(G.V)           // edgeTo[v] = last edge on shortest s->v path
    private val pq = IndexMinPQ<Double>(G.V)    // priority queue of vertices

    init {
        for (e in G.edges()) {
            if (e.weight < 0)
                throw IllegalArgumentException("edge $e has negative weight")
        }
        validateVertex(s)
        distTo[s] = 0.0

        // relax vertices in order of distance from s
        pq.insert(s, distTo[s])
        while (!pq.isEmpty) {
            val v = pq.delMin()
            for (e in G.adj(v))
                relax(e, v)
        }

        // check optimality conditions
        assert(check(G, s))
    }

    // relax edge e and update pq if changed
    private fun relax(e: Edge, v: Int) {
        val w = e.other(v)
        if (distTo[w] > distTo[v] + e.weight) {
            distTo[w] = distTo[v] + e.weight
            edgeTo[w] = e
            if (pq.contains(w))
                pq.decreaseKey(w, distTo[w])
            else
                pq.insert(w, distTo[w])
        }
    }

    /**
     * Returns the length of a shortest path between the source vertex `s` and
     * vertex `v`.
     *
     * @param  v the destination vertex
     * @return the length of a shortest path between the source vertex `s` and
     * the vertex `v`; `Double.POSITIVE_INFINITY` if no such path
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun distTo(v: Int): Double {
        validateVertex(v)
        return distTo[v]
    }

    /**
     * Returns true if there is a path between the source vertex `s` and
     * vertex `v`.
     *
     * @param  v the destination vertex
     * @return `true` if there is a path between the source vertex
     * `s` to vertex `v`; `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun hasPathTo(v: Int): Boolean {
        validateVertex(v)
        return distTo[v] < Double.POSITIVE_INFINITY
    }

    /**
     * Returns a shortest path between the source vertex `s` and vertex `v`.
     *
     * @param  v the destination vertex
     * @return a shortest path between the source vertex `s` and vertex `v`;
     * `null` if no such path
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun pathTo(v: Int): Iterable<Edge>? {
        validateVertex(v)
        if (!hasPathTo(v)) return null
        val path = nnStack<Edge>()
        var x = v
        var e: Edge? = edgeTo[v]
        while (e != null) {
            path.push(e)
            x = e.other(x)
            e = edgeTo[x]
        }
        return path
    }

    // check optimality conditions:
    // (i) for all edges e = v-w:            distTo[w] <= distTo[v] + e.weight()
    // (ii) for all edge e = v-w on the SPT: distTo[w] == distTo[v] + e.weight()
    private fun check(G: EdgeWeightedGraph, s: Int): Boolean {

        // check that edge weights are non-negative
        for (e in G.edges()) {
            if (e.weight < 0) {
                System.err.println("negative edge weight detected")
                return false
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent")
            return false
        }
        for (v in 0 until G.V) {
            if (v == s) continue
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent")
                return false
            }
        }

        // check that all edges e = v-w satisfy distTo[w] <= distTo[v] + e.weight()
        for (v in 0 until G.V) {
            for (e in G.adj(v)) {
                val w = e.other(v)
                if (distTo[v] + e.weight < distTo[w]) {
                    System.err.println("edge $e not relaxed")
                    return false
                }
            }
        }

        // check that all edges e = v-w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (w in 0 until G.V) {
            val e = edgeTo[w] ?: break
            if (w != e.either && w != e.other(e.either)) return false
            val v = e.other(w)
            if (distTo[v] + e.weight != distTo[w]) {
                System.err.println("edge $e on shortest path not tight")
                return false
            }
        }
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = distTo.size
        if (v < 0 || v >= V)
            throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {

        /**
         * Unit tests the `DijkstraUndirectedSP` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = EdgeWeightedGraph(`in`)
            val s = Integer.parseInt(args[1])

            // compute shortest paths
            val sp = DijkstraUndirectedSP(G, s)

            // print shortest path
            for (t in 0 until G.V) {
                if (sp.hasPathTo(t)) {
                    StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t))
                    for (e in sp.pathTo(t)!!) {
                        StdOut.print("$e   ")
                    }
                    StdOut.println()
                } else {
                    StdOut.println("$s to $t         no path")
                }
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
