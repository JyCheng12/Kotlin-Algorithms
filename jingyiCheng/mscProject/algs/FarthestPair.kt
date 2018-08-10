/******************************************************************************
 * Compilation:  javac FarthestPair.java
 * Execution:    java FarthestPair < input.txt
 * Dependencies: GrahamScan.kt Point2D.kt
 * Data files:   https://algs4.cs.princeton.edu/99hull/rs1423.txt
 * https://algs4.cs.princeton.edu/99hull/kw1260.txt
 *
 * Given a set of n points in the plane, find the farthest pair
 * (equivalently, compute the diameter of the set of points).
 *
 * Computes the convex hull of the set of points and using the
 * rotating calipers method to find all antipodal point pairs
 * and the farthest pair.
 *
 * % java FarthestPair < input100.txt
 * 42697.98170874122 from (32011.0, 3140.0) to (822.0, 32301.0)
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `FarthestPair` data type computes the farthest pair of points
 * in a set of *n* points in the plane and provides accessor methods
 * for getting the farthest pair of points and the distance between them.
 * The distance between two points is their Euclidean distance.
 *
 *
 * This implementation computes the convex hull of the set of points and
 * uses the rotating calipers method to find all antipodal point pairs
 * and the farthest pair.
 * It runs in O(*n* log *n*) time in the worst case and uses
 * O(*N*) extra space.
 * See also [ClosestPair] and [GrahamScan].
 *
 *
 * For additional documentation, see [Section 9.9](https://algs4.cs.princeton.edu/99hull) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
class FarthestPair
/**
 * Computes the farthest pair of points in the specified array of points.
 *
 * @param  points the array of points
 * @throws IllegalArgumentException if `points` is `null` or if any
 * entry in `points[]` is `null`
 */
(points: Array<Point2D>?) {
    // farthest pair of points and distance
    lateinit var either: Point2D
        private set
    lateinit var other: Point2D
        private set
    private var bestDistanceSquared = Double.NEGATIVE_INFINITY

    init {
        run {
            if (points == null) throw IllegalArgumentException("constructor argument is null")
            val graham = GrahamScan(points)

            // single point
            if (points.size <= 1) return@run

            // the hull, in counterclockwise order hull[1] to hull[m]
            val hull = Array(graham.hull.size) { Point2D(0.0, 0.0) }
            var m = 0
            for (p in graham.hull()) {
                hull[m++] = p
            }
            m--

            // all points are equal
            if (m == 0) return@run

            // points are collinear
            if (m == 1) {
                either = hull[1]
                other = hull[2]
                bestDistanceSquared = either.distanceSquaredTo(other)
                return@run
            }

            // k = farthest vertex from edge from hull[1] to hull[m]
            var k = 2
            while (Point2D.area2(hull[m], hull[1], hull[k + 1]) > Point2D.area2(hull[m], hull[1], hull[k])) {
                k++
            }

            var j = k
            var i = 1
            while (i <= k && j <= m) {
                // StdOut.println("hull[i] + " and " + hull[j] + " are antipodal");
                if (hull[i].distanceSquaredTo(hull[j]) > bestDistanceSquared) {
                    either = hull[i]
                    other = hull[j]
                    bestDistanceSquared = hull[i].distanceSquaredTo(hull[j])
                }
                while (j < m && Point2D.area2(hull[i], hull[i + 1], hull[j + 1]) > Point2D.area2(hull[i], hull[i + 1], hull[j])) {
                    j++
                    // StdOut.println(hull[i] + " and " + hull[j] + " are antipodal");
                    val distanceSquared = hull[i].distanceSquaredTo(hull[j])
                    if (distanceSquared > bestDistanceSquared) {
                        either = hull[i]
                        other = hull[j]
                        bestDistanceSquared = hull[i].distanceSquaredTo(hull[j])
                    }
                }
                i++
            }
        }

    }

    /**
     * Returns the Eucliden distance between the farthest pair of points.
     * This quantity is also known as the *diameter* of the set of points.
     *
     * @return the Euclidean distance between the farthest pair of points
     * `Double.POSITIVE_INFINITY` if no such pair of points
     * exist (because there are fewer than 2 points)
     */
    fun distance() = Math.sqrt(bestDistanceSquared)

    companion object {

        /**
         * Unit tests the `FarthestPair` data type.
         * Reads in an integer `n` and `n` points (specified by
         * their *x*- and *y*-coordinates) from standard input;
         * computes a farthest pair of points; and prints the pair to standard
         * output.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val n = StdIn.readInt()
            val points = Array(n) { Point2D(StdIn.readDouble(), StdIn.readDouble()) }
            val farthest = FarthestPair(points)
            StdOut.println("${farthest.distance()} from ${farthest.either} to ${farthest.other}")
        }
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
