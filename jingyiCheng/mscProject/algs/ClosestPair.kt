/******************************************************************************
 * Compilation:  javac ClosestPair.java
 * Execution:    java ClosestPair < input.txt
 * Dependencies: Point2D.kt
 * Data files:   https://algs4.cs.princeton.edu/99hull/rs1423.txt
 * https://algs4.cs.princeton.edu/99hull/kw1260.txt
 *
 * Given n points in the plane, find the closest pair in n log n time.
 *
 * Note: could speed it up by comparing square of Euclidean distances
 * instead of Euclidean distances.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `ClosestPair` data type computes a closest pair of points
 * in a set of *n* points in the plane and provides accessor methods
 * for getting the closest pair of points and the distance between them.
 * The distance between two points is their Euclidean distance.
 *
 *
 * This implementation uses a divide-and-conquer algorithm.
 * It runs in O(*n* log *n*) time in the worst case and uses
 * O(*n*) extra space.
 *
 *
 * See also [FarthestPair].
 *
 *
 * For additional documentation, see [Section 9.9](https://algs4.cs.princeton.edu/99hull) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class ClosestPair
/**
 * Computes the closest pair of points in the specified array of points.
 *
 * @param  points the array of points
 * @throws IllegalArgumentException if `points` is `null` or if any
 * entry in `points[]` is `null`
 */
(points: Array<Point2D>) {
    // closest pair of points and their Euclidean distance
    var either: Point2D? = null
        private set
    var other: Point2D? = null
        private set
    var bestDistance = Double.POSITIVE_INFINITY
        private set

    init {
        run {
            val n = points.size
            if (n <= 1) return@run

            // sort by x-coordinate (breaking ties by y-coordinate)
            val pointsByX = Array(n) { points[it] }
            pointsByX.sortWith(Point2D.X_ORDER)

            // check for coincident points
            for (i in 0 until n - 1) {
                if (pointsByX[i] == pointsByX[i + 1]) {
                    bestDistance = 0.0
                    either = pointsByX[i]
                    other = pointsByX[i + 1]
                    return@run
                }
            }

            // sort by y-coordinate (but not yet sorted)
            val pointsByY = Array(n) { pointsByX[it] }

            // auxiliary array
            val aux = Array(n) { Point2D(0.0, 0.0) }
            closest(pointsByX, pointsByY, aux, 0, n - 1)
        }
    }

    // find closest pair of points in pointsByX[lo..hi]
    // precondition:  pointsByX[lo..hi] and pointsByY[lo..hi] are the same sequence of points
    // precondition:  pointsByX[lo..hi] sorted by x-coordinate
    // postcondition: pointsByY[lo..hi] sorted by y-coordinate
    private fun closest(pointsByX: Array<Point2D>, pointsByY: Array<Point2D>, aux: Array<Point2D>, lo: Int, hi: Int): Double {
        if (hi <= lo) return Double.POSITIVE_INFINITY
        val mid = lo + (hi - lo) / 2
        val median = pointsByX[mid]

        // compute closest pair with both endpoints in left subarray or both in right subarray
        val delta1 = closest(pointsByX, pointsByY, aux, lo, mid)
        val delta2 = closest(pointsByX, pointsByY, aux, mid + 1, hi)
        var delta = Math.min(delta1, delta2)

        // merge back so that pointsByY[lo..hi] are sorted by y-coordinate
        merge(pointsByY, aux, lo, mid, hi)

        // aux[0..m-1] = sequence of points closer than delta, sorted by y-coordinate
        var m = 0
        for (i in lo..hi) {
            if (Math.abs(pointsByY[i].x - median.x) < delta)
                aux[m++] = pointsByY[i]
        }

        // compare each point to its neighbors with y-coordinate closer than delta
        for (i in 0 until m) {
            // a geometric packing argument shows that this loop iterates at most 7 times
            var j = i + 1
            while (j < m && aux[j].y - aux[i].y < delta) {
                val distance = aux[i].distanceTo(aux[j])
                if (distance < delta) {
                    delta = distance
                    if (distance < bestDistance) {
                        bestDistance = delta
                        either = aux[i]
                        other = aux[j]
                        // StdOut.println("better distance = " + delta + " from " + either + " to " + other);
                    }
                }
                j++
            }
        }
        return delta
    }

    companion object {
        // is v < w ?
        private fun <T : Comparable<T>> less(v: T, w: T) = v < w

        // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
        // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
        private fun <T : Comparable<T>> merge(a: Array<T>, aux: Array<T>, lo: Int, mid: Int, hi: Int) {
            // copy to aux[]
            for (k in lo..hi) {
                aux[k] = a[k]
            }

            // merge back to a[]
            var i = lo
            var j = mid + 1
            for (k in lo..hi) {
                when {
                    i > mid -> a[k] = aux[j++]
                    j > hi -> a[k] = aux[i++]
                    less(aux[j], aux[i]) -> a[k] = aux[j++]
                    else -> a[k] = aux[i++]
                }
            }
        }

        /**
         * Unit tests the `ClosestPair` data type.
         * Reads in an integer `n` and `n` points (specified by
         * their *x*- and *y*-coordinates) from standard input;
         * computes a closest pair of points; and prints the pair to standard
         * output.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val n = StdIn.readInt()
            val points = Array(n) { Point2D(StdIn.readDouble(), StdIn.readDouble()) }
            val closest = ClosestPair(points)
            StdOut.println("${closest.bestDistance} from ${closest.either} to ${closest.other}")
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
