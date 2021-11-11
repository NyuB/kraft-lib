package computation.flow

import computation.AbstractAlgorithm
import graph.FlowEdge
import graph.FlowGraph

abstract class MaximumFlowAlgorithm<N, E : FlowEdge<N>>(input: MaxFlowAlgorithmInput<N, E>) :
    AbstractAlgorithm<MaxFlowAlgorithmInput<N, E>, FlowGraph<N, E>>(input) {

    override fun validateInputAndRaiseException() {
        if (!input.graph.hasValidFlows()) {
            throw IllegalArgumentException("Invalid flow values in graph")
        }
    }
}