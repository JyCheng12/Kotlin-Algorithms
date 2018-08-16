/******************************************************************************
 * Compilation:  javac GaussianElimination.java
 * Execution:    java GaussianElimination m
 * Dependencies: StdOut.kt
 *
 * Gaussian elimination with partial pivoting for m-by-m system.
 *
 * % java GaussianElimination 5
 *  ----------------------------------------------------
 *  test 1 (3-by-3 system, nonsingular)
 *  ----------------------------------------------------
 *  -1.000000
 *  2.000000
 *  2.000000
 *  ----------------------------------------------------
 *  test 2 (3-by-3 system, nonsingular)
 *  ----------------------------------------------------
 *  3.000000
 *  -1.000000
 *  -2.000000
 *  ----------------------------------------------------
 *  test 3 (5-by-5 system, no solutions)
 *  ----------------------------------------------------
 *  System is infeasible
 *  ----------------------------------------------------
 *  test 4 (5-by-5 system, infinitely many solutions)
 *  ----------------------------------------------------
 *  -6.250000
 *  -4.500000
 *  -0.000000
 *  0.000000
 *  1.000000
 *  ----------------------------------------------------
 *  test 5 (3-by-3 system, no solutions)
 *  ----------------------------------------------------
 *  System is infeasible
 *  ----------------------------------------------------
 *  test 6 (3-by-3 system, infinitely many solutions)
 *  ----------------------------------------------------
 *  -1.375000
 *  1.625000
 *  0.000000
 *  ----------------------------------------------------
 *  test 7 (4-by-3 system, full rank)
 *  ----------------------------------------------------
 *  -1.000000
 *  2.000000
 *  2.000000
 *  ----------------------------------------------------
 *  test 8 (4-by-3 system, no solution)
 *  ----------------------------------------------------
 *  System is infeasible
 *  ----------------------------------------------------
 *  test 9 (3-by-4 system, full rank)
 *  ----------------------------------------------------
 *  3.000000
 *  -1.000000
 *  -2.000000
 *  0.000000
 *  ----------------------------------------------------
 *  5-by-5 (probably nonsingular)
 *  ----------------------------------------------------
 *  -3.828570
 *  -1.539495
 *  -1.770805
 *  3.379989
 *  3.244444
 */

package jingyiCheng.mscProject.algs

/**
 * The `GaussianElimination` data type provides methods
 * to solve a linear system of equations *Ax* = *b*,
 * where *A* is an *m*-by-*n* matrix
 * and *b* is a length *n* vector.
 *
 *
 * This is a bare-bones implementation that uses Gaussian elimination
 * with partial pivoting.
 * See [GaussianEliminationLite.java](https://algs4.cs.princeton.edu/99scientific/GaussianEliminationLite.java.html)
 * for a stripped-down version that assumes the matrix *A* is square
 * and nonsingular. See [GaussJordanElimination] for an alternate
 * implementation that uses Gauss-Jordan elimination.
 * For an industrial-strength numerical linear algebra library,
 * see [JAMA](http://math.nist.gov/javanumerics/jama/).
 *
 *
 * For additional documentation, see
 * [Section 9.9](https://algs4.cs.princeton.edu/99scientific)
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class GaussianElimination
/**
 * Solves the linear system of equations *Ax* = *b*,
 * where *A* is an *m*-by-*n* matrix and *b*
 * is a length *m* vector.
 *
 * @param  A the *m*-by-*n* constraint matrix
 * @param  b the length *m* right-hand-side vector
 * @throws IllegalArgumentException if the dimensions disagree, i.e.,
 * the length of `b` does not equal `m`
 */
