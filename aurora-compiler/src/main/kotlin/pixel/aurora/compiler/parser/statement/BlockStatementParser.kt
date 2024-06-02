package pixel.aurora.compiler.parser.statement

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.StatementParser
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.parser.util.ListParser
import pixel.aurora.compiler.tree.BlockStatement

class BlockStatementParser : Parser<BlockStatement>() {

    override fun parse(): BlockStatement {
        return BlockStatement(include(ListParser(StatementParser(), "{", "}", null)))
    }

}