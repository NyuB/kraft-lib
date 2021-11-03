package computation.path

import graph.Edge
import java.util.*

class FloodPathAlgorithm<N, E : Edge<N>>(input: PathAlgorithmInput<N, E>) : PathAlgorithm<N, E>(input) {
    private val queue: Queue<PathBacktrackLabel<N, E>> = LinkedList(listOf(PathBacktrackLabel(input.origin, null)))
    private val visitedNodes = mutableSetOf(input.origin)

    override fun step(): FloodPathAlgorithm<N, E> {
        if (queue.size == 0) {
            fail()
        } else {
            val nodeToTreat = queue.poll()
            if (isDestination(nodeToTreat)) {
                end(nodeToTreat.asEdgePath(input.origin, input.destination))
            } else {
                for (outingEdge in input.graph.getOutEdgesFrom(nodeToTreat.node)) {
                    if (outingEdge.destination !in visitedNodes) {
                        visitedNodes.add(outingEdge.destination)
                        queue.add(PathBacktrackLabel(outingEdge.destination, Pair(outingEdge, nodeToTreat)))
                    }
                }
            }
        }
        return this
    }
}