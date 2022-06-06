package insa.decaeste.kraft.algorithms.flow

import insa.decaeste.kraft.graph.FlowEdge

class IncreasingGapEdge<N>(referenced: FlowEdge<N>) :
    GapEdge<N>(referenced, referenced.capacity - referenced.flow, false) {

    override fun updateReferencedFlow(maxDelta: Int) {
        referenced.flow += maxDelta
    }
}