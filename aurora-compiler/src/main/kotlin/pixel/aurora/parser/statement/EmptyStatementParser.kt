package pixel.aurora.parser.statement

import pixel.aurora.parser.Parser
import pixel.aurora.parser.buffer
import pixel.aurora.tokenizer.TokenType
import pixel.aurora.tree.EmptyStatement

object EmptyStatementParser : Parser<EmptyStatement>() {

    override fun parse() = EmptyStatement.also {
        buffer.get().expect(";").expect(TokenType.PUNCTUATION)
    }

}