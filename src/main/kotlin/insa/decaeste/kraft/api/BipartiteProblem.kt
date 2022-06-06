package insa.decaeste.kraft.api

class BipartiteProblem<Worker, Job>(
    val workers: Set<Worker>,
    val jobs: Set<Job>,
    val weights: Map<Worker, Map<Job, Double>>
) {
    init {
        for(worker in workers) {
            val workerWeights = weights[worker]
            if(workerWeights == null) {
                throw IllegalArgumentException("Problem structure is incorrect, $worker weights map is missing")
            } else {
                for(job in jobs) {
                    if(job !in workerWeights) {
                        throw IllegalArgumentException("Problem structure is incorrect, job $job is missing in $worker weight map")
                    }
                }
            }
        }
    }
}