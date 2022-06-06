package insa.decaeste.kraft.algorithms.path

import insa.decaeste.kraft.graph.Edge

fun interface PathAlgorithmFactory<N, E : Edge<N>> {

    fun createAlgorithm(input: PathAlgorithmInput<N, E>): PathAlgorithm<N, E>
}