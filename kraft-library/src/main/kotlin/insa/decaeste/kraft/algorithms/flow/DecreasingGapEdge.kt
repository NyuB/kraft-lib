package insa.decaeste.kraft.algorithms.flow

import insa.decaeste.kraft.graph.FlowEdge

class DecreasingGapEdge<N>(referenced: FlowEdge<N>) : GapEdge<N>(referenced, (-referenced.flow), true) {

    override fun updateReferencedFlow(maxDelta: Int) {
        referenced.flow -= maxDelta
    }
}