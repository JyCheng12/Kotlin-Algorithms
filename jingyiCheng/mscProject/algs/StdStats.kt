/******************************************************************************
 * Compilation:  javac StdStats.java
 * Execution:    java StdStats < input.txt
 * Dependencies: StdOut.kt
 *
 * Library of statistical functions.
 *
 * The test client reads an array of real numbers from standard
 * input, and computes the minimum, mean, maximum, and
 * standard deviation.
 *
 * The functions all throw a IllegalArgumentException
 * if the array passed in as an argument is null.
 *
 * The floating-point functions all return NaN if any input is NaN.
 *
 * Unlike Math.min() and Math.max(), the min() and max() functions
 * do not differentiate between -0.0 and 0.0.
 *
 * % more tiny.txt
 * 5
 * 3.0 1.0 2.0 5.0 4.0
 *
 * % java StdStats < tiny.txt
 *        min   1.000
 *       mean   3.000
 *        max   5.000
 *    std dev   1.581
 *
 * Should these funtions use varargs instead of array arguments?
 *
 ******************************************************************************/

package jingyiCheng.mscProject.algs

/**
 *  The {@code StdStats} class provides static methods for computing
 *  statistics such as min, max, mean, sample standard deviation, and
 *  sample variance.
 *  <p>
 *  For additional documentation, see
 *  <a href="https://introcs.cs.princeton.edu/22library">Section 2.2</a> of
 *  <i>Computer Science: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Jingyi Cheng
 *
 */
object StdStats {
    /**
     * Returns the maximum value in the specified array.
     *
     * @param  a the array
     * @return the maximum value in the array `a[]`;
     * `Double.NEGATIVE_INFINITY` if no such value
     */
    fun max(a: DoubleArray): Double {
        validateNotNull(a)
        var max = Double.NEGATIVE_INFINITY
        for (i in a) {
            if (i.isNaN()) return Double.NaN
            if (i > max) max = i
        }
        return max
    }

    /**
     * Returns the maximum value in the specified subarray.
     *
     * @param  a the array
     * @param  lo the left endpoint of the subarray (inclusive)
     * @param  hi the right endpoint of the subarray (exclusive)
     * @return the maximum value in the subarray `a[lo..hi)`;
     * `Double.NEGATIVE_INFINITY` if no such value
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    fun max(a: DoubleArray, lo: Int, hi: Int): Double {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)

        var max = Double.NEGATIVE_INFINITY
        for (i in lo until hi) {
            if (a[i].isNaN()) return Double.NaN
            if (a[i] > max) max = a[i]
        }
        return max
    }

    /**
     * Returns the maximum value in the specified array.
     *
     * @param  a the array
     * @return the maximum value in the array `a[]`;
     * `Integer.MIN_VALUE` if no such value
     */
    fun max(a: IntArray): Int {
        validateNotNull(a)
        var max = Integer.MIN_VALUE
        a.forEach { i -> if (i > max) max = i }
        return max
    }

    /**
     * Returns the minimum value in the specified array.
     *
     * @param  a the array
     * @return the minimum value in the array `a[]`;
     * `Double.POSITIVE_INFINITY` if no such value
     */
    fun min(a: DoubleArray): Double {
        validateNotNull(a)
        var min = Double.POSITIVE_INFINITY
        a.forEach { i ->
            if (i.isNaN()) return Double.NaN
            if (i < min) min = i
        }
        return min
    }

    /**
     * Returns the minimum value in the specified subarray.
     *
     * @param  a the array
     * @param  lo the left endpoint of the subarray (inclusive)
     * @param  hi the right endpoint of the subarray (exclusive)
     * @return the maximum value in the subarray `a[lo..hi)`;
     * `Double.POSITIVE_INFINITY` if no such value
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    fun min(a: DoubleArray, lo: Int, hi: Int): Double {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)

        var min = Double.POSITIVE_INFINITY
        for (i in lo until hi) {
            if (a[i].isNaN()) return Double.NaN
            if (a[i] < min) min = a[i]
        }
        return min
    }

    /**
     * Returns the minimum value in the specified array.
     *
     * @param  a the array
     * @return the minimum value in the array `a[]`;
     * `Integer.MAX_VALUE` if no such value
     */
    fun min(a: IntArray): Int {
        validateNotNull(a)

        var min = Integer.MAX_VALUE
        a.forEach { i -> if (i < min) min = i }
        return min
    }

    /**
     * Returns the average value in the specified array.
     *
     * @param  a the array
     * @return the average value in the array `a[]`;
     * `Double.NaN` if no such value
     */
    fun mean(a: DoubleArray): Double {
        validateNotNull(a)

        if (a.isEmpty()) return Double.NaN
        val sum = sum(a)
        return sum / a.size
    }

