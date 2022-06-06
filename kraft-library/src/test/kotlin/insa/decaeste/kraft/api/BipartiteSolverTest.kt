package insa.decaeste.kraft.api

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.test.assertEquals
typealias StringBipartiteSolverFactory = (BipartiteProblem<String,String>) -> BipartiteSolver<String,String>
class BipartiteSolverTest {

    private fun threeWorkersProblem(): BipartiteProblem<String, String> {
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
        return BipartiteProblem(workers, jobs, weights)
    }

    private fun pseudoRandomProblem(size : Int): BipartiteProblem<String, String> {
        val seed = 42L
        val random = Random(seed)
        val workers = Array(size) {i -> "W$i" }.toSet()
        val jobs = Array(size) {i -> "J$i"}.toSet()
        val weights = HashMap<String, HashMap<String, Double>>()
        workers.forEach { worker ->
            weights[worker] = HashMap()
            jobs.forEach { job ->
                weights[worker]!![job] = random.nextDouble()
            }
        }
        return BipartiteProblem(workers, jobs, weights)
    }

    private fun <A, B> Map<A, B>.containsCorrect(key: A, value: B): Boolean {
        return this.containsKey(key) && this[key] == value
    }

    private fun getFlowAlgorithm(problem : BipartiteProblem<String, String>) : BipartiteSolver<String, String> {
        return BipartiteFlowSolver(problem)
    }

    private fun getExhaustiveAlgorithm(problem : BipartiteProblem<String, String>) : BipartiteSolver<String, String> {
        return BipartiteExhaustiveSolver(problem)
    }

    @Test
    fun `Given 3 workers and 3 jobs where each worker has , each worker should be link to its less weighted job`() {
        for(factory in listOf<StringBipartiteSolverFactory>({ i->getExhaustiveAlgorithm(i)}, { i->getFlowAlgorithm(i)})){
            val res = factory(threeWorkersProblem()).computeUntilEnd().getResult()
            assertTrue(res.isSuccess())
            res.ifSuccessCompute { result ->
                val mapResult = result.matching
                assertTrue(mapResult.containsCorrect("Alice", "A"))
                assertTrue(mapResult.containsCorrect("Bob", "B"))
                assertTrue(mapResult.containsCorrect("Carl", "C"))
                assertEquals(3.0, result.totalCost)
            }
        }

    }

    @Test
    fun `Given pseudo-randomly assigned weights, the solution is minimal for all possible worker-job combination`() {
        val problem = pseudoRandomProblem(10)
        val res = BipartiteFlowSolver(problem).computeUntilEnd().getResult()
        val checkRes = getExhaustiveAlgorithm(problem).computeUntilEnd().getResult()
        assertTrue(res.isSuccess())
        assertTrue(checkRes.isSuccess())
        res.ifSuccessCompute {
            val cost = it.totalCost
            checkRes.ifSuccessCompute { check ->
                assertEquals(check.totalCost, cost, 0.001)
            }
        }
    }

    @Test
    fun `Given incomplete inputs where a worker weight map is missing a job entry, when constructing the problem, should throw`() {

    }

    private fun timedExec(exec : () -> Unit) : Long {
        val start = System.currentTimeMillis()
        exec()
        val end = System.currentTimeMillis()
        return end-start
    }

    @Test
    fun `Flow solver should be faster than exhaustive for input size 12`() {
        val problem = pseudoRandomProblem(12)
        val flowTime = timedExec {
            val res = BipartiteFlowSolver(problem).computeUntilEnd().getResult()
            assertTrue(res.isSuccess())
        }
        val exTime = timedExec {
            val res = BipartiteExhaustiveSolver(problem).computeUntilEnd().getResult()
            assertTrue(res.isSuccess())
        }
        assertTrue(flowTime <= exTime)
    }
}