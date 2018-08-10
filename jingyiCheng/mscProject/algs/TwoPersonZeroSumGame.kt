/******************************************************************************
 * Compilation:  javac TwoPersonZeroSumGame.java
 * Execution:    java TwoPersonZeroSumGame m n
 * Dependencies: LinearProgramming.kt StdOut.kt
 *
 * Solve an m-by-n two-person zero-sum game by reducing it to
 * linear programming. Assuming A is a strictly positive payoff
 * matrix, the optimal row and column player strategies are x* an y*,
 * scaled to be probability distributions.
 *
 * (P)  max  y^T 1         (D)  min   1^T x
 * s.t  A^T y <= 1         s.t   A x >= 1
 * y >= 0                 x >= 0
 *
 * Row player is x, column player is y.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `TwoPersonZeroSumGame` class represents a data type for
 * computing optimal row and column strategies to two-person zero-sum games.
 *
 *
 * This implementation solves an *m*-by-*n* two-person
 * zero-sum game by reducing it to a linear programming problem.
 * Assuming the payoff matrix *A* is strictly positive, the
 * optimal row and column player strategies x* and y* are obtained
 * by solving the following primal and dual pair of linear programs,
 * scaling the results to be probability distributions.
 * <blockquote><pre>
 * (P)  max  y^T 1           (D)  min   1^T x
 * s.t  A^T y  1         s.t   A x  1
 * y  0                 x  0
</pre></blockquote> *
 *
 *
 * If the payoff matrix *A* has any negative entries, we add
 * the same constant to every entry so that every entry is positive.
 * This increases the value of the game by that constant, but does not
 * change solutions to the two-person zero-sum game.
 *
 *
 * This implementation is not suitable for large inputs, as it calls
 * a bare-bones linear programming solver that is neither fast nor
 * robust with respect to floating-point roundoff error.
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
class TwoPersonZeroSumGame
/**
 * Determines an optimal solution to the two-sum zero-sum game
 * with the specified payoff matrix.
 *
 * @param  payoff the *m*-by-*n* payoff matrix
 */
