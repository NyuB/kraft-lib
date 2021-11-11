package computation.flow

import computation.AbstractAlgorithm
import computation.path.PathAlgorithm
import computation.path.PathAlgorithmFactory
import computation.path.PathAlgorithmInput
import graph.EdgePath
import graph.FlowEdge
import graph.FlowGraph
import graph.Graph
import kotlin.math.abs

class BusackerGowenAlgorithm<N, E : FlowEdge<N>>(
    input: MaxFlowAlgorithmInput<N, E>,
    val pathAlgorithmFactory: PathAlgorithmFactory<N, GapEdge<N>>
) :
    MaximumFlowAlgorithm<N, E>(input) {

    private var shortestPathAlgorithm = instantiatePathAlgorithm(generateShortestPathInput())

    private fun resetPathAlgorithmFromGraph() {
        shortestPathAlgorithm = instantiatePathAlgorithm(generateShortestPathInput())
    }

    private fun instantiatePathAlgorithm(input: PathAlgorithmInput<N, GapEdge<N>>): PathAlgorithm<N, GapEdge<N>> {
        return pathAlgorithmFactory.createAlgorithm(input)
    }

    private fun generateShortestPathInput(): PathAlgorithmInput<N, GapEdge<N>> {
        return PathAlgorithmInput(generateGapGraph(), input.graph.source, input.graph.sink)
    }

    private fun Graph<N, GapEdge<N>>.addGapEdgesForEdge(flowEdge: E) {
        if (flowEdge.flow < flowEdge.capacity) {
            this.addEdge(IncreasingGapEdge(flowEdge))
        }
        if (flowEdge.flow > 0) {
            this.addEdge(DecreasingGapEdge(flowEdge))
        }
    }

    private fun generateGapGraph(): Graph<N, GapEdge<N>> {
        val gapGraph = Graph<N, GapEdge<N>>()
        input.graph.getAllEdges().forEach { flowEdge ->
            gapGraph.addGapEdgesForEdge(flowEdge)
        }
        return gapGraph
    }

    private fun getMaximumApplicableFlowDelta(path: EdgePath<N, GapEdge<N>>): Int {
        return (path.edges.minByOrNull { abs(it.flowDelta) }!!.flowDelta)
    }

    private fun updateFlowAlongPath(path: EdgePath<N, GapEdge<N>>) {
        val flowDelta = getMaximumApplicableFlowDelta(path)
        path.edges.forEach { edge ->
            edge.updateReferencedFlow(flowDelta)
        }
    }

    override fun step(): AbstractAlgorithm<MaxFlowAlgorithmInput<N, E>, FlowGraph<N, E>> {
        if (shortestPathAlgorithm.isRunning()) {
            shortestPathAlgorithm.step()
        }
        else {
            val pathResult = shortestPathAlgorithm.getResult()
            pathResult.ifSuccessCompute { path ->
                updateFlowAlongPath(path)
                resetPathAlgorithmFromGraph()
            }.elseThen {
                end(input.graph)
            }
        }
        return this
    }
}