package pixel.aurora.parser.expression

import pixel.aurora.parser.ExpressionParser
import pixel.aurora.parser.Parser
import pixel.aurora.parser.buffer
import pixel.aurora.parser.include
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.Expression

class IndependentExpressionParser : Parser<Expression>() {

    override fun parse(): Expression {
        buffer.get().expect("(").expect(TokenType.PUNCTUATION)
        val expression = include(ExpressionParser())
        buffer.get().expect(")").expect(TokenType.PUNCTUATION)
        return expression
    }

}