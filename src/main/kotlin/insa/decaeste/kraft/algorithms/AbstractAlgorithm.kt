package insa.decaeste.kraft.algorithms

abstract class AbstractAlgorithm<I, T>(val input: I) : StepableComputation<I, T> {

    private var ended = false
    private var result = ComputationResult<T>(null)
    abstract override fun step(): AbstractAlgorithm<I, T>

    init {
        this.validateInputAndRaiseException()
    }

    abstract fun validateInputAndRaiseException()
    protected fun end(result: T?) {
        this.result = ComputationResult(result)
        this.ended = true
    }

    protected fun fail() {
        this.end(null)
    }

    override fun isRunning() = !ended

    override fun isEnded() = ended

    override fun computeUntilEnd(): AbstractAlgorithm<I, T> {
        while (isRunning()) {
            step()
        }
        return this
    }

    override fun getResult(): ComputationResult<T> = result
}