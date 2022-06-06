package insa.decaeste.kraft.algorithms.path

import insa.decaeste.kraft.graph.Edge
import insa.decaeste.kraft.graph.Graph

class DijkstraPathAlgorithmTest : PathAlgorithmTest() {

    override fun instantiateAlgorithm(
        graph: Graph<Char, Edge<Char>>,
        origin: Char,
        destination: Char
    ): PathAlgorithm<Char, Edge<Char>> {
        return DijkstraAlgorithm(PathAlgorithmInput(graph, origin, destination))
    }
}