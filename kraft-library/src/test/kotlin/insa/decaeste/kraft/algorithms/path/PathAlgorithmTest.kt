package insa.decaeste.kraft.algorithms.path

import insa.decaeste.kraft.algorithms.ComputationResult
import insa.decaeste.kraft.graph.Edge
import insa.decaeste.kraft.graph.EdgePath
import insa.decaeste.kraft.graph.Graph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.fail
import java.util.concurrent.TimeUnit


abstract class PathAlgorithmTest {

    abstract fun instantiateAlgorithm(
        graph: Graph<Char, Edge<Char>>,
        origin: Char,
        destination: Char
    ): PathAlgorithm<Char, Edge<Char>>

    val A = 'A'
    val B = 'B'
    val C = 'C'
    val D = 'D'
    val E = 'E'
    val F = 'F'
    val G = 'G'
    val H = 'H'
    val I = 'I'
    val J = 'J'
    val K = 'K'
    val L = 'L'
    val M = 'M'
    val N = 'N'
    val O = 'O'
    val P = 'P'
    val Q = 'Q'
    val R = 'R'
    val S = 'S'

    protected fun singleNodeGraph(node: Char): Graph<Char, Edge<Char>> {
        val graph = Graph<Char, Edge<Char>>()
        graph.addNode(node)
        return graph
    }

    protected fun chainGraph(): Graph<Char, Edge<Char>> {
        val result = Graph<Char, Edge<Char>>()
        result.addEdge(Edge(A, B))
        result.addEdge(Edge(B, C))
        result.addEdge(Edge(C, D))
        result.addEdge(Edge(D, E))
        return result
    }

    protected fun stronglyConnectedGraph(): Graph<Char, Edge<Char>> {
        val result = Graph<Char, Edge<Char>>()
        result.addEdge(Edge(A, B))
        result.addEdge(Edge(B, C))
        result.addEdge(Edge(C, A))
        result.addEdge(Edge(C, D))
        result.addEdge(Edge(D, C))
        result.addEdge(Edge(D, E))
        result.addEdge(Edge(E, F))
        result.addEdge(Edge(F, D))
        return result
    }

    protected fun negativeEdgesNoCycle(): Graph<Char, Edge<Char>> {
        val result = Graph<Char, Edge<Char>>()
        result.addEdge(Edge(A, B, 37.0))
        result.addEdge(Edge(B, C, -5.0))
        result.addEdge(Edge(C, D, 10.0))
        return result
    }

    protected fun negativeCycle(): Graph<Char, Edge<Char>> {
        val result = negativeEdgesNoCycle()
        result.addEdge(Edge(C, B, 3.0))
        return result
    }

    protected fun twoIsolatedComponentGraph(): Graph<Char, Edge<Char>> {
        val result = Graph<Char, Edge<Char>>()
        result.addEdge(Edge(A, B))
        result.addEdge(Edge(B, C))
        result.addEdge(Edge(C, A))

        result.addEdge(Edge(D, E))
        result.addEdge(Edge(E, F))
        result.addEdge(Edge(F, D))
        return result
    }

    fun wikipediaDijkstraGraph(): Graph<Char, Edge<Char>> {
        val graph = Graph<Char, Edge<Char>>()

        graph.addEdge(Edge(A, B, 7.0))
        graph.addEdge(Edge(A, C, 9.0))
        graph.addEdge(Edge(A, F, 14.0))
        graph.addEdge(Edge(B, A, 7.0))
        graph.addEdge(Edge(C, A, 9.0))
        graph.addEdge(Edge(F, A, 14.0))
        graph.addEdge(Edge(B, C, 10.0))
        graph.addEdge(Edge(C, B, 10.0))
        graph.addEdge(Edge(C, D, 11.0))
        graph.addEdge(Edge(D, C, 11.0))
        graph.addEdge(Edge(C, F, 2.0))
        graph.addEdge(Edge(F, C, 2.0))
        graph.addEdge(Edge(D, E, 6.0))
        graph.addEdge(Edge(E, D, 6.0))
        graph.addEdge(Edge(E, F, 9.0))
        graph.addEdge(Edge(F, E, 9.0))

        return graph
    }

    fun withEndResult(
        graph: Graph<Char, Edge<Char>>,
        origin: Char, destination: Char,
        exec: (ComputationResult<EdgePath<Char, Edge<Char>>>) -> Unit
    ) {
        exec(instantiateAlgorithm(graph, origin, destination).computeUntilEnd().getResult())
    }

    fun executeInBoundedSteps(
        graph: Graph<Char, Edge<Char>>,
        origin: Char, destination: Char,
        lowBoundInclusive: Int,
        highBoundInclusive: Int,
        exec: (ComputationResult<EdgePath<Char, Edge<Char>>>) -> Unit
    ) {
        var algo = instantiateAlgorithm(graph, origin, destination)
        var count = 0
        while (algo.isRunning()) {
            if (count > highBoundInclusive) {
                fail("Too many steps")
            }
            algo.step()
            count++
        }
        if (count < lowBoundInclusive) {
            fail("Too few steps")
        }
        exec(algo.getResult())
    }

    @Test
    fun `Given an origin node not in the graph, throw IllegalArgument error at instanciation`() {
        val graph = singleNodeGraph(A)
        assertThrows(IllegalArgumentException::class.java) {
            instantiateAlgorithm(graph, A, B)
        }
    }

    @Test
    fun `Given the same origin and destination, terminates with a success result and an empty path`() =
        withEndResult(chainGraph(), A, A) {
            assertTrue(it.isSuccess())
            it.ifSuccessCompute {
                assertTrue(it.isValid)
                assertTrue(it.edges.isEmpty())
            }
        }

    @Test
    fun `Given an unreachable destination from origin, terminates with a failure result`() =
        withEndResult(twoIsolatedComponentGraph(), A, D) {
            assertFalse(it.isSuccess())
        }

    @Test
    @Timeout(5, unit = TimeUnit.SECONDS)
    fun `Given a strongly connected graph, each pair of nodes should result in a success`() {
        val graph = stronglyConnectedGraph()
        for (origin in graph.nodeSet()) {
            for (destination in graph.nodeSet()) {
                val algo = instantiateAlgorithm(graph, origin, destination)
                algo.computeUntilEnd()
                val result = algo.getResult()
                assertTrue(result.isSuccess())
            }
        }
    }

    @Test
    fun `Given a directed acyclic chain graph, result should be the unique shortest path`() {
        val graph = chainGraph()
        val expected = EdgePath(A, E, listOf(Edge(A, B), Edge(B, C), Edge(C, D), Edge(D, E)))
        val algo = instantiateAlgorithm(graph, A, E)
        val result = algo.computeUntilEnd().getResult()
        assert(result.isSuccess())
        result.ifSuccessCompute {
            assertTrue(it.isValid)
            assertTrue(it.isSamePath(expected))
        }
    }
}