package graph

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FlowGraphTest {
    val sink = 'T'
    val source = 'S'

    val A = 'A'
    val B = 'B'
    val O = 'O'
    val P = 'P'
    val Q = 'Q'
    val R = 'R'
    val S = source
    val T = sink

    //https://fr.wikipedia.org/wiki/Probl%C3%A8me_de_flot_maximum#/media/Fichier:Max_flow.svg
    fun wikiGraph(): FlowGraph<Char, FlowEdge<Char>> {
        val graph = FlowGraph(source, sink)
        graph.addEdge(FlowEdge(Edge(source, O), 3))
        graph.addEdge(FlowEdge(Edge(source, P), 3))
        graph.addEdge(FlowEdge(Edge(O, Q), 3))
        graph.addEdge(FlowEdge(Edge(P, R), 2))
        graph.addEdge(FlowEdge(Edge(Q, T), 2))
        graph.addEdge(FlowEdge(Edge(Q, R), 4))
        graph.addEdge(FlowEdge(Edge(R, T), 3))
        return graph
    }


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