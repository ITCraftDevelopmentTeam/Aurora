package pixel.aurora.compiler.parser

import pixel.aurora.compiler.parser.statement.*
import pixel.aurora.compiler.tree.Statement

class StatementParser : Parser<Statement>() {

    override fun parse(): Statement {
        return include(
            IfStatementParser() or BlockStatementParser() or ReturnStatementParser() or EmptyStatementParser() or ExpressionStatementParser() or InnerDeclarationStatementParser()
        )
    }

}
