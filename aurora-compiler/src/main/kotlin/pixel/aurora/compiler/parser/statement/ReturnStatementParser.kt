package pixel.aurora.compiler.parser.statement

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.tree.ReturnStatement

class ReturnStatementParser : Parser<ReturnStatement>() {

    override fun parse(): ReturnStatement {
        buffer.get().expectIdentifier("return")
        val expression = include(ExpressionParser().optional())
        buffer.get().expectPunctuation(';')
        return ReturnStatement(expression.getOrNull())
    }

}