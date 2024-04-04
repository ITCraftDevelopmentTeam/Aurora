package pixel.aurora.parser

import pixel.aurora.tree.ExpressionStatement

class ExpressionStatementParser : Parser<ExpressionStatement>() {

    override fun parse(): ExpressionStatement {
        val expression = include(ExpressionParser())
        return ExpressionStatement(expression)
    }

}