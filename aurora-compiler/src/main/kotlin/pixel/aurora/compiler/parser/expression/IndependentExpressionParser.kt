package pixel.aurora.compiler.parser.expression

import pixel.aurora.compiler.parser.ExpressionParser
import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.tree.Expression

class IndependentExpressionParser : Parser<Expression>() {

    override fun parse(): Expression {
        buffer.get().expectPunctuation('(')
        val expression = include(ExpressionParser())
        buffer.get().expectPunctuation(')')
        return expression
    }

}