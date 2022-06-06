package insa.decaeste.kraft.graph

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EdgePathTest {
    val a = 'a'
    val b = 'b'
    val c = 'c'
    val d = 'd'
    val e = 'e'
    val f = 'f'
    val ab = Edge(a, b)
    val bc = Edge(b, c)
    val cd = Edge(c, d)
    val de = Edge(d, e)
    val ef = Edge(e, f)
    val aa = Edge(a, a)

    @Test
    fun `Given a void path, it is valid if origin == destination`() {
        val path = EdgePath(a, a, emptyList())
        assertTrue(path.isValid)
    }

    @Test
    fun `Given a void path, it is invalid if origin != destination`() {
        val path = EdgePath(a, b, emptyList())
        assertFalse(path.isValid)
    }

    @Test
    fun `Given a single edge path A-B, origin = A and destination = B, it is valid`() {
        val path = EdgePath(a, b, listOf(ab))
        assertTrue(path.isValid)
    }

    @Test
    fun `Given a path where the path is not the last edge destination it is invalid`() {
        val path = EdgePath(a, c, listOf(ab))
        assertFalse(path.isValid)
    }

    @Test
    fun `Path invalid if it is not continuous`() {
        val path = EdgePath(a, d, listOf(ab, cd))
        assertFalse(path.isValid)
    }

}