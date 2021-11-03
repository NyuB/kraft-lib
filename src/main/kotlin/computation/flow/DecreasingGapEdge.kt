package computation.flow

import graph.FlowEdge

class DecreasingGapEdge<N>(referenced: FlowEdge<N>) : GapEdge<N>(referenced, (-referenced.flow), true) {
    override fun updateReferencedFlow(maxDelta: Int) {
        println("\t${referenced.flow} -> ${referenced.flow - maxDelta}")
        referenced.flow -= maxDelta
    }
}