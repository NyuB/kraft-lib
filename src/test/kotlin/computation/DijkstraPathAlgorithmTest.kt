package computation

import graph.Edge
import graph.Graph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class DijkstraPathAlgorithmTest : PathAlgorithmTest() {
    override fun instantiateAlgorithm(
        graph: Graph<Char, Edge<Char>>,
        origin: Char,
        destination: Char
    ): PathAlgorithm<Char, Edge<Char>> {
        return DijkstraAlgorithm(PathAlgorithmInput(graph, origin ,destination))
    }
    
    @Test
    fun `Given the 'Wikipedia Dijkstra Algorithm' graph, find the correct result in a correct amount of steps`() = executeInBoundedSteps(wikipediaGraph(), A, E, 5,6) {
        assertTrue(it.isSuccess())
        it.ifSuccessCompute { path ->
            assertTrue(path.isValid)
            assertEquals(3, path.edges.size)
            assertEquals(20.0, path.totalWeight())
        }
    }
}