/******************************************************************************
 * Compilation:  javac Polynomial.java
 * Execution:    java Polynomial
 *
 * Polynomials with integer coefficients.
 *
 * % java Polynomial
 * zero(x)     = 0
 * p(x)        = 4x^3 + 3x^2 + 2x + 1
 * q(x)        = 3x^2 + 5
 * p(x) + q(x) = 4x^3 + 6x^2 + 2x + 6
 * p(x) * q(x) = 12x^5 + 9x^4 + 26x^3 + 18x^2 + 10x + 5
 * p(q(x))     = 108x^6 + 567x^4 + 996x^2 + 586
 * p(x) - p(x) = 0
 * 0 - p(x)    = -4x^3 - 3x^2 - 2x - 1
 * p(3)        = 142
 * p'(x)       = 12x^2 + 6x + 2
 * p''(x)      = 24x + 6
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Polynomial` class represents a polynomial with integer
 * coefficients.
 * Polynomials are immutable: their values cannot be changed after they
 * are created.
 * It includes methods for addition, subtraction, multiplication, composition,
 * differentiation, and evaluation.
 *
 *
 * For additional documentation,
 * see [Section 9.9](https://algs4.cs.princeton.edu/99scientific) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class Polynomial
/**
 * Initializes a new polynomial a x^b
 * @param a the leading coefficient
 * @param b the exponent
 * @throws IllegalArgumentException if `b` is negative
 */
