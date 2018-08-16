/******************************************************************************
 * Compilation:  javac GaussJordanElimination.java
 * Execution:    java GaussJordanElimination
 * Dependencies: StdOut.kt
 *
 * Finds a solutions to Ax = b using Gauss-Jordan elimination with partial
 * pivoting. If no solution exists, find a solution to yA = 0, yb != 0,
 * which serves as a certificate of infeasibility.
 *
 * % java GaussJordanElimination
 * -1.000000
 * 2.000000
 * 2.000000
 *
 * 3.000000
 * -1.000000
 * -2.000000
 *
 * System is infeasible
 *
 * -6.250000
 * -4.500000
 * 0.000000
 * 0.000000
 * 1.000000
 *
 * System is infeasible
 *
 * -1.375000
 * 1.625000
 * 0.000000
 *
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `GaussJordanElimination` data type provides methods
 * to solve a linear system of equations *Ax* = *b*,
 * where *A* is an *n*-by-*n* matrix
 * and *b* is a length *n* vector.
 * If no solution exists, it finds a solution *y* to
 * *yA* = 0, *yb*  0, which
 * which serves as a certificate of infeasibility.
 *
 *
 * This implementation uses Gauss-Jordan elimination with partial pivoting.
 * See [GaussianElimination] for an implementation that uses
 * Gaussian elimination (but does not provide the certificate of infeasibility).
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
 */
class GaussJordanElimination
// Gauss-Jordan elimination with partial pivoting
/**
 * Solves the linear system of equations *Ax* = *b*,
 * where *A* is an *n*-by-*n* matrix and *b*
 * is a length *n* vector.
 *
 * @param  A the *n*-by-*n* constraint matrix
 * @param  b the length *n* right-hand-side vector
 */
