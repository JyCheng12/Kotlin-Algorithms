package jingyiCheng.mscProject.algs

object SWsearch {

    @JvmStatic
    fun main(args: Array<String>) {
        val n = Integer.parseInt(args[0])

        var timer: Stopwatch
        val text = arrayOfNulls<String>(n)
        var text1: StringBuilder

        StdOut.println("Creating...")
        for (i in 0 until n)
            text[i] = System.currentTimeMillis().toString()
        text[n / 2] = "15AAAAAAAAAAA"

        var sum1 = 0.0
        var sum2 = 0.0
        var sum3 = 0.0
        for (j in 0..9) {
            StdOut.println("Shuffling...$j")
            StdRandom.shuffle(text)

            text1 = StringBuilder()
            for (i in 0 until n)
                text1.append(text[i])
            val text2 = text1.toString()

            StdOut.println("Operating...")

            timer = Stopwatch()
            val bm = BoyerMoore("15AAAAAAAAAAA")
            bm.search(text2)
            sum1 += timer.elapsedTime()

            timer = Stopwatch()
            val rk = RabinKarp("15AAAAAAAAAAA")
            rk.search(text2)
            sum2 += timer.elapsedTime()

            timer = Stopwatch()
            val kmp = KMP("15AAAAAAAAAAA")
            kmp.search(text2)
            sum3 += timer.elapsedTime()
        }
        StdOut.println("BM " + sum1 / 10)
        StdOut.println("rk " + sum2 / 10)
        StdOut.println("kmp " + sum3 / 10)
    }
}
