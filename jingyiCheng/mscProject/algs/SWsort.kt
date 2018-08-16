package jingyiCheng.mscProject.algs

object SWsort {

    @JvmStatic
    fun main(args: Array<String>) {
        val n = Integer.parseInt(args[0])

        val text = arrayOfNulls<String>(n)
        for (i in 0 until n) {
            text[i] = System.currentTimeMillis().toString()
        }

        StdOut.println("Shuffling...")
        StdRandom.shuffle(text)
        val text0 = text as Array<String>
        val text1 = Array(n){text0[it]}
        val text2 = Array(n){text0[it]}
        val text3 = Array(n){text0[it]}

        val leng = text0[0].length
        StdOut.println("operations...")

        var timer = Stopwatch()
        LSD.sort(text1, leng)
        var time = timer.elapsedTime()
        StdOut.println("LSD $time")

        timer = Stopwatch()
        QuickX.sort(text2)
        time = timer.elapsedTime()
        StdOut.println("QuickX $time")

        timer = Stopwatch()
        Merge.sort(text3)
        time = timer.elapsedTime()
        StdOut.println("Merge $time")

    }
}
