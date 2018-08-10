/******************************************************************************
 * Compilation:  javac LookupIndex.java
 * Execution:    java LookupIndex movies.txt "/"
 * Dependencies: ST.kt Queue.kt In.kt StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/35applications/aminoI.csv
 * https://algs4.cs.princeton.edu/35applications/movies.txt
 *
 * % java LookupIndex amino.csv ","
 * Serine
 * TCT
 * TCA
 * TCG
 * AGT
 * AGC
 * TCG
 * Serine
 *
 * % java LookupIndex movies.txt "/"
 * Bacon, Kevin
 * Animal House (1978)
 * Apollo 13 (1995)
 * Beauty Shop (2005)
 * Diner (1982)
 * Few Good Men, A (1992)
 * Flatliners (1990)
 * Footloose (1984)
 * Friday the 13th (1980)
 * ...
 * Tin Men (1987)
 * DeBoy, David
 * Blumenfeld, Alan
 * ...
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LookupIndex` class provides a data-driven client for reading in a
 * key-value pairs from a file; then, printing the values corresponding to the
 * keys found on standard input. Keys are strings; values are lists of strings.
 * The separating delimiter is taken as a command-line argument. This client
 * is sometimes known as an *inverted index*.
 *
 *
 * For additional documentation, see [Section 3.5](https://algs4.cs.princeton.edu/35applications) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object LookupIndex {

    @JvmStatic
    fun main(args: Array<String>) {
        val filename = args[0]
        val separator = args[1]
        val `in` = In(filename)

        val st = ST<String, nnQueue<String>>()
        val ts = ST<String, nnQueue<String>>()

        while (`in`.hasNextLine()) {
            val line = `in`.readLine()!!
            val fields = line.split(separator).toTypedArray()
            val key = fields[0]
            for (i in 1 until fields.size) {
                val `val` = fields[i]
                if (!st.contains(key)) st.put(key, nnQueue())
                if (!ts.contains(`val`)) ts.put(`val`, nnQueue())
                st[key].enqueue(`val`)
                ts[`val`].enqueue(key)
            }
        }

        StdOut.println("Done indexing")

        // read queries from standard input, one per line
        while (!StdIn.isEmpty) {
            val query = StdIn.readLine()
            if (st.contains(query))
                for (vals in st[query])
                    StdOut.println("  $vals")
            if (ts.contains(query))
                for (keys in ts[query])
                    StdOut.println("  $keys")
        }
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
