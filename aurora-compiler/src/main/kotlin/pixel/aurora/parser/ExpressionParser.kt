package pixel.aurora.parser

import pixel.aurora.parser.expression.IdentifierExpressionParser
import pixel.aurora.parser.expression.LiteralExpressionParser
import pixel.aurora.parser.util.ListParser
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.CallExpression
import pixel.aurora.tree.Expression
import pixel.aurora.tree.Identifier
import pixel.aurora.tree.MemberExpression

class ExpressionParser : Parser<Expression>() {

    fun base() = choose(LiteralExpressionParser(), IdentifierExpressionParser())

    fun part(base: Expression) = parser {
        include(choose(memberExpressionPart(base), callExpressionPart(base)))
    }

    fun callExpressionPart(base: Expression) = parser {
        return@parser CallExpression(base, include(ListParser(ExpressionParser())))
    }

    fun memberExpressionPart(base: Expression) = parser {
        buffer.get().expect(TokenType.PUNCTUATION).expect(".")
        val name = buffer.get().expect(TokenType.IDENTIFIER)
        return@parser MemberExpression(base, Identifier(name.getRaw()))
    }

    override fun parse(): Expression {
        var base: Expression = include(base())
        while (true) {
            val result = include(part(base).optional())
            if (result.getOrNull() == null) break
            else base = result.getOrNull()!!
        }
        return base
    }

}
