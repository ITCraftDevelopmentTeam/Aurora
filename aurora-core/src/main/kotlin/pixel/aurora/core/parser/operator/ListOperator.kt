package pixel.aurora.core.parser.operator

import pixel.aurora.core.parser.Parser
import pixel.aurora.core.parser.buffer
import pixel.aurora.core.parser.include
import pixel.aurora.core.parser.rule
import pixel.aurora.core.tokenizer.expectPunctuation

class ListOperator<T : Any>(
    val element: Parser<T>,
    val prefix: String? = "(",
    val suffix: String? = ")",
    val separator: String? = ",",
    val allowSeparatorEnd: Boolean = false
) : Parser<List<T>>() {

    fun part() = rule {
        if (separator != null) {
            for (character in separator) buffer.get().expectPunctuation(character)
        }
        return@rule include(element)
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
        if (allowSeparatorEnd && separator != null) {
            include(
                rule {
                    for (character in separator) buffer.get().expectPunctuation(character)
                }.optional()
            )
        }
        if (suffix != null) {
            for (character in suffix) buffer.get().expectPunctuation(character)
        }
        return arguments
    }

}


fun <T : Any> Parser<T>.list(
    prefix: String? = "(",
    suffix: String? = ")",
    separator: String? = ",",
    allowSeparatorEnd: Boolean = false
) = ListOperator(this, prefix, suffix, separator, allowSeparatorEnd)

fun <T : Any, R : Any> Parser<out Iterable<T>>.mapEach(block: (T) -> R) = rule {
    include(this@mapEach).map(block)
}

fun <T : Any, R : Any> Parser<out Iterable<T>>.mapEachIndexed(block: (index: Int, T) -> R) = rule {
    include(this@mapEachIndexed).mapIndexed(block)
}
