package computation

import graph.Edge

class PathBackTrackLabelComparator<N, E : Edge<N>>(val distanceGetter : (N) -> Double) : Comparator<PathBacktrackLabel<N,E>> {
    override fun compare(left: PathBacktrackLabel<N, E>?, right: PathBacktrackLabel<N, E>?): Int {
        if(left == null || right == null)throw IllegalArgumentException("Cannot compare null items")
        val distLeft = distanceGetter(left.node)
        val distRight = distanceGetter(right.node)
        return distLeft.compareTo(distRight)

    }
}