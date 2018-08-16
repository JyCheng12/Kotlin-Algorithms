/******************************************************************************
 * Compilation:  javac Edge.java
 * Execution:    java Edge
 * Dependencies: StdOut.kt
 *
 * Immutable weighted edge.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Edge` class represents a weighted edge in an
 * [EdgeWeightedGraph]. Each edge consists of two integers
 * (naming the two vertices) and a real-value weight. The data type
 * provides methods for accessing the two endpoints of the edge and
 * the weight. The natural order for this data type is by
 * ascending order of weight.
 *
 *
 * For additional documentation, see [Section 4.3](https://algs4.cs.princeton.edu/43mst) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class Edge
/**
 * Initializes an edge between vertices `v` and `w` of
 * the given `weight`.
 *
 * @param  v one vertex
 * @param  w the other vertex
 * @param  weight the weight of this edge
 * @throws IllegalArgumentException if either `v` or `w`
 * is a negative integer
 * @throws IllegalArgumentException if `weight` is `NaN`
 */
(val either: Int, val w: Int, val weight: Double) : Comparable<Edge> {
    init {
        if (either < 0) throw IllegalArgumentException("vertex index must be a non-negative integer")
        if (w < 0) throw IllegalArgumentException("vertex index must be a non-negative integer")
        if (weight.isNaN()) throw IllegalArgumentException("Weight is NaN")
    }

    /**
     * Returns the endpoint of this edge that is different from the given vertex.
     *
     * @param  vertex one endpoint of this edge
     * @return the other endpoint of this edge
     * @throws IllegalArgumentException if the vertex is not one of the
     * endpoints of this edge
     */
    fun other(vertex: Int) = when (vertex) {
        either -> w
        w -> either
        else -> throw IllegalArgumentException("Illegal endpoint")
    }

    /**
     * Compares two edges by weight.
     * Note that `compareTo()` is not consistent with `equals()`,
     * which uses the reference equality implementation inherited from `Object`.
     *
     * @param  other the other edge
     * @return a negative integer, zero, or positive integer depending on whether
     * the weight of this is less than, equal to, or greater than the
     * argument edge
     */
    override fun compareTo(other: Edge) = this.weight.compareTo(other.weight)

    /**
     * Returns a string representation of this edge.
     *
     * @return a string representation of this edge
     */
    override fun toString() = String.format("%d-%d %.5f", either, w, weight)

    companion object {
        /**
         * Unit tests the `Edge` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val e = Edge(12, 34, 5.67)
            StdOut.println(e)
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