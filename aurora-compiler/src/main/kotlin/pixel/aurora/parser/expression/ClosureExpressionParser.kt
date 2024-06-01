package pixel.aurora.parser.expression

import pixel.aurora.parser.*
import pixel.aurora.parser.other.ParameterParser
import pixel.aurora.parser.util.ListParser
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.ClosureExpression

class ClosureExpressionParser : Parser<ClosureExpression>() {

    override fun parse(): ClosureExpression {
        buffer.get().expect("{").expect(TokenType.PUNCTUATION)
        val parameters = include(
            parser {
                val list = include(
                    ListParser(ParameterParser(), prefix = null, suffix = null)
                )
                buffer.get().expect("-").expect(TokenType.PUNCTUATION)
                buffer.get().expect(">").expect(TokenType.PUNCTUATION)
                list
            }.optional()
        ).getOrNull() ?: emptyList()
        val body = include(ListParser(StatementParser(), null, null, separator = null))
        buffer.get().expect("}").expect(TokenType.PUNCTUATION)
        return ClosureExpression(parameters, body)
    }

}