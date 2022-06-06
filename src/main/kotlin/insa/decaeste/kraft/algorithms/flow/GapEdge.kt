package insa.decaeste.kraft.algorithms.flow

import insa.decaeste.kraft.graph.Edge
import insa.decaeste.kraft.graph.FlowEdge

sealed class GapEdge<N>(val referenced: FlowEdge<N>, val flowDelta: Int, revert: Boolean) :
    Edge<N>(
        if (revert) referenced.destination else referenced.origin,
        if (revert) referenced.origin else referenced.destination,
        if (revert) -referenced.weight else referenced.weight
    ) {

    abstract fun updateReferencedFlow(maxDelta: Int)
}