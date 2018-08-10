/******************************************************************************
 * Compilation:  javac FordFulkerson.java
 * Execution:    java FordFulkerson V E
 * Dependencies: FlowNetwork.kt FlowEdge.kt Queue.kt
 * Data files:   https://algs4.cs.princeton.edu/65maxflow/tinyFN.txt
 *
 * Ford-Fulkerson algorithm for computing a max flow and
 * a min cut using shortest augmenting path rule.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `FordFulkerson` class represents a data type for computing a
 * *maximum st-flow* and *minimum st-cut* in a flow
 * network.
 *
 *
 * This implementation uses the *Ford-Fulkerson* algorithm with
 * the *shortest augmenting path* heuristic.
 * The constructor takes time proportional to *E V* (*E* + *V*)
 * in the worst case and extra space (not including the network)
 * proportional to *V*, where *V* is the number of vertices
 * and *E* is the number of edges. In practice, the algorithm will
 * run much faster.
 * Afterwards, the `inCut()` and `value()` methods take
 * constant time.
 *
 *
 * If the capacities and initial flow values are all integers, then this
 * implementation guarantees to compute an integer-valued maximum flow.
 * If the capacities and floating-point numbers, then floating-point
 * roundoff error can accumulate.
 *
 *
 * For additional documentation,
 * see [Section 6.4](https://algs4.cs.princeton.edu/64maxflow) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class FordFulkerson
/**
 * Compute a maximum flow and minimum cut in the network `G`
 * from vertex `s` to vertex `t`.
 *
 * @param  G the flow network
 * @param  s the source vertex
 * @param  t the sink vertex
 * @throws IllegalArgumentException unless `0 <= s < V`
 * @throws IllegalArgumentException unless `0 <= t < V`
 * @throws IllegalArgumentException if `s == t`
 * @throws IllegalArgumentException if initial flow is infeasible
 */
