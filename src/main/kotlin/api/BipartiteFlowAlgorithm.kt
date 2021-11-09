package api

import computation.ComputationResult
import computation.StepableComputation
import computation.flow.BusackerGowenAlgorithm
import computation.flow.MaxFlowAlgorithmInput
import computation.path.BellmanFordAlgorithm
import graph.Edge
import graph.FlowEdge
import graph.FlowGraph

class BipartiteFlowAlgorithm<Worker, Job>(val input: BipartiteProblem<Worker, Job>) : BipartiteSolver<Worker, Job> {

    internal class BipartiteNode<A, B> {
        var worker: A? = null
        var job: B? = null
        fun setAsWorker(worker: A): BipartiteNode<A, B> {
            this.worker = worker
            this.job = null
            return this
        }

        fun setAsJob(job: B): BipartiteNode<A, B> {
            this.job = job
            this.worker = null
            return this
        }

        fun isWorker(): Boolean {
            return worker != null
        }

        fun isJob(): Boolean {
            return job != null
        }

        fun asWorker(): A = worker!!
        fun asJob(): B = job!!
    }

    private val source = BipartiteNode<Worker, Job>()
    private val sink = BipartiteNode<Worker, Job>()
    private val graph = FlowGraph(source, sink)
    private val algorithm: BusackerGowenAlgorithm<BipartiteNode<Worker, Job>, FlowEdge<BipartiteNode<Worker, Job>>>

    init {
        val jobNodes = ArrayList<BipartiteNode<Worker, Job>>()

        for (job in input.jobs) {
            val jobNode = BipartiteNode<Worker, Job>().setAsJob(job)
            jobNodes.add(jobNode)
            graph.addEdge(FlowEdge(Edge(jobNode, sink), 1))
        }

        for (worker in input.workers) {
            val workerNode = BipartiteNode<Worker, Job>().setAsWorker(worker)
            graph.addEdge(FlowEdge(Edge(source, workerNode), 1))
            for (jobNode in jobNodes) {
                val edge = FlowEdge(Edge(workerNode, jobNode, input.weights[worker]!![jobNode.asJob()]!!), 1)
                graph.addEdge(edge)
            }
        }

        algorithm = BusackerGowenAlgorithm(MaxFlowAlgorithmInput(graph)) { i -> BellmanFordAlgorithm(i) }
    }

    override fun step(): StepableComputation<BipartiteProblem<Worker, Job>, Map<Worker, Job>> {
        algorithm.step()
        return this
    }

    override fun isRunning(): Boolean {
        return algorithm.isRunning()
    }

    override fun isEnded(): Boolean {
        return algorithm.isEnded()
    }

    override fun computeUntilEnd(): StepableComputation<BipartiteProblem<Worker, Job>, Map<Worker, Job>> {
        algorithm.computeUntilEnd()
        return this
    }

    override fun getResult(): ComputationResult<Map<Worker, Job>> {
        val algorithmResult = algorithm.getResult()
        var res = ComputationResult<Map<Worker, Job>>(null)

        algorithmResult.ifSuccessCompute { resultGraph ->
            val resMap = HashMap<Worker, Job>()
            for (node in resultGraph.nodeSet().filter { it != sink && it != source && it.isWorker() }) {
                for (edge in resultGraph.getOutEdgesFrom(node)) {
                    if (edge.flow == edge.capacity) {
                        resMap[edge.origin.asWorker()] = edge.destination.asJob()
                    }
                }
            }
            res = ComputationResult(resMap)
        }
        return res
    }
}