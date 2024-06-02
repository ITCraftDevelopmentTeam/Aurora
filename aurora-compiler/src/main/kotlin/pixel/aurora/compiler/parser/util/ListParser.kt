package pixel.aurora.compiler.parser.util

import pixel.aurora.compiler.parser.*

class ListParser<T : Any>(
    val element: Parser<T>,
    val prefix: String? = "(",
    val suffix: String? = ")",
    val separator: String? = ","
) : Parser<List<T>>() {

    fun part() = parser {
        if (separator != null) {
            for (character in separator) buffer.get().expectPunctuation(character)
        }
        return@parser include(element)
    }

    override fun parse(): List<T> {
        if (prefix != null) {
            for (character in prefix) buffer.get().expectPunctuation(character)
        }
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
        if (suffix != null) {
            for (character in suffix) buffer.get().expectPunctuation(character)
        }
        return arguments
    }

}