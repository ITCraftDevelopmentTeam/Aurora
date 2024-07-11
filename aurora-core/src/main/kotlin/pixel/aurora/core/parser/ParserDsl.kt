package pixel.aurora.core.parser

import pixel.aurora.core.Aurora
import pixel.aurora.core.tokenizer.TokenBuffer

var AbstractParser<*>.state: AbstractParser.State
    get() = getState()
    set(value) = Unit.also { setState(value) }

var AbstractParser<*>.buffer: TokenBuffer
    get() = state.buffer
    set(value) = Unit.also { setState(getState().copy(buffer = value)) }

fun <T : Any> TokenBuffer.parse(parser: AbstractParser<T>) = rule {
    include(parser)
}.apply {
    setState(AbstractParser.State(Aurora.BLANK_URI, this@parse))
}.parse()

fun <R : Any> AbstractParser<*>.include(parser: AbstractParser<R>): R = includeWithState(parser).second

fun <R : Any> AbstractParser<*>.includeWithState(parser: AbstractParser<R>): Pair<AbstractParser.State, R> {
    val origin = parser.getStateOrNull()
    parser.setState(this.getStateOrNull()?.copy(parent = this))
    val result = parser.parse()
    val resultState = parser.getState()
    parser.setState(origin)
    return resultState to result
}

fun <T : Any> rule(name: String? = null, block: ParserSequence<T>.() -> T) = ParserSequence(name, block)

fun <T : Any, R : Any> AbstractParser<T>.map(block: (T) -> R) = rule {
    block(include(this@map))
}
