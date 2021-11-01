package computation

import graph.Edge
import graph.Graph

class PathAlgorithmInput<N, E : Edge<N>>(val graph : Graph<N, E>, val origin : N, val destination : N)