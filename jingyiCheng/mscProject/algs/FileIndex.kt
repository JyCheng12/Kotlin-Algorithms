/******************************************************************************
 * Compilation:  javac FileIndex.java
 * Execution:    java FileIndex file1.txt file2.txt file3.txt ...
 * Dependencies: ST.kt SET.kt In.kt StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/35applications/ex1.txt
 * https://algs4.cs.princeton.edu/35applications/ex2.txt
 * https://algs4.cs.princeton.edu/35applications/ex3.txt
 * https://algs4.cs.princeton.edu/35applications/ex4.txt
 *
 * % java FileIndex ex*.txt
 * age
 * ex3.txt
 * ex4.txt
 * best
 * ex1.txt
 * was
 * ex1.txt
 * ex2.txt
 * ex3.txt
 * ex4.txt
 *
 * % java FileIndex *.txt
 *
 * % java FileIndex *.java
 *
 */

package jingyiCheng.mscProject.algs

import java.io.File

/**
 * The `FileIndex` class provides a client for indexing a set of files,
 * specified as command-line arguments. It takes queries from standard input
 * and prints each file that contains the given query.
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
object FileIndex {
    @JvmStatic
    fun main(args: Array<String>) {
        // key = word, value = set of files containing that word
        val st = ST<String, SET<File>>()

        // create inverted index of all files
        StdOut.println("Indexing files")
        for (filename in args) {
            StdOut.println("  $filename")
            val file = File(filename)
            val `in` = In(file)
            while (!`in`.isEmpty) {
                val word = `in`.readString()
                if (!st.contains(word)) st.put(word, SET())
                val set = st[word]
                set.add(file)
            }
        }

        // read queries from standard input, one per line
        while (!StdIn.isEmpty) {
            val query = StdIn.readString()
            if (st.contains(query)) {
                val set = st[query]
                for (file in set)
                    StdOut.println("  ${file.name}")
            }
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