package pixel.aurora.parser

import pixel.aurora.tokenizer.Token
import pixel.aurora.tokenizer.TokenBuffer
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tokenizer.isRaw
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

}


fun <R : Any> Parser<*>.include(parser: Parser<R>): R {
    val origin = parser.getStateOrNull()
    parser.setState(this.getStateOrNull())
    val result = parser.parse()
    parser.setState(origin)
    return result
}

val Parser<*>.buffer: TokenBuffer
    get() = this.getState().buffer

fun <R : Any> Parser<R>.optional(rollback: Boolean = true) = object : Parser<Result<R>>() {

    override fun parse(): Result<R> {
        val parser = this@optional
        val position = buffer.position()
        try {
            return Result.success(include(parser))
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

class ChoiceParser<T : Any>(vararg val choices: Parser<out T>) : Parser<T>() {

    override fun parse(): T {
        val exceptions = mutableListOf<Throwable>()
        for (choice in choices) {
            val result = include(choice.optional())
            if (result.isSuccess) return result.getOrNull() ?: throw makeError("Invalid syntax")
            else exceptions += result.exceptionOrNull()!!
        }
        throw makeError("Invalid syntax", cause = exceptions.lastOrNull())
    }

}

fun <T : Any> choose(vararg choices: Parser<out T>) = ChoiceParser(*choices)

fun <T : Any> parser(block: Parser<T>.() -> T) = object : Parser<T>() {
    override fun parse() = block()
}
