package pixel.aurora.compiler.tokenizer

import java.nio.BufferUnderflowException
import java.util.*

class TokenBuffer(private val tokenizer: Tokenizer) : Iterable<Token> {

    private val tokens: List<Token> = Collections.unmodifiableList(tokenizer.reset().tokenize().toList())
    private var position: Int = 0

    fun tokens() = tokens.slice(position until tokens.size)

    override fun iterator() = tokens.iterator()

    fun getTokenizer() = tokenizer
    fun getTokens() = tokens

    fun get() = get(position++)
    operator fun get(position: Int) = tokens.getOrNull(position) ?: throw BufferUnderflowException()

    fun position() = position
    fun position(position: Int) = this.also { this.position = position }

    fun next() = next(1)
    fun next(offset: Int) = position(position() + offset)
    fun hasNext() = position < tokens.size

}