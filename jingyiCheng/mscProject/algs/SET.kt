/******************************************************************************
 * Compilation:  javac SET.java
 * Execution:    java SET
 * Dependencies: StdOut.kt
 *
 * Set implementation using Java's TreeSet library.
 * Does not allow duplicates.
 *
 * % java SET
 * 128.112.136.11
 * 208.216.181.15
 * null
 *
 */

package jingyiCheng.mscProject.algs

import java.util.TreeSet

/**
 * The `SET` class represents an ordered set of comparable keys.
 * It supports the usual *add*, *contains*, and *delete*
 * methods. It also provides ordered methods for finding the *minimum*,
 * *maximum*, *floor*, and *ceiling* and set methods
 * for *union*, *intersection*, and *equality*.
 *
 *
 * Even though this implementation include the method `equals()`, it
 * does not support the method `hashCode()` because sets are mutable.
 *
 *
 * This implementation uses a balanced binary search tree. It requires that
 * the key type implements the `Comparable` interface and calls the
 * `compareTo()` and method to compare two keys. It does not call either
 * `equals()` or `hashCode()`.
 * The *add*, *contains*, *delete*, *minimum*,
 * *maximum*, *ceiling*, and *floor* methods take
 * logarithmic time in the worst case.
 * The *size*, and *is-empty* operations take constant time.
 * Construction takes constant time.
 *
 *
 * For additional documentation, see
 * [Section 3.5](https://algs4.cs.princeton.edu/35applications) of
 * *Algorithms in Java, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 * @param <Key> the generic type of a key in this set
</Key> */

class SET<Key : Comparable<Key>> : Iterable<Key> {
    private var set: TreeSet<Key> = TreeSet()

    /**
     * Returns true if this set is empty.
     *
     * @return `true` if this set is empty;
     * `false` otherwise
     */
    val isEmpty: Boolean
        get() = size() == 0

    /**
     * Initializes an empty set.
     */
    constructor() {
        set = TreeSet()
    }

    /**
     * Initializes a new set that is an independent copy of the specified set.
     *
     * @param x the set to copy
     */
    constructor(x: SET<Key>) {
        set = TreeSet(x.set)
    }

    /**
     * Returns the number of keys in this set.
     *
     * @return the number of keys in this set
     */
    fun size(): Int = set.size

    /**
     * Adds the key to this set (if it is not already present).
     *
     * @param  key the key to add
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun add(key: Key?) {
        if (key == null) throw IllegalArgumentException("called add() with a null key")
        set.add(key)
    }

    /**
     * Returns true if this set contains the given key.
     *
     * @param  key the key
     * @return `true` if this set contains `key`;
     * `false` otherwise
     * @throws IllegalArgumentException if `key` is `null`
     */
    operator fun contains(key: Key?): Boolean {
        if (key == null) throw IllegalArgumentException("called contains() with a null key")
        return set.contains(key)
    }

    /**
     * Removes the specified key from this set (if the set contains the specified key).
     *
     * @param  key the key
     * @throws IllegalArgumentException if `key` is `null`
     */
    fun delete(key: Key?) {
        if (key == null) throw IllegalArgumentException("called delete() with a null key")
        set.remove(key)
    }

    /**
     * Returns all of the keys in this set, as an iterator.
     * To iterate over all of the keys in a set named `set`, use the
     * foreach notation: `for (Key key : set)`.
     *
     * @return an iterator to all of the keys in this set
     */
    override fun iterator() = set.iterator()

    /**
     * Returns the largest key in this set.
     *
     * @return the largest key in this set
     * @throws NoSuchElementException if this set is empty
     */
    fun max(): Key {
        if (isEmpty) throw NoSuchElementException("called max() with empty set")
        return set.last()
    }

    /**
     * Returns the smallest key in this set.
     *
     * @return the smallest key in this set
     * @throws NoSuchElementException if this set is empty
     */
    fun min(): Key {
        if (isEmpty) throw NoSuchElementException("called min() with empty set")
        return set.first()
    }

    /**
     * Returns the smallest key in this set greater than or equal to `key`.
     *
     * @param  key the key
     * @return the smallest key in this set greater than or equal to `key`
     * @throws IllegalArgumentException if `key` is `null`
     * @throws NoSuchElementException if there is no such key
     */
    fun ceiling(key: Key?): Key {
        if (key == null) throw IllegalArgumentException("called ceiling() with a null key")
        return set.ceiling(key) ?: throw NoSuchElementException("all keys are less than $key")
    }

