/******************************************************************************
 * Compilation:  javac BipartiteMatching.java
 * Execution:    java BipartiteMatching V1 V2 E
 * Dependencies: BipartiteX.kt
 *
 * Find a maximum cardinality matching (and minimum cardinality vertex cover)
 * in a bipartite graph using the alternating path algorithm.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `BipartiteMatching` class represents a data type for computing a
 * *maximum (cardinality) matching* and a
 * *minimum (cardinality) vertex cover* in a bipartite graph.
 * A *bipartite graph* in a graph whose vertices can be partitioned
 * into two disjoint sets such that every edge has one endpoint in either set.
 * A *matching* in a graph is a subset of its edges with no common
 * vertices. A *maximum matching* is a matching with the maximum number
 * of edges.
 * A *perfect matching* is a matching which matches all vertices in the graph.
 * A *vertex cover* in a graph is a subset of its vertices such that
 * every edge is incident to at least one vertex. A *minimum vertex cover*
 * is a vertex cover with the minimum number of vertices.
 * By Konig's theorem, in any biparite
 * graph, the maximum number of edges in matching equals the minimum number
 * of vertices in a vertex cover.
 * The maximum matching problem in *nonbipartite* graphs is
 * also important, but all known algorithms for this more general problem
 * are substantially more complicated.
 *
 *
 * This implementation uses the *alternating path algorithm*.
 * It is equivalent to reducing to the maximum flow problem and running
 * the augmenting path algorithm on the resulting flow network, but it
 * does so with less overhead.
 * The order of growth of the running time in the worst case is
 * (*E* + *V*) *V*,
 * where *E* is the number of edges and *V* is the number
 * of vertices in the graph. It uses extra space (not including the graph)
 * proportional to *V*.
 *
 *
 * See also [HopcroftKarp], which solves the problem in  O(*E* sqrt(*V*))
 * using the Hopcroft-Karp algorithm and
 * [BipartiteMatchingToMaxflow](https://algs4.cs.princeton.edu/65reductions/BipartiteMatchingToMaxflow.java.html),
 * which solves the problem in O(*E V*) time via a reduction to maxflow.
 *
 *
 * For additional documentation, see
 * [Section 6.5](https://algs4.cs.princeton.edu/65reductions)
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class BipartiteMatching
/**
 * Determines a maximum matching (and a minimum vertex cover)
 * in a bipartite graph.
 *
 * @param  G the bipartite graph
 * @throws IllegalArgumentException if `G` is not bipartite
 */