    /**
     * Returns the average value in the specified subarray.
     *
     * @param a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (exclusive)
     * @return the average value in the subarray `a[lo..hi)`;
     * `Double.NaN` if no such value
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    fun mean(a: DoubleArray, lo: Int, hi: Int): Double {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)

        val length = hi - lo
        if (length == 0) return Double.NaN

        val sum = sum(a, lo, hi)
        return sum / length
    }

    /**
     * Returns the average value in the specified array.
     *
     * @param  a the array
     * @return the average value in the array `a[]`;
     * `Double.NaN` if no such value
     */
    fun mean(a: IntArray): Double {
        validateNotNull(a)

        if (a.isEmpty()) return Double.NaN
        val sum = sum(a)
        return 1.0 * sum / a.size
    }

    /**
     * Returns the sample variance in the specified array.
     *
     * @param  a the array
     * @return the sample variance in the array `a[]`;
     * `Double.NaN` if no such value
     */
    fun `var`(a: DoubleArray): Double {
        validateNotNull(a)

        if (a.isEmpty()) return Double.NaN
        val avg = mean(a)
        var sum = 0.0
        a.forEach { i -> sum += (i - avg) * (i - avg) }
        return sum / (a.size - 1)
    }

    /**
     * Returns the sample variance in the specified subarray.
     *
     * @param  a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (exclusive)
     * @return the sample variance in the subarray `a[lo..hi)`;
     * `Double.NaN` if no such value
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    fun `var`(a: DoubleArray, lo: Int, hi: Int): Double {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)

        val length = hi - lo
        if (length == 0) return Double.NaN

        val avg = mean(a, lo, hi)
        var sum = 0.0
        for (i in lo until hi) {
            sum += (a[i] - avg) * (a[i] - avg)
        }
        return sum / (length - 1)
    }

    /**
     * Returns the sample variance in the specified array.
     *
     * @param  a the array
     * @return the sample variance in the array `a[]`;
     * `Double.NaN` if no such value
     */
    fun `var`(a: IntArray): Double {
        validateNotNull(a)
        if (a.isEmpty()) return Double.NaN
        val avg = mean(a)
        var sum = 0.0
        a.forEach { i -> sum += (i - avg) * (i - avg) }
        return sum / (a.size - 1)
    }

    /**
     * Returns the population variance in the specified array.
     *
     * @param  a the array
     * @return the population variance in the array `a[]`;
     * `Double.NaN` if no such value
     */
    fun varp(a: DoubleArray): Double {
        validateNotNull(a)
        if (a.isEmpty()) return Double.NaN
        val avg = mean(a)
        var sum = 0.0
        a.forEach { i -> sum += (i - avg) * (i - avg) }
        return sum / a.size
    }

    /**
     * Returns the population variance in the specified subarray.
     *
     * @param  a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (exclusive)
     * @return the population variance in the subarray `a[lo..hi)`;
     * `Double.NaN` if no such value
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    fun varp(a: DoubleArray, lo: Int, hi: Int): Double {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)

        val length = hi - lo
        if (length == 0) return Double.NaN

        val avg = mean(a, lo, hi)
        var sum = 0.0
        for (i in lo until hi) {
            sum += (a[i] - avg) * (a[i] - avg)
        }
        return sum / length
    }

    /**
     * Returns the sample standard deviation in the specified array.
     *
     * @param  a the array
     * @return the sample standard deviation in the array `a[]`;
     * `Double.NaN` if no such value
     */
    fun stddev(a: DoubleArray): Double {
        validateNotNull(a)
        return Math.sqrt(`var`(a))
    }

    /**
     * Returns the sample standard deviation in the specified array.
     *
     * @param  a the array
     * @return the sample standard deviation in the array `a[]`;
     * `Double.NaN` if no such value
     */
    fun stddev(a: IntArray): Double {
        validateNotNull(a)
        return Math.sqrt(`var`(a))
    }

    /**
     * Returns the sample standard deviation in the specified subarray.
     *
     * @param  a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (exclusive)
     * @return the sample standard deviation in the subarray `a[lo..hi)`;
     * `Double.NaN` if no such value
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    fun stddev(a: DoubleArray, lo: Int, hi: Int): Double {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)
        return Math.sqrt(`var`(a, lo, hi))
    }


    /**
     * Returns the population standard deviation in the specified array.
     *
     * @param  a the array
     * @return the population standard deviation in the array;
     * `Double.NaN` if no such value
     */
    fun stddevp(a: DoubleArray): Double {
        validateNotNull(a)
        return Math.sqrt(varp(a))
    }

