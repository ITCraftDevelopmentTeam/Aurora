package pixel.aurora.parser.expression

import pixel.aurora.parser.Parser
import pixel.aurora.parser.buffer
import pixel.aurora.tokenizer.NumericToken
import pixel.aurora.tokenizer.StringToken
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tokenizer.isRaw
import pixel.aurora.tree.*

class LiteralExpressionParser : Parser<Literal<*>>() {

    override fun parse() = when (val literal = buffer.get()) {
        is StringToken -> StringLiteral(literal.getString())
        is NumericToken -> NumberLiteral(literal.getNumber())
        else -> when (literal.getTokenType()) {
            TokenType.BOOLEAN -> if (literal.isRaw("true")) BooleanLiteral.TRUE else BooleanLiteral.FALSE
            TokenType.NULL -> NullLiteral
            else -> throw makeError("Invalid syntax")
        }
    }

}