@file:Suppress("CanBeParameter")

package pixel.aurora.core.parser

import pixel.aurora.core.tokenizer.TokenBuffer
import pixel.aurora.core.tokenizer.Tokenizer
import java.net.URI
import java.nio.CharBuffer

open class BaseParseException(message: String?, val uri: URI, cause: Throwable? = null) :
    RuntimeException(message, cause)

class TokenizerException(
    message: String?,
    val buffer: CharBuffer,
    uri: URI,
    val tokenizer: Tokenizer,
    cause: Throwable? = null
) :
    BaseParseException(message, uri, cause = cause)

class ParserException(message: String?, val buffer: TokenBuffer, val state: Parser.State, cause: Throwable? = null) :
    BaseParseException(message, state.uri, cause = cause) {

    private val tokens = buffer.tokens()

}
