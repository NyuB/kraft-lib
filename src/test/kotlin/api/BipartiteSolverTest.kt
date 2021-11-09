package api

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BipartiteSolverTest {

    fun problem01(): BipartiteProblem<String, String> {
        val workers = setOf("Alice", "Bob", "Carl")
        val jobs = setOf("C", "B", "A")
        val weights = mapOf(
            Pair(
                "Alice", mapOf(
                    Pair("A", 1.0),
                    Pair("B", 2.0),
                    Pair("C", 3.0),
                )
            ),
            Pair(
                "Bob", mapOf(
                    Pair("A", 2.0),
                    Pair("B", 1.0),
                    Pair("C", 3.0),
                )
            ),
            Pair(
                "Carl", mapOf(
                    Pair("A", 3.0),
                    Pair("B", 2.0),
                    Pair("C", 1.0),
                )
            )
        )
        val problem = BipartiteProblem(workers, jobs, weights)
        return problem
    }

    fun <A, B> Map<A, B>.containsCorrect(key: A, value: B): Boolean {
        return this.containsKey(key) && this[key] == value
    }

    @Test
    fun `Given 3 workers and 3 jobs where each worker has , each worker should be link to its less weighted job`() {
        val res = BipartiteFlowAlgorithm(problem01()).computeUntilEnd().getResult()
        assertTrue(res.isSuccess())
        res.ifSuccessCompute { mapResult ->
            mapResult.entries.forEach {
                println("${it.key} -> ${it.value}")
            }
            assertTrue(mapResult.containsCorrect("Alice", "A"))
            assertTrue(mapResult.containsCorrect("Bob", "B"))
            assertTrue(mapResult.containsCorrect("Carl", "C"))
        }
    }

}