(G: Graph) {
    private val V: Int = G.V                // number of vertices in the graph
    private val bipartition: BipartiteX = BipartiteX(G)      // the bipartition
    var cardinality: Int = 0             // cardinality of current matching
        private set
    private val mate = IntArray(V) { UNMATCHED }                 // mate[v] =  w if v-w is an edge in current matching
    //         = -1 if v is not in current matching
    private val inMinVertexCover: BooleanArray  // inMinVertexCover[v] = true iff v is in min vertex cover
    private lateinit var marked: BooleanArray            // marked[v] = true iff v is reachable via alternating path
    private lateinit var edgeTo: IntArray                // edgeTo[v] = w if v-w is last edge on path to w

    /**
     * Returns true if the graph contains a perfect matching.
     * That is, the number of edges in a maximum matching is equal to one half
     * of the number of vertices in the graph (so that every vertex is matched).
     *
     * @return `true` if the graph contains a perfect matching;
     * `false` otherwise
     */
    val isPerfect: Boolean
        get() = cardinality * 2 == V

    init {
        if (!bipartition.isBipartite) throw IllegalArgumentException("graph is not bipartite")

        // alternating path algorithm
        while (hasAugmentingPath(G)) {
            // find one endpoint t in alternating path
            var t = -1
            for (v in 0 until G.V)
                if (!isMatched(v) && edgeTo[v] != -1) {
                    t = v
                    break
                }

            // update the matching according to alternating path in edgeTo[] array
            var v = t
            while (v != -1) {
                val w = edgeTo[v]
                mate[v] = w
                mate[w] = v
                v = edgeTo[edgeTo[v]]
            }
            cardinality++
        }

        // find min vertex cover from marked[] array
        inMinVertexCover = BooleanArray(V)
        for (v in 0 until V) {
            if (bipartition.color(v) && !marked[v]) inMinVertexCover[v] = true
            if (!bipartition.color(v) && marked[v]) inMinVertexCover[v] = true
        }

        assert(certifySolution(G))
    }

    /*
     * is there an augmenting path?
     *   - if so, upon termination adj[] contains the level graph;
     *   - if not, upon termination marked[] specifies those vertices reachable via an alternating
     *     path from one side of the bipartition
     *
     * an alternating path is a path whose edges belong alternately to the matching and not
     * to the matching
     *
     * an augmenting path is an alternating path that starts and ends at unmatched vertices
     *
     * this implementation finds a shortest augmenting path (fewest number of edges), though there
     * is no particular advantage to do so here
     */
    private fun hasAugmentingPath(G: Graph): Boolean {
        marked = BooleanArray(V)
        edgeTo = IntArray(V){-1}

        // breadth-first search (starting from all unmatched vertices on one side of bipartition)
        val queue = nnQueue<Int>()
        for (v in 0 until V) {
            if (bipartition.color(v) && !isMatched(v)) {
                queue.enqueue(v)
                marked[v] = true
            }
        }

        // run BFS, stopping as soon as an alternating path is found
        while (!queue.isEmpty) {
            val v = queue.dequeue()
            for (w in G.adj(v))

                // either (1) forward edge not in matching or (2) backward edge in matching
                if (isResidualGraphEdge(v, w) && !marked[w]) {
                    edgeTo[w] = v
                    marked[w] = true
                    if (!isMatched(w)) return true
                    queue.enqueue(w)
                }
        }
        return false
    }

    // is the edge v-w a forward edge not in the matching or a reverse edge in the matching?
    private fun isResidualGraphEdge(v: Int, w: Int): Boolean {
        if (mate[v] != w && bipartition.color(v)) return true
        return mate[v] == w && !bipartition.color(v)
    }

    /**
     * Returns the vertex to which the specified vertex is matched in
     * the maximum matching computed by the algorithm.
     *
     * @param  v the vertex
     * @return the vertex to which vertex `v` is matched in the
     * maximum matching; `-1` if the vertex is not matched
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun mate(v: Int): Int {
        validate(v)
        return mate[v]
    }

    /**
     * Returns true if the specified vertex is matched in the maximum matching
     * computed by the algorithm.
     *
     * @param  v the vertex
     * @return `true` if vertex `v` is matched in maximum matching;
     * `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun isMatched(v: Int): Boolean {
        validate(v)
        return mate[v] != UNMATCHED
    }

    /**
     * Returns true if the specified vertex is in the minimum vertex cover
     * computed by the algorithm.
     *
     * @param  v the vertex
     * @return `true` if vertex `v` is in the minimum vertex cover;
     * `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun inMinVertexCover(v: Int): Boolean {
        validate(v)
        return inMinVertexCover[v]
    }

    private fun validate(v: Int) {
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    // check that mate[] and inVertexCover[] define a max matching and min vertex cover, respectively
    private fun certifySolution(G: Graph): Boolean {
        // check that mate(v) = w iff mate(w) = v
        for (v in 0 until V) {
            if (mate(v) == -1) continue
            if (mate(mate(v)) != v)
                return false
        }

        // check that size() is consistent with mate()
        var matchedVertices = 0
        for (v in 0 until V)
            if (mate(v) != -1)
                matchedVertices++
        if (2 * cardinality != matchedVertices)
            return false

        // check that size() is consistent with minVertexCover()
        var sizeOfMinVertexCover = 0
        for (v in 0 until V)
            if (inMinVertexCover(v))
                sizeOfMinVertexCover++
        if (cardinality != sizeOfMinVertexCover)
            return false

        // check that mate() uses each vertex at most once
        val isMatched = BooleanArray(V)
        for (v in 0 until V) {
            val w = mate[v]
            if (w == -1) continue
            if (v == w) return false
            if (v >= w) continue
            if (isMatched[v] || isMatched[w]) return false
            isMatched[v] = true
            isMatched[w] = true
        }

        // check that mate() uses only edges that appear in the graph
        for (v in 0 until V) {
            if (mate(v) == -1) continue
            var isEdge = false
            for (w in G.adj(v))
                if (mate(v) == w)
                    isEdge = true
            if (!isEdge) return false
        }

        // check that inMinVertexCover() is a vertex cover
        for (v in 0 until V)
            for (w in G.adj(v))
                if (!inMinVertexCover(v) && !inMinVertexCover(w)) return false
        return true
    }

    companion object {
        private const val UNMATCHED = -1

        /**
         * Unit tests the `HopcroftKarp` data type.
         * Takes three command-line arguments `V1`, `V2`, and `E`;
         * creates a random bipartite graph with `V1` + `V2` vertices
         * and `E` edges; computes a maximum matching and minimum vertex cover;
         * and prints the results.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val V1 = Integer.parseInt(args[0])
            val V2 = Integer.parseInt(args[1])
            val E = Integer.parseInt(args[2])
            val G = GraphGenerator.bipartite(V1, V2, E)

            if (G.V < 1000) StdOut.println(G)
            val matching = BipartiteMatching(G)

            // print maximum matching
            StdOut.printf("Number of edges in max matching        = %d\n", matching.cardinality)
            StdOut.printf("Number of vertices in min vertex cover = %d\n", matching.cardinality)
            StdOut.printf("Graph has a perfect matching           = %b\n", matching.isPerfect)
            StdOut.println()

            if (G.V >= 1000) return

            StdOut.print("Max matching: ")
            for (v in 0 until G.V) {
                val w = matching.mate(v)
                if (matching.isMatched(v) && v < w)
                // print each edge only once
                    StdOut.print("$v-$w ")
            }
            StdOut.println()

            // print minimum vertex cover
            StdOut.print("Min vertex cover: ")
            for (v in 0 until G.V)
                if (matching.inMinVertexCover(v))
                    StdOut.print("$v ")
            StdOut.println()
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