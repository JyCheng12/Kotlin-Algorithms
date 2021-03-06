/******************************************************************************
 * Compilation:  javac Knuth.java
 * Execution:    java Knuth < list.txt
 * Dependencies: StdIn.kt StdOut.kt
 * Data files:   https://algs4.cs.princeton.edu/11model/cards.txt
 * https://algs4.cs.princeton.edu/11model/cardsUnicode.txt
 *
 * Reads in a list of strings and prints them in random order.
 * The Knuth (or Fisher-Yates) shuffling algorithm guarantees
 * to rearrange the elements in uniformly random order, under
 * the assumption that Math.random() generates independent and
 * uniformly distributed numbers between 0 and 1.
 *
 * % more cards.txt
 * 2C 3C 4C 5C 6C 7C 8C 9C 10C JC QC KC AC
 * 2D 3D 4D 5D 6D 7D 8D 9D 10D JD QD KD AD
 * 2H 3H 4H 5H 6H 7H 8H 9H 10H JH QH KH AH
 * 2S 3S 4S 5S 6S 7S 8S 9S 10S JS QS KS AS
 *
 * % java Knuth < cards.txt
 * 6H
 * 9C
 * 8H
 * 7C
 * JS
 * ...
 * KH
 *
 * % more cardsUnicode.txt
 * 2♣ 3♣ 4♣ 5♣ 6♣ 7♣ 8♣ 9♣ 10♣ J♣ Q♣ K♣ A♣
 * 2♦ 3♦ 4♦ 5♦ 6♦ 7♦ 8♦ 9♦ 10♦ J♦ Q♦ K♦ A♦
 * 2♥ 3♥ 4♥ 5♥ 6♥ 7♥ 8♥ 9♥ 10♥ J♥ Q♥ K♥ A♥
 * 2♠ 3♠ 4♠ 5♠ 6♠ 7♠ 8♠ 9♠ 10♠ J♠ Q♠ K♠ A♠
 *
 * % java Knuth < cardsUnicode.txt
 * 2♠
 * K♥
 * 6♥
 * 5♣
 * J♣
 * ...
 * A♦
 *
 */

package jingyiCheng.mscProject.algs

/**
 * The `Knuth` class provides a client for reading in a
 * sequence of strings and *shuffling* them using the Knuth (or Fisher-Yates)
 * shuffling algorithm. This algorithm guarantees to rearrange the
 * elements in uniformly random order, under
 * the assumption that Math.random() generates independent and
 * uniformly distributed numbers between 0 and 1.
 *
 *
 * For additional documentation,
 * see [Section 1.1](https://algs4.cs.princeton.edu/11model) of
 * *Algorithms, 4th Edition* by Robert Sedgewick and Kevin Wayne.
 * See [StdRandom] for versions that shuffle arrays and
 * subarrays of objects, doubles, and ints.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author Jingyi Cheng
 *
 */
object Knuth {
    /**
     * Rearranges an array of objects in uniformly random order
     * (under the assumption that `Math.random()` generates independent
     * and uniformly distributed numbers between 0 and 1).
     * @param a the array to be shuffled
     */
    fun <T> shuffle(a: Array<T>) {
        val n = a.size
        for (i in 0 until n) {
            // choose index uniformly in [0, i]
            val r = (Math.random() * (i + 1)).toInt()
            val swap = a[r]
            a[r] = a[i]
            a[i] = swap
        }
    }

    /**
     * Rearranges an array of objects in uniformly random order
     * (under the assumption that `Math.random()` generates independent
     * and uniformly distributed numbers between 0 and 1).
     * @param a the array to be shuffled
     */
    fun <T> shuffleAlternate(a: Array<T>) {
        val n = a.size
        for (i in 0 until n) {
            // choose index uniformly in [i, n-1]
            val r = i + (Math.random() * (n - i)).toInt()
            val swap = a[r]
            a[r] = a[i]
            a[i] = swap
        }
    }

    /**
     * Reads in a sequence of strings from standard input, shuffles
     * them, and prints out the results.
     *
     * @param args the command-line arguments
     */
    @JvmStatic
    fun main(args: Array<String>) {
        // read in the data
        val a = StdIn.readAllStrings()

        // shuffle the array
        Knuth.shuffle(a)

        // print results.
        for (i in a)
            StdOut.println(i)
    }
}// this class should not be instantiated

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