/******************************************************************************
 * Compilation:  javac LinearProgramming.java
 * Execution:    java LinearProgramming m n
 * Dependencies: StdOut.kt
 *
 * Given an m-by-n matrix A, an m-length vector b, and an
 * n-length vector c, solve the  LP { max cx : Ax <= b, x >= 0 }.
 * Assumes that b >= 0 so that x = 0 is a basic feasible solution.
 *
 * Creates an (m+1)-by-(n+m+1) simplex tableaux with the
 * RHS in column m+n, the objective function in row m, and
 * slack variables in columns m through m+n-1.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LinearProgramming` class represents a data type for solving a
 * linear program of the form { max cx : Ax  b, x  0 }, where A is a m-by-n
 * matrix, b is an m-length vector, and c is an n-length vector. For simplicity,
 * we assume that A is of full rank and that b  0 so that x = 0 is a basic
 * feasible soution.
 *
 *
 * The data type supplies methods for determining the optimal primal and
 * dual solutions.
 *
 *
 * This is a bare-bones implementation of the *simplex algorithm*.
 * It uses Bland's rule to determing the entering and leaving variables.
 * It is not suitable for use on large inputs. It is also not robust
 * in the presence of floating-point roundoff error.
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
class LinearProgramming

/**
 * Determines an optimal solution to the linear program
 * { max cx : Ax  b, x  0 }, where A is a m-by-n
 * matrix, b is an m-length vector, and c is an n-length vector.
 *
 * @param  A the *m*-by-*b* matrix
 * @param  b the *m*-length RHS vector
 * @param  c the *n*-length cost vector
 * @throws IllegalArgumentException unless `b[i] >= 0` for each `i`
 * @throws ArithmeticException if the linear program is unbounded
 */
