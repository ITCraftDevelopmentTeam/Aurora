package pixel.aurora.parser

import java.nio.BufferUnderflowException

class CharactersOperation(private val min: Int = 0, private val max: Int = Int.MAX_VALUE, private val matcher: (Char) -> Boolean) : Parser<CharArray>() {

    override fun parse(): CharArray {
        val buffer = getBuffer()
        var current = 0
        val value = StringBuilder()
        var status: Boolean
        while (true) {
            if (current >= max) {
                status = true
                break
            }
            val startPosition = buffer.position()
            try {
                val char = buffer.get()
                if (matcher(char)) {
                    value.append(char)
                } else {
                    buffer.position(startPosition)
                    status = current + 1 > min
                    break
                }
            } catch (e: BufferUnderflowException) {
                status = current + 1 > min
                break
            }
            current += 1
        }
        if (!status) throw makeError(ParserMessages.invalidSyntax)
        return value.toString().toCharArray()
    }

}

class ChoiceOperator<T : Any>(private vararg val choices: Parser<out T>) : Parser<T>() {

    override fun parse(): T {
        for (i in choices) {
            try {
                val result = Parsers.gotoWhenError(getBuffer()) {
                    include(i)
                }
                if (result.isSuccess) return result.getOrThrow()
            } catch (_: Throwable) {}
        }
        throw makeError(ParserMessages.invalidSyntax)
    }

}

class KeywordOperator(private val name: String) : Parser<String>() {

    override fun parse(): String {
        val buffer = getBuffer()
        var current = ""
        for (i in name) {
            current += buffer.get()
            if (i != current.last()) {
                var invalid = current.substring(0..current.length - 2)
                if (invalid.isNotEmpty()) invalid = ": '$invalid'"
                throw makeError("Invalid keyword$invalid. Did you mean: '$name'?")
            }
        }
        return name
    }

}

class RepeatingOperator<T>(private val parser: Parser<T>, private val min: Int = 0, private val max: Int = Int.MAX_VALUE) : Parser<List<T>>() {

    override fun parse(): List<T> {
        val inputBuffer = getBuffer()
        val result = mutableListOf<T>()
        var occurrenceCount = 0
        if (min > 0) {
            for (i in 1..min) {
                val element = try {
                    include(parser)
                } catch (e: BufferUnderflowException) {
                    break
                }
                result.add(element)
                occurrenceCount++
            }
        }
        while (occurrenceCount < max && inputBuffer.position() < inputBuffer.capacity()) {
            val element = try {
                include(parser)
            } catch (e: BufferUnderflowException) {
                break
            }
            result.add(element)
            occurrenceCount++
        }
        if (occurrenceCount < min || occurrenceCount > max) {
            throw makeError("Expected $min to $max occurrences, but found ${occurrenceCount}.")
        }
        return result
    }

}

fun <T : Any> choice(vararg parsers: Parser<out T>) = ChoiceOperator(*parsers)
fun character(vararg char: Char) = CharactersOperation(1, 1) { it in char }
fun characters(min: Int = 1, max: Int = Integer.MAX_VALUE, matcher: (Char) -> Boolean) = CharactersOperation(min, max, matcher)
fun whitespace(min: Int = 0, max: Int = Int.MAX_VALUE) = CharactersOperation(min, max, Char::isWhitespace)
fun keyword(name: String) = KeywordOperator(name)
fun <T> repeat(parser: Parser<T>, min: Int = 0, max: Int = Int.MAX_VALUE) = RepeatingOperator(parser, min, max)
fun <T> parser(block: Parser<T>.() -> T): Parser<T> = object : Parser<T>() {
    override fun parse() = block()
}
fun semicolon(optional: Boolean = false) = parser {
    val characters = include(characters(min = if (optional) 0 else 1) { it == ';' || it.isWhitespace() })
    if (!optional && ';' !in characters) throw makeError(ParserMessages.invalidCharacter)
}