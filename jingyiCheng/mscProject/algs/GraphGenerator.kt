/******************************************************************************
 * Compilation:  javac GraphGenerator.java
 * Execution:    java GraphGenerator V E
 * Dependencies: Graph.kt
 *
 * A graph generator.
 *
 * For many more graph generators, see
 * http://networkx.github.io/documentation/latest/reference/generators.html
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `GraphGenerator` class provides static methods for creating
 * various graphs, including Erdos-Renyi random graphs, random bipartite
 * graphs, random k-regular graphs, and random rooted trees.
 *
 *
 * For additional documentation, see [Section 4.1](https://algs4.cs.princeton.edu/41graph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object GraphGenerator {
    private class Edge(v: Int, w: Int) : Comparable<Edge> {
        private var v: Int = 0
        private var w: Int = 0

        override fun compareTo(other: Edge): Int {
            if (this.v < other.v) return -1
            if (this.v > other.v) return +1
            if (this.w < other.w) return -1
            return if (this.w > other.w) +1 else 0
        }

        init {
            if (v < w) {
                this.v = v
                this.w = w
            } else {
                this.v = w
                this.w = v
            }
        }
    }

    /**
     * Returns a random simple graph containing `V` vertices and `E` edges.
     * @param V the number of vertices
     * @param E the number of vertices
     * @return a random simple graph on `V` vertices, containing a total
     * of `E` edges
     * @throws IllegalArgumentException if no such simple graph exists
     */
    fun simple(V: Int, E: Int): Graph {
        if (E > V.toLong() * (V - 1) / 2) throw IllegalArgumentException("Too many edges")
        if (E < 0) throw IllegalArgumentException("Too few edges")
        val G = Graph(V)
        val set = SET<Edge>()
        while (G.E < E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val e = Edge(v, w)
            if (v != w && !set.contains(e)) {
                set.add(e)
                G.addEdge(v, w)
            }
        }
        return G
    }

    /**
     * Returns a random simple graph on `V` vertices, with an
     * edge between any two vertices with probability `p`. This is sometimes
     * referred to as the Erdos-Renyi random graph model.
     * @param V the number of vertices
     * @param p the probability of choosing an edge
     * @return a random simple graph on `V` vertices, with an edge between
     * any two vertices with probability `p`
     * @throws IllegalArgumentException if probability is not between 0 and 1
     */
    fun simple(V: Int, p: Double): Graph {
        if (p < 0.0 || p > 1.0) throw IllegalArgumentException("Probability must be between 0 and 1")
        val G = Graph(V)
        for (v in 0 until V)
            for (w in v + 1 until V)
                if (StdRandom.bernoulli(p))
                    G.addEdge(v, w)
        return G
    }

    /**
     * Returns the complete graph on `V` vertices.
     * @param V the number of vertices
     * @return the complete graph on `V` vertices
     */
    fun complete(V: Int) = simple(V, 1.0)

    /**
     * Returns a complete bipartite graph on `V1` and `V2` vertices.
     * @param V1 the number of vertices in one partition
     * @param V2 the number of vertices in the other partition
     * @return a complete bipartite graph on `V1` and `V2` vertices
     * @throws IllegalArgumentException if probability is not between 0 and 1
     */
    fun completeBipartite(V1: Int, V2: Int) = bipartite(V1, V2, V1 * V2)

    /**
     * Returns a random simple bipartite graph on `V1` and `V2` vertices
     * with `E` edges.
     * @param V1 the number of vertices in one partition
     * @param V2 the number of vertices in the other partition
     * @param E the number of edges
     * @return a random simple bipartite graph on `V1` and `V2` vertices,
     * containing a total of `E` edges
     * @throws IllegalArgumentException if no such simple bipartite graph exists
     */
    fun bipartite(V1: Int, V2: Int, E: Int): Graph {
        if (E > V1.toLong() * V2) throw IllegalArgumentException("Too many edges")
        if (E < 0) throw IllegalArgumentException("Too few edges")
        val G = Graph(V1 + V2)

        val vertices = IntArray(V1 + V2) { it }
        StdRandom.shuffle(vertices)

        val set = SET<Edge>()
        while (G.E < E) {
            val i = StdRandom.uniform(V1)
            val j = V1 + StdRandom.uniform(V2)
            val e = Edge(vertices[i], vertices[j])
            if (!set.contains(e)) {
                set.add(e)
                G.addEdge(vertices[i], vertices[j])
            }
        }
        return G
    }

    /**
     * Returns a random simple bipartite graph on `V1` and `V2` vertices,
     * containing each possible edge with probability `p`.
     * @param V1 the number of vertices in one partition
     * @param V2 the number of vertices in the other partition
     * @param p the probability that the graph contains an edge with one endpoint in either side
     * @return a random simple bipartite graph on `V1` and `V2` vertices,
     * containing each possible edge with probability `p`
     * @throws IllegalArgumentException if probability is not between 0 and 1
     */
    fun bipartite(V1: Int, V2: Int, p: Double): Graph {
        if (p < 0.0 || p > 1.0) throw IllegalArgumentException("Probability must be between 0 and 1")
        val vertices = IntArray(V1 + V2) { it }
        StdRandom.shuffle(vertices)
        val G = Graph(V1 + V2)
        for (i in 0 until V1)
            for (j in 0 until V2)
                if (StdRandom.bernoulli(p))
                    G.addEdge(vertices[i], vertices[V1 + j])
        return G
    }

    /**
     * Returns a path graph on `V` vertices.
     * @param V the number of vertices in the path
     * @return a path graph on `V` vertices
     */
    fun path(V: Int): Graph {
        val G = Graph(V)
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)
        for (i in 0 until V - 1)
            G.addEdge(vertices[i], vertices[i + 1])
        return G
    }

    /**
     * Returns a complete binary tree graph on `V` vertices.
     * @param V the number of vertices in the binary tree
     * @return a complete binary tree graph on `V` vertices
     */
    fun binaryTree(V: Int): Graph {
        val G = Graph(V)
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)
        for (i in 1 until V)
            G.addEdge(vertices[i], vertices[(i - 1) / 2])
        return G
    }

    /**
     * Returns a cycle graph on `V` vertices.
     * @param V the number of vertices in the cycle
     * @return a cycle graph on `V` vertices
     */
    fun cycle(V: Int): Graph {
        val G = Graph(V)
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)
        for (i in 0 until V - 1)
            G.addEdge(vertices[i], vertices[i + 1])
        G.addEdge(vertices[V - 1], vertices[0])
        return G
    }

    /**
     * Returns an Eulerian cycle graph on `V` vertices.
     *
     * @param  V the number of vertices in the cycle
     * @param  E the number of edges in the cycle
     * @return a graph that is an Eulerian cycle on `V` vertices
     * and `E` edges
     * @throws IllegalArgumentException if either `V <= 0` or `E <= 0`
     */
    fun eulerianCycle(V: Int, E: Int): Graph {
        if (E <= 0) throw IllegalArgumentException("An Eulerian cycle must have at least one edge")
        if (V <= 0) throw IllegalArgumentException("An Eulerian cycle must have at least one vertex")
        val G = Graph(V)
        val vertices = IntArray(E) { StdRandom.uniform(V) }
        for (i in 0 until E - 1)
            G.addEdge(vertices[i], vertices[i + 1])
        G.addEdge(vertices[E - 1], vertices[0])
        return G
    }

    /**
     * Returns an Eulerian path graph on `V` vertices.
     *
     * @param  V the number of vertices in the path
     * @param  E the number of edges in the path
     * @return a graph that is an Eulerian path on `V` vertices
     * and `E` edges
     * @throws IllegalArgumentException if either `V <= 0` or `E < 0`
     */
    fun eulerianPath(V: Int, E: Int): Graph {
        if (E < 0) throw IllegalArgumentException("negative number of edges")
        if (V <= 0) throw IllegalArgumentException("An Eulerian path must have at least one vertex")
        val G = Graph(V)
        val vertices = IntArray(E + 1) { StdRandom.uniform(V) }
        for (i in 0 until E)
            G.addEdge(vertices[i], vertices[i + 1])
        return G
    }

    /**
     * Returns a wheel graph on `V` vertices.
     * @param V the number of vertices in the wheel
     * @return a wheel graph on `V` vertices: a single vertex connected to
     * every vertex in a cycle on `V-1` vertices
     */
    fun wheel(V: Int): Graph {
        if (V <= 1) throw IllegalArgumentException("Number of vertices must be at least 2")
        val G = Graph(V)
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)

        // simple cycle on V-1 vertices
        for (i in 1 until V - 1)
            G.addEdge(vertices[i], vertices[i + 1])
        G.addEdge(vertices[V - 1], vertices[1])

        // connect vertices[0] to every vertex on cycle
        for (i in 1 until V)
            G.addEdge(vertices[0], vertices[i])
        return G
    }

    /**
     * Returns a star graph on `V` vertices.
     * @param V the number of vertices in the star
     * @return a star graph on `V` vertices: a single vertex connected to
     * every other vertex
     */
    fun star(V: Int): Graph {
        if (V <= 0) throw IllegalArgumentException("Number of vertices must be at least 1")
        val G = Graph(V)
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)

        // connect vertices[0] to every other vertex
        for (i in 1 until V)
            G.addEdge(vertices[0], vertices[i])
        return G
    }

    /**
     * Returns a uniformly random `k`-regular graph on `V` vertices
     * (not necessarily simple). The graph is simple with probability only about e^(-k^2/4),
     * which is tiny when k = 14.
     *
     * @param V the number of vertices in the graph
     * @param k degree of each vertex
     * @return a uniformly random `k`-regular graph on `V` vertices.
     */
    fun regular(V: Int, k: Int): Graph {
        if (V * k % 2 != 0) throw IllegalArgumentException("Number of vertices * k must be even")
        val G = Graph(V)

        // create k copies of each vertex
        val vertices = IntArray(V * k)
        for (v in 0 until V)
            for (j in 0 until k)
                vertices[v + V * j] = v

        // pick a random perfect matching
        StdRandom.shuffle(vertices)
        for (i in 0 until V * k / 2)
            G.addEdge(vertices[2 * i], vertices[2 * i + 1])
        return G
    }

    // http://www.proofwiki.org/wiki/Labeled_Tree_from_Prüfer_Sequence
    // http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.36.6484&rep=rep1&type=pdf
    /**
     * Returns a uniformly random tree on `V` vertices.
     * This algorithm uses a Prufer sequence and takes time proportional to *V log V*.
     * @param V the number of vertices in the tree
     * @return a uniformly random tree on `V` vertices
     */
    fun tree(V: Int): Graph {
        val G = Graph(V)

        // special case
        if (V == 1) return G

        // Cayley's theorem: there are V^(V-2) labeled trees on V vertices
        // Prufer sequence: sequence of V-2 values between 0 and V-1
        // Prufer's proof of Cayley's theorem: Prufer sequences are in 1-1
        // with labeled trees on V vertices
        val prufer = IntArray(V - 2) { StdRandom.uniform(V) }

        // degree of vertex v = 1 + number of times it appers in Prufer sequence
        val degree = IntArray(V) { 1 }
        for (i in 0 until V - 2)
            degree[prufer[i]]++

        // pq contains all vertices of degree 1
        val pq = MinPQ<Int>()
        for (v in 0 until V)
            if (degree[v] == 1) pq.insert(v)

        // repeatedly delMin() degree 1 vertex that has the minimum index
        for (i in 0 until V - 2) {
            val v = pq.delMin()
            G.addEdge(v, prufer[i])
            degree[v]--
            degree[prufer[i]]--
            if (degree[prufer[i]] == 1) pq.insert(prufer[i])
        }
        G.addEdge(pq.delMin(), pq.delMin())
        return G
    }

    /**
     * Unit tests the `GraphGenerator` library.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val V = Integer.parseInt(args[0])
        val E = Integer.parseInt(args[1])
        val V1 = V / 2
        val V2 = V - V1

        StdOut.println("complete graph")
        StdOut.println(complete(V))
        StdOut.println()

        StdOut.println("simple")
        StdOut.println(simple(V, E))
        StdOut.println()

        StdOut.println("Erdos-Renyi")
        val p = E / (V * (V - 1) / 2.0)
        StdOut.println(simple(V, p))
        StdOut.println()

        StdOut.println("complete bipartite")
        StdOut.println(completeBipartite(V1, V2))
        StdOut.println()

        StdOut.println("bipartite")
        StdOut.println(bipartite(V1, V2, E))
        StdOut.println()

        StdOut.println("Erdos Renyi bipartite")
        val q = E / (V1 * V2)
        StdOut.println(bipartite(V1, V2, q))
        StdOut.println()

        StdOut.println("path")
        StdOut.println(path(V))
        StdOut.println()

        StdOut.println("cycle")
        StdOut.println(cycle(V))
        StdOut.println()

        StdOut.println("binary tree")
        StdOut.println(binaryTree(V))
        StdOut.println()

        StdOut.println("tree")
        StdOut.println(tree(V))
        StdOut.println()

        StdOut.println("4-regular")
        StdOut.println(regular(V, 4))
        StdOut.println()

        StdOut.println("star")
        StdOut.println(star(V))
        StdOut.println()

        StdOut.println("wheel")
        StdOut.println(wheel(V))
        StdOut.println()
    }
}// this class cannot be instantiated

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