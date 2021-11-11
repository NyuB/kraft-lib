package computation.path

import graph.Edge

fun interface PathAlgorithmFactory<N, E : Edge<N>> {

    fun createAlgorithm(input: PathAlgorithmInput<N, E>): PathAlgorithm<N, E>
}