(A: Array<DoubleArray>, b: DoubleArray, c: DoubleArray) {
    private val a: Array<DoubleArray>   // tableaux
    private val m: Int = b.size          // number of constraints
    private val n: Int = c.size          // number of original variables
    private val basis: IntArray    // basis[i] = basic variable corresponding to row i

    init {
        for (i in 0 until m)
            if (b[i] < 0) throw IllegalArgumentException("RHS must be non-negative")

        a = Array(m + 1) { DoubleArray(n + m + 1) }
        for (i in 0 until m)
            for (j in 0 until n)
                a[i][j] = A[i][j]
        for (i in 0 until m)
            a[i][n + i] = 1.0
        for (j in 0 until n)
            a[m][j] = c[j]
        for (i in 0 until m)
            a[i][m + n] = b[i]

        basis = IntArray(m) { n + it }
        solve()

        // check optimality conditions
        assert(check(A, b, c))
    }

    // run simplex algorithm starting from initial BFS
    private fun solve() {
        while (true) {
            // find entering column q
            val q = bland()
            if (q == -1) break  // optimal

            // find leaving row p
            val p = minRatioRule(q)
            if (p == -1) throw ArithmeticException("Linear program is unbounded")

            // pivot
            pivot(p, q)

            // update basis
            basis[p] = q
        }
    }

    // lowest index of a non-basic column with a positive cost
    private fun bland(): Int {
        for (j in 0 until m + n)
            if (a[m][j] > 0) return j
        return -1  // optimal
    }

    // index of a non-basic column with most positive cost
    private fun dantzig(): Int {
        var q = 0
        for (j in 1 until m + n)
            if (a[m][j] > a[m][q]) q = j
        return if (a[m][q] <= 0) -1  // optimal
        else q
    }

    // find row p using min ratio rule (-1 if no such row)
    // (smallest such index if there is a tie)
    private fun minRatioRule(q: Int): Int {
        var p = -1
        for (i in 0 until m)
            // if (a[i][q] <= 0) continue;
            if (a[i][q] <= EPSILON)
                continue
            else if (p == -1)
                p = i
            else if (a[i][m + n] / a[i][q] < a[p][m + n] / a[p][q]) p = i
        return p
    }

    // pivot on entry (p, q) using Gauss-Jordan elimination
    private fun pivot(p: Int, q: Int) {
        // everything but row p and column q
        for (i in 0..m)
            for (j in 0..m + n)
                if (i != p && j != q) a[i][j] -= a[p][j] * a[i][q] / a[p][q]

        // zero out column q
        for (i in 0..m)
            if (i != p) a[i][q] = 0.0

        // scale row p
        for (j in 0..m + n)
            if (j != q) a[p][j] /= a[p][q]
        a[p][q] = 1.0
    }

    /**
     * Returns the optimal value of this linear program.
     *
     * @return the optimal value of this linear program
     */
    fun value() = -a[m][m + n]

    /**
     * Returns the optimal primal solution to this linear program.
     *
     * @return the optimal primal solution to this linear program
     */
    fun primal(): DoubleArray {
        val x = DoubleArray(n)
        for (i in 0 until m)
            if (basis[i] < n) x[basis[i]] = a[i][m + n]
        return x
    }

    /**
     * Returns the optimal dual solution to this linear program
     *
     * @return the optimal dual solution to this linear program
     */
    fun dual(): DoubleArray = DoubleArray(m) { -a[m][n + it] }

    // is the solution primal feasible?
    private fun isPrimalFeasible(A: Array<DoubleArray>, b: DoubleArray): Boolean {
        val x = primal()

        // check that x >= 0
        for (j in x.indices)
            if (x[j] < 0.0) {
                StdOut.println("x[$j] = ${x[j]} is negative")
                return false
            }

        // check that Ax <= b
        for (i in 0 until m) {
            var sum = 0.0
            for (j in 0 until n)
                sum += A[i][j] * x[j]
            if (sum > b[i] + EPSILON) {
                StdOut.println("not primal feasible")
                StdOut.println("b[$i] = ${b[i]}, sum = $sum")
                return false
            }
        }
        return true
    }

    // is the solution dual feasible?
    private fun isDualFeasible(A: Array<DoubleArray>, c: DoubleArray): Boolean {
        val y = dual()

        // check that y >= 0
        for (i in y.indices)
            if (y[i] < 0.0) {
                StdOut.println("y[$i] = ${y[i]} is negative")
                return false
            }

        // check that yA >= c
        for (j in 0 until n) {
            var sum = 0.0
            for (i in 0 until m)
                sum += A[i][j] * y[i]
            if (sum < c[j] - EPSILON) {
                StdOut.println("not dual feasible")
                StdOut.println("c[$j] = ${c[j]}, sum = $sum")
                return false
            }
        }
        return true
    }

    // check that optimal value = cx = yb
    private fun isOptimal(b: DoubleArray, c: DoubleArray): Boolean {
        val x = primal()
        val y = dual()
        val value = value()

        // check that value = cx = yb
        var value1 = 0.0
        for (j in x.indices)
            value1 += c[j] * x[j]
        var value2 = 0.0
        for (i in y.indices)
            value2 += y[i] * b[i]
        if (Math.abs(value - value1) > EPSILON || Math.abs(value - value2) > EPSILON) {
            StdOut.println("value = $value, cx = $value1, yb = $value2")
            return false
        }
        return true
    }

    private fun check(A: Array<DoubleArray>, b: DoubleArray, c: DoubleArray) =
            isPrimalFeasible(A, b) && isDualFeasible(A, c) && isOptimal(b, c)

    // print tableaux
    private fun show() {
        StdOut.println("m = $m")
        StdOut.println("n = $n")
        for (i in 0..m) {
            for (j in 0..m + n)
                StdOut.printf("%7.2f ", a[i][j])
            StdOut.println()
        }
        StdOut.println("value = ${value()}")
        for (i in 0 until m)
            if (basis[i] < n) StdOut.println("x_${basis[i]} = ${a[i][m + n]}")
        StdOut.println()
    }

    companion object {
        private val EPSILON = 1.0E-10

        private fun test(A: Array<DoubleArray>, b: DoubleArray, c: DoubleArray) {
            val lp = LinearProgramming(A, b, c)
            StdOut.println("value = ${lp.value()}")
            val x = lp.primal()
            for (i in x.indices)
                StdOut.println("x[$i] = ${x[i]}")
            val y = lp.dual()
            for (j in y.indices)
                StdOut.println("y[$j] = ${y[j]}")
        }

        private fun test1() {
            val A = arrayOf(doubleArrayOf(-1.0, 1.0, 0.0), doubleArrayOf(1.0, 4.0, 0.0), doubleArrayOf(2.0, 1.0, 0.0), doubleArrayOf(3.0, -4.0, 0.0), doubleArrayOf(0.0, 0.0, 1.0))
            val c = doubleArrayOf(1.0, 1.0, 1.0)
            val b = doubleArrayOf(5.0, 45.0, 27.0, 24.0, 4.0)
            test(A, b, c)
        }

        // x0 = 12, x1 = 28, opt = 800
        private fun test2() {
            val c = doubleArrayOf(13.0, 23.0)
            val b = doubleArrayOf(480.0, 160.0, 1190.0)
            val A = arrayOf(doubleArrayOf(5.0, 15.0), doubleArrayOf(4.0, 4.0), doubleArrayOf(35.0, 20.0))
            test(A, b, c)
        }

        // unbounded
        private fun test3() {
            val c = doubleArrayOf(2.0, 3.0, -1.0, -12.0)
            val b = doubleArrayOf(3.0, 2.0)
            val A = arrayOf(doubleArrayOf(-2.0, -9.0, 1.0, 9.0), doubleArrayOf(1.0, 1.0, -1.0, -2.0))
            test(A, b, c)
        }

        // degenerate - cycles if you choose most positive objective function coefficient
        private fun test4() {
            val c = doubleArrayOf(10.0, -57.0, -9.0, -24.0)
            val b = doubleArrayOf(0.0, 0.0, 1.0)
            val A = arrayOf(doubleArrayOf(0.5, -5.5, -2.5, 9.0), doubleArrayOf(0.5, -1.5, -0.5, 1.0), doubleArrayOf(1.0, 0.0, 0.0, 0.0))
            test(A, b, c)
        }

        /**
         * Unit tests the `LinearProgramming` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            StdOut.println("----- test 1 --------------------")
            test1()
            StdOut.println("----- test 2 --------------------")
            test2()
            StdOut.println("----- test 3 --------------------")
            try {
                test3()
            } catch (e: ArithmeticException) {
                println(e)
            }

            StdOut.println("----- test 4 --------------------")
            test4()

            StdOut.println("----- test random ---------------")
            val m = Integer.parseInt(args[0])
            val n = Integer.parseInt(args[1])
            val c = DoubleArray(n) { StdRandom.uniform(1000).toDouble() }
            val b = DoubleArray(m) { StdRandom.uniform(1000).toDouble() }
            val A = Array(m) { DoubleArray(n) { StdRandom.uniform(100).toDouble() } }
            val lp = LinearProgramming(A, b, c)
            StdOut.println(lp.value())
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