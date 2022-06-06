package insa.decaeste.kraft.algorithms.path

import insa.decaeste.kraft.graph.Edge

class BellmanFordAlgorithm<N, E : Edge<N>>(input: PathAlgorithmInput<N, E>) : PathAlgorithm<N, E>(input) {

    private val distances = HashMap<N, Double>()
    private val labels = HashMap<N, PathBacktrackLabel<N, E>>()
    private var updatedSet = mutableSetOf<N>()
    private var stepCount = 0

    init {
        input.graph.nodeSet().forEach {
            distances[it] = Double.MAX_VALUE
        }
        distances[input.origin] = 0.0
        labels[input.origin] = PathBacktrackLabel(input.origin, null)
        updatedSet.add(input.origin)
    }

    override fun step(): PathAlgorithm<N, E> {
        mainLoopStep()
        return this
    }

    private fun mainLoopStep() {
        stepCount++
        val updatedSetCopy: Set<N> = updatedSet.map { it }.toSet()
        updatedSet.clear()
        for (node in updatedSetCopy) {
            for (outEdge in input.graph.getOutEdgesFrom(node)) {
                updateForEdge(outEdge)
            }
        }
        if (updatedSet.isEmpty() || stepCount == input.graph.numberOfNodes()) {
            cycleCheckStep()
        }
    }

    private fun updateForEdge(outEdge: E) {
        val dist = distances[outEdge.origin]!! + outEdge.weight
        if (dist < distances[outEdge.destination]!!) {
            updatedSet.add(outEdge.destination)
            distances[outEdge.destination] = dist
            labels[outEdge.destination] =
                PathBacktrackLabel(outEdge.destination, Pair(outEdge, labels[outEdge.origin]!!))
        }
    }

    private fun cycleCheckStep() {
        if (updatedSet.isNotEmpty() || !labels.containsKey(input.destination)) {
            fail()
        }
        else {
            end(labels[input.destination]!!.asEdgePath(input.origin, input.destination))
        }
    }
}