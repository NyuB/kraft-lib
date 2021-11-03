package computation

import computation.flow.MaxFlowAlgorithmInput
import computation.flow.MaximumFlowAlgorithm
import computation.flow.SPBasedMaxFlowAlgorithm
import graph.FlowEdge

class SPBasedMaxFlowAlgorithmTest : MaxFlowAlgorithmTest() {
    override fun instantiateAlgorithm(input: MaxFlowAlgorithmInput<Char, FlowEdge<Char>>): MaximumFlowAlgorithm<Char, FlowEdge<Char>> {
        return SPBasedMaxFlowAlgorithm(input)
    }
}