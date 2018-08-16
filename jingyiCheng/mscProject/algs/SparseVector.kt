/******************************************************************************
 * Compilation:  javac SparseVector.java
 * Execution:    java SparseVector
 * Dependencies: StdOut.kt
 *
 * A sparse vector, implementing using a symbol table.
 *
 * [Not clear we need the instance variable N except for error checking.]
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `SparseVector` class represents a *d*-dimensional mathematical vector.
 * Vectors are mutable: their values can be changed after they are created.
 * It includes methods for addition, subtraction,
 * dot product, scalar product, unit vector, and Euclidean norm.
 *
 *
 * The implementation is a symbol table of indices and values for which the vector
 * coordinates are nonzero. This makes it efficient when most of the vector coordindates
 * are zero.
 *
 *
 * For additional documentation,
 * see [Section 3.5](https://algs4.cs.princeton.edu/35applications) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 * See also [Vector] for an immutable (dense) vector data type.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class SparseVector
/**
 * Initializes a d-dimensional zero vector.
 * @param d the dimension of the vector
 */
(val d: Int                   // dimension
) {
    private val st: ST<Int, Double> = ST()  // the vector, represented by index-value pairs

    /**
     * Sets the ith coordinate of this vector to the specified value.
     *
     * @param  i the index
     * @param  value the new value
     * @throws IllegalArgumentException unless i is between 0 and d-1
     */
    fun put(i: Int, value: Double) {
        if (i < 0 || i >= d) throw IllegalArgumentException("Illegal index")
        if (value == 0.0)
            st.delete(i)
        else
            st.put(i, value)
    }

    /**
     * Returns the ith coordinate of this vector.
     *
     * @param  i the index
     * @return the value of the ith coordinate of this vector
     * @throws IllegalArgumentException unless i is between 0 and d-1
     */
    operator fun get(i: Int): Double {
        if (i < 0 || i >= d) throw IllegalArgumentException("Illegal index")
        return if (st.contains(i)) st[i]
        else 0.0
    }

    /**
     * Returns the inner product of this vector with the specified vector.
     *
     * @param  that the other vector
     * @return the dot product between this vector and that vector
     * @throws IllegalArgumentException if the lengths of the two vectors are not equal
     */
    fun dot(that: SparseVector): Double {
        if (this.d != that.d) throw IllegalArgumentException("Vector lengths disagree")
        var sum = 0.0

        // iterate over the vector with the fewest nonzeros
        if (this.st.size <= that.st.size) {
            for (i in this.st.keys())
                if (that.st.contains(i)) sum += this[i] * that[i]
        } else
            for (i in that.st.keys())
                if (this.st.contains(i)) sum += this[i] * that[i]
        return sum
    }

    /**
     * Returns the inner product of this vector with the specified array.
     *
     * @param  that the array
     * @return the dot product between this vector and that array
     * @throws IllegalArgumentException if the dimensions of the vector and the array are not equal
     */
    fun dot(that: DoubleArray): Double {
        var sum = 0.0
        for (i in st.keys())
            sum += that[i] * this[i]
        return sum
    }

    /**
     * Returns the magnitude of this vector.
     * This is also known as the L2 norm or the Euclidean norm.
     *
     * @return the magnitude of this vector
     */
    fun magnitude() = Math.sqrt(this.dot(this))

    /**
     * Returns the Euclidean norm of this vector.
     *
     * @return the Euclidean norm of this vector
     */
    @Deprecated("Replaced by {@link #magnitude()}.")
    fun norm(): Double = Math.sqrt(this.dot(this))

    /**
     * Returns the scalar-vector product of this vector with the specified scalar.
     *
     * @param  alpha the scalar
     * @return the scalar-vector product of this vector with the specified scalar
     */
    fun scale(alpha: Double): SparseVector {
        val c = SparseVector(d)
        for (i in this.st.keys()) c.put(i, alpha * this[i])
        return c
    }

    /**
     * Returns the sum of this vector and the specified vector.
     *
     * @param  that the vector to add to this vector
     * @return the sum of this vector and that vector
     * @throws IllegalArgumentException if the dimensions of the two vectors are not equal
     */
    operator fun plus(that: SparseVector): SparseVector {
        if (this.d != that.d) throw IllegalArgumentException("Vector lengths disagree")
        val c = SparseVector(d)
        for (i in this.st.keys()) c.put(i, this[i])                // c = this
        for (i in that.st.keys()) c.put(i, that[i] + c[i])     // c = c + that
        return c
    }

    /**
     * Returns a string representation of this vector.
     * @return a string representation of this vector, which consists of the
     * the vector entries, separates by commas, enclosed in parentheses
     */
    override fun toString(): String {
        val s = StringBuilder()
        for (i in st.keys())
            s.append("($i, ${st[i]}) ")
        return s.toString()
    }

    companion object {
        /**
         * Unit tests the `SparseVector` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val a = SparseVector(10)
            val b = SparseVector(10)
            a.put(3, 0.50)
            a.put(9, 0.75)
            a.put(6, 0.11)
            a.put(6, 0.00)
            b.put(3, 0.60)
            b.put(4, 0.90)
            StdOut.println("a = $a")
            StdOut.println("b = $b")
            StdOut.println("a dot b = ${a.dot(b)}")
            StdOut.println("a + b   = ${a.plus(b)}")
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