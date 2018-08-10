/******************************************************************************
 * Compilation:  javac Arbitrage.java
 * Execution:    java Arbitrage < input.txt
 * Dependencies: EdgeWeightedDigraph.kt DirectedEdge.kt
 * BellmanFordSP.kt
 * Data file:    https://algs4.cs.princeton.edu/44sp/rates.txt
 *
 * Arbitrage detection.
 *
 * % more rates.txt
 * 5
 * USD 1      0.741  0.657  1.061  1.005
 * EUR 1.349  1      0.888  1.433  1.366
 * GBP 1.521  1.126  1      1.614  1.538
 * CHF 0.942  0.698  0.619  1      0.953
 * CAD 0.995  0.732  0.650  1.049  1
 *
 * % java Arbitrage < rates.txt
 * 1000.00000 USD =  741.00000 EUR
 * 741.00000 EUR = 1012.20600 CAD
 * 1012.20600 CAD = 1007.14497 USD
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Arbitrage` class provides a client that finds an arbitrage
 * opportunity in a currency exchange table by constructing a
 * complete-digraph representation of the exchange table and then finding
 * a negative cycle in the digraph.
 *
 *
 * This implementation uses the Bellman-Ford algorithm to find a
 * negative cycle in the complete digraph.
 * The running time is proportional to *V*<sup>3</sup> in the
 * worst case, where *V* is the number of currencies.
 *
 *
 * For additional documentation,
 * see [Section 4.4](https://algs4.cs.princeton.edu/44sp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 */
object Arbitrage {

    /**
     * Reads the currency exchange table from standard input and
     * prints an arbitrage opportunity to standard output (if one exists).
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {

        val In = In(args[0])
        // V currencies
        val V = In.readInt()
        val name = Array(V) { "" }

        // create complete network
        val G = EdgeWeightedDigraph(V)
        for (v in 0 until V) {
            name[v] = In.readString()
            for (w in 0 until V) {
                val rate = In.readDouble()
                val e = DirectedEdge(v, w, -Math.log(rate))
                G.addEdge(e)
            }
        }

        // find negative cycle
        val spt = BellmanFordSP(G, 0)
        if (spt.hasNegativeCycle()) {
            var stake = 1000.0
            for (e in spt.cycle!!) {
                StdOut.printf("%10.5f %s ", stake, name[e.from])
                stake *= Math.exp(-e.weight)
                StdOut.printf("= %10.5f %s\n", stake, name[e.to])
            }
        } else {
            StdOut.println("No arbitrage opportunity")
        }
    }

}// this class cannot be instantiated

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
