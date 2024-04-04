package pixel.aurora.parser

import java.net.URI
import java.nio.CharBuffer
import java.util.*

class ParseException(message: String, val buffer: CharBuffer, val uri: URI, val parser: Parser<*>) : RuntimeException(message)
object ParserMessages {

    const val invalidSyntax = "Invalid syntax"
    const val invalidCharacter = "Invalid character"
    fun expect(content: String) = "Expect: '$content'"

}

object Parsers {

    val defaultURI: URI = URI.create("about:blank")

    fun <T : Any> gotoWhenError(buffer: CharBuffer, position: Int = buffer.position(), block: () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (error: Throwable) {
            buffer.position(position)
            Result.failure(error)
        }
    }

}

abstract class Parser <T> {

    private var uri: URI = Parsers.defaultURI
    private var buffer: CharBuffer? = null

    fun getURI() = uri
    open fun setURI(uri: URI) { this.uri = uri }

    fun getBuffer(): CharBuffer = getBufferOrNull()!!
    open fun getBufferOrNull() = buffer
    open fun setBuffer(buffer: CharBuffer?) { this.buffer = buffer }

    open fun makeError(message: String) = ParseException(message, getBuffer(), getURI(), this)

    open fun resetParserState() {
        buffer = null
        uri = Parsers.defaultURI
    }

    fun <R> include(parser: Parser<R>): R {
        parser.setURI(getURI())
        parser.setBuffer(getBufferOrNull())
        val result = parser.parse()
        parser.resetParserState()
        return result
    }

    @Throws(ParseException::class)
    abstract fun parse(): T

}

