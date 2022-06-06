package insa.decaeste.kraft.api

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class BipartiteProblemTest {
    @Test
    fun `Given incomplete inputs where a worker weight map is missing a job entry, when constructing the problem, should throw`() {
        assertThrows<IllegalArgumentException> {
            BipartiteProblem(setOf("WorkerA"), setOf("JobA","JobB"), mapOf("WorkerA" to mapOf("JobA" to 1.0)))
        }
    }
}