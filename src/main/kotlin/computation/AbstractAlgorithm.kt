package computation

abstract class AbstractAlgorithm<I, T>(val input: I) {
    private var ended = false
    private var result = AlgorithmResult<T>(null)
    abstract fun step(): AbstractAlgorithm<I, T>

    init {
        this.validateInputAndRaiseException()
    }

    abstract fun validateInputAndRaiseException()
    protected fun end(result: T?) {
        this.result = AlgorithmResult(result)
        this.ended = true
    }

    protected fun fail() {
        this.end(null)
    }

    fun isRunning() = !ended

    fun isEnded() = ended

    fun computeUntilEnd(): AbstractAlgorithm<I, T> {
        while (isRunning()) {
            step()
        }
        return this
    }

    fun getResult(): AlgorithmResult<T> = result
}