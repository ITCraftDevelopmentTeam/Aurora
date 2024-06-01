package pixel.aurora.parser

import pixel.aurora.parser.expression.LiteralExpressionParser
import pixel.aurora.tree.Expression

class ExpressionParser : Parser<Expression>() {

    override fun parse(): Expression {
        return include(choose(LiteralExpressionParser()))
    }

}