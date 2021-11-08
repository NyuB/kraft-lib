package computation.path

import graph.Edge
import graph.Graph

class FloodPathAlgorithmTest() : PathAlgorithmTest() {

    override fun instantiateAlgorithm(
        graph: Graph<Char, Edge<Char>>,
        origin: Char,
        destination: Char
    ): PathAlgorithm<Char, Edge<Char>> {
        return FloodPathAlgorithm(PathAlgorithmInput(graph, origin, destination))
    }
}