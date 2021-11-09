package computation

interface StepableComputation<I, T> {
    fun step(): StepableComputation<I, T>
    fun isRunning() : Boolean
    fun isEnded() : Boolean
    fun computeUntilEnd(): StepableComputation<I, T>
    fun getResult(): ComputationResult<T>
}