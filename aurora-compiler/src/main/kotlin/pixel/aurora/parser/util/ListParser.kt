package pixel.aurora.parser.util

import pixel.aurora.parser.*
import pixel.aurora.tokenizer.TokenType

class ListParser<T : Any>(val element: Parser<T>, val prefix: Char? = '(', val suffix: Char? = ')', val separator: Char? = ',') : Parser<List<T>>() {

    fun part() = parser {
        if (separator != null)
            buffer.get().expect(separator.toString()).expect(TokenType.PUNCTUATION)
        return@parser include(element)
    }

    override fun parse(): List<T> {
        if (prefix != null)
            buffer.get().expect(prefix.toString()).expect(TokenType.PUNCTUATION)
        val arguments = mutableListOf<T>()
        val first = include(element.optional()).getOrNull()
        if (first != null) {
            arguments += first
            while (true) {
                val result = include(part().optional()).getOrNull()
                if (result == null) break
                else arguments += result
            }
        }
        if (suffix != null)
            buffer.get().expect(suffix.toString()).expect(TokenType.PUNCTUATION)
        return arguments
    }

}