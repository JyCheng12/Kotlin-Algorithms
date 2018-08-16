/******************************************************************************
 * Compilation:  javac AssignmentProblem.java
 * Execution:    java AssignmentProblem n
 * Dependencies: DijkstraSP.kt DirectedEdge.kt
 *
 * Solve an n-by-n assignment problem in n^3 log n time using the
 * successive shortest path algorithm.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `AssignmentProblem` class represents a data type for computing
 * an optimal solution to an *n*-by-*n* *assignment problem*.
 * The assignment problem is to find a minimum weight matching in an
 * edge-weighted complete bipartite graph.
 *
 *
 * The data type supplies methods for determining the optimal solution
 * and the corresponding dual solution.
 *
 *
 * This implementation uses the *successive shortest paths algorithm*.
 * The order of growth of the running time in the worst case is
 * O(*n*^3 log *n*) to solve an *n*-by-*n*
 * instance.
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
class AssignmentProblem
/**
 * Determines an optimal solution to the assignment problem.
 *
 * @param  weight the *n*-by-*n* matrix of weights
 * @throws IllegalArgumentException unless all weights are nonnegative
 * @throws IllegalArgumentException if `weight` is `null`
 */
(weight: Array<DoubleArray>?) {
    private val n: Int              // number of rows and columns
    private val weight: Array<DoubleArray>  // the n-by-n cost matrix
    private var minWeight: Double = 0.0   // minimum value of any weight
    private val px: DoubleArray        // px[i] = dual variable for row i
    private val py: DoubleArray        // py[j] = dual variable for col j
    private val xy: IntArray           // xy[i] = j means i-j is a match
    private val yx: IntArray           // yx[j] = i means i-j is a match

    // check that dual variables are feasible
    private val isDualFeasible: Boolean
        get() {  // check that all edges have >= 0 reduced cost
            for (i in 0 until n) {
                for (j in 0 until n) {
                    if (reducedCost(i, j) < 0) {
                        StdOut.println("Dual variables are not feasible")
                        return false
                    }
                }
            }
            return true
        }

    // check that primal and dual variables are complementary slack
    private val isComplementarySlack: Boolean
        get() { // check that all matched edges have 0-reduced cost
            for (i in 0 until n) {
                if (xy[i] != UNMATCHED && reducedCost(i, xy[i]) != 0.0) {
                    StdOut.println("Primal and dual variables are not complementary slack")
                    return false
                }
            }
            return true
        }

    // check that primal variables are a perfect matching, that xy[] and yx[] are inverses, that xy[] is a perfect matching
    private val isPerfectMatching: Boolean
        get() {
            val perm = BooleanArray(n)
            for (i in 0 until n) {
                if (perm[xy[i]]) {
                    StdOut.println("Not a perfect matching")
                    return false
                }
                perm[xy[i]] = true
            }
            for (j in 0 until n) {
                if (xy[yx[j]] != j) {
                    StdOut.println("xy[] and yx[] are not inverses")
                    return false
                }
            }
            for (i in 0 until n) {
                if (yx[xy[i]] != i) {
                    StdOut.println("xy[] and yx[] are not inverses")
                    return false
                }
            }
            return true
        }

    init {
        if (weight == null) throw IllegalArgumentException("constructor argument is null")

        n = weight.size
        this.weight = Array(n) { DoubleArray(n) }
        for (i in 0 until n)
            for (j in 0 until n) {
                if (weight[i][j].isNaN()) throw IllegalArgumentException("weight $i-$j is NaN")
                if (weight[i][j] < minWeight) minWeight = weight[i][j]
                this.weight[i][j] = weight[i][j]
            }

        // dual variables
        px = DoubleArray(n)
        py = DoubleArray(n)

        // initial matching is empty
        xy = IntArray(n){ UNMATCHED }
        yx = IntArray(n){ UNMATCHED }

        // add n edges to matching
        for (k in 0 until n) {
            assert(isDualFeasible)
            assert(isComplementarySlack)
            augment()
        }
        assert(certifySolution())
    }

    // find shortest augmenting path and upate
    private fun augment() {

        // build residual graph
        val G = EdgeWeightedDigraph(2 * n + 2)
        val s = 2 * n
        val t = 2 * n + 1
        for (i in 0 until n)
            if (xy[i] == UNMATCHED)
                G.addEdge(DirectedEdge(s, i, 0.0))
        for (j in 0 until n)
            if (yx[j] == UNMATCHED)
                G.addEdge(DirectedEdge(n + j, t, py[j]))
        for (i in 0 until n)
            for (j in 0 until n)
                if (xy[i] == j)
                    G.addEdge(DirectedEdge(n + j, i, 0.0))
                else
                    G.addEdge(DirectedEdge(i, n + j, reducedCost(i, j)))

        // compute shortest path from s to every other vertex
        val spt = DijkstraSP(G, s)

        // augment along alternating path
        for (e in spt.pathTo(t)!!) {
            val i = e.from
            val j = e.to - n
            if (i < n) {
                xy[i] = j
                yx[j] = i
            }
        }

        // update dual variables
        for (i in 0 until n)
            px[i] += spt.distTo(i)
        for (j in 0 until n)
            py[j] += spt.distTo(n + j)
    }

    // reduced cost of i-j
    // (subtracting off minWeight reweights all weights to be non-negative)
    private fun reducedCost(i: Int, j: Int): Double {
        val reducedCost = weight[i][j] - minWeight + px[i] - py[j]

        // to avoid issues with floating-point precision
        val magnitude = Math.abs(weight[i][j]) + Math.abs(px[i]) + Math.abs(py[j])
        if (Math.abs(reducedCost) <= FLOATING_POINT_EPSILON * magnitude) return 0.0

        assert(reducedCost >= 0.0)
        return reducedCost
    }

    /**
     * Returns the dual optimal value for the specified row.
     *
     * @param  i the row index
     * @return the dual optimal value for row `i`
     * @throws IllegalArgumentException unless `0 <= i < n`
     */
    // dual variable for row i
    fun dualRow(i: Int): Double {
        validate(i)
        return px[i]
    }

    /**
     * Returns the dual optimal value for the specified column.
     *
     * @param  j the column index
     * @return the dual optimal value for column `j`
     * @throws IllegalArgumentException unless `0 <= j < n`
     */
    fun dualCol(j: Int): Double {
        validate(j)
        return py[j]
    }

    /**
     * Returns the column associated with the specified row in the optimal solution.
     *
     * @param  i the row index
     * @return the column matched to row `i` in the optimal solution
     * @throws IllegalArgumentException unless `0 <= i < n`
     */
    fun sol(i: Int): Int {
        validate(i)
        return xy[i]
    }

    /**
     * Returns the total weight of the optimal solution
     *
     * @return the total weight of the optimal solution
     */
    fun weight(): Double {
        var total = 0.0
        for (i in 0 until n) {
            if (xy[i] != UNMATCHED)
                total += weight[i][xy[i]]
        }
        return total
    }

    private fun validate(i: Int) {
        if (i < 0 || i >= n) throw IllegalArgumentException("index is not between 0 and ${n - 1}: $i")
    }

    // check optimality conditions
    private fun certifySolution(): Boolean {
        return isPerfectMatching && isDualFeasible && isComplementarySlack
    }

    companion object {
        private const val FLOATING_POINT_EPSILON = 1E-14
        private const val UNMATCHED = -1

        /**
         * Unit tests the `AssignmentProblem` data type.
         * Takes a command-line argument n; creates a random n-by-n matrix;
         * solves the n-by-n assignment problem; and prints the optimal
         * solution.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {

            // create random n-by-n matrix
            val n = Integer.parseInt(args[0])
            val weight = Array(n) { DoubleArray(n) {StdRandom.uniform(900).toDouble() + 100} }

            // solve assignment problem
            val assignment = AssignmentProblem(weight)
            StdOut.printf("weight = %.0f\n", assignment.weight())
            StdOut.println()

            // print n-by-n matrix and optimal solution
            if (n >= 20) return
            for (i in 0 until n) {
                for (j in 0 until n) {
                    if (j == assignment.sol(i))
                        StdOut.printf("*%.0f ", weight[i][j])
                    else
                        StdOut.printf(" %.0f ", weight[i][j])
                }
                StdOut.println()
            }
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