package pixel.aurora.parser

import pixel.aurora.parser.statement.EmptyStatementParser
import pixel.aurora.parser.statement.ExpressionStatementParser
import pixel.aurora.parser.statement.ReturnStatementParser
import pixel.aurora.tree.Statement

class StatementParser : Parser<Statement>() {

    override fun parse(): Statement {
        return include(ReturnStatementParser() or EmptyStatementParser or ExpressionStatementParser())
    }

}
