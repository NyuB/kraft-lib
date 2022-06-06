package insa.decaeste.kraft.algorithms.flow

import insa.decaeste.kraft.graph.FlowEdge
import insa.decaeste.kraft.graph.FlowGraph
import insa.decaeste.kraft.graph.bipartiteGraph
import insa.decaeste.kraft.graph.wikiGraph
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
        val graph = wikiGraph()
        val algo = instantiateAlgorithm(MaxFlowAlgorithmInput(graph))
        val res = algo.computeUntilEnd().getResult()
        assertTrue(res.isSuccess())
        res.ifSuccessCompute { resultGraph ->
            assertEquals(5, resultGraph.totalFlowToSink())
            assertTrue(resultGraph.flowToSink().any { it.flow == 2 })
            assertTrue(resultGraph.flowToSink().any { it.flow == 3 })
        }
    }

    @Test
    fun `Given a weighted bipartite graph with three workers and three tasks, set max flow to three`() {
        val graph = bipartiteGraph()
        val algo = instantiateAlgorithm(MaxFlowAlgorithmInput(graph))
        val res = algo.computeUntilEnd().getResult()
        assertTrue(res.isSuccess())
        res.ifSuccessCompute { resultGraph ->
            assertEquals(3, resultGraph.totalFlowToSink())
            assertEquals(3, (resultGraph.flowToSink().count { it.flow == 1 }))
        }
    }

    protected fun flowEdgeIsFull(graph: FlowGraph<Char, FlowEdge<Char>>, origin: Char, destination: Char): Boolean {
        return graph.getAllEdges()
            .filter { it.flow == it.capacity }
            .any { it.origin == origin && it.destination == destination }
    }

    @Test
    fun `Given a bipartite graph with three workers and three tasks, set max flow to three`() {
        val graph = bipartiteGraph()
        val algo = instantiateAlgorithm(MaxFlowAlgorithmInput(graph))
        val res = algo.computeUntilEnd().getResult()
        assertTrue(res.isSuccess())
        res.ifSuccessCompute { resultGraph ->
            assertEquals(3, resultGraph.totalFlowToSink())
            assertEquals(3, (resultGraph.flowToSink().count { it.flow == 1 }))
        }
    }
}