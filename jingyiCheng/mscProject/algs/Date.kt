/******************************************************************************
 * Compilation:  javac Date.java
 * Execution:    java Date
 * Dependencies: StdOut.kt
 *
 * An immutable data type for dates.
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Date` class is an immutable data type to encapsulate a
 * date (day, month, and year).
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
class Date : Comparable<Date> {
    val month: Int   // month (between 1 and 12)
    val day: Int     // day   (between 1 and DAYS[month]
    val year: Int    // year

    /**
     * Initializes a new date from the month, day, and year.
     * @param month the month (between 1 and 12)
     * @param day the day (between 1 and 28-31, depending on the month)
     * @param year the year
     * @throws IllegalArgumentException if this date is invalid
     */
    constructor(month: Int, day: Int, year: Int) {
        if (!isValid(month, day, year)) throw IllegalArgumentException("Invalid date")
        this.month = month
        this.day = day
        this.year = year
    }

    /**
     * Initializes new date specified as a string in form MM/DD/YYYY.
     * @param date the string representation of this date
     * @throws IllegalArgumentException if this date is invalid
     */
    constructor(date: String) {
        val fields = date.split("/").toTypedArray()
        if (fields.size != 3) {
            throw IllegalArgumentException("Invalid date")
        }
        month = Integer.parseInt(fields[0])
        day = Integer.parseInt(fields[1])
        year = Integer.parseInt(fields[2])
        if (!isValid(month, day, year)) throw IllegalArgumentException("Invalid date")
    }

    /**
     * Returns the next date in the calendar.
     *
     * @return a date that represents the next day after this day
     */
    operator fun next(): Date {
        return when {
            isValid(month, day + 1, year) -> Date(month, day + 1, year)
            isValid(month + 1, 1, year) -> Date(month + 1, 1, year)
            else -> Date(1, 1, year + 1)
        }
    }

    /**
     * Compares two dates chronologically.
     *
     * @param  that the other date
     * @return `true` if this date is after that date; `false` otherwise
     */
    fun isAfter(that: Date): Boolean = compareTo(that) > 0

    /**
     * Compares two dates chronologically.
     *
     * @param  that the other date
     * @return `true` if this date is before that date; `false` otherwise
     */
    fun isBefore(that: Date): Boolean = compareTo(that) < 0

    /**
     * Compares two dates chronologically.
     *
     * @return the value `0` if the argument date is equal to this date;
     * a negative integer if this date is chronologically less than
     * the argument date; and a positive ineger if this date is chronologically
     * after the argument date
     */
    override fun compareTo(other: Date): Int {
        if (this.year < other.year) return -1
        if (this.year > other.year) return +1
        if (this.month < other.month) return -1
        if (this.month > other.month) return +1
        if (this.day < other.day) return -1
        return if (this.day > other.day) +1 else 0
    }

    /**
     * Returns a string representation of this date.
     *
     * @return the string representation in the format MM/DD/YYYY
     */
    override fun toString(): String = "$month/$day/$year"

    /**
     * Compares this date to the specified date.
     *
     * @param  other the other date
     * @return `true` if this date equals `other`; `false` otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other.javaClass != this.javaClass) return false
        val that = other as Date?
        return this.month == that!!.month && this.day == that.day && this.year == that.year
    }

    /**
     * Returns an integer hash code for this date.
     *
     * @return an integer hash code for this date
     */
    override fun hashCode(): Int {
        var hash = 17
        hash = 31 * hash + month
        hash = 31 * hash + day
        hash = 31 * hash + year
        return hash
    }

    companion object {
        private val DAYS = intArrayOf(0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        // is the given date valid?
        private fun isValid(m: Int, d: Int, y: Int): Boolean {
            if (m < 1 || m > 12) return false
            if (d < 1 || d > DAYS[m]) return false
            return !(m == 2 && d == 29 && !isLeapYear(y))
        }

        // is y a leap year?
        private fun isLeapYear(y: Int): Boolean {
            if (y % 400 == 0) return true
            return if (y % 100 == 0) false else y % 4 == 0
        }

        /**
         * Unit tests the `Date` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            var today = Date(2, 25, 2004)
            StdOut.println(today)
            for (i in 0..9) {
                today = today.next()
                StdOut.println(today)
            }

            StdOut.println(today.isAfter(today.next()))
            StdOut.println(today.isAfter(today))
            StdOut.println(today.next().isAfter(today))

            var birthday = Date(10, 16, 1971)
            StdOut.println(birthday)
            for (i in 0..9) {
                birthday = birthday.next()
                StdOut.println(birthday)
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