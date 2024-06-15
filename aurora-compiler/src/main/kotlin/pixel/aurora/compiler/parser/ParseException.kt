@file:Suppress("CanBeParameter")

package pixel.aurora.compiler.parser

import pixel.aurora.compiler.tokenizer.TokenBuffer
import pixel.aurora.compiler.tokenizer.Tokenizer
import pixel.aurora.compiler.tree.Node
import pixel.aurora.compiler.validator.Validator
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

class InvalidNodeException(val validator: Validator<*>, val node: Node? = null, message: String? = null, cause: Throwable? = null) :
    BaseParseException(message, validator.getState().uri, cause = cause)
