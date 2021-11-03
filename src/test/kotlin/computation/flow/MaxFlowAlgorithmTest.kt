package computation

import computation.flow.MaxFlowAlgorithmInput
import computation.flow.MaximumFlowAlgorithm
import graph.FlowEdge
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit

abstract class MaxFlowAlgorithmTest {
    abstract fun instantiateAlgorithm(input: MaxFlowAlgorithmInput<Char, FlowEdge<Char>>): MaximumFlowAlgorithm<Char, FlowEdge<Char>>

    @Test
    @Timeout(3, unit = TimeUnit.SECONDS)
    fun `Given the wikipedia graph with 0 flow, set max flow of 5`() {
        val graph = graph.FlowGraphTest().wikiGraph()
        val algo = instantiateAlgorithm(MaxFlowAlgorithmInput(graph))
        val res = algo.computeUntilEnd().getResult()
        assertTrue(res.isSuccess())
        res.ifSuccessCompute { resultGraph ->
            assertEquals(5, resultGraph.totalFlowToSink())
            assertTrue(resultGraph.flowToSink().any { it.flow == 2 })
            assertTrue(resultGraph.flowToSink().any { it.flow == 3 })
        }
    }
}