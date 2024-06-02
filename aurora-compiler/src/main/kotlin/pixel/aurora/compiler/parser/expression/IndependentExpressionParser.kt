package pixel.aurora.compiler.parser.expression

import pixel.aurora.compiler.parser.ExpressionParser
import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.Expression

class IndependentExpressionParser : Parser<Expression>() {

    override fun parse(): Expression {
        buffer.get().expect("(").expect(TokenType.PUNCTUATION)
        val expression = include(ExpressionParser())
        buffer.get().expect(")").expect(TokenType.PUNCTUATION)
        return expression
    }

}