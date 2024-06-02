package pixel.aurora.compiler.parser.expression

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.tree.UnaryExpression

class UnaryExpressionParser : Parser<UnaryExpression>() {

    fun operator(operator: String) = parser {
        for (character in operator) {
            buffer.get().expectPunctuation(character)
        }
        val expression = include(ExpressionParser())
        return@parser UnaryExpression(expression, operator)
    }

    override fun parse(): UnaryExpression {
        return include(
            operator("++") or operator("--") or operator("+") or operator("-") or operator("!")
        )
    }

}