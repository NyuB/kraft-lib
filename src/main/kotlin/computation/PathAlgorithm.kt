package computation

import graph.Edge
import graph.EdgePath

abstract class PathAlgorithm<N, E : Edge<N>>(val input : PathAlgorithmInput<N,E>) {
    private var ended = false
    private var result = AlgorithmResult<EdgePath<N, E>>(null)
    init {
        this.validateInputAndRaiseException()
    }

    fun validateInputAndRaiseException() {
        if(!containsBothOriginAndDestination()){
            throw IllegalArgumentException("Both origin and destination should belong to the graph")
        }
    }

    private fun containsBothOriginAndDestination() : Boolean {
        return input.graph.containsNode(input.origin) && input.graph.containsNode(input.destination)
    }

    abstract fun step() : PathAlgorithm<N,E>
    protected fun end(result : EdgePath<N, E>?) {
        this.result = AlgorithmResult(result)
        this.ended = true
    }
    protected fun fail() {
        this.end(null)
    }

    fun isRunning() = !ended
    fun isEnded() = ended

    fun computeUntilEnd() : PathAlgorithm<N, E> {
        while(isRunning()){
            step()
        }
        return this
    }

    fun getResult() : AlgorithmResult<EdgePath<N, E>> = result

    protected fun isDestination(item : PathBacktrackLabel<N,E>) : Boolean {
        return item.node == input.destination
    }
}