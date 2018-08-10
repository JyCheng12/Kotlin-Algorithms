/******************************************************************************
 * Compilation:  javac KosarajuSharirSCC.java
 * Execution:    java KosarajuSharirSCC filename.txt
 * Dependencies: Digraph.kt TransitiveClosure.kt StdOut.kt In.kt
 * Data files:   https://algs4.cs.princeton.edu/42digraph/tinyDG.txt
 * https://algs4.cs.princeton.edu/42digraph/mediumDG.txt
 * https://algs4.cs.princeton.edu/42digraph/largeDG.txt
 *
 * Compute the strongly-connected components of a digraph using the
 * Kosaraju-Sharir algorithm.
 *
 * Runs in O(E + V) time.
 *
 * % java KosarajuSharirSCC tinyDG.txt
 * 5 strong components
 * 1
 * 0 2 3 4 5
 * 9 10 11 12
 * 6 8
 * 7
 *
 * % java KosarajuSharirSCC mediumDG.txt
 * 10 strong components
 * 21
 * 2 5 6 8 9 11 12 13 15 16 18 19 22 23 25 26 28 29 30 31 32 33 34 35 37 38 39 40 42 43 44 46 47 48 49
 * 14
 * 3 4 17 20 24 27 36
 * 41
 * 7
 * 45
 * 1
 * 0
 * 10
 *
 * % java -Xss50m KosarajuSharirSCC mediumDG.txt
 * 25 strong components
 * 7 11 32 36 61 84 95 116 121 128 230   ...
 * 28 73 80 104 115 143 149 164 184 185  ...
 * 38 40 200 201 207 218 286 387 418 422 ...
 * 12 14 56 78 87 103 216 269 271 272    ...
 * 42 48 112 135 160 217 243 246 273 346 ...
 * 46 76 96 97 224 237 297 303 308 309   ...
 * 9 15 21 22 27 90 167 214 220 225 227  ...
 * 74 99 133 146 161 166 202 205 245 262 ...
 * 43 83 94 120 125 183 195 206 244 254  ...
 * 1 13 54 91 92 93 106 140 156 194 208  ...
 * 10 39 67 69 131 144 145 154 168 258   ...
 * 6 52 66 113 118 122 139 147 212 213   ...
 * 8 127 150 182 203 204 249 367 400 432 ...
 * 63 65 101 107 108 136 169 170 171 173 ...
 * 55 71 102 155 159 198 228 252 325 419 ...
 * 4 25 34 58 70 152 172 196 199 210 226 ...
 * 2 44 50 88 109 138 141 178 197 211    ...
 * 57 89 129 162 174 179 188 209 238 276 ...
 * 33 41 49 119 126 132 148 181 215 221  ...
 * 3 18 23 26 35 64 105 124 157 186 251  ...
 * 5 16 17 20 31 47 81 98 158 180 187    ...
 * 24 29 51 59 75 82 100 114 117 134 151 ...
 * 30 45 53 60 72 85 111 130 137 142 163 ...
 * 19 37 62 77 79 110 153 352 353 361    ...
 * 0 68 86 123 165 176 193 239 289 336   ...
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `KosarajuSharirSCC` class represents a data type for
 * determining the strong components in a digraph.
 * The *id* operation determines in which strong component
 * a given vertex lies; the *areStronglyConnected* operation
 * determines whether two vertices are in the same strong component;
 * and the *count* operation determines the number of strong
 * components.
 *
 * The *component identifier* of a component is one of the
 * vertices in the strong component: two vertices have the same component
 * identifier if and only if they are in the same strong component.
 *
 *
 *
 * This implementation uses the Kosaraju-Sharir algorithm.
 * The constructor takes time proportional to *V* + *E*
 * (in the worst case),
 * where *V* is the number of vertices and *E* is the number of edges.
 * Afterwards, the *id*, *count*, and *areStronglyConnected*
 * operations take constant time.
 * For alternate implementations of the same API, see
 * [TarjanSCC] and [GabowSCC].
 *
 *
 * For additional documentation,
 * see [Section 4.2](https://algs4.cs.princeton.edu/42digraph) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class KosarajuSharirSCC
/**
 * Computes the strong components of the digraph `G`.
 * @param G the digraph
 */
(G: Digraph) {
    private val marked: BooleanArray     // marked[v] = has vertex v been visited?
    private val id: IntArray             // id[v] = id of strong component containing v
    var count: Int = 0            // number of strongly-connected components
        private set

    init {
        // compute reverse postorder of reverse graph
        val dfs = DepthFirstOrder(G.reverse())

        // run DFS on G, using reverse postorder to guide calculation
        marked = BooleanArray(G.V)
        id = IntArray(G.V)
        for (v in dfs.reversePost()) {
            if (!marked[v]) {
                dfs(G, v)
                count++
            }
        }

        // check that id[] gives strong components
        assert(check(G))
    }

    // DFS on graph G
    private fun dfs(G: Digraph, v: Int) {
        marked[v] = true
        id[v] = count
        for (w in G.adj(v)) {
            if (!marked[w]) dfs(G, w)
        }
    }

    /**
     * Are vertices `v` and `w` in the same strong component?
     * @param  v one vertex
     * @param  w the other vertex
     * @return `true` if vertices `v` and `w` are in the same
     * strong component, and `false` otherwise
     * @throws IllegalArgumentException unless `0 <= v < V`
     * @throws IllegalArgumentException unless `0 <= w < V`
     */
    fun stronglyConnected(v: Int, w: Int): Boolean {
        validateVertex(v)
        validateVertex(w)
        return id[v] == id[w]
    }

    /**
     * Returns the component id of the strong component containing vertex `v`.
     * @param  v the vertex
     * @return the component id of the strong component containing vertex `v`
     * @throws IllegalArgumentException unless `0 <= s < V`
     */
    fun id(v: Int): Int {
        validateVertex(v)
        return id[v]
    }

    // does the id[] array contain the strongly connected components?
    private fun check(G: Digraph): Boolean {
        val tc = TransitiveClosure(G)
        for (v in 0 until G.V) {
            for (w in 0 until G.V) {
                if (stronglyConnected(v, w) != (tc.reachable(v, w) && tc.reachable(w, v)))
                    return false
            }
        }
        return true
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private fun validateVertex(v: Int) {
        val V = marked.size
        if (v < 0 || v >= V) throw IllegalArgumentException("vertex $v is not between 0 and ${V - 1}")
    }

    companion object {
        /**
         * Unit tests the `KosarajuSharirSCC` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val `in` = In(args[0])
            val G = Digraph(`in`)
            val scc = KosarajuSharirSCC(G)

            // number of connected components
            val m = scc.count
            StdOut.println("$m strong components")

            // compute list of vertices in each strong component
            val components = Array(m) { nnQueue<Int>() }
            for (v in 0 until G.V) {
                components[scc.id(v)].enqueue(v)
            }

            // print results
            for (i in 0 until m) {
                for (v in components[i]) {
                    StdOut.print("$v ")
                }
                StdOut.println()
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
