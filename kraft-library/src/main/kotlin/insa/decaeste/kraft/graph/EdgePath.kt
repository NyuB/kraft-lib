package insa.decaeste.kraft.graph

class EdgePath<N, E : Edge<N>>(val origin: N, val destination: N, val edges: List<E>) {

    val isValid = validate()

    private fun validate(): Boolean {
        if (edges.isEmpty()) {
            return origin == destination
        }
        else if (origin != edges[0].origin || destination != edges.last().destination) {
            return false
        }
        else {
            var lastDestination = edges[0].destination
            for (i in 1 until edges.size) {
                val edge = edges[i]
                if (edge.origin != lastDestination) {
                    return false
                }
                lastDestination = edge.destination
            }
            return true
        }
    }

    fun totalWeight(): Double {
        return edges.sumOf { it.weight }
    }

    fun <ExtendedEdgeType : Edge<N>> isSamePath(other: EdgePath<N, ExtendedEdgeType>): Boolean {
        if (other.origin != this.origin) return false
        if (other.destination != this.destination) return false
        if (other.edges.size != this.edges.size) return false
        for (i in edges.indices) {
            val thisEdge = this.edges[i]
            val otherEdge = other.edges[i]
            if (thisEdge.origin != otherEdge.origin) return false
            if (thisEdge.destination != otherEdge.destination) return false
        }
        return true
    }
}