(A: Array<DoubleArray>, b: DoubleArray) {
    private val n: Int = b.size      // n-by-n system
    private val a: Array<DoubleArray>     // n-by-(n+1) augmented matrix

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
        // build augmented matrix
        a = Array(n) { it1: Int ->
            DoubleArray(n + n + 1) { it2: Int ->
                A[it1][it2]
            }
        }

        // only needed if you want to find certificate of infeasibility (or compute inverse)
        for (i in 0 until n)
            a[i][n + i] = 1.0
        for (i in 0 until n)
            a[i][n + n] = b[i]

        solve()
        assert(certifySolution(A, b))
    }

    private fun solve() {
        // Gauss-Jordan elimination
        for (p in 0 until n) {
            // find pivot row using partial pivoting
            var max = p
            for (i in p + 1 until n)
                if (Math.abs(a[i][p]) > Math.abs(a[max][p]))
                    max = i

            // exchange row p with row max
            swap(p, max)

            // singular or nearly singular
            if (Math.abs(a[p][p]) <= EPSILON) continue
                // throw new ArithmeticException("Matrix is singular or nearly singular");

            // pivot
            pivot(p, p)
        }
    }

    // swap row1 and row2
    private fun swap(row1: Int, row2: Int) {
        val temp = a[row1]
        a[row1] = a[row2]
        a[row2] = temp
    }

    // pivot on entry (p, q) using Gauss-Jordan elimination
    private fun pivot(p: Int, q: Int) {
        // everything but row p and column q
        for (i in 0 until n) {
            val alpha = a[i][q] / a[p][q]
            for (j in 0..n + n)
                if (i != p && j != q) a[i][j] -= alpha * a[p][j]
        }

        // zero out column q
        for (i in 0 until n)
            if (i != p) a[i][q] = 0.0

        // scale row p (ok to go from q+1 to n, but do this for consistency with simplex pivot)
        for (j in 0..n + n)
            if (j != q) a[p][j] /= a[p][q]
        a[p][q] = 1.0
    }

    /**
     * Returns a solution to the linear system of equations *Ax* = *b*.
     *
     * @return a solution *x* to the linear system of equations
     * *Ax* = *b*; `null` if no such solution
     */
    fun primal(): DoubleArray? {
        val x = DoubleArray(n)
        for (i in 0 until n)
            if (Math.abs(a[i][i]) > EPSILON)
                x[i] = a[i][n + n] / a[i][i]
            else if (Math.abs(a[i][n + n]) > EPSILON)
                return null
        return x
    }

    /**
     * Returns a solution to the linear system of equations *yA* = 0,
     * *yb*  0.
     *
     * @return a solution *y* to the linear system of equations
     * *yA* = 0, *yb*  0; `null` if no such solution
     */
    fun dual(): DoubleArray? {
        val y = DoubleArray(n)
        for (i in 0 until n)
            if (Math.abs(a[i][i]) <= EPSILON && Math.abs(a[i][n + n]) > EPSILON) {
                for (j in 0 until n)
                    y[j] = a[i][n + j]
                return y
            }
        return null
    }

    // print the tableaux
    private fun show() {
        for (i in 0 until n) {
            for (j in 0 until n)
                StdOut.printf("%8.3f ", a[i][j])
            StdOut.print("| ")
            for (j in n until n + n)
                StdOut.printf("%8.3f ", a[i][j])
            StdOut.printf("| %8.3f\n", a[i][n + n])
        }
        StdOut.println()
    }

    // check that Ax = b or yA = 0, yb != 0
    private fun certifySolution(A: Array<DoubleArray>, b: DoubleArray): Boolean {
        // check that Ax = b
        if (isFeasible) {
            val x = primal()
            for (i in 0 until n) {
                var sum = 0.0
                for (j in 0 until n)
                    sum += A[i][j] * x!![j]
                if (Math.abs(sum - b[i]) > EPSILON) {
                    StdOut.println("not feasible")
                    StdOut.printf("b[%d] = %8.3f, sum = %8.3f\n", i, b[i], sum)
                    return false
                }
            }
            return true
        } else {
            val y = dual()
            for (j in 0 until n) {
                var sum = 0.0
                for (i in 0 until n)
                    sum += A[i][j] * y!![i]
                if (Math.abs(sum) > EPSILON) {
                    StdOut.println("invalid certificate of infeasibility")
                    StdOut.printf("sum = %8.3f\n", sum)
                    return false
                }
            }
            var sum = 0.0
            for (i in 0 until n)
                sum += y!![i] * b[i]
            if (Math.abs(sum) < EPSILON) {
                StdOut.println("invalid certificate of infeasibility")
                StdOut.printf("yb  = %8.3f\n", sum)
                return false
            }
            return true
        }// or that yA = 0, yb != 0
    }

    companion object {
        private const val EPSILON = 1e-8

        private fun test(name: String, A: Array<DoubleArray>, b: DoubleArray) {
            StdOut.println("----------------------------------------------------")
            StdOut.println(name)
            StdOut.println("----------------------------------------------------")
            val gaussian = GaussJordanElimination(A, b)
            if (gaussian.isFeasible) {
                StdOut.println("Solution to Ax = b")
                val x = gaussian.primal()
                for (i in x!!)
                    StdOut.printf("%10.6f\n", i)
            } else {
                StdOut.println("Certificate of infeasibility")
                val y = gaussian.dual()
                for (j in y!!)
                    StdOut.printf("%10.6f\n", j)
            }
            StdOut.println()
            StdOut.println()
        }

        // 3-by-3 nonsingular system
        private fun test1() {
            val A = arrayOf(doubleArrayOf(0.0, 1.0, 1.0), doubleArrayOf(2.0, 4.0, -2.0), doubleArrayOf(0.0, 3.0, 15.0))
            val b = doubleArrayOf(4.0, 2.0, 36.0)
            test("test 1", A, b)
        }

        // 3-by-3 nonsingular system
        private fun test2() {
            val A = arrayOf(doubleArrayOf(1.0, -3.0, 1.0), doubleArrayOf(2.0, -8.0, 8.0), doubleArrayOf(-6.0, 3.0, -15.0))
            val b = doubleArrayOf(4.0, -2.0, 9.0)
            test("test 2", A, b)
        }

        // 5-by-5 singular: no solutions
        // y = [ -1, 0, 1, 1, 0 ]
        private fun test3() {
            val A = arrayOf(doubleArrayOf(2.0, -3.0, -1.0, 2.0, 3.0), doubleArrayOf(4.0, -4.0, -1.0, 4.0, 11.0), doubleArrayOf(2.0, -5.0, -2.0, 2.0, -1.0), doubleArrayOf(0.0, 2.0, 1.0, 0.0, 4.0), doubleArrayOf(-4.0, 6.0, 0.0, 0.0, 7.0))
            val b = doubleArrayOf(4.0, 4.0, 9.0, -6.0, 5.0)
            test("test 3", A, b)
        }

        // 5-by-5 singluar: infinitely many solutions
        private fun test4() {
            val A = arrayOf(doubleArrayOf(2.0, -3.0, -1.0, 2.0, 3.0), doubleArrayOf(4.0, -4.0, -1.0, 4.0, 11.0), doubleArrayOf(2.0, -5.0, -2.0, 2.0, -1.0), doubleArrayOf(0.0, 2.0, 1.0, 0.0, 4.0), doubleArrayOf(-4.0, 6.0, 0.0, 0.0, 7.0))
            val b = doubleArrayOf(4.0, 4.0, 9.0, -5.0, 5.0)
            test("test 4", A, b)
        }

        // 3-by-3 singular: no solutions
        // y = [ 1, 0, 1/3 ]
        private fun test5() {
            val A = arrayOf(doubleArrayOf(2.0, -1.0, 1.0), doubleArrayOf(3.0, 2.0, -4.0), doubleArrayOf(-6.0, 3.0, -3.0))
            val b = doubleArrayOf(1.0, 4.0, 2.0)
            test("test 5", A, b)
        }

        // 3-by-3 singular: infinitely many solutions
        private fun test6() {
            val A = arrayOf(doubleArrayOf(1.0, -1.0, 2.0), doubleArrayOf(4.0, 4.0, -2.0), doubleArrayOf(-2.0, 2.0, -4.0))
            val b = doubleArrayOf(-3.0, 1.0, 6.0)
            test("test 6 (infinitely many solutions)", A, b)
        }

        /**
         * Unit tests the `GaussJordanElimination` data type.
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

            // n-by-n random system (likely full rank)
            val n = Integer.parseInt(args[0])
            var A = Array(n) { DoubleArray(n) { StdRandom.uniform(1000).toDouble() } }
            var b = DoubleArray(n) { StdRandom.uniform(1000).toDouble() }
            test("random $n-by-$n (likely full rank)", A, b)

            // n-by-n random system (likely infeasible)
            A = Array(n) { if (it<n-1) DoubleArray(n) {StdRandom.uniform(1000).toDouble()} else DoubleArray(n) }
            for (i in 0 until n - 1) {
                val alpha = StdRandom.uniform(11) - 5.0
                for (j in 0 until n)
                    A[n - 1][j] += alpha * A[i][j]
            }
            b = DoubleArray(n){ StdRandom.uniform(1000).toDouble() }
            test("random $n-by-$n (likely infeasible)", A, b)
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