package pixel.aurora.core.tokenizer

import pixel.aurora.core.parser.TokenizerException
import java.net.URI
import java.nio.CharBuffer

interface Tokenizer {

    fun getBuffer(): CharBuffer
    fun getUri(): URI

    fun reset() = this.also {
        getBuffer().position(0)
    }

    fun makeError(message: String) = TokenizerException(message, getBuffer(), getUri(), this)

    fun tokenize(): Sequence<Token>

}