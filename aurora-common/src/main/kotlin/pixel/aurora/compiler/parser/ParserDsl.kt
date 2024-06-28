package pixel.aurora.compiler.parser

import pixel.aurora.compiler.parser.operator.ChoiceOperator
import pixel.aurora.compiler.tokenizer.TokenBuffer

val Parser<*>.buffer: TokenBuffer
    get() = this.getState().buffer

fun <R : Any> Parser<*>.include(parser: Parser<R>): R = includeWithState(parser).second

fun <R : Any> Parser<*>.includeWithState(parser: Parser<R>): Pair<Parser.State, R> {
    val origin = parser.getStateOrNull()
    parser.setState(this.getStateOrNull()?.copy(parent = this))
    val result = parser.parse()
    val resultState = parser.getState()
    parser.setState(origin)
    return resultState to result
}


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
infix fun <T : Any> Parser<out T>.or(other: Parser<out T>): ChoiceOperator<T> {
    return if (this is ChoiceOperator<*>) choose(*this.choices, other) as ChoiceOperator<T>
    else if (other is ChoiceOperator<*>) choose(this, *other.choices) as ChoiceOperator<T>
    else choose(this, other)
}

infix fun <T : Any> ChoiceOperator<T>.not(filter: (Parser<out T>) -> Boolean) =
    ChoiceOperator(*this.choices.filter(filter).toTypedArray())

fun <T : Any> choose(vararg choices: Parser<out T>) = ChoiceOperator(*choices)

fun <T : Any> parser(block: Parser<T>.() -> T) = object : Parser<T>() {
    override fun parse() = block()
}

fun <T : Any, R : Any> Parser<T>.map(block: (T) -> R) = parser {
    block(include(this@map))
}

fun <T : Any, R : Any> Parser<out Collection<T>>.mapEach(block: (T) -> R) = parser {
    include(this@mapEach).map(block)
}

fun <T : Any> Parser<Result<T>>.orElse(block: (Throwable) -> T) = parser {
    include(this@orElse).getOrElse(block)
}
