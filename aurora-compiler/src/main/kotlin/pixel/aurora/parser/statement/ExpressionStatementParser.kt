package pixel.aurora.parser.statement

import pixel.aurora.parser.ExpressionParser
import pixel.aurora.parser.Parser
import pixel.aurora.parser.buffer
import pixel.aurora.parser.include
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.ExpressionStatement

class ExpressionStatementParser : Parser<ExpressionStatement>() {

    override fun parse(): ExpressionStatement {
        val expression = include(ExpressionParser())
        buffer.get().expect(";").expect(TokenType.PUNCTUATION)
        return ExpressionStatement(expression)
    }

}
