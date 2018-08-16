/******************************************************************************
 * Compilation:  javac LookupCSV.java
 * Execution:    java LookupCSV file.csv keyField valField
 * Dependencies: ST.kt In.kt StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/35applications/DJIA.csv
 * https://algs4.cs.princeton.edu/35applications/UPC.csv
 * https://algs4.cs.princeton.edu/35applications/amino.csv
 * https://algs4.cs.princeton.edu/35applications/elements.csv
 * https://algs4.cs.princeton.edu/35applications/ip.csv
 * https://algs4.cs.princeton.edu/35applications/morse.csv
 *
 * Reads in a set of key-value pairs from a two-column CSV file
 * specified on the command line; then, reads in keys from standard
 * input and prints out corresponding values.
 *
 * % java LookupCSV amino.csv 0 3     % java LookupCSV ip.csv 0 1
 * TTA                                www.google.com
 * Leucine                            216.239.41.99
 * ABC
 * Not found                          % java LookupCSV ip.csv 1 0
 * TCT                                216.239.41.99
 * Serine                             www.google.com
 *
 * % java LookupCSV amino.csv 3 0     % java LookupCSV DJIA.csv 0 1
 * Glycine                            29-Oct-29
 * GGG                                252.38
 * 20-Oct-87
 * 1738.74
 *
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `LookupCSV` class provides a data-driven client for reading in a
 * key-value pairs from a file; then, printing the values corresponding to the
 * keys found on standard input. Both keys and values are strings.
 * The fields to serve as the key and value are taken as command-line arguments.
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
object LookupCSV {
    @JvmStatic
    fun main(args: Array<String>) {
        val keyField = Integer.parseInt(args[1])
        val valField = Integer.parseInt(args[2])

        // symbol table
        val st = ST<String, String>()

        // read in the data from csv file
        val `in` = In(args[0])
        while (`in`.hasNextLine()) {
            val line = `in`.readLine()!!
            val tokens = line.split(",").toTypedArray()
            val key = tokens[keyField]
            val `val` = tokens[valField]
            st.put(key, `val`)
        }

        while (!StdIn.isEmpty) {
            val s = StdIn.readString()
            if (st.contains(s))
                StdOut.println(st[s])
            else
                StdOut.println("Not found")
        }
    }
}// Do not instantiate.

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