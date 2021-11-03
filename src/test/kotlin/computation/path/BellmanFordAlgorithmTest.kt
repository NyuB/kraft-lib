package computation

import computation.path.BellmanFordAlgorithm
import computation.path.PathAlgorithm
import computation.path.PathAlgorithmInput
import graph.Edge
import graph.Graph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BellmanFordAlgorithmTest : ShortestPathAlgorithmTest() {
    override fun instantiateAlgorithm(
        graph: Graph<Char, Edge<Char>>,
        origin: Char,
        destination: Char
    ): PathAlgorithm<Char, Edge<Char>> {
        return BellmanFordAlgorithm(PathAlgorithmInput(graph, origin, destination))
    }

    @Test
    fun `Given a graph with negative edge without cycle, return the correct result`() =
        withEndResult(negativeEdgesNoCycle(), A, D) {
            assertTrue(it.isSuccess())
            it.ifSuccessCompute { path ->
                assertEquals(3, path.edges.size)
                assertEquals(42.0, path.totalWeight())
            }
        }

    @Test
    fun `Given a graph with negative cycle, return the correct result`() = withEndResult(negativeCycle(), A, D) {
        assertFalse(it.isSuccess())
    }
}