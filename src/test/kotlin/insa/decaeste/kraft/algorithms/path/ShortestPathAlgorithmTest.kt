package insa.decaeste.kraft.algorithms.path

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

abstract class ShortestPathAlgorithmTest : PathAlgorithmTest() {

    @Test
    fun `Given the 'Wikipedia Dijkstra Algorithm' graph, find the correct result`() {
        withEndResult(wikipediaDijkstraGraph(), A, E) {
            Assertions.assertTrue(it.isSuccess())
            it.ifSuccessCompute { path ->
                Assertions.assertTrue(path.isValid)
                Assertions.assertEquals(3, path.edges.size)
                Assertions.assertEquals(20.0, path.totalWeight())
            }
        }
    }
}