package pixel.aurora.compiler.parser.expression

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.tokenizer.NumericToken
import pixel.aurora.compiler.tokenizer.StringToken
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tokenizer.isRaw
import pixel.aurora.compiler.tree.*

class LiteralExpressionParser : Parser<Literal<*>>() {

    override fun parse() = when (val literal = buffer.get()) {
        is StringToken -> StringLiteral(literal.getString())
        is NumericToken -> NumberLiteral(literal.getNumber())
        else -> when (literal.getTokenType()) {
            TokenType.BOOLEAN -> if (literal.isRaw("true")) BooleanLiteral.True() else BooleanLiteral.False()
            TokenType.NULL -> NullLiteral()
            else -> throw makeError("Invalid syntax")
        }
    }

}