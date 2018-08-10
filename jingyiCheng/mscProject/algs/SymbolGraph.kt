/******************************************************************************
 * Compilation:  javac SymbolGraph.java
 * Execution:    java SymbolGraph filename.txt delimiter
 * Dependencies: ST.kt Graph.kt In.kt StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/41graph/routes.txt
 * https://algs4.cs.princeton.edu/41graph/movies.txt
 * https://algs4.cs.princeton.edu/41graph/moviestiny.txt
 * https://algs4.cs.princeton.edu/41graph/moviesG.txt
 * https://algs4.cs.princeton.edu/41graph/moviestopGrossing.txt
 *
 * %  java SymbolGraph routes.txt " "
 * JFK
 * MCO
 * ATL
 * ORD
 * LAX
 * PHX
 * LAS
 *
 * % java SymbolGraph movies.txt "/"
 * Tin Men (1987)
 * Hershey, Barbara
 * Geppi, Cindy
 * Jones, Kathy (II)
 * Herr, Marcia
 * ...
 * Blumenfeld, Alan
 * DeBoy, David
 * Bacon, Kevin
 * Woodsman, The (2004)
 * Wild Things (1998)
 * Where the Truth Lies (2005)
 * Tremors (1990)
 * ...
 * Apollo 13 (1995)
 * Animal House (1978)
 *
 *
 * Assumes that input file is encoded using UTF-8.
 * % iconv -f ISO-8859-1 -t UTF-8 movies-iso8859.txt > movies.txt
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `SymbolGraph` class represents an undirected graph, where the
 * vertex names are arbitrary strings.
 * By providing mappings between string vertex names and integers,
 * it serves as a wrapper around the
 * [Graph] data type, which assumes the vertex names are integers
 * between 0 and *V* - 1.
 * It also supports initializing a symbol graph from a file.
 *
 *
 * This implementation uses an [ST] to map from strings to integers,
 * an array to map from integers to strings, and a [Graph] to store
 * the underlying graph.
 * The *indexOf* and *contains* operations take time
 * proportional to log *V*, where *V* is the number of vertices.
 * The *nameOf* operation takes constant time.
 *
 *
 * For additional documentation, see [Section 4.1](https://algs4.cs.princeton.edu/41graph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class SymbolGraph
/**
 * Initializes a graph from a file using the specified delimiter.
 * Each line in the file contains
 * the name of a vertex, followed by a list of the names
 * of the vertices adjacent to that vertex, separated by the delimiter.
 * @param filename the name of the file
 * @param delimiter the delimiter between fields
 */
(filename: String, delimiter: String) {
    private val st: ST<String, Int> = ST()  // string -> index
    private val keys: Array<String>           // index  -> string
    val graph: Graph             // the underlying graph

    init {
        // First pass builds the index by reading strings to associate
        // distinct strings with an index
        var `in` = In(filename)
        // while (in.hasNextLine()) {
        while (!`in`.isEmpty) {
            val a = `in`.readLine()!!.split(delimiter)
            for (i in a) {
                if (!st.contains(i))
                    st.put(i, st.size)
            }
        }
        StdOut.println("Done reading $filename")

        // inverted index to get string keys in an aray
        keys = Array(st.size) { "" }
        for (name in st.keys()) {
            keys[st[name]] = name
        }

        // second pass builds the graph by connecting first vertex on each
        // line to all others
        graph = Graph(st.size)
        `in` = In(filename)
        while (`in`.hasNextLine()) {
            val a = `in`.readLine()!!.split(delimiter)
            val v = st[a[0]]
            for (i in 1 until a.size)
                graph.addEdge(v, st[a[i]])
        }
    }

    /**
     * Does the graph contain the vertex named `s`?
     * @param s the name of a vertex
     * @return `true` if `s` is the name of a vertex, and `false` otherwise
     */
    operator fun contains(s: String) = st.contains(s)

    /**
     * Returns the integer associated with the vertex named `s`.
     * @param s the name of a vertex
     * @return the integer (between 0 and *V* - 1) associated with the vertex named `s`
     */
    @Deprecated("Replaced by {@link #indexOf(String)}.")
    fun index(s: String) = st[s]

    /**
     * Returns the integer associated with the vertex named `s`.
     * @param s the name of a vertex
     * @return the integer (between 0 and *V* - 1) associated with the vertex named `s`
     */
    fun indexOf(s: String) = st[s]

    /**
     * Returns the name of the vertex associated with the integer `v`.
     * @param  v the integer corresponding to a vertex (between 0 and *V* - 1)
     * @return the name of the vertex associated with the integer `v`
     * @throws IllegalArgumentException unless `0 <= v < V`
     */
    @Deprecated("Replaced by {@link #nameOf(int)}.")
    fun name(v: Int): String {
        validateVertex(v)
        return keys[v]
    }

    /**
     * Returns the name of the vertex associated with the integer `v`.
     * @param  v the integer corresponding to a vertex (between 0 and *V* - 1)
     * @throws IllegalArgumentException unless `0 <= v < V`
     * @return the name of the vertex associated with the integer `v`
     */
    fun nameOf(v: Int): String {
        validateVertex(v)
        return keys[v]
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        if (v < 0 || v >= graph.V) throw IllegalArgumentException("vertex " + v + " is not between 0 and " + (graph.V - 1))
    }

    companion object {
        /**
         * Unit tests the `SymbolGraph` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val filename = args[0]
            val delimiter = args[1]
            val sg = SymbolGraph(filename, delimiter)
            val graph = sg.graph
            while (StdIn.hasNextLine()) {
                val source = StdIn.readLine()!!
                if (sg.contains(source)) {
                    val s = sg.indexOf(source)
                    for (v in graph.adj(s)) {
                        StdOut.println("   " + sg.nameOf(v))
                    }
                } else
                    StdOut.println("input not contain '$source'")
            }
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
