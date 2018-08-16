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
        StdOut.println("FordFulkerson: $time $value")

        timer = Stopwatch()
        val DSP = DijkstraSP(EWD, 0)
        val sp = DSP.distTo(99999)
        time = timer.elapsedTime()
        StdOut.println("DijkstraSP: $time $sp")

        timer = Stopwatch()
        val LPMST = LazyPrimMST(EWG)
        val wei = LPMST.weight
        time = timer.elapsedTime()
        StdOut.println("LazyPrimMST: $time $wei")

        timer = Stopwatch()
        val PMST = PrimMST(EWG)
        val weight = PMST.weight
        time = timer.elapsedTime()
        StdOut.println("PrimMST: $time $weight")

        timer = Stopwatch()
        val BMST = BoruvkaMST(EWG)
        val wei1 = BMST.weight
        time = timer.elapsedTime()
        StdOut.println("BoruvkaMST: $time $wei1")

        timer = Stopwatch()
        val KMST = KruskalMST(EWG)
        val wei2 = KMST.weight
        time = timer.elapsedTime()
        StdOut.println("KruskalMST: $time $wei2")

        timer = Stopwatch()
        val DUSP = DijkstraUndirectedSP(EWG, 0)
        val sup = DUSP.distTo(99999)
        time = timer.elapsedTime()
        StdOut.println("DijkstraUndirectedSP: $time $sup")

        timer = Stopwatch()
        val BSP = BellmanFordSP(EWD, 0)
        val sbp = BSP.distTo(99999)
        time = timer.elapsedTime()
        StdOut.println("BellmanFordSP: $time $sbp")
    }
}