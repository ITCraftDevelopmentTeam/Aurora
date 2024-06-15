package pixel.aurora.compiler.validator

import java.net.URI

abstract class Validator <T : Any> {

    data class State(var uri: URI)

    private var state: State? = null
    open fun setState(state: State?) = this.also { this.state = state }
    open fun getStateOrNull(): State? = state
    open fun getState() = getStateOrNull()!!

    abstract fun validate(node: T)

    fun <T : Any> T.validate(input: Validator<T>): State {
        val origin = input.getStateOrNull()
        input.setState(input.getStateOrNull())
        input.validate(this)
        val resultState = this@Validator.getState()
        input.setState(origin)
        return resultState
    }

}
