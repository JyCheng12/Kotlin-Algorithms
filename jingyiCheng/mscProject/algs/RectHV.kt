/******************************************************************************
 * Compilation:  javac RectHV.java
 * Execution:    none
 * Dependencies: Point2D.kt
 *
 * Immutable data type for 2D axis-aligned rectangle.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `RectHV` class is an immutable data type to encapsulate a
 * two-dimensional axis-aligned rectagle with real-value coordinates.
 * The rectangle is *closed*—it includes the points on the boundary.
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

class RectHV
/**
 * Initializes a new rectangle [*xmin*, *xmax*]
 * x [*ymin*, *ymax*].
 *
 * @param  xmin the *x*-coordinate of the lower-left endpoint
 * @param  xmax the *x*-coordinate of the upper-right endpoint
 * @param  ymin the *y*-coordinate of the lower-left endpoint
 * @param  ymax the *y*-coordinate of the upper-right endpoint
 * @throws IllegalArgumentException if any of `xmin`,
 * `xmax`, `ymin`, or `ymax`
 * is `Double.NaN`.
 * @throws IllegalArgumentException if `xmax < xmin` or `ymax < ymin`.
 */
(val xmin: Double, val ymin: Double   // minimum x- and y-coordinates
 , val xmax: Double, val ymax: Double   // maximum x- and y-coordinates
) {
    init {
        if (xmin.isNaN() || xmax.isNaN()) throw IllegalArgumentException("x-coordinate is NaN: " + toString())
        if (ymin.isNaN() || ymax.isNaN()) throw IllegalArgumentException("y-coordinate is NaN: " + toString())
        if (xmax < xmin) throw IllegalArgumentException("xmax < xmin: " + toString())
        if (ymax < ymin) throw IllegalArgumentException("ymax < ymin: " + toString())
    }

    /**
     * Returns the width of this rectangle.
     *
     * @return the width of this rectangle `xmax - xmin`
     */
    fun width() = xmax - xmin


    /**
     * Returns the height of this rectangle.
     *
     * @return the height of this rectangle `ymax - ymin`
     */
    fun height() = ymax - ymin

    /**
     * Returns true if the two rectangles intersect. This includes
     * *improper intersections* (at points on the boundary
     * of each rectangle) and *nested intersctions*
     * (when one rectangle is contained inside the other)
     *
     * @param  that the other rectangle
     * @return `true` if this rectangle intersect the argument
     * rectangle at one or more points
     */
    fun intersects(that: RectHV) =
            this.xmax >= that.xmin && this.ymax >= that.ymin && that.xmax >= this.xmin && that.ymax >= this.ymin

    /**
     * Returns true if this rectangle contain the point.
     * @param  p the point
     * @return `true` if this rectangle contain the point `p`,
     * possibly at the boundary; `false` otherwise
     */
    operator fun contains(p: Point2D): Boolean = p.x in xmin..xmax && p.y in ymin..ymax

    /**
     * Returns the Euclidean distance between this rectangle and the point `p`.
     *
     * @param  p the point
     * @return the Euclidean distance between the point `p` and the closest point
     * on this rectangle; 0 if the point is contained in this rectangle
     */
    fun distanceTo(p: Point2D): Double = Math.sqrt(this.distanceSquaredTo(p))

    /**
     * Returns the square of the Euclidean distance between this rectangle and the point `p`.
     *
     * @param  p the point
     * @return the square of the Euclidean distance between the point `p` and
     * the closest point on this rectangle; 0 if the point is contained
     * in this rectangle
     */
    fun distanceSquaredTo(p: Point2D): Double {
        var dx = 0.0
        var dy = 0.0
        if (p.x < xmin) dx = p.x - xmin
        else if (p.x > xmax) dx = p.x - xmax
        if (p.y < ymin) dy = p.y - ymin
        else if (p.y > ymax) dy = p.y - ymax
        return dx * dx + dy * dy
    }

    /**
     * Compares this rectangle to the specified rectangle.
     *
     * @param  other the other rectangle
     * @return `true` if this rectangle equals `other`;
     * `false` otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other.javaClass != this.javaClass) return false
        val that = other as RectHV?
        if (this.xmin != that!!.xmin) return false
        if (this.ymin != that.ymin) return false
        if (this.xmax != that.xmax) return false
        return this.ymax == that.ymax
    }

    /**
     * Returns an integer hash code for this rectangle.
     * @return an integer hash code for this rectangle
     */
    override fun hashCode(): Int {
        val hash1 = xmin.hashCode()
        val hash2 = ymin.hashCode()
        val hash3 = xmax.hashCode()
        val hash4 = ymax.hashCode()
        return 31 * (31 * (31 * hash1 + hash2) + hash3) + hash4
    }

    /**
     * Returns a string representation of this rectangle.
     *
     * @return a string representation of this rectangle, using the format
     * `[xmin, xmax] x [ymin, ymax]`
     */
    override fun toString() = "[$xmin, $xmax] x [$ymin, $ymax]"

    /**
     * Draws this rectangle to standard draw.
     */
    fun draw() {
        StdDraw.line(xmin, ymin, xmax, ymin)
        StdDraw.line(xmax, ymin, xmax, ymax)
        StdDraw.line(xmax, ymax, xmin, ymax)
        StdDraw.line(xmin, ymax, xmin, ymin)
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