package insa.decaeste.kraft.algorithms.flow

import insa.decaeste.kraft.algorithms.path.BellmanFordAlgorithm
import insa.decaeste.kraft.algorithms.path.FloodPathAlgorithm
import insa.decaeste.kraft.graph.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BusackerGowenWithFloodTest : MaxFlowAlgorithmTest() {

    override fun instantiateAlgorithm(input: MaxFlowAlgorithmInput<Char, FlowEdge<Char>>): MaximumFlowAlgorithm<Char, FlowEdge<Char>> {
        return BusackerGowenAlgorithm(input) { i -> FloodPathAlgorithm(i) }
    }
}

class BusackerGowenWithBellmanFordTest : MaxFlowAlgorithmTest() {

    override fun instantiateAlgorithm(input: MaxFlowAlgorithmInput<Char, FlowEdge<Char>>): MaximumFlowAlgorithm<Char, FlowEdge<Char>> {
        return BusackerGowenAlgorithm(input) { i -> BellmanFordAlgorithm(i) }
    }

    @Test
    fun `Given a weighted bipartite graph with three workers and three tasks, each worker is assinged the less weighted task`() {
        val graph = weightedBipartiteGraph()
        val algo = instantiateAlgorithm(MaxFlowAlgorithmInput(graph))
        val res = algo.computeUntilEnd().getResult()
        Assertions.assertTrue(res.isSuccess())
        res.ifSuccessCompute { resultGraph ->
            Assertions.assertTrue(flowEdgeIsFull(resultGraph, A, F))
            Assertions.assertTrue(flowEdgeIsFull(resultGraph, B, D))
            Assertions.assertTrue(flowEdgeIsFull(resultGraph, C, E))
        }
    }
}