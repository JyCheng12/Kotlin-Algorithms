/******************************************************************************
 * Compilation:  javac FFT.java
 * Execution:    java FFT n
 * Dependencies: Complex.kt
 *
 * Compute the FFT and inverse FFT of a length n complex sequence.
 * Bare bones implementation that runs in O(n log n) time. Our goal
 * is to optimize the clarity of the code, rather than performance.
 *
 * Limitations
 * -----------
 * -  assumes n is a power of 2
 *
 * -  not the most memory efficient algorithm (because it uses
 * an object type for representing complex numbers and because
 * it re-allocates memory for the subarray, instead of doing
 * in-place or reusing a single temporary array)
 *
 *
 * % java FFT 4
 * x
 * -------------------
 * -0.03480425839330703
 * 0.07910192950176387
 * 0.7233322451735928
 * 0.1659819820667019
 *
 * y = fft(x)
 * -------------------
 * 0.9336118983487516
 * -0.7581365035668999 + 0.08688005256493803i
 * 0.44344407521182005
 * -0.7581365035668999 - 0.08688005256493803i
 *
 * z = ifft(y)
 * -------------------
 * -0.03480425839330703
 * 0.07910192950176387 + 2.6599344570851287E-18i
 * 0.7233322451735928
 * 0.1659819820667019 - 2.6599344570851287E-18i
 *
 * c = cconvolve(x, x)
 * -------------------
 * 0.5506798633981853
 * 0.23461407150576394 - 4.033186818023279E-18i
 * -0.016542951108772352
 * 0.10288019294318276 + 4.033186818023279E-18i
 *
 * d = convolve(x, x)
 * -------------------
 * 0.001211336402308083 - 3.122502256758253E-17i
 * -0.005506167987577068 - 5.058885073636224E-17i
 * -0.044092969479563274 + 2.1934338938072244E-18i
 * 0.10288019294318276 - 3.6147323062478115E-17i
 * 0.5494685269958772 + 3.122502256758253E-17i
 * 0.240120239493341 + 4.655566391833896E-17i
 * 0.02755001837079092 - 2.1934338938072244E-18i
 * 4.01805098805014E-17i
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `FFT` class provides methods for computing the
 * FFT (Fast-Fourier Transform), inverse FFT, linear convolution,
 * and circular convolution of a complex array.
 *
 *
 * It is a bare-bones implementation that runs in *n* log *n* time,
 * where *n* is the length of the complex array. For simplicity,
 * *n* must be a power of 2.
 * Our goal is to optimize the clarity of the code, rather than performance.
 * It is not the most memory efficient implementation because it uses
 * objects to represents complex numbers and it it re-allocates memory
 * for the subarray, instead of doing in-place or reusing a single temporary array.
 *
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
object FFT {
    private val ZERO = Complex(0.0, 0.0)

    /**
     * Returns the FFT of the specified complex array.
     *
     * @param  x the complex array
     * @return the FFT of the complex array `x`
     * @throws IllegalArgumentException if the length of `x` is not a power of 2
     */
    fun fft(x: Array<Complex>): Array<Complex> {
        val n = x.size

        // base case
        if (n == 1) {
            return arrayOf(x[0])
        }

        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw IllegalArgumentException("n is not a power of 2")
        }

        // fft of even terms
        val even = Array(n / 2) { x[2 * it] }
        val q = fft(even)

        // fft of odd terms
        for (k in 0 until n / 2) {
            even[k] = x[2 * k + 1]
        }
        val r = fft(even)

        // combine
        val y = Array(n) { Complex(0.0, 0.0) }
        for (k in 0 until n / 2) {
            val kth = -2.0 * k.toDouble() * Math.PI / n
            val wk = Complex(Math.cos(kth), Math.sin(kth))
            y[k] = q[k].plus(wk.times(r[k]))
            y[k + n / 2] = q[k].minus(wk.times(r[k]))
        }
        return y
    }

    /**
     * Returns the inverse FFT of the specified complex array.
     *
     * @param  x the complex array
     * @return the inverse FFT of the complex array `x`
     * @throws IllegalArgumentException if the length of `x` is not a power of 2
     */
    fun ifft(x: Array<Complex>): Array<Complex> {
        val n = x.size
        var y = Array(n) { x[it].conjugate() }

        // compute forward FFT
        y = fft(y)

        // take conjugate again
        for (i in 0 until n) {
            y[i] = y[i].conjugate()
        }

        // divide by n
        for (i in 0 until n) {
            y[i] = y[i].scale(1.0 / n)
        }
        return y
    }

    /**
     * Returns the circular convolution of the two specified complex arrays.
     *
     * @param  x one complex array
     * @param  y the other complex array
     * @return the circular convolution of `x` and `y`
     * @throws IllegalArgumentException if the length of `x` does not equal
     * the length of `y` or if the length is not a power of 2
     */
    fun cconvolve(x: Array<Complex>, y: Array<Complex>): Array<Complex> {
        // should probably pad x and y with 0s so that they have same length
        // and are powers of 2
        if (x.size != y.size) {
            throw IllegalArgumentException("Dimensions don't agree")
        }
        val n = x.size

        // compute FFT of each sequence
        val a = fft(x)
        val b = fft(y)

        // point-wise multiply
        val c = Array(n) { a[it].times(b[it]) }

        // compute inverse FFT
        return ifft(c)
    }

    /**
     * Returns the linear convolution of the two specified complex arrays.
     *
     * @param  x one complex array
     * @param  y the other complex array
     * @return the linear convolution of `x` and `y`
     * @throws IllegalArgumentException if the length of `x` does not equal
     * the length of `y` or if the length is not a power of 2
     */
    fun convolve(x: Array<Complex>, y: Array<Complex>): Array<Complex> {
        val a = Array(2 * x.size) { if (it < x.size) x[it] else ZERO }
        val b = Array(2 * y.size) { if (it < y.size) y[it] else ZERO }
        return cconvolve(a, b)
    }

    // display an array of Complex numbers to standard output
    private fun show(x: Array<Complex>, title: String) {
        StdOut.println(title)
        StdOut.println("-------------------")
        for (i in x) {
            StdOut.println(i)
        }
        StdOut.println()
    }

    /***************************************************************************
     * Test client.
     */

    /**
     * Unit tests the `FFT` class.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val n = Integer.parseInt(args[0])
        val x = Array(n) { Complex(StdRandom.uniform(-1.0, 1.0), 0.0) }
        show(x, "x")

        // FFT of original data
        val y = fft(x)
        show(y, "y = fft(x)")

        // take inverse FFT
        val z = ifft(y)
        show(z, "z = ifft(y)")

        // circular convolution of x with itself
        val c = cconvolve(x, x)
        show(c, "c = cconvolve(x, x)")

        // linear convolution of x with itself
        val d = convolve(x, x)
        show(d, "d = convolve(x, x)")
    }
}// Do not instantiate.

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
