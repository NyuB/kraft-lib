package insa.decaeste.kraft.graph

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GraphTest {

    private fun <N> constructEmptyGraph() = Graph<N, Edge<N>>()

    private fun <N> withEmptyGraph(actions: Graph<N, Edge<N>>.() -> Unit) =
        withGraph({ constructEmptyGraph<N>() }, actions)

    private fun withEmptyIntGraph(actions: Graph<Int, Edge<Int>>.() -> Unit) = withEmptyGraph(actions)

    private fun <N> withGraph(initializer: () -> Graph<N, Edge<N>>, actions: Graph<N, Edge<N>>.() -> Unit) {
        initializer().actions()
    }


    @Test
    fun `Initial size is zero`() = withEmptyIntGraph {
        assertEquals(0, numberOfNodes())
        assertEquals(0, numberOfEdges())
        assertEquals(numberOfNodes(), nodeSet().size)
        assertEquals(numberOfEdges(), getAllEdges().size)
    }

    @Test
    fun `Adding one node increases number of nodes by one`() = withEmptyIntGraph {
        addNode(0)
        assertEquals(1, numberOfNodes())
    }

    @Test
    fun `Graph containsNode() return true for one added node`() = withEmptyIntGraph {
        addNode(0)
        assertTrue(containsNode(0))
    }

    @Test
    fun `Given an empty graph, adding one edge increases the number of edges by one`() = withEmptyIntGraph {
        addEdge(Edge(0, 1))
        assertEquals(1, numberOfEdges())
    }

    @Test
    fun `Given an empty graph, adding one edge creates both missing nodes`() = withEmptyIntGraph {
        addEdge(Edge(0, 1))
        assertEquals(2, numberOfNodes())
        assertTrue(containsNode(0))
        assertTrue(containsNode(1))
    }

    @Test
    fun `Given an empty graph, calling addNode() twice with the same node add only one node during the first call`() =
        withEmptyIntGraph {
            addNode(0)
            assertEquals(1, numberOfNodes())
            addNode(0)
            assertEquals(1, numberOfNodes())
        }


    @Test
    fun `Data class node identity behavior`() {
        data class TestDataClass(val number: Int)

        withEmptyGraph<TestDataClass> {
            val a = TestDataClass(0)
            val b = TestDataClass(1)
            val aa = TestDataClass(0)
            addNode(a)
            addNode(b)
            assertEquals(2, numberOfNodes())
            addNode(aa)
            assertEquals(2, numberOfNodes())
            assertTrue(containsNode(a))
            assertTrue(containsNode(aa))
        }
    }

    @Test
    fun `Regular classes node identity behavior`() {
        class TestClass(val number: Int)
        withEmptyGraph<TestClass> {

            val a = TestClass(0)
            val b = TestClass(1)
            val aa = TestClass(0)
            addNode(a)
            addNode(b)
            assertEquals(2, numberOfNodes())
            addNode(aa)
            assertEquals(3, numberOfNodes())
            assertTrue(containsNode(a))
            assertTrue(containsNode(aa))
        }
    }
}