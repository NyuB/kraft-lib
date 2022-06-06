package insa.decaeste.kraft.graph

open class Graph<N, E : Edge<N>> {

    private val edges = HashMap<N, MutableList<E>>()

    fun addNode(node: N) {
        edges.putIfAbsent(node, ArrayList())
    }

    fun addEdge(edge: E) {
        edges.getOrPut(edge.origin) { ArrayList() }.add(edge)
        edges.putIfAbsent(edge.destination, ArrayList())
    }

    fun containsNode(node: N): Boolean {
        return node in edges.keys
    }

    fun nodeSet(): Set<N> {
        return edges.keys
    }

    fun getOutEdgesFrom(node: N): List<E> {
        return edges[node]
            ?: throw IllegalArgumentException("Requesting edges from a node outside the insa.decaeste.kraft.graph is not allowed")
    }

    fun getInEdgesTo(node: N): List<E> {
        if (!this.containsNode(node)) {
            throw IllegalArgumentException("Requesting edges to a node outside the insa.decaeste.kraft.graph is not allowed")
        }
        else {
            return getAllEdges().filter { it.destination == node }.toList()
        }
    }

    fun getAllEdges(): List<E> {
        return edges.flatMap { it.value }.toList()
    }

    fun numberOfNodes() = edges.size

    fun numberOfEdges(): Int {
        return getAllEdges().size
    }
}