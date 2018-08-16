/******************************************************************************
 * Compilation:  javac Point2D.java
 * Execution:    java Point2D x0 y0 n
 * Dependencies: StdDraw.kt StdRandom.kt
 *
 * Immutable point data type for points in the plane.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Point` class is an immutable data type to encapsulate a
 * two-dimensional point with real-value coordinates.
 *
 *
 * Note: in order to deal with the difference behavior of double and
 * Double with respect to -0.0 and +0.0, the Point2D constructor converts
 * any coordinates that are -0.0 to +0.0.
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
class Point2D
/**
 * Initializes a new point (x, y).
 * @param x the x-coordinate
 * @param y the y-coordinate
 * @throws IllegalArgumentException if either `x` or `y`
 * is `Double.NaN`, `Double.POSITIVE_INFINITY` or
 * `Double.NEGATIVE_INFINITY`
 */
(x: Double, y: Double) : Comparable<Point2D> {
    val x: Double    // x coordinate
    val y: Double    // y coordinate

    init {
        if (x.isInfinite() || y.isInfinite()) throw IllegalArgumentException("Coordinates must be finite")
        if (x.isNaN() || y.isNaN()) throw IllegalArgumentException("Coordinates cannot be NaN")
        if (x == 0.0) this.x = 0.0  // convert -0.0 to +0.0
        else this.x = x

        if (y == 0.0) this.y = 0.0  // convert -0.0 to +0.0
        else this.y = y
    }

    /**
     * Returns the polar radius of this point.
     * @return the polar radius of this point in polar coordiantes: sqrt(x*x + y*y)
     */
    fun r() = Math.sqrt(x * x + y * y)

    /**
     * Returns the angle of this point in polar coordinates.
     * @return the angle (in radians) of this point in polar coordiantes (between – and )
     */
    fun theta() = Math.atan2(y, x)

    /**
     * Returns the angle between this point and that point.
     * @return the angle in radians (between – and ) between this point and that point (0 if equal)
     */
    private fun angleTo(that: Point2D): Double {
        val dx = that.x - this.x
        val dy = that.y - this.y
        return Math.atan2(dy, dx)
    }

    /**
     * Returns the Euclidean distance between this point and that point.
     * @param that the other point
     * @return the Euclidean distance between this point and that point
     */
    fun distanceTo(that: Point2D): Double {
        val dx = this.x - that.x
        val dy = this.y - that.y
        return Math.sqrt(dx * dx + dy * dy)
    }

    /**
     * Returns the square of the Euclidean distance between this point and that point.
     * @param that the other point
     * @return the square of the Euclidean distance between this point and that point
     */
    fun distanceSquaredTo(that: Point2D): Double {
        val dx = this.x - that.x
        val dy = this.y - that.y
        return dx * dx + dy * dy
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point (x1, y1)
     * if and only if either `y0 < y1` or if `y0 == y1` and `x0 < x1`.
     *
     * @param  other the other point
     * @return the value `0` if this string is equal to the argument
     * string (precisely when `equals()` returns `true`);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    override fun compareTo(other: Point2D): Int {
        if (this.y < other.y) return -1
        if (this.y > other.y) return +1
        if (this.x < other.x) return -1
        return if (this.x > other.x) +1 else 0
    }

    /**
     * Compares two points by polar angle (between 0 and 2) with respect to this point.
     *
     * @return the comparator
     */
    fun polarOrder() = PolarOrder()

    /**
     * Compares two points by atan2() angle (between – and ) with respect to this point.
     *
     * @return the comparator
     */
    fun atan2Order() = Atan2Order()

    /**
     * Compares two points by distance to this point.
     *
     * @return the comparator
     */
    fun distanceToOrder() = DistanceToOrder()

    // compare points according to their x-coordinate
    internal class XOrder : Comparator<Point2D> {
        override fun compare(p: Point2D, q: Point2D): Int {
            if (p.x < q.x) return -1
            return if (p.x > q.x) +1 else 0
        }
    }

    // compare points according to their y-coordinate
    internal class YOrder : Comparator<Point2D> {
        override fun compare(p: Point2D, q: Point2D): Int {
            if (p.y < q.y) return -1
            return if (p.y > q.y) +1 else 0
        }
    }

    // compare points according to their polar radius
    internal class ROrder : Comparator<Point2D> {
        override fun compare(p: Point2D, q: Point2D): Int {
            val delta = p.x * p.x + p.y * p.y - (q.x * q.x + q.y * q.y)
            if (delta < 0) return -1
            return if (delta > 0) +1 else 0
        }
    }

    // compare other points relative to atan2 angle (bewteen -pi/2 and pi/2) they make with this Point
    inner class Atan2Order : Comparator<Point2D> {
        override fun compare(q1: Point2D, q2: Point2D): Int {
            val angle1 = angleTo(q1)
            val angle2 = angleTo(q2)
            return when {
                angle1 < angle2 -> -1
                angle1 > angle2 -> +1
                else -> 0
            }
        }
    }

    // compare other points relative to polar angle (between 0 and 2pi) they make with this Point
    inner class PolarOrder : Comparator<Point2D> {
        override fun compare(q1: Point2D, q2: Point2D): Int {
            val dx1 = q1.x - x
            val dy1 = q1.y - y
            val dx2 = q2.x - x
            val dy2 = q2.y - y

            return when {
                dy1 >= 0 && dy2 < 0 -> -1    // q1 above; q2 below
                dy2 >= 0 && dy1 < 0 -> +1    // q1 below; q2 above
                dy1 == 0.0 && dy2 == 0.0 -> when {// 3-collinear and horizontal
                    dx1 >= 0 && dx2 < 0 -> -1
                    dx2 >= 0 && dx1 < 0 -> +1
                    else -> 0
                }
                else -> -ccw(this@Point2D, q1, q2)
            }     // both above or below
        }
    }

    // compare points according to their distance to this point
    inner class DistanceToOrder : Comparator<Point2D> {
        override fun compare(p: Point2D, q: Point2D): Int {
            val dist1 = distanceSquaredTo(p)
            val dist2 = distanceSquaredTo(q)
            return when {
                dist1 < dist2 -> -1
                dist1 > dist2 -> +1
                else -> 0
            }
        }
    }

    /**
     * Compares this point to the specified point.
     *
     * @param  other the other point
     * @return `true` if this point equals `other`;
     * `false` otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other.javaClass != this.javaClass) return false
        val that = other as Point2D?
        return this.x == that!!.x && this.y == that.y
    }

    /**
     * Return a string representation of this point.
     * @return a string representation of this point in the format (x, y)
     */
    override fun toString() = "($x, $y)"

    /**
     * Returns an integer hash code for this point.
     * @return an integer hash code for this point
     */
    override fun hashCode(): Int {
        val hashX = x.hashCode()
        val hashY = y.hashCode()
        return 31 * hashX + hashY
    }

    /**
     * Plot this point using standard draw.
     */
    fun draw() = StdDraw.point(x, y)

    /**
     * Plot a line from this point to that point using standard draw.
     * @param that the other point
     */
    fun drawTo(that: Point2D?) = if (that != null) StdDraw.line(this.x, this.y, that.x, that.y)
    else throw IllegalArgumentException("attempts to draw a point2D which is null.")

    companion object {

        /**
         * Compares two points by x-coordinate.
         */
        val X_ORDER: Comparator<Point2D> = XOrder()

        /**
         * Compares two points by y-coordinate.
         */
        val Y_ORDER: Comparator<Point2D> = YOrder()

        /**
         * Compares two points by polar radius.
         */
        val R_ORDER: Comparator<Point2D> = ROrder()

        /**
         * Returns true if a→b→c is a counterclockwise turn.
         * @param a first point
         * @param b second point
         * @param c third point
         * @return { -1, 0, +1 } if a→b→c is a { clockwise, collinear; counterclocwise } turn.
         */
        fun ccw(a: Point2D, b: Point2D, c: Point2D): Int {
            val area2 = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)
            return when {
                area2 < 0 -> -1
                area2 > 0 -> +1
                else -> 0
            }
        }

        /**
         * Returns twice the signed area of the triangle a-b-c.
         * @param a first point
         * @param b second point
         * @param c third point
         * @return twice the signed area of the triangle a-b-c
         */
        fun area2(a: Point2D, b: Point2D, c: Point2D) = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)

        /**
         * Unit tests the point data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val x0 = Integer.parseInt(args[0])
            val y0 = Integer.parseInt(args[1])
            val n = Integer.parseInt(args[2])

            StdDraw.setCanvasSize(800, 800)
            StdDraw.setXscale(0.0, 100.0)
            StdDraw.setYscale(0.0, 100.0)
            StdDraw.penRadius = 0.005
            StdDraw.enableDoubleBuffering()

            val points = Array(n) {
                Point2D(StdRandom.uniform(100).toDouble(), StdRandom.uniform(100).toDouble())
            }
            for (i in 0 until n)
                points[i].draw()

            val p = Point2D(x0.toDouble(), y0.toDouble())
            StdDraw.penColor = StdDraw.RED
            StdDraw.penRadius = 0.02
            p.draw()

            // draw line segments from p to each point, one at a time, in polar order
            StdDraw.setPenRadius()
            StdDraw.penColor = StdDraw.BLUE
            points.sortWith(p.polarOrder())
            for (i in 0 until n) {
                p.drawTo(points[i])
                StdDraw.show()
                StdDraw.pause(100)
            }
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