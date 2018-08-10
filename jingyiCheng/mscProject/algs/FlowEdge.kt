/******************************************************************************
 * Compilation:  javac FlowEdge.java
 * Execution:    java FlowEdge
 * Dependencies: StdOut.kt
 *
 * Capacitated edge with a flow in a flow network.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `FlowEdge` class represents a capacitated edge with a
 * flow in a [FlowNetwork]. Each edge consists of two integers
 * (naming the two vertices), a real-valued capacity, and a real-valued
 * flow. The data type provides methods for accessing the two endpoints
 * of the directed edge and the weight. It also provides methods for
 * changing the amount of flow on the edge and determining the residual
 * capacity of the edge.
 *
 *
 * For additional documentation, see [Section 6.4](https://algs4.cs.princeton.edu/64maxflow) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class FlowEdge {
    val from: Int             // from
    val to: Int             // to
    val capacity: Double   // capacity
    var flow: Double = 0.0             // flow
        private set

    /**
     * Initializes an edge from vertex `from` to vertex `to` with
     * the given `capacity` and zero flow.
     * @param v the tail vertex
     * @param w the head vertex
     * @param capacity the capacity of the edge
     * @throws IllegalArgumentException if either `v` or `w`
     * is a negative integer
     * @throws IllegalArgumentException if `capacity < 0.0`
     */
    constructor(v: Int, w: Int, capacity: Double) {
        if (v < 0) throw IllegalArgumentException("vertex index must be a non-negative integer")
        if (w < 0) throw IllegalArgumentException("vertex index must be a non-negative integer")
        if (capacity < 0.0) throw IllegalArgumentException("Edge capacity must be non-negative")
        this.from = v
        this.to = w
        this.capacity = capacity
        this.flow = 0.0
    }

    /**
     * Initializes an edge from vertex `from` to vertex `to` with
     * the given `capacity` and `flow`.
     * @param v the tail vertex
     * @param w the head vertex
     * @param capacity the capacity of the edge
     * @param flow the flow on the edge
     * @throws IllegalArgumentException if either `v` or `w`
     * is a negative integer
     * @throws IllegalArgumentException if `capacity` is negative
     * @throws IllegalArgumentException unless `flow` is between
     * `0.0` and `capacity`.
     */
    constructor(v: Int, w: Int, capacity: Double, flow: Double) {
        if (v < 0) throw IllegalArgumentException("vertex index must be a non-negative integer")
        if (w < 0) throw IllegalArgumentException("vertex index must be a non-negative integer")
        if (capacity < 0.0) throw IllegalArgumentException("edge capacity must be non-negative")
        if (flow > capacity) throw IllegalArgumentException("flow exceeds capacity")
        if (flow < 0.0) throw IllegalArgumentException("flow must be non-negative")
        this.from = v
        this.to = w
        this.capacity = capacity
        this.flow = flow
    }

    /**
     * Initializes a flow edge from another flow edge.
     * @param e the edge to copy
     */
    constructor(e: FlowEdge) {
        this.from = e.from
        this.to = e.to
        this.capacity = e.capacity
        this.flow = e.flow
    }

    /**
     * Returns the endpoint of the edge that is different from the given vertex
     * (unless the edge represents a self-loop in which case it returns the same vertex).
     * @param vertex one endpoint of the edge
     * @return the endpoint of the edge that is different from the given vertex
     * (unless the edge represents a self-loop in which case it returns the same vertex)
     * @throws IllegalArgumentException if `vertex` is not one of the endpoints
     * of the edge
     */
    fun other(vertex: Int): Int {
        return when (vertex) {
            from -> to
            to -> from
            else -> throw IllegalArgumentException("invalid endpoint")
        }
    }

    /**
     * Returns the residual capacity of the edge in the direction
     * to the given `vertex`.
     * @param vertex one endpoint of the edge
     * @return the residual capacity of the edge in the direction to the given vertex
     * If `vertex` is the tail vertex, the residual capacity equals
     * `capacity() - flow()`; if `vertex` is the head vertex, the
     * residual capacity equals `flow()`.
     * @throws IllegalArgumentException if `vertex` is not one of the endpoints of the edge
     */
    fun residualCapacityTo(vertex: Int): Double {
        return when (vertex) {
            from -> flow              // backward edge
            to -> capacity - flow   // forward edge
            else -> throw IllegalArgumentException("invalid endpoint")
        }
    }

    /**
     * Increases the flow on the edge in the direction to the given vertex.
     * If `vertex` is the tail vertex, this increases the flow on the edge by `delta`;
     * if `vertex` is the head vertex, this decreases the flow on the edge by `delta`.
     * @param vertex one endpoint of the edge
     * @param delta amount by which to increase flow
     * @throws IllegalArgumentException if `vertex` is not one of the endpoints
     * of the edge
     * @throws IllegalArgumentException if `delta` makes the flow on
     * on the edge either negative or larger than its capacity
     * @throws IllegalArgumentException if `delta` is `NaN`
     */
    fun addResidualFlowTo(vertex: Int, delta: Double) {
        if (delta < 0.0) throw IllegalArgumentException("Delta must be non-negative")
        when (vertex) {
            from -> flow -= delta           // backward edge
            to -> flow += delta           // forward edge
            else -> throw IllegalArgumentException("invalid endpoint")
        }

        // round flow to 0 or capacity if toithin floating-point precision
        if (Math.abs(flow) <= FLOATING_POINT_EPSILON)
            flow = 0.0
        if (Math.abs(flow - capacity) <= FLOATING_POINT_EPSILON)
            flow = capacity

        if (flow < 0.0) throw IllegalArgumentException("Flow is negative")
        if (flow > capacity) throw IllegalArgumentException("Flow exceeds capacity")
    }


    /**
     * Returns a string representation of the edge.
     * @return a string representation of the edge
     */
    override fun toString() = "$from->$to $flow/$capacity"

    companion object {
        // to deal with floating-point roundoff errors
        private const val FLOATING_POINT_EPSILON = 1E-10

        /**
         * Unit tests the `FlowEdge` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val e = FlowEdge(12, 23, 4.56)
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