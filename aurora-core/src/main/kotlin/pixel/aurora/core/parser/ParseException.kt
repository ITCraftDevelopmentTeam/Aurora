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

class ParserException(
    private val inputMessage: String?,
    val buffer: TokenBuffer,
    val parser: Parser<*>,
    val state: Parser.State,
    private val inputCause: Throwable? = null
) :
    BaseParseException(inputMessage, state.uri, cause = inputCause) {

    private val tokens = buffer.tokens()

    override val message: String = "An error occurred while parsing ${parser.getName()}"
    override val cause: Throwable
        get() = BaseParseException(inputMessage, uri, inputCause)

}
