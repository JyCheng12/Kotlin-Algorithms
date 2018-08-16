/******************************************************************************
 * Compilation:  javac DigraphGenerator.java
 * Execution:    java DigraphGenerator V E
 * Dependencies: Digraph.kt
 *
 * A digraph generator.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DigraphGenerator` class provides static methods for creating
 * various digraphs, including Erdos-Renyi random digraphs, random DAGs,
 * random rooted trees, random rooted DAGs, random tournaments, path digraphs,
 * cycle digraphs, and the complete digraph.
 *
 *
 * For additional documentation, see [Section 4.2](https://algs4.cs.princeton.edu/42digraph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object DigraphGenerator {
    private class Edge(private val v: Int, private val w: Int) : Comparable<Edge> {

        override fun compareTo(other: Edge): Int {
            if (this.v < other.v) return -1
            if (this.v > other.v) return +1
            if (this.w < other.w) return -1
            return if (this.w > other.w) +1 else 0
        }
    }

    /**
     * Returns a random simple digraph containing `V` vertices and `E` edges.
     * @param V the number of vertices
     * @param E the number of vertices
     * @return a random simple digraph on `V` vertices, containing a total
     * of `E` edges
     * @throws IllegalArgumentException if no such simple digraph exists
     */
    fun simple(V: Int, E: Int): Digraph {
        if (E > V.toLong() * (V - 1)) throw IllegalArgumentException("Too many edges")
        if (E < 0) throw IllegalArgumentException("Too few edges")
        val G = Digraph(V)
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
     * Returns a random simple digraph on `V` vertices, with an
     * edge between any two vertices with probability `p`. This is sometimes
     * referred to as the Erdos-Renyi random digraph model.
     * This implementations takes time propotional to V^2 (even if `p` is small).
     * @param V the number of vertices
     * @param p the probability of choosing an edge
     * @return a random simple digraph on `V` vertices, with an edge between
     * any two vertices with probability `p`
     * @throws IllegalArgumentException if probability is not between 0 and 1
     */
    fun simple(V: Int, p: Double): Digraph {
        if (p < 0.0 || p > 1.0) throw IllegalArgumentException("Probability must be between 0 and 1")
        val G = Digraph(V)
        for (v in 0 until V)
            for (w in 0 until V)
                if (v != w)
                    if (StdRandom.bernoulli(p))
                        G.addEdge(v, w)
        return G
    }

    /**
     * Returns the complete digraph on `V` vertices.
     * @param V the number of vertices
     * @return the complete digraph on `V` vertices
     */
    fun complete(V: Int) = simple(V, V * (V - 1))

    /**
     * Returns a random simple DAG containing `V` vertices and `E` edges.
     * Note: it is not uniformly selected at random among all such DAGs.
     * @param V the number of vertices
     * @param E the number of vertices
     * @return a random simple DAG on `V` vertices, containing a total
     * of `E` edges
     * @throws IllegalArgumentException if no such simple DAG exists
     */
    fun dag(V: Int, E: Int): Digraph {
        if (E > V.toLong() * (V - 1) / 2) throw IllegalArgumentException("Too many edges")
        if (E < 0) throw IllegalArgumentException("Too few edges")
        val G = Digraph(V)
        val set = SET<Edge>()
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)
        while (G.E < E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val e = Edge(v, w)
            if (v < w && !set.contains(e)) {
                set.add(e)
                G.addEdge(vertices[v], vertices[w])
            }
        }
        return G
    }

    // tournament
    /**
     * Returns a random tournament digraph on `V` vertices. A tournament digraph
     * is a DAG in which for every two vertices, there is one directed edge.
     * A tournament is an oriented complete graph.
     * @param V the number of vertices
     * @return a random tournament digraph on `V` vertices
     */
    fun tournament(V: Int): Digraph {
        val G = Digraph(V)
        for (v in 0 until G.V)
            for (w in v + 1 until G.V)
                if (StdRandom.bernoulli(0.5))
                    G.addEdge(v, w)
                else
                    G.addEdge(w, v)
        return G
    }

    /**
     * Returns a random rooted-in DAG on `V` vertices and `E` edges.
     * A rooted in-tree is a DAG in which there is a single vertex
     * reachable from every other vertex.
     * The DAG returned is not chosen uniformly at random among all such DAGs.
     * @param V the number of vertices
     * @param E the number of edges
     * @return a random rooted-in DAG on `V` vertices and `E` edges
     */
    fun rootedInDAG(V: Int, E: Int): Digraph {
        if (E > V.toLong() * (V - 1) / 2) throw IllegalArgumentException("Too many edges")
        if (E < V - 1) throw IllegalArgumentException("Too few edges")
        val G = Digraph(V)
        val set = SET<Edge>()

        // fix a topological order
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)

        // one edge pointing from each vertex, other than the root = vertices[V-1]
        for (v in 0 until V - 1) {
            val w = StdRandom.uniform(v + 1, V)
            val e = Edge(v, w)
            set.add(e)
            G.addEdge(vertices[v], vertices[w])
        }

        while (G.E < E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val e = Edge(v, w)
            if (v < w && !set.contains(e)) {
                set.add(e)
                G.addEdge(vertices[v], vertices[w])
            }
        }
        return G
    }

    /**
     * Returns a random rooted-out DAG on `V` vertices and `E` edges.
     * A rooted out-tree is a DAG in which every vertex is reachable from a
     * single vertex.
     * The DAG returned is not chosen uniformly at random among all such DAGs.
     * @param V the number of vertices
     * @param E the number of edges
     * @return a random rooted-out DAG on `V` vertices and `E` edges
     */
    fun rootedOutDAG(V: Int, E: Int): Digraph {
        if (E > V.toLong() * (V - 1) / 2) throw IllegalArgumentException("Too many edges")
        if (E < V - 1) throw IllegalArgumentException("Too few edges")
        val G = Digraph(V)
        val set = SET<Edge>()

        // fix a topological order
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)

        // one edge pointing from each vertex, other than the root = vertices[V-1]
        for (v in 0 until V - 1) {
            val w = StdRandom.uniform(v + 1, V)
            val e = Edge(w, v)
            set.add(e)
            G.addEdge(vertices[w], vertices[v])
        }

        while (G.E < E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val e = Edge(w, v)
            if (v < w && !set.contains(e)) {
                set.add(e)
                G.addEdge(vertices[w], vertices[v])
            }
        }
        return G
    }

    /**
     * Returns a random rooted-in tree on `V` vertices.
     * A rooted in-tree is an oriented tree in which there is a single vertex
     * reachable from every other vertex.
     * The tree returned is not chosen uniformly at random among all such trees.
     * @param V the number of vertices
     * @return a random rooted-in tree on `V` vertices
     */
    fun rootedInTree(V: Int) = rootedInDAG(V, V - 1)

    /**
     * Returns a random rooted-out tree on `V` vertices. A rooted out-tree
     * is an oriented tree in which each vertex is reachable from a single vertex.
     * It is also known as a *arborescence* or *branching*.
     * The tree returned is not chosen uniformly at random among all such trees.
     * @param V the number of vertices
     * @return a random rooted-out tree on `V` vertices
     */
    fun rootedOutTree(V: Int) = rootedOutDAG(V, V - 1)

    /**
     * Returns a path digraph on `V` vertices.
     * @param V the number of vertices in the path
     * @return a digraph that is a directed path on `V` vertices
     */
    fun path(V: Int): Digraph {
        val G = Digraph(V)
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)
        for (i in 0 until V - 1)
            G.addEdge(vertices[i], vertices[i + 1])
        return G
    }

    /**
     * Returns a complete binary tree digraph on `V` vertices.
     * @param V the number of vertices in the binary tree
     * @return a digraph that is a complete binary tree on `V` vertices
     */
    fun binaryTree(V: Int): Digraph {
        val G = Digraph(V)
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)
        for (i in 1 until V)
            G.addEdge(vertices[i], vertices[(i - 1) / 2])
        return G
    }

    /**
     * Returns a cycle digraph on `V` vertices.
     * @param V the number of vertices in the cycle
     * @return a digraph that is a directed cycle on `V` vertices
     */
    fun cycle(V: Int): Digraph {
        val G = Digraph(V)
        val vertices = IntArray(V) { it }
        StdRandom.shuffle(vertices)
        for (i in 0 until V - 1)
            G.addEdge(vertices[i], vertices[i + 1])
        G.addEdge(vertices[V - 1], vertices[0])
        return G
    }

    /**
     * Returns an Eulerian cycle digraph on `V` vertices.
     *
     * @param  V the number of vertices in the cycle
     * @param  E the number of edges in the cycle
     * @return a digraph that is a directed Eulerian cycle on `V` vertices
     * and `E` edges
     * @throws IllegalArgumentException if either `V <= 0` or `E <= 0`
     */
    fun eulerianCycle(V: Int, E: Int): Digraph {
        if (E <= 0) throw IllegalArgumentException("An Eulerian cycle must have at least one edge")
        if (V <= 0) throw IllegalArgumentException("An Eulerian cycle must have at least one vertex")
        val G = Digraph(V)
        val vertices = IntArray(E) { StdRandom.uniform(V) }
        for (i in 0 until E - 1)
            G.addEdge(vertices[i], vertices[i + 1])
        G.addEdge(vertices[E - 1], vertices[0])
        return G
    }

    /**
     * Returns an Eulerian path digraph on `V` vertices.
     *
     * @param  V the number of vertices in the path
     * @param  E the number of edges in the path
     * @return a digraph that is a directed Eulerian path on `V` vertices
     * and `E` edges
     * @throws IllegalArgumentException if either `V <= 0` or `E < 0`
     */
    fun eulerianPath(V: Int, E: Int): Digraph {
        if (E < 0) throw IllegalArgumentException("negative number of edges")
        if (V <= 0) throw IllegalArgumentException("An Eulerian path must have at least one vertex")
        val G = Digraph(V)
        val vertices = IntArray(E + 1) { StdRandom.uniform(V) }
        for (i in 0 until E)
            G.addEdge(vertices[i], vertices[i + 1])
        return G
    }

    /**
     * Returns a random simple digraph on `V` vertices, `E`
     * edges and (at least) `c` strong components. The vertices are randomly
     * assigned integer labels between `0` and `c-1` (corresponding to
     * strong components). Then, a strong component is creates among the vertices
     * with the same label. Next, random edges (either between two vertices with
     * the same labels or from a vetex with a smaller label to a vertex with a
     * larger label). The number of components will be equal to the number of
     * distinct labels that are assigned to vertices.
     *
     * @param V the number of vertices
     * @param E the number of edges
     * @param c the (maximum) number of strong components
     * @return a random simple digraph on `V` vertices and
     * `E` edges, with (at most) `c` strong components
     * @throws IllegalArgumentException if `c` is larger than `V`
     */
    fun strong(V: Int, E: Int, c: Int): Digraph {
        if (c >= V || c <= 0) throw IllegalArgumentException("Number of components must be between 1 and V")
        if (E <= 2 * (V - c)) throw IllegalArgumentException("Number of edges must be at least 2(V-c)")
        if (E > V.toLong() * (V - 1) / 2) throw IllegalArgumentException("Too many edges")

        // the digraph
        val G = Digraph(V)

        // edges added to G (to avoid duplicate edges)
        val set = SET<Edge>()

        val label = IntArray(V) { StdRandom.uniform(c) }

        // make all vertices with label c a strong component by
        // combining a rooted in-tree and a rooted out-tree
        for (i in 0 until c) {
            // how many vertices in component c
            var count = 0
            for (v in 0 until G.V)
                if (label[v] == i) count++

            val vertices = IntArray(count)
            var j = 0
            for (v in 0 until V)
                if (label[v] == i) vertices[j++] = v
            StdRandom.shuffle(vertices)

            // rooted-in tree with root = vertices[count-1]
            for (v in 0 until count - 1) {
                val w = StdRandom.uniform(v + 1, count)
                val e = Edge(w, v)
                set.add(e)
                G.addEdge(vertices[w], vertices[v])
            }

            // rooted-out tree with root = vertices[count-1]
            for (v in 0 until count - 1) {
                val w = StdRandom.uniform(v + 1, count)
                val e = Edge(v, w)
                set.add(e)
                G.addEdge(vertices[v], vertices[w])
            }
        }

        while (G.E < E) {
            val v = StdRandom.uniform(V)
            val w = StdRandom.uniform(V)
            val e = Edge(v, w)
            if (!set.contains(e) && v != w && label[v] <= label[w]) {
                set.add(e)
                G.addEdge(v, w)
            }
        }
        return G
    }

    /**
     * Unit tests the `DigraphGenerator` library.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val V = Integer.parseInt(args[0])
        val E = Integer.parseInt(args[1])
        StdOut.println("complete graph")
        StdOut.println(complete(V))
        StdOut.println()

        StdOut.println("simple")
        StdOut.println(simple(V, E))
        StdOut.println()

        StdOut.println("path")
        StdOut.println(path(V))
        StdOut.println()

        StdOut.println("cycle")
        StdOut.println(cycle(V))
        StdOut.println()

        StdOut.println("Eulierian path")
        StdOut.println(eulerianPath(V, E))
        StdOut.println()

        StdOut.println("Eulierian cycle")
        StdOut.println(eulerianCycle(V, E))
        StdOut.println()

        StdOut.println("binary tree")
        StdOut.println(binaryTree(V))
        StdOut.println()

        StdOut.println("tournament")
        StdOut.println(tournament(V))
        StdOut.println()

        StdOut.println("DAG")
        StdOut.println(dag(V, E))
        StdOut.println()

        StdOut.println("rooted-in DAG")
        StdOut.println(rootedInDAG(V, E))
        StdOut.println()

        StdOut.println("rooted-out DAG")
        StdOut.println(rootedOutDAG(V, E))
        StdOut.println()

        StdOut.println("rooted-in tree")
        StdOut.println(rootedInTree(V))
        StdOut.println()

        StdOut.println("rooted-out DAG")
        StdOut.println(rootedOutTree(V))
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