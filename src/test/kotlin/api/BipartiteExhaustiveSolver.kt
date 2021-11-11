package api

import computation.AbstractAlgorithm
import java.util.*
import kotlin.collections.ArrayList

class BipartiteExhaustiveSolver<Worker, Job>(input: BipartiteProblem<Worker, Job>) :
    AbstractAlgorithm<BipartiteProblem<Worker, Job>, BipartiteSolution<Worker, Job>>(input),
    BipartiteSolver<Worker, Job>{

    inner class SearchItem(
        val workerToMap: Set<Worker>,
        val jobsAvailable: Set<Job>,
        val mapping: HashMap<Worker, Job> = HashMap(),
        val cost: Double = 0.0
    )

    inner class ItemComparator() : Comparator<SearchItem> {
        override fun compare(left: SearchItem?, right: SearchItem?): Int {
            if (left == null || right == null) return -1
            return left.cost.compareTo(right.cost)
        }
    }

    private val queue: PriorityQueue<SearchItem> = PriorityQueue(ItemComparator())

    init {
        queue.add(SearchItem(input.workers, input.jobs))
    }

    override fun step(): AbstractAlgorithm<BipartiteProblem<Worker, Job>, BipartiteSolution<Worker, Job>> {
        if (queue.isEmpty()) {
            fail()
        }
        else {
            val item = queue.poll()
            if (item.workerToMap.isEmpty()) {
                end(BipartiteSolution(item.mapping, item.cost))
            }
            else {
                queue.addAll(listNextForkedItems(item))
            }
        }
        return this
    }

    private fun listNextForkedItems(item : SearchItem) : List<SearchItem> {
        val res = ArrayList<SearchItem>()
        val worker = item.workerToMap.first()
        val nextWorkers = item.workerToMap.without(worker)
        item.jobsAvailable.forEach { job ->
            val nextJobs = item.jobsAvailable.without(job)
            val nextItem = SearchItem(
                nextWorkers,
                nextJobs,
                HashMap(item.mapping),
                item.cost + input.weights[worker]!![job]!!
            )
            nextItem.mapping[worker] = job
            res.add(nextItem)
        }
        return res
    }

    private fun <T> Set<T>.without(item : T) : Set<T> {
        return this.filterNot { it == item }.toSet()
    }

    override fun validateInputAndRaiseException() {
        val (ws, js) = Pair(input.workers.size, input.jobs.size)
        if (ws != js) {
            throw IllegalArgumentException("There should be as many worker as job, found $ws workers and $ws jobs")
        }
    }
}