package graph

class FlowEdge<N>(edge: Edge<N>, val capacity: Int, flow: Int = 0) :
    Edge<N>(edge.origin, edge.destination, edge.weight) {

    var flow = validateFlow(flow)
        set(value) {
            field = validateFlow(value)
        }

    private fun validateFlow(flow: Int): Int {
        if (flow in 0..capacity) {
            return flow
        }
        else {
            throw IllegalArgumentException("Flow must be in [0..capacity] range, ${flow} is invalid")
        }
    }
}