    /**
     * Returns the population standard deviation in the specified subarray.
     *
     * @param  a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (exclusive)
     * @return the population standard deviation in the subarray `a[lo..hi)`;
     * `Double.NaN` if no such value
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    fun stddevp(a: DoubleArray, lo: Int, hi: Int): Double {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)
        return Math.sqrt(varp(a, lo, hi))
    }

    /**
     * Returns the sum of all values in the specified array.
     *
     * @param  a the array
     * @return the sum of all values in the array `a[]`;
     * `0.0` if no such value
     */
    private fun sum(a: DoubleArray): Double {
        validateNotNull(a)
        var sum = 0.0
        a.forEach { i -> sum += i }
        return sum
    }

    /**
     * Returns the sum of all values in the specified subarray.
     *
     * @param  a the array
     * @param lo the left endpoint of the subarray (inclusive)
     * @param hi the right endpoint of the subarray (exclusive)
     * @return the sum of all values in the subarray `a[lo..hi)`;
     * `0.0` if no such value
     * @throws IllegalArgumentException if `a` is `null`
     * @throws IllegalArgumentException unless `(0 <= lo) && (lo < hi) && (hi <= a.length)`
     */
    private fun sum(a: DoubleArray, lo: Int, hi: Int): Double {
        validateNotNull(a)
        validateSubarrayIndices(lo, hi, a.size)
        var sum = 0.0
        for (i in lo until hi)
            sum += a[i]
        return sum
    }

    /**
     * Returns the sum of all values in the specified array.
     *
     * @param  a the array
     * @return the sum of all values in the array `a[]`;
     * `0.0` if no such value
     */
    private fun sum(a: IntArray): Int {
        validateNotNull(a)
        var sum = 0
        a.forEach { i -> sum += i }
        return sum
    }

    /**
     * Plots the points (0, *a*<sub>0</sub>), (1, *a*<sub>1</sub>), ...,
     * (*n*–1, *a*<sub>*n*–1</sub>) to standard draw.
     *
     * @param a the array of values
     */
    fun plotPoints(a: DoubleArray) {
        validateNotNull(a)
        val n = a.size
        StdDraw.setXscale(-1.0, n.toDouble())
        StdDraw.penRadius = 1.0 / (3.0 * n)
        for (i in 0 until n)
            StdDraw.point(i.toDouble(), a[i])
    }

    /**
     * Plots the line segments connecting
     * (*i*, *a*<sub>*i*</sub>) to
     * (*i*+1, *a*<sub>*i*+1</sub>) for
     * each *i* to standard draw.
     *
     * @param a the array of values
     */
    fun plotLines(a: DoubleArray) {
        validateNotNull(a)
        val n = a.size
        StdDraw.setXscale(-1.0, n.toDouble())
        StdDraw.setPenRadius()
        for (i in 1 until n)
            StdDraw.line((i - 1).toDouble(), a[i - 1], i.toDouble(), a[i])
    }

    /**
     * Plots bars from (0, *a*<sub>*i*</sub>) to
     * (*a*<sub>*i*</sub>) for each *i*
     * to standard draw.
     *
     * @param a the array of values
     */
    fun plotBars(a: DoubleArray) {
        validateNotNull(a)
        val n = a.size
        StdDraw.setXscale(-1.0, n.toDouble())
        for (i in 0 until n)
            StdDraw.filledRectangle(i.toDouble(), a[i] / 2, 0.25, a[i] / 2)
    }

    // throw an IllegalArgumentException if x is null
    // (x is either of type double[] or int[])
    private fun validateNotNull(x: Any?) {
        if (x == null) throw IllegalArgumentException("argument is null")
    }

    // throw an exception unless 0 <= lo <= hi <= length
    private fun validateSubarrayIndices(lo: Int, hi: Int, length: Int) {
        if (lo < 0 || hi > length || lo > hi) throw IllegalArgumentException("subarray indices out of bounds: [$lo, $hi)")
    }

    /**
     * Unit tests `StdStats`.
     * Convert command-line arguments to array of doubles and call various methods.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val a = StdArrayIO.readDouble1D()
        StdOut.printf("       min %10.3f\n", min(a))
        StdOut.printf("      mean %10.3f\n", mean(a))
        StdOut.printf("       max %10.3f\n", max(a))
        StdOut.printf("    stddev %10.3f\n", stddev(a))
        StdOut.printf("       var %10.3f\n", `var`(a))
        StdOut.printf("   stddevp %10.3f\n", stddevp(a))
        StdOut.printf("      varp %10.3f\n", varp(a))
    }
}

/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
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
 ******************************************************************************/
