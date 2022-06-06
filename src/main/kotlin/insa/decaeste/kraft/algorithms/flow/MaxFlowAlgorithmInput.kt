package insa.decaeste.kraft.algorithms.flow

import insa.decaeste.kraft.graph.FlowEdge
import insa.decaeste.kraft.graph.FlowGraph

class MaxFlowAlgorithmInput<N, E : FlowEdge<N>>(val graph: FlowGraph<N, E>)