(payoff: Array<DoubleArray>) {
    private val m: Int = payoff.size            // number of rows
    private val n: Int = payoff[0].size            // number of columns
    private val lp: LinearProgramming   // linear program solver
    private var constant: Double = 0.toDouble()        // constant added to each entry in payoff matrix

    // is the row vector x primal feasible?
    private val isPrimalFeasible: Boolean
        get() {
            val x = row()
            var sum = 0.0
            for (j in 0 until n) {
                if (x[j] < 0) {
                    StdOut.println("row vector not a probability distribution")
                    StdOut.printf("    x[%d] = %f\n", j, x[j])
                    return false
                }
                sum += x[j]
            }
            if (Math.abs(sum - 1.0) > EPSILON) {
                StdOut.println("row vector x[] is not a probability distribution")
                StdOut.println("    sum = $sum")
                return false
            }
            return true
        }

    // is the column vector y dual feasible?
    private val isDualFeasible: Boolean
        get() {
            val y = column()
            var sum = 0.0
            for (i in 0 until m) {
                if (y[i] < 0) {
                    StdOut.println("column vector y[] is not a probability distribution")
                    StdOut.printf("    y[%d] = %f\n", i, y[i])
                    return false
                }
                sum += y[i]
            }
            if (Math.abs(sum - 1.0) > EPSILON) {
                StdOut.println("column vector not a probability distribution")
                StdOut.println("    sum = $sum")
                return false
            }
            return true
        }

    init {
        val c = DoubleArray(n) { 1.0 }
        val b = DoubleArray(m) { 1.0 }


        // find smallest entry
        constant = Double.POSITIVE_INFINITY
        for (i in 0 until m)
            for (j in 0 until n)
                if (payoff[i][j] < constant)
                    constant = payoff[i][j]

        // add constant  to every entry to make strictly positive
        constant = if (constant <= 0) -constant + 1 else 0.0
        val A = Array(m) { it1: Int ->
            DoubleArray(n) { it2: Int ->
                payoff[it1][it2] + constant
            }
        }
        lp = LinearProgramming(A, b, c)
        assert(certifySolution(payoff))
    }

    /**
     * Returns the optimal value of this two-person zero-sum game.
     *
     * @return the optimal value of this two-person zero-sum game
     */
    fun value() = 1.0 / scale() - constant


    // sum of x[j]
    private fun scale(): Double {
        val x = lp.primal()
        var sum = 0.0
        for (j in 0 until n)
            sum += x[j]
        return sum
    }

    /**
     * Returns the optimal row strategy of this two-person zero-sum game.
     *
     * @return the optimal row strategy *x* of this two-person zero-sum game
     */
    fun row(): DoubleArray {
        val scale = scale()
        val x = lp.primal()
        for (j in 0 until n)
            x[j] /= scale
        return x
    }

    /**
     * Returns the optimal column strategy of this two-person zero-sum game.
     *
     * @return the optimal column strategy *y* of this two-person zero-sum game
     */
    fun column(): DoubleArray {
        val scale = scale()
        val y = lp.dual()
        for (i in 0 until m)
            y[i] /= scale
        return y
    }

    // is the solution a Nash equilibrium?
    private fun isNashEquilibrium(payoff: Array<DoubleArray>): Boolean {
        val x = row()
        val y = column()
        val value = value()

        // given row player's mixed strategy, find column player's best pure strategy
        var opt1 = Double.NEGATIVE_INFINITY
        for (i in 0 until m) {
            var sum = 0.0
            for (j in 0 until n)
                sum += payoff[i][j] * x[j]
            if (sum > opt1) opt1 = sum
        }
        if (Math.abs(opt1 - value) > EPSILON) {
            StdOut.println("Optimal value = $value")
            StdOut.println("Optimal best response for column player = $opt1")
            return false
        }

        // given column player's mixed strategy, find row player's best pure strategy
        var opt2 = Double.POSITIVE_INFINITY
        for (j in 0 until n) {
            var sum = 0.0
            for (i in 0 until m)
                sum += payoff[i][j] * y[i]
            if (sum < opt2) opt2 = sum
        }
        if (Math.abs(opt2 - value) > EPSILON) {
            StdOut.println("Optimal value = $value")
            StdOut.println("Optimal best response for row player = $opt2")
            return false
        }
        return true
    }

    private fun certifySolution(payoff: Array<DoubleArray>) = isPrimalFeasible && isDualFeasible && isNashEquilibrium(payoff)

    companion object {
        private const val EPSILON = 1E-8

        private fun test(description: String, payoff: Array<DoubleArray>) {
            StdOut.println()
            StdOut.println(description)
            StdOut.println("------------------------------------")
            val m = payoff.size
            val n = payoff[0].size
            val zerosum = TwoPersonZeroSumGame(payoff)
            val x = zerosum.row()
            val y = zerosum.column()

            StdOut.print("x[] = [")
            for (j in 0 until n - 1)
                StdOut.printf("%8.4f, ", x[j])
            StdOut.printf("%8.4f]\n", x[n - 1])

            StdOut.print("y[] = [")
            for (i in 0 until m - 1)
                StdOut.printf("%8.4f, ", y[i])
            StdOut.printf("%8.4f]\n", y[m - 1])
            StdOut.println("value =  " + zerosum.value())
        }

        // row = { 4/7, 3/7 }, column = { 0, 4/7, 3/7 }, value = 20/7
        // http://en.wikipedia.org/wiki/Zero-sum
        private fun test1() {
            val payoff = arrayOf(doubleArrayOf(30.0, -10.0, 20.0), doubleArrayOf(10.0, 20.0, -20.0))
            test("wikipedia", payoff)
        }

        // skew-symmetric => value = 0
        // Linear Programming by Chvatal, p. 230
        private fun test2() {
            val payoff = arrayOf(doubleArrayOf(0.0, 2.0, -3.0, 0.0), doubleArrayOf(-2.0, 0.0, 0.0, 3.0), doubleArrayOf(3.0, 0.0, 0.0, -4.0), doubleArrayOf(0.0, -3.0, 4.0, 0.0))
            test("Chvatal, p. 230", payoff)
        }

        // Linear Programming by Chvatal, p. 234
        // row    = { 0, 56/99, 40/99, 0, 0, 2/99, 0, 1/99 }
        // column = { 28/99, 30/99, 21/99, 20/99 }
        // value  = 4/99
        private fun test3() {
            val payoff = arrayOf(doubleArrayOf(0.0, 2.0, -3.0, 0.0), doubleArrayOf(-2.0, 0.0, 0.0, 3.0), doubleArrayOf(3.0, 0.0, 0.0, -4.0), doubleArrayOf(0.0, -3.0, 4.0, 0.0), doubleArrayOf(0.0, 0.0, -3.0, 3.0), doubleArrayOf(-2.0, 2.0, 0.0, 0.0), doubleArrayOf(3.0, -3.0, 0.0, 0.0), doubleArrayOf(0.0, 0.0, 4.0, -4.0))
            test("Chvatal, p. 234", payoff)
        }

        // Linear Programming by Chvatal, p. 236
        // row    = { 0, 2/5, 7/15, 0, 2/15, 0, 0, 0 }
        // column = { 2/3, 0, 0, 1/3 }
        // value  = -1/3
        private fun test4() {
            val payoff = arrayOf(doubleArrayOf(0.0, 2.0, -1.0, -1.0), doubleArrayOf(0.0, 1.0, -2.0, -1.0), doubleArrayOf(-1.0, -1.0, 1.0, 1.0), doubleArrayOf(-1.0, 0.0, 0.0, 1.0), doubleArrayOf(1.0, -2.0, 0.0, -3.0), doubleArrayOf(1.0, -1.0, -1.0, -3.0), doubleArrayOf(0.0, -3.0, 2.0, -1.0), doubleArrayOf(0.0, -2.0, 1.0, -1.0))
            test("Chvatal p. 236", payoff)
        }

        // rock, paper, scissors
        // row    = { 1/3, 1/3, 1/3 }
        // column = { 1/3, 1/3, 1/3 }
        private fun test5() {
            val payoff = arrayOf(doubleArrayOf(0.0, -1.0, 1.0), doubleArrayOf(1.0, 0.0, -1.0), doubleArrayOf(-1.0, 1.0, 0.0))
            test("rock, paper, scissors", payoff)
        }

        /**
         * Unit tests the `ZeroSumGameToLP` data type.
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

            val m = Integer.parseInt(args[0])
            val n = Integer.parseInt(args[1])
            val payoff = Array(m) { DoubleArray(n) { StdRandom.uniform(-0.5, 0.5) } }
            test("random $m-by-$n", payoff)
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