(A: Array<DoubleArray>, b: DoubleArray) {
    private val m: Int = A.size      // number of rows
    private val n: Int = A[0].size      // number of columns
    private val a: Array<DoubleArray>     // m-by-(n+1) augmented matrix

    /**
     * Returns true if there exists a solution to the linear system of
     * equations *Ax* = *b*.
     *
     * @return `true` if there exists a solution to the linear system
     * of equations *Ax* = *b*; `false` otherwise
     */
    val isFeasible: Boolean
        get() = primal() != null

    init {
        if (b.size != m) throw IllegalArgumentException("Dimensions disagree")

        // build augmented matrix
        a = Array(m) { it1: Int -> DoubleArray(n + 1) { it2: Int -> A[it1][it2]} }
        for (i in 0 until m)
            a[i][n] = b[i]
        forwardElimination()

        assert(certifySolution(A, b))
    }

    // forward elimination
    private fun forwardElimination() {
        for (p in 0 until Math.min(m, n)) {

            // find pivot row using partial pivoting
            var max = p
            for (i in p + 1 until m)
                if (Math.abs(a[i][p]) > Math.abs(a[max][p]))
                    max = i

            // swap
            swap(p, max)

            // singular or nearly singular
            if (Math.abs(a[p][p]) <= EPSILON) continue

            // pivot
            pivot(p)
        }
    }

    // swap row1 and row2
    private fun swap(row1: Int, row2: Int) {
        val temp = a[row1]
        a[row1] = a[row2]
        a[row2] = temp
    }

    // pivot on a[p][p]
    private fun pivot(p: Int) {
        for (i in p + 1 until m) {
            val alpha = a[i][p] / a[p][p]
            for (j in p..n)
                a[i][j] -= alpha * a[p][j]
        }
    }

    /**
     * Returns a solution to the linear system of equations *Ax* = *b*.
     *
     * @return a solution *x* to the linear system of equations
     * *Ax* = *b*; `null` if no such solution
     */
    fun primal(): DoubleArray? {
        // back substitution
        val x = DoubleArray(n)
        for (i in Math.min(n - 1, m - 1) downTo 0) {
            var sum = 0.0
            for (j in i + 1 until n)
                sum += a[i][j] * x[j]
            if (Math.abs(a[i][i]) > EPSILON)
                x[i] = (a[i][n] - sum) / a[i][i]
            else if (Math.abs(a[i][n] - sum) > EPSILON)
                return null
        }

        // redundant rows
        for (i in n until m) {
            var sum = 0.0
            for (j in 0 until n)
                sum += a[i][j] * x[j]
            if (Math.abs(a[i][n] - sum) > EPSILON)
                return null
        }
        return x
    }

    // check that Ax = b
    private fun certifySolution(A: Array<DoubleArray>, b: DoubleArray): Boolean {
        if (!isFeasible) return true
        val x = primal()
        for (i in 0 until m) {
            var sum = 0.0
            for (j in 0 until n)
                sum += A[i][j] * x!![j]
            if (Math.abs(sum - b[i]) > EPSILON) {
                StdOut.println("not feasible")
                StdOut.println("b[$i] = ${b[i]}, sum = $sum")
                return false
            }
        }
        return true
    }

    companion object {
        private const val EPSILON = 1e-8

        /**
         * Unit tests the `GaussianElimination` data type.
         */
        private fun test(name: String, A: Array<DoubleArray>, b: DoubleArray) {
            StdOut.println("----------------------------------------------------")
            StdOut.println(name)
            StdOut.println("----------------------------------------------------")
            val gaussian = GaussianElimination(A, b)
            val x = gaussian.primal()
            if (gaussian.isFeasible)
                for (i in x!!)
                    StdOut.printf("%.6f\n", i)
            else
                StdOut.println("System is infeasible")
            StdOut.println()
            StdOut.println()
        }

        // 3-by-3 nonsingular system
        private fun test1() {
            val A = arrayOf(doubleArrayOf(0.0, 1.0, 1.0), doubleArrayOf(2.0, 4.0, -2.0), doubleArrayOf(0.0, 3.0, 15.0))
            val b = doubleArrayOf(4.0, 2.0, 36.0)
            test("test 1 (3-by-3 system, nonsingular)", A, b)
        }

        // 3-by-3 nonsingular system
        private fun test2() {
            val A = arrayOf(doubleArrayOf(1.0, -3.0, 1.0), doubleArrayOf(2.0, -8.0, 8.0), doubleArrayOf(-6.0, 3.0, -15.0))
            val b = doubleArrayOf(4.0, -2.0, 9.0)
            test("test 2 (3-by-3 system, nonsingular)", A, b)
        }

        // 5-by-5 singular: no solutions
        private fun test3() {
            val A = arrayOf(doubleArrayOf(2.0, -3.0, -1.0, 2.0, 3.0), doubleArrayOf(4.0, -4.0, -1.0, 4.0, 11.0), doubleArrayOf(2.0, -5.0, -2.0, 2.0, -1.0), doubleArrayOf(0.0, 2.0, 1.0, 0.0, 4.0), doubleArrayOf(-4.0, 6.0, 0.0, 0.0, 7.0))
            val b = doubleArrayOf(4.0, 4.0, 9.0, -6.0, 5.0)
            test("test 3 (5-by-5 system, no solutions)", A, b)
        }

        // 5-by-5 singular: infinitely many solutions
        private fun test4() {
            val A = arrayOf(doubleArrayOf(2.0, -3.0, -1.0, 2.0, 3.0), doubleArrayOf(4.0, -4.0, -1.0, 4.0, 11.0), doubleArrayOf(2.0, -5.0, -2.0, 2.0, -1.0), doubleArrayOf(0.0, 2.0, 1.0, 0.0, 4.0), doubleArrayOf(-4.0, 6.0, 0.0, 0.0, 7.0))
            val b = doubleArrayOf(4.0, 4.0, 9.0, -5.0, 5.0)
            test("test 4 (5-by-5 system, infinitely many solutions)", A, b)
        }

        // 3-by-3 singular: no solutions
        private fun test5() {
            val A = arrayOf(doubleArrayOf(2.0, -1.0, 1.0), doubleArrayOf(3.0, 2.0, -4.0), doubleArrayOf(-6.0, 3.0, -3.0))
            val b = doubleArrayOf(1.0, 4.0, 2.0)
            test("test 5 (3-by-3 system, no solutions)", A, b)
        }

        // 3-by-3 singular: infinitely many solutions
        private fun test6() {
            val A = arrayOf(doubleArrayOf(1.0, -1.0, 2.0), doubleArrayOf(4.0, 4.0, -2.0), doubleArrayOf(-2.0, 2.0, -4.0))
            val b = doubleArrayOf(-3.0, 1.0, 6.0)
            test("test 6 (3-by-3 system, infinitely many solutions)", A, b)
        }

        // 4-by-3 full rank and feasible system
        private fun test7() {
            val A = arrayOf(doubleArrayOf(0.0, 1.0, 1.0), doubleArrayOf(2.0, 4.0, -2.0), doubleArrayOf(0.0, 3.0, 15.0), doubleArrayOf(2.0, 8.0, 14.0))
            val b = doubleArrayOf(4.0, 2.0, 36.0, 42.0)
            test("test 7 (4-by-3 system, full rank)", A, b)
        }

        // 4-by-3 full rank and infeasible system
        private fun test8() {
            val A = arrayOf(doubleArrayOf(0.0, 1.0, 1.0), doubleArrayOf(2.0, 4.0, -2.0), doubleArrayOf(0.0, 3.0, 15.0), doubleArrayOf(2.0, 8.0, 14.0))
            val b = doubleArrayOf(4.0, 2.0, 36.0, 40.0)
            test("test 8 (4-by-3 system, no solution)", A, b)
        }

        // 3-by-4 full rank system
        private fun test9() {
            val A = arrayOf(doubleArrayOf(1.0, -3.0, 1.0, 1.0), doubleArrayOf(2.0, -8.0, 8.0, 2.0), doubleArrayOf(-6.0, 3.0, -15.0, 3.0))
            val b = doubleArrayOf(4.0, -2.0, 9.0)
            test("test 9 (3-by-4 system, full rank)", A, b)
        }

        /**
         * Unit tests the `GaussianElimination` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            test1()
            test2()
            test3()
            test4()
            test5()
            test6()
            test7()
            test8()
            test9()

            // n-by-n random system
            val n = Integer.parseInt(args[0])
            val A = Array(n) { DoubleArray(n) { StdRandom.uniform(1000).toDouble() } }
            val b = DoubleArray(n) { StdRandom.uniform(1000).toDouble() }
            test("$n-by-$n (probably nonsingular)", A, b)
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