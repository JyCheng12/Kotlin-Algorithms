/******************************************************************************
 * Compilation:  javac DepthFirstOrder.java
 * Execution:    java DepthFirstOrder digraph.txt
 * Dependencies: Digraph.kt Queue.kt Stack.kt StdOut.kt
 * EdgeWeightedDigraph.kt DirectedEdge.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDAG.txt
 * https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 *
 * Compute preorder and postorder for a digraph or edge-weighted digraph.
 * Runs in O(E + V) time.
 *
 * % java DepthFirstOrder tinyDAG.txt
 * v  pre post
 * --------------
 * 0    0    8
 * 1    3    2
 * 2    9   10
 * 3   10    9
 * 4    2    0
 * 5    1    1
 * 6    4    7
 * 7   11   11
 * 8   12   12
 * 9    5    6
 * 10    8    5
 * 11    6    4
 * 12    7    3
 * Preorder:  0 5 4 1 6 9 11 12 10 2 3 7 8
 * Postorder: 4 5 1 12 11 10 9 6 0 3 2 7 8
 * Reverse postorder: 8 7 2 3 0 6 9 10 11 12 1 5 4
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DepthFirstOrder` class represents a data type for
 * determining depth-first search ordering of the vertices in a digraph
 * or edge-weighted digraph, including preorder, postorder, and reverse postorder.
 *
 *
 * This implementation uses depth-first search.
 * The constructor takes time proportional to *V* + *E*
 * (in the worst case),
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the *preorder*, *postorder*, and *reverse postorder*
 * operation takes take time proportional to *V*.
 *
 *
 * For additional documentation,
 * see [Section 4.2](https://algs4.cs.princeton.edu/42digraph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class DepthFirstOrder {
    private val marked: BooleanArray          // marked[v] = has v been marked in dfs?
    private val pre: IntArray                 // pre[v]    = preorder  number of v
    private val post: IntArray                // post[v]   = postorder number of v
    val preorder: nnQueue<Int> = nnQueue()   // vertices in preorder
    val postorder: nnQueue<Int> = nnQueue()  // vertices in postorder
    private var preCounter: Int = 0            // counter or preorder numbering
    private var postCounter: Int = 0           // counter for postorder numbering

    /**
     * Determines a depth-first order for the digraph `G`.
     * @param G the digraph
     */
    constructor(G: Digraph) {
        pre = IntArray(G.V)
        post = IntArray(G.V)
        marked = BooleanArray(G.V)
        for (v in 0 until G.V)
            if (!marked[v]) dfs(G, v)

        assert(check())
    }

    /**
     * Determines a depth-first order for the edge-weighted digraph `G`.
     * @param G the edge-weighted digraph
     */
    constructor(G: EdgeWeightedDigraph) {
        pre = IntArray(G.V)
        post = IntArray(G.V)
        marked = BooleanArray(G.V)
        for (v in 0 until G.V)
            if (!marked[v]) dfs(G, v)
    }

    // run DFS in digraph G from vertex v and compute preorder/postorder
    private fun dfs(G: Digraph, v: Int) {
        marked[v] = true
        pre[v] = preCounter++
        preorder.enqueue(v)
        for (w in G.adj(v))
            if (!marked[w])
                dfs(G, w)
        postorder.enqueue(v)
        post[v] = postCounter++
    }

    // run DFS in edge-weighted digraph G from vertex v and compute preorder/postorder
    private fun dfs(G: EdgeWeightedDigraph, v: Int) {
        marked[v] = true
        pre[v] = preCounter++
        preorder.enqueue(v)
        for (e in G.adj(v)) {
            val w = e.to
            if (!marked[w])
                dfs(G, w)
        }
        postorder.enqueue(v)
        post[v] = postCounter++
    }

    /**
     * Returns the preorder number of vertex `v`.
     * @param  v the vertex
     * @return the preorder number of vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun pre(v: Int): Int {
        validateVertex(v)
        return pre[v]
    }

    /**
     * Returns the postorder number of vertex `v`.
     * @param  v the vertex
     * @return the postorder number of vertex `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun post(v: Int): Int {
        validateVertex(v)
        return post[v]
    }

    /**
     * Returns the vertices in reverse postorder.
     * @return the vertices in reverse postorder, as an iterable of vertices
     */
    fun reversePost(): Iterable<Int> {
        val reverse = nnStack<Int>()
        for (v in postorder)
            reverse.push(v)
        return reverse
    }


    // check that pre() and post() are consistent with pre(v) and post(v)
    private fun check(): Boolean {
        // check that post(v) is consistent with post()
        var r = 0
        for (v in postorder) {
            if (post(v) != r) {
                StdOut.println("post(v) and post() inconsistent")
                return false
            }
            r++
        }

        // check that pre(v) is consistent with pre()
        r = 0
        for (v in preorder) {
            if (pre(v) != r) {
                StdOut.println("pre(v) and pre() inconsistent")
                return false
            }
            r++
        }
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = marked.size
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {
        /**
         * Unit tests the `DepthFirstOrder` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Digraph(`in`)

            val dfs = DepthFirstOrder(G)
            StdOut.println("   v  pre post")
            StdOut.println("--------------")
            for (v in 0 until G.V)
                StdOut.printf("%4d %4d %4d\n", v, dfs.pre(v), dfs.post(v))

            StdOut.print("Preorder:  ")
            for (v in dfs.preorder)
                StdOut.print("$v ")
            StdOut.println()

            StdOut.print("Postorder: ")
            for (v in dfs.postorder)
                StdOut.print("$v ")
            StdOut.println()

            StdOut.print("Reverse postorder: ")
            for (v in dfs.reversePost())
                StdOut.print("$v ")
            StdOut.println()
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
