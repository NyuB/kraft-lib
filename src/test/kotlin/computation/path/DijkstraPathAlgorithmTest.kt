package computation

import computation.path.DijkstraAlgorithm
import computation.path.PathAlgorithm
import computation.path.PathAlgorithmInput
import graph.Edge
import graph.Graph

class DijkstraPathAlgorithmTest : PathAlgorithmTest() {
    override fun instantiateAlgorithm(
        graph: Graph<Char, Edge<Char>>,
        origin: Char,
        destination: Char
    ): PathAlgorithm<Char, Edge<Char>> {
        return DijkstraAlgorithm(PathAlgorithmInput(graph, origin, destination))
    }
}