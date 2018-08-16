/******************************************************************************
 * Compilation:  javac Transaction.java
 * Execution:    java Transaction
 * Dependencies: StdOut.kt
 *
 * Data type for commercial transactions.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Transaction` class is an immutable data type to encapsulate a
 * commercial transaction with a customer name, date, and amount.
 *
 *
 * For additional documentation,
 * see [Section 1.2](https://algs4.cs.princeton.edu/12oop) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
class Transaction : Comparable<Transaction> {
    val who: String      // customer
    val `when`: Date     // date
    val amount: Double   // amount

    /**
     * Initializes a new transaction from the given arguments.
     *
     * @param  who the person involved in this transaction
     * @param  when the date of this transaction
     * @param  amount the amount of this transaction
     * @throws IllegalArgumentException if `amount`
     * is `Double.NaN`, `Double.POSITIVE_INFINITY`,
     * or `Double.NEGATIVE_INFINITY`
     */
    constructor(who: String, `when`: Date, amount: Double) {
        if (amount.isNaN() || amount.isInfinite())
            throw IllegalArgumentException("Amount cannot be NaN or infinite")
        this.who = who
        this.`when` = `when`
        this.amount = amount
    }

    /**
     * Initializes a new transaction by parsing a string of the form NAME DATE AMOUNT.
     *
     * @param  transaction the string to parse
     * @throws IllegalArgumentException if `amount`
     * is `Double.NaN`, `Double.POSITIVE_INFINITY`,
     * or `Double.NEGATIVE_INFINITY`
     */
    constructor(transaction: String) {
        val a = transaction.split("\\s+").toTypedArray()
        who = a[0]
        `when` = Date(a[1])
        amount = a[2].toDouble()
        if (amount.isNaN() || amount.isInfinite()) throw IllegalArgumentException("Amount cannot be NaN or infinite")
    }

    /**
     * Returns a string representation of this transaction.
     *
     * @return a string representation of this transaction
     */
    override fun toString() = String.format("%-10s %10s %8.2f", who, `when`, amount)

    /**
     * Compares two transactions by amount.
     *
     * @param  other the other transaction
     * @return { a negative integer, zero, a positive integer}, depending
     * on whether the amount of this transaction is { less than,
     * equal to, or greater than } the amount of that transaction
     */
    override fun compareTo(other: Transaction) = this.amount.compareTo(other.amount)

    /**
     * Compares this transaction to the specified object.
     *
     * @param  other the other transaction
     * @return true if this transaction is equal to `other`; false otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other.javaClass != this.javaClass) return false
        val that = other as Transaction?
        return (this.amount == that!!.amount && this.who == that.who
                && this.`when`.equals(that.`when`))
    }

    /**
     * Returns a hash code for this transaction.
     *
     * @return a hash code for this transaction
     */
    override fun hashCode(): Int {
        var hash = 1
        hash = 31 * hash + who.hashCode()
        hash = 31 * hash + `when`.hashCode()
        hash = 31 * hash + amount.toDouble().hashCode()
        return hash
        // return Objects.hash(who, when, amount);
    }

    /**
     * Compares two transactions by customer name.
     */
    class WhoOrder : Comparator<Transaction> {
        override fun compare(v: Transaction, w: Transaction) = v.who.compareTo(w.who)
    }

    /**
     * Compares two transactions by date.
     */
    class WhenOrder : Comparator<Transaction> {
        override fun compare(v: Transaction, w: Transaction) = v.`when`.compareTo(w.`when`)
    }

    /**
     * Compares two transactions by amount.
     */
    class HowMuchOrder : Comparator<Transaction> {
        override fun compare(v: Transaction, w: Transaction) = v.amount.compareTo(w.amount)
    }

    companion object {
        /**
         * Unit tests the `Transaction` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val a = arrayOf(
                    Transaction("Turing   6/17/1990  644.08"),
                    Transaction("Tarjan   3/26/2002 4121.85"),
                    Transaction("Knuth    6/14/1999  288.34"),
                    Transaction("Dijkstra 8/22/2007 2678.40")
            )
            StdOut.println("Unsorted")
            for (i in a)
                StdOut.println(i)
            StdOut.println()

            StdOut.println("Sort by date")
            a.sortWith(WhenOrder())
            for (i in a)
                StdOut.println(i)
            StdOut.println()

            StdOut.println("Sort by customer")
            a.sortWith(WhoOrder())
            for (i in a)
                StdOut.println(i)
            StdOut.println()

            StdOut.println("Sort by amount")
            a.sortWith(HowMuchOrder())
            for (i in a)
                StdOut.println(i)
            StdOut.println()
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