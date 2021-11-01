package graph

class FlowEdge<N>(edge : Edge<N>, val capacity : Int, var flow : Int) : Edge<N>(edge.origin, edge.destination, edge.weight)