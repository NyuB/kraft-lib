package computation.path

import computation.AbstractAlgorithm
import graph.Edge
import graph.EdgePath

abstract class PathAlgorithm<N, E : Edge<N>>(input: PathAlgorithmInput<N, E>) :
    AbstractAlgorithm<PathAlgorithmInput<N, E>, EdgePath<N, E>>(input) {

    init {
        this.validateInputAndRaiseException()
    }

    override fun validateInputAndRaiseException() {
        if (!containsBothOriginAndDestination()) {
            throw IllegalArgumentException("Both origin and destination should belong to the graph")
        }
    }

    private fun containsBothOriginAndDestination(): Boolean {
        return input.graph.containsNode(input.origin) && input.graph.containsNode(input.destination)
    }

    protected fun isDestination(item: PathBacktrackLabel<N, E>): Boolean {
        return item.node == input.destination
    }
}