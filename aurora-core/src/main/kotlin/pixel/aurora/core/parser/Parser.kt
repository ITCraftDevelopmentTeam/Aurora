package pixel.aurora.core.parser

import pixel.aurora.core.tokenizer.*
import java.net.URI

abstract class Parser<T : Any> {

    data class State(var uri: URI, var buffer: TokenBuffer, var parent: Parser<*>? = null, var isMarked: Boolean = false)

    private var state: State? = null
    open fun setState(state: State?) = this.also { this.state = state }
    open fun getStateOrNull(): State? = state
    open fun getState() = getStateOrNull()!!
    open fun mark(isMarked: Boolean) {
        setState(getStateOrNull()?.copy(isMarked = isMarked))
        getStateOrNull()?.parent?.mark(isMarked)
    }
    open fun mark() = getStateOrNull()?.isMarked == true

    abstract fun parse(): T

    open fun makeError(message: String? = null, cause: Throwable? = null) = getState().let {
        ParserException(message, it.buffer, it, cause = cause)
    }

    inline fun <reified T> Token.expect() = this.also {
        if (!T::class.isInstance(this))
            throw makeError("Invalid token type: ${this::class}, expect ${T::class}")
    } as T

    fun <T : Token> T.expect(raw: String) = this.also {
        if (!this.isToken<Token>(raw)) throw makeError("Invalid token '${this.getRaw()}', did you mean '$raw'?")
    }

    fun <T : Token> T.expectIdentifier(identifier: String) = expect<IdentifierToken>().expect(identifier)
    fun <T : Token> T.expectPunctuation(character: Char) = expect<PunctuationToken>().expect(character.toString())

}