(G: FlowNetwork, s: Int, t: Int) {
    private val V: Int = G.V          // number of vertices
    private lateinit var marked: BooleanArray     // marked[v] = true iff s->v path in residual graph
    private lateinit var edgeTo: Array<FlowEdge?>    // edgeTo[v] = last edge on shortest residual s->v path
    var value: Double = 0.0         // current value of max flow
        private set

    init {
        validate(s)
        validate(t)
        if (s == t) throw IllegalArgumentException("Source equals sink")
        if (!isFeasible(G, s, t)) throw IllegalArgumentException("Initial flow is infeasible")

        // while there exists an augmenting path, use it
        value = excess(G, t)
        while (hasAugmentingPath(G, s, t)) {

            // compute bottleneck capacity
            var bottle = Double.POSITIVE_INFINITY
            run {
                var v = t
                while (v != s) {
                    bottle = Math.min(bottle, edgeTo[v]!!.residualCapacityTo(v))
                    v = edgeTo[v]!!.other(v)
                }
            }

            // augment flow
            var v = t
            while (v != s) {
                edgeTo[v]!!.addResidualFlowTo(v, bottle)
                v = edgeTo[v]!!.other(v)
            }
            value += bottle
        }

        // check optimality conditions
        assert(check(G, s, t))
    }

    /**
     * Returns true if the specified vertex is on the `s` side of the mincut.
     *
     * @param  v vertex
     * @return `true` if vertex `v` is on the `s` side of the micut;
     * `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    fun inCut(v: Int): Boolean {
        validate(v)
        return marked[v]
    }

    // throw an IllegalArgumentException if v is outside prescibed range
    private fun validate(v: Int) {
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }


    // is there an augmenting path?
    // if so, upon termination edgeTo[] will contain a parent-link representation of such a path
    // this implementation finds a shortest augmenting path (fewest number of edges),
    // which performs well both in theory and in practice
    private fun hasAugmentingPath(G: FlowNetwork, s: Int, t: Int): Boolean {
        edgeTo = arrayOfNulls(G.V)
        marked = BooleanArray(G.V)

        // breadth-first search
        val queue = nnQueue<Int>()
        queue.enqueue(s)
        marked[s] = true
        while (!queue.isEmpty && !marked[t]) {
            val v = queue.dequeue()

            for (e in G.adj(v)) {
                val w = e.other(v)

                // if residual capacity from v to w
                if (e.residualCapacityTo(w) > 0) {
                    if (!marked[w]) {
                        edgeTo[w] = e
                        marked[w] = true
                        queue.enqueue(w)
                    }
                }
            }
        }
        // is there an augmenting path?
        return marked[t]
    }


    // return excess flow at vertex v
    private fun excess(G: FlowNetwork, v: Int): Double {
        var excess = 0.0
        for (e in G.adj(v)) {
            if (v == e.from)
                excess -= e.flow
            else
                excess += e.flow
        }
        return excess
    }

    // return excess flow at vertex v
    private fun isFeasible(G: FlowNetwork, s: Int, t: Int): Boolean {

        // check that capacity constraints are satisfied
        for (v in 0 until G.V) {
            for (e in G.adj(v)) {
                if (e.flow < -FLOATING_POINT_EPSILON || e.flow > e.capacity + FLOATING_POINT_EPSILON) {
                    System.err.println("Edge does not satisfy capacity constraints: $e")
                    return false
                }
            }
        }

        // check that net flow into a vertex equals zero, except at source and sink
        if (Math.abs(value + excess(G, s)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at source = " + excess(G, s))
            System.err.println("Max flow         = $value")
            return false
        }
        if (Math.abs(value - excess(G, t)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at sink   = " + excess(G, t))
            System.err.println("Max flow         = $value")
            return false
        }
        for (v in 0 until G.V) {
            if (v == s || v == t)
                continue
            else if (Math.abs(excess(G, v)) > FLOATING_POINT_EPSILON) {
                System.err.println("Net flow out of $v doesn't equal zero")
                return false
            }
        }
        return true
    }

    // check optimality conditions
    private fun check(G: FlowNetwork, s: Int, t: Int): Boolean {

        // check that flow is feasible
        if (!isFeasible(G, s, t)) {
            System.err.println("Flow is infeasible")
            return false
        }

        // check that s is on the source side of min cut and that t is not on source side
        if (!inCut(s)) {
            System.err.println("source $s is not on source side of min cut")
            return false
        }
        if (inCut(t)) {
            System.err.println("sink $t is on source side of min cut")
            return false
        }

        // check that value of min cut = value of max flow
        var mincutValue = 0.0
        for (v in 0 until G.V) {
            for (e in G.adj(v)) {
                if (v == e.from && inCut(e.from) && !inCut(e.to))
                    mincutValue += e.capacity
            }
        }

        if (Math.abs(mincutValue - value) > FLOATING_POINT_EPSILON) {
            System.err.println("Max flow value = $value, min cut value = $mincutValue")
            return false
        }

        return true
    }

    companion object {
        private const val FLOATING_POINT_EPSILON = 1E-11

        /**
         * Unit tests the `FordFulkerson` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {

            // create flow network with V vertices and E edges
            val V = Integer.parseInt(args[0])
            val E = Integer.parseInt(args[1])
            val s = 0
            val t = V - 1
            val G = FlowNetwork(V, E)
            StdOut.println(G)

            // compute maximum flow and minimum cut
            val maxflow = FordFulkerson(G, s, t)
            StdOut.println("Max flow from $s to $t")
            for (v in 0 until G.V) {
                for (e in G.adj(v)) {
                    if (v == e.from && e.flow > 0)
                        StdOut.println("   $e")
                }
            }

            // print min-cut
            StdOut.print("Min cut: ")
            for (v in 0 until G.V)
                if (maxflow.inCut(v)) StdOut.print("$v ")
            StdOut.println()

            StdOut.println("Max flow value = ${maxflow.value}")
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
