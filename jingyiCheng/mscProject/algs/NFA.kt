/******************************************************************************
 * Compilation:  javac NFA.java
 * Execution:    java NFA regexp text
 * Dependencies: Stack.kt Bag.kt Digraph.kt DirectedDFS.kt
 *
 * % java NFA "(A*B|AC)D" AAAABD
 * true
 *
 * % java NFA "(A*B|AC)D" AAAAC
 * false
 *
 * % java NFA "(a|(bc)*d)*" abcbcd
 * true
 *
 * % java NFA "(a|(bc)*d)*" abcbcbcdaaaabcbcdaaaddd
 * true
 *
 * Remarks
 * -----------
 * The following features are not supported:
 * - The + operator
 * - Multiway or
 * - Metacharacters in the text
 * - Character classes.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `NFA` class provides a data type for creating a
 * *nondeterministic finite state automaton* (NFA) from a regular
 * expression and testing whether a given string is matched by that regular
 * expression.
 * It supports the following operations: *concatenation*,
 * *closure*, *binary or*, and *parentheses*.
 * It does not support *mutiway or*, *character classes*,
 * *metacharacters* (either in the text or pattern),
 * *capturing capabilities*, *greedy* or *relucantant*
 * modifiers, and other features in industrial-strength implementations
 * such as [java.util.regex.Pattern] and [java.util.regex.Matcher].
 *
 *
 * This implementation builds the NFA using a digraph and a stack
 * and simulates the NFA using digraph search (see the textbook for details).
 * The constructor takes time proportional to *m*, where *m*
 * is the number of characters in the regular expression.
 * The *recognizes* method takes time proportional to *m n*,
 * where *n* is the number of characters in the text.
 *
 *
 * For additional documentation,
 * see [Section 5.4](https://algs4.cs.princeton.edu/54regexp) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class NFA
/**
 * Initializes the NFA from the specified regular expression.
 *
 * @param  regexp the regular expression
 */
(private val regexp: String     // regular expression
) {
    private val m: Int = regexp.length       // number of characters in regular expression
    private val graph = Digraph(m+1)    // digraph of epsilon transitions

    init {
        val ops = nnStack<Int>()

        for (i in 0 until m) {
            var lp = i
            if (regexp[i] == '(' || regexp[i] == '|')
                ops.push(i)
            else if (regexp[i] == ')') {
                val or = ops.pop()

                // 2-way or operator
                when (regexp[or]) {
                    '|' -> {
                        lp = ops.pop()
                        graph.addEdge(lp, or + 1)
                        graph.addEdge(or, i)
                    }
                    '(' -> lp = or
                    else -> assert(false)
                }
            }

            // closure operator (uses 1-character lookahead)
            if (i < m - 1 && regexp[i + 1] == '*') {
                graph.addEdge(lp, i + 1)
                graph.addEdge(i + 1, lp)
            }
            if (regexp[i] == '(' || regexp[i] == '*' || regexp[i] == ')')
                graph.addEdge(i, i + 1)
        }
        if (ops.size != 0)
            throw IllegalArgumentException("Invalid regular expression")
    }

    /**
     * Returns true if the text is matched by the regular expression.
     *
     * @param  txt the text
     * @return `true` if the text is matched by the regular expression,
     * `false` otherwise
     */
    fun recognizes(txt: String): Boolean {
        var dfs = DirectedDFS(graph, 0)
        var pc = nnBag<Int>()
        for (v in 0 until graph.V)
            if (dfs.marked(v)) pc.add(v)

        // Compute possible NFA states for txt[i+1]
        for (i in 0 until txt.length) {
            if (txt[i] == '*' || txt[i] == '|' || txt[i] == '(' || txt[i] == ')')
                throw IllegalArgumentException("text contains the metacharacter '${txt[i]}'")

            val match = nnBag<Int>()
            for (v in pc) {
                if (v == m) continue
                if (regexp[v] == txt[i] || regexp[v] == '.')
                    match.add(v + 1)
            }
            dfs = DirectedDFS(graph, match)
            pc = nnBag<Int>()
            for (v in 0 until graph.V)
                if (dfs.marked(v)) pc.add(v)

            // optimization if no states reachable
            if (pc.size == 0) return false
        }

        // check for accept state
        for (v in pc)
            if (v == m) return true
        return false
    }

    companion object {
        /**
         * Unit tests the `NFA` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val regexp = "(${args[0]})"
            val txt = args[1]
            val nfa = NFA(regexp)
            StdOut.println(nfa.recognizes(txt))
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
