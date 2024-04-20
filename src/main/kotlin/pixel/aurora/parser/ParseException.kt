package pixel.aurora.parser

import pixel.aurora.tokenizer.Tokenizer
import java.net.URI
import java.nio.CharBuffer

open class BaseParseException(message: String, val uri: URI) :
    RuntimeException(message)

class TokenizerParseException(message: String, val buffer: CharBuffer, uri: URI, val tokenizer: Tokenizer) :
    BaseParseException(message, uri)
