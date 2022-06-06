package insa.decaeste.kraft.api

import insa.decaeste.kraft.algorithms.ComputationResult
import insa.decaeste.kraft.algorithms.StepableComputation
import insa.decaeste.kraft.algorithms.flow.BusackerGowenAlgorithm
import insa.decaeste.kraft.algorithms.flow.MaxFlowAlgorithmInput
import insa.decaeste.kraft.algorithms.path.BellmanFordAlgorithm
import insa.decaeste.kraft.graph.Edge
import insa.decaeste.kraft.graph.FlowEdge
import insa.decaeste.kraft.graph.FlowGraph

class BipartiteFlowSolver<Worker, Job>(val input: BipartiteProblem<Worker, Job>) : BipartiteSolver<Worker, Job> {

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
        buildInitialGraph()
        algorithm = buildInitialAlgorithm()

    }

    private fun buildInitialAlgorithm(): BusackerGowenAlgorithm<BipartiteNode<Worker, Job>, FlowEdge<BipartiteNode<Worker, Job>>> {
        return BusackerGowenAlgorithm(MaxFlowAlgorithmInput(graph)) { i -> BellmanFordAlgorithm(i) }
    }

    private fun buildInitialGraph() {
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
    }

    override fun step(): StepableComputation<BipartiteProblem<Worker, Job>, BipartiteSolution<Worker, Job>> {
        algorithm.step()
        return this
    }

    override fun isRunning(): Boolean {
        return algorithm.isRunning()
    }

    override fun isEnded(): Boolean {
        return algorithm.isEnded()
    }

    override fun computeUntilEnd(): StepableComputation<BipartiteProblem<Worker, Job>, BipartiteSolution<Worker, Job>> {
        algorithm.computeUntilEnd()
        return this
    }

    override fun getResult(): ComputationResult<BipartiteSolution<Worker, Job>> {
        var res = ComputationResult<BipartiteSolution<Worker, Job>>(null)

        algorithm.getResult().ifSuccessCompute { resultGraph ->
            val resMap = HashMap<Worker, Job>()
            var resCost = 0.0
            for (node in resultGraph.nodeSet().filter { it != sink && it != source && it.isWorker() }) {
                for (edge in resultGraph.getOutEdgesFrom(node)) {
                    if (edge.flow == edge.capacity) {
                        resMap[edge.origin.asWorker()] = edge.destination.asJob()
                        resCost += edge.weight
                    }
                }
            }
            res = ComputationResult(BipartiteSolution(resMap, resCost))
        }

        return res
    }
}