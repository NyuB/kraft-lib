package insa.decaeste.kraft.algorithms.flow

import insa.decaeste.kraft.algorithms.AbstractAlgorithm
import insa.decaeste.kraft.graph.FlowEdge
import insa.decaeste.kraft.graph.FlowGraph

abstract class MaximumFlowAlgorithm<N, E : FlowEdge<N>>(input: MaxFlowAlgorithmInput<N, E>) :
    AbstractAlgorithm<MaxFlowAlgorithmInput<N, E>, FlowGraph<N, E>>(input) {

    override fun validateInputAndRaiseException() {
        if (!input.graph.hasValidFlows()) {
            throw IllegalArgumentException("Invalid flow values in insa.decaeste.kraft.graph")
        }
    }
}