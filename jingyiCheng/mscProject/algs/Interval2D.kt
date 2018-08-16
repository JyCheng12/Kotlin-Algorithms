/******************************************************************************
 * Compilation:  javac Interval2D.java
 * Execution:    java Interval2D
 * Dependencies: StdOut.kt Interval1D.kt StdDraw.kt
 *
 * 2-dimensional interval data type.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Interval2D` class represents a closed two-dimensional interval,
 * which represents all points (x, y) with both `xmin <= x <= xmax` and
 * `ymin <= y <= ymax`.
 * Two-dimensional intervals are immutable: their values cannot be changed
 * after they are created.
 * The class `Interval2D` includes methods for checking whether
 * a two-dimensional interval contains a point and determining whether
 * two two-dimensional intervals intersect.
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
class Interval2D
/**
 * Initializes a two-dimensional interval.
 * @param x the one-dimensional interval of x-coordinates
 * @param y the one-dimensional interval of y-coordinates
 */
(private val x: Interval1D, private val y: Interval1D) {
    var area = 0.0
        get() = x.length * y.length
    /**
     * Does this two-dimensional interval intersect that two-dimensional interval?
     * @param that the other two-dimensional interval
     * @return true if this two-dimensional interval intersects
     * that two-dimensional interval; false otherwise
     */
    fun intersects(that: Interval2D): Boolean {
        if (!this.x.intersects(that.x)) return false
        return this.y.intersects(that.y)
    }

    /**
     * Does this two-dimensional interval contain the point p?
     * @param p the two-dimensional point
     * @return true if this two-dimensional interval contains the point p; false otherwise
     */
    operator fun contains(p: Point2D) = x.contains(p.x) && y.contains(p.y)

    /**
     * Returns a string representation of this two-dimensional interval.
     * @return a string representation of this two-dimensional interval
     * in the form [xmin, xmax] x [ymin, ymax]
     */
    override fun toString() = "$x x $y"

    /**
     * Does this interval equal the other interval?
     * @param other the other interval
     * @return true if this interval equals the other interval; false otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other.javaClass != this.javaClass) return false
        val that = other as Interval2D?
        return this.x == that!!.x && this.y == that.y
    }

    /**
     * Returns an integer hash code for this interval.
     * @return an integer hash code for this interval
     */
    override fun hashCode(): Int {
        val hash1 = x.hashCode()
        val hash2 = y.hashCode()
        return 31 * hash1 + hash2
    }

    /**
     * Draws this two-dimensional interval to standard draw.
     */
    fun draw() {
        val xc = (x.min + x.max) / 2.0
        val yc = (y.min + y.max) / 2.0
        StdDraw.rectangle(xc, yc, x.length / 2.0, y.length / 2.0)
    }

    companion object {
        /**
         * Unit tests the `Interval2D` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val trials = Integer.parseInt(args[4])
            val xInterval = Interval1D(args[0].toDouble(), args[1].toDouble())
            val yInterval = Interval1D(args[2].toDouble(), args[3].toDouble())
            val box = Interval2D(xInterval, yInterval)
            box.draw()

            val counter = Counter("hits")
            for (t in 0 until trials) {
                val x = StdRandom.uniform(0.0, 1.0)
                val y = StdRandom.uniform(0.0, 1.0)
                val point = Point2D(x, y)
                if (box.contains(point))
                    counter.increment()
                else
                    point.draw()
            }
            StdOut.println(counter)
            StdOut.printf("box area = %.2f\n", box.area)
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