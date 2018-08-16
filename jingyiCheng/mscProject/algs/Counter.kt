/******************************************************************************
 * Compilation:  javac Counter.java
 * Execution:    java Counter n trials
 * Dependencies: StdRandom.kt StdOut.kt
 *
 * A mutable data type for an integer counter.
 *
 * The test clients create n counters and performs trials increment
 * operations on random counters.
 *
 * java Counter 6 600000
 * 100140 counter0
 * 100273 counter1
 * 99848 counter2
 * 100129 counter3
 * 99973 counter4
 * 99637 counter5
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Counter` class is a mutable data type to encapsulate a counter.
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
class Counter
/**
 * Initializes a new counter starting at 0, with the given name.
 *
 * the name of the counter
 */
(private val name: String) : Comparable<Counter> {     // counter name
    var count = 0         // current value
        private set

    /**
     * Increments the counter by 1.
     */
    fun increment() = count++

    /**
     * Returns a string representation of this counter.
     *
     * @return a string representation of this counter
     */
    override fun toString() = "$count $name"

    /**
     * Compares this counter to the specified counter.
     *
     * @param  other the other counter
     * @return `0` if the value of this counter equals
     * the value of that counter; a negative integer if
     * the value of this counter is less than the value of
     * that counter; and a positive integer if the value
     * of this counter is greater than the value of that
     * counter
     */
    override fun compareTo(other: Counter): Int = when {
        this.count < other.count -> -1
        this.count > other.count -> +1
        else -> 0
    }

    companion object {
        /**
         * Reads two command-line integers n and trials; creates n counters;
         * increments trials counters at random; and prints results.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val n = Integer.parseInt(args[0])
            val trials = Integer.parseInt(args[1])

            // create n counters
            val hits = Array(n) { Counter("counter$it") }

            // increment trials counters at random
            for (t in 0 until trials)
                hits[StdRandom.uniform(n)].increment()

            // print results
            for (i in hits)
                StdOut.println(i)
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