package insa.decaeste.kraft.graph

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

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
val T = 'T'
val sink = T
val source = S

//https://fr.wikipedia.org/wiki/Probl%C3%A8me_de_flot_maximum#/media/Fichier:Max_flow.svg
fun wikiGraph(): FlowGraphOfChar {
    val graph = FlowGraphOfChar(source, sink)
    graph.addEdge(FlowEdge(Edge(source, O), 3))
    graph.addEdge(FlowEdge(Edge(source, P), 3))
    graph.addEdge(FlowEdge(Edge(O, Q), 3))
    graph.addEdge(FlowEdge(Edge(P, R), 2))
    graph.addEdge(FlowEdge(Edge(Q, T), 2))
    graph.addEdge(FlowEdge(Edge(Q, R), 4))
    graph.addEdge(FlowEdge(Edge(R, T), 3))
    return graph
}

fun weightedBipartiteGraph(): FlowGraphOfChar {
    val graph = FlowGraphOfChar(source, sink)
    val workers = listOf(A, B, C)
    val tasks = listOf(D, E, F)
    val weightMap: MutableMap<Char, Map<Char, Double>> = HashMap()
    weightMap[A] = mapOf(Pair(D, 3.0), Pair(E, 2.0), Pair(F, 1.0))
    weightMap[B] = mapOf(Pair(D, 1.0), Pair(E, 3.0), Pair(F, 2.0))
    weightMap[C] = mapOf(Pair(D, 2.0), Pair(E, 1.0), Pair(F, 3.0))
    for (worker in workers) {
        graph.addEdge(FlowEdge(Edge(source, worker), 1))
        for (task in tasks) {
            graph.addEdge(FlowEdge(Edge(worker, task, weightMap[worker]!![task]!!), 1))
        }
    }

    for (task in tasks) {
        graph.addEdge(FlowEdge(Edge(task, sink), 1))

    }
    return graph
}

fun bipartiteGraph(): FlowGraphOfChar {
    val graph = FlowGraphOfChar(source, sink)
    val workers = listOf(A, B, C)
    val tasks = listOf(D, E, F)
    for (worker in workers) {
        graph.addEdge(FlowEdge(Edge(source, worker), 1))
        for (task in tasks) {
            graph.addEdge(FlowEdge(Edge(worker, task), 1))
        }
    }

    for (task in tasks) {
        graph.addEdge(FlowEdge(Edge(task, sink), 1))
    }

    return graph
}

class FlowGraphOfChar(source: Char, sink: Char) : FlowGraph<Char, FlowEdge<Char>>(source, sink)

class FlowGraphTest {

    @Test
    fun `Given a graph with only one source and one sink and one edge, graph is valid`() {
        val graph = FlowGraph(source, sink)
        graph.addEdge(FlowEdge(Edge(sink, source), 1))
        assertTrue(graph.hasValidFlows())
    }

    @Test
    fun `When constructing a flow edge, passing a flow greater than capacity throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            FlowEdge(Edge(A, B), 1, 2)
        }
    }

    @Test
    fun `Given a flow edge, setting a flow greater than capacity throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            val edge = FlowEdge(Edge(A, B), 1, 0)
            edge.flow = 2
        }
    }
}