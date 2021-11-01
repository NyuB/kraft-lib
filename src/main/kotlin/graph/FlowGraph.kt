package graph

class FlowGraph<N, E : FlowEdge<N>> : Graph<N, E>() {
    fun hasValidCapacities() : Boolean {
        for(node in nodeSet()){
            val inCap = getInEdgesTo(node).sumOf { it.capacity }
            val outCap = getOutEdgesFrom(node).sumOf { it.capacity }
            if(inCap != outCap){
                return false
            }
         }
        return true
    }
}