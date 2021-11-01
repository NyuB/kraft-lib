package computation

import graph.Edge
import graph.EdgePath

class PathBacktrackLabel<N, E : Edge<N>>(val node : N, val previous : Pair<E, PathBacktrackLabel<N, E>>?) {
    fun isLastItem() = previous == null

    private fun asEdgePath(origin : N, destination : N, accumulator : MutableList<E>) : EdgePath<N, E> {
        return if(previous == null){
            EdgePath(origin, destination, accumulator.reversed())
        } else{
            accumulator.add(previous.first)
            previous.second.asEdgePath(origin, destination, accumulator)
        }
    }

    fun asEdgePath(origin : N, destination: N) = asEdgePath(origin, destination, emptyList<E>().toMutableList())
}