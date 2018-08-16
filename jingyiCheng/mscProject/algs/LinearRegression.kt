/******************************************************************************
 * Compilation:  javac LinearRegression.java
 * Execution:    java  LinearRegression
 * Dependencies: none
 *
 * Compute least squares solution to y = beta * x + alpha.
 * Simple linear regression.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LinearRegression` class performs a simple linear regression
 * on an set of *n* data points (*y<sub>i</sub>*, *x<sub>i</sub>*).
 * That is, it fits a straight line *y* =  +  *x*,
 * (where *y* is the response variable, *x* is the predictor variable,
 *  is the *y-intercept*, and  is the *slope*)
 * that minimizes the sum of squared residuals of the linear regression model.
 * It also computes associated statistics, including the coefficient of
 * determination *R*<sup>2</sup> and the standard deviation of the
 * estimates for the slope and *y*-intercept.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class LinearRegression
/**
 * Performs a linear regression on the data points `(y[i], x[i])`.
 *
 * @param  x the values of the predictor variable
 * @param  y the corresponding values of the response variable
 * @throws IllegalArgumentException if the lengths of the two arrays are not equal
 */
(x: DoubleArray, y: DoubleArray) {
    val intercept: Double
    val slope: Double
    val r2: Double
    private val svar0: Double
    private val svar1: Double

    init {
        if (x.size != y.size) throw IllegalArgumentException("array lengths are not equal")

        val n = x.size

        // first pass
        var sumx = 0.0
        var sumy = 0.0
        var sumx2 = 0.0
        for (i in 0 until n) {
            sumx += x[i]
            sumx2 += x[i] * x[i]
            sumy += y[i]
        }
        val xbar = sumx / n
        val ybar = sumy / n

        // second pass: compute summary statistics
        var xxbar = 0.0
        var yybar = 0.0
        var xybar = 0.0
        for (i in 0 until n) {
            xxbar += (x[i] - xbar) * (x[i] - xbar)
            yybar += (y[i] - ybar) * (y[i] - ybar)
            xybar += (x[i] - xbar) * (y[i] - ybar)
        }
        slope = xybar / xxbar
        intercept = ybar - slope * xbar

        // more statistical analysis
        var rss = 0.0      // residual sum of squares
        var ssr = 0.0      // regression sum of squares
        for (i in 0 until n) {
            val fit = slope * x[i] + intercept
            rss += (fit - y[i]) * (fit - y[i])
            ssr += (fit - ybar) * (fit - ybar)
        }

        val degreesOfFreedom = n - 2
        r2 = ssr / yybar
        val svar = rss / degreesOfFreedom
        svar1 = svar / xxbar
        svar0 = svar / n + xbar * xbar * svar1
    }

    /**
     * Returns the standard error of the estimate for the intercept.
     *
     * @return the standard error of the estimate for the intercept
     */
    fun interceptStdErr() = Math.sqrt(svar0)

    /**
     * Returns the standard error of the estimate for the slope.
     *
     * @return the standard error of the estimate for the slope
     */
    fun slopeStdErr() = Math.sqrt(svar1)

    /**
     * Returns the expected response `y` given the value of the predictor
     * variable `x`.
     *
     * @param  x the value of the predictor variable
     * @return the expected response `y` given the value of the predictor
     * variable `x`
     */
    fun predict(x: Double) = slope * x + intercept

    /**
     * Returns a string representation of the simple linear regression model.
     *
     * @return a string representation of the simple linear regression model,
     * including the best-fit line and the coefficient of determination
     * *R*<sup>2</sup>
     */
    override fun toString(): String {
        val s = StringBuilder()
        s.append(String.format("%.2f n + %.2f", slope, intercept))
        s.append("  (R^2 = " + String.format("%.3f", r2) + ")")
        return s.toString()
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