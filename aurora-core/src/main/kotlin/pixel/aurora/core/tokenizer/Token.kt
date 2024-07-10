package pixel.aurora.core.tokenizer

import java.math.BigDecimal

interface Token {
    fun getRaw(): String
}

inline fun <reified T : Token> Token.isToken(raw: String? = null) =
    (raw ?: getRaw()) == getRaw() && T::class.isInstance(this)

open class UnknownToken(private val raw: String) : Token {

    override fun getRaw() = raw

    override fun toString() = "Token(${getRaw()})"

}

class StringToken(raw: String, private val parsed: String) : UnknownToken(raw) {

    fun getString() = parsed
    override fun toString() = "${super.toString()}{ $parsed }"

    operator fun unaryPlus() = parsed

}

class NumericToken(raw: String, private val parsed: BigDecimal) : UnknownToken(raw) {

    fun getNumber() = parsed
    override fun toString() = "${super.toString()}{ $parsed }"

    operator fun unaryPlus() = parsed

}

class PunctuationToken(private val raw: Char) : UnknownToken(raw.toString()) {

    fun getPunctuation() = raw

    operator fun unaryPlus() = raw

}

class WhitespaceToken(raw: String) : UnknownToken(raw) {

    operator fun unaryPlus() = getRaw()

}

class NullToken : UnknownToken("null") {

    operator fun unaryPlus() = null

}

class BooleanToken(raw: String, private val parsed: Boolean = raw == "true") : UnknownToken(raw) {

    fun getBoolean(): Boolean = parsed


    operator fun unaryPlus() = parsed
    operator fun unaryMinus() = BooleanToken((!parsed).toString(), !parsed)

}

class IdentifierToken(raw: String, private val parsed: String, private val isStringify: Boolean = false) :
    UnknownToken(raw) {

    fun getName() = parsed
    fun isStringify() = isStringify

    operator fun unaryPlus() = parsed

}
