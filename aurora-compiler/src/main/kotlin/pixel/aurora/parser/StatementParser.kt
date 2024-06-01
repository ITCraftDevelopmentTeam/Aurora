package pixel.aurora.parser

import pixel.aurora.parser.statement.EmptyStatementParser
import pixel.aurora.parser.statement.ExpressionStatementParser
import pixel.aurora.tree.Statement

class StatementParser : Parser<Statement>() {

    override fun parse(): Statement {
        return include(ExpressionStatementParser() or EmptyStatementParser)
    }

}
