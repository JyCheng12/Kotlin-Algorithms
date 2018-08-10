package jingyiCheng.mscProject.algs

object SWgraph {

    @JvmStatic
    fun main(args: Array<String>) {
        StdOut.println("Setting up...")
        val fn = In(args[0])
        val dsp = In(args[1])
        val lpmst = In(args[2])

        StdOut.println("Creating graphs...")
        val FN = FlowNetwork(fn)
        val EWD = EdgeWeightedDigraph(dsp)
        val EWG = EdgeWeightedGraph(lpmst)

        StdOut.println("Applying...")
        var timer = Stopwatch()
        val ff = FordFulkerson(FN, 0, 99999)
        val value = ff.value
        var time = timer.elapsedTime()
        StdOut.println("$time $value")

        timer = Stopwatch()
        val DSP = DijkstraSP(EWD, 0)
        val sp = DSP.distTo(99999)
        time = timer.elapsedTime()
        StdOut.println("$time $sp")

        timer = Stopwatch()
        val LPMST = LazyPrimMST(EWG)
        val wei = LPMST.weight
        time = timer.elapsedTime()
        StdOut.println("$time $wei")

        timer = Stopwatch()
        val PMST = PrimMST(EWG)
        val weight = PMST.weight()
        time = timer.elapsedTime()
        StdOut.println("$time $weight")
    }
}
