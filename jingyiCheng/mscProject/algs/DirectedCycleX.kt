/******************************************************************************
 * Compilation:  javac DirectedCycleX.java
 * Execution:    java DirectedCycleX V E F
 * Dependencies: Queue.kt Digraph.kt Stack.kt
 *
 * Find a directed cycle in a digraph, using a nonrecursive, queue-based
 * algorithm. Runs in O(E + V) time.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DirectedCycleX` class represents a data type for
 * determining whether a digraph has a directed cycle.
 * The *hasCycle* operation determines whether the digraph has
 * a directed cycle and, and of so, the *cycle* operation
 * returns one.
 *
 *
 * This implementation uses a non-recursive, queue-based algorithm.
 * The constructor takes time proportional to *V* + *E*
 * (in the worst case),
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the *hasCycle* operation takes constant time;
 * the *cycle* operation takes time proportional
 * to the length of the cycle.
 *
 *
 * See [DirectedCycle] for a recursive version that uses depth-first search.
 * See [Topological] or [TopologicalX] to compute a topological order
 * when the digraph is acyclic.
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

class DirectedCycleX(G: Digraph) {
    var cycle: nnStack<Int>? = null     // the directed cycle; null if digraph is acyclic
        private set

    init {
        // indegrees of remaining vertices
        val indegree = IntArray(G.V){G.indegree(it)}

        // initialize queue to contain all vertices with indegree = 0
        val queue = nnQueue<Int>()
        for (v in 0 until G.V)
            if (indegree[v] == 0) queue.enqueue(v)

        while (!queue.isEmpty) {
            val v = queue.dequeue()
            for (w in G.adj(v)) {
                indegree[w]--
                if (indegree[w] == 0) queue.enqueue(w)
            }
        }

        // there is a directed cycle in subgraph of vertices with indegree >= 1.
        val edgeTo = IntArray(G.V)
        var root = -1  // any vertex with indegree >= -1
        for (v in 0 until G.V) {
            if (indegree[v] == 0)
                continue
            else
                root = v
            for (w in G.adj(v))
                if (indegree[w] > 0)
                    edgeTo[w] = v
        }

        if (root != -1) {
            // find any vertex on cycle
            val visited = BooleanArray(G.V)
            while (!visited[root]) {
                visited[root] = true
                root = edgeTo[root]
            }

            // extract cycle
            val cycle = nnStack<Int>()
            var v = root
            do {
                cycle.push(v)
                v = edgeTo[v]
            } while (v != root)
            cycle.push(root)
            this.cycle = cycle
        }
        assert(check())
    }

    /**
     * Does the digraph have a directed cycle?
     * @return `true` if the digraph has a directed cycle, `false` otherwise
     */
    fun hasCycle() = cycle != null

    // certify that digraph has a directed cycle if it reports one
    private fun check(): Boolean {
        if (hasCycle()) {
            // verify cycle
            var first = -1
            var last = -1
            for (v in cycle!!) {
                if (first == -1) first = v
                last = v
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last)
                return false
            }
        }
        return true
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            // create random DAG with V vertices and E edges; then add F random edges
            val V = Integer.parseInt(args[0])
            val E = Integer.parseInt(args[1])
            val F = Integer.parseInt(args[2])
            val G = DigraphGenerator.dag(V, E)

            // add F extra edges
            for (i in 0 until F) {
                val v = StdRandom.uniform(V)
                val w = StdRandom.uniform(V)
                G.addEdge(v, w)
            }
            StdOut.println(G)

            val finder = DirectedCycleX(G)
            if (finder.hasCycle()) {
                StdOut.print("Directed cycle: ")
                for (v in finder.cycle!!)
                    StdOut.print("$v ")
                StdOut.println()
            } else
                StdOut.println("No directed cycle")
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