    /**
     * Returns the largest key in this set less than or equal to `key`.
     *
     * @param  key the key
     * @return the largest key in this set table less than or equal to `key`
     * @throws IllegalArgumentException if `key` is `null`
     * @throws NoSuchElementException if there is no such key
     */
    fun floor(key: Key?): Key {
        if (key == null) throw IllegalArgumentException("called floor() with a null key")
        return set.floor(key) ?: throw NoSuchElementException("all keys are greater than $key")
    }

    /**
     * Returns the union of this set and that set.
     *
     * @param  that the other set
     * @return the union of this set and that set
     * @throws IllegalArgumentException if `that` is `null`
     */
    fun union(that: SET<Key>?): SET<Key> {
        if (that == null) throw IllegalArgumentException("called union() with a null argument")
        val c = SET<Key>()
        for (x in this)
            c.add(x)
        for (x in that)
            c.add(x)
        return c
    }

    /**
     * Returns the intersection of this set and that set.
     *
     * @param  that the other set
     * @return the intersection of this set and that set
     * @throws IllegalArgumentException if `that` is `null`
     */
    fun intersects(that: SET<Key>?): SET<Key> {
        if (that == null) throw IllegalArgumentException("called intersects() with a null argument")
        val c = SET<Key>()
        if (this.size() < that.size())
            for (x in this)
                if (that.contains(x)) c.add(x)
        else
            for (x in that)
                if (this.contains(x)) c.add(x)
        return c
    }

    /**
     * Compares this set to the specified set.
     *
     *
     * Note that this method declares two empty sets to be equal
     * even if they are parameterized by different generic types.
     * This is consistent with the behavior of `equals()`
     * within Java's Collections framework.
     *
     * @param  other the other set
     * @return `true` if this set equals `other`;
     * `false` otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (other.javaClass != this.javaClass) return false
        val that = other as SET<*>?
        return this.set == that!!.set
    }

    /**
     * This operation is not supported because sets are mutable.
     *
     * @return does not return a value
     * @throws UnsupportedOperationException if called
     */
    override fun hashCode() = throw UnsupportedOperationException("hashCode() is not supported because sets are mutable")

    /**
     * Returns a string representation of this set.
     *
     * @return a string representation of this set, enclosed in curly braces,
     * with adjacent keys separated by a comma and a space
     */
    override fun toString(): String {
        val s = set.toString()
        return "{ " + s.substring(1, s.length - 1) + " }"
    }

    companion object {
        /**
         * Unit tests the `SET` data type.
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val set = SET<String>()
            StdOut.println("set = $set")

            // insert some keys
            set.add("www.cs.princeton.edu")
            set.add("www.cs.princeton.edu")    // overwrite old value
            set.add("www.princeton.edu")
            set.add("www.math.princeton.edu")
            set.add("www.yale.edu")
            set.add("www.amazon.com")
            set.add("www.simpsons.com")
            set.add("www.stanford.edu")
            set.add("www.google.com")
            set.add("www.ibm.com")
            set.add("www.apple.com")
            set.add("www.slashdot.com")
            set.add("www.whitehouse.gov")
            set.add("www.espn.com")
            set.add("www.snopes.com")
            set.add("www.movies.com")
            set.add("www.cnn.com")
            set.add("www.iitb.ac.in")

            StdOut.println(set.contains("www.cs.princeton.edu"))
            StdOut.println(!set.contains("www.harvardsucks.com"))
            StdOut.println(set.contains("www.simpsons.com"))
            StdOut.println()

            StdOut.println("ceiling(www.simpsonr.com) = ${set.ceiling("www.simpsonr.com")}")
            StdOut.println("ceiling(www.simpsons.com) = ${set.ceiling("www.simpsons.com")}")
            StdOut.println("ceiling(www.simpsont.com) = ${set.ceiling("www.simpsont.com")}")
            StdOut.println("floor(www.simpsonr.com)   = ${set.floor("www.simpsonr.com")}")
            StdOut.println("floor(www.simpsons.com)   = ${set.floor("www.simpsons.com")}")
            StdOut.println("floor(www.simpsont.com)   = ${set.floor("www.simpsont.com")}")
            StdOut.println()

            StdOut.println("set = $set")
            StdOut.println()

            val set2 = SET(set)
            StdOut.println(set == set2)

            set2.delete(set2.min())
            set2.delete(set2.max())
            StdOut.println("union = ${set.union(set2)}")

            val set4 = SET<String>()
            set4.add("www.google.com")
            set4.add("www.ed.ac.uk")
            StdOut.println("intersection = ${set.intersects(set4)}")
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