package insa.decaeste.kraft.graph

open class FlowGraph<N, E : FlowEdge<N>>(val source: N, val sink: N) : Graph<N, E>() {

    init {
        addNode(source)
        addNode(sink)
    }

    fun hasValidFlows(): Boolean {
        for (node in nodeSet().filter { it != source && it != sink }) {
            if (!balancedInOut(node)) {
                return false
            }
        }
        return balancedSourceAndSink()
    }

    private fun balancedInOut(node: N): Boolean {
        val inCap = getInEdgesTo(node).sumOf { it.flow }
        val outCap = getOutEdgesFrom(node).sumOf { it.flow }
        return inCap == outCap
    }

    private fun balancedSourceAndSink(): Boolean {
        val outSource = getOutEdgesFrom(source).sumOf { it.flow }
        val inSink = getInEdgesTo(sink).sumOf { it.flow }
        return outSource == inSink
    }

    fun flowToSink(): List<E> {
        return getInEdgesTo(sink)
    }

    fun totalFlowToSink(): Int {
        return flowToSink().sumOf { it.flow }
    }
}