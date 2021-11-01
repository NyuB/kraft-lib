package computation

import graph.Edge
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

open class DijkstraAlgorithm<N, E : Edge<N>>(input : PathAlgorithmInput<N ,E>) : PathAlgorithm<N,E>(input) {
    private val priorityQueue = PriorityQueue(getLabelComparator())

    protected fun getLabelComparator() : Comparator<PathBacktrackLabel<N,E>> {
        return PathBackTrackLabelComparator {
            nodesCurrentDistanceMap[it]!!
        }
    }

    private val fixedNodes = mutableSetOf<N>()

    private val nodesCurrentDistanceMap = HashMap<N, Double>()

    init {
        priorityQueue.add(PathBacktrackLabel(input.origin, null))
        nodesCurrentDistanceMap[input.origin] = 0.0
    }

    override fun step(): PathAlgorithm<N, E> {
        if(priorityQueue.size == 0){
            fail()
        }
        else {
            val treatedNode = priorityQueue.poll()
            if(treatedNode.node in fixedNodes){
                //Shortest path to this node has already been computed
            }
            else if(isDestination(treatedNode)){
                end(treatedNode.asEdgePath(input.origin, input.destination))
            }
            else{
                fixedNodes.add(treatedNode.node)
                for(outingEdge in input.graph.getOutEdgesFrom(treatedNode.node)){
                    updateQueueWithReachableEdge(treatedNode, outingEdge)
                }
            }
        }
        return this
    }

    private fun updateQueueWithReachableEdge(treatedNode: PathBacktrackLabel<N,E>, outingEdge : E){
        val dist = distanceWithEdge(outingEdge)
        if(dist < currentDistance(outingEdge.destination)){
            updateCurrentDistance(outingEdge.destination, dist)
            priorityQueue.add(PathBacktrackLabel(outingEdge.destination, Pair(outingEdge, treatedNode)))
        }
    }

    private fun currentDistance(node : N): Double {
        return nodesCurrentDistanceMap.getOrDefault(node, Double.MAX_VALUE)
    }

    private fun updateCurrentDistance(node : N, distance: Double){
        priorityQueue.removeIf {it.node == node}
        nodesCurrentDistanceMap[node] = distance
    }

    private fun distanceWithEdge(edge : E): Double {
        return currentDistance(edge.origin) + edge.weight
    }
}