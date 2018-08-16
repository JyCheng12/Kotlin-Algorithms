/******************************************************************************
 * Compilation:  javac Interval1D.java
 * Execution:    java Interval1D
 * Dependencies: StdOut.kt
 *
 * 1-dimensional interval data type.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Interval1D` class represents a one-dimensional interval.
 * The interval is *closed*—it contains both endpoints.
 * Intervals are immutable: their values cannot be changed after they are created.
 * The class `Interval1D` includes methods for checking whether
 * an interval contains a point and determining whether two intervals intersect.
 *
 *
 * For additional documentation,
 * see [Section 1.2](https://algs4.cs.princeton.edu/12oop) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class Interval1D
/**
 * Initializes a closed interval [min, max].
 *
 * @param  min the smaller endpoint
 * @param  max the larger endpoint
 * @throws IllegalArgumentException if the min endpoint is greater than the max endpoint
 * @throws IllegalArgumentException if either `min` or `max`
 * is `Double.NaN`, `Double.POSITIVE_INFINITY` or
 * `Double.NEGATIVE_INFINITY`
 */
(val min: Double, val max: Double) {
    init {
        if (min.isInfinite() || max.isInfinite()) throw IllegalArgumentException("Endpoints must be finite")
        if (min.isNaN() || max.isNaN()) throw IllegalArgumentException("Endpoints cannot be NaN")
        if (min > max) throw IllegalArgumentException("Illegal interval")
    }

    var length = 0.0
        get() = max - min

    /**
     * Returns true if this interval intersects the specified interval.
     *
     * @param  that the other interval
     * @return `true` if this interval intersects the argument interval;
     * `false` otherwise
     */
    fun intersects(that: Interval1D): Boolean {
        if (this.max < that.min) return false
        return that.max >= this.min
    }

    /**
     * Returns true if this interval contains the specified value.
     *
     * @param x the value
     * @return `true` if this interval contains the value `x`;
     * `false` otherwise
     */
    operator fun contains(x: Double) = x in min..max

    /**
     * Returns a string representation of this interval.
     *
     * @return a string representation of this interval in the form [min, max]
     */
    override fun toString() = "[$min, $max]"

    /**
     * Compares this transaction to the specified object.
     *
     * @param  other the other interval
     * @return `true` if this interval equals the other interval;
     * `false` otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other.javaClass != this.javaClass) return false
        val that = other as Interval1D?
        return this.min == that!!.min && this.max == that.max
    }

    /**
     * Returns an integer hash code for this interval.
     *
     * @return an integer hash code for this interval
     */
    override fun hashCode(): Int {
        val hash1 = min.hashCode()
        val hash2 = max.hashCode()
        return 31 * hash1 + hash2
    }

    // ascending order of min endpoint, breaking ties by max endpoint
    private class MinEndpointComparator : Comparator<Interval1D> {
        override fun compare(a: Interval1D, b: Interval1D) = when {
            a.min < b.min -> -1
            a.min > b.min -> +1
            a.max < b.max -> -1
            a.max > b.max -> +1
            else -> 0
        }
    }

    // ascending order of max endpoint, breaking ties by min endpoint
    private class MaxEndpointComparator : Comparator<Interval1D> {
        override fun compare(a: Interval1D, b: Interval1D) = when {
            a.max < b.max -> -1
            a.max > b.max -> +1
            a.min < b.min -> -1
            a.min > b.min -> +1
            else -> 0
        }
    }

    // ascending order of length
    private class LengthComparator : Comparator<Interval1D> {
        override fun compare(a: Interval1D, b: Interval1D) = when {
            a.length < b.length -> -1
            a.length > b.length -> +1
            else -> 0
        }
    }

    companion object {

        /**
         * Compares two intervals by min endpoint.
         */
        val MIN_ENDPOINT_ORDER: Comparator<Interval1D> = MinEndpointComparator()

        /**
         * Compares two intervals by max endpoint.
         */
        val MAX_ENDPOINT_ORDER: Comparator<Interval1D> = MaxEndpointComparator()

        /**
         * Compares two intervals by length.
         */
        val LENGTH_ORDER: Comparator<Interval1D> = LengthComparator()

        /**
         * Unit tests the `Interval1D` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val intervals = arrayOf(
                    Interval1D(15.0, 33.0),
                    Interval1D(45.0, 60.0),
                    Interval1D(20.0, 70.0),
                    Interval1D(46.0, 55.0)
            )

            StdOut.println("Unsorted")
            for (i in intervals)
                StdOut.println(i)
            StdOut.println()

            StdOut.println("Sort by min endpoint")
            intervals.sortWith(Interval1D.MIN_ENDPOINT_ORDER)
            for (i in intervals)
                StdOut.println(i)
            StdOut.println()

            StdOut.println("Sort by max endpoint")
            intervals.sortWith(Interval1D.MAX_ENDPOINT_ORDER)
            for (i in intervals)
                StdOut.println(i)
            StdOut.println()

            StdOut.println("Sort by length")
            intervals.sortWith(Interval1D.LENGTH_ORDER)
            for (i in intervals)
                StdOut.println(i)
            StdOut.println()
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