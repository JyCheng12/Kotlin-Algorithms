package jingyiCheng.mscProject.algs

class SWBQS {

    companion object {
        /**
         * Unit tests the `Stopwatch` data type.
         * Takes a command-line argument `n` and computes the
         * sum of the square roots of the first `n` positive integers,
         * first using `Math.sqrt()`, then using `Math.pow()`.
         * It prints to standard output the sum and the amount of time to
         * compute the sum. Note that the discrete sum can be approximated by
         * an integral - the sum should be approximately 2/3 * (n^(3/2) - 1).
         *
         * @param args the command-line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val n = Integer.parseInt(args[0])
            val queue: Queue<Int> = Queue()
            val stack: Stack<Int> = Stack()
            val bag: Bag<Int> = Bag()

/*            var timer = System.currentTimeMillis()
            for (i in 1 until n)
                queue.enqueue(i)
            for (i in 1 until n)
                queue.dequeue()
            val time1 = (System.currentTimeMillis() - timer).toDouble() /1000
            StdOut.printf("End deququeing in %.5f seconds\n", time1)

            timer = System.currentTimeMillis()
            for (i in 1 until n)
                stack.push(i)
            for (i in 1 until n)
                stack.pop()
            val time2 = (System.currentTimeMillis() - timer).toDouble() /1000
            StdOut.printf("End popping in %.5f seconds\n", time2)

            timer = System.currentTimeMillis()
            for (i in 1 until n)
                bag.add(i)
            val time3 = (System.currentTimeMillis() - timer).toDouble() /1000
            StdOut.printf("End adding in %.5f seconds\n", time3)

            val mpq = MinPQ<Int>()
            timer = System.currentTimeMillis()
            for (i in 0 until n)
                mpq.insert(i)
            for (i in 0 until n)
                mpq.delMin()
            val time4 = (System.currentTimeMillis() - timer).toDouble() /1000
            StdOut.printf("delMining (%.3f seconds)\n", time4)*/

            val impq = IndexMinPQ<Int>(n)
            var timer = System.currentTimeMillis()
            for (i in 0 until n)
                impq.insert(n - i - 1, i)
            val time6 = (System.currentTimeMillis()-timer).toDouble() /1000
            println("ipq: $time6")


            timer = System.currentTimeMillis()
            for (i in 0 until n)
                impq.delMin()
            val time5 = (System.currentTimeMillis() - timer).toDouble() /1000
            StdOut.printf("delmining (%.3f seconds)\n", time5)
        }
    }
}
