/******************************************************************************
 * Compilation:  javac DirectedEdge.java
 * Execution:    java DirectedEdge
 * Dependencies: StdOut.kt
 *
 * Immutable weighted directed edge.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `DirectedEdge` class represents a weighted edge in an
 * [EdgeWeightedDigraph]. Each edge consists of two integers
 * (naming the two vertices) and a real-value weight. The data type
 * provides methods for accessing the two endpoints of the directed edge and
 * the weight.
 *
 *
 * For additional documentation, see [Section 4.4](https://algs4.cs.princeton.edu/44sp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */

class DirectedEdge
/**
 * Initializes a directed edge from vertex `v` to vertex `w` with
 * the given `weight`.
 * @param v the tail vertex
 * @param w the head vertex
 * @param weight the weight of the directed edge
 * @throws IllegalArgumentException if either `v` or `w`
 * is a negative integer
 * @throws IllegalArgumentException if `weight` is `NaN`
 */
(val from: Int, val to: Int, val weight: Double) {
    init {
        if (from < 0) throw IllegalArgumentException("Vertex names must be non-negative integers")
        if (to < 0) throw IllegalArgumentException("Vertex names must be non-negative integers")
        if (weight.isNaN()) throw IllegalArgumentException("Weight is NaN")
    }

    /**
     * Returns a string representation of the directed edge.
     * @return a string representation of the directed edge
     */
    override fun toString() = "$from->$to ${String.format("%5.2f", weight)}"

    companion object {
        /**
         * Unit tests the `DirectedEdge` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val e = DirectedEdge(12, 34, 5.67)
            StdOut.println(e)
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
