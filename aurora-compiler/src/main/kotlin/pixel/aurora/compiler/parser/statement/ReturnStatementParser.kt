package pixel.aurora.compiler.parser.statement

import pixel.aurora.compiler.parser.*
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.ReturnStatement

class ReturnStatementParser : Parser<ReturnStatement>() {

    override fun parse(): ReturnStatement {
        buffer.get().expect("return").expect(TokenType.IDENTIFIER)
        val expression = include(ExpressionParser().optional())
        buffer.get().expect(";").expect(TokenType.PUNCTUATION)
        return ReturnStatement(expression.getOrNull())
    }

}