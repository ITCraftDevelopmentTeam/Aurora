package pixel.aurora.parser

import pixel.aurora.parser.statement.*
import pixel.aurora.tree.Statement

class StatementParser : Parser<Statement>() {

    override fun parse(): Statement {
        return include(ExpressionStatementParser() or ReturnStatementParser() or EmptyStatementParser)
    }

}
