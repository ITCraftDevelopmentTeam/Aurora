package pixel.aurora.core.parser

import pixel.aurora.core.Aurora
import pixel.aurora.core.tokenizer.TokenBuffer

var Parser<*>.state: Parser.State
    get() = getState()
    set(value) = Unit.also { setState(value) }

var Parser<*>.buffer: TokenBuffer
    get() = state.buffer
    set(value) = Unit.also { setState(getState().copy(buffer = value)) }

fun <T : Any> TokenBuffer.parse(parser: Parser<T>) = rule {
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

fun <T : Any> rule(name: String? = null, block: Parser<T>.() -> T) = ParserSequence(name, block)

fun <T : Any, R : Any> Parser<T>.map(block: (T) -> R) = rule {
    block(include(this@map))
}
