package pixel.aurora.core.tokenizer

import java.nio.BufferUnderflowException

class TokenBuffer(private val tokens: List<Token>) : Iterable<Token> {

    constructor(tokenizer: Tokenizer) : this(tokenizer.reset().tokenize().toList())

    private var position: Int = 0

    fun tokens() = tokens.slice(position until tokens.size)

    override fun iterator() = tokens.iterator()

    fun getTokens() = tokens

    fun get() = get(position++)
    operator fun get(position: Int) = tokens.getOrNull(position) ?: throw BufferUnderflowException()

    fun position() = position
    fun position(position: Int) = this.also { this.position = position }

    fun next() = next(1)
    fun next(offset: Int) = position(position() + offset)
    fun hasNext() = position < tokens.size

    override fun toString() = toList().toString()

}

fun Tokenizer.toTokenBuffer() = TokenBuffer(this)
