/******************************************************************************
 * Compilation:  javac TopM.java
 * Execution:    java TopM m < input.txt
 * Dependencies: MinPQ.kt Transaction.kt StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/24pq/tinyBatch.txt
 *
 * Given an integer m from the command line and an input stream where
 * each line contains a String and a long value, this MinPQ client
 * prints the m lines whose numbers are the highest.
 *
 * % java TopM 5 < tinyBatch.txt
 * Thompson    2/27/2000  4747.08
 * vonNeumann  2/12/1994  4732.35
 * vonNeumann  1/11/1999  4409.74
 * Hoare       8/18/1992  4381.21
 * vonNeumann  3/26/2002  4121.85
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `TopM` class provides a client that reads a sequence of
 * transactions from standard input and prints the *m* largest ones
 * to standard output. This implementation uses a [MinPQ] of size
 * at most *m* + 1 to identify the *M* largest transactions
 * and a [Stack] to output them in the proper order.
 *
 *
 * For additional documentation, see [Section 2.4](https://algs4.cs.princeton.edu/24pq)
 * of *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object TopM {
    /**
     * Reads a sequence of transactions from standard input; takes a
     * command-line integer m; prints to standard output the m largest
     * transactions in descending order.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val m = Integer.parseInt(args[0])
        val pq = MinPQ<Transaction>(m + 1)

        while (StdIn.hasNextLine()) {
            // Create an entry from the next line and put on the PQ.
            val line = StdIn.readLine()
            val transaction = Transaction(line!!)
            pq.insert(transaction)

            // remove minimum if m+1 entries on the PQ
            if (pq.size > m)
                pq.delMin()
        }   // top m entries are on the PQ

        // print entries on PQ in reverse order
        val stack = Stack<Transaction>()
        for (transaction in pq)
            stack.push(transaction)
        for (transaction in stack)
            StdOut.println(transaction)
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
