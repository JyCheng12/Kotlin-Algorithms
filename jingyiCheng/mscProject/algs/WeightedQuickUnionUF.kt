/******************************************************************************
 * Compilation:  javac WeightedQuickUnionUF.java
 * Execution:  java WeightedQuickUnionUF < input.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/15uf/tinyUF.txt
 * https://algs4.cs.princeton.edu/15uf/mediumUF.txt
 * https://algs4.cs.princeton.edu/15uf/largeUF.txt
 *
 * Weighted quick-union (without path compression).
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `WeightedQuickUnionUF` class represents a *union–find data type*
 * (also known as the *disjoint-sets data type*).
 * It supports the *union* and *find* operations,
 * along with a *connected* operation for determining whether
 * two sites are in the same component and a *count* operation that
 * returns the total number of components.
 *
 *
 * The union–find data type models connectivity among a set of *n*
 * sites, named 0 through *n*–1.
 * The *is-connected-to* relation must be an
 * *equivalence relation*:
 *
 *  *  *Reflexive*: *p* is connected to *p*.
 *  *  *Symmetric*: If *p* is connected to *q*,
 * then *q* is connected to *p*.
 *  *  *Transitive*: If *p* is connected to *q*
 * and *q* is connected to *r*, then
 * *p* is connected to *r*.
 *
 *
 *
 * An equivalence relation partitions the sites into
 * *equivalence classes* (or *components*). In this case,
 * two sites are in the same component if and only if they are connected.
 * Both sites and components are identified with integers between 0 and
 * *n*–1.
 * Initially, there are *n* components, with each site in its
 * own component.  The *component identifier* of a component
 * (also known as the *root*, *canonical element*, *leader*,
 * or *set representative*) is one of the sites in the component:
 * two sites have the same component identifier if and only if they are
 * in the same component.
 *
 *  * *union*(*p*, *q*) adds a
 * connection between the two sites *p* and *q*.
 * If *p* and *q* are in different components,
 * then it replaces
 * these two components with a new component that is the union of
 * the two.
 *  * *find*(*p*) returns the component
 * identifier of the component containing *p*.
 *  * *connected*(*p*, *q*)
 * returns true if both *p* and *q*
 * are in the same component, and false otherwise.
 *  * *count*() returns the number of components.
 *
 *
 *
 * The component identifier of a component can change
 * only when the component itself changes during a call to
 * *union*—it cannot change during a call
 * to *find*, *connected*, or *count*.
 *
 *
 * This implementation uses weighted quick union by size (without path compression).
 * Initializing a data structure with *n* sites takes linear time.
 * Afterwards, the *union*, *find*, and *connected*
 * operations  take logarithmic time (in the worst case) and the
 * *count* operation takes constant time.
 * For alternate implementations of the same API, see
 * [UF], [QuickFindUF], and [QuickUnionUF].
 *
 *
 *
 * For additional documentation, see [Section 1.5](https://algs4.cs.princeton.edu/15uf) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class WeightedQuickUnionUF
/**
 * Initializes an empty union–find data structure with `n` sites
 * `0` through `n-1`. Each site is initially in its own
 * component.
 *
 * @param  n the number of sites
 * @throws IllegalArgumentException if `n < 0`
 */
(n: Int) {
    var count = n           // number of components
        private set

    init {
        if (count < 0) throw IllegalArgumentException()
    }

    private val parent = IntArray(count) { it }    // parent[i] = parent of i
    private val size = IntArray(count) { 1 }      // size[i] = number of sites in subtree rooted at i
    /**
     * Returns the component identifier for the component containing site `p`.
     *
     * @param  p the integer representing one object
     * @return the component identifier for the component containing site `p`
     * @throws IllegalArgumentException unless `0 <= p < n`
     */
    fun find(p: Int): Int {
        var p = p
        validate(p)
        while (p != parent[p])
            p = parent[p]
        return p
    }

    // validate that p is a valid index
    private fun validate(p: Int) {
        val n = parent.size
        if (p < 0 || p >= n) throw IllegalArgumentException("index $p is not between 0 and ${n - 1}")
    }

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @return `true` if the two sites `p` and `q` are in the same component;
     * `false` otherwise
     * @throws IllegalArgumentException unless
     * both `0 <= p < n` and `0 <= q < n`
     */
    fun connected(p: Int, q: Int) = find(p) == find(q)

    /**
     * Merges the component containing site `p` with the
     * the component containing site `q`.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @throws IllegalArgumentException unless
     * both `0 <= p < n` and `0 <= q < n`
     */
    fun union(p: Int, q: Int) {
        val rootP = find(p)
        val rootQ = find(q)
        if (rootP == rootQ) return

        // make smaller root point to larger one
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ
            size[rootQ] += size[rootP]
        } else {
            parent[rootQ] = rootP
            size[rootP] += size[rootQ]
        }
        count--
    }

    companion object {


        /**
         * Reads in a sequence of pairs of integers (between 0 and n-1) from standard input,
         * where each integer represents some object;
         * if the sites are in different components, merge the two components
         * and print the pair to standard output.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val n = StdIn.readInt()
            val uf = WeightedQuickUnionUF(n)
            while (!StdIn.isEmpty) {
                val p = StdIn.readInt()
                val q = StdIn.readInt()
                if (uf.connected(p, q)) continue
                uf.union(p, q)
                StdOut.println("$p $q")
            }
            StdOut.println("${uf.count} components")
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