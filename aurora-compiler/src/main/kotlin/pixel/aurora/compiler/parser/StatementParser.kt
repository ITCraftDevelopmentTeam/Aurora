package pixel.aurora.compiler.parser

import pixel.aurora.compiler.parser.statement.EmptyStatementParser
import pixel.aurora.compiler.parser.statement.ExpressionStatementParser
import pixel.aurora.compiler.parser.statement.InnerDeclarationStatementParser
import pixel.aurora.compiler.parser.statement.ReturnStatementParser
import pixel.aurora.compiler.tree.Statement

class StatementParser : Parser<Statement>() {

    override fun parse(): Statement {
        return include(
            ReturnStatementParser() or EmptyStatementParser or ExpressionStatementParser() or InnerDeclarationStatementParser()
        )
    }

}
