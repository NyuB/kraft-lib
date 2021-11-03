package computation.flow

import graph.FlowEdge
import graph.FlowGraph

class MaxFlowAlgorithmInput<N, E : FlowEdge<N>>(val graph: FlowGraph<N, E>)