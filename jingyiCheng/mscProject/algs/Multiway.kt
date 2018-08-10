/******************************************************************************
 * Compilation:  javac Multiway.java
 * Execution:    java Multiway input1.txt input2.txt input3.txt ...
 * Dependencies: IndexMinPQ.kt In.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/24pq/m1.txt
 * https://algs4.cs.princeton.edu/24pq/m2.txt
 * https://algs4.cs.princeton.edu/24pq/m3.txt
 *
 * Merges together the sorted input stream given as command-line arguments
 * into a single sorted output stream on standard output.
 *
 * % more m1.txt
 * A B C F G I I Z
 *
 * % more m2.txt
 * B D H P Q Q
 *
 * % more m3.txt
 * A B E F J N
 *
 * % java Multiway m1.txt m2.txt m3.txt
 * A A B B B C D E F F G H I I J N P Q Q Z
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Multiway` class provides a client for reading in several
 * sorted text files and merging them together into a single sorted
 * text stream.
 * This implementation uses a [IndexMinPQ] to perform the multiway
 * merge.
 *
 *
 * For additional documentation, see [Section 2.4](https://algs4.cs.princeton.edu/24pq)
 * of *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */

object Multiway {
    // merge together the sorted input streams and write the sorted result to standard output
    private fun merge(streams: Array<In>) {
        val n = streams.size
        val pq = IndexMinPQ<String>(n)
        for (i in 0 until n)
            if (!streams[i].isEmpty)
                pq.insert(i, streams[i].readString())

        // Extract and print min and read next from its stream.
        while (!pq.isEmpty) {
            StdOut.print(pq.minKey() + " ")
            val i = pq.delMin()
            if (!streams[i].isEmpty)
                pq.insert(i, streams[i].readString())
        }
        StdOut.println()
    }

    /**
     * Reads sorted text files specified as command-line arguments;
     * merges them together into a sorted output; and writes
     * the results to standard output.
     * Note: this client does not check that the input files are sorted.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val n = args.size
        val streams = Array(n) { In(args[it]) }
        merge(streams)
    }
}// This class should not be instantiated.

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
