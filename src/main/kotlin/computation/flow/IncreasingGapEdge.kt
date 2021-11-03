package computation.flow

import graph.FlowEdge

class IncreasingGapEdge<N>(referenced: FlowEdge<N>) :
    GapEdge<N>(referenced, referenced.capacity - referenced.flow, false) {
    override fun updateReferencedFlow(maxDelta: Int) {
        println("\t${referenced.flow} -> ${referenced.flow + maxDelta}")
        referenced.flow += maxDelta
    }
}