(a: Int, b: Int) {
    private val coefficients: IntArray   // coefficients p(x) = sum { coefficients[i] * x^i }
    var degree: Int = 0   // degree of polynomial (-1 for the zero polynomial)
        private set

    init {
        if (b < 0) throw IllegalArgumentException("exponent cannot be negative: $b")
        coefficients = IntArray(b + 1)
        coefficients[b] = a
        reduce()
    }

    // pre-compute the degree of the polynomial, in case of leading zero coefficients
    // (that is, the length of the array need not relate to the degree of the polynomial)
    private fun reduce() {
        degree = -1
        for (i in coefficients.indices.reversed())
            if (coefficients[i] != 0) {
                degree = i
                return
            }
    }

    /**
     * Returns the sum of this polynomial and the specified polynomial.
     *
     * @param  that the other polynomial
     * @return the polynomial whose value is `(this(x) + that(x))`
     */
    operator fun plus(that: Polynomial): Polynomial {
        val poly = Polynomial(0, Math.max(this.degree, that.degree))
        for (i in 0..this.degree) poly.coefficients[i] += this.coefficients[i]
        for (i in 0..that.degree) poly.coefficients[i] += that.coefficients[i]
        poly.reduce()
        return poly
    }

    /**
     * Returns the result of subtracting the specified polynomial
     * from this polynomial.
     *
     * @param  that the other polynomial
     * @return the polynomial whose value is `(this(x) - that(x))`
     */
    operator fun minus(that: Polynomial): Polynomial {
        val poly = Polynomial(0, Math.max(this.degree, that.degree))
        for (i in 0..this.degree) poly.coefficients[i] += this.coefficients[i]
        for (i in 0..that.degree) poly.coefficients[i] -= that.coefficients[i]
        poly.reduce()
        return poly
    }

    /**
     * Returns the product of this polynomial and the specified polynomial.
     * Takes time proportional to the product of the degrees.
     * (Faster algorithms are known, e.g., via FFT.)
     *
     * @param  that the other polynomial
     * @return the polynomial whose value is `(this(x) * that(x))`
     */
    operator fun times(that: Polynomial): Polynomial {
        val poly = Polynomial(0, this.degree + that.degree)
        for (i in 0..this.degree)
            for (j in 0..that.degree)
                poly.coefficients[i + j] += this.coefficients[i] * that.coefficients[j]
        poly.reduce()
        return poly
    }

    /**
     * Returns the composition of this polynomial and the specified
     * polynomial.
     * Takes time proportional to the product of the degrees.
     * (Faster algorithms are known, e.g., via FFT.)
     *
     * @param  that the other polynomial
     * @return the polynomial whose value is `(this(that(x)))`
     */
    fun compose(that: Polynomial): Polynomial {
        var poly = Polynomial(0, 0)
        for (i in this.degree downTo 0) {
            val term = Polynomial(this.coefficients[i], 0)
            poly = term.plus(that.times(poly))
        }
        return poly
    }


    /**
     * Compares this polynomial to the specified polynomial.
     *
     * @param  other the other polynoimal
     * @return `true` if this polynomial equals `other`;
     * `false` otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other.javaClass != this.javaClass) return false
        val that = other as Polynomial?
        if (this.degree != that!!.degree) return false
        for (i in this.degree downTo 0)
            if (this.coefficients[i] != that.coefficients[i]) return false
        return true
    }

    /**
     * Returns the result of differentiating this polynomial.
     *
     * @return the polynomial whose value is `this'(x)`
     */
    fun differentiate(): Polynomial {
        if (degree == 0) return Polynomial(0, 0)
        val poly = Polynomial(0, degree - 1)
        poly.degree = degree - 1
        for (i in 0 until degree)
            poly.coefficients[i] = (i + 1) * coefficients[i + 1]
        return poly
    }

    /**
     * Returns the result of evaluating this polynomial at the point x.
     *
     * @param  x the point at which to evaluate the polynomial
     * @return the integer whose value is `(this(x))`
     */
    fun evaluate(x: Int): Int {
        var p = 0
        for (i in degree downTo 0)
            p = coefficients[i] + x * p
        return p
    }

    /**
     * Compares two polynomials by degree, breaking ties by coefficient of leading term.
     *
     * @param  that the other point
     * @return the value `0` if this polynomial is equal to the argument
     * polynomial (precisely when `equals()` returns `true`);
     * a negative integer if this polynomialt is less than the argument
     * polynomial; and a positive integer if this polynomial is greater than the
     * argument point
     */
    operator fun compareTo(that: Polynomial): Int {
        if (this.degree < that.degree) return -1
        if (this.degree > that.degree) return +1
        for (i in this.degree downTo 0) {
            if (this.coefficients[i] < that.coefficients[i]) return -1
            if (this.coefficients[i] > that.coefficients[i]) return +1
        }
        return 0
    }

    /**
     * Return a string representation of this polynomial.
     * @return a string representation of this polynomial in the format
     * 4x^5 - 3x^2 + 11x + 5
     */
    override fun toString(): String = when (degree) {
        -1 -> "0"
        0 -> "${coefficients[0]}"
        1 -> "${coefficients[1]}x + ${coefficients[0]}"
        else -> {
            var s = "${coefficients[degree]}x^$degree"
            for (i in degree - 1 downTo 0) {
                if (coefficients[i] == 0)
                    continue
                else if (coefficients[i] > 0)
                    s = "$s + ${coefficients[i]}"
                else if (coefficients[i] < 0) s = "$s - ${-coefficients[i]}"
                if (i == 1)
                    s += "x"
                else if (i > 1) s = "${s}x^$i"
            }
            s
        }
    }

    companion object {

        /**
         * Unit tests the polynomial data type.
         *
         * @param args the command-line arguments (none)
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val zero = Polynomial(0, 0)

            val p1 = Polynomial(4, 3)
            val p2 = Polynomial(3, 2)
            val p3 = Polynomial(1, 0)
            val p4 = Polynomial(2, 1)
            val p = p1.plus(p2).plus(p3).plus(p4)   // 4x^3 + 3x^2 + 1

            val q1 = Polynomial(3, 2)
            val q2 = Polynomial(5, 0)
            val q = q1.plus(q2)                     // 3x^2 + 5

            val r = p.plus(q)
            val s = p.times(q)
            val t = p.compose(q)
            val u = p.minus(p)

            StdOut.println("zero(x)     = $zero")
            StdOut.println("p(x)        = $p")
            StdOut.println("q(x)        = $q")
            StdOut.println("p(x) + q(x) = $r")
            StdOut.println("p(x) * q(x) = $s")
            StdOut.println("p(q(x))     = $t")
            StdOut.println("p(x) - p(x) = $u")
            StdOut.println("0 - p(x)    = ${zero.minus(p)}")
            StdOut.println("p(3)        = ${p.evaluate(3)}")
            StdOut.println("p'(x)       = ${p.differentiate()}")
            StdOut.println("p''(x)      = ${p.differentiate().differentiate()}")
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