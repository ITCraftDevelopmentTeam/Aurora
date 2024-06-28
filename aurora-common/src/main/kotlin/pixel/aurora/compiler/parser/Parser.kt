package pixel.aurora.compiler.parser

import pixel.aurora.compiler.tokenizer.Token
import pixel.aurora.compiler.tokenizer.TokenBuffer
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tokenizer.isRaw
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

    fun Token.expect(vararg types: TokenType) = this.also {
        if (this.getTokenType() !in types)
            throw makeError("Invalid token type: ${this.getTokenType()}, expect [${types.joinToString(separator = ", ")}]")
    }

    fun Token.expect(raw: String) = this.also {
        if (!this.isRaw(raw))
            throw makeError("Invalid token '${this.getRaw()}', did you mean '$raw'?")
    }

    fun Token.expectIdentifier(identifier: String) = expect(TokenType.IDENTIFIER).expect(identifier)
    fun Token.expectPunctuation(character: Char) = expect(TokenType.PUNCTUATION).expect(character.toString())

}
