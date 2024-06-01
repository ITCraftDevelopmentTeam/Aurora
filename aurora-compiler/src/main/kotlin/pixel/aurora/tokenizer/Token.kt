package pixel.aurora.tokenizer

import java.math.BigDecimal

enum class TokenType {

    UNKNOWN, NULL, BOOLEAN, STRING, NUMERIC, IDENTIFIER, PUNCTUATION;

    fun token(raw: String) = UnknownToken(this, raw)

}


interface Token {
    fun getTokenType(): TokenType

    fun getRaw(): String

}

open class UnknownToken(private val tokenType: TokenType, private val raw: String) : Token {

    override fun getTokenType() = tokenType
    override fun getRaw() = raw

    override fun toString() = "Token(type=${getTokenType()}, raw=${getRaw()})"

}

class StringToken(raw: String, private val parsed: String) : UnknownToken(TokenType.STRING, raw) {

    fun getString() = parsed
    override fun toString() = "${super.toString()}{ $parsed }"

}

class NumericToken(raw: String, private val parsed: BigDecimal) : UnknownToken(TokenType.NUMERIC, raw) {

    fun getNumber() = parsed
    override fun toString() = "${super.toString()}{$parsed}"

}

object TokenHelper {

    const val PUNCTUATIONS = "+-*/<>,.;:?()[]{}!%|&="

}

fun Token.isRaw(raw: String) = getRaw() == raw
