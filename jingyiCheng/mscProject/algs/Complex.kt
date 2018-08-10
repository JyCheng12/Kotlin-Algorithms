/******************************************************************************
 * Compilation:  javac Complex.java
 * Execution:    java Complex
 * Dependencies: StdOut.kt
 *
 * Data type for complex numbers.
 *
 * The data type is "immutable" so once you create and initialize
 * a Complex object, you cannot change it. The "final" keyword
 * when declaring re and im enforces this rule, making it a
 * compile-time error to change the .re or .im fields after
 * they've been initialized.
 *
 * % java Complex
 * a            = 5.0 + 6.0i
 * b            = -3.0 + 4.0i
 * Re(a)        = 5.0
 * Im(a)        = 6.0
 * b + a        = 2.0 + 10.0i
 * a - b        = 8.0 + 2.0i
 * a * b        = -39.0 + 2.0i
 * b * a        = -39.0 + 2.0i
 * a / b        = 0.36 - 1.52i
 * (a / b) * b  = 5.0 + 6.0i
 * conj(a)      = 5.0 - 6.0i
 * |a|          = 7.810249675906654
 * tan(a)       = -6.685231390246571E-6 + 1.0000103108981198i
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Complex` class represents a complex number.
 * Complex numbers are immutable: their values cannot be changed after they
 * are created.
 * It includes methods for addition, subtraction, multiplication, division,
 * conjugation, and other common functions on complex numbers.
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
class Complex
/**
 * Initializes a complex number from the specified real and imaginary parts.
 *
 * @param real the real part
 * @param imag the imaginary part
 */
(val re: Double, val im: Double) {
    /**
     * Returns a string representation of this complex number.
     *
     * @return a string representation of this complex number,
     * of the form 34 - 56i.
     */
    override fun toString(): String {
        if (im == 0.0) return "$re"
        if (re == 0.0) return "${im}i"
        return if (im < 0) "$re - ${-im}i" else "$re + ${im}i"
    }

    /**
     * Returns the absolute value of this complex number.
     * This quantity is also known as the *modulus* or *magnitude*.
     *
     * @return the absolute value of this complex number
     */
    fun abs(): Double = Math.hypot(re, im)

    /**
     * Returns the phase of this complex number.
     * This quantity is also known as the *angle* or *argument*.
     *
     * @return the phase of this complex number, a real number between -pi and pi
     */
    fun phase(): Double = Math.atan2(im, re)

    /**
     * Returns the sum of this complex number and the specified complex number.
     *
     * @param  that the other complex number
     * @return the complex number whose value is `(this + that)`
     */
    operator fun plus(that: Complex): Complex {
        val real = this.re + that.re
        val imag = this.im + that.im
        return Complex(real, imag)
    }

    /**
     * Returns the result of subtracting the specified complex number from
     * this complex number.
     *
     * @param  that the other complex number
     * @return the complex number whose value is `(this - that)`
     */
    operator fun minus(that: Complex): Complex {
        val real = this.re - that.re
        val imag = this.im - that.im
        return Complex(real, imag)
    }

    /**
     * Returns the product of this complex number and the specified complex number.
     *
     * @param  that the other complex number
     * @return the complex number whose value is `(this * that)`
     */
    operator fun times(that: Complex): Complex {
        val real = this.re * that.re - this.im * that.im
        val imag = this.re * that.im + this.im * that.re
        return Complex(real, imag)
    }

    /**
     * Returns the product of this complex number and the specified scalar.
     *
     * @param  alpha the scalar
     * @return the complex number whose value is `(alpha * this)`
     */
    fun scale(alpha: Double) = Complex(alpha * re, alpha * im)

    /**
     * Returns the product of this complex number and the specified scalar.
     *
     * @param  alpha the scalar
     * @return the complex number whose value is `(alpha * this)`
     */
    @Deprecated("Replaced by {@link #scale(double)}.")
    operator fun times(alpha: Double) = Complex(alpha * re, alpha * im)

    /**
     * Returns the complex conjugate of this complex number.
     *
     * @return the complex conjugate of this complex number
     */
    fun conjugate(): Complex = Complex(re, -im)

    /**
     * Returns the reciprocal of this complex number.
     *
     * @return the complex number whose value is `(1 / this)`
     */
    fun reciprocal(): Complex {
        val scale = re * re + im * im
        return Complex(re / scale, -im / scale)
    }

    /**
     * Returns the result of dividing the specified complex number into
     * this complex number.
     *
     * @param  that the other complex number
     * @return the complex number whose value is `(this / that)`
     */
    fun divides(that: Complex) = this.times(that.reciprocal())

    /**
     * Returns the complex exponential of this complex number.
     *
     * @return the complex exponential of this complex number
     */
    fun exp() = Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im))

    /**
     * Returns the complex sine of this complex number.
     *
     * @return the complex sine of this complex number
     */
    fun sin() = Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im))

    /**
     * Returns the complex cosine of this complex number.
     *
     * @return the complex cosine of this complex number
     */
    fun cos() = Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im))

    /**
     * Returns the complex tangent of this complex number.
     *
     * @return the complex tangent of this complex number
     */
    fun tan() = sin().divides(cos())

    companion object {
        /**
         * Unit tests the `Complex` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val a = Complex(5.0, 6.0)
            val b = Complex(-3.0, 4.0)

            StdOut.println("a            = $a")
            StdOut.println("b            = $b")
            StdOut.println("Re(a)        = ${a.re}")
            StdOut.println("Im(a)        = ${a.im}")
            StdOut.println("b + a        = ${b.plus(a)}")
            StdOut.println("a - b        = ${a.minus(b)}")
            StdOut.println("a * b        = ${a.times(b)}")
            StdOut.println("b * a        = ${b.times(a)}")
            StdOut.println("a / b        = ${a.divides(b)}")
            StdOut.println("(a / b) * b  = ${a.divides(b).times(b)}")
            StdOut.println("conj(a)      = ${a.conjugate()}")
            StdOut.println("|a|          = ${a.abs()}")
            StdOut.println("tan(a)       = ${a.tan()}")
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
