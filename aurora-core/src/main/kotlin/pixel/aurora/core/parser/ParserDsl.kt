package pixel.aurora.core.parser

import pixel.aurora.core.Aurora
import pixel.aurora.core.parser.operator.ChoiceOperator
import pixel.aurora.core.parser.operator.OptionalOperator
import pixel.aurora.core.parser.operator.ListOperator
import pixel.aurora.core.tokenizer.TokenBuffer

val Parser<*>.buffer: TokenBuffer
    get() = this.getState().buffer

fun <T : Any> TokenBuffer.parse(parser: Parser<T>) = parser {
    include(parser)
}.apply {
    setState(Parser.State(Aurora.BLANK_URI, this@parse))
}.parse()

fun <R : Any> Parser<*>.include(parser: Parser<R>): R = includeWithState(parser).second

fun <R : Any> Parser<*>.includeWithState(parser: Parser<R>): Pair<Parser.State, R> {
    val origin = parser.getStateOrNull()
    parser.setState(this.getStateOrNull()?.copy(parent = this))
    val result = parser.parse()
    val resultState = parser.getState()
    parser.setState(origin)
    return resultState to result
}


fun <R : Any> Parser<R>.optional(rollback: Boolean = true) = OptionalOperator(this, rollback)

@Suppress("UNCHECKED_CAST")
infix fun <T : Any> Parser<out T>.or(other: Parser<out T>): ChoiceOperator<T> {
    return if (this is ChoiceOperator<*>) choose(*this.choices, other) as ChoiceOperator<T>
    else if (other is ChoiceOperator<*>) choose(this, *other.choices) as ChoiceOperator<T>
    else choose(this, other)
}

infix fun <T : Any> ChoiceOperator<T>.not(filter: (Parser<out T>) -> Boolean) =
    ChoiceOperator(*this.choices.filter(filter).toTypedArray())

inline fun <reified T : Any> ChoiceOperator<T>.not() = not {
    it !is T && !T::class.isInstance(it)
}

fun <T : Any> choose(vararg choices: Parser<out T>) = ChoiceOperator(*choices)

fun <T : Any> parser(block: Parser<T>.() -> T) = object : Parser<T>() {
    override fun parse() = block()
}

fun <T : Any, R : Any> Parser<T>.map(block: (T) -> R) = parser {
    block(include(this@map))
}

fun <T : Any, R : Any> Parser<out Iterable<T>>.mapEach(block: (T) -> R) = parser {
    include(this@mapEach).map(block)
}

fun <T : Any, R : Any> Parser<out Iterable<T>>.mapEachIndexed(block: (index: Int, T) -> R) = parser {
    include(this@mapEachIndexed).mapIndexed(block)
}

fun <T : Any> Parser<Result<T>>.orElse(block: (Throwable) -> T) = parser {
    include(this@orElse).getOrElse(block)
}

fun <T : Any> Parser<Result<T>>.orElse(parser: Parser<T>) = parser {
    include(this@orElse).getOrElse {
        include(parser)
    }
}

fun <T : Any> Parser<T>.list(prefix: String? = "(", suffix: String? = ")", separator: String? = ",", allowSeparatorEnd: Boolean = false) = ListOperator(this, prefix, suffix, separator, allowSeparatorEnd)
