package pixel.aurora.compiler.parser.statement

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.tree.IfStatement

class IfStatementParser : Parser<IfStatement>() {

    override fun parse(): IfStatement {
        buffer.get().expectIdentifier("if")
        buffer.get().expectPunctuation('(')
        val test = include(ExpressionParser())
        buffer.get().expectPunctuation(')')
        val consequent = include(StatementParser())
        val alternate = include(
            parser {
                buffer.get().expectIdentifier("else")
                include(StatementParser())
            }.optional()
        )
        return IfStatement(test, consequent, alternate.getOrNull())
    }

}