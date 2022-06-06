package insa.decaeste.kraft.algorithms

class ComputationResult<T>(private val result: T?) {

    fun ifSuccessCompute(resultHandler: (T) -> Unit): IfSuccessElseBlock<T> {
        result?.let(resultHandler)
        return IfSuccessElseBlock(this)
    }

    fun isSuccess() = result != null

    fun isFailure() = result == null

    class IfSuccessElseBlock<T>(private val origin: ComputationResult<T>) {

        fun elseThen(failureHandler: () -> Unit) {
            if (origin.isFailure()) {
                failureHandler()
            }
        }
    }
}