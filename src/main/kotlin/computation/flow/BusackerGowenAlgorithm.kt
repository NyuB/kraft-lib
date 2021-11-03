package computation.flow

import computation.AbstractAlgorithm
import computation.path.FloodPathAlgorithm
import computation.path.PathAlgorithm
import computation.path.PathAlgorithmInput
import graph.FlowEdge
import graph.FlowGraph
import graph.Graph
import java.lang.Math.abs

class SPBasedMaxFlowAlgorithm<N, E : FlowEdge<N>>(input: MaxFlowAlgorithmInput<N, E>) :
    MaximumFlowAlgorithm<N, E>(input) {
    private var shortestPathAlgorithm = instantiatePathAlgorithm(generateShortestPathInput())

    private fun instantiatePathAlgorithm(input: PathAlgorithmInput<N, GapEdge<N>>): PathAlgorithm<N, GapEdge<N>> {
        return FloodPathAlgorithm(input)
    }

    override fun step(): AbstractAlgorithm<MaxFlowAlgorithmInput<N, E>, FlowGraph<N, E>> {
        val pathResult = shortestPathAlgorithm.computeUntilEnd().getResult()
        println("Iteration")
        pathResult.ifSuccessCompute { path ->
            val maxFlowDelta = path.edges.minByOrNull { abs(it.flowDelta) }!!.flowDelta
            println("Updating flow by delta = $maxFlowDelta")
            path.edges.forEach { edge ->
                println("Updating edge ${edge.origin} -> ${edge.destination}")
                edge.updateReferencedFlow(maxFlowDelta)
            }
            shortestPathAlgorithm = instantiatePathAlgorithm(generateShortestPathInput())
        }.elseThen {
            end(input.graph)
        }
        return this
    }

    private fun generateShortestPathInput(): PathAlgorithmInput<N, GapEdge<N>> {
        return PathAlgorithmInput(generateGapGraph(), input.graph.source, input.graph.sink)
    }

    private fun generateGapGraph(): Graph<N, GapEdge<N>> {
        val gapGraph = Graph<N, GapEdge<N>>()
        for (flowEdge in input.graph.getAllEdges()) {
            if (flowEdge.flow < flowEdge.capacity) {
                gapGraph.addEdge(IncreasingGapEdge(flowEdge))
            }
            if (flowEdge.flow > 0) {
                gapGraph.addEdge(DecreasingGapEdge(flowEdge))
            }
        }
        return gapGraph
    }
}