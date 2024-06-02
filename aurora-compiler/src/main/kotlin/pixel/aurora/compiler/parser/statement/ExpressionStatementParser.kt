package pixel.aurora.compiler.parser.statement

import pixel.aurora.compiler.parser.ExpressionParser
import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.parser.include
import pixel.aurora.compiler.tree.ExpressionStatement

class ExpressionStatementParser : Parser<ExpressionStatement>() {

    override fun parse(): ExpressionStatement {
        val expression = include(ExpressionParser())
        buffer.get().expectPunctuation(';')
        return ExpressionStatement(expression)
    }

}
