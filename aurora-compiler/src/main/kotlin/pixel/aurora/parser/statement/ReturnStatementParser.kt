package pixel.aurora.parser.statement

import pixel.aurora.parser.*
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.ReturnStatement

class ReturnStatementParser : Parser<ReturnStatement>() {

    override fun parse(): ReturnStatement {
        buffer.get().expect("return").expect(TokenType.IDENTIFIER)
        val expression = include(ExpressionParser().optional())
        buffer.get().expect(";").expect(TokenType.PUNCTUATION)
        return ReturnStatement(expression.getOrNull())
    }

}