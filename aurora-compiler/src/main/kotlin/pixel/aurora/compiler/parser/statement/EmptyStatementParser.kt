package pixel.aurora.compiler.parser.statement

import pixel.aurora.compiler.parser.Parser
import pixel.aurora.compiler.parser.buffer
import pixel.aurora.compiler.tokenizer.TokenType
import pixel.aurora.compiler.tree.EmptyStatement

object EmptyStatementParser : Parser<EmptyStatement>() {

    override fun parse() = EmptyStatement.also {
        buffer.get().expect(";").expect(TokenType.PUNCTUATION)
    }

}