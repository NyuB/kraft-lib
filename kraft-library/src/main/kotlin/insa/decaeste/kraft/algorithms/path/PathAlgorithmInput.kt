package insa.decaeste.kraft.algorithms.path

import insa.decaeste.kraft.graph.Edge
import insa.decaeste.kraft.graph.Graph

class PathAlgorithmInput<N, E : Edge<N>>(val graph: Graph<N, E>, val origin: N, val destination: N)