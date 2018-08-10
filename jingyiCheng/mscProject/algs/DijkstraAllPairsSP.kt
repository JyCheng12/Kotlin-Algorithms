/******************************************************************************
 * Compilation:  javac DijkstraAllPairsSP.java
 * Execution:    none
 * Dependencies: EdgeWeightedDigraph.kt Dijkstra.kt
 *
 * Dijkstra's algorithm run from each vertex.
 * Takes time proportional to E V log V and space proportional to EV.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DijkstraAllPairsSP` class represents a data type for solving the
 * all-pairs shortest paths problem in edge-weighted digraphs
 * where the edge weights are nonnegative.
 *
 *
 * This implementation runs Dijkstra's algorithm from each vertex.
 * The constructor takes time proportional to *V* (*E* log *V*)
 * and uses space proprtional to *V*<sup>2</sup>,
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the `dist()` and `hasPath()` methods take
 * constant time and the `path()` method takes time proportional to the
 * number of edges in the shortest path returned.
 *
 *
 * For additional documentation,
 * see [Section 4.4](https://algs4.cs.princeton.edu/44sp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 */
class DijkstraAllPairsSP
/**
 * Computes a shortest paths tree from each vertex to to every other vertex in
 * the edge-weighted digraph `G`.
 * @param G the edge-weighted digraph
 * @throws IllegalArgumentException if an edge weight is negative
 * @throws IllegalArgumentException unless `0 <= s < V`
 */
(G: EdgeWeightedDigraph) {
    private val all: Array<DijkstraSP> = Array(G.V) { DijkstraSP(G, it) }

    /**
     * Returns a shortest path from vertex `s` to vertex `t`.
     * @param  s the source vertex
     * @param  t the destination vertex
     * @return a shortest path from vertex `s` to vertex `t`
     * as an iterable of edges, and `null` if no such path
     * @throws IllegalArgumentException unless `0 <= s < V`
     * @throws IllegalArgumentException unless `0 <= t < V`
     */
    fun path(s: Int, t: Int): Iterable<DirectedEdge>? {
        validateVertex(s)
        validateVertex(t)
        return all[s].pathTo(t)
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
        return dist(s, t) < Double.POSITIVE_INFINITY
    }

    /**
     * Returns the length of a shortest path from vertex `s` to vertex `t`.
     * @param  s the source vertex
     * @param  t the destination vertex
     * @return the length of a shortest path from vertex `s` to vertex `t`;
     * `Double.POSITIVE_INFINITY` if no such path
     * @throws IllegalArgumentException unless `0 <= s < V`
     * @throws IllegalArgumentException unless `0 <= t < V`
     */
    fun dist(s: Int, t: Int): Double {
        validateVertex(s)
        validateVertex(t)
        return all[s].distTo(t)
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = all.size
        if (v < 0 || v >= V)
            throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
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
