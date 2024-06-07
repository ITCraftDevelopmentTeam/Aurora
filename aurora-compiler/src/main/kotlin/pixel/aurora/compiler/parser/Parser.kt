package pixel.aurora.compiler.parser

import pixel.aurora.compiler.tokenizer.Token
import pixel.aurora.compiler.tokenizer.TokenBuffer
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tokenizer.isRaw
import java.net.URI

abstract class Parser<T : Any> {

    data class State(var uri: URI, var buffer: TokenBuffer)

    private var state: State? = null
    open fun setState(state: State?) = this.also { this.state = state }
    open fun getStateOrNull(): State? = state
    open fun getState() = getStateOrNull()!!

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


fun <R : Any> Parser<*>.include(parser: Parser<R>): R = includeWithState(parser).second

fun <R : Any> Parser<*>.includeWithState(parser: Parser<R>): Pair<Parser.State, R> {
    val origin = parser.getStateOrNull()
    parser.setState(this.getStateOrNull())
    val result = parser.parse()
    val resultState = parser.getState()
    parser.setState(origin)
    return resultState to result
}

val Parser<*>.buffer: TokenBuffer
    get() = this.getState().buffer

fun <R : Any> Parser<R>.optional(rollback: Boolean = true) = object : Parser<Result<R>>() {

    override fun parse(): Result<R> {
        val parser = this@optional
        val position = buffer.position()
        try {
            val result = Result.success(include(parser))
            return result
        } catch (throwable: Throwable) {
            if (rollback) {
                buffer.position(position)
            }
            return Result.failure(throwable)
        }
    }

}

@Suppress("UNCHECKED_CAST")
infix fun <T : Any> Parser<out T>.or(other: Parser<out T>): ChoiceParser<T> {
    return if (this is ChoiceParser<*>) choose(*this.choices, other) as ChoiceParser<T>
    else if (other is ChoiceParser<*>) choose(this, *other.choices) as ChoiceParser<T>
    else choose(this, other)
}

infix fun <T : Any> ChoiceParser<T>.not(filter: (Parser<out T>) -> Boolean) =
    ChoiceParser(*this.choices.filter(filter).toTypedArray())

class ChoiceParser<T : Any>(vararg val choices: Parser<out T>) : Parser<T>() {

    override fun parse(): T {
        var exception: Throwable? = null
        for (choice in choices) {
            val result = includeWithState(choice.optional())
            if (result.second.isSuccess) {
                return result.second.getOrNull() ?: throw makeError("Invalid syntax")
            }
            else exception = result.second.exceptionOrNull()!!
        }
        throw makeError("Invalid syntax", cause = exception)
    }

}

fun <T : Any> choose(vararg choices: Parser<out T>) = ChoiceParser(*choices)

fun <T : Any> parser(block: Parser<T>.() -> T) = object : Parser<T>() {
    override fun parse() = block()
}
