/******************************************************************************
 * Compilation:  javac CPM.java
 * Execution:    java CPM < input.txt
 * Dependencies: EdgeWeightedDigraph.kt AcyclicDigraphLP.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/44sp/jobsPC.txt
 *
 * Critical path method.
 *
 * % java CPM < jobsPC.txt
 * job   start  finish
 * --------------------
 * 0     0.0    41.0
 * 1    41.0    92.0
 * 2   123.0   173.0
 * 3    91.0   127.0
 * 4    70.0   108.0
 * 5     0.0    45.0
 * 6    70.0    91.0
 * 7    41.0    73.0
 * 8    91.0   123.0
 * 9    41.0    70.0
 * Finish time:   173.0
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `CPM` class provides a client that solves the
 * parallel precedence-constrained job scheduling problem
 * via the *critical path method*. It reduces the problem
 * to the longest-paths problem in edge-weighted DAGs.
 * It builds an edge-weighted digraph (which must be a DAG)
 * from the job-scheduling problem specification,
 * finds the longest-paths tree, and computes the longest-paths
 * lengths (which are precisely the start times for each job).
 *
 *
 * This implementation uses [AcyclicLP] to find a longest
 * path in a DAG.
 * The running time is proportional to *V* + *E*,
 * where *V* is the number of jobs and *E* is the
 * number of precedence constraints.
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
object CPM {
    /**
     * Reads the precedence constraints from standard input
     * and prints a feasible schedule to standard output.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        // number of jobs
        val n = StdIn.readInt()

        // source and sink
        val source = 2 * n
        val sink = 2 * n + 1

        // build network
        val G = EdgeWeightedDigraph(2 * n + 2)
        for (i in 0 until n) {
            val duration = StdIn.readDouble()
            G.addEdge(DirectedEdge(source, i, 0.0))
            G.addEdge(DirectedEdge(i + n, sink, 0.0))
            G.addEdge(DirectedEdge(i, i + n, duration))

            // precedence constraints
            val m = StdIn.readInt()
            for (j in 0 until m)
                G.addEdge(DirectedEdge(n + i, StdIn.readInt(), 0.0))
        }

        // compute longest path
        val lp = AcyclicLP(G, source)

        // print results
        StdOut.println(" job   start  finish")
        StdOut.println("--------------------")
        for (i in 0 until n) {
            StdOut.printf("%4d %7.1f %7.1f\n", i, lp.distTo(i), lp.distTo(i + n))
        }
        StdOut.printf("Finish time: %7.1f\n", lp.distTo(sink))
    }

}// this class cannot be instantiated

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
