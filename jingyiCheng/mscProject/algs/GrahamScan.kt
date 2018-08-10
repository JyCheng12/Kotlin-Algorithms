/******************************************************************************
 * Compilation:  javac GrahamaScan.java
 * Execution:    java GrahamScan < input.txt
 * Dependencies: Point2D.kt
 * Data files:   https://algs4.cs.princeton.edu/99hull/rs1423.txt
 * https://algs4.cs.princeton.edu/99hull/kw1260.txt
 *
 * Create points from standard input and compute the convex hull using
 * Graham scan algorithm.
 *
 * May be floating-point issues if x- and y-coordinates are not integers.
 *
 * % java GrahamScan < input100.txt
 * (7486.0, 422.0)
 * (29413.0, 596.0)
 * (32011.0, 3140.0)
 * (30875.0, 28560.0)
 * (28462.0, 32343.0)
 * (15731.0, 32661.0)
 * (822.0, 32301.0)
 * (823.0, 15895.0)
 * (1444.0, 10362.0)
 * (4718.0, 4451.0)
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `GrahamScan` data type provides methods for computing the
 * convex hull of a set of *n* points in the plane.
 *
 *
 * The implementation uses the Graham-Scan convex hull algorithm.
 * It runs in O(*n* log *n*) time in the worst case
 * and uses O(*n*) extra memory.
 *
 *
 * For additional documentation, see [Section 9.9](https://algs4.cs.princeton.edu/99scientific) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class GrahamScan
/**
 * Computes the convex hull of the specified array of points.
 *
 * @param  points the array of points
 * @throws IllegalArgumentException if `points` is `null`
 * @throws IllegalArgumentException if any entry in `points[]` is `null`
 * @throws IllegalArgumentException if `points.length` is `0`
 */
(points: Array<Point2D>?) {
    val hull = nnStack<Point2D>()

    // check that boundary of hull is strictly convex
    private val isConvex: Boolean
        get() {
            val n = hull.size
            if (n <= 2) return true
            val points = Array(n){Point2D(0.0,0.0)}
            var k = 0
            for (p in hull()) {
                points[k++] = p
            }
            for (i in 0 until n) {
                if (Point2D.ccw(points[i], points[(i + 1) % n], points[(i + 2) % n]) <= 0) {
                    return false
                }
            }
            return true
        }

    init {
        run{
            if (points == null) throw IllegalArgumentException("argument is null")
            if (points.isEmpty()) throw IllegalArgumentException("array is of length 0")

            // defensive copy
            val n = points.size
            val a = Array(n) {points[it]}

            // preprocess so that a[0] has lowest y-coordinate; break ties by x-coordinate
            // a[0] is an extreme point of the convex hull
            // (alternatively, could do easily in linear time)
            a.sort()

            // sort by polar angle with respect to base point a[0],
            // breaking ties by distance to a[0]
            a.sortWith(a[0].polarOrder(), 1, n)

            hull.push(a[0])       // a[0] is first extreme point

            // find index k1 of first point not equal to a[0]
            var k1 = 1
            while (k1 < n) {
                if (a[0] != a[k1]) break
                k1++
            }
            if (k1 == n) return@run         // all points equal

            // find index k2 of first point not collinear with a[0] and a[k1]
            var k2: Int
            k2 = k1 + 1
            while (k2 < n) {
                if (Point2D.ccw(a[0], a[k1], a[k2]) != 0) break
                k2++
            }
            hull.push(a[k2 - 1])    // a[k2-1] is second extreme point


            // Graham scan; note that a[n-1] is extreme point different from a[0]
            for (i in k2 until n) {
                var top = hull.pop()
                while (Point2D.ccw(hull.peek(), top, a[i]) <= 0) {
                    top = hull.pop()
                }
                hull.push(top)
                hull.push(a[i])
            }
            StdOut.println(hull.size)
            assert(isConvex)
        }

    }

    /**
     * Returns the extreme points on the convex hull in counterclockwise order.
     *
     * @return the extreme points on the convex hull in counterclockwise order
     */
    fun hull(): Iterable<Point2D> {
        val s = nnStack<Point2D>()
        for (p in hull) s.push(p)
        return s
    }

    companion object {

        /**
         * Unit tests the `GrahamScan` data type.
         * Reads in an integer `n` and `n` points (specified by
         * their *x*- and *y*-coordinates) from standard input;
         * computes their convex hull; and prints out the points on the
         * convex hull to standard output.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val n = StdIn.readInt()
            val points = Array(n){Point2D(StdIn.readDouble(), StdIn.readDouble())}
            val graham = GrahamScan(points)

            for (p in graham.hull())
                StdOut.println(p)
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
