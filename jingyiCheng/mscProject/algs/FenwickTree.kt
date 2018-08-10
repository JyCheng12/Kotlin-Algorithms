/******************************************************************************
 * Compilation:  javac FenwickTree.java
 * Execution:    java FenwickTree
 *
 * A Fenwick tree.
 *
 */

package jingyiCheng.mscProject.algs

import jingyiCheng.mscProject.algs.StdOut.print

/**
 * Created by ricardodpsx@gmail.com on 4/01/15.
 *
 *
 * In `Fenwick Tree` structure We arrange the array in an smart way to perform efficient *range queries and updates*.
 * The key point is this: In a fenwick array, each position "responsible" for storing cumulative data of N previous positions (N could be 1)
 * For example:
 * array[40] stores: array[40] + array[39] ... + array[32] (8 positions)
 * array[32] stores: array[32] + array[31] ... + array[1]  (32 positions)
 *
 *
 * **But, how do you know how much positions a given index is "responsible" for?**
 *
 *
 * To know the number of items that a given array position 'ind' is responsible for
 * We should extract from 'ind' the portion up to the first significant one of the binary representation of 'ind'
 * for example, given ind == 40 (101000 in binary), according to Fenwick algorithm
 * what We want is to extract 1000(8 in decimal).
 *
 *
 * This means that array[40] has cumulative information of 8 array items.
 * But We still need to know the cumulative data bellow array[40 - 8 = 32]
 * 32 is  100000 in binnary, and the portion up to the least significant one is 32 itself!
 * So array[32] has information of 32 items, and We are done!
 *
 *
 * So cummulative data of array[1...40] = array[40] + array[32]
 * Because 40 has information of items from 40 to 32, and 32 has information of items from 32 to  1
 *
 *
 * Memory usage:  O(n)
 *
 * @author Ricardo Pacheco
 * @author Jingyi Cheng
 */
class FenwickTree(size: Int) {
    internal var array: IntArray = IntArray(size + 1) // 1-indexed array, In this array We save cumulative information to perform efficient range queries and updates

    /**
     * Range Sum query from 1 to ind
     * ind is 1-indexed
     *
     *
     * Time-Complexity:    O(log(n))
     *
     * @param  ind index
     * @return sum
     */
    fun rsq(ind: Int): Int {
        var ind = ind
        assert(ind > 0)
        var sum = 0
        while (ind > 0) {
            sum += array[ind]
            //Extracting the portion up to the first significant one of the binary representation of 'ind' and decrementing ind by that number
            ind -= ind and -ind
        }
        return sum
    }

    /**
     * Range Sum Query from a to b.
     * Search for the sum from array index from a to b
     * a and b are 1-indexed
     *
     *
     * Time-Complexity:    O(log(n))
     *
     * @param  a left index
     * @param  b right index
     * @return sum
     */
    fun rsq(a: Int, b: Int): Int {
        assert(a in 1..b && b > 0)
        return rsq(b) - rsq(a - 1)
    }

    /**
     * Update the array at ind and all the affected regions above ind.
     * ind is 1-indexed
     *
     *
     * Time-Complexity:    O(log(n))
     *
     * @param  ind   index
     * @param  value value
     */
    fun update(ind: Int, value: Int) {
        var ind = ind
        assert(ind > 0)
        while (ind < array.size) {
            array[ind] += value
            //Extracting the portion up to the first significant one of the binary representation of 'ind' and incrementing ind by that number
            ind += ind and -ind
        }
    }

    fun size() = array.size - 1

    companion object {
        /**
         * Read the following commands:
         * init n     Initializes the array of size n all zeroes
         * set a b c    Initializes the array  with [a, b, c ...]
         * rsq a b      Range Sum Query for the range [a,b]
         * up  i v      Update the i position of the array with value v.
         * exit
         *
         *
         * The array is 1-indexed
         * Example:
         * set 1 2 3 4 5 6
         * rsq 1 3
         * Sum from 1 to 3 = 6
         * rmq 1 3
         * Min from 1 to 3 = 1
         * input up 1 3
         * [3,2,3,4,5,6]
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            var ft: FenwickTree? = null
            while (true) {
                val line = StdIn.readLine()!!.split(" ")
                if (line[0] == "exit") break

                var arg1 = 0
                var arg2 = 0

                if (line.size > 1) {
                    arg1 = Integer.parseInt(line[1])
                }
                if (line.size > 2) {
                    arg2 = Integer.parseInt(line[2])
                }
                if (line[0] != "set" && line[0] != "init" && ft == null) {
                    StdOut.println("FenwickTree not initialized")
                    continue
                }

                when (line[0]) {
                    "init" -> {
                        ft = FenwickTree(arg1)
                        for (i in 1..ft.size()) {
                            print(ft.rsq(i, i).toString() + " ")
                        }
                        StdOut.println()
                    }
                    "set" -> {
                        ft = FenwickTree(line.size - 1)
                        for (i in 1 until line.size) {
                            ft.update(i, Integer.parseInt(line[i]))
                        }
                    }
                    "up" -> {
                        ft!!.update(arg1, arg2)
                        for (i in 1..ft.size()) {
                            print("${ft.rsq(i, i)} ")
                        }
                        StdOut.println()
                    }
                    "rsq" -> StdOut.printf("Sum from %d to %d = %d%n", arg1, arg2, ft!!.rsq(arg1, arg2))
                    else -> StdOut.println("Invalid command")
                }
            }
            StdOut.close()
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
